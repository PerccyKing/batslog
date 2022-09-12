package cn.com.pism.batslog.ui;

import cn.com.pism.batslog.BatsLogBundle;
import cn.com.pism.batslog.model.BslErrorMod;
import cn.com.pism.batslog.util.BslActionToolBarUtil;
import com.intellij.icons.AllIcons;
import com.intellij.openapi.actionSystem.*;
import com.intellij.openapi.project.Project;
import com.intellij.ui.table.JBTable;
import lombok.Data;
import lombok.EqualsAndHashCode;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

import javax.swing.*;
import javax.swing.table.DefaultTableModel;

import static cn.com.pism.batslog.util.BatsLogUtil.ERROR_LIST_TABLE_MODEL;

/**
 * @author PerccyKing
 * @version 0.0.1
 * @date 2021/10/13 下午 04:33
 * @since 0.0.1
 */
@Data
public class ErrorListPanel {
    private JPanel root;
    private JBTable errorList;
    private JPanel toolPanel;

    public ErrorListPanel(Project project) {

        //初始化table
        initTable(project);
        DefaultActionGroup actions = new DefaultActionGroup();
        final MyDeleteRowAction deleteRowAction = new MyDeleteRowAction(project, errorList, BatsLogBundle.message("batslog.action.delete"), AllIcons.General.Remove);
        actions.add(deleteRowAction);
        actions.add(new MyProcessAction(project, errorList, BatsLogBundle.message("batslog.action.viewAndProcess"), AllIcons.Debugger.AttachToProcess));
        actions.addSeparator();
        actions.add(new MyClearAllAction(project, errorList, BatsLogBundle.message("batslog.action.console.clear"), AllIcons.Actions.GC));
        final ActionToolbar actionToolBar = BslActionToolBarUtil.createActionToolBar(ActionPlaces.TOOLWINDOW_CONTENT, true, actions);
        toolPanel.add(actionToolBar.getComponent());

    }

    /**
     * <p>
     * 初始化table
     * </p>
     *
     * @param project : 项目实例
     * @author PerccyKing
     * @date 2021/10/15 上午 09:30
     */
    private void initTable(Project project) {
        DefaultTableModel tableModel = ERROR_LIST_TABLE_MODEL.get(project);
        if (tableModel == null) {
            tableModel = createTableModel();
            ERROR_LIST_TABLE_MODEL.put(project, tableModel);
        }
        errorList.setModel(tableModel);
    }

    @NotNull
    public static DefaultTableModel createTableModel() {
        //表格列名 SQL、参数、时间、错误信息，操作
        String[] columns = new String[]{
                BatsLogBundle.message("batslog.errorList.title.sql"),
                BatsLogBundle.message("batslog.errorList.title.params"),
                BatsLogBundle.message("batslog.errorList.title.time"),
                BatsLogBundle.message("batslog.errorList.title.errorMsg")
        };
        return new DefaultTableModel(null, columns) {
            @Override
            public boolean isCellEditable(int row, int column) {
                return false;
            }
        };
    }

    @EqualsAndHashCode(callSuper = true)
    @Data
    public static class MyDeleteRowAction extends AnAction {

        /**
         * project
         */
        private Project project;
        /**
         * table
         */
        private JBTable table;

        public MyDeleteRowAction(@Nullable Project project,
                                 @Nullable JBTable table,
                                 @Nullable String text,
                                 @Nullable Icon icon) {
            super(text, "", icon);
            this.project = project;
            this.table = table;
        }

        @Override
        public void actionPerformed(@NotNull AnActionEvent e) {
            //获取选中行，然后删除
            final int[] selectedRows = table.getSelectedRows();
            final DefaultTableModel model = (DefaultTableModel) table.getModel();
            for (int i = selectedRows.length - 1; i >= 0; i--) {
                model.removeRow(selectedRows[i]);
            }
        }

        @Override
        public void update(@NotNull AnActionEvent e) {
            super.update(e);
            boolean enabled = table.getSelectedRows().length > 0;
            e.getPresentation().setEnabled(enabled);
        }

    }

    @EqualsAndHashCode(callSuper = true)
    @Data
    public static class MyProcessAction extends AnAction {

        private Project project;

        private JBTable table;

        public MyProcessAction(@Nullable Project project,
                               @Nullable JBTable table,
                               @Nullable String text,
                               @Nullable Icon icon) {
            super(text, "", icon);
            this.project = project;
            this.table = table;
        }

        @Override
        public void actionPerformed(@NotNull AnActionEvent e) {
            //获取选中行的数据
            final int selectedRow = table.getSelectedRow();
            final int columnCount = table.getColumnCount();
            final DefaultTableModel tableModel = (DefaultTableModel) table.getModel();
            Object[] row = new Object[columnCount];
            for (int i = 0; i < columnCount; i++) {
                row[i] = tableModel.getValueAt(selectedRow, i);
            }
            final BslErrorMod bslErrorMod = new BslErrorMod().toMod(row);
            //弹出处理页面
            new ErrorProcessDialog(project, bslErrorMod);
        }

        @Override
        public void update(@NotNull AnActionEvent e) {
            super.update(e);
            boolean enabled = table.getSelectedRows().length == 1;
            e.getPresentation().setEnabled(enabled);
        }
    }

    @EqualsAndHashCode(callSuper = true)
    @Data
    public static class MyClearAllAction extends AnAction {

        private Project project;

        private JBTable table;

        public MyClearAllAction(@Nullable Project project,
                                @Nullable JBTable table,
                                @Nullable String text,
                                @Nullable Icon icon) {
            super(text, "", icon);
            this.project = project;
            this.table = table;
        }

        @Override
        public void actionPerformed(@NotNull AnActionEvent e) {
            final DefaultTableModel tableModel = (DefaultTableModel) table.getModel();
            final int rowCount = tableModel.getRowCount();
            for (int i = rowCount - 1; i >= 0; i--) {
                tableModel.removeRow(i);
            }
        }

        @Override
        public void update(@NotNull AnActionEvent e) {
            super.update(e);
            boolean enabled = table.getModel().getRowCount() > 0;
            e.getPresentation().setEnabled(enabled);
        }
    }
}
