package cn.com.pism.batslog.factory;

import cn.com.pism.batslog.util.BatsLogUtil;
import cn.com.pism.batslog.window.FormatConsole;
import com.intellij.openapi.project.Project;
import com.intellij.openapi.wm.ToolWindow;
import com.intellij.openapi.wm.ToolWindowFactory;
import com.intellij.ui.content.Content;
import com.intellij.ui.content.ContentFactory;
import org.jetbrains.annotations.NotNull;


/**
 * @author PerccyKing
 * @version 0.0.1
 * @date 2020/10/25 下午 12:18
 * @since 0.0.1
 */
public class BatsLogWindowFactory implements ToolWindowFactory {
    @Override
    public void createToolWindowContent(@NotNull Project project, @NotNull ToolWindow toolWindow) {
        BatsLogUtil.TOOL_WINDOW = toolWindow;
        FormatConsole formatConsole = new FormatConsole(project);
        ContentFactory contentFactory = ContentFactory.SERVICE.getInstance();
        Content content = contentFactory.createContent(formatConsole.getRoot(), "", false);
        toolWindow.getContentManager().addContent(content);
        BatsLogUtil.TOOL_WINDOW.setTitleActions(BatsLogUtil.START_ACTION);
    }
}
