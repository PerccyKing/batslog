package cn.com.pism.batslog.enums;

import com.alibaba.druid.util.JdbcConstants;
import com.intellij.icons.AllIcons;

import javax.swing.*;
import java.util.Arrays;
import java.util.List;
import java.util.Optional;

/**
 * @author wangyihuai
 * @date 2020/10/28 上午 09:19
 */
public enum DbType {

    /**
     * mysql
     */
    MYSQL("mysql", JdbcConstants.MYSQL.name(), AllIcons.Providers.Mysql),
    HSQL("hsql", JdbcConstants.HSQL.name(), AllIcons.Providers.Hsqldb),
    DB2("db2", JdbcConstants.DB2.name(), AllIcons.Providers.DB2),
    POSTGRESQL("postgresql", JdbcConstants.POSTGRESQL.name(), AllIcons.Providers.Postgresql),
    SYBASE("sybase", JdbcConstants.SYBASE.name(), AllIcons.Providers.Sybase),
    SQL_SERVER("SqlServer", JdbcConstants.SQL_SERVER.name(), AllIcons.Providers.SqlServer),
    ORACLE("oracle", JdbcConstants.ORACLE.name(), AllIcons.Providers.Oracle),
    ALI_ORACLE("AliOracle", JdbcConstants.ALI_ORACLE.name(), null),
    MARIADB("mariadb", JdbcConstants.MARIADB, AllIcons.Providers.Mariadb),
    DERBY("derby", JdbcConstants.DERBY.name(), AllIcons.Providers.ApacheDerby),
    HBASE("hbase", JdbcConstants.HBASE, null),
    HIVE("hive", JdbcConstants.HIVE.name(), AllIcons.Providers.Hive),
    H2("h2", JdbcConstants.H2.name(), AllIcons.Providers.H2),
    SQLITE("sqlite", JdbcConstants.SQLITE, AllIcons.Providers.Sqlite),
    NONE("none", "none", null);


    private String name;
    private String type;
    private Icon icon;

    DbType(String name, String type, Icon icon) {
        this.name = name;
        this.type = type;
        this.icon = icon;
    }

    public static DbType getByName(String name) {
        DbType[] values = DbType.values();
        Optional<DbType> first = Arrays.stream(values).filter(d -> name.equals(d.getName())).findFirst();
        return first.orElse(NONE);
    }

    public static List<DbType> getRadioButtons() {
        DbType[] values = DbType.values();
        return Arrays.asList(values.clone());
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getType() {
        return type;
    }

    public void setType(String type) {
        this.type = type;
    }

    public Icon getIcon() {
        return icon;
    }

    public void setIcon(Icon icon) {
        this.icon = icon;
    }
}
