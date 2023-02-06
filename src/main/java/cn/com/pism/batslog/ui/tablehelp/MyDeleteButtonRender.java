package cn.com.pism.batslog.ui.tablehelp;

import com.intellij.icons.AllIcons;
import com.intellij.ui.table.JBTable;

import javax.swing.*;
import javax.swing.table.TableCellRenderer;
import javax.swing.table.TableColumn;
import javax.swing.table.TableColumnModel;
import java.awt.*;

/**
 * @author PerccyKing
 * @version 0.0.1
 * @since 2021/06/27 下午 02:46
 */
public class MyDeleteButtonRender implements TableCellRenderer {
    private JPanel panel;

    private JButton button;

    private final JBTable jbTable;


    public MyDeleteButtonRender(JBTable jbTable) {

        this.jbTable = jbTable;
        initButton();

        initPanel();
        this.panel.add(this.button);
    }

    private void initButton() {
        this.button = new JButton();
        this.button.setIcon(AllIcons.Actions.GC);
        this.button.setBackground(null);
        this.button.setBorder(null);
        TableColumnModel columnModel = jbTable.getColumnModel();
        TableColumn column = columnModel.getColumn(5);
        this.button.setBounds(new Rectangle(column.getWidth(), jbTable.getRowHeight()));
    }

    private void initPanel() {
        this.panel = new JPanel();
        this.panel.setLayout(new FlowLayout());
    }

    @Override
    public Component getTableCellRendererComponent(JTable table, Object value, boolean isSelected, boolean hasFocus, int row, int column) {
        return this.panel;
    }
}
