package com.brigido.springrestcodegenerator.generator;

import com.brigido.springrestcodegenerator.dto.PropertyDTO;
import com.brigido.springrestcodegenerator.dto.TableDTO;
import java.io.IOException;
import static com.brigido.springrestcodegenerator.enumeration.Imports.*;
import static com.brigido.springrestcodegenerator.util.StringUtil.*;

public class ControllerGenerator extends BaseGenerator {

    private PropertyDTO propertyDTO;
    private TableDTO tableDTO;

    public void create(PropertyDTO propertyDTO, TableDTO tableDTO) throws IOException {
        this.propertyDTO = propertyDTO;
        this.tableDTO = tableDTO;

        String fileName = tableDTO.getTable() + propertyDTO.getControllerSuffix() + ".java";
        createFile(getDirectory(propertyDTO.getUrlProject(), propertyDTO.getControllerPath()), fileName, getControllerCode());
    }

    private String getControllerCode() {
        StringBuilder code = new StringBuilder();
        String className = "public class %s%s {\n\n".formatted(tableDTO.getTable(), propertyDTO.getControllerSuffix());

        code.append(getPackageName(propertyDTO.getUrlProject(), propertyDTO.getControllerPath()))
            .append(getImports())
            .append(getHeader(tableDTO.getTable()))
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
               .append(".").append(tableDTO.getTable()).append(propertyDTO.getServiceSuffix()).append(";\n")
               .append("import ").append(convertDirectoryToPackage(getDirectory(propertyDTO.getUrlProject(), propertyDTO.getPersistDTOPath())))
               .append(".").append(tableDTO.getTable()).append(propertyDTO.getPersistDTOSuffix()).append(";\n")
               .append("import ").append(convertDirectoryToPackage(getDirectory(propertyDTO.getUrlProject(), propertyDTO.getResponseDTOPath())))
               .append(".").append(tableDTO.getTable()).append(propertyDTO.getResponseDTOSuffix()).append(";\n");

        if (tableDTO.hasUpdate()) {
            imports.append("import ").append(convertDirectoryToPackage(getDirectory(propertyDTO.getUrlProject(), propertyDTO.getUpdateDTOPath())))
                   .append(".").append(tableDTO.getTable()).append(propertyDTO.getUpdateDTOSuffix()).append(";\n");
        }
        imports.append(getImportsIdLine(tableDTO.getColumns())).append("\n");

        return imports.toString();
    }

    private String getHeader(String table) {
        StringBuilder header = new StringBuilder();
        header.append("@RestController\n")
              .append("@RequestMapping(\"")
              .append(parseCamelCaseToSnakeCase(table))
              .append("\")\n");

        return header.toString();
    }

    private String getCrudMethods() {
        StringBuilder crudMethods = new StringBuilder();
        crudMethods.append(getCreateMethod(tableDTO.getTable()))
                   .append(getFindByIdMethod())
                   .append(getDeleteMethod())
                   .append(getUpdateMethod())
                   .append(getFindAllMethod(tableDTO.getTable()));
        return crudMethods.toString();
    }

    private String getCreateMethod(String table) {
        StringBuilder createMethod = new StringBuilder();
        String objectNameLowerCase = lowerCaseFirstLetter(table);
        String methodName = "\tpublic ResponseEntity<%s%s> create(@RequestBody %s%s %s%s) {\n".formatted(
                table, propertyDTO.getResponseDTOSuffix(), table, propertyDTO.getPersistDTOSuffix(),
                objectNameLowerCase, propertyDTO.getPersistDTOSuffix());

        createMethod.append("\t@PostMapping(\"create\")\n")
                    .append(methodName)

                    .append("\t\treturn ResponseEntity.ok(service.create(")
                    .append(objectNameLowerCase).append(propertyDTO.getPersistDTOSuffix()).append("));\n")
                    .append("\t}\n\n");

        return createMethod.toString();
    }

    private String getFindByIdMethod() {
        StringBuilder findByIdMethod = new StringBuilder();
        String methodName = "\tpublic ResponseEntity<%s%s> findById(@PathVariable %s id) {\n".formatted(
                tableDTO.getTable(), propertyDTO.getResponseDTOSuffix(), tableDTO.getIdType());

        findByIdMethod.append("\t@GetMapping(\"{id}\")\n")
                      .append(methodName)
                      .append("\t\treturn ResponseEntity.ok(service.findByIdDTO(id));\n")
                      .append("\t}\n\n");

        return findByIdMethod.toString();
    }

    private String getDeleteMethod() {
        StringBuilder deleteMethod = new StringBuilder();
        String methodName = "\tpublic ResponseEntity<Void> delete(@PathVariable %s id) {\n".formatted(tableDTO.getIdType());

        deleteMethod.append("\t@DeleteMapping(\"{id}\")\n")
                    .append(methodName)
                    .append("\t\tservice.delete(id);\n")
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
        String methodName = "\tpublic ResponseEntity<%s%s> update(@RequestBody %s%s %s%s) {\n".formatted(
                tableDTO.getTable(), propertyDTO.getResponseDTOSuffix(), tableDTO.getTable(), propertyDTO.getUpdateDTOSuffix(),
                objectNameLowerCase, propertyDTO.getUpdateDTOSuffix());

        updateMethod.append("\t@PutMapping(\"update\")\n")
                    .append(methodName)
                    .append("\t\treturn ResponseEntity.ok(service.update(")
                    .append(objectNameLowerCase).append(propertyDTO.getUpdateDTOSuffix()).append("));\n")
                    .append("\t}\n\n");

        return updateMethod.toString();
    }

    private String getFindAllMethod(String table) {
        StringBuilder findAllMethod = new StringBuilder();
        String methodName = "\tpublic ResponseEntity<List<%s%s>> findAll() {\n"
                .formatted(table, propertyDTO.getResponseDTOSuffix());

        findAllMethod.append("\t@GetMapping\n")
                    .append(methodName)
                    .append("\t\treturn ResponseEntity.ok(service.findAll());\n")
                    .append("\t}\n\n");
        return findAllMethod.toString();
    }
}
