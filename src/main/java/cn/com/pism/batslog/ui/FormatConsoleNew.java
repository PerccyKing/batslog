package cn.com.pism.batslog.ui;

import cn.com.pism.batslog.util.BatsLogUtil;
import cn.com.pism.batslog.util.StringUtil;
import com.intellij.execution.ui.ConsoleViewContentType;
import com.intellij.openapi.actionSystem.ActionPlaces;
import com.intellij.openapi.actionSystem.ActionToolbar;
import com.intellij.openapi.project.Project;
import com.intellij.openapi.ui.SimpleToolWindowPanel;
import com.intellij.ui.content.Content;
import com.intellij.ui.content.ContentFactory;
import lombok.Data;

import javax.swing.*;

import static cn.com.pism.batslog.constants.BatsLogConstant.BATS_LOG;

/**
 * @author wangyihuai
 * @date 2020/12/28 13:07
 */
@Data
public class FormatConsoleNew {
    private JPanel root;
    private JPanel sqlPanel;
    private JPanel toolBar;

    public FormatConsoleNew(Project project) {
        MyConsoleViewImpl consoleView = new MyConsoleViewImpl(project, true);
        JComponent component = consoleView.getComponent();
        consoleView.installPopupHandler(BatsLogUtil.START_ACTION);
        ActionToolbar actionToolBar = consoleView.createActionToolBar(ActionPlaces.UNKNOWN, true, BatsLogUtil.START_ACTION);
        toolBar.add(actionToolBar.getComponent());
        sqlPanel.add(component);
        sqlPanel.setBorder(null);
        consoleView.print(StringUtil.encoding(BATS_LOG), ConsoleViewContentType.ERROR_OUTPUT);
        BatsLogUtil.CONSOLE_VIEW_MAP.put(project, consoleView);
    }
}
