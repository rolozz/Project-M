package com.java.project.authserver.services;

import com.java.project.authserver.entities.Role;

public interface RoleService {

    Role findByName(String name);

}
