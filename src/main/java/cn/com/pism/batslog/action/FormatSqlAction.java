package cn.com.pism.batslog.action;

import cn.com.pism.batslog.util.SqlFormatUtils;
import com.intellij.openapi.actionSystem.AnAction;
import com.intellij.openapi.actionSystem.AnActionEvent;
import com.intellij.openapi.actionSystem.CommonDataKeys;
import com.intellij.openapi.editor.Editor;
import com.intellij.openapi.editor.SelectionModel;
import lombok.extern.log4j.Log4j;

/**
 * @author PerccyKing
 * @date 2020/10/18 下午 05:30
 * @version 0.0.1
 * @since 0.0.1
 */
@Log4j
public class FormatSqlAction extends AnAction {

    @Override
    public void actionPerformed(AnActionEvent e) {
        Editor editor = e.getRequiredData(CommonDataKeys.EDITOR);
        SelectionModel selectionModel = editor.getSelectionModel();
        String selectedText = selectionModel.getSelectedText();
        SqlFormatUtils.format(selectedText);
    }
}
