package cn.com.pism.batslog.action;

import cn.com.pism.batslog.util.BatsLogUtil;
import cn.com.pism.batslog.util.SqlFormatUtil;
import com.intellij.openapi.actionSystem.AnAction;
import com.intellij.openapi.actionSystem.AnActionEvent;
import lombok.extern.log4j.Log4j;
import org.jetbrains.annotations.Nls;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

import javax.swing.*;

/**
 * @author PerccyKing
 * @version 0.0.1
 * @since 2020/10/18 下午 05:30
 */
@Log4j
public class FormatSqlAction extends AnAction {

    /**
     * Creates a new action with its text, description and icon set to {@code null}.
     */
    @SuppressWarnings("unused")
    public FormatSqlAction() {
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
    public FormatSqlAction(@Nls(capitalization = Nls.Capitalization.Title) @Nullable String text, @Nls(capitalization = Nls.Capitalization.Sentence) @Nullable String description, @Nullable Icon icon) {
        super(text, description, icon);
    }

    @Override
    public void actionPerformed(@NotNull AnActionEvent e) {
        String selectedText = BatsLogUtil.getSelectText(e);
        SqlFormatUtil.format(selectedText, e.getProject());
    }
}
