package cn.com.pism.batslog.ui;

import cn.com.pism.batslog.enums.DbType;
import com.intellij.openapi.project.Project;
import com.intellij.openapi.ui.ComboBox;
import lombok.Data;

import javax.swing.*;
import java.util.List;

/**
 * @author wangyihuai
 * @date 2020/10/28 上午 09:47
 */
@Data
public class SettingForm {
    private JPanel root;
    private JPanel radioButtonPanel;

    public SettingForm(Project project) {
        List<DbType> radioButtons = DbType.getRadioButtons();
        radioButtons.forEach(rb -> {
            JLabel jLabel = new JLabel(rb.getName());
            jLabel.setIcon(rb.getIcon());
        });
    }


}
