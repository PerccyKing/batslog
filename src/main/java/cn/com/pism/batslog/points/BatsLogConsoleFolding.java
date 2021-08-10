package cn.com.pism.batslog.points;

import cn.com.pism.batslog.util.BatsLogUtil;
import cn.com.pism.batslog.util.SqlFormatUtil;
import com.intellij.execution.ConsoleFolding;
import com.intellij.openapi.project.Project;
import org.apache.commons.collections.CollectionUtils;
import org.jetbrains.annotations.NotNull;

import java.util.ArrayList;
import java.util.List;

import static cn.com.pism.batslog.constants.BatsLogConstant.PARAMS_PREFIX;
import static cn.com.pism.batslog.constants.BatsLogConstant.SQL_PREFIX;
import static cn.com.pism.batslog.util.BatsLogUtil.SOURCE_SQL_LIST_MAP;

/**
 * @author PerccyKing
 * @version 0.0.1
 * @date 2020/10/24 下午 09:06
 * @since 0.0.1
 */
public class BatsLogConsoleFolding extends ConsoleFolding {

    public static final Integer LIST_SIZE = 2;

    /**
     * @param project current project
     * @param line    line to check whether it should be folded or not
     * @return {@code true} if line should be folded, {@code false} if not
     */
    @Override
    public boolean shouldFoldLine(@NotNull Project project, @NotNull String line) {
        line = line.replace("\n", "");
        List<String> sourceSqlList = SOURCE_SQL_LIST_MAP.get(project);
        if ((CollectionUtils.isEmpty(sourceSqlList)) || (sourceSqlList.size() > LIST_SIZE)) {
            sourceSqlList = new ArrayList<>();
        }
        //sql和参数占用多行
        if (BatsLogUtil.getTailStatus(project)) {
            if (line.contains(SQL_PREFIX) && sourceSqlList.size() == 0) {
                sourceSqlList.add(line);
                SOURCE_SQL_LIST_MAP.put(project, sourceSqlList);
            } else if (sourceSqlList.size() >= LIST_SIZE) {
                SOURCE_SQL_LIST_MAP.remove(project);
            }

            if (line.contains(PARAMS_PREFIX) && sourceSqlList.size() != 0) {
                sourceSqlList.add(line);
                SOURCE_SQL_LIST_MAP.put(project, sourceSqlList);
                if (sourceSqlList.size() == LIST_SIZE) {
                    SqlFormatUtil.format(String.join("\n", sourceSqlList), project);
                    SOURCE_SQL_LIST_MAP.remove(project);
                }
            }
        }
        return super.shouldFoldLine(project, line);
    }
}
