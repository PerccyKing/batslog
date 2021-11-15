package cn.com.pism.batslog.util;

import cn.com.pism.batslog.constants.BatsLogConstant;
import cn.com.pism.batslog.constants.KeyWordsConstant;
import cn.com.pism.batslog.enums.DbType;
import cn.com.pism.batslog.model.BslErrorMod;
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

import static cn.com.pism.batslog.constants.BatsLogConstant.PARAMS_PREFIX;
import static cn.com.pism.batslog.constants.BatsLogConstant.SQL_PREFIX;
import static cn.com.pism.batslog.util.BatsLogUtil.ERROR_LIST_TABLE_MODEL;

/**
 * @author PerccyKing
 * @version 0.0.1
 * @date 2021/07/05 下午 03:33
 * @since 0.0.1
 */
public class SqlFormatUtil {

    private static final Logger log = LoggerFactory.getLogger(SqlFormatUtil.class);


    public static final String[] TYPES = new String[]{"Integer", "Long", "Double", "String",
            "Boolean", "Byte", "Short", "Float"};

    public static final String[] CLOUD_BE_TYPES = new String[]{
            "boolean", "byte", "short", "int",
            "long", "float", "double", "BigDecimal",
            "String", "Date", "Time", "Timestamp",
            "InputStream", "Object", "Reader", "Ref",
            "Blob", "Clob", "Array", "URL", "RowId",
            "NClob", "SQLXML"
    };

    private static final int CACHE_LENGTH = 100;

    private static Set<String> keywords;


    public static void format(String str, Project project) {
        format(str, project, Boolean.TRUE);
    }

    public static void format(String str, Project project, Boolean printToConsole) {
        format(str, project, printToConsole, null);
    }


    public static void format(String str, Project project, Boolean printToConsole, ConsoleViewImpl console) {

        List<String> sqlList = new ArrayList<>();
        List<String> paramsList = new ArrayList<>();
        List<String> nameList = new ArrayList<>();

        BatsLogSettingState service = BatsLogSettingState.getInstance(project);

        String sqlPrefix = StringUtils.isBlank(service.getSqlPrefix()) ? SQL_PREFIX : service.getSqlPrefix();
        String paramsPrefix = StringUtils.isBlank(service.getParamsPrefix()) ? PARAMS_PREFIX : service.getParamsPrefix();


        //提取全部的sql和params
        String[] lines = new String[0];
        if (StringUtils.isNotBlank(str)) {
            //先截断一次，从SQL_PREFIX 行开始解析
            String includeFirstLine = str.substring(0, str.indexOf(sqlPrefix));
            boolean lineFeed = includeFirstLine.contains("\n");
            String firstName = " ";
            if (lineFeed) {
                String[] includeFirstLineArr = includeFirstLine.split("\n");
                firstName = includeFirstLineArr[includeFirstLineArr.length - 1];
            } else {
                firstName = includeFirstLine;
            }
            str = str.substring(str.indexOf(sqlPrefix));
            str = firstName + str;
            lines = str.split("\n");
        }

        //下一行是否是参数
        boolean nextLineIsParams = false;

        for (String line : lines) {
            int sqlStart = StringUtils.indexOf(line, sqlPrefix);
            int paramsStart = StringUtils.indexOf(line, paramsPrefix);
            if (sqlStart >= 0) {
                sqlList.add(line.substring(sqlStart + sqlPrefix.getBytes().length));
                nameList.add(getName(line, sqlPrefix, sqlStart));
            } else if (paramsStart > 0) {
                paramsList.add(line.substring(paramsStart + paramsPrefix.getBytes().length));
                //最后一个字符不是 )
                nextLineIsParams = nextLineIsParams(line);
            } else if (nextLineIsParams) {
                int index = paramsList.size() - 1;
                paramsList.set(index, paramsList.get(index) + "\r\n" + line);
                //下一行可能还是参数，只是参数被换行符换到下一行了，先判断是否为 ) 结尾
                if (line.endsWith(")")) {
                    //校验末尾是否为参数
                    nextLineIsParams = nextLineIsParams(line);
                }
            }
        }


        print(project, printToConsole, console, sqlList, paramsList, nameList, service);
    }

    /**
     * <p>
     * 判断下一行是否为参数
     * </p>
     *
     * @param line : 当前行
     * @return {@link boolean}
     * @author PerccyKing
     * @date 2021/08/12 上午 10:05
     */
    public static boolean nextLineIsParams(String line) {
        //如果当前行，只有一个换行符，判断为结束.获取为空行
        if ("\n".equals(line) || StringUtils.isBlank(line)) {
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
            return !(Arrays.stream(TYPES).anyMatch(assertType::equalsIgnoreCase)
                    || Arrays.stream(CLOUD_BE_TYPES).anyMatch(assertType::equalsIgnoreCase));
        } else {
            return true;
        }

    }

    private static void print(Project project, Boolean printToConsole, ConsoleViewImpl console, List<String> sqlList,
                              List<String> paramsList, List<String> nameList, BatsLogSettingState service) {
        String dbTypeStr = JdbcConstants.MYSQL;
        DbType dbType = service.getDbType();
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
                String[] paramArr = params.split(",");
                List<Object> paramList = parseParamToList(paramArr);
                String formatSql = SQLUtils.format(sql, dbTypeStr, paramList, formatOption);
                if (!formatSql.endsWith(";")) {
                    formatSql = formatSql + ";";
                }
                if (printToConsole) {
                    if (console == null) {
                        console = BatsLogUtil.CONSOLE_VIEW_MAP.get(project);
                    }

                    printSeparatorAndName(project, console, name, service);
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
            } catch (Exception e) {
                log.error(e.getMessage());
                //对错误解析的数据 进行补偿处理，先将参数行和SQL行加入列表
                final DefaultTableModel tableModel = ERROR_LIST_TABLE_MODEL.get(project);
                final StackTraceElement[] stackTrace = e.getStackTrace();
                StringBuilder errorMsg = new StringBuilder();
                for (StackTraceElement stackTraceElement : stackTrace) {
                    errorMsg.append(stackTraceElement.toString()).append("\n");
                }
                tableModel.addRow(new BslErrorMod(sql, params, DateFormatUtils.format(System.currentTimeMillis(), "yy/MM/dd HH:mm:ss"),
                        errorMsg.toString()).toArray());
                e.printStackTrace();
            }
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


    public static void printKeyWord(ConsoleViewImpl consoleView, Project project, String keyWord, ConsoleViewContentType contentType) {
        ConsoleViewContentType keyWordContentType = ColoringUtil.getKeyWordConsoleViewContentTypeFromConfig(project);
        keyWordContentType.getAttributes().setBackgroundColor(contentType.getAttributes().getBackgroundColor());
        consoleView.print(StringUtil.encoding(keyWord), keyWordContentType);
    }

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


    /**
     * <p>
     * 打印分隔符和行名称
     * </p>
     *
     * @param project : 项目
     * @param console : console
     * @param name    : 行名称
     * @param service
     * @author PerccyKing
     * @date 2021/04/26 下午 08:42
     */
    private static void printSeparatorAndName(Project project, ConsoleViewImpl console, String name, BatsLogSettingState service) {
        console.print(StringUtil.encoding(BatsLogConstant.SEPARATOR), ConsoleViewContentType.ERROR_OUTPUT);
        int num = BatsLogUtil.NUM;
        num++;
        BatsLogUtil.NUM = num;
        String timestamp = " ";
        long timeMillis = System.currentTimeMillis();
        if (Boolean.TRUE.equals(service.getAddTimestamp())) {
            String timeFormat = service.getTimeFormat();
            timestamp = " " + (StringUtils.isBlank(timeFormat) ? timeMillis : DateFormatUtils.format(timeMillis, timeFormat)) + " ";
        }
        console.print(StringUtil.encoding("# " + String.format("%04d", num)), ColoringUtil.getNoteColor(project));
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
                    int i = s.trim().lastIndexOf("(") + 1;
                    par = s.substring(0, i);
                    type = s.substring(i + 1, s.trim().lastIndexOf(")") + 1);
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


    private static String getName(String str, String sqlPrefix, int start) {
        String name = "";
        String[] lines = StringUtils.split(str.substring(0, start + sqlPrefix.getBytes().length), "\n");
        String line = lines[lines.length - 1];
        if (StringUtils.isNotBlank(line)) {
            name = StringUtils.substring(line, 0, StringUtils.indexOf(line, sqlPrefix));
        }
        return name;
    }
}
