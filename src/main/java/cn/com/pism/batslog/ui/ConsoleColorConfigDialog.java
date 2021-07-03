package cn.com.pism.batslog.ui;

import cn.com.pism.batslog.BatsLogBundle;
import cn.com.pism.batslog.model.ConsoleColorConfig;
import com.intellij.openapi.project.Project;
import com.intellij.openapi.ui.DialogWrapper;
import com.intellij.ui.Gray;
import com.intellij.ui.table.JBTable;
import org.jetbrains.annotations.Nullable;

import javax.swing.*;
import javax.swing.table.DefaultTableModel;
import javax.swing.table.TableColumn;
import javax.swing.table.TableColumnModel;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.util.ArrayList;
import java.util.List;

/**
 * @author PerccyKing
 * @version 0.0.1
 * @date 2021/06/27 下午 04:27
 * @since 0.0.1
 */
public class ConsoleColorConfigDialog extends DialogWrapper {
    private JPanel root;
    private JButton addButton;
    private JBTable colorSettingTable;

    private Project project;

    protected ConsoleColorConfigDialog(@Nullable Project project) {
        super(project);
        this.project = project;
        init();
        initForm();
        show();
        addButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
            }
        });
    }

    private void initForm() {
        setTitle(BatsLogBundle.message("consoleColorConfig"));
        setSize(500, 800);
        initColorSettingTable();
        setAutoAdjustable(true);
    }


    private void initColorSettingTable() {

        String[] columns = new String[]{
                "id",
                BatsLogBundle.message("serialNumber"),
                BatsLogBundle.message("keyword"),
                BatsLogBundle.message("backgroundColor"),
                BatsLogBundle.message("foregroundColor"),
                "是否启用",
                BatsLogBundle.message("operation")
        };
        int[] columnWidth = {50, 200, 50, 50, 100};

        DefaultTableModel tableModel = new DefaultTableModel(null, columns) {
            @Override
            public boolean isCellEditable(int row, int column) {
                return column != 1;
            }
        };

        List<ConsoleColorConfig> mock = mock(50);
        mock.forEach(m -> tableModel.addRow(m.toArray()));

        colorSettingTable.setModel(tableModel);
        TableColumnModel columnModel = colorSettingTable.getColumnModel();
        columnModel.getColumn(0).setMaxWidth(0);
        columnModel.getColumn(0).setMinWidth(0);
        TableColumn column4 = columnModel.getColumn(4);
        TableColumn column3 = columnModel.getColumn(3);
        column4.setCellEditor(new MyColorButtonEditor(project));
        column4.setCellRenderer(new MyColorButtonRender(project));
        column3.setCellEditor(new MyColorButtonEditor(project));
        column3.setCellRenderer(new MyColorButtonRender(project));
        columnModel.getColumn(5).setCellEditor(new MyOnOffButtonEditor());
        columnModel.getColumn(5).setCellRenderer(new MyOnOffButtonRender());
        columnModel.getColumn(6).setCellEditor(new MyDeleteButtonEditor(colorSettingTable));
        columnModel.getColumn(6).setCellRenderer(new MyDeleteButtonRender(colorSettingTable));
        colorSettingTable.getTableHeader().getColumnModel().getColumn(0).setMaxWidth(0);
        colorSettingTable.getTableHeader().getColumnModel().getColumn(0).setMinWidth(0);
        colorSettingTable.doLayout();
        colorSettingTable.setShowColumns(true);

    }

    @Nullable
    @Override
    protected JComponent createCenterPanel() {
        return root;
    }

    public static void show(Project project) {
        new ConsoleColorConfigDialog(project);
    }

    @Override
    protected void doOKAction() {
        super.doOKAction();
    }


    private List<ConsoleColorConfig> mock(int size) {
        List<ConsoleColorConfig> configs = new ArrayList<>();
        for (int i = 0; i < size; i++) {
            configs.add(new ConsoleColorConfig(String.valueOf(i), i, "INSERT", Gray._15, Gray._40, i % 2 > 0));
        }
        return configs;
    }
}
