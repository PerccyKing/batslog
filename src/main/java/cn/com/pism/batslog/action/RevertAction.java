package cn.com.pism.batslog.action;

import com.intellij.icons.AllIcons;
import com.intellij.openapi.actionSystem.ActionUpdateThread;
import com.intellij.openapi.actionSystem.AnAction;
import com.intellij.openapi.actionSystem.AnActionEvent;
import com.intellij.openapi.actionSystem.Presentation;
import org.jetbrains.annotations.NotNull;

import javax.swing.*;

/**
 * @author PerccyKing
 * @since 2021/1/7 14:18
 */
public class RevertAction extends AnAction {

    private final String defaultVal;
    private final JTextField textField;

    public RevertAction(Icon icon, String defaultVal, JTextField textField) {
        super("", "", icon);
        this.defaultVal = defaultVal;
        this.textField = textField;
    }

    @Override
    public void actionPerformed(@NotNull AnActionEvent e) {
        textField.setText(this.defaultVal);
    }


    @Override
    public void update(@NotNull AnActionEvent e) {
        super.update(e);
        Presentation presentation = e.getPresentation();
        presentation.setIcon(AllIcons.Actions.Rollback);
    }

    @Override
    public @NotNull ActionUpdateThread getActionUpdateThread() {
        return ActionUpdateThread.EDT;
    }
}
