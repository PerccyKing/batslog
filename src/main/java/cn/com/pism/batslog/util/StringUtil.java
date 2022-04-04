package cn.com.pism.batslog.util;

import org.apache.commons.lang3.StringUtils;

import java.io.UnsupportedEncodingException;
import java.lang.reflect.Field;
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
        String encoding = getEncoding(str);
        if (StringUtils.isBlank(str)) {
            return str;
        }
        return new String(str.getBytes(Charset.forName(encoding)), Charset.defaultCharset());
    }

    public static boolean isEncoding(String str, String encode) {
        try {
            if (str.equals(new String(str.getBytes(encode), encode))) {
                return true;
            }
        } catch (UnsupportedEncodingException e) {
            e.printStackTrace();
        }
        return false;
    }

    public static String getEncoding(String str) {
        Field[] declaredFields = StandardCharsets.class.getDeclaredFields();
        for (Field declaredField : declaredFields) {
            declaredField.setAccessible(true);
            try {
                String name = declaredField.get("name").toString();
                if (isEncoding(str, name)) {
                    return name;
                }
            } catch (IllegalAccessException e) {
                e.printStackTrace();
            }
        }

        if (Charset.isSupported(GBK_STR) && isEncoding(str, GBK_STR)) {
            return GBK_STR;
        }
        return "";
    }
}
