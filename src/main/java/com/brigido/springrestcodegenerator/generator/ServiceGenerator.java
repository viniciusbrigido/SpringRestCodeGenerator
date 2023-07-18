package com.brigido.springrestcodegenerator.generator;

import com.brigido.springrestcodegenerator.dto.PropertyDTO;
import com.brigido.springrestcodegenerator.dto.TableDTO;
import java.io.IOException;
import static com.brigido.springrestcodegenerator.enumeration.Imports.*;
import static java.lang.String.format;

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
        String interfaceName = format("public interface %sService {\n\n", tableDTO.getTable());

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

        String methodCreateName = format("\t%sResponseDTO create(%sPersistDTO %sPersistDTO);\n",
                tableDTO.getTable(), tableDTO.getTable(), objectNameLowerCase);

        String methodFindByIdName = format("\t%s findById(%s id);\n",
                tableDTO.getTable(), tableDTO.getIdType());

        String methodFindByIdDTOName = format("\t%sResponseDTO findByIdDTO(%s id);\n",
                tableDTO.getTable(), tableDTO.getIdType());

        String methodDeleteName = format("\tvoid delete(%s id);\n", tableDTO.getIdType());

        crudMethods.append(methodCreateName)
                   .append(methodFindByIdName)
                   .append(methodFindByIdDTOName)
                   .append(methodDeleteName);

        if (tableDTO.hasUpdate()) {
            String methodUpdateName = format("\t%sResponseDTO update(%sUpdateDTO %sUpdateDTO);\n",
                    tableDTO.getTable(), tableDTO.getTable(), objectNameLowerCase);

            crudMethods.append(methodUpdateName);
        }

        String methodFindAllName = format("\tList<%sResponseDTO> findAll();\n", tableDTO.getTable());
        crudMethods.append(methodFindAllName);

        return crudMethods.toString();
    }
}
