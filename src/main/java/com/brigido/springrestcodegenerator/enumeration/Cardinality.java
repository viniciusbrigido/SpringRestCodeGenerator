package com.brigido.springrestcodegenerator.enumeration;

import static java.util.Arrays.*;

public enum Cardinality {

    ONE_TO_MANY("OneToMany"),
    MANY_TO_ONE("ManyToOne"),
    ONE_TO_ONE("OneToOne"),
    MANY_TO_MANY("ManyToMany");

    private final String name;

    Cardinality(String name) {
        this.name = name;
    }

    public String getName() {
        return name;
    }

    public static String getAnottation(String cardinalityString) {
        return stream(Cardinality.values())
                .filter(cardinality -> cardinality.getName().equalsIgnoreCase(cardinalityString))
                .findFirst()
                .map(cardinality -> "\t@%s\n".formatted(cardinality.getName()))
                .orElse("");
    }
}
