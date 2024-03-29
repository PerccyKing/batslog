package cn.com.pism.batslog.util;

import com.intellij.lang.Language;
import com.intellij.openapi.editor.Editor;
import com.intellij.openapi.editor.EditorFactory;
import com.intellij.openapi.editor.EditorSettings;
import com.intellij.openapi.project.Project;

import java.util.Objects;

/**
 * @author PerccyKing
 */
public class Editors {

    private Editors() {
    }

    public static Editor createSourceEditor(Project project, Language language, String content, boolean readOnly) {
        final EditorFactory factory = EditorFactory.getInstance();
        final Editor editor = factory.createEditor(factory.createDocument(content), project,
                Objects.requireNonNull(language.getAssociatedFileType()), readOnly);
        EditorSettings settings = editor.getSettings();
        settings.setLineNumbersShown(false);
        settings.setRefrainFromScrolling(false);
        settings.setLineMarkerAreaShown(false);
        return editor;
    }

}