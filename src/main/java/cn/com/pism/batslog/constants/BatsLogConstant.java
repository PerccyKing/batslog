package cn.com.pism.batslog.constants;

import com.intellij.ui.JBColor;

import java.awt.*;
import java.util.Arrays;
import java.util.Collections;
import java.util.List;

/**
 * @author wangyihuai
 * @since 2020/10/27 下午 03:38
 */
public class BatsLogConstant {

    private BatsLogConstant() {
    }

    /**
     * 插件id
     */
    public static final String BATS_LOG_ID = "cn.com.pism.batslog";

    /**
     * 插件名称
     */
    public static final String BATS_LOG_NAME = "BatsLog";

    public static final String BATS_LOG = "\n" +
            "  ____        _       _                 \n" +
            " |  _ \\      | |     | |                \n" +
            " | |_) | __ _| |_ ___| |     ___   __ _ \n" +
            " |  _ < / _` | __/ __| |    / _ \\ / _` |\n" +
            " | |_) | (_| | |_\\__ \\ |___| (_) | (_| |\n" +
            " |____/ \\__,_|\\__|___/______\\___/ \\__, |\n" +
            "                                   __/ |\n" +
            "                                  |___/ \n";

    /**
     * 每行日志输出的分割线
     */
    public static final String SEPARATOR = "-- --------------------------------------\n";

    /**
     * 关键字默认颜色
     */
    public static final Color KEY_WORD_DEF_COL = new JBColor(new Color(204, 120, 50), new Color(204, 120, 50));


    /**
     * 日志SQL行前缀标记
     */
    public static final String SQL_PREFIX = "Preparing:";

    /**
     * 日志参数行浅醉标记
     */
    public static final String PARAMS_PREFIX = "Parameters:";

    /**
     * 默认时间戳格式
     */
    public static final String TIME_FORMAT = "yyyy-MM-dd HH:mm:ss";

    /**
     * 默认编码格式
     */
    public static final String DEFAULT_ENCODING = "AUTO";


    public static final List<String> TYPES = Collections.unmodifiableList(
            Arrays.asList("Integer", "Long", "Double", "String",
            "Boolean", "Byte", "Short", "Float"));

    /**
     * 参数行中可能出现的数据类型
     */
    public static final List<String> CLOUD_BE_TYPES = Collections.unmodifiableList(
            Arrays.asList("boolean", "byte", "short", "int",
                    "long", "float", "double", "BigDecimal",
                    "String", "Date", "Time", "Timestamp",
                    "InputStream", "Object", "Reader", "Ref",
                    "Blob", "Clob", "Array", "URL", "RowId",
                    "NClob", "SQLXML"));

}
