package cn.com.pism.batslog.converter;

import cn.com.pism.batslog.enums.DbType;
import com.intellij.util.xmlb.Converter;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

/**
 * @author PerccyKing
 * @version 0.0.1
 * @date 2021/01/06 下午 10:12
 * @since 0.0.1
 */
public class DbTypeConverter extends Converter<DbType> {
    @Nullable
    @Override
    public DbType fromString(@NotNull String value) {
        return DbType.getByName(value);
    }

    @Nullable
    @Override
    public String toString(@NotNull DbType value) {
        return value.getName();
    }
}
