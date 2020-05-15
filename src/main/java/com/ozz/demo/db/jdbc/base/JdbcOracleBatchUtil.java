package com.ozz.demo.db.jdbc.base;

import com.ozz.demo.date.DateUtil;
import com.ozz.demo.db.jdbc.JdbcBatchUtil;
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

public class JdbcOracleBatchUtil implements JdbcBatchUtil {
  @Override
  public Map<String, ColumnInfo> queryColumnInfo(Connection conn, String tableName) throws SQLException {
    String sql = "select cname,coltype,width from col where tname=upper('" + tableName + "')";
    try (Statement s = conn.createStatement(); ResultSet rs = s.executeQuery(sql)) {
      Map<String, ColumnInfo> typeMap = new HashMap<String, ColumnInfo>();
      while (rs.next()) {
        ColumnInfo dataType = new ColumnInfo();
        dataType.setDataType(rs.getString(2));
        dataType.setDataLength(rs.getInt(3));
        typeMap.put(rs.getString(1), dataType);
      }
      return typeMap;
    }
  }

  @Override
  public void setParameter(PreparedStatement ps, int parameterIndex, String value, ColumnInfo colInfo) throws SQLException, ParseException {
    if ("VARCHAR2".equals(colInfo.getDataType()) || "NVARCHAR2".equals(colInfo.getDataType()) || "NCLOB".equals(colInfo.getDataType())) {
      ps.setString(parameterIndex, value);
    } else if ("NUMBER".equals(colInfo.getDataType())) {
      if (StringUtils.isEmpty(value)) {
        ps.setNull(parameterIndex, Types.NUMERIC);
      } else {
        if (colInfo.getDataLength() == 10) {
          ps.setInt(parameterIndex, Integer.valueOf(value));
        } else if (colInfo.getDataLength() == 22) {
          ps.setLong(parameterIndex, Long.valueOf(value));
        } else if (colInfo.getDataLength() == 5) {
          ps.setShort(parameterIndex, Short.valueOf(value));
        } else {
          ps.setDouble(parameterIndex, Double.valueOf(value));
        }
      }
    } else if ("DATE".equals(colInfo.getDataType())) {
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
