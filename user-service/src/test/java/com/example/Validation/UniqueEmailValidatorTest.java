package com.example.Validation;

import com.example.dbconnect.DbConnect;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.MockedStatic;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;

import static org.assertj.core.api.Assertions.assertThat;
import static org.mockito.Mockito.*;

class UniqueEmailValidatorTest {

    private UniqueEmailValidator validator;

    @BeforeEach
    void setUp() {
        validator = new UniqueEmailValidator();
    }

    @Test
    void shouldReturnFalseWhenEmailAlreadyExists() throws Exception {
        Connection connection = mock(Connection.class);
        PreparedStatement ps = mock(PreparedStatement.class);
        ResultSet rs = mock(ResultSet.class);

        when(connection.prepareStatement(anyString())).thenReturn(ps);
        when(ps.executeQuery()).thenReturn(rs);
        when(rs.next()).thenReturn(true); // simulate row exists

        try (MockedStatic<DbConnect> mocked = mockStatic(DbConnect.class)) {
            mocked.when(DbConnect::getConnection).thenReturn(connection);

            boolean result = validator.isValid("existing@example.com", null);
            assertThat(result).isFalse();
        }
    }

    @Test
    void shouldReturnTrueWhenEmailDoesNotExist() throws Exception {
        Connection connection = mock(Connection.class);
        PreparedStatement ps = mock(PreparedStatement.class);
        ResultSet rs = mock(ResultSet.class);

        when(connection.prepareStatement(anyString())).thenReturn(ps);
        when(ps.executeQuery()).thenReturn(rs);
        when(rs.next()).thenReturn(false); // simulate no rows

        try (MockedStatic<DbConnect> mocked = mockStatic(DbConnect.class)) {
            mocked.when(DbConnect::getConnection).thenReturn(connection);

            boolean result = validator.isValid("new@example.com", null);
            assertThat(result).isTrue();
        }
    }

    @Test
    void shouldReturnTrueWhenEmailIsNullOrEmpty() {
        assertThat(validator.isValid(null, null)).isTrue();
        assertThat(validator.isValid("", null)).isTrue();
        assertThat(validator.isValid("   ", null)).isTrue();
    }
}
