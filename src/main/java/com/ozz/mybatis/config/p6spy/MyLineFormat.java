package com.ozz.mybatis.config.p6spy;

import cn.hutool.core.exceptions.ExceptionUtil;
import com.p6spy.engine.spy.appender.CustomLineFormat;
import lombok.extern.slf4j.Slf4j;

@Slf4j
public class MyLineFormat extends CustomLineFormat {
    @Override
    public String formatMessage(int connectionId, String now, long elapsed, String category, String prepared, String sql, String url) {
        if(sql.contains("from job") || (sql.contains("'jobLimit'")&&sql.contains("t_dict"))) {
            sql = sql.replaceAll("\\s{2,}", " ");
        }
        if(sql.contains("hr-kq.")) {
            throw ExceptionUtil.wrapRuntime("不可包含数据库名hr-kq.");
        }

        sql = sql.replaceAll("(\\s*\n){2,}", "\n");
        return super.formatMessage(connectionId, now, elapsed, category, prepared, sql, url);
    }
}
