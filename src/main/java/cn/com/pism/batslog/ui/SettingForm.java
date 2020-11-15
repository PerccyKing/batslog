package cn.com.pism.batslog.ui;

import cn.com.pism.batslog.enums.DbType;
import cn.com.pism.batslog.settings.BatsLogSetting;
import cn.com.pism.batslog.settings.BatsLogValue;
import com.intellij.openapi.project.Project;
import com.intellij.ui.ColorChooser;
import com.intellij.ui.JBColor;
import com.intellij.uiDesigner.core.GridConstraints;
import com.intellij.uiDesigner.core.GridLayoutManager;
import com.intellij.util.ui.JBUI;
import icons.BatsLogIcons;
import lombok.Data;

import javax.swing.*;
import java.awt.*;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import java.util.List;

import static cn.com.pism.batslog.constants.BatsLogConstant.KEY_WORD_DEF_COL;

/**
 * @author wangyihuai
 * @date 2020/10/28 上午 09:47
 */
@Data
public class SettingForm {

    private Project project;

    private JPanel root;
    private JPanel radioButtonPanel;
    private JComboBox<DbType> dbTypeBox;
    private ColorButton keyWord;
    private JPanel keyWordsPanel;

    public SettingForm(Project project) {
        this.project = project;

        List<DbType> radioButtons = DbType.getRadioButtons();
        radioButtons.forEach(rb -> {
            if (!DbType.NONE.equals(rb)) {
                JLabel jLabel = new JLabel(rb.getName());
                jLabel.setIcon(rb.getIcon());
                dbTypeBox.addItem(rb);
            }
        });
        DbType dbType = BatsLogSetting.getDbType(project);
        if (dbType.equals(DbType.NONE)) {
            dbType = DbType.MYSQL;
        }
        dbTypeBox.setSelectedItem(dbType);
        dbTypeBox.addItemListener(e -> {
            DbType item = (DbType) e.getItem();
            BatsLogSetting.setDbType(project, item);
        });
        DbTypeRender<DbType> dbTypeRender = new DbTypeRender<>();
        dbTypeBox.setRenderer(dbTypeRender);
        ColorButton colorButton = new ColorButton(project, BatsLogSetting.getValue(project, BatsLogSetting.KEYWORDS, Color.class).getValue());
        GridLayoutManager layout = (GridLayoutManager) keyWordsPanel.getLayout();
        GridConstraints constraintsForComponent = layout.getConstraintsForComponent(keyWord);
        layout.removeLayoutComponent(keyWord);
        layout.addLayoutComponent(colorButton, constraintsForComponent);
        keyWordsPanel.add(colorButton, constraintsForComponent);
        keyWordsPanel.revalidate();
    }


    private static class ColorButton extends JButton {

        private Color myColor;

        @Override
        protected void init(String text, Icon icon) {
            super.init(text, icon);
        }

        ColorButton(Project project, Color color) {
            if (color != null) {
                this.myColor = color;
            } else {
                this.myColor = KEY_WORD_DEF_COL;
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
                        BatsLogSetting.setValue(project, new BatsLogValue<>(BatsLogSetting.KEYWORDS, color));
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
