package io.pacenspeedanalysis.util.file.csv;

import com.opencsv.CSVParser;
import com.opencsv.CSVParserBuilder;
import com.opencsv.CSVReader;
import com.opencsv.CSVReaderBuilder;
import io.pacenspeedanalysis.util.io.BOMInputStreamReader;

import java.io.IOException;
import java.io.InputStream;
import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;
import java.util.Arrays;
import java.util.List;
import java.util.UUID;

abstract public class CsvReader<T> {
    protected final CSVParser parser;

    public CsvReader() {
        this.parser = new CSVParserBuilder().withSeparator(';').build();
    }

    abstract public List<T> read(final InputStream stream) throws IOException;

    protected CSVReader getReader(InputStream stream) throws IOException {
        return new CSVReaderBuilder(new BOMInputStreamReader().newReader(stream))
                                                                .withCSVParser(this.parser)
                                                                .build();
    }

    protected boolean hasColumns(List<String> header, String... requiredColumns) {
        return Arrays.stream(requiredColumns).allMatch(header::contains);
    }

    protected UUID getLineUUID(String[] line) {
        final String raw = String.join(";", line);
        final MessageDigest digest;
        try {
            digest = MessageDigest.getInstance("SHA-256");
            final byte[] hash = digest.digest(raw.getBytes());

            return UUID.nameUUIDFromBytes(hash);
        } catch (NoSuchAlgorithmException e) {
            throw new RuntimeException(e);
        }
    }
}
