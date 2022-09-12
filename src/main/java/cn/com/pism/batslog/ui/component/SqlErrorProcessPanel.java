package cn.com.pism.batslog.ui.component;

import cn.com.pism.batslog.model.BslErrorMod;
import com.intellij.openapi.project.Project;
import lombok.Data;

import javax.swing.*;

/**
 * @author PerccyKing
 * @version 0.0.1
 * @date 2021/11/24 下午 08:59
 * @since 0.0.1
 */
@Data
public class SqlErrorProcessPanel {
    private JPanel root;
    private JPanel first;
    private JPanel second;

    private SqlAndParamsPanel sqlAndParamsPanel;

    public SqlErrorProcessPanel(Project project, BslErrorMod bslErrorMod) {
        this.sqlAndParamsPanel = new SqlAndParamsPanel(project, bslErrorMod);
        first.add(sqlAndParamsPanel.getRoot());
        final SqlAndParamsProcessPanel sqlAndParamsProcessPanel = new SqlAndParamsProcessPanel(project, bslErrorMod);
        second.add(sqlAndParamsProcessPanel.getRoot());
    }

    public void reformatCode() {
        sqlAndParamsPanel.reformatCode();
    }

}
