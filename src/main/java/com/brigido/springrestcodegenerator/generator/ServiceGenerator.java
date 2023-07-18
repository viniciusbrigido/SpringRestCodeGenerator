package com.brigido.springrestcodegenerator.generator;

import com.brigido.springrestcodegenerator.dto.PropertyDTO;
import com.brigido.springrestcodegenerator.dto.TableDTO;
import java.io.IOException;
import static com.brigido.springrestcodegenerator.enumeration.Imports.*;

public class ServiceGenerator extends BaseGenerator {

    private String directory;

    public void create(PropertyDTO propertyDTO, TableDTO tableDTO, String directory) throws IOException {
        setPropertyDTO(propertyDTO);
        this.directory = directory;

        String fileName = tableDTO.getTable() + "Service.java";
        createFile(getServiceDirectory(directory), fileName, getServiceCode(tableDTO));
    }

    private String getServiceCode(TableDTO tableDTO) {
        StringBuilder code = new StringBuilder();
        String interfaceName = "public interface %sService {\n\n".formatted(tableDTO.getTable());

        code.append(getPackageName(getServiceDirectory(directory)))
            .append(getImports(tableDTO))
            .append(interfaceName)
            .append(getCrudMethods(tableDTO))
            .append("}");

        return code.toString();
    }

    private String getImports(TableDTO tableDTO) {
        StringBuilder imports = new StringBuilder();
        imports.append("import ").append(convertDirectoryToPackage(getDTODirectory(directory)))
               .append(".").append(tableDTO.getTable()).append("PersistDTO;\n")
               .append("import ").append(convertDirectoryToPackage(getDTODirectory(directory)))
               .append(".").append(tableDTO.getTable()).append("ResponseDTO;\n");

        if (tableDTO.hasUpdate()) {
            imports.append("import ").append(convertDirectoryToPackage(getDTODirectory(directory)))
                   .append(".").append(tableDTO.getTable()).append("UpdateDTO;\n");
        }
        imports.append("import ").append(convertDirectoryToPackage(getEntityDirectory(directory)))
               .append(".").append(tableDTO.getTable()).append(";\n")
               .append(LIST.getFormattedImport())
               .append(getImportsIdLine(tableDTO.getColumns()))
               .append("\n");

        return imports.toString();
    }

    private String getCrudMethods(TableDTO tableDTO) {
        StringBuilder crudMethods = new StringBuilder();
        String objectNameLowerCase = lowerCaseFirstLetter(tableDTO.getTable());

        String methodCreateName = "\t%sResponseDTO create(%sPersistDTO %sPersistDTO);\n".formatted(
                tableDTO.getTable(), tableDTO.getTable(), objectNameLowerCase);

        String methodFindByIdName = "\t%s findById(%s id);\n".formatted(
                tableDTO.getTable(), tableDTO.getIdType());

        String methodFindByIdDTOName = "\t%sResponseDTO findByIdDTO(%s id);\n".formatted(
                tableDTO.getTable(), tableDTO.getIdType());

        String methodDeleteName = "\tvoid delete(%s id);\n".formatted(tableDTO.getIdType());

        crudMethods.append(methodCreateName)
                   .append(methodFindByIdName)
                   .append(methodFindByIdDTOName)
                   .append(methodDeleteName);

        if (tableDTO.hasUpdate()) {
            String methodUpdateName = "\t%sResponseDTO update(%sUpdateDTO %sUpdateDTO);\n".formatted(
                    tableDTO.getTable(), tableDTO.getTable(), objectNameLowerCase);

            crudMethods.append(methodUpdateName);
        }

        String methodFindAllName = "\tList<%sResponseDTO> findAll();\n".formatted(tableDTO.getTable());
        crudMethods.append(methodFindAllName);

        return crudMethods.toString();
    }
}
