package com.brigido.springrestcodegenerator.dto;

import java.util.*;
import static com.brigido.springrestcodegenerator.util.StringUtil.lowerCaseFirstLetter;
import static java.util.Objects.*;
import static java.util.stream.Collectors.*;
import static java.util.stream.Stream.*;

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

    public List<ColumnDTO> getColumnsPersist(Map<String, String> entitiesId) {
        List<ColumnDTO> columnsPersist = columns.stream()
                .filter(columnDTO -> !columnDTO.isPrimaryKey() && !columnDTO.isCollection() && !columnDTO.hasCardinality())
                .toList();

        return concat(columnsPersist.stream(), getExternalColumns(entitiesId).stream()).collect(toList());
    }

    private List<ColumnDTO> getExternalColumns(Map<String, String> entitiesId) {
        List<ColumnDTO> externalColumns = new ArrayList<>();
        entitiesId.forEach((table, primaryKey) -> columns.forEach(columnDTO -> {
            if (table.equals(columnDTO.getType())) {
                ColumnDTO column = new ColumnDTO();
                column.setType(primaryKey);
                column.setName(lowerCaseFirstLetter(table) + "Id");
                externalColumns.add(column);
            }
        }));

        return externalColumns;
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

    public boolean hasPersist(Map<String, String> entitiesId) {
        return !getColumnsPersist(entitiesId).isEmpty();
    }

    public boolean hasNotNull() {
        return columns.stream().anyMatch(ColumnDTO::isNotNull);
    }

    public boolean hasNotEmpty() {
        return columns.stream().anyMatch(ColumnDTO::isNotEmpty);
    }

    public boolean hasMin() {
        return columns.stream().anyMatch(columnDTO -> nonNull(columnDTO.getMin()));
    }

    public boolean hasMax() {
        return columns.stream().anyMatch(columnDTO -> nonNull(columnDTO.getMax()));
    }

    public String getIdType() {
        return columns.stream()
                .filter(ColumnDTO::isPrimaryKey)
                .map(ColumnDTO::getType)
                .findFirst()
                .orElse("");
    }
}
