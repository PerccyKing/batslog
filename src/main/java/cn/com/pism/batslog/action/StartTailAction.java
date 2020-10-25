package cn.com.pism.batslog.action;

import cn.com.pism.batslog.util.StringUtil;
import com.intellij.icons.AllIcons;
import com.intellij.openapi.actionSystem.ActionManager;
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
 * @author wangyihuai
 */
public class StartTailAction extends AnAction {

    private ToolWindow toolWindow;

    @Override
    public void actionPerformed(@NotNull AnActionEvent e) {
        List<AnAction> anActions = new ArrayList<>();
        SuspendTailAction startTailAction = new SuspendTailAction(StringUtil.encoding("启动"), StringUtil.encoding("开启SQL监听"), AllIcons.Actions.Suspend, toolWindow);
        anActions.add(startTailAction);
        anActions.add(new ClearAllAction(StringUtil.encoding("清空面板"), StringUtil.encoding("开启SQL监听"), AllIcons.Actions.GC));
        toolWindow.setTitleActions(anActions);
    }

    /**
     * Creates a new action with {@code icon} provided. Its text, description set to {@code null}.
     *
     * @param icon Default icon to appear in toolbars and menus (Note some platform don't have icons in menu).
     */
    public StartTailAction(Icon icon) {
        super(icon);
    }

    /**
     * Creates a new action with its text, description and icon set to {@code null}.
     */
    public StartTailAction() {
        super();
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
    public StartTailAction(@Nullable @NlsActions.ActionText String text, @Nullable @NlsActions.ActionDescription String description,
                           @Nullable Icon icon, ToolWindow toolWindow) {
        super(text, description, icon);
        this.toolWindow = toolWindow;
    }


}
