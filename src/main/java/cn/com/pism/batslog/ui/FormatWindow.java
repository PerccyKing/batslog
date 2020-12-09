package cn.com.pism.batslog.ui;

import cn.com.pism.batslog.util.SqlFormatUtils;
import cn.com.pism.batslog.util.StringUtil;
import com.intellij.execution.impl.ConsoleViewImpl;
import com.intellij.openapi.actionSystem.*;
import com.intellij.openapi.project.Project;
import com.intellij.openapi.project.ProjectManager;
import com.intellij.openapi.ui.DialogWrapper;
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
    private JTextArea logArea;
    private JPanel sqlConsole;
    private JPanel consoleBar;

    private ConsoleViewImpl consoleView;

    private Project project;

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
        this.project = project;
        Project defaultProject = ProjectManager.getInstance().getDefaultProject();
        ConsoleViewImpl consoleView = new ConsoleViewImpl(defaultProject, true);
        this.consoleView = consoleView;

        //添加操作栏
        DefaultActionGroup actions = new DefaultActionGroup();
        ActionToolbar actionToolbar = ActionManager.getInstance().createActionToolbar(ActionPlaces.UNKNOWN, actions, true);
        consoleBar.add(actionToolbar.getComponent());
        sqlConsole.add(consoleView.getComponent());
        AnAction[] consoleActions = consoleView.createConsoleActions();
        for (AnAction action : consoleActions) {
            actions.add(action);
        }

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

    /**
     * This method is invoked by default implementation of "OK" action. It just closes dialog
     * with {@code OK_EXIT_CODE}. This is convenient place to override functionality of "OK" action.
     * Note that the method does nothing if "OK" action isn't enabled.
     */
    @Override
    protected void doOKAction() {
        String text = this.logArea.getText();
        SqlFormatUtils.format(text, project, Boolean.TRUE, this.consoleView);
    }

}
