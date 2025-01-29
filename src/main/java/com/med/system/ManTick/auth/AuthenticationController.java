package com.med.system.ManTick.auth;

import java.io.IOException;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.authentication.BadCredentialsException;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import lombok.RequiredArgsConstructor;

@RestController
@RequestMapping("/api/v1/auth")
@RequiredArgsConstructor
public class AuthenticationController {

    private final AuthenticationService service;



    // @PostMapping("/register")
    // @PreAuthorize("hasAuthority('admin:create')")
    // public ResponseEntity<AuthenticationResponse> register(
    //     @RequestBody RegisterRequest request
    // ) {
    //     return ResponseEntity.ok(service.register(request));
    // }

    
    @PostMapping("/authenticate")
    public ResponseEntity<AuthenticationResponse> authenticate(
        @RequestBody AuthenticationRequest request
    ) {
        try {
            return ResponseEntity.ok(service.authenticate(request));
        } catch (BadCredentialsException e) {
            return ResponseEntity.status(HttpStatus.UNAUTHORIZED).body(
                AuthenticationResponse.builder()
                    .accessToken("")
                    .refreshToken("")
                    .message("Invalid username or password")
                    .success(false)
                    .build()
            );
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(
                AuthenticationResponse.builder()
                    .accessToken("")
                    .refreshToken("")
                    .message("An unexpected error occurred")
                    .success(false)
                    .build()
            );
        }
    
    }


    @PostMapping("/refresh-token")
    public void refreshToken(
        HttpServletRequest request,
        HttpServletResponse response
    ) throws IOException {
        service.refreshToken(request, response);
    }
}
