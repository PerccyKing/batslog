package cn.com.pism.batslog.points;

import com.intellij.codeInsight.editorActions.enter.EnterHandlerDelegate;
import com.intellij.execution.ConsoleFolding;
import com.intellij.execution.actions.ConsoleActionsPostProcessor;
import com.intellij.execution.ui.ConsoleView;
import com.intellij.openapi.actionSystem.AnAction;
import com.intellij.openapi.actionSystem.DataContext;
import com.intellij.openapi.editor.Editor;
import com.intellij.openapi.editor.actionSystem.EditorActionHandler;
import com.intellij.openapi.project.Project;
import com.intellij.openapi.util.Ref;
import com.intellij.psi.PsiDocumentManager;
import com.intellij.psi.PsiFile;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

import java.util.List;

/**
 * @author PerccyKing
 * @version 0.0.1
 * @date 2020/10/24 下午 09:06
 * @since 0.0.1
 */
public class BatsLogConsoleFolding extends ConsoleFolding {
    /**
     * @param project current project
     * @param line    line to check whether it should be folded or not
     * @return {@code true} if line should be folded, {@code false} if not
     */
    @Override
    public boolean shouldFoldLine(@NotNull Project project, @NotNull String line) {
        System.out.println(line);
        return super.shouldFoldLine(project, line);
    }
}
