package cn.com.pism.batslog.util;

import static java.nio.charset.StandardCharsets.UTF_8;

/**
 * @author PerccyKing
 * @version 0.0.1
 * @date 2020/10/25 下午 08:47
 * @since 0.0.1
 */
public class StringUtil {
    public static String encoding(String str) {
        return new String(str.getBytes(), UTF_8);
    }
}
