package cn.com.pism.batslog.action;

import cn.com.pism.batslog.util.StringUtil;
import com.intellij.icons.AllIcons;
import com.intellij.openapi.actionSystem.AnAction;
import com.intellij.openapi.actionSystem.AnActionEvent;
import com.intellij.openapi.util.NlsActions;
import com.intellij.openapi.wm.ToolWindow;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

import javax.swing.*;
import java.util.ArrayList;
import java.util.List;

/**
 * @author PerccyKing
 * @version 0.0.1
 * @date 2020/10/25 下午 11:17
 * @since 0.0.1
 */
public class SuspendTailAction extends AnAction {

    private ToolWindow toolWindow;

    /**
     * Implement this method to provide your action handler.
     *
     * @param e Carries information on the invocation place
     */
    @Override
    public void actionPerformed(@NotNull AnActionEvent e) {
        List<AnAction> anActions = new ArrayList<>();
        StartTailAction suspendTailAction = new StartTailAction(StringUtil.encoding("启动"), StringUtil.encoding("开启SQL监听"), AllIcons.Actions.Execute, toolWindow);
        anActions.add(suspendTailAction);
        anActions.add(new ClearAllAction(StringUtil.encoding("清空面板"), StringUtil.encoding("开启SQL监听"), AllIcons.Actions.GC));
        toolWindow.setTitleActions(anActions);
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
    public SuspendTailAction(@Nullable @NlsActions.ActionText String text, @Nullable @NlsActions.ActionDescription String description, @Nullable Icon icon, ToolWindow toolWindow) {
        super(text, description, icon);
        this.toolWindow = toolWindow;
    }
}
