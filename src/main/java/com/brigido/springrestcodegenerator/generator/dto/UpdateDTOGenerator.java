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
        createFile(getDTODirectory(propertyDTO.getUrlProject()), fileName, getResponseDTOCode(enums));
    }

    private String getResponseDTOCode(List<String> enums) {
        StringBuilder code = new StringBuilder();
        String className = "public class %s%s {\n\n".formatted(tableDTO.getTable(), propertyDTO.getUpdateDTOSuffix());

        code.append(getPackageName(getDTODirectory(propertyDTO.getUrlProject())))
            .append(getImports(enums))
            .append(getLombokHeader(propertyDTO.isUseLombok()))
            .append(className)
            .append(getColumns())
            .append(getConstructors(propertyDTO.isUseLombok(), tableDTO, propertyDTO.getUpdateDTOSuffix()))
            .append(getGettersSetters(propertyDTO.isUseLombok(), tableDTO))
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
            imports.append("import ").append(convertDirectoryToPackage(getEnumerationDirectory(propertyDTO.getUrlProject())))
                   .append(".*;\n");
        }

        imports.append("\n");
        return imports.toString();
    }
}
