package cn.com.pism.batslog.factory;

import cn.com.pism.batslog.util.BatsLogUtil;
import cn.com.pism.batslog.util.StringUtil;
import cn.com.pism.batslog.ui.FormatConsole;
import cn.com.pism.batslog.ui.SettingForm;
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
        SettingForm settingForm = new SettingForm(project);
        ContentFactory contentFactory = ContentFactory.SERVICE.getInstance();
        Content formatConsoleContent = contentFactory.createContent(formatConsole.getRoot(), StringUtil.encoding("控制台"), false);
        Content settingFormContent = contentFactory.createContent(settingForm.getRoot(), StringUtil.encoding("设置"), false);
        toolWindow.getContentManager().addContent(formatConsoleContent);
        toolWindow.getContentManager().addContent(settingFormContent);
        BatsLogUtil.TOOL_WINDOW.setTitleActions(BatsLogUtil.START_ACTION);
    }
}
