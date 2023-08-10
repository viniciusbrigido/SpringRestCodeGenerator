package com.brigido.springrestcodegenerator.enumeration;

import static java.util.Arrays.stream;

public enum CascadeType {

    ALL, PERSIST, MERGE, REMOVE, REFRESH, DETACH;

    public static String getCascadeType(String cascadeTypeString) {
        return stream(values())
                .filter(cascadeType -> cascadeType.name().equalsIgnoreCase(cascadeTypeString))
                .findFirst()
                .map(cascadeType -> "cascade = CascadeType.%s".formatted(cascadeType.name()))
                .orElse("");
    }
}
