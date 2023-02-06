package cn.com.pism.batslog.util;

import cn.com.pism.batslog.ui.FormatConsole;
import com.intellij.execution.ui.ConsoleView;
import com.intellij.execution.ui.ConsoleViewContentType;
import com.intellij.openapi.project.Project;
import com.intellij.ui.components.OnOffButton;

import javax.swing.table.DefaultTableModel;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * 全局变量
 *
 * @author PerccyKing
 * @since 2023/1/22 16:41
 */
public class GlobalVar {
    private GlobalVar() {
    }

    /**
     * format console
     */
    private static final Map<Project, FormatConsole> FORMAT_CONSOLE_MAP = new HashMap<>();

    public static FormatConsole getFormatConsole(Project project) {
        return FORMAT_CONSOLE_MAP.get(project);
    }

    public static void putFormatConsole(Project project, FormatConsole formatConsole) {
        FORMAT_CONSOLE_MAP.put(project, formatConsole);
    }

    /**
     * 项目监控状态
     */
    private static final Map<Project, Boolean> TAIL_STATUS = new HashMap<>();


    public static Boolean getTailStatus(Project project) {
        return TAIL_STATUS.get(project);
    }

    public static void putTailStatus(Project project, Boolean bol) {
        TAIL_STATUS.put(project, bol);
    }

    /**
     * consoleView
     */
    private static final Map<Project, ConsoleView> CONSOLE_VIEW_MAP = new HashMap<>();

    public static ConsoleView getConsoleView(Project project) {
        return CONSOLE_VIEW_MAP.get(project);
    }

    public static void putConsoleView(Project project, ConsoleView consoleView) {
        CONSOLE_VIEW_MAP.put(project, consoleView);
    }

    /**
     * 美化按钮
     */
    private static OnOffButton prettyFormat;

    public static OnOffButton getPrettyFormat() {
        return prettyFormat;
    }

    public static void setPrettyFormat(OnOffButton prettyFormat) {
        GlobalVar.prettyFormat = prettyFormat;
    }

    /**
     * 从console中解析到的sql行和参数行
     */
    private static final Map<Project, List<String>> SOURCE_SQL_LIST_MAP = new HashMap<>();

    public static List<String> getSourceSqlList(Project project) {
        return SOURCE_SQL_LIST_MAP.get(project);
    }

    public static void putSourceSqlList(Project project, List<String> sourceList) {
        SOURCE_SQL_LIST_MAP.put(project, sourceList);
    }

    public static void removeSourceListByProject(Project project) {
        SOURCE_SQL_LIST_MAP.remove(project);
    }

    /**
     * sql缓存
     */
    private static final Map<Project, List<String>> SQL_CACHE = new HashMap<>();

    public static List<String> getSqlCacheList(Project project) {
        return SQL_CACHE.get(project);
    }

    public static void putSqlCache(Project project, List<String> sqlCacheList) {
        SQL_CACHE.put(project, sqlCacheList);
    }

    /**
     * 关键字颜色配置
     */
    private static Map<String, ConsoleViewContentType> keyColorMap;

    public static Map<String, ConsoleViewContentType> getKeyColorMap() {
        return keyColorMap;
    }

    public static void setKeyColorMap(Map<String, ConsoleViewContentType> keyColorMap) {
        GlobalVar.keyColorMap = keyColorMap;
    }


    /**
     * sql编号
     */
    private static int sqlNumber = 0;

    public static int getSqlNumber() {
        return sqlNumber;
    }

    public static void setSqlNumber(int sqlNumber) {
        GlobalVar.sqlNumber = sqlNumber;
    }


    /**
     * sql错误table model
     */
    private static final Map<Project, DefaultTableModel> ERROR_LIST_TABLE_MODEL = new HashMap<>(0);

    public static DefaultTableModel getErrorListTableModel(Project project) {
        return ERROR_LIST_TABLE_MODEL.get(project);
    }

    public static void putErrorListTableModel(Project project, DefaultTableModel tableModel) {
        ERROR_LIST_TABLE_MODEL.put(project, tableModel);
    }
}