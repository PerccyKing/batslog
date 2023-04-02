package cn.com.pism.batslog.model;

import cn.com.pism.batslog.util.SqlFormatUtil;
import com.intellij.execution.impl.ConsoleViewImpl;
import com.intellij.openapi.project.Project;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.experimental.Accessors;

/**
 * @author PerccyKing
 * @since 2023/4/2 17:59
 */
@Data
@Builder
@AllArgsConstructor
@NoArgsConstructor
@Accessors(chain = true)
public class FormatIn {

    /**
     * 日志字符串
     */
    private String log;

    /**
     * 项目对象
     */
    private Project project;

    /**
     * 是否输出到console
     */
    private boolean printToConsole;

    /**
     * console
     */
    private ConsoleViewImpl console;

    /**
     * 回调方法
     */
    private SqlFormatUtil.CallBack callBack;
}
