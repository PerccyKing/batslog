package cn.com.pism.batslog.points;

import cn.com.pism.batslog.util.BatsLogUtil;
import cn.com.pism.batslog.util.SqlFormatUtils;
import com.intellij.execution.ConsoleFolding;
import com.intellij.openapi.project.Project;
import org.jetbrains.annotations.NotNull;

import static cn.com.pism.batslog.util.BatsLogUtil.*;

/**
 * @author PerccyKing
 * @version 0.0.1
 * @date 2020/10/24 下午 09:06
 * @since 0.0.1
 */
public class BatsLogConsoleFolding extends ConsoleFolding {
    /**
     * @param project current project
     * @param line    line to check whether it should be folded or not
     * @return {@code true} if line should be folded, {@code false} if not
     */
    @Override
    public boolean shouldFoldLine(@NotNull Project project, @NotNull String line) {
        line = line.replace("\n", "");
        if (BatsLogUtil.TAIL_STATUS) {
            if (line.contains(PREPARING) && SOURCE_SQL_LIST.size() == 0) {
                SOURCE_SQL_LIST.add(line);
            } else if (SOURCE_SQL_LIST.size() >= 2) {
                SOURCE_SQL_LIST.clear();
            }

            if (line.contains(PARAMETERS) && SOURCE_SQL_LIST.size() != 0) {
                SOURCE_SQL_LIST.add(line);
                if (SOURCE_SQL_LIST.size() == 2) {
                    SqlFormatUtils.format(String.join("\n", SOURCE_SQL_LIST));
                    SOURCE_SQL_LIST.clear();
                }
            }
        }
        return super.shouldFoldLine(project, line);
    }
}
