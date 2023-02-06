package cn.com.pism.batslog.ui.tablehelp;

import cn.com.pism.batslog.BatsLogBundle;
import cn.com.pism.batslog.model.EnabledRgbColor;
import cn.com.pism.batslog.ui.component.EnabledColorButton;
import cn.com.pism.batslog.util.BatsLogUtil;
import com.intellij.openapi.project.Project;

import javax.swing.*;
import javax.swing.table.TableCellRenderer;
import java.awt.*;

/**
 * @author PerccyKing
 * @version 0.0.1
 * @since 2021/06/27 下午 05:55
 */
public class MyColorButtonRender implements TableCellRenderer {

    private JPanel panel;

    private final Project project;

    public MyColorButtonRender(Project project) {
        this.project = project;
    }


    private void initPanel() {
        this.panel = new JPanel();
        this.panel.setLayout(new FlowLayout());
    }

    @Override
    public Component getTableCellRendererComponent(JTable table, Object value, boolean isSelected, boolean hasFocus, int row, int column) {
        EnabledRgbColor color = (EnabledRgbColor) value;
        EnabledColorButton button = new EnabledColorButton(project, BatsLogUtil.toColor(color.getRgbColor()), 18, 18,
                BatsLogBundle.message("config.form.console.enabledKeyWordColor.tips") + "render",
                choseColor -> {
                }, e -> {
        });
        button.getColorButton().setBounds(new Rectangle(18, 18));
        button.setEnabledColor(color.isEnabledColor());
        initPanel();
        this.panel.add(button.getRoot());

        return this.panel;
    }
}
