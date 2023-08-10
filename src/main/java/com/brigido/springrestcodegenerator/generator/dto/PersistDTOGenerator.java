package com.brigido.springrestcodegenerator.generator.dto;

import com.brigido.springrestcodegenerator.dto.*;
import com.brigido.springrestcodegenerator.enumeration.Imports;
import com.brigido.springrestcodegenerator.generator.BaseGenerator;
import java.io.IOException;
import java.util.*;
import java.util.concurrent.atomic.AtomicReference;
import static com.brigido.springrestcodegenerator.enumeration.Imports.*;
import static com.brigido.springrestcodegenerator.util.StringUtil.*;
import static java.util.Arrays.stream;

public class PersistDTOGenerator extends BaseGenerator {

    private PropertyDTO propertyDTO;
    private TableDTO tableDTO;

    public void create(PropertyDTO propertyDTO, TableDTO tableDTO, Map<String, String> entitiesId, List<String> enums) throws IOException {
        this.propertyDTO = propertyDTO;
        this.tableDTO = tableDTO;

        String fileName = tableDTO.getTable() + propertyDTO.getPersistDTOSuffix() + ".java";
        createFile(getDTODirectory(propertyDTO.getUrlProject()), fileName, getResponseDTOCode(entitiesId, enums));
    }

    private String getResponseDTOCode(Map<String, String> entitiesId, List<String> enums) {
        StringBuilder code = new StringBuilder();
        String className = "public class %s%s {\n\n".formatted(tableDTO.getTable(), propertyDTO.getPersistDTOSuffix());

        code.append(getPackageName(getDTODirectory(propertyDTO.getUrlProject())))
            .append(getImports(entitiesId, enums))
            .append(getLombokHeader(propertyDTO.isUseLombok()))
            .append(className)
            .append(getColumns(entitiesId))
            .append(getConstructors(propertyDTO.isUseLombok(), tableDTO, propertyDTO.getPersistDTOSuffix()))
            .append(getGettersSetters(propertyDTO.isUseLombok(), tableDTO))
            .append("}");

        return code.toString();
    }

    private String getColumns(Map<String, String> entitiesId) {
        StringBuilder columns = new StringBuilder();
        for (ColumnDTO columnDTO : tableDTO.getColumnsPersist()) {
            columns.append(getFieldCode(columnDTO, entitiesId));
        }
        return columns.toString();
    }

    private String getFieldCode(ColumnDTO columnDTO, Map<String, String> entitiesId) {
        if (columnDTO.isCollection() || columnDTO.isPrimaryKey()) {
            return "";
        }

        String required = columnDTO.isRequired() ? "\t@NotNull\n" : "";

        if (columnDTO.hasCardinality()) {
            AtomicReference<String> fieldCode = new AtomicReference<>("");
            entitiesId.forEach((table, primaryKey) -> {
                if (table.equals(columnDTO.getType())) {
                    fieldCode.set("%s\tprivate %s %sId;\n\n".formatted(required, primaryKey, lowerCaseFirstLetter(table)));
                }
            });

            return fieldCode.get();
        }
        return "%s\tprivate %s %s;\n\n".formatted(required, columnDTO.getType(), columnDTO.getName());
    }

    private String getImports(Map<String, String> entitiesId, List<String> enums) {
        StringBuilder imports = new StringBuilder();
        imports.append(getLombokImport(propertyDTO.isUseLombok()))
               .append(getImportsByConfigEntityDTO(tableDTO.getColumnsPersist()))
               .append(getExternalImports(entitiesId));

        if (tableDTO.hasRequired()) {
            imports.append(NOT_NULL.getFormattedImport());
        }

        if (tableDTO.hasEnum(enums)) {
            imports.append("import ").append(convertDirectoryToPackage(getEnumerationDirectory(propertyDTO.getUrlProject())))
                   .append(".*;\n");
        }

        imports.append("\n");
        return imports.toString();
    }

    private String getExternalImports(Map<String, String> entitiesId) {
        StringBuilder externalImports = new StringBuilder();
        List<String> imports = new ArrayList<>();

        entitiesId.forEach((table, primaryKey) -> tableDTO.getColumns().forEach(columnDTO -> {
            if (table.equals(columnDTO.getType())) {
                imports.add(primaryKey);
            }
        }));
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
