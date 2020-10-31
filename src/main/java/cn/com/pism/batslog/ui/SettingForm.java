package cn.com.pism.batslog.ui;

import cn.com.pism.batslog.enums.DbType;
import cn.com.pism.batslog.settings.BatsLogSetting;
import com.intellij.openapi.project.Project;
import icons.BatsLogIcons;
import lombok.Data;

import javax.swing.*;
import java.awt.*;
import java.util.List;

/**
 * @author wangyihuai
 * @date 2020/10/28 上午 09:47
 */
@Data
public class SettingForm {
    private JPanel root;
    private JPanel radioButtonPanel;
    private JComboBox<DbType> dbTypeBox;

    public SettingForm(Project project) {
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
