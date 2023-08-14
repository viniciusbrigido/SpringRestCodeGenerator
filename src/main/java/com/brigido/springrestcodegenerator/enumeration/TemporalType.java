package com.brigido.springrestcodegenerator.enumeration;

import static java.util.Arrays.stream;

public enum TemporalType {

    DATE, TIME, TIMESTAMP;

    public static String getTemporalType(String temporalTypeString) {
        return stream(values())
                .filter(temporalType -> temporalType.name().equalsIgnoreCase(temporalTypeString))
                .findFirst()
                .map(temporalType -> "\t@Temporal(TemporalType.%s)\n".formatted(temporalType.name()))
                .orElse("");
    }
}
