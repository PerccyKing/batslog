package cn.com.pism.batslog.points;

import cn.com.pism.batslog.settings.BatsLogSettingState;
import cn.com.pism.batslog.util.BatsLogUtil;
import cn.com.pism.batslog.util.GlobalVar;
import cn.com.pism.batslog.util.SqlFormatUtil;
import com.intellij.execution.ConsoleFolding;
import com.intellij.openapi.project.Project;
import org.apache.commons.collections.CollectionUtils;
import org.apache.commons.lang3.StringUtils;
import org.jetbrains.annotations.NotNull;

import java.util.ArrayList;
import java.util.List;

import static cn.com.pism.batslog.constants.BatsLogConstant.PARAMS_PREFIX;
import static cn.com.pism.batslog.constants.BatsLogConstant.SQL_PREFIX;
import static cn.hutool.core.text.StrPool.COMMA;

/**
 * @author PerccyKing
 * @version 0.0.1
 * @since 2020/10/24 下午 09:06
 */
public class BatsLogConsoleFolding extends ConsoleFolding {

    /**
     * @param project current project
     * @param line    line to check whether it should be folded or not
     * @return {@code true} if line should be folded, {@code false} if not
     */
    @Override
    public boolean shouldFoldLine(@NotNull Project project, @NotNull String line) {
        //先判断有没有开启SQL监听
        if (Boolean.TRUE.equals(BatsLogUtil.getTailStatus(project))) {

            BatsLogSettingState service = BatsLogSettingState.getInstance(project);

            String sqlPrefix = StringUtils.isBlank(service.getSqlPrefix()) ? SQL_PREFIX : service.getSqlPrefix();
            String paramsPrefix = StringUtils.isBlank(service.getParamsPrefix()) ? PARAMS_PREFIX : service.getParamsPrefix();

            if (Boolean.TRUE.equals(service.getEnableMixedPrefix()) && sqlPrefix.split(COMMA).length > 1) {
                enabledMultiSqlPrefix(project, line, sqlPrefix, paramsPrefix);
            } else {
                notEnabledMultiSqlPrefix(project, line, sqlPrefix, paramsPrefix);
            }
        }
        return super.shouldFoldLine(project, line);
    }

    private void notEnabledMultiSqlPrefix(@NotNull Project project, @NotNull String line, String sqlPrefix, String paramsPrefix) {
        //从缓存中获取一次日志
        List<String> sourceSqlList = GlobalVar.getSourceSqlList(project);
        if ((CollectionUtils.isEmpty(sourceSqlList))) {
            //缓存中部存在新增一个列表
            sourceSqlList = new ArrayList<>();
            //缓存为空，当前行如果匹配SQL行直接放入缓存
            if (line.contains(sqlPrefix)) {
                sourceSqlList.add(line);
                GlobalVar.putSourceSqlList(project, sourceSqlList);
            }
        } else {
            //缓存不为空，判断缓存中 最后一行日志是否以 `)`结束
            String lastLine = sourceSqlList.get(sourceSqlList.size() - 1);
            String currCache = String.join("\n", sourceSqlList);
            //最后一行可能是结束行判断，如果最后一行包含参数前缀，判断为可能是最后一行
            boolean maybeEndLine = currCache.contains(sqlPrefix) && currCache.contains(paramsPrefix);
            if (maybeEndLine) {
                maybeEndLine = !SqlFormatUtil.nextLineIsParams(lastLine, paramsPrefix);
            }
            if (maybeEndLine) {
                //缓存末行正常结束，判断日志中 参数和SQL存在数量，数量一致，进行格式化
                String logs = String.join("\n", sourceSqlList);
                if (StringUtils.countMatches(logs, sqlPrefix) != 0 &&
                        StringUtils.countMatches(logs, paramsPrefix) != 0 &&
                        (StringUtils.countMatches(logs, sqlPrefix) == StringUtils.countMatches(logs, paramsPrefix))) {
                    GlobalVar.removeSourceListByProject(project);
                    sourceSqlList = new ArrayList<>();
                    SqlFormatUtil.format(logs, project);
                }
            }
            sourceSqlList.add(line);
            GlobalVar.putSourceSqlList(project, sourceSqlList);
        }
    }

    /**
     * <p>
     * 开启了汇合前缀处理
     * </p>
     *
     * @param project      : 项目对象
     * @param line         : console进来的日志行
     * @param sqlPrefix    : sql行前缀
     * @param paramsPrefix : 参数行前缀
     * @author PerccyKing
     * @since 2023/1/22 19:31
     */
    private void enabledMultiSqlPrefix(@NotNull Project project, @NotNull String line, String sqlPrefix, String paramsPrefix) {
        //从缓存中获取一次日志
        List<String> sourceSqlList = GlobalVar.getSourceSqlList(project);
        if ((CollectionUtils.isEmpty(sourceSqlList))) {
            //缓存中部存在新增一个列表
            sourceSqlList = new ArrayList<>();
            //缓存为空，当前行如果匹配SQL行直接放入缓存

            if (StringUtils.containsAny(line, sqlPrefix.split(COMMA))) {
                sourceSqlList.add(line);
                GlobalVar.putSourceSqlList(project, sourceSqlList);
            }
        } else {
            //缓存不为空，判断缓存中 最后一行日志是否以 `)`结束
            String lastLine = sourceSqlList.get(sourceSqlList.size() - 1);
            String currCache = String.join("\n", sourceSqlList);
            //最后一行可能是结束行判断，如果最后一行包含参数前缀，判断为可能是最后一行
            boolean maybeEndLine = StringUtils.containsAny(currCache, sqlPrefix.split(",")) && currCache.contains(paramsPrefix);
            if (maybeEndLine) {
                maybeEndLine = !SqlFormatUtil.nextLineIsParams(lastLine, paramsPrefix);
            }
            if (maybeEndLine) {
                sourceSqlList = getSourceSqlListFromEndLine(project, sqlPrefix, paramsPrefix, sourceSqlList);
            }
            sourceSqlList.add(line);
            GlobalVar.putSourceSqlList(project, sourceSqlList);
        }
    }

    @NotNull
    private static List<String> getSourceSqlListFromEndLine(@NotNull Project project,
                                                            String sqlPrefix,
                                                            String paramsPrefix,
                                                            List<String> sourceSqlList) {
        //缓存末行正常结束，判断日志中 参数和SQL存在数量，数量一致，进行格式化
        String logs = String.join("\n", sourceSqlList);
        String[] split = sqlPrefix.split(",");
        int countMatches = 0;
        for (String s : split) {
            countMatches = countMatches + StringUtils.countMatches(logs, s);
        }
        if (countMatches != 0 &&
                StringUtils.countMatches(logs, paramsPrefix) != 0 &&
                (countMatches == StringUtils.countMatches(logs, paramsPrefix))) {
            GlobalVar.removeSourceListByProject(project);
            sourceSqlList = new ArrayList<>();
            SqlFormatUtil.format(logs, project);
        }
        return sourceSqlList;
    }
}
