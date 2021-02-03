package cn.com.pism.batslog.factory;

import cn.com.pism.batslog.BatsLogBundle;
import cn.com.pism.batslog.action.CopySqlAction;
import cn.com.pism.batslog.action.FormatSqlAction;
import cn.com.pism.batslog.ui.FormatConsole;
import cn.com.pism.batslog.ui.SettingForm;
import cn.com.pism.batslog.util.BatsLogUtil;
import com.intellij.database.autoconfig.DataSourceConfigUtil;
import com.intellij.database.console.JdbcConsole;
import com.intellij.database.console.RunSqlScriptAction;
import com.intellij.database.dataSource.DataSourceUiUtil;
import com.intellij.database.datagrid.DataGridUtil;
import com.intellij.database.editor.DatabaseEditorHelper;
import com.intellij.database.editor.DatabaseTableFileEditor;
import com.intellij.database.editor.SqlInsertsTableFileEditor;
import com.intellij.database.model.DasDataSource;
import com.intellij.database.psi.DataSourceManager;
import com.intellij.database.psi.DbDataSource;
import com.intellij.database.psi.DbDataSourceImpl;
import com.intellij.database.script.generator.ui.ScriptGeneratorTool;
import com.intellij.database.script.generator.ui.ScriptGeneratorUI;
import com.intellij.database.view.editors.DatabaseEditorUtil;
import com.intellij.database.view.ui.SqlPreviewPanel;
import com.intellij.icons.AllIcons;
import com.intellij.ide.BrowserUtil;
import com.intellij.openapi.actionSystem.ActionManager;
import com.intellij.openapi.actionSystem.AnAction;
import com.intellij.openapi.actionSystem.AnActionEvent;
import com.intellij.openapi.fileEditor.FileDocumentManager;
import com.intellij.openapi.project.Project;
import com.intellij.openapi.wm.ToolWindow;
import com.intellij.openapi.wm.ToolWindowFactory;
import com.intellij.openapi.wm.ex.ToolWindowEx;
import com.intellij.sql.database.SqlDataSource;
import com.intellij.sql.database.SqlDataSourceImpl;
import com.intellij.sql.database.SqlDataSourceManager;
import com.intellij.sql.dialects.SqlResolveEditor;
import com.intellij.sql.psi.impl.SqlFileImpl;
import com.intellij.sql.psi.impl.SqlUseDatabaseStatementImpl;
import com.intellij.ui.content.Content;
import com.intellij.ui.content.ContentFactory;
import com.intellij.ui.content.ContentManager;
import icons.BatsLogIcons;
import org.jetbrains.annotations.NotNull;

import java.util.List;


/**
 * @author PerccyKing
 * @version 0.0.1
 * @date 2020/10/25 下午 12:18
 * @since 0.0.1
 */
public class BatsLogWindowFactory implements ToolWindowFactory {
    @Override
    public void createToolWindowContent(@NotNull Project project, @NotNull ToolWindow toolWindow) {

        //项目默认监听状态
        BatsLogUtil.TAIL_STATUS.put(project, Boolean.FALSE);

        BatsLogUtil.TOOL_WINDOW = (ToolWindowEx) toolWindow;
        FormatConsole formatConsole = new FormatConsole(project);
        SettingForm settingForm = new SettingForm(project);
        ContentFactory contentFactory = ContentFactory.SERVICE.getInstance();
        Content formatConsoleContent = contentFactory.createContent(formatConsole.getRoot(), BatsLogBundle.message("console"), false);
        Content settingFormContent = contentFactory.createContent(settingForm.getRoot(), BatsLogBundle.message("consoleSetting"), false);
        ContentManager contentManager = toolWindow.getContentManager();
        ((ToolWindowEx) toolWindow).setTabActions(new AnAction(AllIcons.Vcs.Vendors.Github) {
            @Override
            public void actionPerformed(@NotNull AnActionEvent e) {
                BrowserUtil.browse("https://github.com/PerccyKing/batslog/issues");
            }
        }, new AnAction(AllIcons.Toolwindows.Documentation) {
            @Override
            public void actionPerformed(@NotNull AnActionEvent e) {
                BrowserUtil.browse("https://blog.csdn.net/qq_29602577/article/details/110390650");
            }
        });
        contentManager.addContent(formatConsoleContent);
        contentManager.addContent(settingFormContent);
        SqlDataSourceManager.getManagers(project).get(1).getDataSources();
        DataSourceManager<DasDataSource> dataSourceManager = (DataSourceManager<DasDataSource>) SqlDataSourceManager.getManagers(project).get(1);
        List<?> dataSources = dataSourceManager.getDataSources();
        SqlResolveEditor sqlResolveEditor = new SqlResolveEditor(project);
        sqlResolveEditor.getComponent();
        DbDataSourceImpl dbDataSource = new DbDataSourceImpl(project, (DasDataSource) dataSources.get(0), dataSourceManager);
        SqlPreviewPanel sqlPreviewPanel = new SqlPreviewPanel(project, dbDataSource, null, "", "", false, new Runnable() {
            @Override
            public void run() {

            }
        });
//        JdbcConsole build = JdbcConsole.newConsole(project).fromDataSource((DasDataSource) dataSources.get(0)).build();
        Content test = contentFactory.createContent(sqlPreviewPanel, "TEST", false);
        contentManager.addContent(test);
        ActionManager instance = ActionManager.getInstance();
        instance.replaceAction("$FormatSql", new FormatSqlAction(BatsLogBundle.message("formatSql"), "", BatsLogIcons.BATS_LOG));
        instance.replaceAction("$CopySql", new CopySqlAction(BatsLogBundle.message("copySql"), "", BatsLogIcons.BATS_LOG_COPY));
    }

}
