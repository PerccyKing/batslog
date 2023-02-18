package cn.com.pism.batslog.util;

import cn.com.pism.batslog.model.RgbColor;
import com.intellij.lang.Language;
import com.intellij.openapi.actionSystem.AnActionEvent;
import com.intellij.openapi.actionSystem.CommonDataKeys;
import com.intellij.openapi.editor.Editor;
import com.intellij.openapi.editor.SelectionModel;
import com.intellij.openapi.project.Project;
import com.intellij.ui.JBColor;
import com.intellij.ui.LanguageTextField;
import org.apache.commons.collections.CollectionUtils;

import java.awt.*;
import java.awt.datatransfer.Clipboard;
import java.awt.datatransfer.StringSelection;
import java.util.ArrayList;
import java.util.List;

/**
 * @author PerccyKing
 * @since 2020/10/26 下午 01:17
 */
public class BatsLogUtil {

    private BatsLogUtil() {
    }

    public static void copySqlToClipboard(AnActionEvent e, String text) {

        SqlFormatUtil.format(text, e.getProject(), Boolean.FALSE);
        List<String> sqlCache = GlobalVar.getSqlCacheList(e.getProject());
        if (CollectionUtils.isNotEmpty(sqlCache)) {
            String cache = String.join("\n\n", sqlCache);
            //复制到剪贴板
            copyToClipboard(cache);
        }
        //清空缓存
        GlobalVar.putSqlCache(e.getProject(), new ArrayList<>());
    }

    public static void copyToClipboard(String cache) {
        Clipboard clipboard = Toolkit.getDefaultToolkit().getSystemClipboard();
        clipboard.setContents(new StringSelection(cache), null);
    }

    public static Boolean getTailStatus(Project project) {
        Boolean tailStatus = GlobalVar.getTailStatus(project);
        tailStatus = tailStatus == null ? Boolean.FALSE : tailStatus;
        return tailStatus;
    }

    public static Color toColor(RgbColor rgbColor) {
        return new JBColor(new Color(rgbColor.getR(), rgbColor.getG(), rgbColor.getB()),
                new Color(rgbColor.getR(), rgbColor.getG(), rgbColor.getB()));
    }

    /**
     * <p>
     * 创建一个默认的LanguageTextField
     * </p>
     *
     * @param language :语言
     * @param project  :项目
     * @param str      :内容
     * @return {@link LanguageTextField}
     * @author PerccyKing
     * @since 2021/12/11 下午 06:06
     */
    public static LanguageTextField createLanguageTextField(Language language, Project project, String str) {
        LanguageTextField languageTextField = new LanguageTextField(language, project, str, false);
        languageTextField.ensureWillComputePreferredSize();
        languageTextField.getPreferredSize();
        languageTextField.addNotify();
        languageTextField.setEnabled(true);
        languageTextField.setText(str);
        return languageTextField;
    }


    public static String getSelectText(AnActionEvent e) {
        Editor editor = e.getRequiredData(CommonDataKeys.EDITOR);
        SelectionModel selectionModel = editor.getSelectionModel();
        return selectionModel.getSelectedText();
    }

    public static RgbColor toRgbColor(Color c) {
        return new RgbColor(c.getRed(), c.getGreen(), c.getBlue());
    }
}
