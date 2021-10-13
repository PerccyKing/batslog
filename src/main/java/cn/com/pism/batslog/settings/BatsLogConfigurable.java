package cn.com.pism.batslog.settings;

import cn.com.pism.batslog.constants.BatsLogConstant;
import cn.com.pism.batslog.ui.SettingForm;
import cn.com.pism.batslog.util.BatsLogUtil;
import com.intellij.openapi.options.ConfigurationException;
import com.intellij.openapi.options.SearchableConfigurable;
import org.jetbrains.annotations.Nls;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

import javax.swing.*;

/**
 * @author PerccyKing
 * @version 0.0.1
 * @date 2021/10/07 下午 04:53
 * @since 0.0.1
 */
public class BatsLogConfigurable implements SearchableConfigurable {

    private SettingForm settingForm;

    @NotNull
    @Override
    public String getId() {
        return BatsLogConstant.BATS_LOG_NAME;
    }

    @Nls(capitalization = Nls.Capitalization.Title)
    @Override
    public String getDisplayName() {
        return getId();
    }

    @Nullable
    @Override
    public JComponent createComponent() {
        if (settingForm == null) {
            settingForm = new SettingForm();
        }
        return new JLabel("开发中...");
    }

    @Override
    public boolean isModified() {
        return false;
    }

    @Override
    public void apply() throws ConfigurationException {

    }
}
