package com.brigido.springrestcodegenerator.dto;

import java.util.List;

public class EnumDTO {

    private String name;
    private List<String> values;

    public EnumDTO() {
    }

    public EnumDTO(String name, List<String> values) {
        this.name = name;
        this.values = values;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public List<String> getValues() {
        return values;
    }

    public void setValues(List<String> values) {
        this.values = values;
    }
}
