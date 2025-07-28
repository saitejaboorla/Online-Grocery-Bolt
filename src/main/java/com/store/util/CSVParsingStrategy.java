package com.store.util;

import com.store.model.Product;
import org.apache.commons.csv.CSVRecord;

public interface CSVParsingStrategy {
    Product parseRecord(CSVRecord record) throws FileProcessingException;
    String[] getExpectedHeaders();
}