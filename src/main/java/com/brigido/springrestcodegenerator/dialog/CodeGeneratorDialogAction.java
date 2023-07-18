package com.brigido.springrestcodegenerator.dialog;

import com.intellij.openapi.actionSystem.AnAction;
import com.intellij.openapi.actionSystem.AnActionEvent;

public class CodeGeneratorDialogAction extends AnAction {

    @Override
    public void actionPerformed(AnActionEvent e) {
        CodeGeneratorDialog dialog = new CodeGeneratorDialog();
        dialog.show();
    }
}
