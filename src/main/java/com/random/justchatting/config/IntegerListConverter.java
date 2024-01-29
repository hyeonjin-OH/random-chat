package com.random.justchatting.config;

import jakarta.persistence.AttributeConverter;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.stream.Collectors;

public class IntegerListConverter  implements AttributeConverter<List<Integer>, String> {

    private static final String SPLIT_CHAR = ",";

    @Override
    public String convertToDatabaseColumn(List<Integer> dataList){
        return dataList.stream().map(String::valueOf).collect(Collectors.joining(SPLIT_CHAR));

    }

    @Override
    public List<Integer> convertToEntityAttribute(String dbData) {
        if (dbData.isEmpty()) {
            return new ArrayList<>();
        }
        else{
            return Arrays.stream(dbData.split(SPLIT_CHAR))
                    .map(Integer::parseInt)
                    .collect(Collectors.toList());
        }
    }

}
