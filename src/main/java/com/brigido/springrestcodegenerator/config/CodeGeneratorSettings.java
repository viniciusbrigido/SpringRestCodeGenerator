package com.brigido.springrestcodegenerator.config;

import com.brigido.springrestcodegenerator.dto.PropertyDTO;
import com.google.gson.Gson;
import com.intellij.ide.util.PropertiesComponent;
import static java.util.Objects.*;

public class CodeGeneratorSettings {

    private static final String PROPERTY_DTO_KEY = "codegenerator.propertyDTO";

    private PropertyDTO propertyDTO;

    public PropertyDTO getPropertyDTO() {
        loadSettings();
        return propertyDTO;
    }

    public void setPropertyDTO(PropertyDTO propertyDTO) {
        this.propertyDTO = propertyDTO;
        saveSettings();
    }

    private void saveSettings() {
        PropertiesComponent.getInstance().setValue(PROPERTY_DTO_KEY, new Gson().toJson(propertyDTO));
    }

    public void loadSettings() {
        String serializedDTO = PropertiesComponent.getInstance().getValue(PROPERTY_DTO_KEY);
        if (nonNull(serializedDTO)) {
            propertyDTO = new Gson().fromJson(serializedDTO, PropertyDTO.class);
        }
    }
}
