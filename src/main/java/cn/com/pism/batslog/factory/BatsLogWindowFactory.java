package cn.com.pism.batslog.factory;

import cn.com.pism.batslog.BatsLogBundle;
import cn.com.pism.batslog.action.CopySqlAction;
import cn.com.pism.batslog.action.FormatSqlAction;
import cn.com.pism.batslog.ui.FormatConsole;
import cn.com.pism.batslog.ui.SettingForm;
import cn.com.pism.batslog.util.BatsLogUtil;
import com.intellij.openapi.actionSystem.ActionManager;
import com.intellij.openapi.project.Project;
import com.intellij.openapi.wm.ToolWindow;
import com.intellij.openapi.wm.ToolWindowFactory;
import com.intellij.openapi.wm.ex.ToolWindowEx;
import com.intellij.ui.content.Content;
import com.intellij.ui.content.ContentFactory;
import com.intellij.ui.content.ContentManager;
import icons.BatsLogIcons;
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

        //项目默认监听状态
        BatsLogUtil.TAIL_STATUS.put(project, Boolean.FALSE);

        BatsLogUtil.TOOL_WINDOW = (ToolWindowEx) toolWindow;
        FormatConsole formatConsole = new FormatConsole(project);
        SettingForm settingForm = new SettingForm(project);
        ContentFactory contentFactory = ContentFactory.SERVICE.getInstance();
        Content formatConsoleContent = contentFactory.createContent(formatConsole.getRoot(), BatsLogBundle.message("console"), false);
        Content settingFormContent = contentFactory.createContent(settingForm.getRoot(), BatsLogBundle.message("consoleSetting"), false);
        ContentManager contentManager = toolWindow.getContentManager();
        contentManager.addContent(formatConsoleContent);
        contentManager.addContent(settingFormContent);
        ActionManager instance = ActionManager.getInstance();
        instance.replaceAction("$FormatSql", new FormatSqlAction(BatsLogBundle.message("formatSql"), "", BatsLogIcons.BATS_LOG));
        instance.replaceAction("$CopySql", new CopySqlAction(BatsLogBundle.message("copySql"), "", BatsLogIcons.BATS_LOG_COPY));
    }
}
