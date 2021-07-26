package cn.com.pism.batslog.action;

import cn.com.pism.batslog.constants.BatsLogConstant;
import cn.com.pism.batslog.settings.BatsLogSettingState;
import cn.com.pism.batslog.util.BatsLogUtil;
import com.intellij.execution.impl.ConsoleViewImpl;
import com.intellij.execution.ui.ConsoleViewContentType;
import com.intellij.openapi.actionSystem.AnActionEvent;
import com.intellij.openapi.actionSystem.ToggleAction;
import com.intellij.openapi.components.ServiceManager;
import com.intellij.openapi.project.Project;
import org.apache.commons.lang3.StringUtils;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

import javax.swing.*;
import java.util.Objects;

/**
 * @author PerccyKing
 * @version 0.0.1
 * @date 2021/04/25 下午 09:47
 * @since 0.0.1
 */
public class BeautyAction extends ToggleAction {

    public BeautyAction() {
    }

    public BeautyAction(@Nullable String text) {
        super(text);
    }

    public BeautyAction(@Nullable String text, @Nullable String description, @Nullable Icon icon) {
        super(text, description, icon);
    }

    /**
     * Returns the selected (checked, pressed) state of the action.
     *
     * @param e the action event representing the place and context in which the selected state is queried.
     * @return true if the action is selected, false otherwise
     */
    @Override
    public boolean isSelected(@NotNull AnActionEvent e) {
        return Boolean.TRUE.equals(getService(e).getPrettyFormat());
    }

    private BatsLogSettingState getService(@NotNull AnActionEvent e) {
        return ServiceManager.getService(Objects.requireNonNull(e.getProject()), BatsLogSettingState.class);
    }

    /**
     * Sets the selected state of the action to the specified value.
     *
     * @param e     the action event which caused the state change.
     * @param state the new selected state of the action.
     */
    @Override
    public void setSelected(@NotNull AnActionEvent e, boolean state) {
        reFormat(e);
    }

    private void reFormat(AnActionEvent e) {
        Project project = e.getProject();
        assert project != null;
        Boolean prettyFormat = getService(e).getPrettyFormat();

        ConsoleViewImpl consoleView = BatsLogUtil.CONSOLE_VIEW_MAP.get(project);
        String text = consoleView.getText();
        //清空console
//        consoleView.clear();

        //分析出 sql，分隔符，name，other（LOGO）
//        parseText(text, consoleView);

        //取消UI选中/反选
        getService(e).setPrettyFormat(!prettyFormat);

        BatsLogUtil.PRETTY_FORMAT.setSelected(!prettyFormat);
    }

    private void parseText(String text, ConsoleViewImpl consoleView) {
        //先判断text是否为空 不为空才会进行分析操作
        if (StringUtils.isNotBlank(text)) {
            String[] split = text.split("\n");
            for (String s : split) {
                if (BatsLogConstant.SEPARATOR.equals(s)) {
                    consoleView.print(s, ConsoleViewContentType.ERROR_OUTPUT);
                }
            }
        }

    }
}
