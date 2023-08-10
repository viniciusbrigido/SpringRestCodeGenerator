package com.brigido.springrestcodegenerator.generator;

import com.brigido.springrestcodegenerator.dto.PropertyDTO;
import com.brigido.springrestcodegenerator.dto.TableDTO;
import java.io.IOException;
import static com.brigido.springrestcodegenerator.enumeration.Imports.*;
import static com.brigido.springrestcodegenerator.util.StringUtil.*;

public class ServiceGenerator extends BaseGenerator {

    private PropertyDTO propertyDTO;
    private TableDTO tableDTO;

    public void create(PropertyDTO propertyDTO, TableDTO tableDTO) throws IOException {
        this.propertyDTO = propertyDTO;
        this.tableDTO = tableDTO;

        String fileName = tableDTO.getTable() + propertyDTO.getServiceSuffix() + ".java";
        createFile(getDirectory(propertyDTO.getUrlProject(), propertyDTO.getServicePath()), fileName, getServiceCode());
    }

    private String getServiceCode() {
        StringBuilder code = new StringBuilder();
        String interfaceName = "public interface %s%s {\n\n".formatted(tableDTO.getTable(), propertyDTO.getServiceSuffix());

        code.append(getPackageName(propertyDTO.getUrlProject(), propertyDTO.getServicePath()))
            .append(getImports())
            .append(interfaceName)
            .append(getCrudMethods())
            .append("}");

        return code.toString();
    }

    private String getImports() {
        StringBuilder imports = new StringBuilder();
        imports.append("import ").append(convertDirectoryToPackage(getDirectory(propertyDTO.getUrlProject(), propertyDTO.getPersistDTOPath())))
               .append(".").append(tableDTO.getTable()).append(propertyDTO.getPersistDTOSuffix()).append(";\n")
               .append("import ").append(convertDirectoryToPackage(getDirectory(propertyDTO.getUrlProject(), propertyDTO.getResponseDTOPath())))
               .append(".").append(tableDTO.getTable()).append(propertyDTO.getResponseDTOSuffix()).append(";\n");

        if (tableDTO.hasUpdate()) {
            imports.append("import ").append(convertDirectoryToPackage(getDirectory(propertyDTO.getUrlProject(), propertyDTO.getUpdateDTOPath())))
                   .append(".").append(tableDTO.getTable()).append(propertyDTO.getUpdateDTOSuffix()).append(";\n");
        }
        imports.append("import ")
               .append(convertDirectoryToPackage(getDirectory(propertyDTO.getUrlProject(), propertyDTO.getEntityPath())))
               .append(".").append(tableDTO.getTable()).append(";\n")
               .append(LIST.getFormattedImport())
               .append(getImportsIdLine(tableDTO.getColumns()))
               .append("\n");

        return imports.toString();
    }

    private String getCrudMethods() {
        StringBuilder crudMethods = new StringBuilder();
        String objectNameLowerCase = lowerCaseFirstLetter(tableDTO.getTable());

        String methodCreateName = "\t%s%s create(%s%s %s%s);\n".formatted(
                tableDTO.getTable(), propertyDTO.getResponseDTOSuffix(), tableDTO.getTable(), propertyDTO.getPersistDTOSuffix(),
                objectNameLowerCase, propertyDTO.getPersistDTOSuffix());

        String methodFindByIdName = "\t%s findById(%s id);\n".formatted(
                tableDTO.getTable(), tableDTO.getIdType());

        String methodFindByIdDTOName = "\t%s%s findByIdDTO(%s id);\n".formatted(
                tableDTO.getTable(), propertyDTO.getResponseDTOSuffix(), tableDTO.getIdType());

        String methodDeleteName = "\tvoid delete(%s id);\n".formatted(tableDTO.getIdType());

        crudMethods.append(methodCreateName)
                   .append(methodFindByIdName)
                   .append(methodFindByIdDTOName)
                   .append(methodDeleteName);

        if (tableDTO.hasUpdate()) {
            String methodUpdateName = "\t%s%s update(%s%s %s%s);\n".formatted(
                    tableDTO.getTable(), propertyDTO.getResponseDTOSuffix(), tableDTO.getTable(), propertyDTO.getUpdateDTOSuffix(),
                    objectNameLowerCase, propertyDTO.getUpdateDTOSuffix());

            crudMethods.append(methodUpdateName);
        }

        String methodFindAllName = "\tList<%s%s> findAll();\n".formatted(tableDTO.getTable(), propertyDTO.getResponseDTOSuffix());
        crudMethods.append(methodFindAllName);

        return crudMethods.toString();
    }
}
