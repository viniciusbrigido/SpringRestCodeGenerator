package com.brigido.springrestcodegenerator.generator.dto;

import com.brigido.springrestcodegenerator.dto.*;
import com.brigido.springrestcodegenerator.generator.BaseGenerator;
import java.io.IOException;
import java.util.List;
import static com.brigido.springrestcodegenerator.enumeration.Imports.LIST;
import static com.brigido.springrestcodegenerator.enumeration.Imports.SET;

public class ResponseDTOGenerator extends BaseGenerator {

    private PropertyDTO propertyDTO;
    private TableDTO tableDTO;

    public void create(PropertyDTO propertyDTO, TableDTO tableDTO, List<String> enums) throws IOException {
        this.propertyDTO = propertyDTO;
        this.tableDTO = tableDTO;

        String fileName = tableDTO.getTable() + propertyDTO.getResponseDTOSuffix() + ".java";
        createFile(getDirectory(propertyDTO.getUrlProject(), propertyDTO.getResponseDTOPath()), fileName, getResponseDTOCode(enums));
    }

    private String getResponseDTOCode(List<String> enums) {
        StringBuilder code = new StringBuilder();
        String className = "public class %s%s {\n\n".formatted(tableDTO.getTable(), propertyDTO.getResponseDTOSuffix());

        code.append(getPackageName(propertyDTO.getUrlProject(), propertyDTO.getResponseDTOPath()))
            .append(getImports(enums))
            .append(getLombokHeader(propertyDTO.isUseLombok()))
            .append(className)
            .append(getColumns())
            .append(getConstructors(propertyDTO.isUseLombok(), tableDTO, propertyDTO.getResponseDTOSuffix()))
            .append(getGettersSetters(propertyDTO.isUseLombok(), tableDTO))
            .append("}");

        return code.toString();
    }

    private String getColumns() {
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

        String typeFormatted = columnDTO.isCollection() ? "%s<%s>".formatted(columnDTO.isList() ? LIST.getName() : SET.getName(), columnDTO.getType()) : columnDTO.getType();
        String propertyName = "\tprivate %s %s;\n\n".formatted(typeFormatted, columnDTO.getName());
        fieldCode.append(propertyName);

        return fieldCode.toString();
    }

    private String getImports(List<String> enums) {
        StringBuilder imports = new StringBuilder();
        imports.append(getLombokImport(propertyDTO.isUseLombok()))
               .append(getImportsByConfigEntityDTO(tableDTO.getColumns()));

        if (tableDTO.hasEnum(enums)) {
            imports.append("import ").append(convertDirectoryToPackage(getDirectory(propertyDTO.getUrlProject(), "enumeration")))
                   .append(".*;\n");
        }

        imports.append("\n");
        return imports.toString();
    }
}
