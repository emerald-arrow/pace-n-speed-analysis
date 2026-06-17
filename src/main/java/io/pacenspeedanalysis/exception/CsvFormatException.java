package io.pacenspeedanalysis.exception;

import com.opencsv.exceptions.CsvValidationException;

public class CsvFormatException extends RuntimeException {
    public CsvFormatException(String message, CsvValidationException e) {
        super(message, e);
    }
}
