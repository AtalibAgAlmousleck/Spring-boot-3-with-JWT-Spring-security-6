package com.atalibdev.service;

import com.atalibdev.entities.User;
import com.atalibdev.request.ChangePasswordRequest;

import java.security.Principal;
import java.util.List;

public interface UserService {
    List<User> fetchUsers();
    void changePassword(ChangePasswordRequest request, Principal principal);
}
