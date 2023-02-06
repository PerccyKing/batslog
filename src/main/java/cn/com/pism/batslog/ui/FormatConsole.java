package cn.com.pism.batslog.ui;

import cn.com.pism.batslog.BatsLogBundle;
import cn.com.pism.batslog.action.BeautyAction;
import cn.com.pism.batslog.action.CopyAction;
import cn.com.pism.batslog.action.OpenFormatWindowAction;
import cn.com.pism.batslog.action.TailAction;
import cn.com.pism.batslog.util.GlobalVar;
import cn.com.pism.batslog.util.StringUtil;
import com.intellij.execution.ui.ConsoleViewContentType;
import com.intellij.icons.AllIcons;
import com.intellij.openapi.actionSystem.ActionPlaces;
import com.intellij.openapi.actionSystem.ActionToolbar;
import com.intellij.openapi.actionSystem.AnAction;
import com.intellij.openapi.project.Project;
import com.intellij.openapi.wm.ToolWindow;
import com.intellij.openapi.wm.ToolWindowAnchor;
import icons.BatsLogIcons;
import lombok.Data;

import javax.swing.*;
import java.awt.*;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import static cn.com.pism.batslog.constants.BatsLogConstant.BATS_LOG;
import static com.intellij.icons.AllIcons.RunConfigurations.Applet;

/**
 * @author wangyihuai
 * @since 2020/12/28 13:07
 */
@Data
public class FormatConsole {
    private JPanel root;
    private JPanel sqlPanel;
    private JPanel parentPanel;
    private JPanel toolBar;


    private static final Map<Project, AnAction[]> TAIL_ACTION = new HashMap<>();

    public FormatConsole(Project project) {
        new FormatConsole(project, null);
    }

    public FormatConsole(Project project, ToolWindow toolWindow) {

        //初始化console的action
        initConsoleAction(project, toolWindow);

        MyConsoleViewImpl consoleView = new MyConsoleViewImpl(project, true);
        consoleView.print(StringUtil.encoding(BATS_LOG, project), ConsoleViewContentType.ERROR_OUTPUT);
        GlobalVar.putConsoleView(project, consoleView);

    }

    public void initConsoleToComponent(Project project, MyConsoleViewImpl consoleView, boolean inBottom) {
        JComponent component = consoleView.getComponent();
        load(project, consoleView, inBottom);
        sqlPanel.add(component);
        sqlPanel.setBorder(null);
    }

    private void load(Project project, MyConsoleViewImpl consoleView, boolean inBottom) {
        ActionToolbar actionToolBar = consoleView.createActionToolBar(ActionPlaces.UNKNOWN, !inBottom, TAIL_ACTION.get(project));
        if (toolBar == null) {
            consoleView.installPopupHandler(consoleView.getActionToolbar().getActions());
        }
        toolBar = new JPanel(new BorderLayout());
        toolBar.add(actionToolBar.getComponent());
        int a = 2;
        if (parentPanel.getComponents().length >= a) {
            parentPanel.remove(1);
        }
        if (inBottom) {
            parentPanel.add(toolBar, BorderLayout.WEST);
        } else {
            parentPanel.add(toolBar, BorderLayout.NORTH);
        }
        toolBar.setVisible(true);
        parentPanel.revalidate();
        parentPanel.repaint();
    }

    private void initConsoleAction(Project project, ToolWindow toolWindow) {
        OpenFormatWindowAction openFormatWindowAction = new OpenFormatWindowAction(BatsLogBundle.message("batslog.formatWindow"), BatsLogBundle.message("batslog.formatWindow"), Applet);

        TailAction tailAction = new TailAction(BatsLogBundle.message("start"), BatsLogBundle.message("startSqlListener"), AllIcons.Actions.Execute,
                () -> load(project, (MyConsoleViewImpl) GlobalVar.getConsoleView(project),
                        toolWindow != null && ToolWindowAnchor.BOTTOM.equals(toolWindow.getAnchor())));
        BeautyAction beautyAction = new BeautyAction("Beauty", "Beauty", BatsLogIcons.BEAUTY, project);
        List<AnAction> anActions = new ArrayList<>();
        anActions.add(tailAction);
        anActions.add(openFormatWindowAction);
        anActions.add(beautyAction);
        anActions.add(new CopyAction("Copy", "Copy selected text to clipboard", AllIcons.Actions.Copy));

        TAIL_ACTION.put(project, anActions.toArray(new AnAction[0]));
    }
}
