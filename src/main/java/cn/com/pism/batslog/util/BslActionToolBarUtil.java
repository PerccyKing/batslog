package cn.com.pism.batslog.util;

import com.intellij.openapi.actionSystem.ActionManager;
import com.intellij.openapi.actionSystem.ActionToolbar;
import com.intellij.openapi.actionSystem.DefaultActionGroup;

/**
 * @author PerccyKing
 * @version 0.0.1
 * @since 2021/10/15 上午 10:43
 */
public class BslActionToolBarUtil {
    private BslActionToolBarUtil() {
    }

    /**
     * <p>
     * 创建一个action工具栏
     * </p>
     *
     * @param places     : 位置 {@link com.intellij.openapi.actionSystem.ActionPlaces}
     * @param horizontal : 是否水平放置
     * @param actions    : action组
     * @return {@link ActionToolbar}
     * @author PerccyKing
     * @since 2021/10/15 上午 10:50
     */
    public static ActionToolbar createActionToolBar(String places, boolean horizontal, DefaultActionGroup actions) {
        ActionManager actionManager = ActionManager.getInstance();
        return actionManager.createActionToolbar(places, actions, horizontal);
    }
}
