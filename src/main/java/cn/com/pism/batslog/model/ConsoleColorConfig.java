package cn.com.pism.batslog.model;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

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
    private RgbColor backgroundColor;
    private RgbColor foregroundColor;
    private boolean enabled;

    public Object[] toArray() {
        return new Object[]{id, sort, keyWord, backgroundColor, foregroundColor, enabled};
    }

    public ConsoleColorConfig toConfig(Object[] objects) {
        return new ConsoleColorConfig(
                (String) objects[0],
                (int) objects[1],
                (String) objects[2],
                (RgbColor) objects[3],
                (RgbColor) objects[4],
                (Boolean) objects[5]
        );
    }
}
