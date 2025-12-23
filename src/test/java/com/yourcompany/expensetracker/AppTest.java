package com.emsi.expensetracker;

import com.emsi.expensetracker.service.AuthService;
import com.emsi.expensetracker.util.DatabaseConnection;
import junit.framework.Test;
import junit.framework.TestCase;
import junit.framework.TestSuite;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;

/**
 * Unit test for Expense Tracker functionalities.
 */

public class AppTest 
    extends TestCase
{
    /**
     * Create the test case
     *
     * @param testName name of the test case
     */
    public AppTest( String testName )
    {
        super( testName );
    }

    /**
     * @return the suite of tests being tested
     */
    public static Test suite()
    {
        return new TestSuite( AppTest.class );
    }

    /**
     * Rigourous Test :-)
     */
    public void testApp()
    {
        assertTrue( true );
    }

    public void testRegisterUser() {
        boolean result = AuthService.register("testUser", "password123", "test@example.com");
        assertTrue("User registration failed", result);
    }

    public void testLoginUser() {
        AuthService.register("testLogin", "password123", "login@example.com");
        boolean result = AuthService.login("testLogin", "password123");
        assertTrue("User login failed", result);
    }

    public void testInvalidLogin() {
        boolean result = AuthService.login("nonexistentUser", "wrongPassword");
        assertFalse("Invalid login should fail", result);
    }

    public void testDatabaseInitialization() {
        DatabaseConnection.initialize();
        try (Connection conn = DatabaseConnection.getConnection();
             PreparedStatement stmt = conn.prepareStatement("SELECT name FROM sqlite_master WHERE type='table' AND name='users'");
             ResultSet rs = stmt.executeQuery()) {
            assertTrue("Users table does not exist", rs.next());
        } catch (Exception e) {
            fail("Database initialization test failed: " + e.getMessage());
        }
    }


}
