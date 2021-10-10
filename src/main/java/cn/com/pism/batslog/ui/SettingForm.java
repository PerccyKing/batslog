package cn.com.pism.batslog.ui;

import cn.com.pism.batslog.BatsLogBundle;
import cn.com.pism.batslog.action.RevertAction;
import cn.com.pism.batslog.constants.BatsLogConstant;
import cn.com.pism.batslog.enums.DbType;
import cn.com.pism.batslog.model.RgbColor;
import cn.com.pism.batslog.settings.BatsLogSettingState;
import cn.com.pism.batslog.util.BatsLogUtil;
import com.intellij.icons.AllIcons;
import com.intellij.openapi.actionSystem.ActionPlaces;
import com.intellij.openapi.actionSystem.ActionToolbar;
import com.intellij.openapi.actionSystem.Presentation;
import com.intellij.openapi.actionSystem.impl.ActionButton;
import com.intellij.openapi.components.ServiceManager;
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
import java.util.List;

import static cn.com.pism.batslog.constants.BatsLogConstant.PARAMS_PREFIX;
import static cn.com.pism.batslog.constants.BatsLogConstant.SQL_PREFIX;

/**
 * @author wangyihuai
 * @date 2021/1/6 11:30
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

    private BatsLogSettingState service;

    public SettingForm(){}


    public SettingForm(Project project) {
        this.project = project;
        this.service = BatsLogSettingState.getInstance(project);

        //初始化数据库选择
        initDbTypeBox();
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
        addListen(project);
        RevertAction revertSqlAction = new RevertAction(AllIcons.Actions.Rollback, SQL_PREFIX, sqlPrefix);
        revertSqlPanel.add(new ActionButton(revertSqlAction, new Presentation(), ActionPlaces.UNKNOWN, ActionToolbar.DEFAULT_MINIMUM_BUTTON_SIZE));
        RevertAction revertParamsAction = new RevertAction(AllIcons.Actions.Rollback, PARAMS_PREFIX, paramsPrefix);
        revertParamsPanel.add(new ActionButton(revertParamsAction, new Presentation(), ActionPlaces.UNKNOWN, ActionToolbar.DEFAULT_MINIMUM_BUTTON_SIZE));

        initFormatConfig();

        configButton.addActionListener(e -> ConsoleColorConfigDialog.show(project));
    }

    private void initFormatConfig() {
        desensitize.setSelected(service.getDesensitize());
        setOnOffText(desensitize);
        parameterized.setSelected(service.getParameterized());
        setOnOffText(parameterized);
        prettyFormat.setSelected(service.getPrettyFormat());
        setOnOffText(prettyFormat);
        BatsLogUtil.PRETTY_FORMAT = prettyFormat;
        Boolean toUpperCase = service.getToUpperCase();
        if (toUpperCase != null) {
            this.toUpperCase.setSelected(toUpperCase);
        } else {
            this.toUpperCase.setSelected(false);
        }
        setOnOffText(this.toUpperCase);
        addTimestamp.setSelected(service.getAddTimestamp());
        setOnOffText(addTimestamp);
        startWithProject.setSelected(service.getStartWithProject());
        setOnOffText(startWithProject);
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
     * @date 2021/06/26 下午 03:40
     */
    private void initKeyWordColorButton(Project project) {
        ColorButton colorButton = new ColorButton(project, BatsLogUtil.toColor(service.getKeyWordDefCol()), 16, 16, choseColor -> {
            service.setKeyWordDefCol(new RgbColor(choseColor.getRed(), choseColor.getGreen(), choseColor.getBlue()));
        });
        GridLayoutManager layout = (GridLayoutManager) keyWordsPanel.getLayout();
        GridConstraints constraintsForComponent = layout.getConstraintsForComponent(keyWord);
        layout.removeLayoutComponent(keyWord);
        layout.addLayoutComponent(colorButton, constraintsForComponent);
        keyWordsPanel.add(colorButton, constraintsForComponent);
        keyWordsPanel.revalidate();
    }

    /**
     * <p>
     * 初始化数据库选择
     * </p>
     *
     * @author PerccyKing
     * @date 2021/06/26 下午 03:39
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
            service.setDbType(item);
        });
        DbTypeRender<DbType> dbTypeRender = new DbTypeRender<>();
        dbTypeBox.setRenderer(dbTypeRender);
    }

    private void addListen(Project project) {
        sqlPrefix.getDocument().addDocumentListener(new DocumentListener() {
            @Override
            public void insertUpdate(DocumentEvent e) {
                updateSqlPrefix(e, project);
            }

            @Override
            public void removeUpdate(DocumentEvent e) {
                updateSqlPrefix(e, project);
            }

            @Override
            public void changedUpdate(DocumentEvent e) {
                updateSqlPrefix(e, project);
            }
        });

        paramsPrefix.getDocument().addDocumentListener(new DocumentListener() {
            @Override
            public void insertUpdate(DocumentEvent e) {
                updateParamsPrefix(e, project);
            }

            @Override
            public void removeUpdate(DocumentEvent e) {
                updateParamsPrefix(e, project);
            }

            @Override
            public void changedUpdate(DocumentEvent e) {
                updateParamsPrefix(e, project);
            }
        });
        timestampFormat.getDocument().addDocumentListener(new DocumentListener() {
            @Override
            public void insertUpdate(DocumentEvent e) {
                updateTimeFormat(e, project);
            }

            @Override
            public void removeUpdate(DocumentEvent e) {
                updateTimeFormat(e, project);
            }

            @Override
            public void changedUpdate(DocumentEvent e) {
                updateTimeFormat(e, project);
            }
        });
        desensitize.addActionListener(ac -> service.setDesensitize(desensitize.isSelected()));
        prettyFormat.addActionListener(ac -> service.setPrettyFormat(prettyFormat.isSelected()));
        parameterized.addActionListener(ac -> service.setParameterized(parameterized.isSelected()));
        toUpperCase.addActionListener(ac -> service.setToUpperCase(toUpperCase.isSelected()));
        addTimestamp.addActionListener(ac -> service.setAddTimestamp(addTimestamp.isSelected()));
        startWithProject.addActionListener(ac -> service.setStartWithProject(startWithProject.isSelected()));
    }

    private void updateParamsPrefix(DocumentEvent e, Project project) {
        String text = getSettingPrefix(e);
        service.setParamsPrefix(text);
    }

    private void updateTimeFormat(DocumentEvent e, Project project) {
        service.setTimeFormat(getSettingPrefix(e));
    }


    private void updateSqlPrefix(DocumentEvent e, Project project) {
        String text = getSettingPrefix(e);
        service.setSqlPrefix(text);
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


    protected static class DbTypeRender<T extends DbType> extends JLabel implements ListCellRenderer<T> {


        @Override
        public Component getListCellRendererComponent(JList<? extends T> list, T value, int index, boolean isSelected, boolean cellHasFocus) {
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

}
