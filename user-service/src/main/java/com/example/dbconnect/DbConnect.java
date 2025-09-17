package com.example.dbconnect;

import java.sql.Connection;
import java.sql.DriverManager;

public class DbConnect {
    public static Connection connection = null;

    public static Connection getConnection() throws Exception {
        if (connection == null) {
            Class.forName("com.mysql.cj.jdbc.Driver");
            connection = DriverManager.getConnection("jdbc:mysql://localhost:3306/getemployed", "root", "pass@word1");
        }
        return connection;
    }
}
