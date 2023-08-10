package com.brigido.springrestcodegenerator.generator;

import com.brigido.springrestcodegenerator.dto.PropertyDTO;
import com.brigido.springrestcodegenerator.dto.TableDTO;
import java.io.IOException;
import static com.brigido.springrestcodegenerator.enumeration.Imports.*;

public class RepositoryGenerator extends BaseGenerator {

    private PropertyDTO propertyDTO;
    private TableDTO tableDTO;

    public void create(PropertyDTO propertyDTO, TableDTO tableDTO) throws IOException {
        this.propertyDTO = propertyDTO;
        this.tableDTO = tableDTO;

        String fileName = tableDTO.getTable() + propertyDTO.getRepositorySuffix() + ".java";
        createFile(getRepositoryDirectory(propertyDTO.getUrlProject()), fileName, getRepositoryCode());
    }

    private String getRepositoryCode() {
        StringBuilder code = new StringBuilder();
        String interfaceName = "public interface %s%s extends JpaRepository<%s, %s> {\n}".formatted(
                tableDTO.getTable(), propertyDTO.getRepositorySuffix(), tableDTO.getTable(), tableDTO.getIdType());

        code.append(getPackageName(getRepositoryDirectory(propertyDTO.getUrlProject())))
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
               .append("import ")
               .append(convertDirectoryToPackage(getEntityDirectory(propertyDTO.getUrlProject(), propertyDTO.getPackageEntity())))
               .append(".").append(tableDTO.getTable()).append(";\n");
        return imports.toString();
    }
}
