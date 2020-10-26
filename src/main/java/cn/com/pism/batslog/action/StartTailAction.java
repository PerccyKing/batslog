package cn.com.pism.batslog.action;

import cn.com.pism.batslog.util.BatsLogUtil;
import com.intellij.openapi.actionSystem.AnAction;
import com.intellij.openapi.actionSystem.AnActionEvent;
import com.intellij.openapi.util.NlsActions;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

import javax.swing.*;

/**
 * @author wangyihuai
 */
public class StartTailAction extends AnAction {


    @Override
    public void actionPerformed(@NotNull AnActionEvent e) {
        BatsLogUtil.TAIL_STATUS = Boolean.TRUE;
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
    public StartTailAction(@Nullable @NlsActions.ActionText String text, @Nullable @NlsActions.ActionDescription String description,
                           @Nullable Icon icon) {
        super(text, description, icon);
    }


}
