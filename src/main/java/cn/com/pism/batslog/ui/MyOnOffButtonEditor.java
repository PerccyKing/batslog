package cn.com.pism.batslog.ui;

import com.intellij.ui.components.OnOffButton;

import javax.swing.*;
import java.awt.*;

/**
 * @author PerccyKing
 * @version 0.0.1
 * @date 2021/06/27 下午 05:19
 * @since 0.0.1
 */
public class MyOnOffButtonEditor extends DefaultCellEditor {

    private JPanel panel;

    private OnOffButton button;

    public MyOnOffButtonEditor() {
        super(new JTextField());
        this.setClickCountToStart(1);

        initButton();

        initPanel();
        this.panel.add(this.button);
    }

    private void initButton() {
        button = new OnOffButton();
        this.button.setBounds(new Rectangle(50, 24));
        fireEditingCanceled();
    }

    private void initPanel() {
        this.panel = new JPanel();
        this.panel.setLayout(new FlowLayout());
    }

    @Override
    public Component getTableCellEditorComponent(JTable table, Object value, boolean isSelected, int row, int column) {
        return this.panel;
    }


    @Override
    public Object getCellEditorValue() {
        return button.isSelected();
    }

}
