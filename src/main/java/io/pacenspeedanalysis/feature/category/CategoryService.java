package io.pacenspeedanalysis.feature.category;

import io.pacenspeedanalysis.model.data.DataRecord;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

@Service
public class CategoryService {

    public List<String> getCategories(List<DataRecord> data) {
        final Set<String> categories = new HashSet<>();

        for (DataRecord record : data) {
            categories.add(record.category());
        }

        return categories.stream()
                            .sorted(String::compareTo)
                            .toList();
    }

    public List<DataRecord> filterOutCategories(List<DataRecord> records, String chosenCategory) {
        final List<DataRecord> filteredRecords = new ArrayList<>();

        for (DataRecord record : records) {
            if (record.category().equalsIgnoreCase(chosenCategory)) {
                filteredRecords.add(record);
            }
        }

        return filteredRecords;
    }
}
