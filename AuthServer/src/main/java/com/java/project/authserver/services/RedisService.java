package com.java.project.authserver.services;

public interface RedisService {

    void saveValue(String key, String value);

    String getValue(String key);

}
