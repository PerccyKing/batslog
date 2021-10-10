package cn.com.pism.batslog.settings;

import com.intellij.openapi.components.PersistentStateComponent;
import com.intellij.openapi.components.ServiceManager;
import com.intellij.openapi.components.State;
import com.intellij.openapi.components.Storage;
import com.intellij.util.xmlb.XmlSerializerUtil;
import lombok.Data;
import lombok.EqualsAndHashCode;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

import java.io.Serializable;

/**
 * @author PerccyKing
 * @version 0.0.1
 * @date 2021/10/07 下午 06:32
 * @since 0.0.1
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
        return ServiceManager.getService(BatsLogGlobalConfigState.class);
    }
}
