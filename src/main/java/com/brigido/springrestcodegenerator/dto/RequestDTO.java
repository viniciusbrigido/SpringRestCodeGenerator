package com.brigido.springrestcodegenerator.dto;

import java.util.List;

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
        return tables;
    }

    public void setTables(List<TableDTO> tables) {
        this.tables = tables;
    }

    public List<EnumDTO> getEnums() {
        return enums;
    }

    public void setEnums(List<EnumDTO> enums) {
        this.enums = enums;
    }
}
