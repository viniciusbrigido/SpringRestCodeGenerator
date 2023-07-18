package com.brigido.springrestcodegenerator.generator;

import com.brigido.springrestcodegenerator.dto.PropertyDTO;
import com.brigido.springrestcodegenerator.dto.TableDTO;
import java.io.IOException;
import static com.brigido.springrestcodegenerator.enumeration.Imports.*;
import static java.lang.String.*;

public class RepositoryGenerator extends BaseGenerator {

    private String directory;

    public void create(PropertyDTO propertyDTO, TableDTO tableDTO, String directory) throws IOException {
        setPropertyDTO(propertyDTO);
        this.directory = directory;

        String fileName = tableDTO.getTable() + "Repository.java";
        createFile(getRepositoryDirectory(directory), fileName, getRepositoryCode(tableDTO));
    }

    private String getRepositoryCode(TableDTO tableDTO) {
        StringBuilder code = new StringBuilder();
        String interfaceName = format("public interface %sRepository extends JpaRepository<%s, %s> {\n}",
                tableDTO.getTable(), tableDTO.getTable(), tableDTO.getIdType());

        code.append(getPackageName(getRepositoryDirectory(directory)))
            .append(getImports(tableDTO))
            .append("\n")
            .append("@Repository\n")
            .append(interfaceName);

        return code.toString();
    }

    private String getImports(TableDTO tableDTO) {
        StringBuilder imports = new StringBuilder();
        imports.append(getImportsIdLine(tableDTO.getColumns()))
               .append(JPA_REPOSITORY.getFormattedImport())
               .append(REPOSITORY.getFormattedImport())
               .append("import ").append(convertDirectoryToPackage(getEntityDirectory(directory)))
               .append(".").append(tableDTO.getTable()).append(";\n");
        return imports.toString();
    }
}
