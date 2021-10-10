package cn.com.pism.batslog.ui;

import cn.com.pism.batslog.BatsLogBundle;
import cn.com.pism.batslog.model.ConsoleColorConfig;
import cn.com.pism.batslog.model.RgbColor;
import cn.com.pism.batslog.model.ShowColorConfig;
import cn.com.pism.batslog.settings.BatsLogSettingState;
import cn.com.pism.batslog.ui.tablehelp.*;
import cn.com.pism.batslog.util.BatsLogUtil;
import cn.com.pism.batslog.util.ColoringUtil;
import cn.com.pism.batslog.util.ConsoleColorConfigUtil;
import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONValidator;
import com.intellij.json.JsonFileType;
import com.intellij.json.JsonLanguage;
import com.intellij.openapi.application.ApplicationManager;
import com.intellij.openapi.components.ServiceManager;
import com.intellij.openapi.editor.Document;
import com.intellij.openapi.editor.event.DocumentEvent;
import com.intellij.openapi.editor.event.DocumentListener;
import com.intellij.openapi.project.Project;
import com.intellij.openapi.ui.DialogWrapper;
import com.intellij.psi.PsiDocumentManager;
import com.intellij.psi.PsiFile;
import com.intellij.psi.PsiFileFactory;
import com.intellij.ui.EditorTextField;
import com.intellij.ui.HyperlinkLabel;
import com.intellij.ui.JBColor;
import com.intellij.ui.table.JBTable;
import org.apache.commons.collections.CollectionUtils;
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
    private JButton clearButton;
    private JPanel configEditor;
    private HyperlinkLabel oac;


    private Project project;

    private EditorTextField textField;


    private BatsLogSettingState service;

    protected ConsoleColorConfigDialog(@Nullable Project project) {
        super(project);
        this.service = BatsLogSettingState.getInstance(project);
        this.project = project;
        init();
        initForm(project);
        addListener();
        show();
    }

    private void addListener() {
        addButton.addActionListener(e -> {
            DefaultTableModel tableModel = (DefaultTableModel) colorSettingTable.getModel();
            tableModel.addRow(addRow());
            reloadConfig();
        });
        clearButton.addActionListener(e -> {
            deleteAllData(getTableModel());
            reloadConfig();
        });
        colorSettingTable.putClientProperty("terminateEditOnFocusLost", Boolean.TRUE);
        getTableModel().addTableModelListener(e -> {
            if (2 == e.getColumn()) {
                getTableModel().fireTableDataChanged();
                reloadConfig();
            }
        });
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

    private void initForm(Project project) {
        setTitle(BatsLogBundle.message("config.form.label.consoleColorConfig"));
        oac.setHyperlinkTarget("https://perccyking.github.io/batslog/");
        oac.setHyperlinkText(BatsLogBundle.message("config.form.console.colorConfigForm.OAC"));
        initColorSettingTable();
        initConfigPanel(project);
        setAutoAdjustable(true);
    }

    private void initConfigPanel(Project project) {

        List<ConsoleColorConfig> colorConfigs = service.getColorConfigs();
        List<ShowColorConfig> showConfig = ColoringUtil.toShowConfig(colorConfigs);
        PsiFile psiFile = PsiFileFactory.getInstance(project).createFileFromText(JsonLanguage.INSTANCE, "[]");

        Document document = PsiDocumentManager.getInstance(project).getDocument(psiFile);

        EditorTextField textField = new EditorTextField(document, project, JsonFileType.INSTANCE, false, false);
        textField.setOneLineMode(false);
        textField.setEnabled(true);
        textField.addDocumentListener(new DocumentListener() {
            @Override
            public void documentChanged(@NotNull DocumentEvent event) {
                //判断是否需要重新加载config
                String text = event.getDocument().getText();
                reloadTable(text);
                DocumentListener.super.documentChanged(event);
            }
        });
        textField.setText(CollectionUtils.isNotEmpty(showConfig) ? JSON.toJSONString(showConfig, true) : "[]");

        this.textField = textField;
        configEditor.add(textField);
    }


    private void initColorSettingTable() {

        String[] columns = new String[]{
                "id",
                BatsLogBundle.message("config.form.console.colorConfigForm.table.label.serialNumber"),
                BatsLogBundle.message("config.form.console.colorConfigForm.table.label.keyword"),
                BatsLogBundle.message("config.form.console.colorConfigForm.table.label.bgColor"),
                BatsLogBundle.message("config.form.console.colorConfigForm.table.label.fgColor"),
                BatsLogBundle.message("config.form.console.colorConfigForm.table.label.enable"),
                BatsLogBundle.message("config.form.console.colorConfigForm.table.label.operation")
        };
        int[] columnWidth = {0, 50, 200, 100, 100, 100, 100};
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
        column4.setCellEditor(new MyColorButtonEditor(project, color -> {
            getTableModel().fireTableDataChanged();
            reloadConfig();
        }));
        column4.setCellRenderer(new MyColorButtonRender(project));
        column3.setCellEditor(new MyColorButtonEditor(project, color -> {
            getTableModel().fireTableDataChanged();
            reloadConfig();
        }));
        column3.setCellRenderer(new MyColorButtonRender(project));

        columnModel.getColumn(5).setCellEditor(new MyOnOffButtonEditor(c -> reloadConfig()));
        columnModel.getColumn(5).setCellRenderer(new MyOnOffButtonRender());
        columnModel.getColumn(6).setCellEditor(new MyDeleteButtonEditor(colorSettingTable, cel -> reloadConfig()));
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

    private void reloadTable(String text) {
        if (StringUtils.isBlank(text)) {
            ApplicationManager.getApplication().invokeLater(() -> textField.setText("[]"));
        }
        boolean validate = false;
        try {
            validate = StringUtils.isNotBlank(text) && JSONValidator.from(text).validate();
        } catch (Exception e) {
            e.printStackTrace();
        }
        if (validate) {
            List<ShowColorConfig> configs = JSON.parseArray(text, ShowColorConfig.class);
            try {
                List<ConsoleColorConfig> colorConfigs = ColoringUtil.toColorConfig(configs);
                reloadTable(colorConfigs);
            } catch (Exception e) {
                e.printStackTrace();
            }
        }
    }

    private void reloadTable(List<ConsoleColorConfig> colorConfigs) {
        DefaultTableModel tableModel = (DefaultTableModel) colorSettingTable.getModel();
        deleteAllData(tableModel);
        colorConfigs.forEach(c -> tableModel.addRow(c.toArray()));
    }

    private void deleteAllData(DefaultTableModel tableModel) {
        int rowCount = tableModel.getRowCount();
        for (int i = rowCount - 1; i >= 0; i--) {
            tableModel.removeRow(i);
        }
    }

    private DefaultTableModel getTableModel() {
        return (DefaultTableModel) colorSettingTable.getModel();
    }

    private void reloadConfig() {
        List<ConsoleColorConfig> colorConfigs = getColorConfigs();
        List<ShowColorConfig> showConfig = ColoringUtil.toShowConfig(colorConfigs);
        if (this.textField != null) {
            ApplicationManager.getApplication().invokeLater(() ->
                    this.textField.setText(CollectionUtils.isNotEmpty(showConfig) ? JSON.toJSONString(showConfig, true) : "[]"));
        }
    }
}
