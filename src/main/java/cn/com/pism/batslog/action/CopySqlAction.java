package cn.com.pism.batslog.action;

import cn.com.pism.batslog.util.BatsLogUtil;
import cn.com.pism.batslog.util.SqlFormatUtils;
import com.intellij.openapi.actionSystem.AnAction;
import com.intellij.openapi.actionSystem.AnActionEvent;
import com.intellij.openapi.actionSystem.CommonDataKeys;
import com.intellij.openapi.editor.Editor;
import com.intellij.openapi.editor.SelectionModel;

import java.awt.*;
import java.awt.datatransfer.Clipboard;
import java.awt.datatransfer.StringSelection;
import java.util.ArrayList;
import java.util.List;

/**
 * @author PerccyKing
 */
public class CopySqlAction extends AnAction {

    @Override
    public void actionPerformed(AnActionEvent e) {
        Editor editor = e.getRequiredData(CommonDataKeys.EDITOR);
        SelectionModel selectionModel = editor.getSelectionModel();
        String selectedText = selectionModel.getSelectedText();
        SqlFormatUtils.format(selectedText, e.getProject(), Boolean.FALSE);
        List<String> sqlCache = BatsLogUtil.SQL_CACHE.get(e.getProject());
        String cache = String.join(";\n\n", sqlCache);
        //复制到剪贴板
        Clipboard clipboard = Toolkit.getDefaultToolkit().getSystemClipboard();
        clipboard.setContents(new StringSelection(cache), null);
        //清空缓存
        BatsLogUtil.SQL_CACHE.put(e.getProject(), new ArrayList<>());
    }
}
