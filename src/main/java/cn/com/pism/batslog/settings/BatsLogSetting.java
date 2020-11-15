package cn.com.pism.batslog.settings;

import cn.com.pism.batslog.enums.DbType;
import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONObject;
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
    public static final String KEYWORDS = PREFIX + "Keywords";


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

    public static void setValue(@NotNull Project project, BatsLogValue<?> batsLogValue) {
        PropertiesComponent.getInstance(project).setValue(batsLogValue.getKey(), JSON.toJSONString(batsLogValue));
    }

    public static <T> BatsLogValue<T> getValue(@NotNull Project project, String key, Class<T> clazz) {
        String value = PropertiesComponent.getInstance(project).getValue(key);
        if (StringUtils.isNotBlank(value)) {
            JSONObject jsonObject = JSON.parseObject(value);
            return new BatsLogValue<>(key, JSON.parseObject(jsonObject.getString("value"), clazz));
        } else {
            return new BatsLogValue<>(key, null);
        }

    }

}
