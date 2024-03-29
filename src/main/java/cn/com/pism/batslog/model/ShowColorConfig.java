package cn.com.pism.batslog.model;

import com.alibaba.fastjson2.annotation.JSONField;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

/**
 * @author PerccyKing
 * @version 0.0.1
 * @since 2021/08/02 下午 06:42
 */
@Data
@NoArgsConstructor
@AllArgsConstructor
public class ShowColorConfig {
    @JSONField
    private String keyword;

    @JSONField(ordinal = 1)
    private boolean enabled;

    @JSONField(ordinal = 2)
    private String bg;

    @JSONField(ordinal = 3)
    private boolean bgEnabled;

    @JSONField(ordinal = 4)
    private String fg;

    @JSONField(ordinal = 5)
    private boolean fgEnabled;
}
