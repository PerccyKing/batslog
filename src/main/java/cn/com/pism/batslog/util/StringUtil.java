package cn.com.pism.batslog.util;

import java.nio.charset.Charset;
import java.nio.charset.StandardCharsets;

/**
 * @author PerccyKing
 * @version 0.0.1
 * @date 2020/10/25 下午 08:47
 * @since 0.0.1
 */
public class StringUtil {

    private StringUtil() {
    }

    public static final String GBK_STR = "GBK";
    public static String encoding(String str) {
        if (Charset.isSupported(GBK_STR)){
            return new String(str.getBytes(), Charset.forName("GBK"));
        }
        return new String(str.getBytes(), StandardCharsets.UTF_8);
    }
}
