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
import java.awt.event.KeyEvent;
import java.io.FileNotFoundException;
import com.intellij.openapi.vfs.LocalFileSystem;
import static com.brigido.springrestcodegenerator.util.ConstUtil.*;
import static com.brigido.springrestcodegenerator.util.MessageUtil.*;
import static java.util.Objects.*;

public class CodeGeneratorDialog extends DialogWrapper {

    private JBTextField urlProject;
    private JBTextField pathClass;
    private JBCheckBox useLombok;
    private JBCheckBox useSerializable;

    private PropertyDTO propertyDTO;

    public CodeGeneratorDialog() {
        super(true);
        setTitle(TITLE_CODE_GENERATOR);
        setOKButtonText("Gerar [F2]");
        setCancelButtonText("Sair [Esc]");
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

        urlProject = new JBTextField();
        urlProject.setEnabled(false);
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
        pathClass.setEnabled(false);
        panel.add(new JLabel("Arquivo de Geração:*"), gbc);
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
        useLombok = new JBCheckBox("Usar Lombok", true);
        useSerializable = new JBCheckBox("Usar Serializable");
        checkboxPanel.add(useLombok);
        checkboxPanel.add(useSerializable);
        panel.add(checkboxPanel, gbc);

        gbc.gridy++;
        JButton extraSettingsButton = new JButton("Configurações Extras");
        extraSettingsButton.addActionListener(e -> showExtraSettingsDialog());
        panel.add(extraSettingsButton, gbc);

        panel.setPreferredSize(new Dimension(530, panel.getPreferredSize().height));
        loadProperties();
        return panel;
    }

    @Override
    protected void doOKAction() {
        if (urlProject.getText().isEmpty()) {
            showErrorMessageDialog("Preencha o campo de Pasta Principal do Projeto.");
            urlProject.requestFocus();
            return;
        }

        if (pathClass.getText().isEmpty()) {
            showErrorMessageDialog("Preencha o campo de Arquivo de Geração.");
            pathClass.requestFocus();
            return;
        }

        new CodeGeneratorSettings().setPropertyDTO(getPropertyDTO());
        try {
            new Generator().generate(getPropertyDTO());
        } catch (FileNotFoundException | SyntaxErrorException e) {
            showErrorMessageDialog(e.getMessage());
            return;
        } catch (Exception e) {
            showErrorMessageDialog("Erro de sintaxe na geração do(s) arquivo(s).");
            return;
        }

        super.doOKAction();

        showInformationMessageDialog("Código gerado!");
        reloadFiles();
    }

    private PropertyDTO getPropertyDTO() {
        if (isNull(propertyDTO)) {
            propertyDTO = new PropertyDTO();
        }

        propertyDTO.setUseLombok(useLombok.isSelected());
        propertyDTO.setUseSerializable(useSerializable.isSelected());

        propertyDTO.setUrlProject(urlProject.getText());
        propertyDTO.setPathClass(pathClass.getText());

        return propertyDTO;
    }

    private void loadProperties() {
        propertyDTO = new CodeGeneratorSettings().getPropertyDTO();
        if (isNull(propertyDTO)) {
            return;
        }
        useLombok.setSelected(propertyDTO.isUseLombok());
        useSerializable.setSelected(propertyDTO.isUseSerializable());

        urlProject.setText(propertyDTO.getUrlProject());
        pathClass.setText(propertyDTO.getPathClass());
    }

    private void reloadFiles() {
        try {
            LocalFileSystem.getInstance().refresh(true);
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    private void showExtraSettingsDialog() {
        new ExtraSettingsDialog(this).show();
    }

    public void setExtraSettings(PropertyDTO propertyDTOExtraSettings) {
        getPropertyDTO().setServiceSuffix(propertyDTOExtraSettings.getServiceSuffix());
        getPropertyDTO().setServicePath(propertyDTOExtraSettings.getServicePath());
        getPropertyDTO().setServiceImplSuffix(propertyDTOExtraSettings.getServiceImplSuffix());
        getPropertyDTO().setServiceImplPath(propertyDTOExtraSettings.getServiceImplPath());
        getPropertyDTO().setRepositorySuffix(propertyDTOExtraSettings.getRepositorySuffix());
        getPropertyDTO().setRepositoryPath(propertyDTOExtraSettings.getRepositoryPath());
        getPropertyDTO().setPersistDTOSuffix(propertyDTOExtraSettings.getPersistDTOSuffix());
        getPropertyDTO().setPersistDTOPath(propertyDTOExtraSettings.getPersistDTOPath());
        getPropertyDTO().setUpdateDTOSuffix(propertyDTOExtraSettings.getUpdateDTOSuffix());
        getPropertyDTO().setUpdateDTOPath(propertyDTOExtraSettings.getUpdateDTOPath());
        getPropertyDTO().setResponseDTOSuffix(propertyDTOExtraSettings.getResponseDTOSuffix());
        getPropertyDTO().setResponseDTOPath(propertyDTOExtraSettings.getResponseDTOPath());
        getPropertyDTO().setControllerSuffix(propertyDTOExtraSettings.getControllerSuffix());
        getPropertyDTO().setControllerPath(propertyDTOExtraSettings.getControllerPath());
        getPropertyDTO().setEntitySuffix(propertyDTOExtraSettings.getEntitySuffix());
        getPropertyDTO().setEntityPath(propertyDTOExtraSettings.getEntityPath());
    }
}
