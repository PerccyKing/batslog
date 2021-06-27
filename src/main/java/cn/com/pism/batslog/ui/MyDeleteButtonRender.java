package cn.com.pism.batslog.ui;

import cn.com.pism.batslog.BatsLogBundle;
import com.alibaba.fastjson.JSON;
import com.intellij.ui.table.JBTable;

import javax.swing.*;
import javax.swing.table.TableCellRenderer;
import javax.swing.table.TableColumn;
import javax.swing.table.TableColumnModel;
import javax.swing.table.TableModel;
import java.awt.*;

/**
 * @author PerccyKing
 * @version 0.0.1
 * @date 2021/06/27 下午 02:46
 * @since 0.0.1
 */
public class MyDeleteButtonRender implements TableCellRenderer {
    private JPanel panel;

    private JButton button;

    private JBTable jbTable;


    public MyDeleteButtonRender(JBTable jbTable) {

        this.jbTable = jbTable;
        initButton();

        initPanel();
        this.panel.add(this.button);
    }

    private void initButton() {
        this.button = new JButton(BatsLogBundle.message("delete"));
        TableColumnModel columnModel = jbTable.getColumnModel();
        TableColumn column = columnModel.getColumn(5);
        this.button.setBounds(new Rectangle(column.getWidth(), jbTable.getRowHeight()));
    }

    private void initPanel() {
        this.panel = new JPanel();
        this.panel.setLayout(null);
    }

    @Override
    public Component getTableCellRendererComponent(JTable table, Object value, boolean isSelected, boolean hasFocus, int row, int column) {
        this.button.setText(BatsLogBundle.message("delete"));
        return this.panel;
    }
}
