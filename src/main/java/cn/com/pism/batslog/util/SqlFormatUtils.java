package cn.com.pism.batslog.util;

import cn.com.pism.batslog.constants.BatsLogConstant;
import cn.com.pism.batslog.constants.KeyWordsConstant;
import cn.com.pism.batslog.enums.DbType;
import cn.com.pism.batslog.settings.BatsLogSettingState;
import com.alibaba.druid.sql.SQLUtils;
import com.alibaba.druid.util.JdbcConstants;
import com.intellij.execution.impl.ConsoleViewImpl;
import com.intellij.execution.ui.ConsoleViewContentType;
import com.intellij.openapi.components.ServiceManager;
import com.intellij.openapi.project.Project;
import org.apache.commons.collections.CollectionUtils;
import org.apache.commons.lang3.StringUtils;
import org.jetbrains.annotations.NotNull;

import java.util.*;

import static cn.com.pism.batslog.constants.BatsLogConstant.PARAMS_PREFIX;
import static cn.com.pism.batslog.constants.BatsLogConstant.SQL_PREFIX;

/**
 * @author PerccyKing
 * @version 0.0.1
 * @date 2020/10/19 下午 08:55
 * @since 0.0.1
 */
public class SqlFormatUtils {

    public static final String[] TYPES = new String[]{"Integer", "Long", "Double", "String",
            "Boolean", "Byte", "Short", "Float"};

    public static void format(String str, Project project) {
        format(str, project, Boolean.TRUE);
    }

    public static void format(String str, Project project, Boolean printToConsole) {
        format(str, project, printToConsole, null);
    }

    public static void format(String str, Project project, Boolean printToConsole, ConsoleViewImpl console) {
        BatsLogSettingState service = ServiceManager.getService(project, BatsLogSettingState.class);

        String sqlPrefix = service.getSqlPrefix();
        sqlPrefix = StringUtils.isBlank(sqlPrefix) ? SQL_PREFIX : sqlPrefix;
        String paramsPrefix = service.getParamsPrefix();
        paramsPrefix = StringUtils.isBlank(paramsPrefix) ? PARAMS_PREFIX : paramsPrefix;

        if (StringUtils.isNotBlank(str)) {
            str = str + "\nend";
            //从第一个====>  Preparing:开始
            int start = StringUtils.indexOf(str, sqlPrefix);
            if (start < 0) {
                return;
            }
            //提取sql所在行的前部分字符
            String name = "";
            name = getName(str, sqlPrefix, start, name);
            String subStr = str.substring(start + sqlPrefix.getBytes().length);
            int sqlEnd = StringUtils.indexOf(subStr, "\n");
            String sql = subStr.substring(0, sqlEnd);
            //参数
            subStr = subStr.substring(sqlEnd);
            int paramStart = StringUtils.indexOf(subStr, paramsPrefix);
            subStr = subStr.substring(paramStart + paramsPrefix.getBytes().length);
            int paramEnd = StringUtils.indexOf(subStr, "\n");
            String params = subStr.substring(0, paramEnd);

            //提取参数
            String[] paramArr = params.split(",");
            List<Object> paramList = parseParamToList(paramArr);

            String dbTypeStr = JdbcConstants.MYSQL;
            DbType dbType = service.getDbType();
            if (!DbType.NONE.equals(dbType)) {
                dbTypeStr = dbType.getType();
            }

            SQLUtils.FormatOption formatOption = getFormatOption(service);

            String formatSql = SQLUtils.format(sql, dbTypeStr, paramList, formatOption);
            if (!formatSql.endsWith(";")) {
                formatSql = formatSql + ";";
            }
            if (printToConsole) {
                if (console == null) {
                    console = BatsLogUtil.CONSOLE_VIEW_MAP.get(project);
                }

                printSeparatorAndName(project, console, name);
                printSql(formatSql, project, console);
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
            if (StringUtils.indexOf(substring, sqlPrefix) > 0) {
                format(subStr, project, printToConsole, console);
            }
        }
    }

    /**
     * <p>
     * 获取格式化配置
     * </p>
     *
     * @param service : 配置
     * @return {@link SQLUtils.FormatOption}
     * @author PerccyKing
     * @date 2021/05/19 下午 09:29
     */
    @NotNull
    private static SQLUtils.FormatOption getFormatOption(BatsLogSettingState service) {
        SQLUtils.FormatOption formatOption = new SQLUtils.FormatOption();
        formatOption.setDesensitize(service.getDesensitize());
        formatOption.setPrettyFormat(service.getPrettyFormat());
        formatOption.setParameterized(service.getParameterized());
        formatOption.setUppCase(service.getToUpperCase());
        return formatOption;
    }

    /**
     * <p>
     * 将参数数组转换为list，同时进行类型判断
     * </p>
     *
     * @param paramArr : 待解析的参数数组
     * @return {@link List<Object>}
     * @author PerccyKing
     * @date 2021/05/19 下午 09:23
     */
    @NotNull
    private static List<Object> parseParamToList(String[] paramArr) {
        List<Object> paramList = new ArrayList<>();
        //fix https://github.com/PerccyKing/batslog/issues/12 参数为空的时候会造成这种情况
        for (String s : paramArr) {
            if (StringUtils.isNotBlank(s)) {
                String par;
                String type = null;
                if (s.trim().contains("(")) {
                    int i = s.trim().indexOf("(") + 1;
                    par = s.substring(0, i);
                    type = s.substring(i + 1, s.trim().indexOf(")") + 1);
                } else {
                    par = s;
                }

                try {
                    pack(paramList, par.trim(), type);
                } catch (ClassNotFoundException e) {
                    e.printStackTrace();
                }
                //提取数据类型
            } else {
                paramList.add("");
            }
        }
        return paramList;
    }

    /**
     * <p>
     * 打印分隔符和行名称
     * </p>
     *
     * @param project : 项目
     * @param console : console
     * @param name    : 行名称
     * @author PerccyKing
     * @date 2021/04/26 下午 08:42
     */
    private static void printSeparatorAndName(Project project, ConsoleViewImpl console, String name) {
        console.print(StringUtil.encoding(BatsLogConstant.SEPARATOR), ConsoleViewContentType.ERROR_OUTPUT);
        if (StringUtils.isNotBlank(name)) {
            console.print(StringUtil.encoding("### " + name + "\n"), ColoringUtil.getNoteColor(project));
        }
    }

    private static String getName(String str, String sqlPrefix, int start, String name) {
        String[] lines = StringUtils.split(str.substring(0, start + sqlPrefix.getBytes().length), "\n");
        String line = lines[lines.length - 1];
        if (StringUtils.isNotBlank(line)) {
            name = StringUtils.substring(line, 0, StringUtils.indexOf(line, sqlPrefix));
        }
        return name;
    }

    private static void pack(List<Object> paramList, String par, String type) throws ClassNotFoundException {
        if (StringUtils.isBlank(type)) {
            if ("null".equals(par)) {
                paramList.add(null);
            } else {
                paramList.add(par);
            }
        } else if (Arrays.stream(TYPES).anyMatch(type::equalsIgnoreCase)) {
            Class<?> aClass = Class.forName("java.lang." + type);
            if (aClass == Integer.class) {
                paramList.add(Integer.valueOf(par));
            } else if (aClass == Long.class) {
                paramList.add(Long.valueOf(par));
            } else if (aClass == Double.class) {
                paramList.add(Double.valueOf(par));
            } else if (aClass == Boolean.class) {
                paramList.add(Boolean.valueOf(par));
            } else if (aClass == Byte.class) {
                paramList.add(Byte.valueOf(par));
            } else if (aClass == Short.class) {
                paramList.add(Short.valueOf(par));
            } else if (aClass == Float.class) {
                paramList.add(Float.valueOf(par));
            } else {
                paramList.add(String.valueOf(par));
            }
        } else {
            paramList.add(par);
        }

    }

    /**
     * <p>
     * 打印sql
     * </p>
     *
     * @param sql         : SQL ，仅sql
     * @param project     : 项目
     * @param consoleView : console
     * @author PerccyKing
     * @date 2021/04/26 下午 08:44
     */
    public static void printSql(String sql, Project project, ConsoleViewImpl consoleView) {

        Map<String, ConsoleViewContentType> keyColorMap = BatsLogUtil.KEY_COLOR_MAP;

        ConsoleViewContentType defaultContentType = ConsoleViewContentType.NORMAL_OUTPUT;

        for (Map.Entry<String, ConsoleViewContentType> entry : keyColorMap.entrySet()) {
            String k = entry.getKey();
            ConsoleViewContentType v = entry.getValue();
            if (sql.trim().toUpperCase(Locale.ROOT).startsWith(k)) {
                defaultContentType = v;
            }
        }
        String[] chars = sql.split("");
        //关键字校验
        String[] words = sql.split(" |\t\n|\n|\t");
        int charLength = 0;
        for (String word : words) {
            boolean keyword = isKeyword(word);
            charLength = charLength + word.length();
            String supplement = "";

            if (keyword) {
                printKeyWord(consoleView, project, word, defaultContentType);
            } else {
                consoleView.print(StringUtil.encoding(word), defaultContentType);
            }
            if (charLength < chars.length) {
                supplement = chars[charLength];
                charLength = charLength + supplement.length();
                consoleView.print(supplement, defaultContentType);
            }
        }
        consoleView.print("\n", defaultContentType);
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

    public static void printKeyWord(ConsoleViewImpl consoleView, Project project, String keyWord, ConsoleViewContentType contentType) {
        ConsoleViewContentType keyWordContentType = ColoringUtil.getKeyWordConsoleViewContentTypeFromConfig(project);
        keyWordContentType.getAttributes().setBackgroundColor(contentType.getAttributes().getBackgroundColor());
        consoleView.print(StringUtil.encoding(keyWord), keyWordContentType);
    }

}
