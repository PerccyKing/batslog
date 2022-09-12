package cn.com.pism.batslog.converter;

import cn.com.pism.batslog.model.ConsoleColorConfig;
import com.alibaba.fastjson2.JSON;
import com.intellij.util.xmlb.Converter;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

import java.util.List;

/**
 * @author PerccyKing
 * @version 0.0.1
 * @date 2021/07/03 下午 10:59
 * @since 0.0.1
 */
public class ConsoleColorConfigConverter extends Converter<List<ConsoleColorConfig>> {
    @Nullable
    @Override
    public List<ConsoleColorConfig> fromString(@NotNull String value) {
        return JSON.parseArray(value, ConsoleColorConfig.class);
    }

    @Nullable
    @Override
    public String toString(@NotNull List<ConsoleColorConfig> value) {
        return JSON.toJSONString(value);
    }
}
