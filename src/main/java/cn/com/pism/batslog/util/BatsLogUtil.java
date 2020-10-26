package cn.com.pism.batslog.util;

import cn.com.pism.batslog.action.ClearAllAction;
import cn.com.pism.batslog.action.StartTailAction;
import cn.com.pism.batslog.action.SuspendTailAction;
import com.intellij.execution.ui.ConsoleView;
import com.intellij.icons.AllIcons;
import com.intellij.openapi.actionSystem.AnAction;
import com.intellij.openapi.wm.ToolWindow;

import java.util.ArrayList;
import java.util.List;

/**
 * @author PerccyKing
 * @date 2020/10/26 下午 01:17
 */
public class BatsLogUtil {
    public static List<AnAction> START_ACTION;
    public static List<AnAction> SUSPEND_ACTION;
    public static ToolWindow TOOL_WINDOW;
    public static Boolean TAIL_STATUS = Boolean.FALSE;
    public static ConsoleView CONSOLE_VIEW;

    static {
        List<AnAction> anActions = new ArrayList<>();
        anActions.add(new StartTailAction(StringUtil.encoding("启动"), StringUtil.encoding("开启SQL监听"), AllIcons.Actions.Execute));
        anActions.add(new ClearAllAction(StringUtil.encoding("清空面板"), StringUtil.encoding("清空面板"), AllIcons.Actions.GC));

        START_ACTION = anActions;
        List<AnAction> suspend = new ArrayList<>();
        suspend.add(new SuspendTailAction(StringUtil.encoding("停止"), StringUtil.encoding("停止SQL监听"), AllIcons.Actions.Suspend));
        suspend.add(new ClearAllAction(StringUtil.encoding("清空面板"), StringUtil.encoding("清空面板"), AllIcons.Actions.GC));
        SUSPEND_ACTION = suspend;

    }
}
