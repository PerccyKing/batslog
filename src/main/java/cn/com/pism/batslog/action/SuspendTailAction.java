package cn.com.pism.batslog.action;

import cn.com.pism.batslog.util.BatsLogUtil;
import cn.com.pism.batslog.util.StringUtil;
import com.intellij.execution.ui.ConsoleViewContentType;
import com.intellij.openapi.actionSystem.AnAction;
import com.intellij.openapi.actionSystem.AnActionEvent;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

import javax.swing.*;
import java.util.Objects;

/**
 * @author PerccyKing
 * @version 0.0.1
 * @date 2020/10/25 下午 11:17
 * @since 0.0.1
 */
public class SuspendTailAction extends AnAction {


    /**
     * Implement this method to provide your action handler.
     *
     * @param e Carries information on the invocation place
     */
    @Override
    public void actionPerformed(@NotNull AnActionEvent e) {
        BatsLogUtil.TAIL_STATUS = Boolean.FALSE;
        BatsLogUtil.CONSOLE_VIEW_MAP.get(Objects.requireNonNull(e.getProject())).print(StringUtil.encoding("SQL监听已停止\n"), ConsoleViewContentType.LOG_DEBUG_OUTPUT);
        BatsLogUtil.TOOL_WINDOW.setTitleActions(BatsLogUtil.START_ACTION);
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
    public SuspendTailAction(@Nullable String text, @Nullable String description, @Nullable Icon icon) {
        super(text, description, icon);
    }
}
