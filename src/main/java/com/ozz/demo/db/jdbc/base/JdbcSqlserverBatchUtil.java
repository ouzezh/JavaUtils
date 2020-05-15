package com.ozz.demo.db.jdbc.base;

import com.ozz.demo.date.DateUtil;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.sql.Types;
import java.text.ParseException;
import java.util.Date;
import java.util.HashMap;
import java.util.Map;

import org.apache.commons.lang3.StringUtils;

import com.ozz.demo.db.jdbc.JdbcBatchUtil;

public class JdbcSqlserverBatchUtil implements JdbcBatchUtil {
  @Override
  public Map<String, ColumnInfo> queryColumnInfo(Connection conn, String tableName) throws SQLException {
    String sql = "SELECT name,type_name(user_type_id) as [type_name],max_length FROM sys.columns where [object_id] = object_id('" + tableName + "')";
    try (Statement s = conn.createStatement(); ResultSet rs = s.executeQuery(sql)) {
      Map<String, ColumnInfo> map = new HashMap<String, ColumnInfo>();
      while (rs.next()) {
        ColumnInfo dataType = new ColumnInfo();
        dataType.setDataType(rs.getString(2));
        dataType.setDataLength(rs.getInt(3));
        map.put(rs.getString(1), dataType);
      }
      return map;
    }
  }

  @Override
  public void setParameter(PreparedStatement ps, int parameterIndex, String value, ColumnInfo colInfo) throws SQLException, ParseException {
    if ("nvarchar".equals(colInfo.getDataType()) || "varchar".equals(colInfo.getDataType())) {
      ps.setString(parameterIndex, value);
    } else if ("int".equals(colInfo.getDataType())) {
      if (StringUtils.isEmpty(value)) {
        ps.setNull(parameterIndex, Types.INTEGER);
      } else {
        ps.setInt(parameterIndex, Integer.valueOf(value));
      }
    } else if ("smallint".equals(colInfo.getDataType())) {
      if (StringUtils.isEmpty(value)) {
        ps.setNull(parameterIndex, Types.SMALLINT);
      } else {
        ps.setShort(parameterIndex, Short.valueOf(value));
      }
    } else if ("datetime".equals(colInfo.getDataType())) {
      if (StringUtils.isEmpty(value)) {
        ps.setNull(parameterIndex, Types.DATE);
      } else {
        Date date = DateUtil.parseDate(value);
        ps.setDate(parameterIndex, new java.sql.Date(date.getTime()));
      }
    } else {
      throw new RuntimeException("新的类型，请联系开发人员：" + colInfo.getDataType());
    }
  }

}
