package cn.com.pism.batslog.ui;

import cn.com.pism.batslog.BatsLogBundle;
import cn.com.pism.batslog.action.RevertAction;
import cn.com.pism.batslog.constants.BatsLogConstant;
import cn.com.pism.batslog.enums.DbType;
import cn.com.pism.batslog.model.RgbColor;
import cn.com.pism.batslog.settings.BatsLogConfig;
import cn.com.pism.batslog.settings.BatsLogGlobalConfigState;
import cn.com.pism.batslog.settings.BatsLogSettingState;
import cn.com.pism.batslog.ui.component.EnabledColorButton;
import cn.com.pism.batslog.util.BatsLogUtil;
import cn.com.pism.batslog.util.GlobalVar;
import com.intellij.icons.AllIcons;
import com.intellij.openapi.actionSystem.ActionPlaces;
import com.intellij.openapi.actionSystem.ActionToolbar;
import com.intellij.openapi.actionSystem.Presentation;
import com.intellij.openapi.actionSystem.impl.ActionButton;
import com.intellij.openapi.project.Project;
import com.intellij.ui.components.OnOffButton;
import com.intellij.uiDesigner.core.GridConstraints;
import com.intellij.uiDesigner.core.GridLayoutManager;
import icons.BatsLogIcons;
import lombok.Data;
import org.apache.commons.lang3.StringUtils;
import org.jetbrains.annotations.Nullable;

import javax.swing.*;
import javax.swing.event.DocumentEvent;
import javax.swing.event.DocumentListener;
import javax.swing.text.BadLocationException;
import javax.swing.text.Document;
import java.awt.*;
import java.nio.charset.StandardCharsets;
import java.util.List;

import static cn.com.pism.batslog.constants.BatsLogConstant.*;

/**
 * <p>项目配置和全局配置的页面</p>
 * <pre>
 *     如果是项目的配置，必须使用构造器创建页面 {@link cn.com.pism.batslog.ui.SettingForm#SettingForm(com.intellij.openapi.project.Project)}
 * </pre>
 *
 * @author wangyihuai
 * @since 2021/1/6 11:30
 */
@Data
public class SettingForm {

    private Project project;

    private JPanel root;
    private JComboBox<DbType> dbTypeBox;
    private ColorButton keyWord;
    private JPanel keyWordsPanel;
    private JTextField sqlPrefix;
    private JTextField paramsPrefix;
    private JPanel revertSqlPanel;
    private JPanel revertParamsPanel;
    private JButton configButton;
    private OnOffButton desensitize;
    private OnOffButton prettyFormat;
    private OnOffButton parameterized;
    private OnOffButton toUpperCase;
    private OnOffButton addTimestamp;
    private JTextField timestampFormat;
    private OnOffButton startWithProject;
    private JComboBox<String> consoleEncoding;
    private OnOffButton enableMixedPrefix;
    private JLabel sqlPrefixTips;

    private BatsLogConfig service;

    private EnabledColorButton keyWordsColorButton;

    private BatsLogConfig tmpConfig = new BatsLogConfig();


    private ConsoleColorConfigDialog consoleColorConfigDialog;

    public SettingForm(Project project) {
        this.project = project;
        this.service = project == null ? BatsLogGlobalConfigState.getInstance() : BatsLogSettingState.getInstance(project);

        //初始化数据库选择
        initDbTypeBox();
        //初始化编码列表
        initEncodingBox();
        //初始化关键字颜色选择按钮
        initKeyWordColorButton(project);

        String sqlPrefixStr = service.getSqlPrefix();
        if (StringUtils.isBlank(sqlPrefixStr)) {
            sqlPrefixStr = BatsLogConstant.SQL_PREFIX;
        }
        sqlPrefix.setText(sqlPrefixStr);

        String paramsPrefixStr = service.getParamsPrefix();
        if (StringUtils.isBlank(paramsPrefixStr)) {
            paramsPrefixStr = BatsLogConstant.PARAMS_PREFIX;
        }
        paramsPrefix.setText(paramsPrefixStr);

        String timeFormat = service.getTimeFormat();
        if (StringUtils.isBlank(timeFormat)) {
            timeFormat = BatsLogConstant.TIME_FORMAT;
        }
        timestampFormat.setText(timeFormat);
        addListen();
        RevertAction revertSqlAction = new RevertAction(AllIcons.Actions.Rollback, SQL_PREFIX, sqlPrefix);
        revertSqlPanel.add(new ActionButton(revertSqlAction, new Presentation(), ActionPlaces.UNKNOWN, ActionToolbar.DEFAULT_MINIMUM_BUTTON_SIZE));
        RevertAction revertParamsAction = new RevertAction(AllIcons.Actions.Rollback, PARAMS_PREFIX, paramsPrefix);
        revertParamsPanel.add(new ActionButton(revertParamsAction, new Presentation(), ActionPlaces.UNKNOWN, ActionToolbar.DEFAULT_MINIMUM_BUTTON_SIZE));

        initFormatConfig();

        configButton.addActionListener(e -> {
            consoleColorConfigDialog = new ConsoleColorConfigDialog(project, tmpConfig);
            consoleColorConfigDialog.show();
        });
    }

    /**
     * <p>
     * 初始化编码列表
     * </p>
     *
     * @author PerccyKing
     * @since 2022/04/13 下午 10:44
     */
    private void initEncodingBox() {
        String encoding = service.getEncoding();
        consoleEncoding.addItem(DEFAULT_ENCODING);
        consoleEncoding.addItem(StandardCharsets.UTF_8.displayName());
        consoleEncoding.addItem("GBK");
        consoleEncoding.addItem(StandardCharsets.ISO_8859_1.displayName());
        consoleEncoding.addItem(StandardCharsets.US_ASCII.displayName());
        consoleEncoding.addItem(StandardCharsets.UTF_16BE.displayName());
        consoleEncoding.addItem(StandardCharsets.UTF_16LE.displayName());
        consoleEncoding.addItem(StandardCharsets.UTF_16.displayName());

        consoleEncoding.setSelectedItem(encoding);
        consoleEncoding.addItemListener(e -> whenProject(() -> tmpConfig.setEncoding((String) e.getItem()),
                () -> service.setEncoding((String) e.getItem())));
    }

    private void initFormatConfig() {
        desensitize.setSelected(service.getDesensitize());
        setOnOffText(desensitize);
        parameterized.setSelected(service.getParameterized());
        setOnOffText(parameterized);
        prettyFormat.setSelected(service.getPrettyFormat());
        tmpConfig.setPrettyFormat(service.getPrettyFormat());
        setOnOffText(prettyFormat);
        GlobalVar.setPrettyFormat(prettyFormat);
        Boolean seToUpperCase = service.getToUpperCase();
        if (seToUpperCase != null) {
            this.toUpperCase.setSelected(seToUpperCase);
        } else {
            this.toUpperCase.setSelected(false);
        }
        setOnOffText(this.toUpperCase);
        addTimestamp.setSelected(service.getAddTimestamp());
        setOnOffText(addTimestamp);
        startWithProject.setSelected(service.getStartWithProject());
        setOnOffText(startWithProject);
        enableMixedPrefix.setSelected(service.getEnableMixedPrefix());
        setOnOffText(enableMixedPrefix);
    }

    private void setOnOffText(OnOffButton offButton) {
        offButton.setOffText(BatsLogBundle.message("batslog.action.onOffButton.off"));
        offButton.setOnText(BatsLogBundle.message("batslog.action.onOffButton.on"));
    }

    /**
     * <p>
     * 初始化关键字颜色选择按钮
     * </p>
     *
     * @param project : 项目
     * @author PerccyKing
     * @since 2021/06/26 下午 03:40
     */
    private void initKeyWordColorButton(Project project) {
        EnabledColorButton colorButton = new EnabledColorButton(project, BatsLogUtil.toColor(service.getKeyWordDefCol()), 16, 16,
                BatsLogBundle.message("config.form.console.enabledKeyWordColor.tips"),
                choseColor -> {
                    RgbColor newColor = new RgbColor(choseColor.getRed(), choseColor.getGreen(), choseColor.getBlue());
                    whenProject(() -> tmpConfig.setKeyWordDefCol(newColor), () -> service.setKeyWordDefCol(newColor));
                },
                enabled -> whenProject(() -> tmpConfig.setEnabledKeyWordDefCol(enabled),
                        () -> service.setEnabledKeyWordDefCol(enabled)));
        this.keyWordsColorButton = colorButton;
        colorButton.setEnableCheckBox(service.isEnabledKeyWordDefCol());
        GridLayoutManager layout = (GridLayoutManager) keyWordsPanel.getLayout();
        GridConstraints constraintsForComponent = layout.getConstraintsForComponent(keyWord);
        layout.removeLayoutComponent(keyWord);
        layout.addLayoutComponent(colorButton.getRoot(), constraintsForComponent);
        keyWordsPanel.add(colorButton.getRoot(), constraintsForComponent);
        keyWordsPanel.revalidate();
    }

    /**
     * <p>
     * 初始化数据库选择
     * </p>
     *
     * @author PerccyKing
     * @since 2021/06/26 下午 03:39
     */
    private void initDbTypeBox() {
        List<DbType> radioButtons = DbType.getRadioButtons();
        radioButtons.forEach(rb -> {
            if (!DbType.NONE.equals(rb)) {
                JLabel jLabel = new JLabel(rb.getName());
                jLabel.setIcon(rb.getIcon());
                dbTypeBox.addItem(rb);
            }
        });
        DbType dbType = service.getDbType();
        if (dbType.equals(DbType.NONE)) {
            dbType = DbType.MYSQL;
        }
        dbTypeBox.setSelectedItem(dbType);
        dbTypeBox.addItemListener(e -> {
            DbType item = (DbType) e.getItem();
            whenProject(() -> tmpConfig.setDbType(item), () -> service.setDbType(item));
        });
        DbTypeRender dbTypeRender = new DbTypeRender();
        dbTypeBox.setRenderer(dbTypeRender);
    }

    private void addListen() {
        sqlPrefix.getDocument().addDocumentListener(new DocumentListener() {
            @Override
            public void insertUpdate(DocumentEvent e) {
                updateSqlPrefix(e);
            }

            @Override
            public void removeUpdate(DocumentEvent e) {
                updateSqlPrefix(e);
            }

            @Override
            public void changedUpdate(DocumentEvent e) {
                updateSqlPrefix(e);
            }
        });

        paramsPrefix.getDocument().addDocumentListener(new DocumentListener() {
            @Override
            public void insertUpdate(DocumentEvent e) {
                updateParamsPrefix(e);
            }

            @Override
            public void removeUpdate(DocumentEvent e) {
                updateParamsPrefix(e);
            }

            @Override
            public void changedUpdate(DocumentEvent e) {
                updateParamsPrefix(e);
            }
        });

        timestampFormat.getDocument().addDocumentListener(new DocumentListener() {
            @Override
            public void insertUpdate(DocumentEvent e) {
                updateTimeFormat(e);
            }

            @Override
            public void removeUpdate(DocumentEvent e) {
                updateTimeFormat(e);
            }

            @Override
            public void changedUpdate(DocumentEvent e) {
                updateTimeFormat(e);
            }
        });

        desensitize.addActionListener(ac -> {
            boolean isSelected = desensitize.isSelected();
            whenProject(() -> tmpConfig.setDesensitize(isSelected),
                    () -> service.setDesensitize(isSelected));
        });

        prettyFormat.addActionListener(ac -> {
                    boolean isSelected = prettyFormat.isSelected();
                    whenProject(() -> tmpConfig.setPrettyFormat(isSelected),
                            () -> service.setPrettyFormat(isSelected));
                }
        );

        parameterized.addActionListener(ac -> {
            boolean isSelected = parameterized.isSelected();
            whenProject(() -> tmpConfig.setParameterized(isSelected),
                    () -> service.setParameterized(isSelected));
        });

        toUpperCase.addActionListener(ac -> {
            boolean isSelected = toUpperCase.isSelected();
            whenProject(() -> tmpConfig.setToUpperCase(isSelected),
                    () -> service.setToUpperCase(isSelected));
        });

        addTimestamp.addActionListener(ac -> {
            boolean isSelected = addTimestamp.isSelected();
            whenProject(() -> tmpConfig.setAddTimestamp(isSelected),
                    () -> service.setAddTimestamp(isSelected));
        });

        startWithProject.addActionListener(ac -> {
            boolean isSelected = startWithProject.isSelected();
            whenProject(() -> tmpConfig.setStartWithProject(isSelected),
                    () -> service.setStartWithProject(isSelected));
        });

        enableMixedPrefix.addActionListener(as -> {
            boolean isSelected = enableMixedPrefix.isSelected();
            whenProject(() -> tmpConfig.setEnableMixedPrefix(isSelected),
                    () -> service.setEnableMixedPrefix(isSelected));
            sqlPrefixTips.setVisible(isSelected);
        });
    }

    private void updateParamsPrefix(DocumentEvent e) {
        String text = getSettingPrefix(e);
        whenProject(() -> tmpConfig.setParamsPrefix(text), () -> service.setParamsPrefix(text));
    }

    private void updateTimeFormat(DocumentEvent e) {
        String text = getSettingPrefix(e);
        whenProject(() -> tmpConfig.setTimeFormat(text), () -> service.setTimeFormat(text));
    }


    private void updateSqlPrefix(DocumentEvent e) {
        String text = getSettingPrefix(e);
        whenProject(() -> tmpConfig.setSqlPrefix(text), () -> service.setSqlPrefix(text));
    }

    @Nullable
    private String getSettingPrefix(DocumentEvent e) {
        String text = null;
        try {
            Document document = e.getDocument();
            int length = document.getLength();
            text = document.getText(0, length);
        } catch (BadLocationException badLocationException) {
            badLocationException.printStackTrace();
        }
        return text;
    }


    protected static class DbTypeRender extends JLabel implements ListCellRenderer<DbType> {


        @Override
        public Component getListCellRendererComponent(JList<? extends DbType> list, DbType value, int index,
                                                      boolean isSelected, boolean cellHasFocus) {
            if (!value.equals(DbType.NONE)) {
                this.setIcon(value.getIcon());
                if (value.getIcon() == null) {
                    this.setIcon(BatsLogIcons.BATS_LOG);
                }
                this.setText(value.getName());
            }
            return this;
        }
    }

    /**
     * 条件判断
     *
     * @param isNull  当项目为null时
     * @param notNull 当项目不为null时
     */
    private void whenProject(Runnable isNull, Runnable notNull) {
        if (this.project == null) {
            isNull.run();
        } else {
            notNull.run();
        }
    }

    public void reset() {
        BatsLogGlobalConfigState globalService = BatsLogGlobalConfigState.getInstance();
        //全局配置的时候才能进行此操作
        dbTypeBox.setSelectedItem(globalService.getDbType());
        keyWord.setSelected(globalService.isEnabledKeyWordDefCol());
        sqlPrefix.setText(globalService.getSqlPrefix());
        paramsPrefix.setText(globalService.getParamsPrefix());
        desensitize.setSelected(globalService.getDesensitize());
        prettyFormat.setSelected(globalService.getPrettyFormat());
        parameterized.setSelected(globalService.getParameterized());
        toUpperCase.setSelected(globalService.getToUpperCase());
        addTimestamp.setSelected(globalService.getAddTimestamp());
        timestampFormat.setText(globalService.getTimeFormat());
        startWithProject.setSelected(globalService.getStartWithProject());
        consoleEncoding.setSelectedItem(globalService.getEncoding());
        enableMixedPrefix.setSelected(globalService.getEnableMixedPrefix());
        keyWordsColorButton.setEnabledColor(globalService.isEnabledKeyWordDefCol());
        keyWordsColorButton.callEnabled();
        keyWordsColorButton.getColorButton().setSelectColor(BatsLogUtil.toColor(globalService.getKeyWordDefCol()));
        keyWordsColorButton.getColorButton().callCallback();
        keyWordsPanel.revalidate();
        //重置颜色配置
        consoleColorConfigDialog.rest(globalService.getColorConfigs());
    }

}
