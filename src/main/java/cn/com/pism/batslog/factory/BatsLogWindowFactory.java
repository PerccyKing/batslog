package cn.com.pism.batslog.factory;

import cn.com.pism.batslog.action.ClearAllAction;
import cn.com.pism.batslog.action.StartTailAction;
import cn.com.pism.batslog.util.StringUtil;
import cn.com.pism.batslog.window.FormatConsole;
import com.intellij.icons.AllIcons;
import com.intellij.openapi.actionSystem.AnAction;
import com.intellij.openapi.project.Project;
import com.intellij.openapi.wm.ToolWindow;
import com.intellij.openapi.wm.ToolWindowFactory;
import com.intellij.ui.content.Content;
import com.intellij.ui.content.ContentFactory;
import org.jetbrains.annotations.NotNull;

import java.util.ArrayList;
import java.util.List;


/**
 * @author PerccyKing
 * @version 0.0.1
 * @date 2020/10/25 下午 12:18
 * @since 0.0.1
 */
public class BatsLogWindowFactory implements ToolWindowFactory {
    @Override
    public void createToolWindowContent(@NotNull Project project, @NotNull ToolWindow toolWindow) {
        FormatConsole formatConsole = new FormatConsole();
        ContentFactory contentFactory = ContentFactory.SERVICE.getInstance();
        Content content = contentFactory.createContent(formatConsole.getRoot(), "", false);
        toolWindow.getContentManager().addContent(content);
        List<AnAction> anActions = new ArrayList<>();
        StartTailAction startTailAction = new StartTailAction(StringUtil.encoding("启动"), StringUtil.encoding("开启SQL监听"), AllIcons.Actions.Execute, toolWindow);
        anActions.add(startTailAction);
        anActions.add(new ClearAllAction(StringUtil.encoding("清空面板"), StringUtil.encoding("开启SQL监听"), AllIcons.Actions.GC));
        toolWindow.setTitleActions(anActions);
    }
}
