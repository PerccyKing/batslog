package cn.com.pism.batslog.ui;

import cn.com.pism.batslog.util.StringUtil;
import com.intellij.execution.impl.ConsoleViewImpl;
import com.intellij.openapi.project.Project;
import com.intellij.openapi.ui.DialogWrapper;
import com.intellij.ui.components.JBTextArea;
import com.intellij.ui.components.OnOffButton;
import lombok.Getter;
import lombok.Setter;
import org.jetbrains.annotations.Nullable;

import javax.swing.*;

/**
 * @author PerccyKing
 * @version 0.0.1
 * @date 2020/12/07 下午 10:29
 * @since 0.0.1
 */
@Getter
@Setter
public class FormatWindow extends DialogWrapper {
    private JPanel root;
    private JBTextArea textArea1;
    private ConsoleViewImpl textArea2;

    /**
     * Creates modal {@code DialogWrapper} that can be parent for other windows.
     * The currently active window will be the dialog's parent.
     *
     * @param project parent window for the dialog will be calculated based on focused window for the
     *                specified {@code project}. This parameter can be {@code null}. In this case parent window
     *                will be suggested based on current focused window.
     * @throws IllegalStateException if the dialog is invoked not on the event dispatch thread
     * @see DialogWrapper#DialogWrapper(Project, boolean)
     */
    protected FormatWindow(@Nullable Project project) {
        super(project);
        init();
        setSize(800, 500);
        setTitle(StringUtil.encoding("BatsLog"));
        show();
    }

    /**
     * Factory method. It creates panel with dialog options. Options panel is located at the
     * center of the dialog's content pane. The implementation can return {@code null}
     * value. In this case there will be no options panel.
     */
    @Override
    protected @Nullable JComponent createCenterPanel() {
        return root;
    }

    public static void show(Project project) {
        new FormatWindow(project);
    }

}
