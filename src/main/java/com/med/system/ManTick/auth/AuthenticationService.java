
package com.med.system.ManTick.auth;

import java.io.IOException;
import java.util.List;

import org.springframework.http.HttpHeaders;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.med.system.ManTick.JWT.JwtService;
import com.med.system.ManTick.Users.Role;
import com.med.system.ManTick.Users.User;
import com.med.system.ManTick.Users.UserRepository;
import com.med.system.ManTick.token.Token;
import com.med.system.ManTick.token.TokenRepository;
import com.med.system.ManTick.token.TokenType;

import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import lombok.RequiredArgsConstructor;



@Service
@RequiredArgsConstructor
public class AuthenticationService {

    private final UserRepository repository;
    private final TokenRepository tokenRepository;
    private final PasswordEncoder passwordEncoder;
    private final JwtService jwtService;
    private final AuthenticationManager authenticationManager;

    @Transactional
    public AuthenticationResponse register(RegisterRequest request) {
        User user = User.builder()
            .firstname(request.getFirstname())
            .lastname(request.getLastname())
            .email(request.getEmail())
            .password(passwordEncoder.encode(request.getPassword()))
            .role(request.getRole())
            .build();

        User findUser = repository.findByEmail(request.getEmail()).orElse(null);

        if (findUser != null) {
          return AuthenticationResponse.builder()
          .accessToken("")
          .refreshToken("")
          .message("This email already exists")
          .success(false)
          .build();
        }
        User saveUser = repository.save(user);

        return AuthenticationResponse.builder()
            .accessToken("jwtToken")
            .refreshToken("refreshToken")
            .message("Successfully registered")
            .success(true)
            .build();
    }


    @Transactional
    public AuthenticationResponse authenticate(AuthenticationRequest request) {
        authenticationManager.authenticate(
            new UsernamePasswordAuthenticationToken(
                request.getEmail(),
                request.getPassword()
        ));

        User user = repository.findByEmail(request.getEmail())
            .orElseThrow(() -> new RuntimeException("User not found with email: " + request.getEmail()));

            
        String jwtToken = jwtService.generateToken(user);
        String refreshToken = jwtService.generateRefreshToken(user);

        revokeAllUserTokens(user);
        saveUserToken(user, jwtToken);

        return AuthenticationResponse.builder()
            .accessToken(jwtToken)
            .refreshToken(refreshToken)
            .message("Successfully logged in")
            .success(true)
            .build();
    }


    @Transactional
    private void revokeAllUserTokens(User user) {
        List<Token> validUserTokens = tokenRepository.findAllValidTokenByUser(user.getId());
        if (validUserTokens.isEmpty()) return;
        validUserTokens.forEach(t -> {
            t.setExpired(true);
            t.setRevoked(true);
        });
        tokenRepository.saveAll(validUserTokens);
    }


    @Transactional
    private void saveUserToken( User user, String jwtToken) {
        Token token = Token.builder()
            .user(user)
            .token(jwtToken)
            .tokenType(TokenType.BEARER)
            .revoked(false)
            .expired(false)
            .build();

        tokenRepository.save(token);
    }


    @Transactional
    public void refreshToken(
            HttpServletRequest request,
            HttpServletResponse response
    ) throws IOException {
      final String authHeader = request.getHeader(HttpHeaders.AUTHORIZATION);
      final String refreshToken;
      final String userEmail;
      if (authHeader == null ||!authHeader.startsWith("Bearer ")) {
        return;
      }
      refreshToken = authHeader.substring(7);
      userEmail = jwtService.extractUsername(refreshToken);
      if (userEmail != null) {
        var user = this.repository.findByEmail(userEmail)
                .orElseThrow();
        if (jwtService.isTokenValid(refreshToken, user)) {
          var accessToken = jwtService.generateToken(user);
          revokeAllUserTokens(user);
          saveUserToken(user, accessToken);
          var authResponse = AuthenticationResponse.builder()
                  .accessToken(accessToken)
                  .refreshToken(refreshToken)
                  .build();
          new ObjectMapper().writeValue(response.getOutputStream(), authResponse);
        }
      }
    }

}
