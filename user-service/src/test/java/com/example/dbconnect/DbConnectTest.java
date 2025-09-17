package com.example.dbconnect;

import org.junit.jupiter.api.Test;

import java.sql.Connection;

import static org.junit.jupiter.api.Assertions.*;

class DbConnectTest {

    @Test
    void testGetConnectionReturnsValidConnection() throws Exception {
        // Act
        Connection connection = DbConnect.getConnection();

        // Assert
        assertNotNull(connection, "Connection should not be null");
        assertTrue(connection.isValid(2), "Connection should be valid");
    }

    @Test
    void testGetConnectionIsSingleton() throws Exception {
        // Act
        Connection connection1 = DbConnect.getConnection();
        Connection connection2 = DbConnect.getConnection();

        // Assert
        assertSame(connection1, connection2, "DbConnect should return the same connection instance");
    }
}
