package com.ozz.demo.commons.pool2.conn;

import lombok.extern.slf4j.Slf4j;

import java.sql.*;
import java.util.*;
import java.util.concurrent.Executor;

@Slf4j
public class MyProxyConn implements Connection {
    private Long id;
    protected Connection realConn;
    protected MyGenericObjectPool<MyProxyConn> pool;
    protected boolean isClosed;
    private final Set<Statement> openStatements = Collections.synchronizedSet(new HashSet<>());

    public MyProxyConn(Connection realConn, Long id) {
        this.realConn = realConn;
    }

    private void trackStatement(Statement stmt) {
        openStatements.add(stmt);
    }

    public void closeAllStatements() {
        synchronized (openStatements) {
            for (Statement stmt : openStatements) {
                try {
                    if (!stmt.isClosed()) {
                        stmt.close();
                    }
                } catch (SQLException e) {
                    // 记录日志但继续处理其他statement
                    log.error("closeAllStatements fail", e);
                }
            }
            openStatements.clear();
        }
    }

    @Override
    public String toString() {
        return "MyProxyConn{id=" + id + '}';
    }

    @Override
    public Statement createStatement() throws SQLException {
        Statement stmt = realConn.createStatement();
        trackStatement(stmt);
        return stmt;
    }

    @Override
    public PreparedStatement prepareStatement(String sql) throws SQLException {
        PreparedStatement stmt = realConn.prepareStatement(sql);
        trackStatement(stmt);
        return stmt;
    }

    @Override
    public CallableStatement prepareCall(String sql) throws SQLException {
        CallableStatement stmt = realConn.prepareCall(sql);
        trackStatement(stmt);
        return stmt;
    }

    @Override
    public String nativeSQL(String sql) throws SQLException {
        return realConn.nativeSQL(sql);
    }

    @Override
    public void setAutoCommit(boolean autoCommit) throws SQLException {
        realConn.setAutoCommit(autoCommit);
    }

    @Override
    public boolean getAutoCommit() throws SQLException {
        return realConn.getAutoCommit();
    }

    @Override
    public void commit() throws SQLException {
        realConn.commit();
    }

    @Override
    public void rollback() throws SQLException {
        realConn.rollback();
    }

    @Override
    public void close() throws SQLException {
        closeAllStatements(); // 在返回连接池前关闭所有statement
        pool.returnObject(this);
        this.isClosed = true;
    }

    public void realClose() throws SQLException {
        realConn.close();
    }

    @Override
    public boolean isClosed() throws SQLException {
        return this.isClosed;
    }

    public boolean realIsClosed() throws SQLException {
        return realConn.isClosed();
    }

    @Override
    public DatabaseMetaData getMetaData() throws SQLException {
        return realConn.getMetaData();
    }

    @Override
    public void setReadOnly(boolean readOnly) throws SQLException {
        realConn.setReadOnly(readOnly);
    }

    @Override
    public boolean isReadOnly() throws SQLException {
        return realConn.isReadOnly();
    }

    @Override
    public void setCatalog(String catalog) throws SQLException {
        realConn.setCatalog(catalog);
    }

    @Override
    public String getCatalog() throws SQLException {
        return realConn.getCatalog();
    }

    @Override
    public void setTransactionIsolation(int level) throws SQLException {
        realConn.setTransactionIsolation(level);
    }

    @Override
    public int getTransactionIsolation() throws SQLException {
        return realConn.getTransactionIsolation();
    }

    @Override
    public SQLWarning getWarnings() throws SQLException {
        return realConn.getWarnings();
    }

    @Override
    public void clearWarnings() throws SQLException {
        realConn.clearWarnings();
    }

    @Override
    public Statement createStatement(int resultSetType, int resultSetConcurrency) throws SQLException {
        Statement stmt = realConn.createStatement(resultSetType, resultSetConcurrency);
        trackStatement(stmt);
        return stmt;
    }

    @Override
    public PreparedStatement prepareStatement(String sql, int resultSetType, int resultSetConcurrency) throws SQLException {
        PreparedStatement stmt = realConn.prepareStatement(sql, resultSetType, resultSetConcurrency);
        trackStatement(stmt);
        return stmt;
    }

    @Override
    public CallableStatement prepareCall(String sql, int resultSetType, int resultSetConcurrency) throws SQLException {
        CallableStatement stmt = realConn.prepareCall(sql, resultSetType, resultSetConcurrency);
        trackStatement(stmt);
        return stmt;
    }

    @Override
    public Map<String, Class<?>> getTypeMap() throws SQLException {
        return realConn.getTypeMap();
    }

    @Override
    public void setTypeMap(Map<String, Class<?>> map) throws SQLException {
        realConn.setTypeMap(map);
    }

    @Override
    public void setHoldability(int holdability) throws SQLException {
        realConn.setHoldability(holdability);
    }

    @Override
    public int getHoldability() throws SQLException {
        return realConn.getHoldability();
    }

    @Override
    public Savepoint setSavepoint() throws SQLException {
        return realConn.setSavepoint();
    }

    @Override
    public Savepoint setSavepoint(String name) throws SQLException {
        return realConn.setSavepoint(name);
    }

    @Override
    public void rollback(Savepoint savepoint) throws SQLException {
        realConn.rollback(savepoint);
    }

    @Override
    public void releaseSavepoint(Savepoint savepoint) throws SQLException {
        realConn.releaseSavepoint(savepoint);
    }

    @Override
    public Statement createStatement(int resultSetType, int resultSetConcurrency, int resultSetHoldability) throws SQLException {
        Statement stmt = realConn.createStatement(resultSetType, resultSetConcurrency, resultSetHoldability);
        trackStatement(stmt);
        return stmt;
    }

    @Override
    public PreparedStatement prepareStatement(String sql, int resultSetType, int resultSetConcurrency, int resultSetHoldability) throws SQLException {
        PreparedStatement stmt = realConn.prepareStatement(sql, resultSetType, resultSetConcurrency, resultSetHoldability);
        trackStatement(stmt);
        return stmt;
    }

    @Override
    public CallableStatement prepareCall(String sql, int resultSetType, int resultSetConcurrency, int resultSetHoldability) throws SQLException {
        CallableStatement stmt = realConn.prepareCall(sql, resultSetType, resultSetConcurrency, resultSetHoldability);
        trackStatement(stmt);
        return stmt;
    }

    @Override
    public PreparedStatement prepareStatement(String sql, int autoGeneratedKeys) throws SQLException {
        PreparedStatement stmt = realConn.prepareStatement(sql, autoGeneratedKeys);
        trackStatement(stmt);
        return stmt;
    }

    @Override
    public PreparedStatement prepareStatement(String sql, int[] columnIndexes) throws SQLException {
        PreparedStatement stmt = realConn.prepareStatement(sql, columnIndexes);
        trackStatement(stmt);
        return stmt;
    }

    @Override
    public PreparedStatement prepareStatement(String sql, String[] columnNames) throws SQLException {
        PreparedStatement stmt = realConn.prepareStatement(sql, columnNames);
        trackStatement(stmt);
        return stmt;
    }

    @Override
    public Clob createClob() throws SQLException {
        return realConn.createClob();
    }

    @Override
    public Blob createBlob() throws SQLException {
        return realConn.createBlob();
    }

    @Override
    public NClob createNClob() throws SQLException {
        return realConn.createNClob();
    }

    @Override
    public SQLXML createSQLXML() throws SQLException {
        return realConn.createSQLXML();
    }

    @Override
    public boolean isValid(int timeout) throws SQLException {
        return realConn.isValid(timeout);
    }

    @Override
    public void setClientInfo(String name, String value) throws SQLClientInfoException {
        realConn.setClientInfo(name, value);
    }

    @Override
    public void setClientInfo(Properties properties) throws SQLClientInfoException {
        realConn.setClientInfo(properties);
    }

    @Override
    public String getClientInfo(String name) throws SQLException {
        return realConn.getClientInfo(name);
    }

    @Override
    public Properties getClientInfo() throws SQLException {
        return realConn.getClientInfo();
    }

    @Override
    public Array createArrayOf(String typeName, Object[] elements) throws SQLException {
        return realConn.createArrayOf(typeName, elements);
    }

    @Override
    public Struct createStruct(String typeName, Object[] attributes) throws SQLException {
        return realConn.createStruct(typeName, attributes);
    }

    @Override
    public void setSchema(String schema) throws SQLException {
        realConn.setSchema(schema);
    }

    @Override
    public String getSchema() throws SQLException {
        return realConn.getSchema();
    }

    @Override
    public void abort(Executor executor) throws SQLException {
        realConn.abort(executor);
    }

    @Override
    public void setNetworkTimeout(Executor executor, int milliseconds) throws SQLException {
        realConn.setNetworkTimeout(executor, milliseconds);
    }

    @Override
    public int getNetworkTimeout() throws SQLException {
        return realConn.getNetworkTimeout();
    }

    @Override
    public <T> T unwrap(Class<T> iface) throws SQLException {
        return realConn.unwrap(iface);
    }

    @Override
    public boolean isWrapperFor(Class<?> iface) throws SQLException {
        return realConn.isWrapperFor(iface);
    }

}
