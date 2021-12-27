package cn.com.pism.batslog.util;

import sun.nio.cs.GBK;

/**
 * @author PerccyKing
 * @version 0.0.1
 * @date 2020/10/25 下午 08:47
 * @since 0.0.1
 */
public class StringUtil {
    public static String encoding(String str) {
        return new String(str.getBytes(), GBK.defaultCharset());
    }
}
