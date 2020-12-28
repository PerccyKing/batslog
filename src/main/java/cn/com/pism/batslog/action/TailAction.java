package cn.com.pism.batslog.action;

import cn.com.pism.batslog.BatsLogBundle;
import cn.com.pism.batslog.ui.MyConsoleViewImpl;
import cn.com.pism.batslog.util.BatsLogUtil;
import com.intellij.execution.ui.ConsoleViewContentType;
import com.intellij.icons.AllIcons;
import com.intellij.openapi.actionSystem.AnAction;
import com.intellij.openapi.actionSystem.AnActionEvent;
import com.intellij.openapi.actionSystem.Presentation;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

import javax.swing.*;
import java.util.Objects;

/**
 * @author wangyihuai
 */
public class TailAction extends AnAction {


    @Override
    public void actionPerformed(@NotNull AnActionEvent e) {
        BatsLogUtil.TAIL_STATUS = !BatsLogUtil.TAIL_STATUS;
        MyConsoleViewImpl consoleView = (MyConsoleViewImpl) BatsLogUtil.CONSOLE_VIEW_MAP.get(Objects.requireNonNull(e.getProject()));
        String tipMsg = BatsLogUtil.TAIL_STATUS ? BatsLogBundle.message("SqlListenerHasStarted") : BatsLogBundle.message("SqlListenerHasStop");
        consoleView.print(tipMsg, ConsoleViewContentType.LOG_DEBUG_OUTPUT);
        consoleView.installPopupHandler(consoleView.getActionToolbar().getActions());
    }


    @Override
    public void update(@NotNull AnActionEvent e) {
        super.update(e);
        Boolean tailStatus = BatsLogUtil.TAIL_STATUS;
        Icon icon = tailStatus ? AllIcons.Actions.Suspend : AllIcons.Actions.Execute;
        String text = tailStatus ? BatsLogBundle.message("stop") : BatsLogBundle.message("start");
        String description = tailStatus ? BatsLogBundle.message("stopSqlListener") : BatsLogBundle.message("startSqlListener");

        Presentation presentation = e.getPresentation();
        presentation.setIcon(icon);
        presentation.setDescription(description);
        presentation.setText(text);
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
    public TailAction(@Nullable String text, @Nullable String description,
                      @Nullable Icon icon) {
        super(text, description, icon);
    }


}
