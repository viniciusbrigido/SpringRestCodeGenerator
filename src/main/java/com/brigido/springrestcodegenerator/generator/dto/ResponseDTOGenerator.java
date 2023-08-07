package com.brigido.springrestcodegenerator.generator.dto;

import com.brigido.springrestcodegenerator.dto.*;
import com.brigido.springrestcodegenerator.generator.BaseGenerator;
import java.io.IOException;
import java.util.List;
import static com.brigido.springrestcodegenerator.enumeration.Imports.LIST;
import static java.util.Objects.*;

public class ResponseDTOGenerator extends BaseGenerator {

    private String directory;

    public void create(PropertyDTO propertyDTO, TableDTO tableDTO, String directory, List<String> enums) throws IOException {
        setPropertyDTO(propertyDTO);
        this.directory = directory;

        String fileName = tableDTO.getTable() + "ResponseDTO.java";
        createFile(getDTODirectory(directory), fileName, getResponseDTOCode(tableDTO, enums));
    }

    private String getResponseDTOCode(TableDTO tableDTO, List<String> enums) {
        StringBuilder code = new StringBuilder();
        String className = "public class %sResponseDTO {\n\n".formatted(tableDTO.getTable());

        code.append(getPackageName(getDTODirectory(directory)))
            .append(getImports(tableDTO, enums))
            .append(getLombokHeader())
            .append(className)
            .append(getColumns(tableDTO))
            .append(getConstructors(tableDTO, "ResponseDTO"))
            .append(getGettersSetters(tableDTO))
            .append("}");

        return code.toString();
    }

    private String getColumns(TableDTO tableDTO) {
        StringBuilder columns = new StringBuilder();
        for (ColumnDTO columnDTO : tableDTO.getColumns()) {
            columns.append(getFieldCode(columnDTO));
        }
        return columns.toString();
    }

    private String getFieldCode(ColumnDTO columnDTO) {
        StringBuilder fieldCode = new StringBuilder();
        if (columnDTO.hasCardinality()) {
            return "";
        }

        String typeFormatted = columnDTO.isList() ? "%s<%s>".formatted(LIST.getName(), columnDTO.getType()) : columnDTO.getType();
        String propertyName = "\tprivate %s %s;\n\n".formatted(typeFormatted, columnDTO.getName());
        fieldCode.append(propertyName);

        return fieldCode.toString();
    }

    private String getImports(TableDTO tableDTO, List<String> enums) {
        StringBuilder imports = new StringBuilder();
        imports.append(getLombokImport())
               .append(getImportsByConfigEntityDTO(tableDTO.getColumns()));

        if (tableDTO.hasEnum(enums)) {
            imports.append("import ").append(convertDirectoryToPackage(getEnumerationDirectory(directory)))
                   .append(".*;\n");
        }

        imports.append("\n");
        return imports.toString();
    }
}
