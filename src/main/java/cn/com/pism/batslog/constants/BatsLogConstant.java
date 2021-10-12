package cn.com.pism.batslog.constants;

import com.intellij.ui.JBColor;

import java.awt.*;

/**
 * @author wangyihuai
 * @date 2020/10/27 下午 03:38
 */
public class BatsLogConstant {
    public static final String BATS_LOG = "\n" +
            "  ____        _       _                 \n" +
            " |  _ \\      | |     | |                \n" +
            " | |_) | __ _| |_ ___| |     ___   __ _ \n" +
            " |  _ < / _` | __/ __| |    / _ \\ / _` |\n" +
            " | |_) | (_| | |_\\__ \\ |___| (_) | (_| |\n" +
            " |____/ \\__,_|\\__|___/______\\___/ \\__, |\n" +
            "                                   __/ |\n" +
            "                                  |___/ \n";

    public static final String SEPARATOR = "-- --------------------------------------\n";

    public static final String PRE = "\u001B[";

    public static final Color KEY_WORD_DEF_COL = new JBColor(new Color(204, 120, 50), new Color(204, 120, 50));


    public static String SQL_PREFIX = "Preparing:";
    public static String PARAMS_PREFIX = "Parameters:";

    public static String TIME_FORMAT = "yyyy-MM-dd HH:mm:ss";

    public static String BATS_LOG_ID = "cn.com.pism.batslog";

    public static String BATS_LOG_NAME = "BatsLog";
}
