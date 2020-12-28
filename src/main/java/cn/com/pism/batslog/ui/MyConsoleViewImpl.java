package cn.com.pism.batslog.ui;

import com.intellij.execution.impl.ConsoleViewImpl;
import com.intellij.openapi.actionSystem.*;
import com.intellij.openapi.editor.event.EditorMouseEvent;
import com.intellij.openapi.editor.ex.EditorEx;
import com.intellij.openapi.editor.impl.ContextMenuPopupHandler;
import com.intellij.openapi.project.Project;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

/**
 * @author wangyihuai
 * @date 2020/12/24 10:08
 */
public class MyConsoleViewImpl extends ConsoleViewImpl {

    ActionToolbar actionToolbar;

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
     * @date 2020/12/28 10:44
     */
    public void installPopupHandler(AnAction[] anActions) {
        installPopupHandler(Arrays.asList(anActions));
    }

    /**
     * <p>
     * 更新邮件菜单
     * </p>
     *
     * @param anActions : 新的事件列表
     * @author wangyihuai
     * @date 2020/12/28 10:44
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
        AnAction[] consoleActions = createConsoleActions();
        //只保留console自身的事件
        for (AnAction action : consoleActions) {
            boolean transparentUpdate = action.isTransparentUpdate();
            anActions.add(action);
        }
        DefaultActionGroup actions = new DefaultActionGroup();
        ActionToolbar actionToolbar = ActionManager.getInstance().createActionToolbar(places, actions, horizontal);
        for (AnAction action : anActions) {
            actions.add(action);
        }
        setActionToolbar(actionToolbar);
        return actionToolbar;
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
    public ActionToolbar createActionToolBar(String places, boolean horizontal, AnAction[] anActions) {
        return createActionToolBar(places, horizontal, new ArrayList<>(Arrays.asList(anActions)));
    }


    public ActionToolbar getActionToolbar() {
        return actionToolbar;
    }

    public void setActionToolbar(ActionToolbar actionToolbar) {
        this.actionToolbar = actionToolbar;
    }
}
