package cn.com.pism.batslog.ui.tablehelp;

import cn.com.pism.batslog.model.RgbColor;
import cn.com.pism.batslog.ui.ColorButton;
import cn.com.pism.batslog.util.BatsLogUtil;
import com.intellij.openapi.project.Project;

import javax.swing.*;
import javax.swing.table.TableModel;
import java.awt.*;
import java.util.function.Consumer;

/**
 * @author PerccyKing
 * @version 0.0.1
 * @date 2021/06/27 下午 05:55
 * @since 0.0.1
 */
public class MyColorButtonEditor extends DefaultCellEditor {
    private JPanel panel;

    private ColorButton button;

    private Project project;

    private Consumer<RgbColor> consumer;

    public MyColorButtonEditor(Project project, Consumer<RgbColor> consumer) {
        super(new JTextField());
        this.setClickCountToStart(1);
        this.project = project;
        this.consumer = consumer;
        fireEditingCanceled();
    }

    private void initButton() {

    }

    private void initPanel() {
        this.panel = new JPanel();
        this.panel.setLayout(new FlowLayout());
    }

    @Override
    public Component getTableCellEditorComponent(JTable table, Object value, boolean isSelected, int row, int column) {
        this.button = new ColorButton(project, BatsLogUtil.toColor((RgbColor) value), 18, 18, choseColor -> {
            TableModel model = table.getModel();
            RgbColor rgbColor = new RgbColor(choseColor.getRed(), choseColor.getGreen(), choseColor.getBlue());
            model.setValueAt(rgbColor, row, column);
            consumer.accept(rgbColor);
        });
        this.button.setBounds(new Rectangle(18, 18));

        initPanel();
        this.panel.add(this.button);

        return this.panel;
    }

    @Override
    public Object getCellEditorValue() {
        Color selectColor = this.button.getSelectColor();

        return new RgbColor(selectColor.getRed(), selectColor.getGreen(), selectColor.getBlue());
    }
}
