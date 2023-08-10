package com.brigido.springrestcodegenerator.generator;

import com.brigido.springrestcodegenerator.dto.*;
import com.brigido.springrestcodegenerator.enumeration.Imports;
import java.io.*;
import java.util.*;
import static com.brigido.springrestcodegenerator.enumeration.Imports.*;
import static com.brigido.springrestcodegenerator.util.StringUtil.*;
import static java.lang.String.*;
import static java.util.Arrays.*;
import static java.util.stream.Collectors.*;

public abstract class BaseGenerator {

    public void createFile(String directory, String fileName, String code) throws IOException {
        try {
            createDirectory(directory);
            String filePath = directory + File.separator + fileName;
            FileWriter writer = new FileWriter(filePath);
            writer.write(code);
            writer.close();
        } catch (Exception e) {
            throw new FileNotFoundException("Arquivo de Geração inválido ou não encontrado.");
        }
    }

    public void createDirectory(String directory) {
        File destineDirectory = new File(directory);
        if (!destineDirectory.exists()) {
            destineDirectory.mkdirs();
        }
    }

    public String convertDirectoryToPackage(String directoryPrincipal) {
        String directory = directoryPrincipal.replace("/", ".");
        String packageCommand = "main.java.";
        if (directory.contains(packageCommand)) {
            int startIndex = directory.indexOf(packageCommand) + 10;
            directory = directory.substring(startIndex);
        }
        return directory;
    }

    public String getPackageName(String directory) {
        return "package %s;\n\n".formatted(convertDirectoryToPackage(directory));
    }

    public String getImportsByConfigEntityDTO(List<ColumnDTO> columns) {
        ConfigEntityDTO configEntityDTO = getConfigEntityDTO(columns);
        StringBuilder imports = new StringBuilder();

        stream(Imports.values())
              .filter(importEnum -> checkImport(configEntityDTO, importEnum))
              .map(Imports::getFormattedImport)
              .forEach(imports::append);

        return imports.toString();
    }

    public boolean checkImport(ConfigEntityDTO configEntityDTO, Imports importEnum) {
        return switch (importEnum) {
            case DATE -> configEntityDTO.isContainsDate();
            case UUID -> configEntityDTO.isContainsUUID();
            case BIG_INTEGER -> configEntityDTO.isContainsBigInteger();
            case BIG_DECIMAL -> configEntityDTO.isContainsBigDecimal();
            case LOCAL_DATE -> configEntityDTO.isContainsLocalDate();
            case LOCAL_DATE_TIME -> configEntityDTO.isContainsLocalDateTime();
            case LIST -> configEntityDTO.isContainsList();
            case SET -> configEntityDTO.isContainsSet();
            default -> false;
        };
    }

    public String getImportsIdLine(List<ColumnDTO> columns) {
        List<ColumnDTO> columnsPrimaryKey = columns.stream()
                .filter(ColumnDTO::isPrimaryKey)
                .collect(toList());

        return getImportsByConfigEntityDTO(columnsPrimaryKey);
    }

    public ConfigEntityDTO getConfigEntityDTO(List<ColumnDTO> columns) {
        ConfigEntityDTO configEntityDTO = new ConfigEntityDTO();
        for (ColumnDTO columnDTO : columns) {
            String type = columnDTO.getType();
            if (type.equals(DATE.getName())) {
                configEntityDTO.setContainsDate(true);
            }
            if (type.equals(UUID.getName())) {
                configEntityDTO.setContainsUUID(true);
            }
            if (type.equals(BIG_INTEGER.getName())) {
                configEntityDTO.setContainsBigInteger(true);
            }
            if (type.equals(BIG_DECIMAL.getName())) {
                configEntityDTO.setContainsBigDecimal(true);
            }
            if (type.equals(LOCAL_DATE.getName())) {
                configEntityDTO.setContainsLocalDate(true);
            }
            if (type.equals(LOCAL_DATE_TIME.getName())) {
                configEntityDTO.setContainsLocalDateTime(true);
            }
            if (columnDTO.isList()) {
                configEntityDTO.setContainsList(true);
            }
            if (columnDTO.isSet()) {
                configEntityDTO.setContainsSet(true);
            }
        }
        return configEntityDTO;
    }

    public String getLombokHeader(boolean isUseLombok) {
        if (!isUseLombok) {
            return "";
        }
        return "@Getter @Setter\n@AllArgsConstructor @NoArgsConstructor\n@Builder\n";
    }

    public String getLombokImport(boolean isUseLombok) {
        if (!isUseLombok) {
            return "";
        }
        return LOMBOK.getFormattedImport();
    }

    public String getSerializableImplements(boolean isUseSerializable) {
        if (!isUseSerializable) {
            return "";
        }
        return "implements Serializable ";
    }

    public String getSerializableImport(boolean isUseSerializable) {
        if (!isUseSerializable) {
            return "";
        }
        return SERIALIZABLE.getFormattedImport();
    }

    public String getConstructors(boolean isUseLombok, TableDTO tableDTO, String suffix) {
        if (isUseLombok) {
            return "";
        }

        StringBuilder constructors = new StringBuilder();

        String emptyConstructor = "\tpublic %s%s() {\n\t}\n\n".formatted(tableDTO.getTable(), suffix);
        constructors.append(emptyConstructor);

        List<String> properties = new ArrayList<>();
        List<String> setterLines = new ArrayList<>();
        for (ColumnDTO columnDTO : tableDTO.getColumns()) {
            String typeFormatted = columnDTO.isCollection() ? "%s<%s>"
                    .formatted(columnDTO.isList() ? LIST.getName() : SET.getName(), columnDTO.getType()) : columnDTO.getType();
            properties.add("%s %s".formatted(typeFormatted, columnDTO.getName()));
            setterLines.add("\t\tthis.%s = %s;\n".formatted(columnDTO.getName(), columnDTO.getName()));
        }

        String fullConstructor = "\tpublic %s(%s) {\n".formatted(tableDTO.getTable(), join(", ", properties));
        constructors.append(fullConstructor);
        setterLines.forEach(constructors::append);
        constructors.append("\t}\n\n");

        return constructors.toString();
    }

    public String getGettersSetters(boolean isUseLombok, TableDTO tableDTO) {
        if (isUseLombok) {
            return "";
        }

        StringBuilder gettersSetters = new StringBuilder();
        for (ColumnDTO columnDTO : tableDTO.getColumns()) {
            String typeFormatted = columnDTO.isCollection() ? "%s<%s>".formatted(columnDTO.isList() ? LIST.getName() : SET.getName(), columnDTO.getType()) : columnDTO.getType();
            String methodGetName = "\tpublic %s get%s() {\n".formatted(typeFormatted, capitalizeFirstLetter(columnDTO.getName()));
            gettersSetters.append(methodGetName)
                          .append("\t\treturn ").append(columnDTO.getName()).append(";\n")
                          .append("\t}\n\n");

            String methodSetName = "\tpublic void set%s(%s %s) {\n".formatted(capitalizeFirstLetter(columnDTO.getName()), typeFormatted, columnDTO.getName());
            gettersSetters.append(methodSetName)
                          .append("\t\tthis.").append(columnDTO.getName()).append(" = ").append(columnDTO.getName()).append(";\n")
                          .append("\t}\n\n");
        }
        return gettersSetters.toString();
    }

    public String getControllerName(String packageController) {
        return capitalizeFirstLetter(packageController);
    }

    public String getControllerDirectory(String directory, String packageController) {
        return "%s/%s".formatted(directory, packageController);
    }

    public String getDTODirectory(String directory) {
        return "%s/dto".formatted(directory);
    }

    public String getEntityDirectory(String directory, String packageEntity) {
        return "%s/%s".formatted(directory, packageEntity);
    }

    public String getRepositoryDirectory(String directory) {
        return "%s/repository".formatted(directory);
    }

    public String getServiceDirectory(String directory) {
        return "%s/service".formatted(directory);
    }

    public String getServiceImplDirectory(String directory) {
        return "%s/service/impl".formatted(directory);
    }

    public String getEnumerationDirectory(String directory) {
        return "%s/enumeration".formatted(directory);
    }
}
