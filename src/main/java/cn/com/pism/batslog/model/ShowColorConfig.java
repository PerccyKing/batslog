package cn.com.pism.batslog.model;

import com.alibaba.fastjson.annotation.JSONField;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

/**
 * @author PerccyKing
 * @version 0.0.1
 * @date 2021/08/02 下午 06:42
 * @since 0.0.1
 */
@Data
@NoArgsConstructor
@AllArgsConstructor
public class ShowColorConfig {
    @JSONField(ordinal = 0)
    private String keyword;

    @JSONField(ordinal = 1)
    private boolean enabled;

    @JSONField(ordinal = 2)
    private String bg;

    @JSONField(ordinal = 3)
    private String fg;
}
