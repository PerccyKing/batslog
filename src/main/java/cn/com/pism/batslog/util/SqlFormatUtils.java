package cn.com.pism.batslog.util;

import cn.com.pism.batslog.constants.BatsLogConstant;
import com.alibaba.druid.sql.SQLUtils;
import com.alibaba.druid.util.JdbcConstants;
import com.intellij.execution.ui.ConsoleViewContentType;
import org.apache.commons.lang3.StringUtils;

import java.util.ArrayList;
import java.util.List;

import static cn.com.pism.batslog.util.BatsLogUtil.PARAMETERS;
import static cn.com.pism.batslog.util.BatsLogUtil.PREPARING;

/**
 * @author PerccyKing
 * @version 0.0.1
 * @date 2020/10/19 下午 08:55
 * @since 0.0.1
 */
public class SqlFormatUtils {
    public static void format(String str) {
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

        String formatSql = SQLUtils.format(sql, JdbcConstants.MYSQL, paramList);
        printSql(formatSql, "");

        String substring = subStr.substring(paramEnd);
        if (StringUtils.indexOf(substring, PREPARING) > 0) {
            format(subStr);
        }
    }

    private static void printSql(String sql, String methodName) {
        BatsLogUtil.CONSOLE_VIEW.print(StringUtil.encoding(BatsLogConstant.SEPARATOR), ConsoleViewContentType.ERROR_OUTPUT);
        BatsLogUtil.CONSOLE_VIEW.print(StringUtil.encoding(sql + "\n"), ConsoleViewContentType.LOG_INFO_OUTPUT);
        BatsLogUtil.PANE_BAR.setValue(BatsLogUtil.PANE_BAR.getMaximum());
    }
}
