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
 * @date 2022/04/14 下午 03:04
 * @since 0.0.1
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

    public EnabledColorButton(Project project, Color color, int width, int height, Callback<Color> colorCallback, Callback<Boolean> enabled) {
        this.project = project;
        this.color = color;
        this.width = width;
        this.height = height;
        this.colorCallback = colorCallback;
        this.enabled = enabled;
        enableCheckBox.addActionListener(e -> {
            if (enabled != null) {
                enabled.call(enableCheckBox.isSelected());
            }
        });
    }

    public Color getColor() {
        if (!enableCheckBox.isSelected()) {
            return null;
        }
        return color;
    }

    private void createUIComponents() {
        this.colorButton = new ColorButton(project, color, width, height, colorCallback);
    }

    public void setEnableCheckBox(boolean selected) {
        this.enableCheckBox.setSelected(selected);
    }
}
