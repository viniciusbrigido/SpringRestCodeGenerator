package com.brigido.springrestcodegenerator.generator;

import com.brigido.springrestcodegenerator.dto.*;
import com.brigido.springrestcodegenerator.generator.dto.*;
import com.google.gson.Gson;
import java.io.*;
import java.util.*;
import static java.util.stream.Collectors.*;

public class Generator {

    public void generate(PropertyDTO propertyDTO) throws IOException {
        validateFiles(propertyDTO);

        String directory = propertyDTO.getUrlProject();
        RequestDTO requestDTO = getRequestDTO(propertyDTO);
        Map<String, String> entitiesId = getEntitiesId(requestDTO.getTables());
        List<String> enums = getEnums(requestDTO.getEnums());

        for (TableDTO tableDTO : requestDTO.getTables()) {
            new EntityGenerator().create(propertyDTO, tableDTO, directory, enums);
            new RepositoryGenerator().create(propertyDTO, tableDTO, directory);
            new ServiceGenerator().create(propertyDTO, tableDTO, directory);
            new ServiceImplGenerator().create(propertyDTO, tableDTO, directory);
            new ControllerGenerator().create(propertyDTO, tableDTO, directory);
            new ResponseDTOGenerator().create(propertyDTO, tableDTO, directory, enums);
            new PersistDTOGenerator().create(propertyDTO, tableDTO, directory, entitiesId, enums);
            new UpdateDTOGenerator().create(propertyDTO, tableDTO, directory, enums);
        }

        for (EnumDTO enumDTO : requestDTO.getEnums()) {
            new EnumGenerator().create(propertyDTO, enumDTO, directory);
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

    private Map<String, String> getEntitiesId(List<TableDTO> tables) {
        Map<String, String> entitiesId = new HashMap<>();
        tables.forEach(table -> entitiesId.put(table.getTable(), table.getIdType()));
        return entitiesId;
    }

    private List<String> getEnums(List<EnumDTO> enums) {
        return enums.stream().map(EnumDTO::getName).collect(toList());
    }
}
