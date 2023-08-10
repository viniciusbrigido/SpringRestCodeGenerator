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

    private JBTextField controllerSuffix;
    private JBTextField controllerPath;

    private JBTextField entitySuffix;
    private JBTextField entityPath;

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

        panel.add(new JBLabel("Prefixo da Entidade:"), gbc);
        gbc.gridx = 1;
        panel.add(new JBLabel("Pasta da Entidade:"), gbc);
        gbc.gridy++;
        gbc.gridx = 0;
        entitySuffix = new JBTextField();
        entitySuffix.getEmptyText().setText("Model, Entity, ...");
        panel.add(entitySuffix, gbc);
        gbc.gridx = 1;
        entityPath = new JBTextField();
        panel.add(entityPath, gbc);

        gbc.gridy++;
        gbc.gridx = 0;
        panel.add(new JBLabel("Prefixo do DTO de Persistência:"), gbc);
        gbc.gridx = 1;
        panel.add(new JBLabel("Pasta do DTO de Persistência:"), gbc);
        gbc.gridy++;
        gbc.gridx = 0;
        persistDTOSuffix = new JBTextField();
        panel.add(persistDTOSuffix, gbc);
        gbc.gridx = 1;
        persistDTOPath = new JBTextField();
        panel.add(persistDTOPath, gbc);

        gbc.gridy++;
        gbc.gridx = 0;
        panel.add(new JBLabel("Prefixo do DTO de Atualização:"), gbc);
        gbc.gridx = 1;
        panel.add(new JBLabel("Pasta do DTO de Atualização:"), gbc);
        gbc.gridy++;
        gbc.gridx = 0;
        updateDTOSuffix = new JBTextField();
        panel.add(updateDTOSuffix, gbc);
        gbc.gridx = 1;
        updateDTOPath = new JBTextField();
        panel.add(updateDTOPath, gbc);

        gbc.gridy++;
        gbc.gridx = 0;
        panel.add(new JBLabel("Prefixo do DTO de Resposta:"), gbc);
        gbc.gridx = 1;
        panel.add(new JBLabel("Pasta do DTO de Resposta:"), gbc);
        gbc.gridy++;
        gbc.gridx = 0;
        responseDTOSuffix = new JBTextField();
        panel.add(responseDTOSuffix, gbc);
        gbc.gridx = 1;
        responseDTOPath = new JBTextField();
        panel.add(responseDTOPath, gbc);

        gbc.gridy++;
        gbc.gridx = 0;
        panel.add(new JBLabel("Prefixo do Controlador:"), gbc);
        gbc.gridx = 1;
        panel.add(new JBLabel("Pasta do Controlador:"), gbc);
        gbc.gridy++;
        gbc.gridx = 0;
        controllerSuffix = new JBTextField();
        panel.add(controllerSuffix, gbc);
        gbc.gridx = 1;
        controllerPath = new JBTextField();
        panel.add(controllerPath, gbc);

        gbc.gridy++;
        gbc.gridx = 0;
        panel.add(new JBLabel("Prefixo do Serviço:"), gbc);
        gbc.gridx = 1;
        panel.add(new JBLabel("Pasta do Serviço:"), gbc);
        gbc.gridy++;
        gbc.gridx = 0;
        serviceSuffix = new JBTextField();
        panel.add(serviceSuffix, gbc);
        gbc.gridx = 1;
        servicePath = new JBTextField();
        panel.add(servicePath, gbc);

        gbc.gridy++;
        gbc.gridx = 0;
        panel.add(new JBLabel("Prefixo da Implementação do Serviço:"), gbc);
        gbc.gridx = 1;
        panel.add(new JBLabel("Pasta da Implementação do Serviço:"), gbc);
        gbc.gridy++;
        gbc.gridx = 0;
        serviceImplSuffix = new JBTextField();
        panel.add(serviceImplSuffix, gbc);
        gbc.gridx = 1;
        serviceImplPath = new JBTextField();
        panel.add(serviceImplPath, gbc);

        gbc.gridy++;
        gbc.gridx = 0;
        panel.add(new JBLabel("Prefixo do Repositório:"), gbc);
        gbc.gridx = 1;
        panel.add(new JBLabel("Pasta do Repositório:"), gbc);
        gbc.gridy++;
        gbc.gridx = 0;
        repositorySuffix = new JBTextField();
        panel.add(repositorySuffix, gbc);
        gbc.gridx = 1;
        repositoryPath = new JBTextField();
        panel.add(repositoryPath, gbc);

        panel.setPreferredSize(new Dimension(530, panel.getPreferredSize().height));
        loadProperties();
        return panel;
    }


    private void loadProperties() {
        propertyDTO = new CodeGeneratorSettings().getPropertyDTO();
        if (isNull(propertyDTO)) {
            propertyDTO = new PropertyDTO();
        }
        serviceSuffix.setText(propertyDTO.getServiceSuffix());
        servicePath.setText(propertyDTO.getServicePath());
        serviceImplSuffix.setText(propertyDTO.getServiceImplSuffix());
        serviceImplPath.setText(propertyDTO.getServiceImplPath());
        repositorySuffix.setText(propertyDTO.getRepositorySuffix());
        repositoryPath.setText(propertyDTO.getRepositoryPath());
        persistDTOSuffix.setText(propertyDTO.getPersistDTOSuffix());
        persistDTOPath.setText(propertyDTO.getPersistDTOPath());
        updateDTOSuffix.setText(propertyDTO.getUpdateDTOSuffix());
        updateDTOPath.setText(propertyDTO.getUpdateDTOPath());
        responseDTOSuffix.setText(propertyDTO.getResponseDTOSuffix());
        responseDTOPath.setText(propertyDTO.getResponseDTOPath());
        controllerSuffix.setText(propertyDTO.getControllerSuffix());
        controllerPath.setText(propertyDTO.getControllerPath());
        entitySuffix.setText(propertyDTO.getEntitySuffix());
        entityPath.setText(propertyDTO.getEntityPath());
    }

    @Override
    protected void doOKAction() {
        codeGeneratorDialog.setExtraSettings(getPropertyDTO());
        close(OK_EXIT_CODE);
    }

    private PropertyDTO getPropertyDTO() {
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
        propertyDTO.setControllerSuffix(getTextFieldValue(controllerSuffix));
        propertyDTO.setControllerPath(getTextFieldValue(controllerPath));
        propertyDTO.setEntitySuffix(getTextFieldValue(entitySuffix));
        propertyDTO.setEntityPath(getTextFieldValue(entityPath));

        return propertyDTO;
    }

    private String getTextFieldValue(JBTextField textField) {
        if (nonNull(textField) && nonNull(textField.getText()) && !textField.getText().isEmpty()) {
            return textField.getText();
        }
        return null;
    }
}