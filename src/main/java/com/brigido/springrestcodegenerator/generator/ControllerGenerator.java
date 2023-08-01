package com.brigido.springrestcodegenerator.generator;

import com.brigido.springrestcodegenerator.dto.PropertyDTO;
import com.brigido.springrestcodegenerator.dto.TableDTO;
import java.io.IOException;
import static com.brigido.springrestcodegenerator.enumeration.Imports.*;
import static com.brigido.springrestcodegenerator.util.StringUtil.*;

public class ControllerGenerator extends BaseGenerator {

    private String directory;

    public void create(PropertyDTO propertyDTO, TableDTO tableDTO, String directory) throws IOException {
        setPropertyDTO(propertyDTO);
        this.directory = directory;

        String fileName = "%s%s.java".formatted(tableDTO.getTable(), getControllerName());
        createFile(getControllerDirectory(directory), fileName, getControllerCode(tableDTO));
    }

    private String getControllerCode(TableDTO tableDTO) {
        StringBuilder code = new StringBuilder();
        String className = "public class %s%s {\n\n".formatted(tableDTO.getTable(), getControllerName());

        code.append(getPackageName(getControllerDirectory(directory)))
            .append(getImports(tableDTO))
            .append(getHeader(tableDTO.getTable()))
            .append(className)
            .append("\t@Autowired\n")
            .append("\tprivate ").append(tableDTO.getTable()).append("Service service;\n\n")
            .append(getCrudMethods(tableDTO))
            .append("}");

        return code.toString();
    }

    private String getImports(TableDTO tableDTO) {
        StringBuilder imports = new StringBuilder();
        imports.append(SPRING_ANNOTATION.getFormattedImport())
               .append(AUTOWIRED.getFormattedImport())
               .append(RESPONSE_ENTITY.getFormattedImport())
               .append(LIST.getFormattedImport())
               .append("import ").append(convertDirectoryToPackage(getServiceDirectory(directory)))
               .append(".").append(tableDTO.getTable()).append("Service;\n")
               .append("import ").append(convertDirectoryToPackage(getDTODirectory(directory)))
               .append(".").append(tableDTO.getTable()).append("PersistDTO;\n")
               .append("import ").append(convertDirectoryToPackage(getDTODirectory(directory)))
               .append(".").append(tableDTO.getTable()).append("ResponseDTO;\n");

        if (tableDTO.hasUpdate()) {
            imports.append("import ").append(convertDirectoryToPackage(getDTODirectory(directory)))
                   .append(".").append(tableDTO.getTable()).append("UpdateDTO;\n");
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

    private String getCrudMethods(TableDTO tableDTO) {
        StringBuilder crudMethods = new StringBuilder();
        crudMethods.append(getCreateMethod(tableDTO.getTable()))
                   .append(getFindByIdMethod(tableDTO))
                   .append(getDeleteMethod(tableDTO))
                   .append(getUpdateMethod(tableDTO))
                   .append(getFindAllMethod(tableDTO.getTable()));
        return crudMethods.toString();
    }

    private String getCreateMethod(String table) {
        StringBuilder createMethod = new StringBuilder();
        String objectNameLowerCase = lowerCaseFirstLetter(table);
        String methodName = "\tpublic ResponseEntity<%sResponseDTO> create(@RequestBody %sPersistDTO %sPersistDTO) {\n".formatted(
                table, table, objectNameLowerCase);

        createMethod.append("\t@PostMapping(\"create\")\n")
                    .append(methodName)

                    .append("\t\treturn ResponseEntity.ok(service.create(")
                    .append(objectNameLowerCase).append("PersistDTO));\n")
                    .append("\t}\n\n");

        return createMethod.toString();
    }

    private String getFindByIdMethod(TableDTO tableDTO) {
        StringBuilder findByIdMethod = new StringBuilder();
        String methodName = "\tpublic ResponseEntity<%sResponseDTO> findById(@PathVariable %s id) {\n".formatted(
                tableDTO.getTable(), tableDTO.getIdType());

        findByIdMethod.append("\t@GetMapping(\"{id}\")\n")
                      .append(methodName)
                      .append("\t\treturn ResponseEntity.ok(service.findByIdDTO(id));\n")
                      .append("\t}\n\n");

        return findByIdMethod.toString();
    }

    private String getDeleteMethod(TableDTO tableDTO) {
        StringBuilder deleteMethod = new StringBuilder();
        String methodName = "\tpublic ResponseEntity<Void> delete(@PathVariable %s id) {\n".formatted(tableDTO.getIdType());

        deleteMethod.append("\t@DeleteMapping(\"{id}\")\n")
                    .append(methodName)
                    .append("\t\tservice.delete(id);\n")
                    .append("\t\treturn ResponseEntity.ok().build();\n")
                    .append("\t}\n\n");

        return deleteMethod.toString();
    }

    private String getUpdateMethod(TableDTO tableDTO) {
        if (!tableDTO.hasUpdate()) {
            return "";
        }

        StringBuilder updateMethod = new StringBuilder();
        String objectNameLowerCase = lowerCaseFirstLetter(tableDTO.getTable());
        String methodName = "\tpublic ResponseEntity<%sResponseDTO> update(@RequestBody %sUpdateDTO %sUpdateDTO) {\n".formatted(
                tableDTO.getTable(), tableDTO.getTable(), objectNameLowerCase);

        updateMethod.append("\t@PutMapping(\"update\")\n")
                    .append(methodName)
                    .append("\t\treturn ResponseEntity.ok(service.update(")
                    .append(objectNameLowerCase).append("UpdateDTO));\n")
                    .append("\t}\n\n");

        return updateMethod.toString();
    }

    private String getFindAllMethod(String table) {
        StringBuilder findAllMethod = new StringBuilder();
        String methodName = "\tpublic ResponseEntity<List<%sResponseDTO>> findAll() {\n".formatted(table);

        findAllMethod.append("\t@GetMapping\n")
                    .append(methodName)
                    .append("\t\treturn ResponseEntity.ok(service.findAll());\n")
                    .append("\t}\n\n");
        return findAllMethod.toString();
    }
}
