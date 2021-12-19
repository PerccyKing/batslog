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
        final SqlAndParamsPanel sqlAndParamsPanel = new SqlAndParamsPanel(project, bslErrorMod);
        this.sqlAndParamsPanel = sqlAndParamsPanel;
        first.add(sqlAndParamsPanel.getRoot());
        final SqlAndParamsProcessPanel sqlAndParamsProcessPanel = new SqlAndParamsProcessPanel(project, bslErrorMod);
        second.add(sqlAndParamsProcessPanel.getRoot());
    }

    public void reformatCode() {
        sqlAndParamsPanel.reformatCode();
    }
    /*private JPanel sqlContent;
    private JPanel sqlContentLine;

    private Project project;

    private BslErrorMod bslErrorMod;

    private SqlEditorPanel sqlEditorPanel;

    public SqlErrorProcessPanel(Project project, BslErrorMod bslErrorMod) {
        this.project = project;
        this.bslErrorMod = bslErrorMod;

        final String sql = bslErrorMod.getSql();
        final SqlEditorPanel sqlEditorPanel = new SqlEditorPanel(project, sql);
        this.sqlEditorPanel = sqlEditorPanel;
        sqlContent.add(sqlEditorPanel.getRoot());
        sqlContentLine.setLayout(new GridLayoutManager(10, 1));
        final String[] split = sql.split("\\?");
        for (int i = 0; i < split.length; i++) {
            final String line = split[i];
            if (line.length() > 50) {
                addLineToRow(i, "……" + line.substring(line.length() - 50));
            } else {
                addLineToRow(i, line);
            }
        }
        GuiUtils.replaceJSplitPaneWithIDEASplitter(root);

    }

    private void addLineToRow(int i, String line) {
        final JPanel jPanel = new JPanel(new FlowLayout(FlowLayout.RIGHT));
        jPanel.add(new JLabel(line, SwingConstants.RIGHT));
        GridConstraints gridConstraints =
                new GridConstraints(i, 0, 1, 1,
                        GridConstraints.ANCHOR_EAST,
                        GridConstraints.FILL_HORIZONTAL,
                        GridConstraints.SIZEPOLICY_CAN_GROW,
                        GridConstraints.SIZEPOLICY_CAN_SHRINK,
                        new Dimension(-1, -1),
                        new Dimension(-1, -1),
                        new Dimension(-1, -1),
                        0
                );
        sqlContentLine.add(jPanel, gridConstraints);
    }*/

}
