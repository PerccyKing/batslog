package cn.com.pism.batslog.settings;

import cn.com.pism.batslog.enums.DbType;
import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONObject;
import com.alibaba.fastjson.TypeReference;
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

    public static final String SQL_PREFIX = PREFIX + "SqlPrefix";
    public static final String PARAMS_PREFIX = PREFIX + "ParamsPrefix";


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
        PropertiesComponent.getInstance(project).setValue(project.getName() + batsLogValue.getKey(), JSON.toJSONString(batsLogValue));
    }

    public static String getVal(@NotNull Project project, String key) {
        return getValue(project, key).getValue();
    }

    public static <T> T getVal(@NotNull Project project, String key, Class<T> clazz) {
        BatsLogValue<T> batsLogValue = getValue(project, key, clazz);
        return batsLogValue.getValue();
    }

    public static BatsLogValue<String> getValue(@NotNull Project project, String key) {
        return getValue(project, key, String.class);
    }

    public static <T> BatsLogValue<T> getValue(@NotNull Project project, String key, Class<T> clazz) {
        String value = PropertiesComponent.getInstance(project).getValue(project.getName() + key);
        try {
            if (StringUtils.isNotBlank(value)) {
                return JSONObject.parseObject(value, new TypeReference<BatsLogValue<T>>(clazz) {
                });
            } else {
                return new BatsLogValue<>(key, null);
            }
        } catch (Exception e) {
            e.printStackTrace();
            return new BatsLogValue<>(key, null);
        }
    }

}
