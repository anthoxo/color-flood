package com.anthoxo.hackhaton.entities.converters;

import jakarta.persistence.AttributeConverter;

import java.util.List;
import java.util.stream.Collectors;
import java.util.stream.Stream;

public class GridConverter implements
        AttributeConverter<List<List<Integer>>, String> {

    @Override
    public String convertToDatabaseColumn(List<List<Integer>> lists) {
        return lists.stream()
                .map(list -> list
                        .stream()
                        .map(String::valueOf)
                        .collect(Collectors.joining(",")))
                .collect(Collectors.joining(";"));
    }

    @Override
    public List<List<Integer>> convertToEntityAttribute(String s) {
        return Stream.of(s.split(";"))
                .map(str -> Stream.of(str.split(","))
                        .map(Integer::parseInt)
                        .toList()
                )
                .toList();
    }
}
