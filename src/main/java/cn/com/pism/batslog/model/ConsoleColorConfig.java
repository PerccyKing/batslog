package cn.com.pism.batslog.model;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.awt.*;

/**
 * @author PerccyKing
 * @version 0.0.1
 * @date 2021/06/26 下午 04:58
 * @since 0.0.1
 */
@Data
@NoArgsConstructor
@AllArgsConstructor
public class ConsoleColorConfig {
    private String id;
    private int sort;
    private String keyWord;
    private Color backgroundColor;
    private Color foregroundColor;
    private boolean enabled;

    public Object[] toArray() {
        return new Object[]{id, sort, keyWord, backgroundColor, foregroundColor, enabled};
    }
}
