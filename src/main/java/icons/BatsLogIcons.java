package icons;


import com.intellij.openapi.util.IconLoader;

import javax.swing.*;

/**
 * @author PerccyKing
 * @version 0.0.1
 * @date 2020/10/25 上午 11:57
 * @since 0.0.1
 */
public interface BatsLogIcons {
    /**
     * 主图标
     */
    Icon BATS_LOG = IconLoader.getIcon("/icons/batsLog.svg", BatsLogIcons.class);

    /**
     * 复制
     */
    Icon BATS_LOG_COPY = IconLoader.getIcon("/icons/batsLogCopy.svg", BatsLogIcons.class);

    /**
     * 美化
     */
    Icon BEAUTY = IconLoader.getIcon("/icons/beauty.svg", BatsLogIcons.class);

    /**
     * beta
     */
    Icon BETA = IconLoader.getIcon("/icons/beta.svg", BatsLogIcons.class);

}
