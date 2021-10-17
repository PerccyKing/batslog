package cn.com.pism.batslog.model;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

/**
 * @author PerccyKing
 * @version 0.0.1
 * @date 2021/10/15 上午 09:15
 * @since 0.0.1
 */
@Data
@NoArgsConstructor
@AllArgsConstructor
public class BslErrorMod {
    /**
     * sql
     */
    private String sql;
    /**
     * 参数
     */
    private String params;
    /**
     * 发生时间
     */
    private String time;
    /**
     * 错误信息
     */
    private String errorMsg;

    public Object[] toArray() {
        return new Object[]{sql, params, time, errorMsg};
    }

    public BslErrorMod toMod(Object[] obj) {
        return new BslErrorMod(
                (String) obj[0],
                (String) obj[1],
                (String) obj[2],
                (String) obj[3]);
    }
}
