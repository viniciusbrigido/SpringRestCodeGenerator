package com.brigido.springrestcodegenerator.generator;

import com.brigido.springrestcodegenerator.dto.PropertyDTO;
import com.brigido.springrestcodegenerator.dto.TableDTO;
import java.io.IOException;
import java.util.Map;
import static com.brigido.springrestcodegenerator.enumeration.Imports.*;
import static com.brigido.springrestcodegenerator.util.ConstUtil.*;
import static com.brigido.springrestcodegenerator.util.StringUtil.*;

public class ServiceGenerator extends BaseGenerator {

    private PropertyDTO propertyDTO;
    private TableDTO tableDTO;
    private Map<String, String> entitiesId;

    public void create(PropertyDTO propertyDTO, TableDTO tableDTO, Map<String, String> entitiesId) throws IOException {
        this.propertyDTO = propertyDTO;
        this.tableDTO = tableDTO;
        this.entitiesId = entitiesId;

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
        if (tableDTO.hasPersist(entitiesId)) {
            imports.append("import ").append(convertDirectoryToPackage(getDirectory(propertyDTO.getUrlProject(), propertyDTO.getPersistDTOPath())))
                   .append(".").append(tableDTO.getTable()).append(propertyDTO.getPersistDTOSuffix()).append(";\n");
        }

        imports.append("import ").append(convertDirectoryToPackage(getDirectory(propertyDTO.getUrlProject(), propertyDTO.getResponseDTOPath())))
               .append(".").append(tableDTO.getTable()).append(propertyDTO.getResponseDTOSuffix()).append(";\n");

        if (tableDTO.hasUpdate()) {
            imports.append("import ").append(convertDirectoryToPackage(getDirectory(propertyDTO.getUrlProject(), propertyDTO.getUpdateDTOPath())))
                   .append(".").append(tableDTO.getTable()).append(propertyDTO.getUpdateDTOSuffix()).append(";\n");
        }
        imports.append("import ")
               .append(convertDirectoryToPackage(getDirectory(propertyDTO.getUrlProject(), propertyDTO.getEntityPath())))
               .append(".").append(tableDTO.getTable()).append(propertyDTO.getEntitySuffix()).append(";\n")
               .append(LIST.getFormattedImport())
               .append(getImportsIdLine(tableDTO.getColumns()))
               .append("\n");

        return imports.toString();
    }

    private String getCrudMethods() {
        StringBuilder crudMethods = new StringBuilder();
        String objectNameLowerCase = lowerCaseFirstLetter(tableDTO.getTable());

        String methodCreateName = "\t%s%s %s(%s%s %s%s);\n".formatted(
                tableDTO.getTable(), propertyDTO.getResponseDTOSuffix(), propertyDTO.getFunctionCreate(),
                tableDTO.getTable(), propertyDTO.getPersistDTOSuffix(),
                objectNameLowerCase, propertyDTO.getPersistDTOSuffix());

        String methodFindByIdName = "\t%s%s %s%s(%s id);\n".formatted(
                tableDTO.getTable(), propertyDTO.getEntitySuffix(),
                propertyDTO.getFunctionFindById(), VALIDATE, tableDTO.getIdType());

        String methodFindByIdDTOName = "\t%s%s %s(%s id);\n".formatted(
                tableDTO.getTable(), propertyDTO.getResponseDTOSuffix(),
                propertyDTO.getFunctionFindById(), tableDTO.getIdType());

        String methodDeleteName = "\tvoid %s(%s id);\n".formatted(propertyDTO.getFunctionDelete(), tableDTO.getIdType());

        if (tableDTO.hasPersist(entitiesId)) {
            crudMethods.append(methodCreateName);
        }

        crudMethods.append(methodFindByIdName)
                   .append(methodFindByIdDTOName)
                   .append(methodDeleteName);

        if (tableDTO.hasUpdate()) {
            String methodUpdateName = "\t%s%s %s(%s%s %s%s);\n".formatted(
                    tableDTO.getTable(), propertyDTO.getResponseDTOSuffix(), propertyDTO.getFunctionUpdate(),
                    tableDTO.getTable(), propertyDTO.getUpdateDTOSuffix(),
                    objectNameLowerCase, propertyDTO.getUpdateDTOSuffix());

            crudMethods.append(methodUpdateName);
        }

        String methodFindAllName = "\tList<%s%s> findAll();\n".formatted(tableDTO.getTable(), propertyDTO.getResponseDTOSuffix());
        crudMethods.append(methodFindAllName);

        return crudMethods.toString();
    }
}
