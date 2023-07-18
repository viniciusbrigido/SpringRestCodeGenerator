package com.brigido.springrestcodegenerator.dto;

import java.util.List;
import java.util.Optional;
import static java.util.stream.Collectors.*;

public class TableDTO {

    private String table;
    private List<ColumnDTO> columns;

    public TableDTO() {
    }

    public TableDTO(String table, List<ColumnDTO> columns) {
        this.table = table;
        this.columns = columns;
    }

    public String getTable() {
        return table;
    }

    public void setTable(String table) {
        this.table = table;
    }

    public List<ColumnDTO> getColumns() {
        return columns;
    }

    public void setColumns(List<ColumnDTO> columns) {
        this.columns = columns;
    }

    public List<ColumnDTO> getColumnsPersist() {
        return columns.stream().filter(columnDTO -> !columnDTO.isPrimaryKey()).collect(toList());
    }

    public List<ColumnDTO> getColumnsUpdate() {
        return columns.stream().filter(columnDTO -> columnDTO.isUpdatable() || columnDTO.isPrimaryKey()).collect(toList());
    }

    public boolean hasEnum(List<String> enums) {
        List<String> types = columns.stream().map(ColumnDTO::getType).collect(toList());
        return types.stream()
                .anyMatch(enums::contains);
    }

    public boolean hasUpdate() {
        return columns.stream().anyMatch(ColumnDTO::isUpdatable);
    }

    public boolean hasRequired() {
        return columns.stream().anyMatch(ColumnDTO::isRequired);
    }

    public String getIdType() {
        Optional<ColumnDTO> columnIdOptional = columns.stream().filter(ColumnDTO::isPrimaryKey).findFirst();
        if (columnIdOptional.isPresent()) {
            return columnIdOptional.get().getType();
        }
        return "";
    }
}
