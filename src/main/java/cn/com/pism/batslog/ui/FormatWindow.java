package cn.com.pism.batslog.ui;

import cn.com.pism.batslog.util.BatsLogUtil;
import cn.com.pism.batslog.util.Editors;
import cn.com.pism.batslog.util.SqlFormatUtils;
import cn.com.pism.batslog.util.StringUtil;
import com.intellij.execution.impl.ConsoleViewImpl;
import com.intellij.icons.AllIcons;
import com.intellij.lang.Language;
import com.intellij.openapi.actionSystem.*;
import com.intellij.openapi.command.WriteCommandAction;
import com.intellij.openapi.editor.Document;
import com.intellij.openapi.editor.Editor;
import com.intellij.openapi.project.Project;
import com.intellij.openapi.project.ProjectManager;
import com.intellij.openapi.ui.DialogWrapper;
import icons.BatsLogIcons;
import lombok.Getter;
import lombok.Setter;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

import javax.swing.*;
import java.util.ArrayList;
import java.util.List;

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
    private JPanel logPanel;
    private JPanel sqlConsole;
    private JPanel consoleBar;
    /**
     * 日志工具栏
     */
    private JPanel logToolBar;

    private ConsoleViewImpl consoleView;

    private Editor myEditor;

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
        //创建一个编辑器
        Project defaultProject = ProjectManager.getInstance().getDefaultProject();
        Editor logEditor = Editors.createSourceEditor(project, Language.findLanguageByID("TEXT"), "", false);
        this.myEditor = logEditor;

        //添加一个工具栏
        List<AnAction> logActions = new ArrayList<>(getLogActions(logEditor));
        ActionToolbar logToolBar = getActionToolBar(ActionPlaces.UNKNOWN, true, logActions.toArray(new AnAction[0]));
        this.logToolBar.add(logToolBar.getComponent());

        //将编辑器加入panel
        logPanel.add(logEditor.getComponent());


        //右边console
        ConsoleViewImpl consoleView = new ConsoleViewImpl(defaultProject, true);
        this.consoleView = consoleView;

        //添加操作栏,调用一次getComponent ，editor才会创建
        JComponent component = consoleView.getComponent();
        AnAction[] consoleActions = consoleView.createConsoleActions();
        ActionToolbar sqlConsoleToolBar = getActionToolBar(ActionPlaces.UNKNOWN, true, consoleActions);
        consoleBar.add(sqlConsoleToolBar.getComponent());
        sqlConsole.add(component);

        setSize(1000, 800);
        setTitle(StringUtil.encoding("BatsLog"));
        setAutoAdjustable(true);
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
        String text = this.myEditor.getDocument().getText();
        SqlFormatUtils.format(text, project, Boolean.TRUE, this.consoleView);
    }


    public ActionToolbar getActionToolBar(String places, boolean horizontal, AnAction[] anActions) {
        DefaultActionGroup actions = new DefaultActionGroup();
        ActionToolbar actionToolbar = ActionManager.getInstance().createActionToolbar(places, actions, horizontal);
        for (AnAction action : anActions) {
            actions.add(action);
        }

        return actionToolbar;
    }

    public List<AnAction> getLogActions(Editor editor) {
        List<AnAction> logActions = new ArrayList<>();
        AnAction clear = new AnAction("清空", "", AllIcons.Actions.GC) {
            @Override
            public void actionPerformed(@NotNull AnActionEvent e) {
                Document document = editor.getDocument();
                int length = document.getText().length();
                WriteCommandAction.runWriteCommandAction(project, () ->
                        document.replaceString(0, length, "")
                );
            }
        };
        AnAction copySqlToClipboard = new AnAction("复制SQL到剪贴板", "", BatsLogIcons.BATS_LOG_COPY) {
            @Override
            public void actionPerformed(@NotNull AnActionEvent e) {
                BatsLogUtil.copySqlToClipboard(e, editor.getDocument().getText());
            }
        };

        logActions.add(copySqlToClipboard);
        logActions.add(clear);

        return logActions;
    }
}
