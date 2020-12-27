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
import java.awt.*;
import java.util.ArrayList;

import static cn.com.pism.batslog.constants.BatsLogConstant.BATS_LOG;

/**
 * @author PerccyKing
 * @version 0.0.1
 * @date 2020/10/25 上午 11:48
 * @since 0.0.1
 */
@Data
public class FormatConsole {
    private JPanel root;
    private JPanel sqlPanel;
    private JScrollPane sqlPane;
    private JPanel toolBar;

    public FormatConsole(Project project) {
        SimpleToolWindowPanel simpleToolWindowPanel = new SimpleToolWindowPanel(false, true);
        MyConsoleViewImpl consoleView = new MyConsoleViewImpl(project, true);
        simpleToolWindowPanel.setContent(consoleView.getComponent());
        ContentFactory contentFactory = ContentFactory.SERVICE.getInstance();
        Content content = contentFactory.createContent(simpleToolWindowPanel, "", false);
        sqlPanel.add(content.getComponent());
        JScrollBar paneBar = sqlPane.getVerticalScrollBar();
        BatsLogUtil.PANE_BAR = paneBar;
        paneBar.setUnitIncrement(16);
        consoleView.print(StringUtil.encoding(BATS_LOG), ConsoleViewContentType.ERROR_OUTPUT);
        consoleView.installPopupHandler(BatsLogUtil.START_ACTION);
        ActionToolbar actionToolBar = consoleView.createActionToolBar(ActionPlaces.UNKNOWN, true, BatsLogUtil.START_ACTION);
        toolBar.add(actionToolBar.getComponent());
        BatsLogUtil.CONSOLE_VIEW_MAP.put(project, consoleView);
    }
}
