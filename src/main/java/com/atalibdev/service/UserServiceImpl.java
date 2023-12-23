package com.atalibdev.service;

import com.atalibdev.entities.User;
import com.atalibdev.repository.UserRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
@RequiredArgsConstructor
public class UserServiceImpl implements UserService {

    private final UserRepository userRepository;

    @Override
    public List<User> fetchUsers() {
        return userRepository.findAll();
    }
}
