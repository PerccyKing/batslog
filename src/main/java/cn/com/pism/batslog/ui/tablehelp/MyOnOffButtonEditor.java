package cn.com.pism.batslog.ui.tablehelp;

import com.intellij.ui.components.OnOffButton;

import javax.swing.*;
import java.awt.*;
import java.util.function.Consumer;

/**
 * @author PerccyKing
 * @version 0.0.1
 * @since 2021/06/27 下午 05:19
 */
public class MyOnOffButtonEditor extends DefaultCellEditor {

    private JPanel panel;

    private OnOffButton button;

    private final transient Consumer<Boolean> consumer;

    public MyOnOffButtonEditor(Consumer<Boolean> consumer) {
        super(new JTextField());
        this.setClickCountToStart(1);

        this.consumer = consumer;
    }


    private void initPanel() {
        this.panel = new JPanel();
        this.panel.setLayout(new FlowLayout());
    }

    @Override
    public Component getTableCellEditorComponent(JTable table, Object value, boolean isSelected, int row, int column) {
        this.button = new OnOffButton();
        this.button.setBounds(new Rectangle(50, 24));
        fireEditingCanceled();
        this.button.addChangeListener(e -> {
            table.getModel().setValueAt(button.isSelected(), row, column);
            consumer.accept(button.isSelected());
        });

        initPanel();
        this.panel.add(this.button);

        return this.panel;
    }


    @Override
    public Object getCellEditorValue() {
        return button.isSelected();
    }

}
