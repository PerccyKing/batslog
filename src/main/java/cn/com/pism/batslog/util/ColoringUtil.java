package cn.com.pism.batslog.util;

import cn.com.pism.batslog.settings.BatsLogSetting;
import cn.com.pism.batslog.settings.BatsLogValue;
import com.intellij.execution.ui.ConsoleViewContentType;
import com.intellij.openapi.editor.markup.EffectType;
import com.intellij.openapi.editor.markup.TextAttributes;
import com.intellij.openapi.project.Project;

import java.awt.*;

/**
 * @author PerccyKing
 * @version 0.0.1
 * @date 2020/11/01 上午 10:55
 * @since 0.0.1
 */
public class ColoringUtil {
    public static final String ANSI_PRE = "\u001B[";
    /**
     * 前景色
     */
    public static final Integer FG = 38;
    /**
     * 背景色
     */
    public static final Integer BG = 48;


    public static ConsoleViewContentType getKeyWordConsoleViewContentTypeFromConfig(Project project) {
        BatsLogValue<Color> keyWord = BatsLogSetting.getValue(project, BatsLogSetting.KEYWORDS, Color.class);
        Color value = keyWord.getValue();
        TextAttributes textAttributes = new TextAttributes(value, null, null, EffectType.BOXED, Font.PLAIN);
        return new ConsoleViewContentType(project.getName() + BatsLogSetting.KEYWORDS, textAttributes);
    }
}
