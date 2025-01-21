
package com.med.system.ManTick.auth;

import java.util.List;

import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import com.med.system.ManTick.JWT.JwtService;
import com.med.system.ManTick.Users.Role;
import com.med.system.ManTick.Users.User;
import com.med.system.ManTick.Users.UserRepository;
import com.med.system.ManTick.token.Token;
import com.med.system.ManTick.token.TokenRepository;
import com.med.system.ManTick.token.TokenType;

import lombok.RequiredArgsConstructor;



@Service
@RequiredArgsConstructor
public class AuthenticationService {

    private final UserRepository repository;
    private final TokenRepository tokenRepository;
    private final PasswordEncoder passwordEncoder;
    private final JwtService jwtService;
    private final AuthenticationManager authenticationManager;

    public AuthenticationResponse register(RegisterRequest request) {
        User user = User.builder()
            .firstname(request.getFirstname())
            .lastname(request.getLastname())
            .email(request.getEmail())
            .password(passwordEncoder.encode(request.getPassword()))
            .role(Role.USER)
            .build();
        User saveUser = repository.save(user);

        String jwtToken = jwtService.generateToken(user);
        
        saveUserToken(saveUser, jwtToken);

        return AuthenticationResponse.builder()
            .token(jwtToken)
            .build();
    }


    public AuthenticationResponse authenticate(AuthenticationRequest request) {
        authenticationManager.authenticate(
            new UsernamePasswordAuthenticationToken(
                request.getEmail(),
                request.getPassword()
        ));

        User user = repository.findByEmail(request.getEmail())
            .orElseThrow();

            
        String jwtToken = jwtService.generateToken(user);

        revokeAllUserTokens(user);
        saveUserToken(user, jwtToken);

        return AuthenticationResponse.builder()
            .token(jwtToken)
            .build();
    }


    private void revokeAllUserTokens(User user) {
        List<Token> validUserTokens = tokenRepository.findAllValidTokenByUser(user.getId());
        if (validUserTokens.isEmpty()) return;
        validUserTokens.forEach(t -> {
            t.setExpired(true);
            t.setRevoked(true);
        });
        tokenRepository.saveAll(validUserTokens);
    }


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

}
