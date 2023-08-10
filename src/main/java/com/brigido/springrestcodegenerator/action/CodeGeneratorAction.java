package com.brigido.springrestcodegenerator.action;

import com.brigido.springrestcodegenerator.dialog.CodeGeneratorDialog;
import com.intellij.openapi.actionSystem.AnAction;
import com.intellij.openapi.actionSystem.AnActionEvent;
import org.jetbrains.annotations.NotNull;

public class CodeGeneratorAction extends AnAction {

    @Override
    public void actionPerformed(@NotNull AnActionEvent e) {
        new CodeGeneratorDialog().show();
    }
}
