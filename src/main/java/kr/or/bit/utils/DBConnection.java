package kr.or.bit.utils;

import java.sql.Connection;
import java.sql.SQLException;
import javax.naming.Context;
import javax.naming.InitialContext;
import javax.naming.NamingException;
import javax.sql.DataSource;

public class DBConnection {
    private static DataSource ds = null;

    static {
        try {
            Context context = new InitialContext();
            ds = (DataSource) context.lookup("java:comp/env/jdbc/oracle");
        } catch (NamingException e) {
            e.printStackTrace();
        }
    }

    public static Connection getConnection() throws SQLException {
        if (ds == null) {
            throw new SQLException("DataSource not initialized");
        }
        return ds.getConnection();
    }
}
