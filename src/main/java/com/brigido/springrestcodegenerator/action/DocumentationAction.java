package com.brigido.springrestcodegenerator.action;

import com.intellij.openapi.actionSystem.AnAction;
import com.intellij.openapi.actionSystem.AnActionEvent;
import org.jetbrains.annotations.NotNull;
import java.awt.*;
import java.net.URI;

public class DocumentationAction extends AnAction {

    @Override
    public void actionPerformed(@NotNull AnActionEvent e) {
        try {
            URI uri = new URI("https://github.com/viniciusbrigido/SpringRestCodeGenerator");
            Desktop.getDesktop().browse(uri);
        } catch (Exception exception) {
            exception.printStackTrace();
        }
    }
}
