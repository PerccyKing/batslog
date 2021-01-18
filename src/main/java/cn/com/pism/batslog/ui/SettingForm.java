package cn.com.pism.batslog.ui;

import cn.com.pism.batslog.action.RevertAction;
import cn.com.pism.batslog.constants.BatsLogConstant;
import cn.com.pism.batslog.enums.DbType;
import cn.com.pism.batslog.settings.BatsLogSettingState;
import com.intellij.icons.AllIcons;
import com.intellij.openapi.actionSystem.ActionPlaces;
import com.intellij.openapi.actionSystem.ActionToolbar;
import com.intellij.openapi.actionSystem.Presentation;
import com.intellij.openapi.actionSystem.impl.ActionButton;
import com.intellij.openapi.components.ServiceManager;
import com.intellij.openapi.project.Project;
import com.intellij.ui.ColorChooser;
import com.intellij.uiDesigner.core.GridConstraints;
import com.intellij.uiDesigner.core.GridLayoutManager;
import com.intellij.util.ui.JBUI;
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
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import java.util.List;

import static cn.com.pism.batslog.constants.BatsLogConstant.*;

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
    private JTextField sqlTerminator;
    private JTextField paramsTerminator;

    private BatsLogSettingState service;


    public SettingForm(Project project) {
        this.project = project;
        this.service = ServiceManager.getService(project, BatsLogSettingState.class);

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
        ColorButton colorButton = new ColorButton(project, service.getKeyWordDefCol());
        GridLayoutManager layout = (GridLayoutManager) keyWordsPanel.getLayout();
        GridConstraints constraintsForComponent = layout.getConstraintsForComponent(keyWord);
        layout.removeLayoutComponent(keyWord);
        layout.addLayoutComponent(colorButton, constraintsForComponent);
        keyWordsPanel.add(colorButton, constraintsForComponent);
        keyWordsPanel.revalidate();

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

        sqlTerminator.setText(service.getSqlTerminator());
        paramsTerminator.setText(service.getParamsTerminator());
        inputListen(project);
        RevertAction revertSqlAction = new RevertAction(AllIcons.Actions.Rollback, SQL_PREFIX, sqlPrefix);
        revertSqlPanel.add(new ActionButton(revertSqlAction, new Presentation(), ActionPlaces.UNKNOWN, ActionToolbar.DEFAULT_MINIMUM_BUTTON_SIZE));
        RevertAction revertParamsAction = new RevertAction(AllIcons.Actions.Rollback, PARAMS_PREFIX, paramsPrefix);
        revertParamsPanel.add(new ActionButton(revertParamsAction, new Presentation(), ActionPlaces.UNKNOWN, ActionToolbar.DEFAULT_MINIMUM_BUTTON_SIZE));
    }

    private void inputListen(Project project) {
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
    }

    private void updateParamsPrefix(DocumentEvent e, Project project) {
        String text = getSettingPrefix(e);
        service.setParamsPrefix(text);
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


    public static class ColorButton extends JButton {

        private Color myColor;

        private BatsLogSettingState service;

        @Override
        protected void init(String text, Icon icon) {
            super.init(text, icon);
        }

        ColorButton(Project project, Color color) {
            this.service = ServiceManager.getService(project, BatsLogSettingState.class);
            if (color != null) {
                this.myColor = color;
            } else {
                this.myColor = KEY_WORD_DEF_COL;
                service.setKeyWordDefCol(KEY_WORD_DEF_COL);
            }
            buttonInit(project, color);
        }

        ColorButton() {
            buttonInit(null, null);
        }

        private void buttonInit(Project project, Color color) {
            setMargin(JBUI.emptyInsets());
            setFocusable(false);
            setDefaultCapable(false);
            setFocusable(false);
            if (color != null) {
                myColor = color;
            }
            MouseAdapter adapter = new MouseAdapter() {
                /**
                 * {@inheritDoc}
                 *
                 * @param e
                 */
                @Override
                public void mouseClicked(MouseEvent e) {
                    Color color = ColorChooser.chooseColor(new JPanel(), "选择颜色", myColor);
                    if (color != null) {
                        myColor = color;
                        service.setKeyWordDefCol(color);
                    }
                    super.mouseClicked(e);
                }
            };
            addMouseListener(adapter);
        }


        @Override
        public void paint(Graphics g) {
            final Color color = g.getColor();
            g.setColor(myColor);
            g.fillRect(0, 0, 12, 12);
            g.setColor(color);
        }

        @Override
        public Dimension getMinimumSize() {
            return getPreferredSize();
        }

        @Override
        public Dimension getMaximumSize() {
            return getPreferredSize();
        }

        @Override
        public Dimension getPreferredSize() {
            return new Dimension(12, 12);
        }


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
