package com.atalibdev.service;

import com.atalibdev.authentication.AuthenticationRequest;
import com.atalibdev.authentication.AuthenticationResponse;
import com.atalibdev.authentication.RegistrationRequest;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;

import java.io.IOException;

public interface AuthenticationService {
    AuthenticationResponse register(RegistrationRequest request);
    AuthenticationResponse authentication(AuthenticationRequest request);
    void refreshToken(HttpServletRequest request, HttpServletResponse response) throws IOException;
}
