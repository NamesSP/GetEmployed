package com.example.Validation;
import com.example.dbconnect.DbConnect;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.MockedStatic;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

class UniqueUserNameValidatorTest {

    private UniqueUserNameValidator validator;

    @BeforeEach
    void setUp() {
        validator = new UniqueUserNameValidator();
    }

    @Test
    void testValidWhenUserNameIsNull() {
        assertTrue(validator.isValid(null, null));
    }

    @Test
    void testValidWhenUserNameIsEmpty() {
        assertTrue(validator.isValid("   ", null));
    }

    @Test
    void testValidWhenUserNameDoesNotExistInDB() throws Exception {
        try (MockedStatic<DbConnect> dbMock = mockStatic(DbConnect.class)) {
            Connection conn = mock(Connection.class);
            PreparedStatement ps = mock(PreparedStatement.class);
            ResultSet rs = mock(ResultSet.class);

            dbMock.when(DbConnect::getConnection).thenReturn(conn);
            when(conn.prepareStatement("Select * from user where user_name=?")).thenReturn(ps);
            when(ps.executeQuery()).thenReturn(rs);
            when(rs.next()).thenReturn(false); // No user found

            boolean result = validator.isValid("newuser", null);

            assertTrue(result);
            verify(ps).setString(1, "newuser");
        }
    }

    @Test
    void testInvalidWhenUserNameExistsInDB() throws Exception {
        try (MockedStatic<DbConnect> dbMock = mockStatic(DbConnect.class)) {
            Connection conn = mock(Connection.class);
            PreparedStatement ps = mock(PreparedStatement.class);
            ResultSet rs = mock(ResultSet.class);

            dbMock.when(DbConnect::getConnection).thenReturn(conn);
            when(conn.prepareStatement("Select * from user where user_name=?")).thenReturn(ps);
            when(ps.executeQuery()).thenReturn(rs);
            when(rs.next()).thenReturn(true); // User already exists

            boolean result = validator.isValid("existinguser", null);

            assertFalse(result);
            verify(ps).setString(1, "existinguser");
        }
    }

    @Test
    void testValidWhenSQLExceptionOccurs() throws Exception {
        try (MockedStatic<DbConnect> dbMock = mockStatic(DbConnect.class)) {
            Connection conn = mock(Connection.class);

            dbMock.when(DbConnect::getConnection).thenReturn(conn);
            when(conn.prepareStatement(anyString())).thenThrow(new RuntimeException("DB error"));

            boolean result = validator.isValid("user123", null);

            assertTrue(result); // Fallback to true on exception
        }
    }
}
