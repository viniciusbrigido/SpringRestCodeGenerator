package com.brigido.springrestcodegenerator.enumeration;

public enum Imports {

    LOMBOK(null, "import lombok.*;\n"),
    SPRING_ANNOTATION(null, "import org.springframework.web.bind.annotation.*;\n"),
    AUTOWIRED(null, "import org.springframework.beans.factory.annotation.Autowired;\n"),
    RESPONSE_ENTITY(null, "import org.springframework.http.ResponseEntity;\n"),
    LIST("List", "import java.util.List;\n"),
    PERSISTENCE(null, "import jakarta.persistence.*;\n"),
    OPTIONAL(null, "import static java.util.Optional.*;\n"),
    JPA_REPOSITORY(null, "import org.springframework.data.jpa.repository.JpaRepository;\n"),
    REPOSITORY(null, "import org.springframework.stereotype.Repository;\n"),
    SERVICE(null, "import org.springframework.stereotype.Service;\n"),
    COLLECTORS(null, "import java.util.stream.Collectors;\n"),
    MODEL_MAPPER(null, "import org.modelmapper.ModelMapper;\n"),
    DATE("Date", "import java.util.Date;\n"),
    UUID("UUID", "import java.util.UUID;\n"),
    BIG_INTEGER("BigInteger", "import java.math.BigInteger;\n"),
    BIG_DECIMAL("BigDecimal", "import java.math.BigDecimal;\n"),
    LOCAL_DATE("LocalDate", "import java.time.LocalDate;\n"),
    LOCAL_DATE_TIME("LocalDateTime", "import java.time.LocalDateTime;\n"),
    SERIALIZABLE(null, "import java.io.Serializable;\n"),
    NOT_NULL(null, "import jakarta.validation.constraints.NotNull;\n");

    private final String name;
    private final String formattedImport;

    Imports(String name, String formattedImport) {
        this.name = name;
        this.formattedImport = formattedImport;
    }

    public String getName() {
        return name;
    }

    public String getFormattedImport() {
        return formattedImport;
    }
}
