package cn.com.pism.batslog.ui;

import cn.com.pism.batslog.BatsLogBundle;
import cn.com.pism.batslog.util.*;
import com.intellij.execution.impl.ConsoleViewImpl;
import com.intellij.icons.AllIcons;
import com.intellij.openapi.actionSystem.*;
import com.intellij.openapi.command.WriteCommandAction;
import com.intellij.openapi.editor.Document;
import com.intellij.openapi.editor.Editor;
import com.intellij.openapi.fileTypes.PlainTextLanguage;
import com.intellij.openapi.project.Project;
import com.intellij.openapi.project.ProjectManager;
import com.intellij.openapi.ui.DialogWrapper;
import icons.BatsLogIcons;
import lombok.Getter;
import lombok.Setter;
import org.apache.commons.lang.StringUtils;
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
    private JButton format;
    private JButton clearAndFormat;
    private JButton formatAndClear;
    private JButton clearAll;

    private MyConsoleViewImpl consoleView;

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
        initForm(project);
        setOKButtonTooltip(BatsLogBundle.message("printSqlToConsole"));

        initFormAction(project);

        show();
    }

    private void initFormAction(@Nullable Project project) {
        format.addActionListener(e -> format(project, myEditor.getDocument().getText(), true, consoleView));

        clearAndFormat.addActionListener(e -> {
            //清空
            consoleView.clear();
            //格式化
            format(project, myEditor.getDocument().getText(), true, consoleView);
        });

        formatAndClear.addActionListener(e -> {
            //格式化
            format(project, myEditor.getDocument().getText(), true, consoleView);
            //清空
            clearLogEditor(myEditor);
        });

        clearAll.addActionListener(e -> {
            clearLogEditor(myEditor);
            consoleView.clear();
        });
    }

    private void format(@Nullable Project project, String text, boolean b, ConsoleViewImpl consoleView) {
        if (StringUtils.isNotBlank(text)) {
            SqlFormatUtil.format(text, project, b, consoleView);
        }
    }

    private void initForm(@Nullable Project project) {
        //创建一个编辑器
        Project defaultProject = ProjectManager.getInstance().getDefaultProject();
        Editor logEditor = Editors.createSourceEditor(project, PlainTextLanguage.INSTANCE, "", false);
        this.myEditor = logEditor;

        //添加一个工具栏
        List<AnAction> logActions = new ArrayList<>(getLogActions(logEditor));
        ActionToolbar logToolBar = getActionToolBar(ActionPlaces.UNKNOWN, true, logActions.toArray(new AnAction[0]));
        this.logToolBar.add(logToolBar.getComponent());

        //将编辑器加入panel
        logPanel.add(logEditor.getComponent());


        //右边console
        MyConsoleViewImpl consoleView = new MyConsoleViewImpl(defaultProject, true);
        this.consoleView = consoleView;

        //添加操作栏,调用一次getComponent ，editor才会创建
        JComponent component = consoleView.getComponent();
        List<AnAction> consoleActionList = new ArrayList<>();
        consoleActionList.add(new AnAction(BatsLogBundle.message("copyAllSQL"),
                BatsLogBundle.message("copyAllSQL"),
                BatsLogIcons.BATS_LOG_COPY) {
            @Override
            public void actionPerformed(@NotNull AnActionEvent e) {
                String sqls = consoleView.getText();
                if (StringUtils.isNotBlank(sqls)) {
                    BatsLogUtil.copyToClipboard(sqls);
                }
            }
        });
        ActionToolbar sqlConsoleToolBar = consoleView.createActionToolBar(ActionPlaces.UNKNOWN, true, consoleActionList);
        consoleView.installPopupHandler(consoleView.getActionToolbar().getActions());
        consoleBar.add(sqlConsoleToolBar.getComponent());
        sqlConsole.add(component);

        setSize(1000, 800);
        setTitle(StringUtil.encoding("BatsLog"));
        setAutoAdjustable(true);
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
        if (StringUtils.isNotBlank(text)) {
            format(project, text, Boolean.TRUE, BatsLogUtil.CONSOLE_VIEW_MAP.get(this.project));
        }
        super.doOKAction();
    }


    /**
     * <p>
     * 生成一个ActionToolBar
     * </p>
     *
     * @param places     : 位置 {@link ActionPlaces}
     * @param horizontal : 是否水平
     * @param anActions  :
     * @return {@link ActionToolbar} ActionToolBar
     * @author PerccyKing
     * @date 2020/12/12 下午 08:36
     */
    public ActionToolbar getActionToolBar(String places, boolean horizontal, AnAction[] anActions) {
        DefaultActionGroup actions = new DefaultActionGroup();
        ActionToolbar actionToolbar = ActionManager.getInstance().createActionToolbar(places, actions, horizontal);
        for (AnAction action : anActions) {
            actions.add(action);
        }

        return actionToolbar;
    }

    /**
     * <p>
     * 生成日志输入框的操作事件
     * </p>
     *
     * @param editor : 编辑器
     * @return {@link List<AnAction>} 事件列表
     * @author PerccyKing
     * @date 2020/12/12 下午 08:38
     */
    public List<AnAction> getLogActions(Editor editor) {
        List<AnAction> logActions = new ArrayList<>();
        AnAction clear = new AnAction(BatsLogBundle.message("clear"), BatsLogBundle.message("clearEditor"), AllIcons.Actions.GC) {
            @Override
            public void actionPerformed(@NotNull AnActionEvent e) {
                clearLogEditor(editor);
            }
        };
        AnAction copySqlToClipboard = new AnAction(BatsLogBundle.message("copySQLToClipboard"),
                BatsLogBundle.message("copySQLToClipboard"), BatsLogIcons.BATS_LOG_COPY) {
            @Override
            public void actionPerformed(@NotNull AnActionEvent e) {
                BatsLogUtil.copySqlToClipboard(e, editor.getDocument().getText());
            }
        };

        logActions.add(copySqlToClipboard);
        logActions.add(clear);

        return logActions;
    }

    private void clearLogEditor(Editor editor) {
        Document document = editor.getDocument();
        int length = document.getText().length();
        WriteCommandAction.runWriteCommandAction(project, () ->
                document.replaceString(0, length, "")
        );
    }
}
