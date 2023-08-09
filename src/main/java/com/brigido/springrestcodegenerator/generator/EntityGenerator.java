package com.brigido.springrestcodegenerator.generator;

import com.brigido.springrestcodegenerator.dto.*;
import com.brigido.springrestcodegenerator.enumeration.*;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import static com.brigido.springrestcodegenerator.util.StringUtil.*;
import static com.brigido.springrestcodegenerator.enumeration.Imports.*;
import static java.lang.String.join;
import static java.util.Objects.*;

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
        String className = "public class %s %s{\n\n".formatted(tableDTO.getTable(), getSerializableImplements());

        code.append(getPackageName(getEntityDirectory(directory)))
            .append(getImports(tableDTO, enums))
            .append(getHeader(tableDTO))
            .append(className)
            .append(getColumns(tableDTO, enums))
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
              .append("@Table(name = \"%s\")\n".formatted(parseCamelCaseToSnakeCase(tableDTO.getTable())));

        return header.toString();
    }

    private String getColumns(TableDTO tableDTO, List<String> enums) {
        StringBuilder columns = new StringBuilder();
        for (ColumnDTO columnDTO : tableDTO.getColumns()) {
            columns.append(getFieldCode(columnDTO, enums));
        }
        return columns.toString();
    }

    private String getFieldCode(ColumnDTO columnDTO, List<String> enums) {
        StringBuilder fieldCode = new StringBuilder();

        if (columnDTO.isPrimaryKey()) {
            fieldCode.append("\t@Id\n")
                     .append(getGeneratedValue(columnDTO));
        }

        if (columnDTO.hasEnumType()) {
            fieldCode.append("\t@Enumerated(EnumType.%s)\n".formatted(columnDTO.getEnumType().toUpperCase()));
        } else if (enums.contains(columnDTO.getType())) {
            fieldCode.append("\t@Enumerated(EnumType.STRING)\n");
        }

        String typeFormatted = columnDTO.isList() ? "List<%s>".formatted(columnDTO.getType()) : columnDTO.getType();
        String propertyName = "\tprivate %s %s;\n".formatted(typeFormatted, columnDTO.getName());

        fieldCode.append(getCardinality(columnDTO))
                 .append(getColumnName(columnDTO))
                 .append(propertyName)
                 .append("\n");

        return fieldCode.toString();
    }

    private String getColumnName(ColumnDTO columnDTO) {
        List<String> configsField = new ArrayList<>();
        String nameSnakeCase = parseCamelCaseToSnakeCase(columnDTO.getName());

        if (columnDTO.hasCardinality() && !columnDTO.isList()) {
            configsField.add("name = \"%s_id\"".formatted(nameSnakeCase));
        } else if (!columnDTO.hasCardinality() && !nameSnakeCase.equals(columnDTO.getName())) {
             configsField.add("name = \"%s\"".formatted(nameSnakeCase));
        }

        if (columnDTO.isRequired()) {
            configsField.add("nullable = false");
        }
        if (nonNull(columnDTO.getLength())) {
            configsField.add("length = %s".formatted(columnDTO.getLength()));
        }
        if (columnDTO.isUnique()) {
            configsField.add("unique = true");
        }

        if (configsField.isEmpty()) {
            return "";
        }

        String joinColumn = columnDTO.hasCardinality() ? "Join" : "";
        return "\t@%sColumn(%s)\n".formatted(joinColumn, join(", ", configsField));
    }

    private String getCardinality(ColumnDTO columnDTO) {
        if (!columnDTO.hasCardinality()) {
            return "";
        }
        List<String> configsCardinality = new ArrayList<>();
        if (columnDTO.hasMappedBy()) {
            configsCardinality.add("mappedBy = \"%s\"".formatted(columnDTO.getMappedBy()));
        }

        return Cardinality.getAnottation(columnDTO.getCardinality(), configsCardinality);
    }

    private String getGeneratedValue(ColumnDTO columnDTO) {
        if (!columnDTO.hasGenerationType()) {
            return "\t@GeneratedValue\n";
        }
        return GenerationType.getAnottation(columnDTO.getGenerationType());
    }

    private String getUpdateMethod(TableDTO tableDTO) {
        if (!tableDTO.hasUpdate()) {
            return "";
        }

        List<ColumnDTO> columns = tableDTO.getColumns().stream()
                .filter(column -> column.isUpdatable() && !column.hasCardinality() && !column.isPrimaryKey())
                .toList();

        StringBuilder updateMethod = new StringBuilder();
        String objectNameLowerCase = lowerCaseFirstLetter(tableDTO.getTable());
        String methodName = "\tpublic void update(%sUpdateDTO %sUpdateDTO) {\n".formatted(tableDTO.getTable(), objectNameLowerCase);

        updateMethod.append(methodName);

        for (ColumnDTO columnDTO : columns) {
            updateMethod.append("\t\t")
                        .append(columnDTO.getName())
                        .append(" = ofNullable(")
                        .append(lowerCaseFirstLetter(objectNameLowerCase))
                        .append("UpdateDTO.get")
                        .append(capitalizeFirstLetter(columnDTO.getName()))
                        .append("()).orElse(").append(columnDTO.getName()).append(");\n");
        }

        updateMethod.append("\t}\n\n");
        return updateMethod.toString();
    }
}
