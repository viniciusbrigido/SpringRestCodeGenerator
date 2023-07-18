package com.brigido.springrestcodegenerator.generator;

import com.brigido.springrestcodegenerator.dto.PropertyDTO;
import com.brigido.springrestcodegenerator.dto.TableDTO;
import java.io.IOException;
import static com.brigido.springrestcodegenerator.enumeration.Imports.*;

public class ServiceImplGenerator extends BaseGenerator {

    private String directory;

    public void create(PropertyDTO propertyDTO, TableDTO tableDTO, String directory) throws IOException {
        setPropertyDTO(propertyDTO);
        this.directory = directory;

        String fileName = tableDTO.getTable() + "ServiceImpl.java";
        createFile(getServiceImplDirectory(directory), fileName, getServiceImplCode(tableDTO));
    }

    private String getServiceImplCode(TableDTO tableDTO) {
        StringBuilder code = new StringBuilder();
        String className = "public class %sServiceImpl implements %sService {\n\n".formatted(tableDTO.getTable(), tableDTO.getTable());

        code.append(getPackageName(getServiceImplDirectory(directory)))
            .append(getImports(tableDTO))
            .append("\n")
            .append("@Service\n")
            .append(className)
            .append(getCrudMethods(tableDTO))
            .append("}");

        return code.toString();
    }

    private String getImports(TableDTO tableDTO) {
        StringBuilder imports = new StringBuilder();
        imports.append(SERVICE.getFormattedImport())
               .append(AUTOWIRED.getFormattedImport())
               .append(LIST.getFormattedImport())
               .append(COLLECTORS.getFormattedImport())
               .append(MODEL_MAPPER.getFormattedImport())
               .append("import ").append(convertDirectoryToPackage(getServiceDirectory(directory)))
               .append(".").append(tableDTO.getTable()).append("Service;\n")
               .append("import ").append(convertDirectoryToPackage(getRepositoryDirectory(directory)))
               .append(".").append(tableDTO.getTable()).append("Repository;\n")
               .append("import ").append(convertDirectoryToPackage(getDTODirectory(directory)))
               .append(".").append(tableDTO.getTable()).append("PersistDTO;\n")
               .append("import ").append(convertDirectoryToPackage(getDTODirectory(directory)))
               .append(".").append(tableDTO.getTable()).append("ResponseDTO;\n");

        if (tableDTO.hasUpdate()) {
            imports.append("import ").append(convertDirectoryToPackage(getDTODirectory(directory)))
                   .append(".").append(tableDTO.getTable()).append("UpdateDTO;\n");
        }
        imports.append("import ").append(convertDirectoryToPackage(getEntityDirectory(directory)))
               .append(".").append(tableDTO.getTable()).append(";\n")
               .append(getImportsIdLine(tableDTO.getColumns()));

        return imports.toString();
    }

    private String getCrudMethods(TableDTO tableDTO) {
        StringBuilder crudMethods = new StringBuilder();
        crudMethods.append("\t@Autowired\n")
                   .append("\tprivate ").append(tableDTO.getTable()).append("Repository repository;\n\n")
                   .append("\t@Autowired\n")
                   .append("\tprivate ModelMapper modelMapper;\n\n")
                   .append(getCreateMethod(tableDTO.getTable()))
                   .append(getFindByIdMethod(tableDTO))
                   .append(getFindByIdDTOMethod(tableDTO))
                   .append(getDeleteMethod(tableDTO.getIdType()));

        if (tableDTO.hasUpdate()) {
            crudMethods.append(getUpdateMethod(tableDTO.getTable()));
        }
        crudMethods.append(getFindAllMethod(tableDTO.getTable()))
                   .append(getToResponseDTOMethod(tableDTO.getTable()));

        return crudMethods.toString();
    }

    private String getCreateMethod(String table) {
        StringBuilder createMethod = new StringBuilder();
        String objectNameLowerCase = lowerCaseFirstLetter(table);
        String methodName = "\tpublic %sResponseDTO create(%sPersistDTO %sPersistDTO) {\n".formatted(
                table, table, objectNameLowerCase);

        createMethod.append("\t@Override\n")
                    .append(methodName)

                    .append("\t\t").append(table).append(" ").append(objectNameLowerCase)
                    .append(" = modelMapper.map(").append(objectNameLowerCase)
                    .append("PersistDTO, ").append(table).append(".class);\n")

                    .append("\t\treturn toResponseDTO(repository.save(").append(objectNameLowerCase)
                    .append("));\n")
                    .append("\t}\n\n");
        return createMethod.toString();
    }

    private String getFindByIdMethod(TableDTO tableDTO) {
        StringBuilder findByIdMethod = new StringBuilder();
        String methodName = "\tpublic %s findById(%s id) {\n".formatted(tableDTO.getTable(), tableDTO.getIdType());

        findByIdMethod.append("\t@Override\n")
                      .append(methodName)

                      .append("\t\treturn repository.findById(id)\n")
                      .append("\t\t\t.orElseThrow(() -> new RuntimeException(\"Not found.\"));\n")
                      .append("\t}\n\n");

        return findByIdMethod.toString();
    }

    private String getFindByIdDTOMethod(TableDTO tableDTO) {
        StringBuilder findByIdMethodDTO = new StringBuilder();
        String methodName = "\tpublic %sResponseDTO findByIdDTO(%s id) {\n".formatted(tableDTO.getTable(), tableDTO.getIdType());

        findByIdMethodDTO.append("\t@Override\n")
                         .append(methodName)

                         .append("\t\treturn toResponseDTO(findById(id));\n")
                         .append("\t}\n\n");

        return findByIdMethodDTO.toString();
    }

    private String getFindAllMethod(String table) {
        StringBuilder findAllMethod = new StringBuilder();
        String methodName = "\tpublic List<%sResponseDTO> findAll() {\n".formatted(table);

        findAllMethod.append("\t@Override\n")
                     .append(methodName)

                     .append("\t\treturn repository.findAll()\n")
                     .append("\t\t\t.stream()\n")
                     .append("\t\t\t.map(this::toResponseDTO)\n")
                     .append("\t\t\t.collect(Collectors.toList());\n")
                     .append("\t}\n\n");

        return findAllMethod.toString();
    }

    private String getUpdateMethod(String table) {
        StringBuilder updateMethod = new StringBuilder();
        String objectNameLowerCase = lowerCaseFirstLetter(table);
        String methodName = "\tpublic %sResponseDTO update(%sUpdateDTO %sUpdateDTO) {\n".formatted(
                table, table, objectNameLowerCase);

        updateMethod.append("\t@Override\n")
                    .append(methodName)

                    .append("\t\t").append(table).append(" ").append(objectNameLowerCase)
                    .append(" = findById(").append(objectNameLowerCase).append("UpdateDTO.getId());\n")

                    .append("\t\t")
                    .append(objectNameLowerCase).append(".update(")
                    .append(objectNameLowerCase).append("UpdateDTO);\n")

                    .append("\t\treturn toResponseDTO(repository.save(").append(objectNameLowerCase).append("));\n")
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

    private String getToResponseDTOMethod(String table) {
        StringBuilder toResponseDTO = new StringBuilder();
        String objectNameLowerCase = lowerCaseFirstLetter(table);
        String methodName = "\tprivate %sResponseDTO toResponseDTO(%s %s) {\n".formatted(table, table, objectNameLowerCase);

        toResponseDTO.append(methodName)
                     .append("\t\treturn modelMapper.map(").append(objectNameLowerCase).append(", ").append(table).append("ResponseDTO.class);\n")
                     .append("\t}\n\n");

        return toResponseDTO.toString();
    }
}
