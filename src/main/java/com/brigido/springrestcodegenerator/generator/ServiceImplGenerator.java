package com.brigido.springrestcodegenerator.generator;

import com.brigido.springrestcodegenerator.dto.PropertyDTO;
import com.brigido.springrestcodegenerator.dto.TableDTO;
import java.io.IOException;
import java.util.Map;
import static com.brigido.springrestcodegenerator.enumeration.Imports.*;
import static com.brigido.springrestcodegenerator.util.ConstUtil.VALIDATE;
import static com.brigido.springrestcodegenerator.util.StringUtil.*;

public class ServiceImplGenerator extends BaseGenerator {

    private PropertyDTO propertyDTO;
    private TableDTO tableDTO;
    private Map<String, String> entitiesId;

    public void create(PropertyDTO propertyDTO, TableDTO tableDTO, Map<String, String> entitiesId) throws IOException {
        this.propertyDTO = propertyDTO;
        this.tableDTO = tableDTO;
        this.entitiesId = entitiesId;

        String fileName = tableDTO.getTable() + propertyDTO.getServiceImplSuffix() + ".java";
        createFile(getDirectory(propertyDTO.getUrlProject(), propertyDTO.getServiceImplPath()), fileName, getServiceImplCode());
    }

    private String getServiceImplCode() {
        StringBuilder code = new StringBuilder();
        String className = "public class %s%s implements %s%s {\n\n"
                .formatted(tableDTO.getTable(), propertyDTO.getServiceImplSuffix(), tableDTO.getTable(), propertyDTO.getServiceSuffix());

        code.append(getPackageName(propertyDTO.getUrlProject(), propertyDTO.getServiceImplPath()))
            .append(getImports())
            .append("\n")
            .append("@Service\n")
            .append(className)
            .append(getCrudMethods())
            .append("}");

        return code.toString();
    }

    private String getImports() {
        StringBuilder imports = new StringBuilder();
        imports.append(SERVICE.getFormattedImport())
               .append(AUTOWIRED.getFormattedImport())
               .append(LIST.getFormattedImport())
               .append(COLLECTORS.getFormattedImport())
               .append(MODEL_MAPPER.getFormattedImport())
               .append("import ").append(convertDirectoryToPackage(getDirectory(propertyDTO.getUrlProject(), propertyDTO.getServicePath())))
               .append(".").append(tableDTO.getTable()).append(propertyDTO.getServiceSuffix()).append(";\n")
               .append("import ").append(convertDirectoryToPackage(getDirectory(propertyDTO.getUrlProject(), propertyDTO.getRepositoryPath())))
               .append(".").append(tableDTO.getTable()).append(propertyDTO.getRepositorySuffix()).append(";\n");

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
        imports.append("import ").append(convertDirectoryToPackage(getDirectory(propertyDTO.getUrlProject(), propertyDTO.getEntityPath())))
               .append(".").append(tableDTO.getTable()).append(propertyDTO.getEntitySuffix()).append(";\n")
               .append(getImportsIdLine(tableDTO.getColumns()));

        return imports.toString();
    }

    private String getCrudMethods() {
        StringBuilder crudMethods = new StringBuilder();
        crudMethods.append("\t@Autowired\n")
                   .append("\tprivate ")
                   .append(tableDTO.getTable()).append(propertyDTO.getRepositorySuffix()).append(" repository;\n\n")
                   .append("\t@Autowired\n")
                   .append("\tprivate ModelMapper modelMapper;\n\n");

        if (tableDTO.hasPersist(entitiesId)) {
            crudMethods.append(getCreateMethod());
        }

        crudMethods.append(getFindByIdMethod())
                   .append(getFindByIdDTOMethod())
                   .append(getDeleteMethod(tableDTO.getIdType()));

        if (tableDTO.hasUpdate()) {
            crudMethods.append(getUpdateMethod());
        }
        crudMethods.append(getFindAllMethod())
                   .append(getToResponseDTOMethod());

        return crudMethods.toString();
    }

    private String getCreateMethod() {
        if (!tableDTO.hasPersist(entitiesId)) {
            return "";
        }

        StringBuilder createMethod = new StringBuilder();
        String objectNameLowerCase = lowerCaseFirstLetter(tableDTO.getTable());
        String methodName = "\tpublic %s%s %s(%s%s %s%s) {\n".formatted(
                tableDTO.getTable(), propertyDTO.getResponseDTOSuffix(), propertyDTO.getFunctionCreate(), tableDTO.getTable(), propertyDTO.getPersistDTOSuffix(),
                objectNameLowerCase, propertyDTO.getPersistDTOSuffix());

        createMethod.append("\t@Override\n")
                    .append(methodName)

                    .append("\t\t").append(tableDTO.getTable()).append(propertyDTO.getEntitySuffix()).append(" ").append(objectNameLowerCase)
                    .append(" = modelMapper.map(").append(objectNameLowerCase)
                    .append(propertyDTO.getPersistDTOSuffix()).append(", ").append(tableDTO.getTable()).append(propertyDTO.getEntitySuffix()).append(".class);\n")

                    .append("\t\treturn to%s(repository.save(".formatted(propertyDTO.getResponseDTOSuffix())).append(objectNameLowerCase)
                    .append("));\n")
                    .append("\t}\n\n");
        return createMethod.toString();
    }

    private String getFindByIdMethod() {
        StringBuilder findByIdMethod = new StringBuilder();
        String methodName = "\tpublic %s%s %s%s(%s id) {\n"
                .formatted(tableDTO.getTable(), propertyDTO.getEntitySuffix(),
                        propertyDTO.getFunctionFindById(), VALIDATE, tableDTO.getIdType());

        findByIdMethod.append("\t@Override\n")
                      .append(methodName)

                      .append("\t\treturn repository.findById(id)\n")
                      .append("\t\t\t.orElseThrow(() -> new RuntimeException(\"Not found.\"));\n")
                      .append("\t}\n\n");

        return findByIdMethod.toString();
    }

    private String getFindByIdDTOMethod() {
        StringBuilder findByIdMethodDTO = new StringBuilder();
        String methodName = "\tpublic %s%s %s(%s id) {\n"
                .formatted(tableDTO.getTable(), propertyDTO.getResponseDTOSuffix(),
                        propertyDTO.getFunctionFindById(), tableDTO.getIdType());

        findByIdMethodDTO.append("\t@Override\n")
                         .append(methodName)

                         .append("\t\treturn to%s(%s%s(id));\n".formatted(propertyDTO.getResponseDTOSuffix(),
                                 propertyDTO.getFunctionFindById(), VALIDATE))
                         .append("\t}\n\n");

        return findByIdMethodDTO.toString();
    }

    private String getFindAllMethod() {
        StringBuilder findAllMethod = new StringBuilder();
        String methodName = "\tpublic List<%s%s> %s() {\n"
                .formatted(tableDTO.getTable(), propertyDTO.getResponseDTOSuffix(), propertyDTO.getFunctionFindAll());

        findAllMethod.append("\t@Override\n")
                     .append(methodName)

                     .append("\t\treturn repository.findAll()\n")
                     .append("\t\t\t.stream()\n")
                     .append("\t\t\t.map(this::to%s)\n".formatted(propertyDTO.getResponseDTOSuffix()))
                     .append("\t\t\t.collect(Collectors.toList());\n")
                     .append("\t}\n\n");

        return findAllMethod.toString();
    }

    private String getUpdateMethod() {
        StringBuilder updateMethod = new StringBuilder();
        String objectNameLowerCase = lowerCaseFirstLetter(tableDTO.getTable());
        String methodName = "\tpublic %s%s %s(%s%s %s%s) {\n".formatted(
                tableDTO.getTable(), propertyDTO.getResponseDTOSuffix(), propertyDTO.getFunctionUpdate(),
                tableDTO.getTable(), propertyDTO.getUpdateDTOSuffix(),
                objectNameLowerCase, propertyDTO.getUpdateDTOSuffix());

        updateMethod.append("\t@Override\n")
                    .append(methodName)

                    .append("\t\t").append(tableDTO.getTable()).append(propertyDTO.getEntitySuffix()).append(" ").append(objectNameLowerCase)
                    .append(" = %s%s(".formatted(propertyDTO.getFunctionFindById(), VALIDATE)).append(objectNameLowerCase)
                    .append(propertyDTO.getUpdateDTOSuffix()).append(".get%s());\n".formatted(capitalizeFirstLetter(tableDTO.getIdName())))

                    .append("\t\t")
                    .append(objectNameLowerCase).append(".update(")
                    .append(objectNameLowerCase).append(propertyDTO.getUpdateDTOSuffix()).append(");\n")

                    .append("\t\treturn to%s(repository.save(".formatted(propertyDTO.getResponseDTOSuffix())).append(objectNameLowerCase).append("));\n")
                    .append("\t}\n\n");

        return updateMethod.toString();
    }

    private String getDeleteMethod(String idType) {
        StringBuilder deleteMethod = new StringBuilder();
        String methodName = "\tpublic void delete(%s id) {\n".formatted(idType);

        deleteMethod.append("\t@Override\n")
                    .append(methodName)
                    .append("\t\trepository.deleteById(id);\n")
                    .append("\t}\n\n");

        return deleteMethod.toString();
    }

    private String getToResponseDTOMethod() {
        StringBuilder toResponseDTO = new StringBuilder();
        String objectNameLowerCase = lowerCaseFirstLetter(tableDTO.getTable());
        String methodName = "\tprivate %s%s to%s(%s%s %s) {\n"
                .formatted(tableDTO.getTable(), propertyDTO.getResponseDTOSuffix(),
                        propertyDTO.getResponseDTOSuffix(), tableDTO.getTable(),
                        propertyDTO.getEntitySuffix(), objectNameLowerCase);

        toResponseDTO.append(methodName)
                     .append("\t\treturn modelMapper.map(").append(objectNameLowerCase).append(", ").append(tableDTO.getTable())
                     .append(propertyDTO.getResponseDTOSuffix()).append(".class);\n")
                     .append("\t}\n\n");

        return toResponseDTO.toString();
    }
}
