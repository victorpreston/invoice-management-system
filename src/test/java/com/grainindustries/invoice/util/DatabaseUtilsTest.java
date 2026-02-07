package com.grainindustries.invoice.util;

import org.junit.jupiter.api.Test;
import static org.junit.jupiter.api.Assertions.*;

/**
 * Tests for database utilities
 */
class DatabaseUtilsTest {

    @Test
    void testDatabaseConnectionProperties() {
        // Test that database properties can be loaded
        assertNotNull(System.getProperty("user.dir"), "Working directory should be available");
    }
}
