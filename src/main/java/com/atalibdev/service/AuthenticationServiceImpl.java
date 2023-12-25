package com.atalibdev.service;

import com.atalibdev.request.AuthenticationRequest;
import com.atalibdev.request.AuthenticationResponse;
import com.atalibdev.request.RegistrationRequest;
import com.atalibdev.entities.User;
import com.atalibdev.jwt.JwtService;
import com.atalibdev.repository.UserRepository;
import com.atalibdev.request.VerificationRequest;
import com.atalibdev.tfa.TwoFactorAuthenticationService;
import com.atalibdev.token.Token;
import com.atalibdev.token.TokenRepository;
import com.atalibdev.token.TokenType;
import com.fasterxml.jackson.databind.ObjectMapper;
import jakarta.persistence.EntityNotFoundException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpHeaders;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.BadCredentialsException;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import java.io.IOException;

@Service
@RequiredArgsConstructor
public class AuthenticationServiceImpl implements AuthenticationService {

    private final UserRepository userRepository;
    private final TokenRepository tokenRepository;
    private final PasswordEncoder passwordEncoder;
    private final JwtService jwtService;
    private final AuthenticationManager authenticationManager;
    private final TwoFactorAuthenticationService twoFactorAuthenticationService;

    @Override
    public AuthenticationResponse register(RegistrationRequest request) {
        var user = User.builder()
                .firstname(request.getFirstname())
                .lastname(request.getLastname())
                .email(request.getEmail())
                .password(passwordEncoder.encode(request.getPassword()))
                .role(request.getRole())
                .tfEnabled(request.isTfEnabled())
                .build();

        //todo: if TFA enable then generate a secret
        if (request.isTfEnabled())
            user.setSecret(twoFactorAuthenticationService.generateTheNewSecrete());
        var savedUser = userRepository.save(user);
        var jwtToken = jwtService.generateToken(user);
        var refreshToken = jwtService.generateFreshToken(user);
        userToken(savedUser, jwtToken); // saveUserToken
        return AuthenticationResponse.builder()
                .secretImageUrl(twoFactorAuthenticationService
                        .generateQRCodeImageUrl(user.getSecret()))
                .accessToken(jwtToken)
                .refreshToken(refreshToken)
                .tfEnabled(user.isTfEnabled())
                .build();
    }

    @Override
    public AuthenticationResponse authentication(AuthenticationRequest request) {
        authenticationManager.authenticate(
                new UsernamePasswordAuthenticationToken(
                        request.getEmail(), request.getPassword())
        );
        var user = userRepository.findByEmail(request.getEmail())
                .orElseThrow();
        //todo enable tfa
        if (user.isTfEnabled()) {
            return AuthenticationResponse.builder()
                    .accessToken("")
                    .refreshToken("")
                    .tfEnabled(true)
                    .build();
        }
        var jwtToken = jwtService.generateToken(user);
        var refreshToken = jwtService.generateFreshToken(user);
        userWithOneToken(user); // revoke token
        userToken(user, jwtToken);
        return AuthenticationResponse.builder()
                .accessToken(jwtToken)
                .refreshToken(refreshToken)
                .tfEnabled(false)
                .build();
    }

    @Override
    public void refreshToken(HttpServletRequest request, HttpServletResponse response) throws IOException {
        final String authHeader = request.getHeader(HttpHeaders.AUTHORIZATION);
        final String refreshToken;
        final String userEmail;
        if (authHeader == null || !authHeader.startsWith("Bearer "))
            return;
        refreshToken = authHeader.substring(7);
        userEmail = jwtService.extractUsername(refreshToken);
        if (userEmail != null) {
            var user = this.userRepository.findByEmail(userEmail).orElseThrow();
            //userWithOneToken(user); // revoked user
            if (jwtService.isTokenValid(refreshToken, user)) {
                var accessToken = jwtService.generateToken(user);
                userWithOneToken(user);
                userToken(user, accessToken);
                var authResponse = AuthenticationResponse.builder()
                        .accessToken(accessToken)
                        .refreshToken(refreshToken)
                        .tfEnabled(false)
                        .build();
                new ObjectMapper().writeValue(response.getOutputStream(), authResponse);
            }
        }

    }

    @Override
    public AuthenticationResponse verifyCode(VerificationRequest verificationRequest) {
        User user = userRepository.findByEmail(verificationRequest.getEmail())
                .orElseThrow(() -> new EntityNotFoundException(
                        String.format("User not found %s", verificationRequest.getEmail())
                ));
        if (twoFactorAuthenticationService.isOptNotValid(user.getSecret(), verificationRequest.getCode()))
            throw new BadCredentialsException("Code is not correct");
        var jwtToken = jwtService.generateToken(user);
        return AuthenticationResponse.builder()
                .accessToken(jwtToken)
                .tfEnabled(user.isTfEnabled())
                .build();
    }

    private void userWithOneToken(User user) {
        var validTokenOfUser = tokenRepository.findAllValidTokenByUser(user.getId());
        if (validTokenOfUser.isEmpty())
            return;
        validTokenOfUser.forEach(token -> {
            token.setExpired(true);
            token.setRevoked(true);
        });
        tokenRepository.saveAll(validTokenOfUser);
    }

    private void userToken(User user, String jwtToken) {
        var token = Token.builder()
                .user(user)
                .token(jwtToken)
                .tokenType(TokenType.BEARER)
                .revoked(false)
                .expired(false)
                .build();
        tokenRepository.save(token);
    }
}
