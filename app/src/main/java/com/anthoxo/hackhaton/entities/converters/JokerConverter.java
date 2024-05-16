package com.anthoxo.hackhaton.entities.converters;

import com.anthoxo.hackhaton.models.Joker;
import jakarta.persistence.AttributeConverter;

import java.util.List;
import java.util.stream.Collectors;
import java.util.stream.Stream;

public class JokerConverter implements AttributeConverter<List<Joker>, String> {

    @Override
    public String convertToDatabaseColumn(List<Joker> jokers) {
        return jokers.stream()
                .map(Joker::name)
                .collect(Collectors.joining(","));
    }

    @Override
    public List<Joker> convertToEntityAttribute(String s) {
        return Stream.of(s.split(","))
            .map(Joker::getJoker)
            .toList();
    }
}
