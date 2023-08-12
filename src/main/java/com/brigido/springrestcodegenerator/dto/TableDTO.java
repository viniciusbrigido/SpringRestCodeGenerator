package com.brigido.springrestcodegenerator.dto;

import java.util.List;
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
        return columns.stream()
                .filter(columnDTO -> !columnDTO.isPrimaryKey() && !columnDTO.isCollection())
                .collect(toList());
    }

    public List<ColumnDTO> getColumnsUpdate() {
        return columns.stream()
                .filter(columnDTO -> (columnDTO.isUpdatable() || columnDTO.isPrimaryKey()) && !columnDTO.hasCardinality())
                .collect(toList());
    }

    public List<ColumnDTO> getColumnsResponse() {
        return columns.stream()
                .filter(columnDTO -> !columnDTO.hasCardinality())
                .collect(toList());
    }

    public boolean hasEnum(List<String> enums) {
        List<String> types = columns.stream().map(ColumnDTO::getType).toList();
        return types.stream()
                .anyMatch(enums::contains);
    }

    public boolean hasUpdate() {
        return columns.stream()
                .anyMatch(column -> column.isUpdatable() && !column.hasCardinality() && !column.isPrimaryKey());
    }

    public boolean hasPersist() {
        return !getColumnsPersist().isEmpty();
    }

    public boolean hasRequired() {
        return columns.stream().anyMatch(ColumnDTO::isRequired);
    }

    public String getIdType() {
        return columns.stream()
                .filter(ColumnDTO::isPrimaryKey)
                .map(ColumnDTO::getType)
                .findFirst()
                .orElse("");
    }
}
