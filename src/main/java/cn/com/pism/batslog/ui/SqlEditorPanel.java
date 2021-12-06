package cn.com.pism.batslog.ui;

import com.intellij.lang.Language;
import com.intellij.openapi.actionSystem.*;
import com.intellij.openapi.application.ApplicationManager;
import com.intellij.openapi.project.Project;
import com.intellij.ui.LanguageTextField;
import com.intellij.ui.components.JBScrollPane;
import lombok.Data;

import javax.swing.*;

/**
 * @author PerccyKing
 * @version 0.0.1
 * @date 2021/11/15 下午 06:33
 * @since 0.0.1
 */
@Data
public class SqlEditorPanel {
    private JPanel root;
    private JPanel content;

    private LanguageTextField editorTextField;


    private Project project;

    private String str;

    public SqlEditorPanel(Project project, String str) {

        this.project = project;
        this.str = str;

        editorTextField = new LanguageTextField(Language.findLanguageByID("SQL"), project, str, false);
        editorTextField.ensureWillComputePreferredSize();
        editorTextField.getPreferredSize();
        editorTextField.addNotify();
        editorTextField.setEnabled(true);
        editorTextField.setText(str);
        content.add(editorTextField);
    }

    private static AnAction getReformatCodeAction() {
        final String actionId = IdeActions.ACTION_EDITOR_REFORMAT;
        return ActionManager.getInstance().getAction(actionId);
    }

    /**
     * <p>
     * 格式化代码
     * </p>
     *
     * @author PerccyKing
     * @date 2021/11/23 下午 10:17
     */
    public void reformatCode() {
        final AnAction reformatCodeAction = getReformatCodeAction();
        final AnActionEvent fromAnAction = AnActionEvent.createFromAnAction(reformatCodeAction, null, "", dataId -> {
            if (CommonDataKeys.PROJECT.is(dataId)) {
                return project;
            }
            if (CommonDataKeys.EDITOR.is(dataId)) {
                return editorTextField.getEditor();
            }
            return null;
        });
        ApplicationManager.getApplication().invokeLater(() -> reformatCodeAction.actionPerformed(fromAnAction));
    }

    private void createUIComponents() {

    }
}
