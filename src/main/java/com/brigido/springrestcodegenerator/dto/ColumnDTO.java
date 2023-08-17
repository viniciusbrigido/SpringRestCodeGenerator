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
    private Boolean set;
    private Boolean unique;
    private String generationType;
    private String cardinality;
    private String enumType;
    private String mappedBy;
    private String orderBy;
    private String cascadeType;
    private String temporalType;
    private Boolean notNull;
    private Boolean notEmpty;
    private Integer max;
    private Integer min;

    public ColumnDTO() {
    }

    public ColumnDTO(String name, String type, Integer length, Boolean primaryKey, Boolean required, Boolean updatable,
                     Boolean list, Boolean set, Boolean unique, String generationType, String cardinality, String enumType,
                     String mappedBy, String orderBy, String cascadeType, String temporalType, Boolean notNull,
                     Boolean notEmpty, Integer max, Integer min) {
        this.name = name;
        this.type = type;
        this.length = length;
        this.primaryKey = primaryKey;
        this.required = required;
        this.updatable = updatable;
        this.list = list;
        this.set = set;
        this.unique = unique;
        this.generationType = generationType;
        this.cardinality = cardinality;
        this.enumType = enumType;
        this.mappedBy = mappedBy;
        this.orderBy = orderBy;
        this.cascadeType = cascadeType;
        this.temporalType = temporalType;
        this.notNull = notNull;
        this.notEmpty = notEmpty;
        this.max = max;
        this.min = min;
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

    public Boolean isSet() {
        return requireNonNullElse(set, false);
    }

    public void setSet(Boolean set) {
        this.set = set;
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

    public String getOrderBy() {
        return orderBy;
    }

    public void setOrderBy(String orderBy) {
        this.orderBy = orderBy;
    }

    public String getCascadeType() {
        return cascadeType;
    }

    public void setCascadeType(String cascadeType) {
        this.cascadeType = cascadeType;
    }

    public String getTemporalType() {
        return temporalType;
    }

    public void setTemporalType(String temporalType) {
        this.temporalType = temporalType;
    }

    public Boolean isNotNull() {
        return requireNonNullElse(notNull, false);
    }

    public void setNotNull(Boolean notNull) {
        this.notNull = notNull;
    }

    public Boolean isNotEmpty() {
        return requireNonNullElse(notEmpty, false);
    }

    public void setNotEmpty(Boolean notEmpty) {
        this.notEmpty = notEmpty;
    }

    public Integer getMax() {
        return max;
    }

    public void setMax(Integer max) {
        this.max = max;
    }

    public Integer getMin() {
        return min;
    }

    public void setMin(Integer min) {
        this.min = min;
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

    public boolean hasOrderBy() {
        return nonNull(orderBy) && !orderBy.isEmpty();
    }

    public boolean hasCascadeType() {
        return nonNull(cascadeType) && !cascadeType.isEmpty();
    }

    public boolean hasTemporalType() {
        return nonNull(temporalType) && !temporalType.isEmpty();
    }

    public boolean isCollection() {
        return isList() || isSet();
    }
}
