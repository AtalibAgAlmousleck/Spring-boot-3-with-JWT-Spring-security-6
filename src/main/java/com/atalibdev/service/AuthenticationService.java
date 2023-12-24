package com.atalibdev.service;

import com.atalibdev.request.AuthenticationRequest;
import com.atalibdev.request.AuthenticationResponse;
import com.atalibdev.request.RegistrationRequest;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;

import java.io.IOException;

public interface AuthenticationService {
    AuthenticationResponse register(RegistrationRequest request);
    AuthenticationResponse authentication(AuthenticationRequest request);
    void refreshToken(HttpServletRequest request, HttpServletResponse response) throws IOException;
}
