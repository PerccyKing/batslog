package cn.com.pism.batslog.util;

import cn.com.pism.batslog.constants.BatsLogConstant;
import cn.com.pism.batslog.constants.KeyWordsConstant;
import cn.com.pism.batslog.enums.DbType;
import cn.com.pism.batslog.model.BslErrorMod;
import cn.com.pism.batslog.model.FormatIn;
import cn.com.pism.batslog.settings.BatsLogConfig;
import cn.com.pism.batslog.settings.BatsLogSettingState;
import com.alibaba.druid.sql.SQLUtils;
import com.alibaba.druid.util.JdbcConstants;
import com.intellij.execution.impl.ConsoleViewImpl;
import com.intellij.execution.ui.ConsoleViewContentType;
import com.intellij.openapi.project.Project;
import org.apache.commons.collections.CollectionUtils;
import org.apache.commons.lang3.StringUtils;
import org.apache.commons.lang3.time.DateFormatUtils;
import org.jetbrains.annotations.NotNull;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import javax.swing.table.DefaultTableModel;
import java.util.*;

import static cn.com.pism.batslog.constants.BatsLogConstant.*;

/**
 * @author PerccyKing
 * @version 0.0.1
 * @since 2021/07/05 下午 03:33
 */
public class SqlFormatUtil {

    private static final Logger log = LoggerFactory.getLogger(SqlFormatUtil.class);


    private static Set<String> keywords;


    /**
     * <p>
     * 格式化SQL
     * </p>
     *
     * @param str     : 日志字符串
     * @param project : 项目对象
     * @author PerccyKing
     * @since 2023/1/23 14:13
     */
    public static void format(String str, Project project) {
        format(str, project, Boolean.TRUE);
    }

    /**
     * <p>
     * 手动格式化SQL
     * </p>
     *
     * @param str      : 日志字符串
     * @param project  : 项目对象
     * @param callBack : 事件回调
     * @author PerccyKing
     * @since 2023/1/23 14:14
     */
    public static void manualFormat(String str, Project project, CallBack callBack) {
        format(FormatIn.builder().log(str).project(project).printToConsole(Boolean.FALSE).callBack(callBack).build());
    }

    /**
     * <p>
     * 格式SQL
     * </p>
     *
     * @param str            : 日志字符串
     * @param project        : 项目对象
     * @param printToConsole : 是否输出到console
     * @author PerccyKing
     * @since 2023/1/23 14:15
     */
    public static void format(String str, Project project, Boolean printToConsole) {
        format(FormatIn.builder().log(str).project(project).printToConsole(printToConsole).build());
    }

    public static void format(String str,
                              Project project,
                              Boolean printToConsole,
                              ConsoleViewImpl console,
                              CallBack callBack) {
        format(FormatIn.builder().log(str).project(project).printToConsole(printToConsole)
                .console(console).callBack(callBack).build());
    }

    /**
     * <p>
     * 格式化SQL
     * </p>
     *
     * @param fi:格式化参数
     * @author PerccyKing
     * @since 2023/1/23 14:16
     */
    public static void format(FormatIn fi) {

        List<String> sqlList = new ArrayList<>();
        List<String> paramsList = new ArrayList<>();
        List<String> nameList = new ArrayList<>();

        BatsLogConfig service = BatsLogSettingState.getInstance(fi.getProject());

        String sqlPrefix = StringUtils.isBlank(service.getSqlPrefix()) ? SQL_PREFIX : service.getSqlPrefix();
        String paramsPrefix = StringUtils.isBlank(service.getParamsPrefix()) ? PARAMS_PREFIX : service.getParamsPrefix();

        //如果开启了多前缀模式，并且只有一个前缀的时候，也执行默认单前缀逻辑
        if (Boolean.TRUE.equals(service.getEnableMixedPrefix()) && sqlPrefix.split(",").length > 1) {
            enabledMultiSqlPrefix(fi.getLog(), sqlList, paramsList, nameList, sqlPrefix, paramsPrefix);
        } else {
            notEnabledMultiSqlPrefix(fi.getLog(), sqlList, paramsList, nameList, sqlPrefix, paramsPrefix);
        }

        if (fi.getCallBack() != null) {
            fi.getCallBack().callback(!sqlList.isEmpty() ? sqlList.get(0) : "", !paramsList.isEmpty() ? paramsList.get(0) : "");
        } else {
            print(fi.getProject(), fi.isPrintToConsole(), fi.getConsole(), sqlList, paramsList, nameList, service);
        }
    }

    /**
     * <p>
     * 开启了混合SQL前缀
     * </p>
     *
     * @param str          : 日志字符串
     * @param sqlList      : sql列表
     * @param paramsList   :参数列表
     * @param nameList     : 行名称列表
     * @param sqlPrefix    : sql前缀
     * @param paramsPrefix : 参数前缀
     * @author PerccyKing
     * @since 2023/1/23 14:18
     */
    private static void enabledMultiSqlPrefix(String str, List<String> sqlList, List<String> paramsList, List<String> nameList, String sqlPrefix, String paramsPrefix) {

        //提取全部的sql和params
        String[] lines = new String[0];
        if (StringUtils.isNotBlank(str)) {
            int startSqlPrefixIndex = StringUtils.indexOfAny(str, sqlPrefix.split(","));
            //先截断一次，从SQL_PREFIX 行开始解析
            String includeFirstLine = str.substring(0, startSqlPrefixIndex);
            String firstName = getLineName(includeFirstLine);
            str = str.substring(startSqlPrefixIndex);
            str = firstName + str;
            lines = str.split("\n");
        }

        //下一行是否是参数
        boolean nextLineIsParams = false;

        String currSqlPrefix = "";
        for (String line : lines) {
            String[] sqlPrefixArr = sqlPrefix.split(",");
            if (sqlPrefixArr.length > 1) {
                for (String s : sqlPrefixArr) {
                    int i = StringUtils.indexOf(line, s);
                    if (i >= 0) {
                        currSqlPrefix = s;
                    }
                }
            }
            nextLineIsParams = processLine(sqlList, paramsList, nameList, paramsPrefix, nextLineIsParams, currSqlPrefix, line);
        }
    }

    /**
     * <p>
     * 获取当前行的名称：截取日志首行作为名称
     * </p>
     *
     * @param includeFirstLine : 首行日志
     * @return {@link String} 当前行名称
     * @author PerccyKing
     * @since 2023/1/23 16:32
     */
    private static String getLineName(String includeFirstLine) {
        boolean lineFeed = includeFirstLine.contains("\n");
        String firstName;
        if (lineFeed) {
            String[] includeFirstLineArr = includeFirstLine.split("\n");
            firstName = includeFirstLineArr[includeFirstLineArr.length - 1];
        } else {
            firstName = includeFirstLine;
        }
        return firstName;
    }

    private static boolean processLine(List<String> sqlList, List<String> paramsList, List<String> nameList, String paramsPrefix, boolean nextLineIsParams, String currSqlPrefix, String line) {
        int sqlStart = StringUtils.indexOf(line, currSqlPrefix);
        int paramsStart = StringUtils.indexOf(line, paramsPrefix);
        if (sqlStart >= 0) {
            sqlList.add(line.substring(sqlStart + currSqlPrefix.getBytes().length));
            nameList.add(getName(line, currSqlPrefix, sqlStart));
        } else if (paramsStart > 0) {
            paramsList.add(line.substring(paramsStart + paramsPrefix.getBytes().length));
            //最后一个字符不是 )
            nextLineIsParams = nextLineIsParams(line, paramsPrefix);
        } else if (nextLineIsParams) {
            int index = paramsList.size() - 1;
            paramsList.set(index, paramsList.get(index) + "\r\n" + line);
            //下一行可能还是参数，只是参数被换行符换到下一行了，先判断是否为 ) 结尾
            if (line.endsWith(")")) {
                //校验末尾是否为参数
                nextLineIsParams = nextLineIsParams(line, paramsPrefix);
            }
        }
        return nextLineIsParams;
    }

    /**
     * <p>
     * 未开启混合SQL前缀
     * </p>
     *
     * @param str          : 日志字符串
     * @param sqlList      : SQL列表
     * @param paramsList   : 参数列表
     * @param nameList     : 名称列表
     * @param sqlPrefix    : SQL前缀
     * @param paramsPrefix : 参数前缀
     * @author PerccyKing
     * @since 2023/1/23 14:27
     */
    private static void notEnabledMultiSqlPrefix(String str, List<String> sqlList, List<String> paramsList, List<String> nameList, String sqlPrefix, String paramsPrefix) {
        //提取全部的sql和params
        String[] lines = new String[0];
        if (StringUtils.isNotBlank(str)) {
            //先截断一次，从SQL_PREFIX 行开始解析
            String includeFirstLine = str.substring(0, str.indexOf(sqlPrefix));
            String firstName = getLineName(includeFirstLine);
            str = str.substring(str.indexOf(sqlPrefix));
            str = firstName + str;
            lines = str.split("\n");
        }

        //下一行是否是参数
        boolean nextLineIsParams = false;

        for (String line : lines) {
            nextLineIsParams = processLine(sqlList, paramsList, nameList, paramsPrefix, nextLineIsParams, sqlPrefix, line);
        }
    }

    /**
     * <p>
     * 判断下一行是否为参数
     * </p>
     *
     * @param line : 当前行
     * @return {@link boolean}
     * @author PerccyKing
     * @since 2021/08/12 上午 10:05
     */
    public static boolean nextLineIsParams(String line, String paramsPrefix) {
        //如果当前行，是参数行的开始，并且参数后没有任何东西，判定下一行不为参数行
        if (line.contains(paramsPrefix) && StringUtils.isBlank(line.substring(line.indexOf(paramsPrefix) + paramsPrefix.length()))) {
            return false;
        }
        //先检查当前行有没有左括号
        int leftIndex = StringUtils.lastIndexOf(line, "(");
        //没有左括号，先占时判定为当前行不是最后一行，下一行可能为参数行
        if (leftIndex >= 0 && line.endsWith(")")) {
            //断言括号之间可能是某个参数类型
            String assertType = StringUtils.substring(line, leftIndex);
            assertType = assertType.substring(assertType.indexOf("(") + 1, assertType.lastIndexOf(")"));
            //如果能匹配上预置参数类型，说明当前行就是最后一行，下一行不为参数行
            return !(TYPES.stream().anyMatch(assertType::equalsIgnoreCase)
                    || CLOUD_BE_TYPES.stream().anyMatch(assertType::equalsIgnoreCase));
        } else {
            return true;
        }

    }

    /**
     * <p>
     * 打印SQL
     * </p>
     *
     * @param project        : 项目对象
     * @param printToConsole : 是否输出到console
     * @param console        : console对象
     * @param sqlList        : sql列表
     * @param paramsList     : 参数列表
     * @param nameList       : 名称列表
     * @param service        : batslog配置service
     * @author PerccyKing
     * @since 2023/2/8 10:21
     */
    private static void print(Project project, Boolean printToConsole, ConsoleViewImpl console, List<String> sqlList,
                              List<String> paramsList, List<String> nameList, BatsLogConfig service) {
        String dbTypeStr = JdbcConstants.MYSQL.name();
        DbType dbType = BatsLogSettingState.getInstance(project).getDbType();
        if (!DbType.NONE.equals(dbType)) {
            dbTypeStr = dbType.getType();
        }

        SQLUtils.FormatOption formatOption = getFormatOption(service);

        for (int i = 0; i < paramsList.size(); i++) {
            String sql = sqlList.get(i);
            String name = nameList.get(i);
            String params = paramsList.get(i);
            try {
                //提取参数
                List<Object> paramList = parseParamToList(params);

                String formatSql = SQLUtils.format(sql, com.alibaba.druid.DbType.of(dbTypeStr.toLowerCase(Locale.ROOT)), paramList, formatOption);
                if (!formatSql.endsWith(";")) {
                    formatSql = formatSql + ";";
                }
                if (Boolean.TRUE.equals(printToConsole)) {
                    if (console == null) {
                        console = (ConsoleViewImpl) GlobalVar.getConsoleView(project);
                    }
                    // 先打印一次分隔符和‘SQL名称’
                    printSeparatorAndName(project, console, name, service);
                    //打印SQL
                    printSql(formatSql, project, console);
                } else {
                    //放入缓存
                    List<String> sqlCache = GlobalVar.getSqlCacheList(project);
                    if (!CollectionUtils.isNotEmpty(sqlCache)) {
                        sqlCache = new ArrayList<>();
                    }
                    sqlCache.add(formatSql);
                    GlobalVar.putSqlCache(project, sqlCache);
                }
            } catch (Exception e) {
                exceptionHandle(project, sql, params, e);
            }
        }
    }

    /**
     * <p>
     * 异常处理
     * </p>
     *
     * @param project : 项目对象
     * @param sql     : 解析异常的sql
     * @param params  : 解析异常的参数
     * @param e       : 异常详细信息
     * @author PerccyKing
     * @since 2023/1/23 14:25
     */
    private static void exceptionHandle(Project project, String sql, String params, Exception e) {
        log.error(e.getMessage());
        //对错误解析的数据 进行补偿处理，先将参数行和SQL行加入列表
        final DefaultTableModel tableModel = GlobalVar.getErrorListTableModel(project);
        final StackTraceElement[] stackTrace = e.getStackTrace();
        StringBuilder errorMsg = new StringBuilder();
        for (StackTraceElement stackTraceElement : stackTrace) {
            errorMsg.append(stackTraceElement.toString()).append("\n");
        }
        tableModel.addRow(new BslErrorMod(sql, params, DateFormatUtils.format(System.currentTimeMillis(), "yy/MM/dd HH:mm:ss"),
                errorMsg.toString()).toArray());
        e.printStackTrace();
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
     * @since 2021/04/26 下午 08:44
     */
    public static void printSql(String sql, Project project, ConsoleViewImpl consoleView) {

        Map<String, ConsoleViewContentType> keyColorMap = GlobalVar.getKeyColorMap(project);

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
            charLength = charLength + (StringUtils.isNotEmpty(word) ? word.length() : 0);
            String supplement;

            if (keyword) {
                printKeyWord(consoleView, project, word, defaultContentType);
            } else {
                consoleView.print(StringUtil.encoding(word, project), defaultContentType);
            }
            if (charLength < chars.length) {
                supplement = chars[charLength];
                charLength = charLength + supplement.length();
                consoleView.print(supplement, defaultContentType);
            }
        }
        consoleView.print("\n", defaultContentType);
    }


    public static void printKeyWord(ConsoleViewImpl consoleView, Project project, String keyWord, ConsoleViewContentType contentType) {
        ConsoleViewContentType keyWordContentType = ColoringUtil.getKeyWordConsoleViewContentTypeFromConfig(project);
        keyWordContentType.getAttributes().setBackgroundColor(contentType.getAttributes().getBackgroundColor());
        boolean isEnabledKeyWordDefCol = BatsLogSettingState.getInstance(project).isEnabledKeyWordDefCol();
        if (isEnabledKeyWordDefCol) {
            consoleView.print(StringUtil.encoding(keyWord, project), keyWordContentType);
        } else {
            consoleView.print(StringUtil.encoding(keyWord, project), contentType);

        }
    }

    /**
     * <p>
     * 校验SQL是否是关键字
     * </p>
     *
     * @param str : 待校验字符串
     * @return {@link boolean}
     * @author PerccyKing
     * @since 2023/1/23 14:21
     */
    public static boolean isKeyword(String str) {
        if (str == null) {
            return false;
        }

        String nameLower = str.toLowerCase();

        Set<String> words = keywords;
        if (CollectionUtils.isEmpty(words)) {
            words = KeyWordsConstant.MYSQL;
            keywords = words;
        }

        return words.contains(nameLower);
    }


    /**
     * <p>
     * 打印分隔符和行名称
     * </p>
     *
     * @param project : 项目
     * @param console : console
     * @param name    : 行名称
     * @param service : service
     * @author PerccyKing
     * @since 2021/04/26 下午 08:42
     */
    private static void printSeparatorAndName(Project project, ConsoleViewImpl console, String name, BatsLogConfig service) {
        console.print(StringUtil.encoding(BatsLogConstant.SEPARATOR, project), ConsoleViewContentType.ERROR_OUTPUT);
        int num = GlobalVar.getSqlNumber();
        num++;
        GlobalVar.setSqlNumber(num);
        String timestamp = " ";
        long timeMillis = System.currentTimeMillis();
        if (Boolean.TRUE.equals(service.getAddTimestamp())) {
            String timeFormat = service.getTimeFormat();
            timestamp = " " + (StringUtils.isBlank(timeFormat) ? timeMillis : DateFormatUtils.format(timeMillis, timeFormat)) + " ";
        }
        console.print(StringUtil.encoding("# " + String.format("%04d", num), project), ColoringUtil.getNoteColor(project));
        console.print(timestamp, ConsoleViewContentType.ERROR_OUTPUT);
        console.print(name + "\n", ColoringUtil.getNoteColor(project));
    }

    /**
     * <p>
     * 获取格式化配置
     * </p>
     *
     * @param service : 配置
     * @return {@link SQLUtils.FormatOption}
     * @author PerccyKing
     * @since 2021/05/19 下午 09:29
     */
    @NotNull
    private static SQLUtils.FormatOption getFormatOption(BatsLogConfig service) {
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
     * @param params : 待解析的参数
     * @return {@link List<Object>}
     * @author PerccyKing
     * @since 2021/05/19 下午 09:23
     */
    @NotNull
    public static List<Object> parseParamToList(String params) {
        if (StringUtils.isBlank(params)) {
            return new ArrayList<>();
        }
        String[] paramArr = params.split("\\),");
        List<Object> paramList = new ArrayList<>();
        //fix https://github.com/PerccyKing/batslog/issues/12 参数为空的时候会造成这种情况
        for (String s : paramArr) {
            if (StringUtils.isNotBlank(s)) {
                s = s.trim();
                String par;
                String type = null;
                if (s.contains("(")) {
                    int i = s.lastIndexOf("(");
                    par = s.substring(0, i);
                    type = s.substring(i + 1);
                } else {
                    par = s;
                }

                try {
                    paramList.add(pack(par.trim(), type));
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


    public static Object pack(String par, String type) throws ClassNotFoundException {
        Object typeParam = null;
        if (StringUtils.isBlank(type)) {
            String nullStr = "null";
            if (!nullStr.equals(par)) {
                typeParam = par;
            }
        } else if (TYPES.stream().anyMatch(type::equalsIgnoreCase)) {
            Class<?> aClass = Class.forName("java.lang." + type);
            if (aClass == Integer.class) {
                typeParam = Integer.valueOf(par);
            } else if (aClass == Long.class) {
                typeParam = Long.valueOf(par);
            } else if (aClass == Double.class) {
                typeParam = Double.valueOf(par);
            } else if (aClass == Boolean.class) {
                typeParam = Boolean.valueOf(par);
            } else if (aClass == Byte.class) {
                typeParam = Byte.valueOf(par);
            } else if (aClass == Short.class) {
                typeParam = Short.valueOf(par);
            } else if (aClass == Float.class) {
                typeParam = Float.valueOf(par);
            } else {
                typeParam = String.valueOf(par);
            }
        } else {
            typeParam = par;
        }
        return typeParam;
    }


    private static String getName(String str, String sqlPrefix, int start) {
        String name = "";
        try {
            String[] lines = StringUtils.split(str.substring(0, start + sqlPrefix.getBytes().length), "\n");
            String line = lines[lines.length - 1];
            if (StringUtils.isNotBlank(line)) {
                name = StringUtils.substring(line, 0, StringUtils.indexOf(line, sqlPrefix));
            }
        } catch (Exception e) {
            log.error(e.getMessage());
        }
        return name;
    }


    public interface CallBack {
        void callback(String sql, String params);
    }
}
