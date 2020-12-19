package cn.com.pism.batslog.util;

import cn.com.pism.batslog.constants.BatsLogConstant;
import cn.com.pism.batslog.constants.KeyWordsConstant;
import cn.com.pism.batslog.enums.DbType;
import cn.com.pism.batslog.settings.BatsLogSetting;
import com.alibaba.druid.sql.SQLUtils;
import com.alibaba.druid.util.JdbcConstants;
import com.intellij.execution.impl.ConsoleViewImpl;
import com.intellij.execution.ui.ConsoleViewContentType;
import com.intellij.openapi.project.Project;
import org.apache.commons.collections.CollectionUtils;
import org.apache.commons.lang3.StringUtils;

import java.util.ArrayList;
import java.util.List;
import java.util.Set;

import static cn.com.pism.batslog.util.BatsLogUtil.PARAMETERS;
import static cn.com.pism.batslog.util.BatsLogUtil.PREPARING;

/**
 * @author PerccyKing
 * @version 0.0.1
 * @date 2020/10/19 下午 08:55
 * @since 0.0.1
 */
public class SqlFormatUtils {
    public static void format(String str, Project project) {
        format(str, project, Boolean.TRUE);
    }

    public static void format(String str, Project project, Boolean printToConsole) {
        format(str, project, printToConsole, null);
    }

    public static void format(String str, Project project, Boolean printToConsole, ConsoleViewImpl console) {

        if (StringUtils.isNotBlank(str)) {
            str = str + "\nend";
            //从第一个====>  Preparing:开始
            int start = StringUtils.indexOf(str, PREPARING);
            String subStr = str.substring(start + PREPARING.getBytes().length);
            int sqlEnd = StringUtils.indexOf(subStr, "\n");
            String sql = subStr.substring(0, sqlEnd);
            //参数
            subStr = subStr.substring(sqlEnd);
            int paramStart = StringUtils.indexOf(subStr, PARAMETERS);
            subStr = subStr.substring(paramStart + PARAMETERS.getBytes().length);
            int paramEnd = StringUtils.indexOf(subStr, "\n");
            String params = subStr.substring(0, paramEnd);

            //提取参数
            String[] paramArr = params.split(",");
            List<Object> paramList = new ArrayList<>();
            for (String s : paramArr) {
                if (StringUtils.isNotBlank(s)) {
                    String par = s.substring(0, s.trim().indexOf("(") + 1);
                    paramList.add(par.trim());
                } else {
                    paramList.add("");
                }
            }

            String dbTypeStr = JdbcConstants.MYSQL;
            DbType dbType = BatsLogSetting.getDbType(project);
            if (!DbType.NONE.equals(dbType)) {
                dbTypeStr = dbType.getType();
            }

            String formatSql = SQLUtils.format(sql, dbTypeStr, paramList);
            if (printToConsole) {
                if (console == null) {
                    console = BatsLogUtil.CONSOLE_VIEW_MAP.get(project);
                }

                printSql(formatSql, "", project, console);
            } else {
                //放入缓存
                List<String> sqlCache = BatsLogUtil.SQL_CACHE.get(project);
                if (!CollectionUtils.isNotEmpty(sqlCache)) {
                    sqlCache = new ArrayList<>();
                }
                sqlCache.add(formatSql);
                BatsLogUtil.SQL_CACHE.put(project, sqlCache);
            }

            String substring = subStr.substring(paramEnd);
            if (StringUtils.indexOf(substring, PREPARING) > 0) {
                format(subStr, project, printToConsole, console);
            }
        }
    }

    private static void printSql(String sql, String methodName, Project project, ConsoleViewImpl consoleView) {

        consoleView.print(StringUtil.encoding(BatsLogConstant.SEPARATOR), ConsoleViewContentType.ERROR_OUTPUT);
        String[] chars = sql.split("");
        //关键字校验
        String[] words = sql.split(" |\t\n|\n|\t");
        int charLength = 0;
        for (String word : words) {
            boolean keyword = isKeyword(word);
            charLength = charLength + word.length();
            String supplement = "";

            if (keyword) {
                printKeyWord(consoleView, project, word);
            } else {
                consoleView.print(StringUtil.encoding(word), ConsoleViewContentType.NORMAL_OUTPUT);
            }
            if (charLength < chars.length) {
                supplement = chars[charLength];
                charLength = charLength + supplement.length();
                consoleView.print(supplement, ConsoleViewContentType.NORMAL_OUTPUT);
            }
        }
        consoleView.print("\n", ConsoleViewContentType.NORMAL_OUTPUT);
        BatsLogUtil.PANE_BAR.setValue(BatsLogUtil.PANE_BAR.getMaximum());
    }


    private static Set<String> keywords;

    public static boolean isKeyword(String name) {
        if (name == null) {
            return false;
        }

        String nameLower = name.toLowerCase();

        Set<String> words = keywords;

        if (words == null || words.size() == 0) {
            words = KeyWordsConstant.MYSQL;
            keywords = words;
        }

        return words.contains(nameLower);
    }

    public static void printKeyWord(ConsoleViewImpl consoleView, Project project, String keyWord) {
        consoleView.print(StringUtil.encoding(keyWord), ColoringUtil.getKeyWordConsoleViewContentTypeFromConfig(project));
    }

}
