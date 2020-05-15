package com.ozz.demo.db.mysql;

import java.sql.Connection;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;

/**
 * 取自spring-jdbc.jar包中org.springframework.jdbc.support.incrementer.MySQLMaxValueIncrementer
 */
public class MySQLMaxValueIncrementer {
  public synchronized long getNextKey(Connection con, String tableName, String columnName) throws SQLException {
      try(Statement stmt = con.createStatement()) {
        stmt.setQueryTimeout(5);
        // Increment the sequence column...
        stmt.executeUpdate("update "+ tableName + " set " + columnName + " = last_insert_id(" + columnName + "+1)");
        
        // Retrieve the new max of the sequence column...
        try(ResultSet rs = stmt.executeQuery("select last_insert_id()")) {
          if (!rs.next()) {
            throw new RuntimeException("last_insert_id() failed after executing an update");
          }
          return rs.getLong(1);
        }
      }
  }
}
