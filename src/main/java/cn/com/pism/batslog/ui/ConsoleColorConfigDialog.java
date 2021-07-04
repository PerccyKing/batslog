package cn.com.pism.batslog.ui;

import cn.com.pism.batslog.BatsLogBundle;
import cn.com.pism.batslog.model.ConsoleColorConfig;
import cn.com.pism.batslog.settings.BatsLogSettingState;
import cn.com.pism.batslog.settings.RgbColor;
import cn.com.pism.batslog.ui.tablehelp.*;
import cn.com.pism.batslog.util.BatsLogUtil;
import cn.com.pism.batslog.util.ConsoleColorConfigUtil;
import com.intellij.openapi.components.ServiceManager;
import com.intellij.openapi.project.Project;
import com.intellij.openapi.ui.DialogWrapper;
import com.intellij.ui.JBColor;
import com.intellij.ui.table.JBTable;
import org.apache.commons.lang3.StringUtils;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

import javax.swing.*;
import javax.swing.table.DefaultTableModel;
import javax.swing.table.TableColumn;
import javax.swing.table.TableColumnModel;
import java.util.*;

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


    private BatsLogSettingState service;

    protected ConsoleColorConfigDialog(@Nullable Project project) {
        super(project);
        this.service = ServiceManager.getService(project, BatsLogSettingState.class);
        this.project = project;
        init();
        initForm();
        addButton.addActionListener(e -> {
            DefaultTableModel tableModel = (DefaultTableModel) colorSettingTable.getModel();
            tableModel.addRow(addRow());
        });
        show();
    }

    private Object[] addRow() {
        List<ConsoleColorConfig> colorConfigs = service.getColorConfigs();
        int sort = 1;
        Optional<ConsoleColorConfig> max = colorConfigs.stream().max(Comparator.comparingInt(ConsoleColorConfig::getSort));
        if (max.isPresent()) {
            sort = max.get().getSort();
        }
        List<ConsoleColorConfig> notSaveConfigs = getColorConfigs();
        Optional<ConsoleColorConfig> max1 = notSaveConfigs.stream().max(Comparator.comparingInt(ConsoleColorConfig::getSort));
        if (max1.isPresent()) {
            sort = Math.max(sort, max1.get().getSort());
        }
        sort++;
        return new ConsoleColorConfig(UUID.randomUUID().toString(),
                sort,
                BatsLogBundle.message("modifyTheKeyword"),
                new RgbColor(JBColor.BLUE.getRed(), JBColor.BLUE.getGreen(), JBColor.BLUE.getBlue()),
                new RgbColor(JBColor.YELLOW.getRed(), JBColor.YELLOW.getGreen(), JBColor.YELLOW.getBlue()),
                true).toArray();
    }

    private void initForm() {
        setTitle(BatsLogBundle.message("consoleColorConfig"));
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
                BatsLogBundle.message("enable"),
                BatsLogBundle.message("operation")
        };
        int[] columnWidth = {0, 50, 200, 50, 50, 100, 100};

        DefaultTableModel tableModel = new DefaultTableModel(null, columns) {
            @Override
            public boolean isCellEditable(int row, int column) {
                return column != 1;
            }
        };

        List<ConsoleColorConfig> colorConfigs = service.getColorConfigs();
        colorConfigs.forEach(config -> tableModel.addRow(config.toArray()));

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

        TableColumnModel headerModel = colorSettingTable.getTableHeader().getColumnModel();

        int columnCount = headerModel.getColumnCount();
        for (int i = 0; i < columnCount; i++) {
            headerModel.getColumn(i).setMaxWidth(columnWidth[i]);
            headerModel.getColumn(i).setMinWidth(columnWidth[i]);
        }

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
        List<ConsoleColorConfig> configData = getColorConfigs();
        for (int i = 0; i < configData.size(); i++) {
            ConsoleColorConfig colorConfig = configData.get(i);
            colorConfig.setSort(i + 1);
            if (StringUtils.isNotBlank(colorConfig.getKeyWord())) {
                colorConfig.setKeyWord(colorConfig.getKeyWord().toUpperCase(Locale.ROOT));
            }
        }
        service.setColorConfigs(configData);

        List<ConsoleColorConfig> colorConfigs = service.getColorConfigs();
        BatsLogUtil.KEY_COLOR_MAP = ConsoleColorConfigUtil.toConsoleViewContentTypeMap(project, colorConfigs);
        super.doOKAction();
    }

    @NotNull
    private List<ConsoleColorConfig> getColorConfigs() {
        int columnCount = colorSettingTable.getColumnCount();
        DefaultTableModel tableModel = (DefaultTableModel) colorSettingTable.getModel();
        int rowCount = tableModel.getRowCount();
        List<ConsoleColorConfig> configData = new ArrayList<>();
        for (int i = 0; i < rowCount; i++) {
            Object[] row = new Object[columnCount];
            for (int j = 0; j < columnCount; j++) {
                row[j] = tableModel.getValueAt(i, j);
            }
            configData.add(new ConsoleColorConfig().toConfig(row));
        }
        return configData;
    }


    private List<ConsoleColorConfig> mock(int size) {
        List<ConsoleColorConfig> configs = new ArrayList<>();
        for (int i = 0; i < size; i++) {
            configs.add(new ConsoleColorConfig(String.valueOf(i), i, "INSERT", new RgbColor(150, 150, 150),
                    new RgbColor(130, 130, 130), i % 2 > 0));
        }
        return configs;
    }
}
