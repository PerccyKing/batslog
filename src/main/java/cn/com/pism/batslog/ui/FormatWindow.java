package cn.com.pism.batslog.ui;

import cn.com.pism.batslog.util.Editors;
import com.intellij.lang.Language;
import com.intellij.openapi.project.Project;
import com.intellij.openapi.project.ProjectManager;
import com.intellij.ui.LanguageTextField;

import javax.swing.*;
import java.awt.event.*;
import java.util.Objects;

/**
 * @author PerccyKing
 */
public class FormatWindow extends JDialog {
    private JPanel contentPane;
    private JButton buttonOk;
    private JButton buttonCancel;
    private LanguageTextField logConsole;

    private void createUIComponents(){
        Language velocityLanguage = Objects.requireNonNull(Language.findLanguageByID("SQL"));
        Project defaultProject = ProjectManager.getInstance().getDefaultProject();
        logConsole = new LanguageTextField(velocityLanguage,
                defaultProject, "",
                (value, language, project) -> Editors.createSourceEditor(project, velocityLanguage, value, true)
                        .getDocument(), false);
    }

    public FormatWindow() {
        setContentPane(contentPane);
        setModal(true);
        getRootPane().setDefaultButton(buttonOk);

        buttonOk.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                onOk();
            }
        });

        buttonCancel.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                onCancel();
            }
        });

        // call onCancel() when cross is clicked
        setDefaultCloseOperation(DO_NOTHING_ON_CLOSE);
        addWindowListener(new WindowAdapter() {
            @Override
            public void windowClosing(WindowEvent e) {
                onCancel();
            }
        });

        // call onCancel() on ESCAPE
        contentPane.registerKeyboardAction(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                onCancel();
            }
        }, KeyStroke.getKeyStroke(KeyEvent.VK_ESCAPE, 0), JComponent.WHEN_ANCESTOR_OF_FOCUSED_COMPONENT);
    }


    private void onOk() {
        // add your code here
//        dispose();
    }

    private void onCancel() {
        // add your code here if necessary
        dispose();
    }

    /*public static void main(String[] args) {

    }*/
}
