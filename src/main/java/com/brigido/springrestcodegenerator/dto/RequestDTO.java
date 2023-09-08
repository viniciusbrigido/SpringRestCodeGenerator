package com.brigido.springrestcodegenerator.dto;

import java.util.*;
import static java.util.Optional.*;

public class RequestDTO {

    private String apiVersion;
    private List<TableDTO> tables;
    private List<EnumDTO> enums;

    public RequestDTO() {
    }

    public RequestDTO(String apiVersion, List<TableDTO> tables, List<EnumDTO> enums) {
        this.apiVersion = apiVersion;
        this.tables = tables;
        this.enums = enums;
    }

    public String getApiVersion() {
        return apiVersion;
    }

    public void setApiVersion(String apiVersion) {
        this.apiVersion = apiVersion;
    }

    public List<TableDTO> getTables() {
        return ofNullable(tables).orElseGet(() -> tables = new ArrayList<>());
    }

    public void setTables(List<TableDTO> tables) {
        this.tables = tables;
    }

    public List<EnumDTO> getEnums() {
        return ofNullable(enums).orElseGet(() -> enums = new ArrayList<>());
    }

    public void setEnums(List<EnumDTO> enums) {
        this.enums = enums;
    }
}
