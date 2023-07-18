package com.brigido.springrestcodegenerator.generator.dto;

import com.brigido.springrestcodegenerator.dto.*;
import com.brigido.springrestcodegenerator.generator.BaseGenerator;
import java.io.IOException;
import java.util.List;

public class UpdateDTOGenerator extends BaseGenerator {

    private String directory;

    public void create(PropertyDTO propertyDTO, TableDTO tableDTO, String directory, List<String> enums) throws IOException {
        if (!tableDTO.hasUpdate()) {
            return;
        }
        setPropertyDTO(propertyDTO);
        this.directory = directory;

        String fileName = tableDTO.getTable() + "UpdateDTO.java";
        createFile(getDTODirectory(directory), fileName, getResponseDTOCode(tableDTO, enums));
    }

    private String getResponseDTOCode(TableDTO tableDTO, List<String> enums) {
        StringBuilder code = new StringBuilder();
        String className = "public class %sUpdateDTO {\n\n".formatted(tableDTO.getTable());

        code.append(getPackageName(getDTODirectory(directory)))
            .append(getImports(tableDTO, enums))
            .append(getLombokHeader())
            .append(className)
            .append(getColumns(tableDTO))
            .append(getConstructors(tableDTO, "UpdateDTO"))
            .append(getGettersSetters(tableDTO))
            .append("}");

        return code.toString();
    }

    private String getColumns(TableDTO tableDTO) {
        StringBuilder columns = new StringBuilder();
        for (ColumnDTO columnDTO : tableDTO.getColumnsUpdate()) {
            columns.append(getFieldCode(columnDTO));
        }
        return columns.toString();
    }

    private String getFieldCode(ColumnDTO columnDTO) {
        return "\tprivate %s %s;\n\n".formatted(columnDTO.getType(), columnDTO.getName());
    }

    private String getImports(TableDTO tableDTO, List<String> enums) {
        StringBuilder imports = new StringBuilder();
        imports.append(getLombokImport())
               .append(getImportsByConfigEntityDTO(tableDTO.getColumnsUpdate()));

        if (tableDTO.hasEnum(enums)) {
            imports.append("import ").append(convertDirectoryToPackage(getEnumerationDirectory(directory)))
                   .append(".*;\n");
        }

        imports.append("\n");
        return imports.toString();
    }
}
