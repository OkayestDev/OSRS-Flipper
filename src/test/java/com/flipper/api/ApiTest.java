package com.flipper.api;

import static org.junit.Assert.assertNotNull;

import com.flipper.responses.LoginResponse;

import org.junit.Test;

public class ApiTest {
    @Test
    public void testGet() {
        LoginResponse loginResponse = UserApi.login("test@gmail.com", "test");
        assertNotNull(loginResponse);
        assertNotNull(loginResponse.user);
    }
}
