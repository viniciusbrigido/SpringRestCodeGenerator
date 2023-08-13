package com.brigido.springrestcodegenerator.generator.dto;

import com.brigido.springrestcodegenerator.dto.*;
import com.brigido.springrestcodegenerator.generator.BaseGenerator;
import java.io.IOException;
import java.util.List;

public class UpdateDTOGenerator extends BaseGenerator {

    private PropertyDTO propertyDTO;
    private TableDTO tableDTO;

    public void create(PropertyDTO propertyDTO, TableDTO tableDTO, List<String> enums) throws IOException {
        if (!tableDTO.hasUpdate()) {
            return;
        }
        this.propertyDTO = propertyDTO;
        this.tableDTO = tableDTO;

        String fileName = tableDTO.getTable() + propertyDTO.getUpdateDTOSuffix() + ".java";
        createFile(getDirectory(propertyDTO.getUrlProject(), propertyDTO.getUpdateDTOPath()), fileName, getResponseDTOCode(enums));
    }

    private String getResponseDTOCode(List<String> enums) {
        StringBuilder code = new StringBuilder();
        String className = "public class %s%s {\n\n".formatted(tableDTO.getTable(), propertyDTO.getUpdateDTOSuffix());

        code.append(getPackageName(propertyDTO.getUrlProject(), propertyDTO.getUpdateDTOPath()))
            .append(getImports(enums))
            .append(getLombokHeader(propertyDTO.isUseLombok()))
            .append(className)
            .append(getColumns())
            .append(getConstructors(propertyDTO.isUseLombok(), tableDTO.getTable(), tableDTO.getColumnsUpdate(), propertyDTO.getUpdateDTOSuffix()))
            .append(getGettersSetters(propertyDTO.isUseLombok(), tableDTO.getColumnsUpdate()))
            .append("}");

        return code.toString();
    }

    private String getColumns() {
        StringBuilder columns = new StringBuilder();
        for (ColumnDTO columnDTO : tableDTO.getColumnsUpdate()) {
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
               .append(getImportsByConfigEntityDTO(tableDTO.getColumnsUpdate()));

        if (tableDTO.hasEnum(enums)) {
            imports.append("import ").append(convertDirectoryToPackage(getDirectory(propertyDTO.getUrlProject(), "enumeration")))
                   .append(".*;\n");
        }

        imports.append("\n");
        return imports.toString();
    }
}
