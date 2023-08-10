package com.brigido.springrestcodegenerator.dialog;

import com.brigido.springrestcodegenerator.config.CodeGeneratorSettings;
import com.brigido.springrestcodegenerator.dto.PropertyDTO;
import com.intellij.openapi.ui.DialogWrapper;
import com.intellij.ui.components.JBLabel;
import com.intellij.ui.components.JBTextField;
import com.intellij.util.ui.JBUI;
import org.jetbrains.annotations.Nullable;
import javax.swing.*;
import java.awt.*;
import static java.util.Objects.*;

public class ExtraSettingsDialog extends DialogWrapper {

    private PropertyDTO propertyDTO;
    private CodeGeneratorDialog codeGeneratorDialog;

    private JBTextField serviceSuffix;
    private JBTextField servicePath;

    private JBTextField serviceImplSuffix;
    private JBTextField serviceImplPath;

    private JBTextField repositorySuffix;
    private JBTextField repositoryPath;

    private JBTextField persistDTOSuffix;
    private JBTextField persistDTOPath;

    private JBTextField updateDTOSuffix;
    private JBTextField updateDTOPath;

    private JBTextField responseDTOSuffix;
    private JBTextField responseDTOPath;

    public ExtraSettingsDialog(CodeGeneratorDialog codeGeneratorDialog) {
        super(true);
        this.codeGeneratorDialog = codeGeneratorDialog;
        setTitle("Configurações Extras");
        init();
    }

    @Nullable
    @Override
    protected JComponent createCenterPanel() {
        JPanel panel = new JPanel(new GridBagLayout());
        GridBagConstraints gbc = new GridBagConstraints();
        gbc.gridx = 0;
        gbc.gridy = 0;
        gbc.weightx = 1.0;
        gbc.fill = GridBagConstraints.HORIZONTAL;
        gbc.insets = JBUI.insets(5);

        panel.add(new JBLabel("Prefixo do Service:"), gbc);
        gbc.gridx = 1;
        panel.add(new JBLabel("Caminho do Service:"), gbc);
        gbc.gridy++;
        gbc.gridx = 0;
        serviceSuffix = new JBTextField();
        panel.add(serviceSuffix, gbc);
        gbc.gridx = 1;
        servicePath = new JBTextField();
        panel.add(servicePath, gbc);

        gbc.gridy++;
        gbc.gridx = 0;
        panel.add(new JBLabel("Prefixo do ServiceImpl:"), gbc);
        gbc.gridx = 1;
        panel.add(new JBLabel("Caminho do ServiceImpl:"), gbc);
        gbc.gridy++;
        gbc.gridx = 0;
        serviceImplSuffix = new JBTextField();
        panel.add(serviceImplSuffix, gbc);
        gbc.gridx = 1;
        serviceImplPath = new JBTextField();
        panel.add(serviceImplPath, gbc);

        gbc.gridy++;
        gbc.gridx = 0;
        panel.add(new JBLabel("Prefixo do Repository:"), gbc);
        gbc.gridx = 1;
        panel.add(new JBLabel("Caminho do Repository:"), gbc);
        gbc.gridy++;
        gbc.gridx = 0;
        repositorySuffix = new JBTextField();
        panel.add(repositorySuffix, gbc);
        gbc.gridx = 1;
        repositoryPath = new JBTextField();
        panel.add(repositoryPath, gbc);

        gbc.gridy++;
        gbc.gridx = 0;
        panel.add(new JBLabel("Prefixo do DTO de Persistência:"), gbc);
        gbc.gridx = 1;
        panel.add(new JBLabel("Caminho do DTO de Persistência:"), gbc);
        gbc.gridy++;
        gbc.gridx = 0;
        persistDTOSuffix = new JBTextField();
        panel.add(persistDTOSuffix, gbc);
        gbc.gridx = 1;
        persistDTOPath = new JBTextField();
        panel.add(persistDTOPath, gbc);

        gbc.gridy++;
        gbc.gridx = 0;
        panel.add(new JBLabel("Prefixo do DTO de Update:"), gbc);
        gbc.gridx = 1;
        panel.add(new JBLabel("Caminho do DTO de Update:"), gbc);
        gbc.gridy++;
        gbc.gridx = 0;
        updateDTOSuffix = new JBTextField();
        panel.add(updateDTOSuffix, gbc);
        gbc.gridx = 1;
        updateDTOPath = new JBTextField();
        panel.add(updateDTOPath, gbc);

        gbc.gridy++;
        gbc.gridx = 0;
        panel.add(new JBLabel("Prefixo do DTO de Response:"), gbc);
        gbc.gridx = 1;
        panel.add(new JBLabel("Caminho do DTO de Response:"), gbc);
        gbc.gridy++;
        gbc.gridx = 0;
        responseDTOSuffix = new JBTextField();
        panel.add(responseDTOSuffix, gbc);
        gbc.gridx = 1;
        responseDTOPath = new JBTextField();
        panel.add(responseDTOPath, gbc);

        panel.setPreferredSize(new Dimension(530, panel.getPreferredSize().height));
        loadProperties();
        return panel;
    }


    private void loadProperties() {
        propertyDTO = new CodeGeneratorSettings().getPropertyDTO();
        if (isNull(propertyDTO)) {
            propertyDTO = new PropertyDTO();
        }

    }

    @Override
    protected void doOKAction() {
        codeGeneratorDialog.setExtraSettings(getPropertyDTO());
    }

    private PropertyDTO getPropertyDTO() {
        if (isNull(propertyDTO)) {
            propertyDTO = new PropertyDTO();
        }

        propertyDTO.setServiceSuffix(getTextFieldValue(serviceSuffix));
        propertyDTO.setServicePath(getTextFieldValue(servicePath));
        propertyDTO.setServiceImplSuffix(getTextFieldValue(serviceImplSuffix));
        propertyDTO.setServiceImplPath(getTextFieldValue(serviceImplPath));
        propertyDTO.setRepositorySuffix(getTextFieldValue(repositorySuffix));
        propertyDTO.setRepositoryPath(getTextFieldValue(repositoryPath));
        propertyDTO.setPersistDTOSuffix(getTextFieldValue(persistDTOSuffix));
        propertyDTO.setPersistDTOPath(getTextFieldValue(persistDTOPath));
        propertyDTO.setUpdateDTOSuffix(getTextFieldValue(updateDTOSuffix));
        propertyDTO.setUpdateDTOPath(getTextFieldValue(updateDTOPath));
        propertyDTO.setResponseDTOSuffix(getTextFieldValue(responseDTOSuffix));
        propertyDTO.setResponseDTOPath(getTextFieldValue(responseDTOPath));

        return propertyDTO;
    }

    private String getTextFieldValue(JBTextField textField) {
        if (nonNull(textField) && nonNull(textField.getText()) && !textField.getText().isEmpty()) {
            return textField.getText();
        }
        return null;
    }

    public JBTextField getServiceSuffix() {
        return serviceSuffix;
    }

    public JBTextField getServicePath() {
        return servicePath;
    }

    public JBTextField getServiceImplSuffix() {
        return serviceImplSuffix;
    }

    public JBTextField getServiceImplPath() {
        return serviceImplPath;
    }

    public JBTextField getRepositorySuffix() {
        return repositorySuffix;
    }

    public JBTextField getRepositoryPath() {
        return repositoryPath;
    }

    public JBTextField getPersistDTOSuffix() {
        return persistDTOSuffix;
    }

    public JBTextField getPersistDTOPath() {
        return persistDTOPath;
    }

    public JBTextField getUpdateDTOSuffix() {
        return updateDTOSuffix;
    }

    public JBTextField getUpdateDTOPath() {
        return updateDTOPath;
    }

    public JBTextField getResponseDTOSuffix() {
        return responseDTOSuffix;
    }

    public JBTextField getResponseDTOPath() {
        return responseDTOPath;
    }
}