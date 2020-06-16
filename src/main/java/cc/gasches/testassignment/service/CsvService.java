package cc.gasches.testassignment.service;

import java.io.IOException;
import java.io.InputStream;
import java.nio.charset.StandardCharsets;

import javax.annotation.Nonnull;

import org.apache.commons.csv.CSVFormat;
import org.apache.commons.csv.CSVParser;
import org.apache.commons.csv.CSVPrinter;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;

@Service
public class CsvService {
    private static final Logger log = LoggerFactory.getLogger(CsvService.class);

    private final CSVFormat writeFormat;
    private final CSVFormat readFormat;

    public CsvService(@Value("${app.csv-delimiter}") char csvDelimiter) {
        this.writeFormat = CSVFormat.DEFAULT.withDelimiter(csvDelimiter).withTrim();
        this.readFormat = CSVFormat.DEFAULT.withDelimiter(csvDelimiter).withFirstRecordAsHeader().withSkipHeaderRecord()
                .withTrim();
    }

    @Nonnull
    public CSVPrinter newCsvPrinter(@Nonnull Appendable out) throws IOException {
        return new CSVPrinter(out, writeFormat);
    }

    @Nonnull
    public CSVParser parse(@Nonnull InputStream inputStream) throws IOException {
        return CSVParser.parse(inputStream, StandardCharsets.UTF_8, readFormat);
    }
}
