package cn.com.pism.batslog.ui.component;

import cn.com.pism.batslog.ui.Callback;
import cn.com.pism.batslog.ui.ColorButton;
import com.intellij.openapi.project.Project;
import lombok.Data;

import javax.swing.*;
import java.awt.*;

/**
 * 带启用禁用的button
 *
 * @author PerccyKing
 * @version 0.0.1
 * @since 2022/04/14 下午 03:04
 */
@Data
public class EnabledColorButton {
    private ColorButton colorButton;
    private JCheckBox enableCheckBox;

    private Project project;

    private Color color;

    private JPanel root;

    private int width;
    private int height;
    private Callback<Color> colorCallback;

    Callback<Boolean> enabled;

    private String enabledTips;

    private boolean enabledColor;

    public EnabledColorButton(Project project, Color color, int width, int height, String tips, Callback<Color> colorCallback, Callback<Boolean> enabled) {
        this.project = project;
        this.color = color;
        this.width = width;
        this.height = height;
        this.colorCallback = colorCallback;
        this.enabled = enabled;
        this.enabledTips = tips;
        enableCheckBox.addActionListener(e -> {
            if (enabled != null) {
                this.enabledColor = enableCheckBox.isSelected();
                enabled.call(enableCheckBox.isSelected());
            }
        });
        enableCheckBox.setToolTipText(tips);
    }

    public Color getColor() {
        if (!enableCheckBox.isSelected()) {
            return null;
        }
        return color;
    }

    @SuppressWarnings({"all"})
    private void createUIComponents() {
        this.colorButton = new ColorButton(color, width, height, colorCallback);
    }

    public void setEnabledColor(boolean enabledColor) {
        this.enabledColor = enabledColor;
        this.enableCheckBox.setSelected(enabledColor);
    }

    public void setEnableCheckBox(boolean selected) {
        this.enableCheckBox.setSelected(selected);
    }
}
