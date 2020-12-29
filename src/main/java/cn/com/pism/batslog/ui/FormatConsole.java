package cn.com.pism.batslog.ui;

import cn.com.pism.batslog.BatsLogBundle;
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
import lombok.Data;

import javax.swing.*;

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
    private JPanel toolBar;

    public FormatConsole(Project project) {

        //初始化console的action
        initConsoleAction(project);

        MyConsoleViewImpl consoleView = new MyConsoleViewImpl(project, true);
        JComponent component = consoleView.getComponent();
        ActionToolbar actionToolBar = consoleView.createActionToolBar(ActionPlaces.UNKNOWN, true, BatsLogUtil.TAIL_ACTION.get(project));
        consoleView.installPopupHandler(consoleView.getActionToolbar().getActions());
        toolBar.add(actionToolBar.getComponent());
        sqlPanel.add(component);
        sqlPanel.setBorder(null);
        consoleView.print(StringUtil.encoding(BATS_LOG), ConsoleViewContentType.ERROR_OUTPUT);
        BatsLogUtil.CONSOLE_VIEW_MAP.put(project, consoleView);
    }

    private void initConsoleAction(Project project) {
        OpenFormatWindowAction openFormatWindowAction = new OpenFormatWindowAction(BatsLogBundle.message("formatWindow"), BatsLogBundle.message("formatWindow"), Applet);

        TailAction tailAction = new TailAction(BatsLogBundle.message("start"), BatsLogBundle.message("startSqlListener"), AllIcons.Actions.Execute);
        List<AnAction> anActions = new ArrayList<>();
        anActions.add(tailAction);
        anActions.add(openFormatWindowAction);

        BatsLogUtil.TAIL_ACTION.put(project, anActions.toArray(new AnAction[0]));
    }
}
