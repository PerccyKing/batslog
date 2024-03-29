package cn.com.pism.batslog.ui.component;

import cn.com.pism.batslog.model.BslErrorMod;
import cn.com.pism.batslog.util.SqlFormatUtil;
import com.intellij.openapi.project.Project;
import com.intellij.openapi.ui.ComboBox;
import com.intellij.ui.GuiUtils;
import com.jgoodies.forms.layout.CellConstraints;
import com.jgoodies.forms.layout.FormLayout;
import lombok.Data;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.lang3.StringUtils;
import org.jetbrains.annotations.NotNull;

import javax.swing.*;
import java.awt.*;
import java.util.ArrayList;
import java.util.List;

import static cn.com.pism.batslog.constants.BatsLogConstant.TYPES;

/**
 * @author PerccyKing
 * @version 0.0.1
 * @since 2021/12/11 下午 09:07
 */
@Data
@Slf4j
public class SqlAndParamsProcessPanel {
    private JPanel root;
    private JPanel sqlContentLine;

    private Project project;

    private BslErrorMod bslErrorMod;

    public SqlAndParamsProcessPanel(Project project, BslErrorMod bslErrorMod) {
        this.project = project;
        this.bslErrorMod = bslErrorMod;

        String errorModSql = bslErrorMod.getSql();

        boolean lastNeedShow = errorModSql.endsWith("?");

        //生成SQL行
        final String[] sqlLines = errorModSql.split("\\?");
        List<String> encodedRowSpecs = new ArrayList<>();


        for (int i = 0; i < sqlLines.length; i++) {
            encodedRowSpecs.add("default");
        }

        FormLayout formLayout = new FormLayout("5dlu, right:default, 5dlu, 80dlu, 60dlu", String.join(",", encodedRowSpecs));
        sqlContentLine.setLayout(formLayout);

        String params = bslErrorMod.getParams();
        List<Object> paramList;
        try {
            paramList = SqlFormatUtil.parseParamToList(params);
        } catch (Exception e) {
            paramList = exceptionHandle(params, e);
        }
        createProcessPanel(sqlLines, paramList, lastNeedShow);

        GuiUtils.replaceJSplitPaneWithIDEASplitter(root);
    }

    @NotNull
    private static List<Object> exceptionHandle(String params, Exception e) {
        List<Object> paramList;
        //异常解析
        paramList = new ArrayList<>();
        String[] paramArr = params.split(",");
        for (String maybeParam : paramArr) {
            //如果分割出来的参数没有()，判断没有type
            if (maybeParam.contains("(") && maybeParam.contains(")") && maybeParam.indexOf("(") < maybeParam.indexOf(")")) {
                //解析type
                String paramVal = maybeParam.substring(maybeParam.lastIndexOf("(") + 1);
                String type = maybeParam.substring(maybeParam.lastIndexOf("(") + 2, maybeParam.lastIndexOf(")") + 1);
                try {
                    paramList.add(SqlFormatUtil.pack(paramVal, type));
                } catch (ClassNotFoundException ex) {
                    e.printStackTrace();
                }
            } else {
                try {
                    paramList.add(SqlFormatUtil.pack(maybeParam, null));
                } catch (ClassNotFoundException ex) {
                    e.printStackTrace();
                }
            }
        }
        return paramList;
    }

    private void createProcessPanel(String[] sqlLines, List<Object> paramsList, boolean lastNeedShow) {
        log.info("lastNeedShow:{}", lastNeedShow);
        if (sqlLines.length >= paramsList.size()) {
            createWhenNoteq(sqlLines, paramsList);
        } else {
            for (int i = 0; i < paramsList.size(); i++) {
                Object param = paramsList.get(i);
                String sqlLine = "";
                if (i < sqlLines.length) {
                    sqlLine = sqlLines[i];
                    if (sqlLine.length() > 50) {
                        sqlLine = "……" + sqlLine.substring(sqlLine.length() - 50);
                    }
                }
                String type = param.getClass().getTypeName();
                type = type.substring(type.lastIndexOf(".") + 1);
                addLineToRow(i, sqlLine, type, param.toString());
            }
        }
    }

    private void createWhenNoteq(String[] sqlLines, List<Object> paramsList) {
        boolean notHaveParams = paramsList.isEmpty();
        for (int i = 0; i < sqlLines.length; i++) {
            String line = sqlLines[i];
            if (line.length() > 50) {
                line = "……" + line.substring(line.length() - 50);
            }
            String type = "";
            String paramsVal = "";
            if (i < paramsList.size()) {
                Object param = paramsList.get(i);
                type = param.getClass().getTypeName();
                type = type.substring(type.lastIndexOf(".") + 1);
                paramsVal = param.toString();
            }
            if (notHaveParams) {
                type = TYPES.get(0);
                paramsVal = "";
            }
            addLineToRow(i, line, type, paramsVal);
        }
    }


    private void addLineToRow(int i, String line, String type, String paramsVal) {
        CellConstraints c = new CellConstraints();
        if (line != null) {
            final JPanel jPanel = new JPanel(new FlowLayout(FlowLayout.RIGHT));
            jPanel.setSize(new Dimension(-1, Integer.MAX_VALUE));
            jPanel.add(new JLabel(line, SwingConstants.RIGHT));
            sqlContentLine.add(jPanel, c.xy(2, i + 1));
        }
        if (paramsVal != null) {
            sqlContentLine.add(new JTextField(paramsVal), c.xy(4, i + 1));
            sqlContentLine.add(getSelection(type), c.xy(5, i + 1));
        }
    }

    private JComboBox<String> getSelection(String type) {
        ComboBox<String> comboBox = new ComboBox<>();
        comboBox.addItem("--请选择--");
        for (String ty : TYPES) {
            comboBox.addItem(ty);
        }
        if (StringUtils.isNotBlank(type)) {
            comboBox.setSelectedItem(type);
        }
        return comboBox;
    }

}
