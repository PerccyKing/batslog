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

/**
 * @author PerccyKing
 * @version 0.0.1
 * @since 2021/07/04 下午 04:16
 */
public class ConsoleColorConfigUtil {
    private ConsoleColorConfigUtil() {
    }

    public static Map<String, ConsoleViewContentType> toConsoleViewContentTypeMap(Project project, List<ConsoleColorConfig> colorConfigs) {
        Map<String, ConsoleViewContentType> map = new HashMap<>(0);
        ConsoleViewContentType normalOutput = ConsoleViewContentType.NORMAL_OUTPUT;
        for (ConsoleColorConfig colorConfig : colorConfigs) {
            TextAttributes textAttributes = new TextAttributes(
                    colorConfig.isEnabledFgColor() ? BatsLogUtil.toColor(colorConfig.getForegroundColor()) : normalOutput.getAttributes().getForegroundColor(),
                    colorConfig.isEnableBgColor() ? BatsLogUtil.toColor(colorConfig.getBackgroundColor()) : normalOutput.getAttributes().getBackgroundColor(),
                    null, EffectType.BOXED, Font.PLAIN);
            if (StringUtils.isNotBlank(colorConfig.getKeyWord()) && colorConfig.isEnabled()) {
                String baseName = project == null ? "" : project.getName();
                map.put(colorConfig.getKeyWord(), new ConsoleViewContentType(baseName + "ConsoleColor" + colorConfig.getKeyWord(), textAttributes));
            }
        }
        return map;
    }
}
