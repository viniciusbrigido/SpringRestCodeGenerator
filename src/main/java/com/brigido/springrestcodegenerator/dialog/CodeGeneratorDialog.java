package com.brigido.springrestcodegenerator.dialog;

import com.brigido.springrestcodegenerator.config.CodeGeneratorSettings;
import com.brigido.springrestcodegenerator.dto.PropertyDTO;
import com.brigido.springrestcodegenerator.exception.SyntaxErrorException;
import com.brigido.springrestcodegenerator.generator.Generator;
import com.intellij.openapi.fileChooser.*;
import com.intellij.openapi.ui.*;
import com.intellij.openapi.vfs.VirtualFile;
import com.intellij.ui.components.*;
import com.intellij.util.ui.JBUI;
import org.jetbrains.annotations.Nullable;
import javax.swing.*;
import java.awt.*;
import java.io.FileNotFoundException;
import com.intellij.openapi.vfs.LocalFileSystem;
import static java.util.Objects.*;

public class CodeGeneratorDialog extends DialogWrapper {

    public static final String TITLE = "Code Generator";
    
    private JBTextField urlProject;
    private JBTextField pathClass;
    private JBCheckBox cbUseLombok;
    private JBCheckBox cbUseSerializable;

    private JBTextField packageEntity;
    private JBTextField packageController;

    private PropertyDTO propertyDTO;

    protected CodeGeneratorDialog() {
        super(true);
        setTitle(TITLE);
        setOKButtonText("Gerar");
        setCancelButtonText("Sair");
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

        urlProject = new JBTextField();
        panel.add(new JLabel("Pasta Principal do Projeto:*"), gbc);
        gbc.gridy++;
        panel.add(urlProject, gbc);

        TextFieldWithBrowseButton browseUrlProjectButton = new TextFieldWithBrowseButton(urlProject);
        browseUrlProjectButton.addActionListener(e -> {
            FileChooserDescriptor descriptor = FileChooserDescriptorFactory.createSingleFolderDescriptor();
            descriptor.setTitle("Selecionar Pasta");
            VirtualFile selectedFolder = FileChooser.chooseFile(descriptor, null, null);
            if (selectedFolder != null) {
                String selectedPath = selectedFolder.getPath();
                urlProject.setText(selectedPath);
            }
        });
        panel.add(browseUrlProjectButton, gbc);

        gbc.gridy++;
        pathClass = new JBTextField();
        panel.add(new JLabel("Arquivo de Gera��o:*"), gbc);
        gbc.gridy++;
        panel.add(pathClass, gbc);

        TextFieldWithBrowseButton browsePathClassButton = new TextFieldWithBrowseButton(pathClass);
        browsePathClassButton.addActionListener(e -> {
            FileChooserDescriptor descriptor = FileChooserDescriptorFactory.createSingleFileDescriptor("json");
            descriptor.setTitle("Selecionar arquivo JSON");

            VirtualFile selectedFile = FileChooser.chooseFile(descriptor, null, null);
            if (selectedFile != null) {
                String selectedPath = selectedFile.getPath();
                pathClass.setText(selectedPath);
            }
        });
        panel.add(browsePathClassButton, gbc);

        gbc.gridy++;
        gbc.gridwidth = 2;
        gbc.weightx = 0.5;
        gbc.fill = GridBagConstraints.HORIZONTAL;

        JPanel checkboxPanel = new JPanel(new GridLayout(1, 2));
        cbUseLombok = new JBCheckBox("Usar Lombok", true);
        cbUseSerializable = new JBCheckBox("Usar Serializable");
        checkboxPanel.add(cbUseLombok);
        checkboxPanel.add(cbUseSerializable);

        panel.add(checkboxPanel, gbc);

        JPanel additionalFieldsPanel = new JPanel(new GridBagLayout());

        GridBagConstraints additionalFieldsGbc = new GridBagConstraints();
        additionalFieldsGbc.gridx = 0;
        additionalFieldsGbc.gridy = 0;
        additionalFieldsGbc.weightx = 0.5;
        additionalFieldsGbc.fill = GridBagConstraints.HORIZONTAL;
        additionalFieldsGbc.insets = JBUI.insets(5);

        packageEntity = new JBTextField();
        additionalFieldsPanel.add(new JLabel("Package Entity:"), additionalFieldsGbc);
        additionalFieldsGbc.gridx++;
        additionalFieldsPanel.add(packageEntity, additionalFieldsGbc);

        packageController = new JBTextField();
        additionalFieldsGbc.gridx = 0;
        additionalFieldsGbc.gridy++;
        additionalFieldsPanel.add(new JLabel("Package Controller:"), additionalFieldsGbc);
        additionalFieldsGbc.gridx++;
        additionalFieldsPanel.add(packageController, additionalFieldsGbc);

        gbc.gridy++;
        panel.add(additionalFieldsPanel, gbc);

        panel.setPreferredSize(new Dimension(500, panel.getPreferredSize().height));
        loadProperties();
        return panel;
    }

    @Override
    protected void doOKAction() {
        if (urlProject.getText().isEmpty()) {
            Messages.showMessageDialog("Preencha o campo de Pasta Principal do Projeto.", TITLE, Messages.getErrorIcon());
            urlProject.requestFocus();
            return;
        }

        if (pathClass.getText().isEmpty()) {
            Messages.showMessageDialog("Preencha o campo de Arquivo de Gera��o.", TITLE, Messages.getErrorIcon());
            pathClass.requestFocus();
            return;
        }

        new CodeGeneratorSettings().setPropertyDTO(getPropertyDTO());
        try {
            new Generator().generate(getPropertyDTO());
        } catch (FileNotFoundException | SyntaxErrorException e) {
            Messages.showMessageDialog(e.getMessage(), TITLE, Messages.getErrorIcon());
            return;
        } catch (Exception e) {
            Messages.showMessageDialog("Erro de sintaxe na gera��o do(s) arquivo(s).", TITLE, Messages.getErrorIcon());
            return;
        }

        super.doOKAction();

        Messages.showMessageDialog("C�digo gerado!", TITLE, Messages.getInformationIcon());
        reloadFiles();
    }

    private PropertyDTO getPropertyDTO() {
        if (isNull(propertyDTO)) {
            propertyDTO = new PropertyDTO();
        }

        propertyDTO.setUseLombok(cbUseLombok.isSelected());
        propertyDTO.setUseSerializable(cbUseSerializable.isSelected());

        propertyDTO.setUrlProject(urlProject.getText());
        propertyDTO.setPathClass(pathClass.getText());

        propertyDTO.setPackageEntity(getPackageName(packageEntity, "entity"));
        propertyDTO.setPackageController(getPackageName(packageController, "controller"));

        return propertyDTO;
    }

    private String getPackageName(JBTextField textField, String defaultValue) {
        if (nonNull(textField) && nonNull(textField.getText()) && !textField.getText().isEmpty()) {
            return textField.getText();
        }
        return defaultValue;
    }

    private void loadProperties() {
        propertyDTO = new CodeGeneratorSettings().getPropertyDTO();
        if (isNull(propertyDTO)) {
            return;
        }
        cbUseLombok.setSelected(propertyDTO.isUseLombok());
        cbUseSerializable.setSelected(propertyDTO.isUseSerializable());

        urlProject.setText(propertyDTO.getUrlProject());
        pathClass.setText(propertyDTO.getPathClass());

        packageEntity.setText(propertyDTO.getPackageEntity());
        packageController.setText(propertyDTO.getPackageController());
    }

    private void reloadFiles() {
        try {
            LocalFileSystem.getInstance().refresh(true);
        } catch (Exception e) {
            e.printStackTrace();
        }
    }
}
