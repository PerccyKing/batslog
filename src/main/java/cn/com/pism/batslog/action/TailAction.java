package cn.com.pism.batslog.action;

import cn.com.pism.batslog.BatsLogBundle;
import cn.com.pism.batslog.ui.MyConsoleViewImpl;
import cn.com.pism.batslog.util.BatsLogUtil;
import cn.com.pism.batslog.util.GlobalVar;
import com.intellij.execution.ui.ConsoleViewContentType;
import com.intellij.icons.AllIcons;
import com.intellij.openapi.actionSystem.ActionUpdateThread;
import com.intellij.openapi.actionSystem.AnAction;
import com.intellij.openapi.actionSystem.AnActionEvent;
import com.intellij.openapi.actionSystem.Presentation;
import com.intellij.openapi.project.Project;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

import javax.swing.*;

/**
 * @author wangyihuai
 */
public class TailAction extends AnAction {

    private final Runnable runnable;

    @Override
    public void actionPerformed(@NotNull AnActionEvent e) {
        Project project = e.getProject();
        GlobalVar.removeSourceListByProject(project);
        if (project != null) {
            Boolean tailStatus = BatsLogUtil.getTailStatus(project);
            GlobalVar.putTailStatus(project, !tailStatus);
            MyConsoleViewImpl consoleView = (MyConsoleViewImpl) GlobalVar.getConsoleView(project);
            String tipMsg = Boolean.TRUE.equals(tailStatus) ? BatsLogBundle.message("SqlListenerHasStop") : BatsLogBundle.message("SqlListenerHasStarted");
            consoleView.print(tipMsg, ConsoleViewContentType.LOG_DEBUG_OUTPUT);
            consoleView.installPopupHandler(consoleView.getActionToolbar().getActions());

            runnable.run();
        }
    }


    @Override
    public void update(@NotNull AnActionEvent e) {
        super.update(e);
        Project project = e.getProject();
        if (project != null) {
            Boolean tailStatus = BatsLogUtil.getTailStatus(project);
            Icon icon = Boolean.TRUE.equals(tailStatus) ? AllIcons.Actions.Suspend : AllIcons.Actions.Execute;
            String text = Boolean.TRUE.equals(tailStatus) ? BatsLogBundle.message("stop") : BatsLogBundle.message("start");
            String description = Boolean.TRUE.equals(tailStatus) ? BatsLogBundle.message("stopSqlListener") : BatsLogBundle.message("startSqlListener");

            Presentation presentation = e.getPresentation();
            presentation.setIcon(icon);
            presentation.setDescription(description);
            presentation.setText(text);
        }
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
                      @Nullable Icon icon,
                      Runnable runnable) {
        super(text, description, icon);
        this.runnable = runnable;
    }


    @Override
    public @NotNull ActionUpdateThread getActionUpdateThread() {
        return ActionUpdateThread.EDT;
    }
}
