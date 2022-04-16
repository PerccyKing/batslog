package cn.com.pism.batslog.model;

import lombok.AllArgsConstructor;
import lombok.Data;

/**
 * @author PerccyKing
 * @version 0.0.1
 * @date 2022/04/15 下午 03:18
 * @since 0.0.1
 */
@Data
@AllArgsConstructor
public class EnabledRgbColor {
    private boolean enabledColor;
    private RgbColor rgbColor;
}
