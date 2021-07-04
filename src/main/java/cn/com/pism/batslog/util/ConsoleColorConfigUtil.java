package cn.com.pism.batslog.util;

import cn.com.pism.batslog.model.ConsoleColorConfig;
import com.intellij.execution.ui.ConsoleViewContentType;
import com.intellij.openapi.editor.markup.EffectType;
import com.intellij.openapi.editor.markup.TextAttributes;
import com.intellij.openapi.project.Project;
import org.apache.commons.lang3.StringUtils;

import java.awt.*;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

/**
 * @author PerccyKing
 * @version 0.0.1
 * @date 2021/07/04 下午 04:16
 * @since 0.0.1
 */
public class ConsoleColorConfigUtil {
    private ConsoleColorConfigUtil() {
    }

    public static Map<String, ConsoleColorConfig> toMap(List<ConsoleColorConfig> colorConfigs) {
        return colorConfigs.stream().collect(Collectors.toMap(ConsoleColorConfig::getKeyWord, v -> v));
    }

    public static Map<String, ConsoleViewContentType> toConsoleViewContentTypeMap(Project project, List<ConsoleColorConfig> colorConfigs) {
        Map<String, ConsoleViewContentType> map = new HashMap<>(0);
        for (ConsoleColorConfig colorConfig : colorConfigs) {
            TextAttributes textAttributes = new TextAttributes(BatsLogUtil.toColor(colorConfig.getForegroundColor()), BatsLogUtil.toColor(colorConfig.getBackgroundColor()), null, EffectType.BOXED, Font.PLAIN);
            if (StringUtils.isNotBlank(colorConfig.getKeyWord()) && colorConfig.isEnabled()) {
                map.put(colorConfig.getKeyWord(), new ConsoleViewContentType(project.getName() + "ConsoleColor" + colorConfig.getKeyWord(), textAttributes));
            }
        }
        return map;
    }
}
