package cn.com.pism.batslog.action;

import cn.com.pism.batslog.settings.BatsLogConfig;
import cn.com.pism.batslog.settings.BatsLogSettingState;
import cn.com.pism.batslog.util.GlobalVar;
import com.intellij.openapi.actionSystem.ActionUpdateThread;
import com.intellij.openapi.actionSystem.AnActionEvent;
import com.intellij.openapi.actionSystem.ToggleAction;
import com.intellij.openapi.project.Project;
import lombok.Getter;
import lombok.Setter;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

import javax.swing.*;

/**
 * @author PerccyKing
 * @version 0.0.1
 * @since 2021/04/25 下午 09:47
 */
@Setter
@Getter
public class BeautyAction extends ToggleAction {

    private Project project;

    @SuppressWarnings("unused")
    public BeautyAction() {
    }

    @SuppressWarnings("unused")
    public BeautyAction(@Nullable String text) {
        super(text);
    }

    public BeautyAction(@Nullable String text, @Nullable String description, @Nullable Icon icon, Project project) {
        super(text, description, icon);
        this.project = project;
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

    private BatsLogConfig getService(@NotNull AnActionEvent e) {
        Project project1 = getProject();
        if (project1 == null) {
            project1 = e.getProject();
        }
        assert project1 != null;
        return BatsLogSettingState.getInstance(project1);
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
        Project project1 = e.getProject();
        assert project1 != null;
        Boolean prettyFormat = getService(e).getPrettyFormat();

        //取消UI选中/反选
        getService(e).setPrettyFormat(!prettyFormat);

        GlobalVar.getPrettyFormat().setSelected(!prettyFormat);
    }

    @Override
    public @NotNull ActionUpdateThread getActionUpdateThread() {
        return super.getActionUpdateThread();
    }
}
