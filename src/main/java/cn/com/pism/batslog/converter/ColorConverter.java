package cn.com.pism.batslog.converter;

import cn.com.pism.batslog.model.RgbColor;
import com.alibaba.fastjson2.JSON;
import com.intellij.util.xmlb.Converter;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

/**
 * @author PerccyKing
 * @version 0.0.1
 * @since 2021/01/06 下午 10:17
 */
public class ColorConverter extends Converter<RgbColor> {
    @Nullable
    @Override
    public RgbColor fromString(@NotNull String value) {
        return JSON.parseObject(value, RgbColor.class);
    }

    @Nullable
    @Override
    public String toString(@NotNull RgbColor value) {
        return JSON.toJSONString(value);
    }
}
