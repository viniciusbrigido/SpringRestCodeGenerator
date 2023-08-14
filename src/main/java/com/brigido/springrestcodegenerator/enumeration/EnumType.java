package com.brigido.springrestcodegenerator.enumeration;

import static java.util.Arrays.stream;

public enum EnumType {

    ORDINAL, STRING;

    public static String getEnumType(String enumTypeString) {
        return stream(values())
                .filter(enumType -> enumType.name().equalsIgnoreCase(enumTypeString))
                .findFirst()
                .map(enumType -> "\t@Enumerated(EnumType.%s)\n".formatted(enumType.name()))
                .orElse("");
    }
}
