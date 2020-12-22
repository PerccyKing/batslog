package cn.com.pism.batslog;

import com.intellij.AbstractBundle;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.PropertyKey;

/**
 * @author wangyihuai
 * @date 2020/12/21 下午 02:43
 */
public class BatsLogBundle extends AbstractBundle {

    private static final String BUNDLE = "text.BatsLogBundle";

    public static String message(@NotNull @PropertyKey(resourceBundle = BUNDLE) String key, @NotNull Object... params) {
        return INSTANCE.getMessage(key, params);
    }

    private static final BatsLogBundle INSTANCE = new BatsLogBundle();

    private BatsLogBundle() {
        super(BUNDLE);
    }
}
