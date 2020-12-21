package cn.com.pism.batslog;

import com.intellij.AbstractBundle;

/**
 * @author wangyihuai
 * @date 2020/12/21 下午 02:43
 */
public class BatsLogBundle extends AbstractBundle {

    private static final String BUNDLE = "text.BatsLogBundle";

    public BatsLogBundle() {
        super(BUNDLE);
    }

    public String text() {
        return getMessage("");
    }
}
