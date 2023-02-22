package cn.com.pism.batslog.factory;

import cn.com.pism.batslog.BatsLogBundle;
import cn.com.pism.batslog.action.CopySqlAction;
import cn.com.pism.batslog.action.FormatSqlAction;
import cn.com.pism.batslog.settings.BatsLogConfig;
import cn.com.pism.batslog.settings.BatsLogGlobalConfigState;
import cn.com.pism.batslog.settings.BatsLogSettingState;
import cn.com.pism.batslog.ui.ErrorListPanel;
import cn.com.pism.batslog.ui.FormatConsole;
import cn.com.pism.batslog.ui.MyConsoleViewImpl;
import cn.com.pism.batslog.ui.SettingForm;
import cn.com.pism.batslog.util.GlobalVar;
import com.intellij.openapi.actionSystem.ActionManager;
import com.intellij.openapi.project.Project;
import com.intellij.openapi.wm.ToolWindow;
import com.intellij.openapi.wm.ToolWindowAnchor;
import com.intellij.openapi.wm.ToolWindowFactory;
import com.intellij.ui.content.Content;
import com.intellij.ui.content.ContentFactory;
import com.intellij.ui.content.ContentManager;
import icons.BatsLogIcons;
import org.jetbrains.annotations.NotNull;


/**
 * @author PerccyKing
 * @version 0.0.1
 * @since 2020/10/25 下午 12:18
 */
public class BatsLogWindowFactory implements ToolWindowFactory {

    @Override
    public void createToolWindowContent(@NotNull Project project, @NotNull ToolWindow toolWindow) {

        FormatConsole formatConsole = GlobalVar.getFormatConsole(project);
        BatsLogConfig service = BatsLogSettingState.getInstance(project);
        if (Boolean.TRUE.equals(service.getUseGlobalConfig())) {
            service = BatsLogGlobalConfigState.getInstance();
        }
        //当监听状态默认开启时，修改启动按钮状态
        GlobalVar.putTailStatus(project, service.getStartWithProject());
        //当sql console没有实例化，将其实例化
        if (formatConsole == null) {
            formatConsole = new FormatConsole(project, toolWindow);
            GlobalVar.putFormatConsole(project, formatConsole);
        }
        boolean inBottom = ToolWindowAnchor.BOTTOM.equals(toolWindow.getAnchor());
        formatConsole.initConsoleToComponent(project, (MyConsoleViewImpl) GlobalVar.getConsoleView(project), inBottom);
        SettingForm settingForm = new SettingForm(project);
        ContentFactory contentFactory = ContentFactory.SERVICE.getInstance();
        Content formatConsoleContent = contentFactory.createContent(formatConsole.getRoot(), BatsLogBundle.message("batslog.console"), false);
        Content errorListContent = contentFactory.createContent(new ErrorListPanel(project).getRoot(), BatsLogBundle.message("batslog.error"), false);
        Content settingFormContent = contentFactory.createContent(settingForm.getRoot(), BatsLogBundle.message("batslog.config"), false);
        ContentManager contentManager = toolWindow.getContentManager();
        contentManager.addContent(formatConsoleContent);
        contentManager.addContent(settingFormContent);
        contentManager.addContent(errorListContent);
        ActionManager instance = ActionManager.getInstance();
        instance.replaceAction("$FormatSql", new FormatSqlAction(BatsLogBundle.message("batslog.action.formatSql"), "", BatsLogIcons.BATS_LOG));
        instance.replaceAction("$CopySql", new CopySqlAction(BatsLogBundle.message("batslog.action.copySql"), "", BatsLogIcons.BATS_LOG_COPY));

    }

}
