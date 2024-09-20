package cn.com.pism.batslog.ui;

import cn.com.pism.batslog.util.GlobalVar;
import com.intellij.execution.ExecutionBundle;
import com.intellij.execution.impl.ConsoleViewImpl;
import com.intellij.execution.ui.ConsoleView;
import com.intellij.icons.AllIcons;
import com.intellij.ide.CommonActionsManager;
import com.intellij.openapi.actionSystem.*;
import com.intellij.openapi.editor.Editor;
import com.intellij.openapi.editor.actions.ScrollToTheEndToolbarAction;
import com.intellij.openapi.editor.actions.ToggleUseSoftWrapsToolbarAction;
import com.intellij.openapi.editor.event.EditorMouseEvent;
import com.intellij.openapi.editor.ex.EditorEx;
import com.intellij.openapi.editor.impl.ContextMenuPopupHandler;
import com.intellij.openapi.editor.impl.softwrap.SoftWrapAppliancePlaces;
import com.intellij.openapi.project.DumbAwareAction;
import com.intellij.openapi.project.Project;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

/**
 * @author wangyihuai
 * @since 2020/12/24 10:08
 */
public class MyConsoleViewImpl extends ConsoleViewImpl {

    transient ActionToolbar actionToolbar;

    public MyConsoleViewImpl(@NotNull Project project, boolean viewer) {
        super(project, viewer);
        setBorder(null);
    }


    /**
     * <p>
     * 更新邮件菜单
     * </p>
     *
     * @param anActions : 新的事件列表
     * @author wangyihuai
     * @since 2020/12/28 10:44
     */
    public void installPopupHandler(List<AnAction> anActions) {
        installPopupHandler(new ContextMenuPopupHandler() {
            @Override
            public @NotNull ActionGroup getActionGroup(@NotNull EditorMouseEvent event) {
                return new ActionGroup() {
                    @NotNull
                    @Override
                    public AnAction[] getChildren(@Nullable AnActionEvent e) {
                        return anActions.toArray(new AnAction[0]);
                    }
                };
            }
        });
    }

    public void installPopupHandler(ContextMenuPopupHandler handler) {
        EditorEx editor = (EditorEx) getEditor();
        editor.installPopupHandler(handler);
    }

    public ActionToolbar createActionToolBar(String places, boolean horizontal, List<AnAction> anActions) {
        AnAction[] consoleActions = createConsoleActionArr();
        ActionManager actionManager = ActionManager.getInstance();
        //只保留console自身的事件
        anActions.addAll(Arrays.asList(consoleActions));
        DefaultActionGroup actions = new DefaultActionGroup();
        ActionToolbar newActionToolbar = actionManager.createActionToolbar(places, actions, horizontal);
        for (AnAction action : anActions) {
            actions.add(action);
        }
        setActionToolbar(newActionToolbar);
        return newActionToolbar;
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
     * @since 2020/12/12 下午 08:36
     */
    public ActionToolbar createActionToolBar(String places, boolean horizontal, AnAction[] anActions) {
        return createActionToolBar(places, horizontal, new ArrayList<>(Arrays.asList(anActions)));
    }


    public ActionToolbar getActionToolbar() {
        return actionToolbar;
    }

    public void setActionToolbar(ActionToolbar actionToolbar) {
        this.actionToolbar = actionToolbar;
    }

    public AnAction[] createConsoleActionArr() {
        //Initializing prev and next occurrences actions
        final CommonActionsManager actionsManager = CommonActionsManager.getInstance();
        final AnAction prevAction = actionsManager.createPrevOccurenceAction(this);
        prevAction.getTemplatePresentation().setText(getPreviousOccurenceActionName());
        final AnAction nextAction = actionsManager.createNextOccurenceAction(this);
        nextAction.getTemplatePresentation().setText(getNextOccurenceActionName());

        Editor editor = getEditor();
        final AnAction switchSoftWrapsAction = new ToggleUseSoftWrapsToolbarAction(SoftWrapAppliancePlaces.CONSOLE) {
            @Override
            protected Editor getEditor(@NotNull AnActionEvent e) {
                return editor;
            }
        };
        final AnAction autoScrollToTheEndAction = new ScrollToTheEndToolbarAction(editor);

        List<AnAction> consoleActions = new ArrayList<>();
        consoleActions.add(prevAction);
        consoleActions.add(nextAction);
        consoleActions.add(switchSoftWrapsAction);
        consoleActions.add(autoScrollToTheEndAction);
        consoleActions.add(ActionManager.getInstance().getAction("Print"));
        consoleActions.add(new ClearThisConsoleViewAction(this));
        return consoleActions.toArray(AnAction.EMPTY_ARRAY);
    }


    private static class ClearThisConsoleViewAction extends ClearAllAction {
        private final ConsoleView myConsoleView;

        ClearThisConsoleViewAction(@NotNull ConsoleView consoleView) {
            myConsoleView = consoleView;
        }

        @Override
        public void update(@NotNull AnActionEvent e) {
            boolean enabled = myConsoleView.getContentSize() > 0;
            e.getPresentation().setEnabled(enabled);
        }

        @Override
        public void actionPerformed(@NotNull final AnActionEvent e) {
            GlobalVar.setSqlNumber(0);
            myConsoleView.clear();
        }

        @Override
        public @NotNull ActionUpdateThread getActionUpdateThread() {
            return ActionUpdateThread.EDT;
        }
    }

    public static class ClearAllAction extends DumbAwareAction {
        public ClearAllAction() {
            super(ExecutionBundle.message("clear.all.from.console.action.name"), "Clear the contents of the console", AllIcons.Actions.GC);
        }

        @Override
        public void update(@NotNull AnActionEvent e) {
            ConsoleView data = e.getData(LangDataKeys.CONSOLE_VIEW);
            boolean enabled = data != null && data.getContentSize() > 0;
            e.getPresentation().setEnabled(enabled);
        }

        @Override
        public void actionPerformed(@NotNull final AnActionEvent e) {
            final ConsoleView consoleView = e.getData(LangDataKeys.CONSOLE_VIEW);
            if (consoleView != null) {
                consoleView.clear();
            }
        }
    }
}
