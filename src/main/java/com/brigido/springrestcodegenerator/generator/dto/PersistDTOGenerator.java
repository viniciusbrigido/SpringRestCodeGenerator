package com.brigido.springrestcodegenerator.generator.dto;

import com.brigido.springrestcodegenerator.dto.*;
import com.brigido.springrestcodegenerator.enumeration.Imports;
import com.brigido.springrestcodegenerator.generator.BaseGenerator;
import java.io.IOException;
import java.util.*;
import java.util.concurrent.atomic.AtomicReference;
import static com.brigido.springrestcodegenerator.enumeration.Imports.*;
import static java.lang.String.format;
import static java.util.Arrays.stream;
import static java.util.Objects.*;

public class PersistDTOGenerator extends BaseGenerator {

    private String directory;

    public void create(PropertyDTO propertyDTO, TableDTO tableDTO, String directory, Map<String, String> entitiesId, List<String> enums) throws IOException {
        setPropertyDTO(propertyDTO);
        this.directory = directory;

        String fileName = tableDTO.getTable() + "PersistDTO.java";
        createFile(getDTODirectory(directory), fileName, getResponseDTOCode(tableDTO, entitiesId, enums));
    }

    private String getResponseDTOCode(TableDTO tableDTO, Map<String, String> entitiesId, List<String> enums) {
        StringBuilder code = new StringBuilder();
        String className = format("public class %sPersistDTO {\n\n", tableDTO.getTable());

        code.append(getPackageName(getDTODirectory(directory)))
            .append(getImports(tableDTO, entitiesId, enums))
            .append(getLombokHeader())
            .append(className)
            .append(getColumns(tableDTO, entitiesId))
            .append(getConstructors(tableDTO, "PersistDTO"))
            .append(getGettersSetters(tableDTO))
            .append("}");

        return code.toString();
    }

    private String getColumns(TableDTO tableDTO, Map<String, String> entitiesId) {
        StringBuilder columns = new StringBuilder();
        for (ColumnDTO columnDTO : tableDTO.getColumnsPersist()) {
            columns.append(getFieldCode(columnDTO, entitiesId));
        }
        return columns.toString();
    }

    private String getFieldCode(ColumnDTO columnDTO, Map<String, String> entitiesId) {
        if (columnDTO.isList() || columnDTO.isPrimaryKey()) {
            return "";
        }

        String required = columnDTO.isRequired() ? "\t@NotNull\n" : "";

        if (nonNull(columnDTO.getCardinality())) {
            AtomicReference<String> fieldCode = new AtomicReference<>("");
            entitiesId.forEach((table, primaryKey) -> {
                if (table.equals(columnDTO.getType())) {
                    fieldCode.set(format("%s\tprivate %s %sId;\n\n", required, primaryKey, lowerCaseFirstLetter(table)));
                }
            });

            return fieldCode.get();
        }
        return format("%s\tprivate %s %s;\n\n", required, columnDTO.getType(), columnDTO.getName());
    }

    private String getImports(TableDTO tableDTO, Map<String, String> entitiesId, List<String> enums) {
        StringBuilder imports = new StringBuilder();
        imports.append(getLombokImport())
               .append(getImportsByConfigEntityDTO(tableDTO.getColumnsPersist()))
               .append(getExternalImports(tableDTO, entitiesId));

        if (tableDTO.hasRequired()) {
            imports.append(NOT_NULL.getFormattedImport());
        }

        if (tableDTO.hasEnum(enums)) {
            imports.append("import ").append(convertDirectoryToPackage(getEnumerationDirectory(directory)))
                   .append(".*;\n");
        }

        imports.append("\n");
        return imports.toString();
    }

    private String getExternalImports(TableDTO tableDTO, Map<String, String> entitiesId) {
        StringBuilder externalImports = new StringBuilder();
        List<String> imports = new ArrayList<>();

        entitiesId.forEach((table, primaryKey) -> {
            tableDTO.getColumns().forEach(columnDTO -> {
                if (table.equals(columnDTO.getType())) {
                    imports.add(primaryKey);
                }
            });
        });
        List<String> distinctImports = new ArrayList<>(new HashSet<>(imports));
        ConfigEntityDTO configEntityDTO = getConfigEntityDTOFromExternalImports(distinctImports);

        stream(Imports.values())
                .filter(importEnum -> checkImport(configEntityDTO, importEnum))
                .map(Imports::getFormattedImport)
                .forEach(externalImports::append);

        return externalImports.toString();
    }

    public ConfigEntityDTO getConfigEntityDTOFromExternalImports(List<String> types) {
        ConfigEntityDTO configEntityDTO = new ConfigEntityDTO();
        for (String type : types) {
            if (type.equals(DATE.getName())) {
                configEntityDTO.setContainsDate(true);
            }
            if (type.equals(UUID.getName())) {
                configEntityDTO.setContainsUUID(true);
            }
            if (type.equals(BIG_INTEGER.getName())) {
                configEntityDTO.setContainsBigInteger(true);
            }
            if (type.equals(BIG_DECIMAL.getName())) {
                configEntityDTO.setContainsBigDecimal(true);
            }
            if (type.equals(LOCAL_DATE.getName())) {
                configEntityDTO.setContainsLocalDate(true);
            }
            if (type.equals(LOCAL_DATE_TIME.getName())) {
                configEntityDTO.setContainsLocalDateTime(true);
            }
        }
        return configEntityDTO;
    }
}
