package com.brigido.springrestcodegenerator.generator;

import com.brigido.springrestcodegenerator.dto.*;
import com.brigido.springrestcodegenerator.enumeration.*;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import static com.brigido.springrestcodegenerator.enumeration.Imports.*;
import static java.lang.String.format;
import static java.lang.String.join;
import static java.util.Objects.*;
import static java.util.stream.Collectors.*;

public class EntityGenerator extends BaseGenerator {

    private String directory;

    public void create(PropertyDTO propertyDTO, TableDTO tableDTO, String directory, List<String> enums) throws IOException {
        setPropertyDTO(propertyDTO);
        this.directory = directory;

        String fileName = tableDTO.getTable() + ".java";
        createFile(getEntityDirectory(directory), fileName, getEntityCode(tableDTO, enums));
    }

    private String getEntityCode(TableDTO tableDTO, List<String> enums) {
        StringBuilder code = new StringBuilder();
        String className = format("public class %s %s{\n\n", tableDTO.getTable(), getSerializableImplements());

        code.append(getPackageName(getEntityDirectory(directory)))
            .append(getImports(tableDTO, enums))
            .append(getHeader(tableDTO))
            .append(className)
            .append(getColumns(tableDTO))
            .append(getConstructors(tableDTO, ""))
            .append(getGettersSetters(tableDTO))
            .append(getUpdateMethod(tableDTO))
            .append("}");
        return code.toString();
    }

    private String getImports(TableDTO tableDTO, List<String> enums) {
        StringBuilder imports = new StringBuilder();
        imports.append(PERSISTENCE.getFormattedImport())
               .append(getSerializableImport())
               .append(getLombokImport())
               .append(getImportsByConfigEntityDTO(tableDTO.getColumns()));

        if (tableDTO.hasEnum(enums)) {
            imports.append("import ").append(convertDirectoryToPackage(getEnumerationDirectory(directory)))
                   .append(".*;\n");
        }

        if (tableDTO.hasUpdate()) {
            imports.append("import ").append(convertDirectoryToPackage(getDTODirectory(directory)))
                   .append(".").append(tableDTO.getTable()).append("UpdateDTO;\n")
                   .append(OPTIONAL.getFormattedImport());
        }

        imports.append("\n");
        return imports.toString();
    }

    private String getHeader(TableDTO tableDTO) {
        StringBuilder header = new StringBuilder();
        header.append(getLombokHeader())
              .append("@Entity\n")
              .append("@Table(name = \"")
              .append(parseCamelCaseToSnakeCase(tableDTO.getTable()))
              .append("\")\n");

        return header.toString();
    }

    private String getColumns(TableDTO tableDTO) {
        StringBuilder columns = new StringBuilder();
        for (ColumnDTO columnDTO : tableDTO.getColumns()) {
            columns.append(getFieldCode(columnDTO)).append("\n");
        }
        return columns.toString();
    }

    private String getFieldCode(ColumnDTO columnDTO) {
        StringBuilder fieldCode = new StringBuilder();

        if (columnDTO.isPrimaryKey()) {
            fieldCode.append("\t@Id\n")
                     .append(getGeneratedValue(columnDTO));
        }

        String typeFormatted = columnDTO.isList() ? format("List<%s>", columnDTO.getType()) : columnDTO.getType();
        String propertyName = format("\tprivate %s %s;\n", typeFormatted, columnDTO.getName());

        fieldCode.append(getCardinality(columnDTO))
                 .append(getColumnName(columnDTO))
                 .append(propertyName);

        return fieldCode.toString();
    }

    private String getColumnName(ColumnDTO columnDTO) {
        List<String> configsField = new ArrayList<>();
        String nameSnakeCase = parseCamelCaseToSnakeCase(columnDTO.getName());

        if (columnDTO.isRequired()) {
            configsField.add("nullable = false");
        }
        if (columnDTO.getType().equals("String") && nonNull(columnDTO.getLength())) {
            configsField.add(format("length = %s", columnDTO.getLength()));
        }
        if (!nameSnakeCase.equals(columnDTO.getName())) {
            configsField.add(format("name = \"%s\"", nameSnakeCase));
        }

        if (configsField.isEmpty()) {
            return "";
        }
        return format("\t@Column(%s)\n", join(", ", configsField));
    }

    private String getCardinality(ColumnDTO columnDTO) {
        if (isNull(columnDTO.getCardinality()) || columnDTO.getCardinality().isEmpty()) {
            return "";
        }

        return Cardinality.getAnottation(columnDTO.getCardinality());
    }

    private String getGeneratedValue(ColumnDTO columnDTO) {
        if (isNull(columnDTO.getGenerationType()) || columnDTO.getGenerationType().isEmpty()) {
            return "\t@GeneratedValue\n";
        }

        return GenerationType.getAnottation(columnDTO.getGenerationType());
    }

    private String getUpdateMethod(TableDTO tableDTO) {
        if (!tableDTO.hasUpdate()) {
            return "";
        }

        List<ColumnDTO> columns = tableDTO.getColumns()
                .stream().filter(ColumnDTO::isUpdatable).collect(toList());

        StringBuilder updateMethod = new StringBuilder();
        String objectNameLowerCase = lowerCaseFirstLetter(tableDTO.getTable());
        String methodName = format("\tpublic void update(%sUpdateDTO %sUpdateDTO) {\n", tableDTO.getTable(), objectNameLowerCase);

        updateMethod.append(methodName);

        for (ColumnDTO columnDTO : columns) {
            updateMethod.append("\t\t")
                        .append(columnDTO.getName())
                        .append(" = ofNullable(")
                        .append(parseCamelCaseToSnakeCase(columnDTO.getName()))
                        .append("UpdateDTO.get")
                        .append(capitalizeFirstLetter(columnDTO.getName()))
                        .append("()).orElse(").append(columnDTO.getName()).append(");\n");
        }

        updateMethod.append("\t}\n\n");
        return updateMethod.toString();
    }
}
