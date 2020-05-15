package com.ozz.demo.db.jdbc;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.SQLException;
import java.text.ParseException;
import java.util.Map;

import com.ozz.demo.db.jdbc.base.ColumnInfo;
import com.ozz.demo.db.jdbc.base.JdbcOracleBatchUtil;
import com.ozz.demo.db.jdbc.base.JdbcSqlserverBatchUtil;

public interface JdbcBatchUtil {

  public default JdbcBatchUtil getOracleInstance() {
    return new JdbcOracleBatchUtil();
  }

  public default JdbcBatchUtil getSqlserverInstance() {
    return new JdbcSqlserverBatchUtil();
  }

  public Map<String, ColumnInfo> queryColumnInfo(Connection conn, String tableName) throws SQLException;

  public void setParameter(PreparedStatement ps, int parameterIndex, String value, ColumnInfo colInfo) throws SQLException, ParseException;

}
