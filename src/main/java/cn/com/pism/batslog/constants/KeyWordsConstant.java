package cn.com.pism.batslog.constants;

import java.util.Arrays;
import java.util.HashSet;
import java.util.Set;

/**
 * @author PerccyKing
 * @version 0.0.1
 * @date 2020/11/15 下午 03:23
 * @since 0.0.1
 */
public class KeyWordsConstant {
    public static final Set<String> MYSQL = new HashSet<>();


    static {
        MYSQL.addAll(Arrays.asList(KeyWords.MYSQL));
    }

    private static class KeyWords {
        public static final String[] MYSQL = {
                "accessible", "compact", "end", "identified", "match", "order", "replicate_ignore_table", "sqlwarning", "union",
                "add", "completion", "ends", "if", "maxvalue", "out", "replicate_rewrite_db", "sql_after_mts_gaps", "unique",
                "after", "compressed", "engine", "ignore", "max_connections_per_hour", "outer", "replicate_wild_do_table", "sql_before_gtids", "unknown",
                "against", "compression", "engines", "import", "max_queries_per_hour", "outfile", "replicate_wild_ignore_table", "sql_big_result", "unlock",
                "aggregate", "concurrent", "enum", "in", "max_rows", "pack_keys", "replication", "sql_buffer_result", "unsigned",
                "all", "condition", "error", "index", "max_size", "page", "require", "sql_cache", "until",
                "alter", "connection", "errors", "indexes", "max_statement_time", "parser", "reset", "sql_calc_found_rows", "update",
                "always", "consistent", "escape", "infile", "max_updates_per_hour", "parse_gcol_expr", "resignal", "sql_no_cache", "upgrade",
                "analyse", "constraint", "escaped", "inner", "max_user_connections", "partial", "restore", "sql_small_result", "usage",
                "analyze", "contains", "event", "inout", "medium", "partition", "restrict", "ssl", "use",
                "and", "context", "events", "insensitive", "mediumblob", "partitioning", "resume", "stacked", "user",
                "any", "continue", "every", "insert", "mediumint", "partitions", "return", "starting", "use_frm",
                "as", "convert", "exchange", "install", "mediumtext", "password", "returned_sqlstate", "starts", "using",
                "asc", "cpu", "execute", "int", "merge", "phase", "returns", "stats_auto_recalc", "utc_date",
                "ascii", "create", "exists", "int1", "microsecond", "plugin", "reverse", "stats_persistent", "utc_time",
                "asensitive", "cross", "exit", "int2", "middleint", "plugins", "revoke", "stats_sample_pages", "utc_timestamp",
                "at", "cube", "expansion", "int3", "migrate", "plugin_dir", "right", "stop", "validation",
                "auto_increment", "current", "explain", "int4", "minute", "polygon", "rlike", "storage", "values",
                "avg", "current_date", "export", "int8", "minute_microsecond", "precedes", "rollback", "stored", "varbinary",
                "avg_row_length", "current_time", "extended", "integer", "minute_second", "precision", "rollup", "straight_join", "varchar",
                "backup", "current_timestamp", "extent_size", "interval", "minvalue", "prepare", "rotate", "string", "varcharacter",
                "before", "current_user", "FALSE", "into", "mod", "preserve", "routine", "subclass_origin", "variables",
                "begin", "cursor", "fast", "io", "modifies", "prev", "row", "subpartition", "varying",
                "between", "database", "faults", "io_after_gtids", "modify", "primary", "rows", "subpartitions", "view",
                "bigint", "databases", "fetch", "io_before_gtids", "month", "privileges", "row_count", "super", "virtual",
                "binary", "date", "fields", "io_thread", "multilinestring", "procedure", "row_format", "suspend", "wait",
                "binlog", "datetime", "filter", "ipc", "multipoint", "processlist", "rtree", "swaps", "warnings",
                "bit", "day", "first", "is", "multipolygon", "purge", "savepoint", "switches", "week",
                "blob", "deallocate", "fixed", "isolation", "mutex", "quarter", "schedule", "table", "weight_string",
                "block", "dec", "float", "issuer", "mysql_errno", "query", "schema", "tables", "when",
                "bool", "decimal", "float4", "iterate", "national", "range", "schemas", "tablespace", "where",
                "boolean", "declare", "float8", "join", "natural", "read", "second", "temporary", "while",
                "both", "default", "flush", "json", "nchar", "reads", "second_microsecond", "temptable", "with",
                "btree", "definer", "for", "key", "ndb", "read_only", "security", "terminated", "without",
                "by", "delayed", "force", "keys", "ndbcluster", "read_write", "select", "text", "work",
                "byte", "delete", "foreign", "kill", "never", "real", "sensitive", "than", "wrapper",
                "cache", "desc", "format", "leading", "new", "rebuild", "separator", "then", "write",
                "call", "describe", "found", "leave", "next", "recover", "serial", "time", "x509",
                "cascade", "deterministic", "from", "leaves", "no", "redofile", "serializable", "timestamp", "xa",
                "cascaded", "diagnostics", "full", "left", "nodegroup", "redo_buffer_size", "server", "timestampadd", "xml",
                "case", "directory", "fulltext", "less", "nonblocking", "redundant", "session", "timestampdiff", "xor",
                "chain", "disable", "function", "like", "none", "references", "set", "tinyblob", "year_month",
                "change", "discard", "general", "limit", "not", "regexp", "share", "tinyint", "zerofill",
                "changed", "disk", "generated", "linear", "no_wait", "relay", "show", "tinytext",
                "channel", "distinct", "geometry", "lines", "no_write_to_binlog", "relaylog", "shutdown", "to",
                "char", "distinctrow", "geometrycollection", "linestring", "null", "relay_log_file", "signal", "trailing",
                "character", "div", "get", "list", "number", "relay_log_pos", "signed", "transaction",
                "charset", "do", "grant", "load", "numeric", "relay_thread", "slave", "trigger",
                "check", "double", "grants", "local", "nvarchar", "release", "slow", "triggers",
                "checksum", "drop", "group", "localtime", "offset", "reload", "smallint", "TRUE",
                "cipher", "dual", "group_replication", "localtimestamp", "on", "remove", "snapshot", "truncate",
                "close", "dumpfile", "handler", "lock", "one", "rename", "some", "type",
                "coalesce", "duplicate", "having", "locks", "only", "reorganize", "soname", "types",
                "collate", "dynamic", "high_priority", "logfile", "open", "repair", "sounds", "uncommitted",
                "collation", "each", "host", "logs", "optimize", "repeat", "source", "undefined",
                "column", "else", "hosts", "long", "optimizer_costs", "repeatable", "spatial", "undo",
                "columns", "elseif", "hour", "longblob", "option", "replace", "specific", "undofile",
                "comment", "enable", "hour_microsecond", "longtext", "optionally", "replicate_do_db", "sql", "undo_buffer_size",
                "commit", "enclosed", "hour_minute", "loop", "options", "replicate_do_table", "sqlexception", "unicode",
                "committed", "encryption", "hour_second", "low_priority", "or", "replicate_ignore_db", "sqlstate", "uninstall"

        };
    }
}
