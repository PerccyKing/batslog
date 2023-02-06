package icons;


import com.intellij.openapi.util.IconLoader;

import javax.swing.*;

/**
 * @author PerccyKing
 * @version 0.0.1
 * @since 2020/10/25 上午 11:57
 */
public class BatsLogIcons {
    private BatsLogIcons() {
    }

    /**
     * 主图标
     */
    public static final Icon BATS_LOG = IconLoader.getIcon("/icons/batsLog.svg", BatsLogIcons.class);

    /**
     * 复制
     */
    public static final Icon BATS_LOG_COPY = IconLoader.getIcon("/icons/batsLogCopy.svg", BatsLogIcons.class);

    /**
     * 美化
     */
    public static final Icon BEAUTY = IconLoader.getIcon("/icons/beauty.svg", BatsLogIcons.class);

    /**
     * beta
     */
    @SuppressWarnings("unused")
    public static final Icon BETA = IconLoader.getIcon("/icons/beta.svg", BatsLogIcons.class);

}
