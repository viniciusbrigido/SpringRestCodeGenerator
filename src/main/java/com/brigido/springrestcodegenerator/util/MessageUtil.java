package com.brigido.springrestcodegenerator.util;

import static com.brigido.springrestcodegenerator.util.ConstUtil.TITLE_CODE_GENERATOR;
import static com.intellij.openapi.ui.Messages.*;

public class MessageUtil {

    public static void showInformationMessageDialog(String message) {
        showMessageDialog(message, TITLE_CODE_GENERATOR, getInformationIcon());
    }

    public static void showErrorMessageDialog(String message) {
        showMessageDialog(message, TITLE_CODE_GENERATOR, getErrorIcon());
    }
}
