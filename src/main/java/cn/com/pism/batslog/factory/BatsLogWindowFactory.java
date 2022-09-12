package cn.com.pism.batslog.factory;

import cn.com.pism.batslog.BatsLogBundle;
import cn.com.pism.batslog.action.CopySqlAction;
import cn.com.pism.batslog.action.FormatSqlAction;
import cn.com.pism.batslog.model.ConsoleColorConfig;
import cn.com.pism.batslog.model.RgbColor;
import cn.com.pism.batslog.settings.BatsLogSettingState;
import cn.com.pism.batslog.ui.ErrorListPanel;
import cn.com.pism.batslog.ui.FormatConsole;
import cn.com.pism.batslog.ui.MyConsoleViewImpl;
import cn.com.pism.batslog.ui.SettingForm;
import cn.com.pism.batslog.util.BatsLogUtil;
import cn.com.pism.batslog.util.ConsoleColorConfigUtil;
import com.intellij.openapi.actionSystem.ActionManager;
import com.intellij.openapi.project.Project;
import com.intellij.openapi.wm.ToolWindow;
import com.intellij.openapi.wm.ToolWindowAnchor;
import com.intellij.openapi.wm.ToolWindowFactory;
import com.intellij.openapi.wm.ex.ToolWindowEx;
import com.intellij.ui.content.Content;
import com.intellij.ui.content.ContentFactory;
import com.intellij.ui.content.ContentManager;
import icons.BatsLogIcons;
import org.jetbrains.annotations.NotNull;

import java.util.ArrayList;
import java.util.List;

import static cn.com.pism.batslog.util.BatsLogUtil.FORMAT_CONSOLE_MAP;


/**
 * @author PerccyKing
 * @version 0.0.1
 * @date 2020/10/25 下午 12:18
 * @since 0.0.1
 */
public class BatsLogWindowFactory implements ToolWindowFactory {
    @Override
    public void createToolWindowContent(@NotNull Project project, @NotNull ToolWindow toolWindow) {

        BatsLogUtil.TOOL_WINDOW = (ToolWindowEx) toolWindow;
        FormatConsole formatConsole = FORMAT_CONSOLE_MAP.get(project);
        BatsLogSettingState service = BatsLogSettingState.getInstance(project);
        //当监听状态默认开启时，修改启动按钮状态
        if (Boolean.TRUE.equals(service.getStartWithProject())) {
            BatsLogUtil.TAIL_STATUS.put(project, Boolean.TRUE);
        }
        //当sql console没有实例化，将其实例化
        if (formatConsole == null) {
            formatConsole = new FormatConsole(project);
            FORMAT_CONSOLE_MAP.put(project, formatConsole);
        }
        boolean inBottom = ToolWindowAnchor.BOTTOM.equals(toolWindow.getAnchor());
        formatConsole.initConsoleToComponent(project, (MyConsoleViewImpl) BatsLogUtil.CONSOLE_VIEW_MAP.get(project), inBottom);
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

        List<ConsoleColorConfig> colorConfigs = service.getColorConfigs();
        if (colorConfigs == null || colorConfigs.isEmpty()) {
            colorConfigs = new ArrayList<>();
            colorConfigs.add(new ConsoleColorConfig("1", 1, "INSERT", new RgbColor(41, 204, 152), true, new RgbColor(255, 255, 255), true, true));
            colorConfigs.add(new ConsoleColorConfig("2", 2, "UPDATE", new RgbColor(118, 147, 255), true, new RgbColor(255, 255, 255), true, true));
            colorConfigs.add(new ConsoleColorConfig("3", 3, "DELETE", new RgbColor(255, 137, 151), true, new RgbColor(255, 255, 255), true, true));
            service.setColorConfigs(colorConfigs);
        }
        BatsLogUtil.KEY_COLOR_MAP = ConsoleColorConfigUtil.toConsoleViewContentTypeMap(project, colorConfigs);
    }

}
