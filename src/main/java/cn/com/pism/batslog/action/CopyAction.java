package cn.com.pism.batslog.action;

import cn.com.pism.batslog.util.BatsLogUtil;
import com.intellij.openapi.actionSystem.AnAction;
import com.intellij.openapi.actionSystem.AnActionEvent;
import org.jetbrains.annotations.Nls;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

import javax.swing.*;

import static cn.com.pism.batslog.util.BatsLogUtil.copyToClipboard;

/**
 * 复制
 *
 * @author PerccyKing
 * @date 2022/06/11 下午 01:01
 */
public class CopyAction extends AnAction {


    /**
     * Constructs a new action with the specified text, description and icon.
     *
     * @param text        Serves as a tooltip when the presentation is a button and the name of the
     *                    menu item when the presentation is a menu item
     * @param description Describes current action, this description will appear on
     *                    the status bar when presentation has focus
     * @param icon        Action's icon
     */
    public CopyAction(@Nls(capitalization = Nls.Capitalization.Title) @Nullable String text, @Nls(capitalization = Nls.Capitalization.Sentence) @Nullable String description, @Nullable Icon icon) {
        super(text, description, icon);
    }

    @Override
    public void actionPerformed(@NotNull AnActionEvent e) {
        String selectText = BatsLogUtil.getSelectText(e);
        copyToClipboard(selectText);
    }
}
