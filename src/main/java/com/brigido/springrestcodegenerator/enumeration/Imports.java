package com.brigido.springrestcodegenerator.enumeration;

public enum Imports {

    LOMBOK(null, "lombok.*"),
    SPRING_ANNOTATION(null, "org.springframework.web.bind.annotation.*"),
    AUTOWIRED(null, "org.springframework.beans.factory.annotation.Autowired"),
    RESPONSE_ENTITY(null, "org.springframework.http.ResponseEntity"),
    LIST("List", "java.util.List"),
    PERSISTENCE(null, "jakarta.persistence.*"),
    OPTIONAL(null, "static java.util.Optional.*"),
    JPA_REPOSITORY(null, "org.springframework.data.jpa.repository.JpaRepository"),
    REPOSITORY(null, "org.springframework.stereotype.Repository"),
    SERVICE(null, "org.springframework.stereotype.Service"),
    COLLECTORS(null, "java.util.stream.Collectors"),
    MODEL_MAPPER(null, "org.modelmapper.ModelMapper"),
    DATE("Date", "java.util.Date"),
    UUID("UUID", "java.util.UUID"),
    BIG_INTEGER("BigInteger", "java.math.BigInteger"),
    BIG_DECIMAL("BigDecimal", "java.math.BigDecimal"),
    LOCAL_DATE("LocalDate", "java.time.LocalDate"),
    LOCAL_DATE_TIME("LocalDateTime", "java.time.LocalDateTime"),
    SERIALIZABLE(null, "java.io.Serializable"),
    NOT_NULL(null, "jakarta.validation.constraints.NotNull");

    private final String name;
    private final String importName;

    Imports(String name, String importName) {
        this.name = name;
        this.importName = importName;
    }

    public String getName() {
        return name;
    }

    public String getFormattedImport() {
        return "import %s;\n".formatted(importName);
    }
}
