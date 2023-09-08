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

    private PropertyDTO propertyDTO;
    private TableDTO tableDTO;

    public void create(PropertyDTO propertyDTO, TableDTO tableDTO, List<String> enums) throws IOException {
        this.propertyDTO = propertyDTO;
        this.tableDTO = tableDTO;

        String fileName = tableDTO.getTable() + propertyDTO.getEntitySuffix() + ".java";
        createFile(getDirectory(propertyDTO.getUrlProject(), propertyDTO.getEntityPath()), fileName, getEntityCode(enums));
    }

    private String getEntityCode(List<String> enums) {
        StringBuilder code = new StringBuilder();
        String className = "public class %s%s %s{\n\n"
                .formatted(tableDTO.getTable(), propertyDTO.getEntitySuffix(), getSerializableImplements(propertyDTO.isUseSerializable()));

        code.append(getPackageName(propertyDTO.getUrlProject(), propertyDTO.getEntityPath()))
            .append(getImports(enums))
            .append(getHeader())
            .append(className)
            .append(getColumns(enums))
            .append(getConstructors(propertyDTO.isUseLombok(), tableDTO.getTable(), tableDTO.getColumns(), propertyDTO.getEntitySuffix()))
            .append(getGettersSetters(propertyDTO.isUseLombok(), tableDTO.getColumns()))
            .append(getUpdateMethod())
            .append("}");
        return code.toString();
    }

    private String getImports(List<String> enums) {
        StringBuilder imports = new StringBuilder();
        imports.append(PERSISTENCE.getFormattedImport())
               .append(getSerializableImport(propertyDTO.isUseSerializable()))
               .append(getLombokImport(propertyDTO.isUseLombok()))
               .append(getImportsByConfigEntityDTO(tableDTO.getColumns()));

        if (tableDTO.hasEnum(enums)) {
            imports.append("import ").append(convertDirectoryToPackage(getDirectory(propertyDTO.getUrlProject(), propertyDTO.getEnumerationPath())))
                   .append(".*;\n");
        }

        if (tableDTO.hasUpdate()) {
            imports.append("import ").append(convertDirectoryToPackage(getDirectory(propertyDTO.getUrlProject(), propertyDTO.getUpdateDTOPath())))
                   .append(".").append(tableDTO.getTable()).append(propertyDTO.getUpdateDTOSuffix()).append(";\n")
                   .append(OPTIONAL.getFormattedImport());
        }

        imports.append("\n");
        return imports.toString();
    }

    private String getHeader() {
        StringBuilder header = new StringBuilder();
        header.append(getLombokHeader(propertyDTO.isUseLombok()))
              .append("@Entity\n")
              .append("@Table(name = \"%s\")\n".formatted(parseCamelCaseToSnakeCase(tableDTO.getTable())));

        return header.toString();
    }

    private String getColumns(List<String> enums) {
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
            fieldCode.append(EnumType.getEnumType(columnDTO.getEnumType()));
        } else if (enums.contains(columnDTO.getType())) {
            fieldCode.append("\t@Enumerated(EnumType.STRING)\n");
        }

        String typeFormatted = columnDTO.isCollection() ? "%s<%s>".formatted(columnDTO.isList() ? LIST.getName() : SET.getName(), columnDTO.getType()) : columnDTO.getType();
        String propertyName = "\tprivate %s %s;\n".formatted(typeFormatted, columnDTO.getName());

        fieldCode.append(getCardinality(columnDTO))
                 .append(getOrderBy(columnDTO))
                 .append(getTemporalType(columnDTO))
                 .append(getColumnName(columnDTO))
                 .append(propertyName)
                 .append("\n");

        return fieldCode.toString();
    }

    private String getCardinality(ColumnDTO columnDTO) {
        if (!columnDTO.hasCardinality()) {
            return "";
        }
        List<String> configsCardinality = new ArrayList<>();
        if (columnDTO.hasMappedBy() && !Cardinality.MANY_TO_ONE.getName().equalsIgnoreCase(columnDTO.getCardinality())) {
            configsCardinality.add("mappedBy = \"%s\"".formatted(columnDTO.getMappedBy()));
        }
        if (columnDTO.hasCascadeType()) {
            configsCardinality.add(CascadeType.getCascadeType(columnDTO.getCascadeType()));
        }
        return Cardinality.getAnottation(columnDTO.getCardinality(), configsCardinality);
    }

    private String getOrderBy(ColumnDTO columnDTO) {
        if (!columnDTO.hasOrderBy() || !columnDTO.isCollection()) {
            return "";
        }
        return "\t@OrderBy(\"%s\")\n".formatted(columnDTO.getOrderBy());
    }

    private String getTemporalType(ColumnDTO columnDTO) {
        if (!columnDTO.hasTemporalType()) {
            return "";
        }
        return TemporalType.getTemporalType(columnDTO.getTemporalType());
    }

    private String getColumnName(ColumnDTO columnDTO) {
        List<String> configsField = new ArrayList<>();
        String nameSnakeCase = parseCamelCaseToSnakeCase(columnDTO.getName());

        if (columnDTO.hasCardinality() && !columnDTO.isCollection()) {
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

    private String getGeneratedValue(ColumnDTO columnDTO) {
        if (!columnDTO.hasGenerationType()) {
            return "\t@GeneratedValue\n";
        }
        return GenerationType.getAnottation(columnDTO.getGenerationType());
    }

    private String getUpdateMethod() {
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
