package cn.com.pism.batslog.ui.tablehelp;

import cn.com.pism.batslog.BatsLogBundle;
import cn.com.pism.batslog.model.EnabledRgbColor;
import cn.com.pism.batslog.model.RgbColor;
import cn.com.pism.batslog.ui.Callback;
import cn.com.pism.batslog.ui.component.EnabledColorButton;
import cn.com.pism.batslog.util.BatsLogUtil;
import com.intellij.openapi.project.Project;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

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

    private EnabledColorButton button;

    private Project project;

    private Consumer<RgbColor> consumer;

    private Callback<Row> enabled;

    private boolean colorEnabled;

    public MyColorButtonEditor(Project project, Consumer<RgbColor> consumer, Callback<Row> enabled) {
        super(new JTextField());
        this.setClickCountToStart(1);
        this.project = project;
        this.consumer = consumer;
        this.enabled = enabled;
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
        EnabledRgbColor enabledRgbColor = (EnabledRgbColor) value;
        this.colorEnabled = enabledRgbColor.isEnabledColor();
        this.button = new EnabledColorButton(project, BatsLogUtil.toColor(enabledRgbColor.getRgbColor()), 18, 18,
                BatsLogBundle.message("config.form.console.enabledKeyWordColor.tips"),
                choseColor -> {
                    TableModel model = table.getModel();
                    RgbColor rgbColor = new RgbColor(choseColor.getRed(), choseColor.getGreen(), choseColor.getBlue());
                    model.setValueAt(new EnabledRgbColor(this.colorEnabled, rgbColor), row, column);
                    consumer.accept(rgbColor);
                },
                colorIsEnabled -> {
                    this.colorEnabled = colorIsEnabled;
                    enabled.call(new Row((String) table.getModel().getValueAt(row, 0), colorIsEnabled));
                });
        this.button.getColorButton().setBounds(new Rectangle(18, 18));
        this.button.setEnabledColor(enabledRgbColor.isEnabledColor());
        initPanel();
        this.panel.add(this.button.getRoot());

        return this.panel;
    }

    @Override
    public Object getCellEditorValue() {
        Color selectColor = this.button.getColorButton().getSelectColor();
        RgbColor rgbColor = new RgbColor(selectColor.getRed(), selectColor.getGreen(), selectColor.getBlue());
        return new EnabledRgbColor(this.colorEnabled, rgbColor);
    }

    @Data
    @NoArgsConstructor
    @AllArgsConstructor
    public static class Row {
        private String id;
        private boolean enabled;
    }
}
