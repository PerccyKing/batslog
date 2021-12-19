package cn.com.pism.batslog.util;

import cn.com.pism.batslog.model.RgbColor;
import cn.com.pism.batslog.ui.FormatConsole;
import com.intellij.execution.impl.ConsoleViewImpl;
import com.intellij.execution.ui.ConsoleViewContentType;
import com.intellij.lang.Language;
import com.intellij.openapi.actionSystem.AnAction;
import com.intellij.openapi.actionSystem.AnActionEvent;
import com.intellij.openapi.project.Project;
import com.intellij.openapi.wm.ex.ToolWindowEx;
import com.intellij.ui.JBColor;
import com.intellij.ui.LanguageTextField;
import com.intellij.ui.components.OnOffButton;
import org.apache.commons.collections.CollectionUtils;

import javax.swing.table.DefaultTableModel;
import java.awt.*;
import java.awt.datatransfer.Clipboard;
import java.awt.datatransfer.StringSelection;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * @author PerccyKing
 * @date 2020/10/26 下午 01:17
 */
public class BatsLogUtil {
    public static Map<Project, AnAction[]> TAIL_ACTION = new HashMap<>();
    public static ToolWindowEx TOOL_WINDOW;
    public static Map<Project, Boolean> TAIL_STATUS = new HashMap<>();
    public static Map<Project, ConsoleViewImpl> CONSOLE_VIEW_MAP = new HashMap<>();
    public static OnOffButton PRETTY_FORMAT;

    public static Map<Project, List<String>> SOURCE_SQL_LIST_MAP = new HashMap<>();

    public static Map<Project, List<String>> SQL_CACHE = new HashMap<>();

    public static Map<String, ConsoleViewContentType> KEY_COLOR_MAP = new HashMap<>();

    public static int NUM = 0;

    public static Map<Project, FormatConsole> FORMAT_CONSOLE_MAP = new HashMap<>();

    public static Map<Project, DefaultTableModel> ERROR_LIST_TABLE_MODEL = new HashMap<>();

    public static void copySqlToClipboard(AnActionEvent e, String text) {

        SqlFormatUtil.format(text, e.getProject(), Boolean.FALSE);
        List<String> sqlCache = BatsLogUtil.SQL_CACHE.get(e.getProject());
        if (CollectionUtils.isNotEmpty(sqlCache)) {
            String cache = String.join("\n\n", sqlCache);
            //复制到剪贴板
            copyToClipboard(cache);
        }
        //清空缓存
        BatsLogUtil.SQL_CACHE.put(e.getProject(), new ArrayList<>());
    }

    public static void copyToClipboard(String cache) {
        Clipboard clipboard = Toolkit.getDefaultToolkit().getSystemClipboard();
        clipboard.setContents(new StringSelection(cache), null);
    }

    public static Boolean getTailStatus(Project project) {
        Boolean tailStatus = TAIL_STATUS.get(project);
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
     * @date 2021/12/11 下午 06:06
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
}
