package cc.gasches.geodata.service;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileWriter;
import java.io.IOException;
import java.io.InputStream;
import java.io.UncheckedIOException;
import java.nio.charset.StandardCharsets;
import java.sql.Blob;
import java.sql.SQLException;
import java.util.Optional;

import javax.persistence.EntityManager;
import javax.persistence.LockModeType;

import org.apache.commons.csv.CSVPrinter;
import org.hibernate.LockOptions;
import org.hibernate.engine.jdbc.BlobProxy;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Propagation;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.servlet.mvc.method.annotation.StreamingResponseBody;

import cc.gasches.geodata.PersistenceUtils;
import cc.gasches.geodata.model.ExportItem;
import cc.gasches.geodata.model.GeologicalClass;
import cc.gasches.geodata.model.Section;
import cc.gasches.geodata.repository.ExportItemRepository;
import cc.gasches.geodata.repository.SectionRepository;
import lombok.RequiredArgsConstructor;

@Service
@RequiredArgsConstructor
public class ExportService {
    private static final Logger log = LoggerFactory.getLogger(ExportService.class);

    private final ExportItemRepository exportRepository;
    private final EntityManager entityManager;
    private final SectionRepository sectionRepository;
    private final CsvService csvService;

    @Transactional
    public long scheduleDataExport() {
        ExportItem exportItem = new ExportItem();
        exportItem.setStatus(ExportItem.Status.IN_PROGRESS);
        return exportRepository.save(exportItem).getId();
    }

    @Transactional(readOnly = true)
    public Optional<ExportItem.Status> getJobStatus(long id) {
        return exportRepository.findById(id).map(ExportItem::getStatus);
    }

    @Transactional(readOnly = true, propagation = Propagation.MANDATORY)
    public Optional<StreamingResponseBody> getFile(long id) {
        Optional<ExportItem> itemOpt = exportRepository.findById(id);
        if (itemOpt.isEmpty()) {
            return Optional.empty();
        }
        ExportItem exportItem = itemOpt.get();
        Blob file = exportItem.getFile();
        if (file == null) {
            return Optional.empty();
        }
        StreamingResponseBody csv = outputStream -> {
            try (InputStream is = file.getBinaryStream()) {
                is.transferTo(outputStream);
            } catch (SQLException e) {
                throw new IOException(e);
            }
        };
        return Optional.of(csv);
    }

    @Transactional
    @Scheduled(fixedRateString = "${app.export-period}")
    public void processExport() throws IOException {
        if (exportRepository.countByStatus(ExportItem.Status.IN_PROGRESS) == 0L) {
            return;
        }
        File exportFile = buildExportCsv();
        entityManager
                .createQuery("select et from ExportItem et where et.status = :status order by et.id", ExportItem.class)
                .setParameter("status", ExportItem.Status.IN_PROGRESS)
                .setLockMode(LockModeType.PESSIMISTIC_WRITE)
                .setHint(PersistenceUtils.LOCK_TIMEOUT_HINT, LockOptions.SKIP_LOCKED)
                .getResultStream()
                .forEach(item -> {
                    log.info("Process export request #{}", item.getId());
                    try (FileInputStream fis = new FileInputStream(exportFile)) {
                        item.setFile(BlobProxy.generateProxy(fis, exportFile.length()));
                        item.setStatus(ExportItem.Status.DONE);
                        entityManager.merge(item);
                        entityManager.flush();
                        log.info("Export request #{} successfully processed", item.getId());
                    } catch (Exception e) {
                        log.error("Export failed", e);
                        item.setStatus(ExportItem.Status.ERROR);
                        item.setMessage(e.getMessage());
                        entityManager.merge(item);
                        entityManager.flush();
                        if (e instanceof IOException) {
                            throw new UncheckedIOException((IOException) e);
                        } else {
                            throw new RuntimeException(e);
                        }
                    }
                });
    }

    private File buildExportCsv() throws IOException {
        log.info("Export sections to tmp file");
        File geo = File.createTempFile("geo", ".tmp");
        geo.deleteOnExit();
        try (FileWriter writer = new FileWriter(geo, StandardCharsets.UTF_8);
                CSVPrinter printer = csvService.newCsvPrinter(writer)) {
            long n = 0L;
            for (Section section : sectionRepository.findAll()) {
                if (n++ % 100 == 0) {
                    printer.flush();
                }
                printer.print(section.getName());
                if (section.getGeologicalClasses() != null) {
                    for (GeologicalClass geoClass : section.getGeologicalClasses()) {
                        printer.print(geoClass.getName());
                        printer.print(geoClass.getCode());
                    }
                }
                printer.println();
            }
        }
        return geo;
    }
}
