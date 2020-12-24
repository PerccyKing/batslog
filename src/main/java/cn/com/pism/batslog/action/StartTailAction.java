package cn.com.pism.batslog.action;

import cn.com.pism.batslog.BatsLogBundle;
import cn.com.pism.batslog.ui.MyConsoleViewImpl;
import cn.com.pism.batslog.util.BatsLogUtil;
import com.intellij.execution.ui.ConsoleViewContentType;
import com.intellij.openapi.actionSystem.AnAction;
import com.intellij.openapi.actionSystem.AnActionEvent;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

import javax.swing.*;
import java.util.Objects;

/**
 * @author wangyihuai
 */
public class StartTailAction extends AnAction {


    @Override
    public void actionPerformed(@NotNull AnActionEvent e) {
        BatsLogUtil.TAIL_STATUS = Boolean.TRUE;
        MyConsoleViewImpl consoleView = (MyConsoleViewImpl) BatsLogUtil.CONSOLE_VIEW_MAP.get(Objects.requireNonNull(e.getProject()));
        consoleView.print(BatsLogBundle.message("SqlListenerHasStarted"), ConsoleViewContentType.LOG_DEBUG_OUTPUT);
        consoleView.installPopupHandler(BatsLogUtil.SUSPEND_ACTION);
        BatsLogUtil.TOOL_WINDOW.setTitleActions(BatsLogUtil.SUSPEND_ACTION);
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
    public StartTailAction(@Nullable String text, @Nullable String description,
                           @Nullable Icon icon) {
        super(text, description, icon);
    }


}
