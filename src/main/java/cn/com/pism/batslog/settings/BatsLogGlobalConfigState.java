package cn.com.pism.batslog.settings;

import cn.com.pism.batslog.model.ConsoleColorConfig;
import com.intellij.openapi.components.PersistentStateComponent;
import com.intellij.openapi.components.State;
import com.intellij.openapi.components.Storage;
import com.intellij.util.xmlb.XmlSerializerUtil;
import lombok.Data;
import lombok.EqualsAndHashCode;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

import java.io.Serializable;
import java.util.List;

import static cn.com.pism.batslog.util.GlobalVar.getDefaultColorConfigs;
import static cn.com.pism.batslog.util.IntellijServiceUtil.getService;

/**
 * @author PerccyKing
 * @version 0.0.1
 * @since 2021/10/07 下午 06:32
 */
@EqualsAndHashCode(callSuper = true)
@Data
@State(name = "BatsLogGlobalConfigState", storages = @Storage(value = "$APP_CONFIG$/BatsLogGlobal.xml"))
public class BatsLogGlobalConfigState extends BatsLogConfig implements PersistentStateComponent<BatsLogGlobalConfigState>, Serializable {
    @Nullable
    @Override
    public BatsLogGlobalConfigState getState() {
        return this;
    }

    @Override
    public void loadState(@NotNull BatsLogGlobalConfigState state) {
        XmlSerializerUtil.copyBean(state, this);
    }

    public static BatsLogGlobalConfigState getInstance() {
        return getService(null, BatsLogGlobalConfigState.class);
    }

    @Override
    public Boolean getUseGlobalConfig() {
        return Boolean.FALSE;
    }

    @Override
    public List<ConsoleColorConfig> getColorConfigs() {
        List<ConsoleColorConfig> colorConfigs = super.getColorConfigs();
        if (colorConfigs == null || colorConfigs.isEmpty()) {
            return getDefaultColorConfigs();
        }
        return super.getColorConfigs();
    }
}
