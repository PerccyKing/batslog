package cn.com.pism.batslog.ui;

import com.intellij.openapi.project.Project;
import com.intellij.openapi.ui.DialogWrapper;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

import java.awt.*;

/**
 * @author PerccyKing
 * @version 0.0.1
 * @date 2020/12/08 上午 12:31
 * @since 0.0.1
 */
public class FormatWindowDialog extends DialogWrapper {

    /**
     * @param parent      parent component which is used to calculate heavy weight window ancestor.
     *                    {@code parent} cannot be {@code null} and must be showing.
     * @param canBeParent can be parent
     * @throws IllegalStateException if the dialog is invoked not on the event dispatch thread
     */
    protected FormatWindowDialog(@NotNull Component parent, boolean canBeParent) {
        super(parent, canBeParent);
    }
}
