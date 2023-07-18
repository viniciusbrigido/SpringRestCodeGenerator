package com.brigido.springrestcodegenerator.dto;

public class ColumnDTO {

    private String name;
    private String type;
    private Integer length;
    private boolean primaryKey;
    private boolean required;
    private boolean updatable;
    private boolean list;
    private boolean unique;
    private String generationType;
    private String cardinality;

    public ColumnDTO() {
    }

    public ColumnDTO(String name, String type, Integer length, boolean primaryKey, boolean required, boolean updatable,
                     boolean list, boolean unique, String generationType, String cardinality) {
        this.name = name;
        this.type = type;
        this.length = length;
        this.primaryKey = primaryKey;
        this.required = required;
        this.updatable = updatable;
        this.list = list;
        this.unique = unique;
        this.generationType = generationType;
        this.cardinality = cardinality;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getType() {
        return type;
    }

    public void setType(String type) {
        this.type = type;
    }

    public Integer getLength() {
        return length;
    }

    public void setLength(Integer length) {
        this.length = length;
    }

    public boolean isPrimaryKey() {
        return primaryKey;
    }

    public void setPrimaryKey(boolean primaryKey) {
        this.primaryKey = primaryKey;
    }

    public boolean isRequired() {
        return required;
    }

    public void setRequired(boolean required) {
        this.required = required;
    }

    public boolean isUpdatable() {
        return updatable;
    }

    public void setUpdatable(boolean updatable) {
        this.updatable = updatable;
    }

    public boolean isList() {
        return list;
    }

    public void setList(boolean list) {
        this.list = list;
    }

    public boolean isUnique() {
        return unique;
    }

    public void setUnique(boolean unique) {
        this.unique = unique;
    }

    public String getGenerationType() {
        return generationType;
    }

    public void setGenerationType(String generationType) {
        this.generationType = generationType;
    }

    public String getCardinality() {
        return cardinality;
    }

    public void setCardinality(String cardinality) {
        this.cardinality = cardinality;
    }
}
