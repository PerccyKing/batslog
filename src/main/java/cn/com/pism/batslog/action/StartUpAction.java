package cn.com.pism.batslog.action;

import cn.com.pism.batslog.BatsLogBundle;
import cn.com.pism.batslog.constants.BatsLogConstant;
import cn.com.pism.batslog.settings.BatsLogGlobalConfigState;
import cn.com.pism.batslog.settings.BatsLogSettingState;
import cn.com.pism.batslog.ui.FormatConsole;
import cn.com.pism.batslog.ui.Notifier;
import cn.com.pism.batslog.util.GlobalVar;
import cn.hutool.core.thread.ThreadUtil;
import cn.hutool.http.HttpUtil;
import com.alibaba.fastjson2.JSON;
import com.alibaba.fastjson2.JSONArray;
import com.alibaba.fastjson2.JSONObject;
import com.intellij.ide.BrowserUtil;
import com.intellij.ide.plugins.IdeaPluginDescriptor;
import com.intellij.ide.plugins.PluginManagerCore;
import com.intellij.notification.Notification;
import com.intellij.notification.NotificationType;
import com.intellij.openapi.actionSystem.AnAction;
import com.intellij.openapi.actionSystem.AnActionEvent;
import com.intellij.openapi.project.Project;
import com.intellij.openapi.startup.StartupActivity;
import org.jetbrains.annotations.NotNull;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.List;

import static cn.com.pism.batslog.constants.BatsLogConstant.BATS_LOG_ID;

/**
 * @author PerccyKing
 * @version 0.0.1
 * @since 2021/10/10 下午 06:28
 */
public class StartUpAction implements StartupActivity {
    private static final Logger log = LoggerFactory.getLogger(StartUpAction.class);

    @Override
    public void runActivity(@NotNull Project project) {

        //项目默认监听状态
        BatsLogSettingState service = BatsLogSettingState.getInstance(project);
        //当监听状态默认开启时，需要修改启动按钮状态，并将sql console实例化
        Boolean useGlobalConfig = service.getUseGlobalConfig();
        Boolean startWithProject = Boolean.FALSE;
        if (Boolean.TRUE.equals(useGlobalConfig)) {
            startWithProject = BatsLogGlobalConfigState.getInstance().getStartWithProject();
        }
        if (Boolean.TRUE.equals(startWithProject)) {
            GlobalVar.putTailStatus(project, Boolean.TRUE);
            FormatConsole formatConsole = GlobalVar.getFormatConsole(project);
            //如果console没有实例化需要做一次实例化
            if (formatConsole == null) {
                GlobalVar.putFormatConsole(project, new FormatConsole(project, null));
            }
        }
        try {
            ThreadUtil.execAsync(() -> versionCheck(project));
        } catch (Exception e) {
            log.error(e.getMessage());
        }
    }

    private void versionCheck(Project project) {
        String res = HttpUtil.get("https://plugins.jetbrains.com/api/plugins/15301/updates?channel=&size=1", 1000 * 60);
        log.info("version:{}", res);
        JSONArray jsonArray = JSON.parseArray(res);
        if (jsonArray != null && !jsonArray.isEmpty()) {
            JSONObject jsonObject = jsonArray.getJSONObject(0);
            String version = jsonObject.getString("version");
            List<? extends IdeaPluginDescriptor> loadedPlugins = PluginManagerCore.getLoadedPlugins();
            loadedPlugins.forEach(lp -> handlePluginVersion(project, version, lp));
        }
    }

    private void handlePluginVersion(Project project, String version, IdeaPluginDescriptor lp) {
        if (BATS_LOG_ID.equals(lp.getPluginId().getIdString())) {
            //进行版本比较
            String pluginVersion = lp.getVersion();
            String[] versionNum = version.split("-");
            String[] pluginVersionNum = pluginVersion.split("-");
            long publishVersionInt = Long.parseLong(versionNum[0].replace(".", ""));
            long pluginVersionInt = Long.parseLong(pluginVersionNum[0].replace(".", ""));
            if (publishVersionInt > pluginVersionInt) {
                //弹出更新提示
                final Notification notifyInfo = Notifier.getInstance(NotificationType.INFORMATION);
                notifyInfo.setTitle(BatsLogConstant.BATS_LOG_NAME, BatsLogBundle.message("batslog.versionCheck.notification.title"));
                notifyInfo.setContent(pluginVersion + " → " + version);
                notifyInfo.addAction(new AnAction(BatsLogBundle.message("batslog.versionCheck.action.getNewVersion")) {
                    @Override
                    public void actionPerformed(@NotNull AnActionEvent e) {
                        BrowserUtil.browse("https://plugins.jetbrains.com/plugin/15301-batslog");
                    }
                });
                notifyInfo.notify(project);
            }
        }
    }
}
