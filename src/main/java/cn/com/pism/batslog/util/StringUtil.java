package cn.com.pism.batslog.util;

import cn.com.pism.batslog.constants.BatsLogConstant;
import cn.com.pism.batslog.settings.BatsLogSettingState;
import com.intellij.openapi.project.Project;
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

    public static String encoding(String str, Project project) {
        BatsLogSettingState state = BatsLogSettingState.getInstance(project);
        String encoding = state.getEncoding();
        if (StringUtils.isNotBlank(encoding) && !BatsLogConstant.DEFAULT_ENCODING.equals(encoding)
                && Charset.isSupported(encoding)) {
            return new String(str.getBytes(), Charset.forName(encoding));
        }
        return str;
    }

}
