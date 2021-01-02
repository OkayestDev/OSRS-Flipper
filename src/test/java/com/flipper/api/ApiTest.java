package com.flipper.api;

import static org.junit.Assert.assertNotNull;

import org.junit.Test;

public class ApiTest {
    @Test
    public void testGet() {
        String loginResponse = UserApi.login("test@gmail.com", "test");
        assertNotNull(loginResponse);
    }
}
