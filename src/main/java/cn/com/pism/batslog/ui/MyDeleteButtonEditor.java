package cn.com.pism.batslog.ui;

import com.alibaba.fastjson.JSON;
import com.intellij.icons.AllIcons;
import com.intellij.ui.table.JBTable;

import javax.swing.*;
import javax.swing.table.DefaultTableModel;
import javax.swing.table.TableColumn;
import javax.swing.table.TableColumnModel;
import java.awt.*;

/**
 * @author PerccyKing
 * @version 0.0.1
 * @date 2021/06/27 下午 02:34
 * @since 0.0.1
 */
public class MyDeleteButtonEditor extends DefaultCellEditor {

    private JPanel panel;

    private JButton button;

    private JBTable jbTable;

    public MyDeleteButtonEditor() {
        super(new JTextField());
    }

    public MyDeleteButtonEditor(JBTable jbTable) {
        super(new JTextField());

        this.jbTable = jbTable;
        this.setClickCountToStart(1);

        initButton();

        initPanel();
        this.panel.add(this.button);
    }

    private void initButton() {
        this.button = new JButton();
        this.button.setIcon(AllIcons.Actions.GC);
        TableColumnModel columnModel = jbTable.getColumnModel();
        TableColumn column = columnModel.getColumn(5);
        this.button.setBounds(new Rectangle(column.getWidth(), jbTable.getRowHeight()));

        this.button.addActionListener(e -> {
            MyDeleteButtonEditor.this.fireEditingCanceled();
            int selectedRow = jbTable.getSelectedRow();
            DefaultTableModel model = (DefaultTableModel) jbTable.getModel();
            int columnCount = model.getColumnCount();
            for (int i = 0; i < columnCount; i++) {
                Object obj = model.getValueAt(selectedRow, i);
                System.out.print(JSON.toJSONString(obj) + "\t");
                System.out.println("");
            }
            model.removeRow(selectedRow);
        });
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
        return this.button.getText();
    }
}
