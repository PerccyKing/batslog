package cn.com.pism.batslog.ui.tablehelp;

import cn.com.pism.batslog.ui.ColorButton;
import com.intellij.openapi.project.Project;

import javax.swing.*;
import java.awt.*;

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

    public MyColorButtonEditor(Project project) {
        super(new JTextField());
        this.setClickCountToStart(1);
        this.project = project;
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
        this.button = new ColorButton(project, (Color) value, 18, 18,choseColor -> {});
        this.button.setBounds(new Rectangle(18, 18));

        initPanel();
        this.panel.add(this.button);

        return this.panel;
    }

    @Override
    public Object getCellEditorValue() {
        return this.button.getSelectColor();
    }
}
