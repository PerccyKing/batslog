package cn.com.pism.batslog.action;

import com.intellij.openapi.actionSystem.AnAction;
import com.intellij.openapi.actionSystem.AnActionEvent;
import org.jetbrains.annotations.Nls;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

import javax.swing.*;

/**
 * @author PerccyKing
 * @version 0.0.1
 * @date 2021/04/25 下午 09:47
 * @since 0.0.1
 */
public class BeautyAction extends AnAction {

    /**
     * Creates a new action with its text, description and icon set to {@code null}.
     */
    public BeautyAction() {
    }

    /**
     * Creates a new action with {@code icon} provided. Its text, description set to {@code null}.
     *
     * @param icon Default icon to appear in toolbars and menus (Note some platform don't have icons in menu).
     */
    public BeautyAction(Icon icon) {
        super(icon);
    }

    /**
     * Creates a new action with the specified text. Description and icon are
     * set to {@code null}.
     *
     * @param text Serves as a tooltip when the presentation is a button and the name of the
     *             menu item when the presentation is a menu item.
     */
    public BeautyAction(@Nls(capitalization = Nls.Capitalization.Title) @Nullable String text) {
        super(text);
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
    public BeautyAction(@Nls(capitalization = Nls.Capitalization.Title) @Nullable String text, @Nls(capitalization = Nls.Capitalization.Sentence) @Nullable String description, @Nullable Icon icon) {
        super(text, description, icon);
    }

    /**
     * Implement this method to provide your action handler.
     *
     * @param e Carries information on the invocation place
     */
    @Override
    public void actionPerformed(@NotNull AnActionEvent e) {
        System.out.println("beauty");
    }
}
