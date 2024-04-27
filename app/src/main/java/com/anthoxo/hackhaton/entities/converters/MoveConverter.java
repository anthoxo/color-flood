package com.anthoxo.hackhaton.entities.converters;

import jakarta.persistence.AttributeConverter;

import java.util.List;
import java.util.stream.Collectors;
import java.util.stream.Stream;

public class MoveConverter implements AttributeConverter<List<String>, String> {

    @Override
    public String convertToDatabaseColumn(List<String> strings) {
        return strings.stream()
                .collect(Collectors.joining(","));
    }

    @Override
    public List<String> convertToEntityAttribute(String s) {
        return Stream.of(s.split(",")).toList();
    }
}
