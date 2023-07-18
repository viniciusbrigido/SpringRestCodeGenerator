package com.brigido.springrestcodegenerator.enumeration;

import static java.lang.String.format;
import static java.util.Arrays.stream;

public enum GenerationType {
    TABLE, SEQUENCE, IDENTITY, UUID, AUTO;

    public static String getAnottation(String generationTypeString) {
        return stream(GenerationType.values())
                .filter(generationType -> generationType.name().equalsIgnoreCase(generationTypeString))
                .findFirst()
                .map(generationType -> format("\t@GeneratedValue(strategy = GenerationType.%s)\n", generationType.name()))
                .orElse("");
    }
}
