package com.brigido.springrestcodegenerator.generator;

import com.brigido.springrestcodegenerator.dto.*;
import com.brigido.springrestcodegenerator.enumeration.Imports;
import java.io.*;
import java.util.*;
import static com.brigido.springrestcodegenerator.enumeration.Imports.*;
import static java.lang.Character.*;
import static java.lang.String.*;
import static java.lang.String.format;
import static java.util.Arrays.*;
import static java.util.stream.Collectors.*;

public abstract class BaseGenerator {

    private PropertyDTO propertyDTO;

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
        return format("package %s;\n\n", convertDirectoryToPackage(directory));
    }

    public String parseCamelCaseToSnakeCase(String input) {
        StringBuilder output = new StringBuilder();
        for (int i = 0; i < input.length(); i++) {
            char currentChar = input.charAt(i);
            if (isUpperCase(currentChar)) {
                if (i > 0) {
                    output.append('_');
                }
                output.append(toLowerCase(currentChar));
            } else {
                output.append(currentChar);
            }
        }
        return output.toString();
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
        switch (importEnum) {
            case DATE: return configEntityDTO.isContainsDate();
            case UUID: return configEntityDTO.isContainsUUID();
            case BIG_INTEGER: return configEntityDTO.isContainsBigInteger();
            case BIG_DECIMAL: return configEntityDTO.isContainsBigDecimal();
            case LOCAL_DATE: return configEntityDTO.isContainsLocalDate();
            case LOCAL_DATE_TIME: return configEntityDTO.isContainsLocalDateTime();
            case LIST: return configEntityDTO.isContainsList();
            default: return false;
        }
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
        }
        return configEntityDTO;
    }

    public String getLombokHeader() {
        if (!propertyDTO.isUseLombok()) {
            return "";
        }
        return "@Getter @Setter\n@AllArgsConstructor @NoArgsConstructor\n";
    }

    public String getLombokImport() {
        if (!propertyDTO.isUseLombok()) {
            return "";
        }
        return LOMBOK.getFormattedImport();
    }

    public String getSerializableImplements() {
        if (!propertyDTO.isUseSerializable()) {
            return "";
        }
        return "implements Serializable ";
    }

    public String getSerializableImport() {
        if (!propertyDTO.isUseSerializable()) {
            return "";
        }
        return SERIALIZABLE.getFormattedImport();
    }

    public String getConstructors(TableDTO tableDTO, String sufix) {
        if (propertyDTO.isUseLombok()) {
            return "";
        }

        StringBuilder constructors = new StringBuilder();

        String emptyConstructor = format("\tpublic %s%s() {\n\t}\n\n", tableDTO.getTable(), sufix);
        constructors.append(emptyConstructor);

        List<String> properties = new ArrayList<>();
        List<String> setterLines = new ArrayList<>();
        for (ColumnDTO columnDTO : tableDTO.getColumns()) {
            properties.add(format("%s %s", columnDTO.getType(), columnDTO.getName()));
            setterLines.add(format("\t\tthis.%s = %s;\n", columnDTO.getName(), columnDTO.getName()));
        }

        String fullConstructor = format("\tpublic %s(%s) {\n", tableDTO.getTable(), join(", ", properties));
        constructors.append(fullConstructor);
        setterLines.forEach(constructors::append);
        constructors.append("\t}\n\n");

        return constructors.toString();
    }

    public String getGettersSetters(TableDTO tableDTO) {
        if (propertyDTO.isUseLombok()) {
            return "";
        }

        StringBuilder gettersSetters = new StringBuilder();
        for (ColumnDTO columnDTO : tableDTO.getColumns()) {
            String methodGetName = format("\tpublic %s get%s() {\n", columnDTO.getType(), capitalizeFirstLetter(columnDTO.getName()));
            gettersSetters.append(methodGetName)
                          .append("\t\treturn ").append(columnDTO.getName()).append(";\n")
                          .append("\t}\n\n");

            String methodSetName = format("\tpublic void set%s(%s %s) {\n", capitalizeFirstLetter(columnDTO.getName()), columnDTO.getType(), columnDTO.getName());
            gettersSetters.append(methodSetName)
                          .append("\t\tthis.").append(columnDTO.getName()).append(" = ").append(columnDTO.getName()).append(";\n")
                          .append("\t}\n\n");
        }
        return gettersSetters.toString();
    }

    public String[] getTokens(String line) {
        return line.trim().split("\\s+");
    }

    public static String capitalizeFirstLetter(String value) {
        char firstChar = toUpperCase(value.charAt(0));
        return firstChar + value.substring(1);
    }

    public static String lowerCaseFirstLetter(String value) {
        char firstChar = toLowerCase(value.charAt(0));
        return firstChar + value.substring(1);
    }

    public String getControllerName() {
        return capitalizeFirstLetter(propertyDTO.getPackageController());
    }

    public String getControllerDirectory(String directory) {
        return format("%s/%s", directory, propertyDTO.getPackageController());
    }

    public String getDTODirectory(String directory) {
        return format("%s/dto", directory);
    }

    public String getEntityDirectory(String directory) {
        return format("%s/%s", directory, propertyDTO.getPackageEntity());
    }

    public String getRepositoryDirectory(String directory) {
        return format("%s/repository", directory);
    }

    public String getServiceDirectory(String directory) {
        return format("%s/service", directory);
    }

    public String getServiceImplDirectory(String directory) {
        return format("%s/service/impl", directory);
    }

    public String getEnumerationDirectory(String directory) {
        return format("%s/enumeration", directory);
    }

    public PropertyDTO getPropertyDTO() {
        return propertyDTO;
    }

    public void setPropertyDTO(PropertyDTO propertyDTO) {
        this.propertyDTO = propertyDTO;
    }
}
