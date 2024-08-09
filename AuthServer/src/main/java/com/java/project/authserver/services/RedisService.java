package com.java.project.authserver.services;

public interface RedisService {

    void storeToken(String username, String token);
    String getToken(String username);
    void deleteToken(String username);

}
