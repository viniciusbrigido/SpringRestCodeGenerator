package com.brigido.springrestcodegenerator.dto;

import static java.util.Objects.*;

public class ColumnDTO {

    private String name;
    private String type;
    private Integer length;
    private Boolean primaryKey;
    private Boolean required;
    private Boolean updatable;
    private Boolean list;
    private Boolean unique;
    private String generationType;
    private String cardinality;
    private String enumType;
    private String mappedBy;

    public ColumnDTO() {
    }

    public ColumnDTO(String name, String type, Integer length, Boolean primaryKey, Boolean required, Boolean updatable,
                     Boolean list, Boolean unique, String generationType, String cardinality, String enumType, String mappedBy) {
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
        this.enumType = enumType;
        this.mappedBy = mappedBy;
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

    public Boolean isPrimaryKey() {
        return requireNonNullElse(primaryKey, false);
    }

    public void setPrimaryKey(Boolean primaryKey) {
        this.primaryKey = primaryKey;
    }

    public Boolean isRequired() {
        return requireNonNullElse(required, false);
    }

    public void setRequired(Boolean required) {
        this.required = required;
    }

    public Boolean isUpdatable() {
        return requireNonNullElse(updatable, true);
    }

    public void setUpdatable(Boolean updatable) {
        this.updatable = updatable;
    }

    public Boolean isList() {
        return requireNonNullElse(list, false);
    }

    public void setList(Boolean list) {
        this.list = list;
    }

    public Boolean isUnique() {
        return requireNonNullElse(unique, false);
    }

    public void setUnique(Boolean unique) {
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

    public String getEnumType() {
        return enumType;
    }

    public void setEnumType(String enumType) {
        this.enumType = enumType;
    }

    public String getMappedBy() {
        return mappedBy;
    }

    public void setMappedBy(String mappedBy) {
        this.mappedBy = mappedBy;
    }

    public boolean hasCardinality() {
        return nonNull(cardinality) && !cardinality.isEmpty();
    }

    public boolean hasGenerationType() {
        return nonNull(generationType) && !generationType.isEmpty();
    }

    public boolean hasEnumType() {
        return nonNull(enumType) && !enumType.isEmpty();
    }

    public boolean hasMappedBy() {
        return nonNull(mappedBy) && !mappedBy.isEmpty();
    }
}
