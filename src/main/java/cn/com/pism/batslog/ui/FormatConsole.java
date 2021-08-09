package cn.com.pism.batslog.ui;

import cn.com.pism.batslog.BatsLogBundle;
import cn.com.pism.batslog.action.BeautyAction;
import cn.com.pism.batslog.action.OpenFormatWindowAction;
import cn.com.pism.batslog.action.TailAction;
import cn.com.pism.batslog.util.BatsLogUtil;
import cn.com.pism.batslog.util.StringUtil;
import com.intellij.execution.ui.ConsoleViewContentType;
import com.intellij.icons.AllIcons;
import com.intellij.openapi.actionSystem.ActionPlaces;
import com.intellij.openapi.actionSystem.ActionToolbar;
import com.intellij.openapi.actionSystem.AnAction;
import com.intellij.openapi.project.Project;
import com.intellij.openapi.wm.ToolWindowAnchor;
import icons.BatsLogIcons;
import lombok.Data;

import javax.swing.*;
import java.awt.*;
import java.util.ArrayList;
import java.util.List;

import static cn.com.pism.batslog.constants.BatsLogConstant.BATS_LOG;
import static com.intellij.icons.AllIcons.RunConfigurations.Applet;

/**
 * @author wangyihuai
 * @date 2020/12/28 13:07
 */
@Data
public class FormatConsole {
    private JPanel root;
    private JPanel sqlPanel;
    private JPanel parentPanel;
    private JPanel toolBar;

    public FormatConsole(Project project) {

        //初始化console的action
        initConsoleAction(project);

        MyConsoleViewImpl consoleView = new MyConsoleViewImpl(project, true);
        JComponent component = consoleView.getComponent();
        load(project, consoleView);
        sqlPanel.add(component);
        sqlPanel.setBorder(null);
        consoleView.print(StringUtil.encoding(BATS_LOG), ConsoleViewContentType.ERROR_OUTPUT);
        BatsLogUtil.CONSOLE_VIEW_MAP.put(project, consoleView);
    }

    private void load(Project project, MyConsoleViewImpl consoleView) {
        ToolWindowAnchor anchor = BatsLogUtil.TOOL_WINDOW.getAnchor();
        boolean inBottom = ToolWindowAnchor.BOTTOM.equals(anchor);
        ActionToolbar actionToolBar = consoleView.createActionToolBar(ActionPlaces.UNKNOWN, !inBottom, BatsLogUtil.TAIL_ACTION.get(project));
        if (toolBar == null) {
            consoleView.installPopupHandler(consoleView.getActionToolbar().getActions());
        }
        toolBar = new JPanel(new BorderLayout());
        toolBar.add(actionToolBar.getComponent());
        if (parentPanel.getComponents().length >= 2) {
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

    private void initConsoleAction(Project project) {
        OpenFormatWindowAction openFormatWindowAction = new OpenFormatWindowAction(BatsLogBundle.message("formatWindow"), BatsLogBundle.message("formatWindow"), Applet);

        TailAction tailAction = new TailAction(BatsLogBundle.message("start"), BatsLogBundle.message("startSqlListener"), AllIcons.Actions.Execute,
                () -> load(project, (MyConsoleViewImpl) BatsLogUtil.CONSOLE_VIEW_MAP.get(project)));

        BeautyAction beautyAction = new BeautyAction("Beauty", "Beauty", BatsLogIcons.BEAUTY, project);
        List<AnAction> anActions = new ArrayList<>();
        anActions.add(tailAction);
        anActions.add(openFormatWindowAction);
        anActions.add(beautyAction);

        BatsLogUtil.TAIL_ACTION.put(project, anActions.toArray(new AnAction[0]));
    }
}
