package cn.com.pism.batslog.settings;

import cn.com.pism.batslog.constants.BatsLogConstant;
import cn.com.pism.batslog.converter.ColorConverter;
import cn.com.pism.batslog.converter.ConsoleColorConfigConverter;
import cn.com.pism.batslog.converter.DbTypeConverter;
import cn.com.pism.batslog.enums.DbType;
import cn.com.pism.batslog.model.ConsoleColorConfig;
import cn.com.pism.batslog.model.RgbColor;
import com.alibaba.fastjson2.JSON;
import com.intellij.util.xmlb.annotations.OptionTag;
import lombok.Data;
import org.apache.commons.collections.CollectionUtils;
import org.apache.commons.lang3.BooleanUtils;

import java.util.ArrayList;
import java.util.List;

import static cn.com.pism.batslog.util.GlobalVar.getDefaultColorConfigs;

/**
 * @author PerccyKing
 * @version 0.0.1
 * @since 2021/10/07 下午 06:39
 */
@Data
public class BatsLogConfig {

    /**
     * 如果使用全局配置，除了数据库配置，都会以全局配置优先
     */
    private Boolean useGlobalConfig = Boolean.TRUE;


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

    private Boolean enableMixedPrefix = Boolean.FALSE;

    private String encoding = BatsLogConstant.DEFAULT_ENCODING;

    /**
     * 数据库类型
     */
    @OptionTag(converter = DbTypeConverter.class)
    public DbType dbType = DbType.MYSQL;

    @OptionTag(converter = ColorConverter.class)
    private RgbColor keyWordDefCol = new RgbColor(204, 120, 50);

    /**
     * 是否启用关键字颜色
     */
    private boolean enabledKeyWordDefCol = true;

    @OptionTag(converter = ConsoleColorConfigConverter.class)
    private List<ConsoleColorConfig> colorConfigs = new ArrayList<>();


    public String hash() {
        return sqlPrefix + paramsPrefix + timeFormat + encoding
                + BooleanUtils.toStringYesNo(desensitize)
                + BooleanUtils.toStringYesNo(prettyFormat)
                + BooleanUtils.toStringYesNo(parameterized)
                + BooleanUtils.toStringYesNo(toUpperCase)
                + BooleanUtils.toStringYesNo(addTimestamp)
                + BooleanUtils.toStringYesNo(startWithProject)
                + BooleanUtils.toStringYesNo(enableMixedPrefix)
                + BooleanUtils.toStringYesNo(enabledKeyWordDefCol)
                + dbType.getName()
                + JSON.toJSONString(keyWordDefCol)
                + JSON.toJSONString(hashColorConfigs(CollectionUtils.isEmpty(colorConfigs) ? getDefaultColorConfigs() : colorConfigs));
    }

    private String hashColorConfigs(List<ConsoleColorConfig> consoleColorConfigs) {
        ArrayList<ConsoleColorConfig> configs = new ArrayList<>(consoleColorConfigs);
        configs.forEach(item -> item.setId(""));
        return JSON.toJSONString(configs);
    }
}
