package com.brigido.springrestcodegenerator.enumeration;

import java.util.List;
import static java.lang.String.join;
import static java.util.Arrays.*;
import static java.util.Objects.*;

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

    public static String getAnottation(String cardinalityString, List<String> configsCardinality) {
        return stream(values())
                .filter(cardinality -> cardinality.getName().equalsIgnoreCase(cardinalityString))
                .findFirst()
                .map(cardinality -> "\t@%s%s\n"
                        .formatted(cardinality.getName(), isNull(configsCardinality) || configsCardinality.isEmpty() ? "" :  "(%s)".formatted(join(", ", configsCardinality))))
                .orElse("");
    }
}
