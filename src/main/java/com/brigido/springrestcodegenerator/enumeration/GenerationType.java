package com.brigido.springrestcodegenerator.enumeration;

import static java.util.Arrays.stream;

public enum GenerationType {

    TABLE, SEQUENCE, IDENTITY, UUID, AUTO;

    public static String getAnottation(String generationTypeString) {
        return stream(GenerationType.values())
                .filter(generationType -> generationType.name().equalsIgnoreCase(generationTypeString))
                .findFirst()
                .map(generationType -> "\t@GeneratedValue(strategy = GenerationType.%s)\n".formatted(generationType.name()))
                .orElse("");
    }
}
