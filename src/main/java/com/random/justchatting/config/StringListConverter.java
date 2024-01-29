package com.random.justchatting.config;


import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import jakarta.persistence.AttributeConverter;
import jakarta.persistence.Converter;

import java.util.List;

@Converter
public class StringListConverter implements AttributeConverter<List, String> {

    private final ObjectMapper mapper = new ObjectMapper();


    @Override
    public List convertToEntityAttribute(String data){
        try{
            return mapper.readValue(data, List.class);
        }catch (JsonProcessingException e){
            throw new RuntimeException(e);
        }
    }

    @Override
    public String convertToDatabaseColumn(List dataList){
        try{
            return mapper.writeValueAsString(dataList);
        }catch (JsonProcessingException e){
            throw new RuntimeException(e);
        }
    }
}
