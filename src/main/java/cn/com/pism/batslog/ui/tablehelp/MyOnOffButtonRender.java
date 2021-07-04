package cn.com.pism.batslog.ui.tablehelp;

import com.intellij.ui.components.OnOffButton;

import javax.swing.*;
import javax.swing.table.TableCellRenderer;
import java.awt.*;

/**
 * @author PerccyKing
 * @version 0.0.1
 * @date 2021/06/27 下午 05:22
 * @since 0.0.1
 */
public class MyOnOffButtonRender implements TableCellRenderer {

    private JPanel panel;

    private OnOffButton button;

    public MyOnOffButtonRender() {
        initButton();

        initPanel();
        this.panel.add(this.button);
    }

    private void initButton() {
        button = new OnOffButton();
        this.button.setBounds(new Rectangle(50,24));
    }

    private void initPanel() {
        this.panel = new JPanel();
        this.panel.setLayout(new FlowLayout());
    }

    @Override
    public Component getTableCellRendererComponent(JTable table, Object value, boolean isSelected, boolean hasFocus, int row, int column) {
        this.button.setSelected((Boolean) value);
        return this.panel;
    }

}
