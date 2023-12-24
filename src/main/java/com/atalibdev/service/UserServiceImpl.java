package com.atalibdev.service;

import com.atalibdev.entities.User;
import com.atalibdev.repository.UserRepository;
import com.atalibdev.request.ChangePasswordRequest;
import lombok.RequiredArgsConstructor;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import java.security.Principal;
import java.util.List;

@Service
@RequiredArgsConstructor
public class UserServiceImpl implements UserService {

    private final UserRepository userRepository;
    private final PasswordEncoder passwordEncoder;

    @Override
    public List<User> fetchUsers() {
        return userRepository.findAll();
    }

    @Override
    public void changePassword(ChangePasswordRequest request, Principal principal) {
        var user = (User) ((UsernamePasswordAuthenticationToken) principal).getPrincipal();

        // check if the current password is correct
        if (!passwordEncoder.matches(request.getCurrentPassword(), user.getPassword()))
            throw new IllegalStateException("Wrong password");
        // check if the two new password are equals
        if (!request.getNewPassword().equals(request.getConfirmationPassword()))
            throw new IllegalStateException("Password are not match");
        // update the password
        user.setPassword(passwordEncoder.encode(request.getNewPassword()));
        userRepository.save(user);
    }
}
