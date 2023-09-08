package com.brigido.springrestcodegenerator.dialog;

import com.brigido.springrestcodegenerator.config.CodeGeneratorSettings;
import com.brigido.springrestcodegenerator.dto.PropertyDTO;
import com.brigido.springrestcodegenerator.exception.FileNameException;
import com.intellij.openapi.ui.DialogWrapper;
import com.intellij.ui.components.JBLabel;
import com.intellij.ui.components.JBTextField;
import com.intellij.util.ui.JBUI;
import org.jetbrains.annotations.Nullable;
import javax.swing.*;
import java.awt.*;
import java.awt.event.KeyEvent;
import static com.brigido.springrestcodegenerator.util.ConstUtil.TITLE_FUNCTION_SETTINGS;
import static com.brigido.springrestcodegenerator.util.MessageUtil.showErrorMessageDialog;
import static com.brigido.springrestcodegenerator.util.StringUtil.*;
import static java.util.Objects.isNull;

public class FunctionSettingsDialog extends DialogWrapper {

    private PropertyDTO propertyDTO;
    private final CodeGeneratorDialog codeGeneratorDialog;

    private JBTextField functionFindById;
    private JBTextField endpointFindById;

    private JBTextField functionCreate;
    private JBTextField endpointCreate;

    private JBTextField functionDelete;
    private JBTextField endpointDelete;

    private JBTextField functionUpdate;
    private JBTextField endpointUpdate;

    private JBTextField functionFindAll;
    private JBTextField endpointFindAll;

    public FunctionSettingsDialog(CodeGeneratorDialog codeGeneratorDialog) {
        super(true);
        this.codeGeneratorDialog = codeGeneratorDialog;
        setOKButtonText("Salvar [F2]");
        setCancelButtonText("Sair [Esc]");
        setTitle(TITLE_FUNCTION_SETTINGS);
        init();

        getRootPane().registerKeyboardAction(
                e -> doOKAction(),
                KeyStroke.getKeyStroke(KeyEvent.VK_F2, 0),
                JComponent.WHEN_IN_FOCUSED_WINDOW
        );
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

        gbc.gridx = 0;
        panel.add(new JBLabel("Função de Persistência:"), gbc);
        gbc.gridx = 1;
        panel.add(new JBLabel("Endpoint de Persistência:"), gbc);
        gbc.gridy++;
        gbc.gridx = 0;
        functionCreate = new JBTextField();
        panel.add(functionCreate, gbc);
        gbc.gridx = 1;
        endpointCreate = new JBTextField();
        panel.add(endpointCreate, gbc);

        gbc.gridy++;
        gbc.gridx = 0;
        panel.add(new JBLabel("Função de Busca por ID:"), gbc);
        gbc.gridx = 1;
        panel.add(new JBLabel("Endpoint de Busca por ID:"), gbc);
        gbc.gridy++;
        gbc.gridx = 0;
        functionFindById = new JBTextField();
        panel.add(functionFindById, gbc);
        gbc.gridx = 1;
        endpointFindById = new JBTextField();
        panel.add(endpointFindById, gbc);

        gbc.gridy++;
        gbc.gridx = 0;
        panel.add(new JBLabel("Função de Atualização:"), gbc);
        gbc.gridx = 1;
        panel.add(new JBLabel("Endpoint de Atualização:"), gbc);
        gbc.gridy++;
        gbc.gridx = 0;
        functionUpdate = new JBTextField();
        panel.add(functionUpdate, gbc);
        gbc.gridx = 1;
        endpointUpdate = new JBTextField();
        panel.add(endpointUpdate, gbc);

        gbc.gridy++;
        gbc.gridx = 0;
        panel.add(new JBLabel("Função de Remoção:"), gbc);
        gbc.gridx = 1;
        panel.add(new JBLabel("Endpoint de Remoção:"), gbc);
        gbc.gridy++;
        gbc.gridx = 0;
        functionDelete = new JBTextField();
        panel.add(functionDelete, gbc);
        gbc.gridx = 1;
        endpointDelete = new JBTextField();
        panel.add(endpointDelete, gbc);

        gbc.gridy++;
        gbc.gridx = 0;
        panel.add(new JBLabel("Função de Listagem:"), gbc);
        gbc.gridx = 1;
        panel.add(new JBLabel("Endpoint de Listagem:"), gbc);
        gbc.gridy++;
        gbc.gridx = 0;
        functionFindAll = new JBTextField();
        panel.add(functionFindAll, gbc);
        gbc.gridx = 1;
        endpointFindAll = new JBTextField();
        panel.add(endpointFindAll, gbc);

        panel.setPreferredSize(new Dimension(530, panel.getPreferredSize().height));
        loadProperties();
        return panel;
    }

    private void loadProperties() {
        propertyDTO = new CodeGeneratorSettings().getPropertyDTO();
        if (isNull(propertyDTO)) {
            propertyDTO = new PropertyDTO();
        }

        functionFindById.setText(propertyDTO.getFunctionFindById());
        endpointFindById.setText(propertyDTO.getEndpointFindById());
        functionCreate.setText(propertyDTO.getFunctionCreate());
        endpointCreate.setText(propertyDTO.getEndpointCreate());
        functionDelete.setText(propertyDTO.getFunctionDelete());
        endpointDelete.setText(propertyDTO.getEndpointDelete());
        functionUpdate.setText(propertyDTO.getFunctionUpdate());
        endpointUpdate.setText(propertyDTO.getEndpointUpdate());
        functionFindAll.setText(propertyDTO.getFunctionFindAll());
        endpointFindAll.setText(propertyDTO.getEndpointFindAll());
    }

    @Override
    protected void doOKAction() {
        PropertyDTO propertyDTO;
        try {
            propertyDTO = getPropertyDTO();
        } catch (FileNameException e) {
            showErrorMessageDialog("Os campos não podem conter caracteres inválidos.");
            return;
        }

        codeGeneratorDialog.setFunctionSettings(propertyDTO);
        close(OK_EXIT_CODE);
    }

    private PropertyDTO getPropertyDTO() {
        propertyDTO.setFunctionFindById(getTextFieldValue(functionFindById));
        propertyDTO.setEndpointFindById(getTextFieldValue(endpointFindById));
        propertyDTO.setFunctionCreate(getTextFieldValue(functionCreate));
        propertyDTO.setEndpointCreate(getTextFieldValue(endpointCreate));
        propertyDTO.setFunctionDelete(getTextFieldValue(functionDelete));
        propertyDTO.setEndpointDelete(getTextFieldValue(endpointDelete));
        propertyDTO.setFunctionUpdate(getTextFieldValue(functionUpdate));
        propertyDTO.setEndpointUpdate(getTextFieldValue(endpointUpdate));
        propertyDTO.setFunctionFindAll(getTextFieldValue(functionFindAll));
        propertyDTO.setEndpointFindAll(getTextFieldValue(endpointFindAll));

        return propertyDTO;
    }

    private String getTextFieldValue(JBTextField textField) {
        if (isNull(textField) || isNull(textField.getText()) || textField.getText().isEmpty()) {
            return null;
        }
        return lowerCaseFirstLetter(textField.getText());
    }
}
