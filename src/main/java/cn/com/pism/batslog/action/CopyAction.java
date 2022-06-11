package cn.com.pism.batslog.action;

import cn.com.pism.batslog.util.BatsLogUtil;
import com.intellij.openapi.actionSystem.AnAction;
import com.intellij.openapi.actionSystem.AnActionEvent;
import com.intellij.openapi.util.NlsActions;
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

    public CopyAction(@Nullable @NlsActions.ActionText String text, @Nullable @NlsActions.ActionDescription String description, @Nullable Icon icon) {
        super(text, description, icon);
    }

    @Override
    public void actionPerformed(@NotNull AnActionEvent e) {
        String selectText = BatsLogUtil.getSelectText(e);
        copyToClipboard(selectText);
    }
}
