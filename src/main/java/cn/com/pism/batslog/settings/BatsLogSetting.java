package cn.com.pism.batslog.settings;

import cn.com.pism.batslog.enums.DbType;
import com.intellij.ide.util.PropertiesComponent;
import com.intellij.openapi.project.Project;
import org.apache.commons.lang3.StringUtils;
import org.jetbrains.annotations.NotNull;

/**
 * @author wangyihuai
 * @date 2020/10/28 上午 09:11
 */
public class BatsLogSetting {
    private static final String PREFIX = "BatsLog";

    private static final String SELECTED_TYPE = PREFIX + "SelectedType";

    public static DbType getDbType(@NotNull Project project) {
        String dbName = PropertiesComponent.getInstance(project).getValue(project.getName() + SELECTED_TYPE);
        if (StringUtils.isNotBlank(dbName)) {
            return DbType.getByName(dbName);
        } else {
            return DbType.NONE;
        }
    }

    public static void setDbType(@NotNull Project project, DbType value) {
        PropertiesComponent.getInstance(project).setValue(project.getName() + SELECTED_TYPE, value.getName());
    }

}
