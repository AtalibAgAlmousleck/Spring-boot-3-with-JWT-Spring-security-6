package com.atalibdev.service;

import com.atalibdev.entities.User;

import java.util.List;

public interface UserService {
    List<User> fetchUsers();
}
