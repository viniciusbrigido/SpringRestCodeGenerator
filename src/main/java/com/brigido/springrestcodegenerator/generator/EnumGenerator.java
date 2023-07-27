package com.brigido.springrestcodegenerator.generator;

import com.brigido.springrestcodegenerator.dto.EnumDTO;
import com.brigido.springrestcodegenerator.dto.PropertyDTO;
import java.io.IOException;
import java.util.List;
import static java.util.stream.Collectors.*;

public class EnumGenerator extends BaseGenerator {

    private String directory;

    public void create(PropertyDTO propertyDTO, EnumDTO enumDTO, String directory) throws IOException {
        setPropertyDTO(propertyDTO);
        this.directory = directory;

        String fileName = enumDTO.getName() + ".java";
        createFile(getEnumerationDirectory(directory), fileName, getEnumCode(enumDTO));
    }

    private String getEnumCode(EnumDTO enumDTO) {
        StringBuilder code = new StringBuilder();
        String enumName = "public enum %s {\n".formatted(enumDTO.getName());

        code.append(getPackageName(getEnumerationDirectory(directory)))
            .append(enumName)
            .append("\n")
            .append(getEnumValues(enumDTO.getValues()))
            .append("\n}");
        return code.toString();
    }

    private String getEnumValues(List<String> values) {
        return "\t" + values.stream()
                .map(line -> line.equals(line.toUpperCase()) ? line : parseCamelCaseToSnakeCase(line.trim()).toUpperCase())
                .collect(joining(", "));
    }

}
