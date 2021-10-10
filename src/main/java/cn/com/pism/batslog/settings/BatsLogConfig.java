package cn.com.pism.batslog.settings;

import cn.com.pism.batslog.constants.BatsLogConstant;
import cn.com.pism.batslog.converter.ColorConverter;
import cn.com.pism.batslog.converter.ConsoleColorConfigConverter;
import cn.com.pism.batslog.converter.DbTypeConverter;
import cn.com.pism.batslog.enums.DbType;
import cn.com.pism.batslog.model.ConsoleColorConfig;
import cn.com.pism.batslog.model.RgbColor;
import com.intellij.util.xmlb.annotations.OptionTag;
import lombok.Data;

import java.util.ArrayList;
import java.util.List;

/**
 * @author PerccyKing
 * @version 0.0.1
 * @date 2021/10/07 下午 06:39
 * @since 0.0.1
 */
@Data
public class BatsLogConfig {


    /**
     * 日志SQL行截取前缀
     */
    private String sqlPrefix = BatsLogConstant.SQL_PREFIX;

    /**
     * 日志参数行前缀
     */
    private String paramsPrefix = BatsLogConstant.PARAMS_PREFIX;

    /**
     * 时间格式化
     */
    private String timeFormat = BatsLogConstant.TIME_FORMAT;

    /**
     * 脱敏
     */
    private Boolean desensitize = Boolean.FALSE;

    /**
     * 美化
     */
    private Boolean prettyFormat = Boolean.TRUE;

    /**
     * 参数化
     */
    private Boolean parameterized = Boolean.FALSE;

    /**
     * 关键字转大写
     */
    private Boolean toUpperCase = Boolean.FALSE;

    /**
     * 日志开启时间戳
     */
    private Boolean addTimestamp = Boolean.FALSE;

    private Boolean startWithProject = Boolean.FALSE;

    /**
     * 数据库类型
     */
    @OptionTag(converter = DbTypeConverter.class)
    public DbType dbType = DbType.MYSQL;

    @OptionTag(converter = ColorConverter.class)
    private RgbColor keyWordDefCol = new RgbColor(204, 120, 50);

    @OptionTag(converter = ConsoleColorConfigConverter.class)
    private List<ConsoleColorConfig> colorConfigs = new ArrayList<>();

}
