package cn.com.pism.batslog.util;

import cn.com.pism.batslog.settings.BatsLogSettingState;
import com.intellij.execution.ui.ConsoleViewContentType;
import com.intellij.openapi.components.ServiceManager;
import com.intellij.openapi.editor.markup.EffectType;
import com.intellij.openapi.editor.markup.TextAttributes;
import com.intellij.openapi.project.Project;
import com.intellij.ui.Gray;
import com.intellij.ui.JBColor;

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
        BatsLogSettingState service = ServiceManager.getService(project, BatsLogSettingState.class);
        Color value = BatsLogUtil.toColor(service.getKeyWordDefCol());
        TextAttributes textAttributes = new TextAttributes(value, null, null, EffectType.BOXED, Font.PLAIN);
        return new ConsoleViewContentType(project.getName() + "BatsLogKeywords", textAttributes);
    }

    public static ConsoleViewContentType getNoteColor(Project project) {
        Color value = new JBColor(Gray._140, Gray._128);
        TextAttributes textAttributes = new TextAttributes(value, null, null, EffectType.BOXED, Font.PLAIN);
        return new ConsoleViewContentType(project.getName() + "BatsLogNotes", textAttributes);
    }
}
