package cn.com.pism.batslog.converter;

import com.alibaba.fastjson.JSONObject;
import com.intellij.util.xmlb.Converter;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

import java.awt.*;

/**
 * @author PerccyKing
 * @version 0.0.1
 * @date 2021/01/06 下午 10:17
 * @since 0.0.1
 */
public class ColorConverter extends Converter<Color> {
    @Nullable
    @Override
    public Color fromString(@NotNull String value) {
        return JSONObject.parseObject(value, Color.class);
    }

    @Nullable
    @Override
    public String toString(@NotNull Color value) {
        return JSONObject.toJSONString(value);
    }
}
