package cn.com.pism.batslog.settings;

import com.intellij.openapi.components.*;
import com.intellij.openapi.project.Project;
import com.intellij.util.xmlb.XmlSerializerUtil;
import lombok.Data;
import lombok.EqualsAndHashCode;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

import java.io.Serializable;

/**
 * @author PerccyKing
 * @version 0.0.1
 * @date 2021/01/06 下午 08:47
 * @since 0.0.1
 */
@EqualsAndHashCode(callSuper = true)
@Data
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

    public static BatsLogSettingState getInstance(Project project) {
        return ServiceManager.getService(project, BatsLogSettingState.class);
    }
}
