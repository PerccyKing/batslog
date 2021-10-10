package cn.com.pism.batslog.action;

import cn.com.pism.batslog.settings.BatsLogSettingState;
import cn.com.pism.batslog.ui.FormatConsole;
import cn.com.pism.batslog.util.BatsLogUtil;
import com.intellij.openapi.project.Project;
import com.intellij.openapi.startup.StartupActivity;
import org.jetbrains.annotations.NotNull;

import static cn.com.pism.batslog.util.BatsLogUtil.FORMAT_CONSOLE_MAP;

/**
 * @author PerccyKing
 * @version 0.0.1
 * @date 2021/10/10 下午 06:28
 * @since 0.0.1
 */
public class StartUpAction implements StartupActivity {
    @Override
    public void runActivity(@NotNull Project project) {

        //项目默认监听状态
        BatsLogUtil.TAIL_STATUS.put(project, Boolean.FALSE);

        //项目默认监听状态
        BatsLogSettingState service = BatsLogSettingState.getInstance(project);
        if (Boolean.TRUE.equals(service.getStartWithProject())) {
            BatsLogUtil.TAIL_STATUS.put(project, Boolean.TRUE);
            FORMAT_CONSOLE_MAP.put(project, new FormatConsole(project));
        }

    }
}
