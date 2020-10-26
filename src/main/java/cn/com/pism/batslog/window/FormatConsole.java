package cn.com.pism.batslog.window;

import cn.com.pism.batslog.util.BatsLogUtil;
import cn.com.pism.batslog.util.StringUtil;
import com.intellij.execution.impl.ConsoleViewImpl;
import com.intellij.execution.ui.ConsoleViewContentType;
import com.intellij.openapi.project.Project;
import com.intellij.openapi.ui.SimpleToolWindowPanel;
import com.intellij.ui.content.Content;
import com.intellij.ui.content.ContentFactory;
import lombok.Data;

import javax.swing.*;

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

    public FormatConsole(Project project) {
        SimpleToolWindowPanel simpleToolWindowPanel = new SimpleToolWindowPanel(false, true);
        ConsoleViewImpl consoleView = new ConsoleViewImpl(project, true);
        simpleToolWindowPanel.setContent(consoleView.getComponent());
        ContentFactory contentFactory = ContentFactory.SERVICE.getInstance();
        Content content = contentFactory.createContent(simpleToolWindowPanel, "", false);
        sqlPanel.add(content.getComponent());
        consoleView.print(StringUtil.encoding("BatsLog已准备成功\n"), ConsoleViewContentType.ERROR_OUTPUT);
        BatsLogUtil.CONSOLE_VIEW = consoleView;
    }
}
