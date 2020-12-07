package cn.com.pism.batslog.action;

import cn.com.pism.batslog.ui.FormatWindow;
import com.intellij.openapi.actionSystem.AnAction;
import com.intellij.openapi.actionSystem.AnActionEvent;
import com.intellij.openapi.ui.popup.ComponentPopupBuilder;
import com.intellij.openapi.ui.popup.JBPopupFactory;
import com.intellij.openapi.util.NlsActions;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

import javax.swing.*;

/**
 * 打开格式化窗口
 *
 * @author PerccyKing
 * @date 2020/12/07 上午 09:18
 */
public class OpenFormatWindowAction extends AnAction {
    /**
     * Implement this method to provide your action handler.
     *
     * @param e Carries information on the invocation place
     */
    @Override
    public void actionPerformed(@NotNull AnActionEvent e) {
        FormatWindowDialog.showDialog();
        FormatWindow formatWindow = new FormatWindow();
        JBPopupFactory instance = JBPopupFactory.getInstance();
        ComponentPopupBuilder window = instance.createComponentPopupBuilder(formatWindow.getRoot(), formatWindow.getButton1());
        window.setShowShadow(false);
        window.setTitle("BatsLog Format");
        window.setAdText("This some ad");
    }

    /**
     * Constructs a new action with the specified text, description and icon.
     *
     * @param text        Serves as a tooltip when the presentation is a button and the name of the
     *                    menu item when the presentation is a menu item
     * @param description Describes current action, this description will appear on
     *                    the status bar when presentation has focus
     * @param icon        Action's icon
     */
    public OpenFormatWindowAction(@Nullable @NlsActions.ActionText String text, @Nullable @NlsActions.ActionDescription String description, @Nullable Icon icon) {
        super(text, description, icon);
    }
}
