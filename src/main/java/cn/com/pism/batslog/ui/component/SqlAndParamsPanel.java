package cn.com.pism.batslog.ui.component;

import cn.com.pism.batslog.model.BslErrorMod;
import cn.com.pism.batslog.util.BatsLogUtil;
import com.intellij.lang.Language;
import com.intellij.openapi.project.Project;
import com.intellij.ui.GuiUtils;
import com.intellij.ui.LanguageTextField;
import lombok.Data;

import javax.swing.*;

/**
 * @author PerccyKing
 * @version 0.0.1
 * @since 2021/12/11 下午 05:32
 */
@Data
public class SqlAndParamsPanel {
    private JPanel root;
    private JPanel sqlContent;
    private JPanel paramsEditorPanel;

    private Project project;

    private SqlEditorPanel sqlEditorPanel;

    private BslErrorMod bslErrorMod;

    private LanguageTextField textField;

    public SqlAndParamsPanel(Project project, BslErrorMod bslErrorMod) {
        this.project = project;
        final SqlEditorPanel newSqlEditorPanel = new SqlEditorPanel(project, bslErrorMod.getSql());
        this.sqlEditorPanel = newSqlEditorPanel;
        sqlContent.add(newSqlEditorPanel.getRoot());

        this.textField = BatsLogUtil.createLanguageTextField(Language.findLanguageByID("TEXT"), project, bslErrorMod.getParams());
        paramsEditorPanel.add(textField);
        GuiUtils.replaceJSplitPaneWithIDEASplitter(root);
    }

    public void reformatCode() {
        sqlEditorPanel.reformatCode();
    }
}
