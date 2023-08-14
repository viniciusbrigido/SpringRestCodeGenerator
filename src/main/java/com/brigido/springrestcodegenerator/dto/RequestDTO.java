package com.brigido.springrestcodegenerator.dto;

import java.util.*;
import static java.util.Optional.*;

public class RequestDTO {

    private List<TableDTO> tables;
    private List<EnumDTO> enums;

    public RequestDTO() {
    }

    public RequestDTO(List<TableDTO> tables, List<EnumDTO> enums) {
        this.tables = tables;
        this.enums = enums;
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
