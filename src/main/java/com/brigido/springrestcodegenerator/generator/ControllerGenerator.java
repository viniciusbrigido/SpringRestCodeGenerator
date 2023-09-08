package com.brigido.springrestcodegenerator.generator;

import com.brigido.springrestcodegenerator.dto.PropertyDTO;
import com.brigido.springrestcodegenerator.dto.TableDTO;
import java.io.IOException;
import java.util.Map;
import static com.brigido.springrestcodegenerator.enumeration.Imports.*;
import static com.brigido.springrestcodegenerator.util.StringUtil.*;
import static java.util.Objects.*;

public class ControllerGenerator extends BaseGenerator {

    private String apiVersion;
    private PropertyDTO propertyDTO;
    private TableDTO tableDTO;
    private Map<String, String> entitiesId;

    public void create(String apiVersion, PropertyDTO propertyDTO, TableDTO tableDTO, Map<String, String> entitiesId) throws IOException {
        this.apiVersion = apiVersion;
        this.propertyDTO = propertyDTO;
        this.tableDTO = tableDTO;
        this.entitiesId = entitiesId;

        String fileName = tableDTO.getTable() + propertyDTO.getControllerSuffix() + ".java";
        createFile(getDirectory(propertyDTO.getUrlProject(), propertyDTO.getControllerPath()), fileName, getControllerCode());
    }

    private String getControllerCode() {
        StringBuilder code = new StringBuilder();
        String className = "public class %s%s {\n\n".formatted(tableDTO.getTable(), propertyDTO.getControllerSuffix());

        code.append(getPackageName(propertyDTO.getUrlProject(), propertyDTO.getControllerPath()))
            .append(getImports())
            .append(getHeader())
            .append(className)
            .append("\t@Autowired\n")
            .append("\tprivate ").append(tableDTO.getTable()).append(propertyDTO.getServiceSuffix()).append(" service;\n\n")
            .append(getCrudMethods())
            .append("}");

        return code.toString();
    }

    private String getImports() {
        StringBuilder imports = new StringBuilder();
        imports.append(SPRING_ANNOTATION.getFormattedImport())
               .append(AUTOWIRED.getFormattedImport())
               .append(RESPONSE_ENTITY.getFormattedImport())
               .append(LIST.getFormattedImport())
               .append("import ").append(convertDirectoryToPackage(getDirectory(propertyDTO.getUrlProject(), propertyDTO.getServicePath())))
               .append(".").append(tableDTO.getTable()).append(propertyDTO.getServiceSuffix()).append(";\n");

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
        imports.append(getImportsIdLine(tableDTO.getColumns())).append("\n");

        return imports.toString();
    }

    private String getHeader() {
        StringBuilder header = new StringBuilder();
        header.append("@RestController\n")
              .append("@RequestMapping(\"")
              .append(getControllerName())
              .append("\")\n");

        return header.toString();
    }

    private String getControllerName() {
        String controlerTable = parseCamelCaseToSnakeCase(tableDTO.getTable());
        if (isNull(apiVersion) || apiVersion.isEmpty()) {
            return controlerTable;
        }
        if (apiVersion.endsWith("/")) {
            return apiVersion + controlerTable;
        }
        return "%s/%s".formatted(apiVersion, controlerTable);
    }

    private String getCrudMethods() {
        StringBuilder crudMethods = new StringBuilder();
        crudMethods.append(getCreateMethod())
                   .append(getFindByIdMethod())
                   .append(getDeleteMethod())
                   .append(getUpdateMethod())
                   .append(getFindAllMethod());
        return crudMethods.toString();
    }

    private String getCreateMethod() {
        if (!tableDTO.hasPersist(entitiesId)) {
            return "";
        }

        StringBuilder createMethod = new StringBuilder();
        String objectNameLowerCase = lowerCaseFirstLetter(tableDTO.getTable());
        String methodName = "\tpublic ResponseEntity<%s%s> %s(@RequestBody %s%s %s%s) {\n".formatted(
                tableDTO.getTable(), propertyDTO.getResponseDTOSuffix(), propertyDTO.getFunctionCreate(), tableDTO.getTable(),
                propertyDTO.getPersistDTOSuffix(), objectNameLowerCase, propertyDTO.getPersistDTOSuffix());

        createMethod.append("\t@PostMapping%s\n".formatted(getEndpointName(propertyDTO.getEndpointCreate())))
                    .append(methodName)

                    .append("\t\treturn ResponseEntity.ok(service.%s(".formatted(propertyDTO.getFunctionCreate()))
                    .append(objectNameLowerCase).append(propertyDTO.getPersistDTOSuffix()).append("));\n")
                    .append("\t}\n\n");

        return createMethod.toString();
    }

    private String getFindByIdMethod() {
        StringBuilder findByIdMethod = new StringBuilder();
        String methodName = "\tpublic ResponseEntity<%s%s> %s(@PathVariable %s id) {\n".formatted(
                tableDTO.getTable(), propertyDTO.getResponseDTOSuffix(), propertyDTO.getFunctionFindById(),
                tableDTO.getIdType());

        findByIdMethod.append("\t@GetMapping%s\n".formatted(getEndpointNameWithId(propertyDTO.getEndpointFindById())))
                      .append(methodName)
                      .append("\t\treturn ResponseEntity.ok(service.%s(id));\n".formatted(propertyDTO.getFunctionFindById()))
                      .append("\t}\n\n");

        return findByIdMethod.toString();
    }

    private String getDeleteMethod() {
        StringBuilder deleteMethod = new StringBuilder();
        String methodName = "\tpublic ResponseEntity<Void> %s(@PathVariable %s id) {\n".formatted(
                propertyDTO.getFunctionDelete(), tableDTO.getIdType());

        deleteMethod.append("\t@DeleteMapping%s\n".formatted(getEndpointNameWithId(propertyDTO.getEndpointDelete())))
                    .append(methodName)
                    .append("\t\tservice.%s(id);\n".formatted(propertyDTO.getFunctionDelete()))
                    .append("\t\treturn ResponseEntity.ok().build();\n")
                    .append("\t}\n\n");

        return deleteMethod.toString();
    }

    private String getUpdateMethod() {
        if (!tableDTO.hasUpdate()) {
            return "";
        }

        StringBuilder updateMethod = new StringBuilder();
        String objectNameLowerCase = lowerCaseFirstLetter(tableDTO.getTable());
        String methodName = "\tpublic ResponseEntity<%s%s> %s(@RequestBody %s%s %s%s) {\n".formatted(
                tableDTO.getTable(), propertyDTO.getResponseDTOSuffix(), propertyDTO.getFunctionUpdate(),
                tableDTO.getTable(), propertyDTO.getUpdateDTOSuffix(), objectNameLowerCase, propertyDTO.getUpdateDTOSuffix());

        updateMethod.append("\t@PutMapping%s\n".formatted(getEndpointName(propertyDTO.getEndpointUpdate())))
                    .append(methodName)
                    .append("\t\treturn ResponseEntity.ok(service.%s(".formatted(propertyDTO.getFunctionUpdate()))
                    .append(objectNameLowerCase).append(propertyDTO.getUpdateDTOSuffix()).append("));\n")
                    .append("\t}\n\n");

        return updateMethod.toString();
    }

    private String getFindAllMethod() {
        StringBuilder findAllMethod = new StringBuilder();
        String methodName = "\tpublic ResponseEntity<List<%s%s>> %s() {\n"
                .formatted(tableDTO.getTable(), propertyDTO.getResponseDTOSuffix(), propertyDTO.getFunctionFindAll());

        findAllMethod.append("\t@GetMapping%s\n".formatted(getEndpointName(propertyDTO.getEndpointFindAll())))
                     .append(methodName)
                     .append("\t\treturn ResponseEntity.ok(service.%s());\n".formatted(propertyDTO.getFunctionFindAll()))
                     .append("\t}\n\n");
        return findAllMethod.toString();
    }

    private String getEndpointName(String endpointName) {
        if (isNull(endpointName) || endpointName.isEmpty()) {
            return "";
        }
        return "(\"%s\")".formatted(endpointName);
    }

    private String getEndpointNameWithId(String endpointName) {
        if (isNull(endpointName) || endpointName.isEmpty()) {
            return "(\"{id}\")";
        }
        return "(\"%s/{id}\")".formatted(endpointName);
    }
}
