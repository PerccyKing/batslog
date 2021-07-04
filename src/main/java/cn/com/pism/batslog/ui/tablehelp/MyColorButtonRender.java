package cn.com.pism.batslog.ui.tablehelp;

import cn.com.pism.batslog.settings.RgbColor;
import cn.com.pism.batslog.ui.ColorButton;
import cn.com.pism.batslog.util.BatsLogUtil;
import com.intellij.openapi.project.Project;

import javax.swing.*;
import javax.swing.table.TableCellRenderer;
import java.awt.*;

/**
 * @author PerccyKing
 * @version 0.0.1
 * @date 2021/06/27 下午 05:55
 * @since 0.0.1
 */
public class MyColorButtonRender implements TableCellRenderer {

    private JPanel panel;

    private ColorButton button;
    private Project project;

    public MyColorButtonRender(Project project) {
        this.project = project;
    }

    private void initButton() {

    }

    private void initPanel() {
        this.panel = new JPanel();
        this.panel.setLayout(new FlowLayout());
    }

    @Override
    public Component getTableCellRendererComponent(JTable table, Object value, boolean isSelected, boolean hasFocus, int row, int column) {
        RgbColor color = (RgbColor) value;
        this.button = new ColorButton(project, BatsLogUtil.toColor(color), 18, 18, choseColor -> {
        });
        this.button.setBounds(new Rectangle(18, 18));

        initPanel();
        this.panel.add(this.button);

        return this.panel;
    }
}
