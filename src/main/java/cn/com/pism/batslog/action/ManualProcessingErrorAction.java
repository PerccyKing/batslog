package cn.com.pism.batslog.action;

import cn.com.pism.batslog.model.BslErrorMod;
import cn.com.pism.batslog.ui.ErrorProcessDialog;
import cn.com.pism.batslog.util.BatsLogUtil;
import cn.com.pism.batslog.util.SqlFormatUtil;
import com.intellij.openapi.actionSystem.AnAction;
import com.intellij.openapi.actionSystem.AnActionEvent;
import org.jetbrains.annotations.NotNull;

/**
 * 手动处理事件
 * @author PerccyKing
 * @version 0.0.1
 * @since 2021/12/19 下午 08:16
 */
public class ManualProcessingErrorAction extends AnAction {
    @Override
    public void actionPerformed(@NotNull AnActionEvent e) {
        String selectedText = BatsLogUtil.getSelectText(e);
        SqlFormatUtil.manualFormat(selectedText, e.getProject(), (sql, params) -> {
            BslErrorMod bslErrorMod = new BslErrorMod();
            bslErrorMod.setErrorMsg("");
            bslErrorMod.setParams(params);
            bslErrorMod.setSql(sql);
            bslErrorMod.setTime("");
            new ErrorProcessDialog(e.getProject(), bslErrorMod);
        });

    }
}
