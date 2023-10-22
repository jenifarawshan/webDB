package utils;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;

public class DB {

    private static final String JDBC_URL = "jdbc:sqlite:sqlite.db";

    public static Connection getConnection() throws SQLException, ClassNotFoundException {
        Class.forName("org.sqlite.JDBC");
        return DriverManager.getConnection(JDBC_URL);
    }
}

