package cc.gasches.testassignment.service;

import java.io.IOException;
import java.io.InputStream;
import java.sql.Blob;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

import javax.persistence.EntityManager;
import javax.persistence.LockModeType;

import org.apache.commons.csv.CSVParser;
import org.apache.commons.csv.CSVRecord;
import org.hibernate.LockOptions;
import org.hibernate.engine.jdbc.BlobProxy;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.multipart.MultipartFile;

import cc.gasches.testassignment.PersistenceUtils;
import cc.gasches.testassignment.model.GeologicalClass;
import cc.gasches.testassignment.model.ImportItem;
import cc.gasches.testassignment.model.Section;
import cc.gasches.testassignment.repository.ImportItemRepository;
import cc.gasches.testassignment.repository.SectionRepository;
import lombok.RequiredArgsConstructor;

@Service
@RequiredArgsConstructor
public class ImportService {
    private static final Logger log = LoggerFactory.getLogger(ImportService.class);

    private final ImportItemRepository importRepository;
    private final EntityManager entityManager;
    private final SectionRepository sectionRepository;
    private final CsvService csvService;

    @Transactional
    public Long uploadAsync(MultipartFile file) throws IOException {
        Blob fileBlob = BlobProxy.generateProxy(file.getInputStream(), file.getSize());
        ImportItem importItem = new ImportItem();
        importItem.setFile(fileBlob);
        importItem.setStatus(ImportItem.Status.IN_PROGRESS);
        ImportItem savedItem = importRepository.save(importItem);
        return savedItem.getId();
    }

    @Transactional
    @Scheduled(fixedRateString = "${app.import-period}")
    public void processImport() {
        entityManager
                .createQuery("select it from ImportItem it where status = : status order by it.id", ImportItem.class)
                .setParameter("status", ImportItem.Status.IN_PROGRESS)
                .setLockMode(LockModeType.PESSIMISTIC_WRITE)
                .setHint(PersistenceUtils.LOCK_TIMEOUT_HINT, LockOptions.SKIP_LOCKED)
                .getResultStream()
                .forEach(this::processItem);
    }

    private void processItem(ImportItem importItem) {
        log.info("Import data from #{}", importItem.getId());
        try (InputStream is = importItem.getFile().getBinaryStream(); CSVParser parser = csvService.parse(is)) {
            List<Section> sections = new ArrayList<>();
            for (CSVRecord record : parser) {
                int colSize = record.size();
                if (colSize < 0) {
                    continue;
                }
                Section section = new Section();
                section.setName(record.get(0));
                for (int i = 2; i < colSize; i += 2) {
                    section.addGeologicalClass(new GeologicalClass(record.get(i - 1), record.get(i)));
                }
                sections.add(section);
                if (record.getRecordNumber() % 100L == 0) {
                    sectionRepository.saveAll(sections);
                    sections.clear();
                }
            }
            if (!sections.isEmpty()) {
                sectionRepository.saveAll(sections);
            }
            importItem.setStatus(ImportItem.Status.DONE);
        } catch (Exception e) {
            log.error("Import of #" + importItem.getId() + " is failed", e);
            importItem.setStatus(ImportItem.Status.ERROR);
            importItem.setMessage(e.getMessage());
        }
        entityManager.merge(importItem);
        log.info("Import of #{} is finished", importItem.getId());
    }

    @Transactional(readOnly = true)
    public Optional<ImportItem.Status> getJobStatus(long id) {
        return importRepository.findById(id).map(ImportItem::getStatus);
    }
}
