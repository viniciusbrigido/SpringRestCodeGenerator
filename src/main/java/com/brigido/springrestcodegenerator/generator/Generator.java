package com.brigido.springrestcodegenerator.generator;

import com.brigido.springrestcodegenerator.dto.*;
import com.brigido.springrestcodegenerator.enumeration.*;
import com.brigido.springrestcodegenerator.exception.SyntaxErrorException;
import com.brigido.springrestcodegenerator.generator.dto.*;
import com.google.gson.Gson;
import java.io.*;
import java.util.*;
import static java.util.Objects.*;
import static java.util.stream.Collectors.*;

public class Generator {

    public void generate(PropertyDTO propertyDTO) throws IOException {
        validateFiles(propertyDTO);

        RequestDTO requestDTO = getRequestDTO(propertyDTO);
        validateRequestDTO(requestDTO);

        Map<String, String> entitiesId = getEntitiesId(requestDTO.getTables());
        List<String> enums = getEnums(requestDTO.getEnums());

        for (TableDTO tableDTO : requestDTO.getTables()) {
            new EntityGenerator().create(propertyDTO, tableDTO, enums);
            new RepositoryGenerator().create(propertyDTO, tableDTO);
            new ServiceGenerator().create(propertyDTO, tableDTO);
            new ServiceImplGenerator().create(propertyDTO, tableDTO);
            new ControllerGenerator().create(propertyDTO, tableDTO);
            new ResponseDTOGenerator().create(propertyDTO, tableDTO, enums);
            new PersistDTOGenerator().create(propertyDTO, tableDTO, entitiesId, enums);
            new UpdateDTOGenerator().create(propertyDTO, tableDTO, enums);
        }

        for (EnumDTO enumDTO : requestDTO.getEnums()) {
            new EnumGenerator().create(propertyDTO, enumDTO);
        }
    }

    private void validateFiles(PropertyDTO propertyDTO) throws FileNotFoundException {
        if (!new File(propertyDTO.getUrlProject()).exists()) {
            throw new FileNotFoundException("Pasta Principal do Projeto não encontrada.");
        }
        if (!new File(propertyDTO.getPathClass()).exists()) {
            throw new FileNotFoundException("Arquivo de Geração não encontrado.");
        }
    }

    private RequestDTO getRequestDTO(PropertyDTO propertyDTO) throws FileNotFoundException {
        try (FileReader fileReader = new FileReader(propertyDTO.getPathClass())) {
            return new Gson().fromJson(fileReader, RequestDTO.class);
        } catch (Exception e) {
            throw new FileNotFoundException("Erro de sintaxe na geração do(s) arquivo(s).");
        }
    }

    private void validateRequestDTO(RequestDTO requestDTO) throws SyntaxErrorException {
        validateTables(requestDTO.getTables());
        validateEnums(requestDTO.getEnums());
    }

    private void validateTables(List<TableDTO> tables) throws SyntaxErrorException {
        for (TableDTO tableDTO : tables) {
            if (isNull(tableDTO.getTable()) || tableDTO.getTable().isEmpty()) {
                throw new SyntaxErrorException("Há entidades sem nome.");
            }
            if (isNull(tableDTO.getColumns()) || tableDTO.getColumns().isEmpty()) {
                throw new SyntaxErrorException("A entidade %s não tem colunas.".formatted(tableDTO.getTable()));
            }
            if (tableDTO.getIdType().isEmpty()) {
                throw new SyntaxErrorException("A entidade %s não possui uma coluna de chave primária.".formatted(tableDTO.getTable()));
            }

            for (ColumnDTO columnDTO : tableDTO.getColumns()) {
                if (isNull(columnDTO.getName()) || columnDTO.getName().isEmpty()) {
                    throw new SyntaxErrorException("A entidade %s possui colunas sem nome.".formatted(tableDTO.getTable()));
                }
                if (isNull(columnDTO.getType()) || columnDTO.getType().isEmpty()) {
                    throw new SyntaxErrorException("A coluna %s da entidade %s não possui tipo."
                            .formatted(columnDTO.getName(), tableDTO.getTable()));
                }
                if (columnDTO.hasEnumType() && EnumType.getEnumType(columnDTO.getEnumType()).isEmpty()) {
                    throw new SyntaxErrorException("O tipo de enumeração da coluna %s da entidade %s é inválido."
                            .formatted(columnDTO.getName(), tableDTO.getTable()));
                }
                if (columnDTO.hasCardinality() && Cardinality.getAnottation(columnDTO.getCardinality(), null).isEmpty()) {
                    throw new SyntaxErrorException("A cardinalidade da coluna %s da entidade %s é inválido."
                            .formatted(columnDTO.getName(), tableDTO.getTable()));
                }
                if (columnDTO.hasGenerationType() && GenerationType.getAnottation(columnDTO.getGenerationType()).isEmpty()) {
                    throw new SyntaxErrorException("O tipo de geração da coluna %s da entidade %s é inválido."
                            .formatted(columnDTO.getName(), tableDTO.getTable()));
                }
                if (columnDTO.hasCascadeType() && CascadeType.getCascadeType(columnDTO.getCascadeType()).isEmpty()) {
                    throw new SyntaxErrorException("O tipo de cascata da coluna %s da entidade %s é inválido."
                            .formatted(columnDTO.getName(), tableDTO.getTable()));
                }
                if (columnDTO.hasTemporalType() && TemporalType.getTemporalType(columnDTO.getTemporalType()).isEmpty()) {
                    throw new SyntaxErrorException("O tipo de tempo da coluna %s da entidade %s é inválido."
                            .formatted(columnDTO.getName(), tableDTO.getTable()));
                }
            }
        }
    }

    private void validateEnums(List<EnumDTO> enums) throws SyntaxErrorException {
        for (EnumDTO enumDTO : enums) {
            if (isNull(enumDTO.getName()) || enumDTO.getName().isEmpty()) {
                throw new SyntaxErrorException("Há enumerações sem nome.");
            }
            if (isNull(enumDTO.getValues()) || enumDTO.getValues().isEmpty()) {
                throw new SyntaxErrorException("A enumeração %s não tem valores.".formatted(enumDTO.getName()));
            }
        }
    }

    private Map<String, String> getEntitiesId(List<TableDTO> tables) {
        Map<String, String> entitiesId = new HashMap<>();
        tables.forEach(table -> entitiesId.put(table.getTable(), table.getIdType()));
        return entitiesId;
    }

    private List<String> getEnums(List<EnumDTO> enums) {
        return enums.stream().map(EnumDTO::getName).collect(toList());
    }
}
