package com.atalibdev.service;

import com.atalibdev.authentication.AuthenticationRequest;
import com.atalibdev.authentication.AuthenticationResponse;
import com.atalibdev.authentication.RegistrationRequest;

public interface AuthenticationService {
    AuthenticationResponse register(RegistrationRequest request);
    AuthenticationResponse authentication(AuthenticationRequest request);
}
