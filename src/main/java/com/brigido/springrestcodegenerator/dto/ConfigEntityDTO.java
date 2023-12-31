package com.brigido.springrestcodegenerator.dto;

public class ConfigEntityDTO {

    private boolean containsDate;
    private boolean containsUUID;
    private boolean containsBigInteger;
    private boolean containsBigDecimal;
    private boolean containsLocalDate;
    private boolean containsLocalDateTime;
    private boolean containsList;
    private boolean containsSet;
    private boolean containsCalendar;

    public ConfigEntityDTO() {
    }

    public ConfigEntityDTO(boolean containsDate, boolean containsUUID, boolean containsBigInteger, boolean containsBigDecimal,
                           boolean containsLocalDate, boolean containsLocalDateTime, boolean containsList,
                           boolean containsSet, boolean containsCalendar) {
        this.containsDate = containsDate;
        this.containsUUID = containsUUID;
        this.containsBigInteger = containsBigInteger;
        this.containsBigDecimal = containsBigDecimal;
        this.containsLocalDate = containsLocalDate;
        this.containsLocalDateTime = containsLocalDateTime;
        this.containsList = containsList;
        this.containsSet = containsSet;
        this.containsCalendar = containsCalendar;
    }

    public boolean isContainsDate() {
        return containsDate;
    }

    public void setContainsDate(boolean containsDate) {
        this.containsDate = containsDate;
    }

    public boolean isContainsUUID() {
        return containsUUID;
    }

    public void setContainsUUID(boolean containsUUID) {
        this.containsUUID = containsUUID;
    }

    public boolean isContainsBigInteger() {
        return containsBigInteger;
    }

    public void setContainsBigInteger(boolean containsBigInteger) {
        this.containsBigInteger = containsBigInteger;
    }

    public boolean isContainsBigDecimal() {
        return containsBigDecimal;
    }

    public void setContainsBigDecimal(boolean containsBigDecimal) {
        this.containsBigDecimal = containsBigDecimal;
    }

    public boolean isContainsLocalDate() {
        return containsLocalDate;
    }

    public void setContainsLocalDate(boolean containsLocalDate) {
        this.containsLocalDate = containsLocalDate;
    }

    public boolean isContainsLocalDateTime() {
        return containsLocalDateTime;
    }

    public void setContainsLocalDateTime(boolean containsLocalDateTime) {
        this.containsLocalDateTime = containsLocalDateTime;
    }

    public boolean isContainsList() {
        return containsList;
    }

    public void setContainsList(boolean containsList) {
        this.containsList = containsList;
    }

    public boolean isContainsSet() {
        return containsSet;
    }

    public void setContainsSet(boolean containsSet) {
        this.containsSet = containsSet;
    }

    public boolean isContainsCalendar() {
        return containsCalendar;
    }

    public void setContainsCalendar(boolean containsCalendar) {
        this.containsCalendar = containsCalendar;
    }
}
