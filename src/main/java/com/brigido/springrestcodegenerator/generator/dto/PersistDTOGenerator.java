package com.brigido.springrestcodegenerator.generator.dto;

import com.brigido.springrestcodegenerator.dto.*;
import com.brigido.springrestcodegenerator.generator.BaseGenerator;
import java.io.IOException;
import java.util.*;

public class PersistDTOGenerator extends BaseGenerator {

    private PropertyDTO propertyDTO;
    private TableDTO tableDTO;
    private Map<String, String> entitiesId;

    public void create(PropertyDTO propertyDTO, TableDTO tableDTO, Map<String, String> entitiesId, List<String> enums) throws IOException {
        if (!tableDTO.hasPersist(entitiesId)) {
            return;
        }
        this.propertyDTO = propertyDTO;
        this.tableDTO = tableDTO;
        this.entitiesId = entitiesId;

        String fileName = tableDTO.getTable() + propertyDTO.getPersistDTOSuffix() + ".java";
        createFile(getDirectory(propertyDTO.getUrlProject(), propertyDTO.getPersistDTOPath()), fileName, getResponseDTOCode(enums));
    }

    private String getResponseDTOCode(List<String> enums) {
        StringBuilder code = new StringBuilder();
        String className = "public class %s%s {\n\n".formatted(tableDTO.getTable(), propertyDTO.getPersistDTOSuffix());

        code.append(getPackageName(propertyDTO.getUrlProject(), propertyDTO.getPersistDTOPath()))
            .append(getImports(enums))
            .append(getLombokHeader(propertyDTO.isUseLombok()))
            .append(className)
            .append(getColumns())
            .append(getConstructors(propertyDTO.isUseLombok(), tableDTO.getTable(), tableDTO.getColumnsPersist(entitiesId), propertyDTO.getPersistDTOSuffix()))
            .append(getGettersSetters(propertyDTO.isUseLombok(), tableDTO.getColumnsPersist(entitiesId)))
            .append("}");

        return code.toString();
    }

    private String getColumns() {
        StringBuilder columns = new StringBuilder();
        for (ColumnDTO columnDTO : tableDTO.getColumnsPersist(entitiesId)) {
            columns.append(getFieldCode(columnDTO));
        }
        return columns.toString();
    }

    private String getFieldCode(ColumnDTO columnDTO) {
        return "\tprivate %s %s;\n\n".formatted(columnDTO.getType(), columnDTO.getName());
    }

    private String getImports(List<String> enums) {
        StringBuilder imports = new StringBuilder();
        imports.append(getLombokImport(propertyDTO.isUseLombok()))
               .append(getImportsByConfigEntityDTO(tableDTO.getColumnsPersist(entitiesId)));

        if (tableDTO.hasEnum(enums)) {
            imports.append("import ").append(convertDirectoryToPackage(getDirectory(propertyDTO.getUrlProject(), "enumeration")))
                   .append(".*;\n");
        }

        imports.append("\n");
        return imports.toString();
    }
}
