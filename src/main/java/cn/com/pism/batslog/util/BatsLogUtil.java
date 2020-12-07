package cn.com.pism.batslog.util;

import cn.com.pism.batslog.action.*;
import com.intellij.execution.impl.ConsoleViewImpl;
import com.intellij.icons.AllIcons;
import com.intellij.openapi.actionSystem.AnAction;
import com.intellij.openapi.project.Project;
import com.intellij.openapi.wm.ToolWindow;

import javax.swing.*;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import static com.intellij.icons.AllIcons.RunConfigurations.Applet;
import static com.intellij.icons.AllIcons.RunConfigurations.Scroll_down;

/**
 * @author PerccyKing
 * @date 2020/10/26 下午 01:17
 */
public class BatsLogUtil {
    public static List<AnAction> START_ACTION;
    public static List<AnAction> SUSPEND_ACTION;
    public static ToolWindow TOOL_WINDOW;
    public static Boolean TAIL_STATUS = Boolean.FALSE;
    public static Map<Project, ConsoleViewImpl> CONSOLE_VIEW_MAP = new HashMap<>();
    public static JScrollBar PANE_BAR;

    public static String PREPARING = "Preparing:";
    public static String PARAMETERS = "Parameters:";

    public static List<String> SOURCE_SQL_LIST = new ArrayList<>();

    public static Map<Project, List<String>> SQL_CACHE = new HashMap<>();

    static {
        ClearAllAction clearAllAction = new ClearAllAction(StringUtil.encoding("清空面板"), StringUtil.encoding("清空面板"), AllIcons.Actions.GC);
        ScrollToEndAction scrollToEndAction = new ScrollToEndAction(StringUtil.encoding("最新"), StringUtil.encoding("滑动到最新行"), Scroll_down);
        OpenFormatWindowAction openFormatWindowAction = new OpenFormatWindowAction(StringUtil.encoding("最新"), StringUtil.encoding("滑动到最新行"), Applet);

        List<AnAction> anActions = new ArrayList<>();
        anActions.add(new StartTailAction(StringUtil.encoding("启动"), StringUtil.encoding("开启SQL监听"), AllIcons.Actions.Execute));
        anActions.add(clearAllAction);
        anActions.add(scrollToEndAction);
        anActions.add(openFormatWindowAction);

        START_ACTION = anActions;
        List<AnAction> suspend = new ArrayList<>();
        suspend.add(new SuspendTailAction(StringUtil.encoding("停止"), StringUtil.encoding("停止SQL监听"), AllIcons.Actions.Suspend));
        suspend.add(clearAllAction);
        suspend.add(scrollToEndAction);
        suspend.add(openFormatWindowAction);
        SUSPEND_ACTION = suspend;

    }
}
