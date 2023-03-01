package cn.com.pism.batslog.settings;

import com.intellij.openapi.components.PersistentStateComponent;
import com.intellij.openapi.components.RoamingType;
import com.intellij.openapi.components.State;
import com.intellij.openapi.components.Storage;
import com.intellij.openapi.project.Project;
import com.intellij.util.xmlb.XmlSerializerUtil;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.extern.slf4j.Slf4j;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

import java.io.Serializable;

import static cn.com.pism.batslog.util.IntellijServiceUtil.getService;

/**
 * @author PerccyKing
 * @version 0.0.1
 * @since 2021/01/06 下午 08:47
 */
@EqualsAndHashCode(callSuper = true)
@Data
@Slf4j
@State(name = "BatsLogSettingState", storages = {
        @Storage(value = "BatsLog.xml", roamingType = RoamingType.DISABLED)
})
public class BatsLogSettingState extends BatsLogConfig implements PersistentStateComponent<BatsLogSettingState>, Serializable {

    @Nullable
    @Override
    public BatsLogSettingState getState() {
        return this;
    }

    @Override
    public void loadState(@NotNull BatsLogSettingState state) {
        XmlSerializerUtil.copyBean(state, this);
    }

    public static BatsLogConfig getInstance(Project project) {
        BatsLogConfig config = getService(project, BatsLogSettingState.class);
        if (Boolean.TRUE.equals(config.getUseGlobalConfig())) {
            config = BatsLogGlobalConfigState.getInstance();
        }
        return config;
    }

    public static BatsLogSettingState getDefaultInstance(Project project) {
        return getService(project, BatsLogSettingState.class);
    }
}
