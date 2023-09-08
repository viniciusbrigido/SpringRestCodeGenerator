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
        JPanel buttonPanel = new JPanel(new GridLayout(1, 2));
        buttonPanel.setBorder(BorderFactory.createEmptyBorder(5, 0, 0, 0));

        JButton structureSettingsButton = new JButton("Estrutura");
        structureSettingsButton.addActionListener(e -> showStructureSettingsDialog());
        buttonPanel.add(structureSettingsButton);

        JButton functionsSettingsButton = new JButton("Funções");
        functionsSettingsButton.addActionListener(e -> showFunctionsSettingsDialog());
        buttonPanel.add(functionsSettingsButton);

        JPanel settingsPanel = new JPanel(new BorderLayout());
        settingsPanel.add(new JLabel("Configurações Extras:"), BorderLayout.NORTH);
        settingsPanel.setBorder(BorderFactory.createEmptyBorder(10, 0, 0, 0));
        settingsPanel.add(buttonPanel, BorderLayout.CENTER);

        GridBagConstraints settingsPanelGbc = new GridBagConstraints();
        settingsPanelGbc.gridx = 0;
        settingsPanelGbc.gridy = gbc.gridy;
        settingsPanelGbc.gridwidth = 2;
        settingsPanelGbc.fill = GridBagConstraints.HORIZONTAL;
        panel.add(settingsPanel, settingsPanelGbc);

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

    private void showStructureSettingsDialog() {
        new StructureSettingsDialog(this).show();
    }

    private void showFunctionsSettingsDialog() {
        new FunctionSettingsDialog(this).show();
    }

    public void setStructureSettings(PropertyDTO propertyDTOStructureSettings) {
        getPropertyDTO().setServiceSuffix(propertyDTOStructureSettings.getServiceSuffix());
        getPropertyDTO().setServicePath(propertyDTOStructureSettings.getServicePath());
        getPropertyDTO().setServiceImplSuffix(propertyDTOStructureSettings.getServiceImplSuffix());
        getPropertyDTO().setServiceImplPath(propertyDTOStructureSettings.getServiceImplPath());
        getPropertyDTO().setRepositorySuffix(propertyDTOStructureSettings.getRepositorySuffix());
        getPropertyDTO().setRepositoryPath(propertyDTOStructureSettings.getRepositoryPath());
        getPropertyDTO().setPersistDTOSuffix(propertyDTOStructureSettings.getPersistDTOSuffix());
        getPropertyDTO().setPersistDTOPath(propertyDTOStructureSettings.getPersistDTOPath());
        getPropertyDTO().setUpdateDTOSuffix(propertyDTOStructureSettings.getUpdateDTOSuffix());
        getPropertyDTO().setUpdateDTOPath(propertyDTOStructureSettings.getUpdateDTOPath());
        getPropertyDTO().setResponseDTOSuffix(propertyDTOStructureSettings.getResponseDTOSuffix());
        getPropertyDTO().setResponseDTOPath(propertyDTOStructureSettings.getResponseDTOPath());
        getPropertyDTO().setControllerSuffix(propertyDTOStructureSettings.getControllerSuffix());
        getPropertyDTO().setControllerPath(propertyDTOStructureSettings.getControllerPath());
        getPropertyDTO().setEntitySuffix(propertyDTOStructureSettings.getEntitySuffix());
        getPropertyDTO().setEntityPath(propertyDTOStructureSettings.getEntityPath());
    }

    public void setFunctionSettings(PropertyDTO propertyDTOFunctionSettings) {
        getPropertyDTO().setFunctionFindById(propertyDTOFunctionSettings.getFunctionFindById());
        getPropertyDTO().setEndpointFindById(propertyDTOFunctionSettings.getEndpointFindById());
        getPropertyDTO().setFunctionCreate(propertyDTOFunctionSettings.getFunctionCreate());
        getPropertyDTO().setEndpointCreate(propertyDTOFunctionSettings.getEndpointCreate());
        getPropertyDTO().setFunctionDelete(propertyDTOFunctionSettings.getFunctionDelete());
        getPropertyDTO().setEndpointDelete(propertyDTOFunctionSettings.getEndpointDelete());
        getPropertyDTO().setFunctionUpdate(propertyDTOFunctionSettings.getFunctionUpdate());
        getPropertyDTO().setEndpointUpdate(propertyDTOFunctionSettings.getEndpointUpdate());
        getPropertyDTO().setFunctionFindAll(propertyDTOFunctionSettings.getFunctionFindAll());
        getPropertyDTO().setEndpointFindAll(propertyDTOFunctionSettings.getEndpointFindAll());
    }
}
