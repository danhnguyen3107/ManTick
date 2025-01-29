
package com.med.system.ManTick.admin;


import java.util.List;
import java.util.stream.Collectors;


import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.med.system.ManTick.Users.User;
import com.med.system.ManTick.Users.RequestResponse.UserResponse;
import com.med.system.ManTick.admin.RequestResponse.SearchRequest;
import com.med.system.ManTick.admin.services.AdminService;
import com.med.system.ManTick.auth.AuthenticationResponse;
import com.med.system.ManTick.auth.AuthenticationService;
import com.med.system.ManTick.auth.RegisterRequest;

import lombok.RequiredArgsConstructor;




@RestController
@RequestMapping("/api/v1/admin")
@PreAuthorize("hasRole('ADMIN')")
@RequiredArgsConstructor
public class AdminController {

    private final AuthenticationService service;
    private final AdminService adminService;
    
    @GetMapping
    @PreAuthorize("hasAuthority('admin:read')")
    public String get() {
        return "GET:: admin controller";
    }

    @PostMapping
    @PreAuthorize("hasAuthority('admin:create')")
    public String post() {
        return "POST:: admin controller";
    }

    @PutMapping
    @PreAuthorize("hasAuthority('admin:update')")
    public String put() {
        return "PUT:: admin controller";
    }

    @DeleteMapping
    @PreAuthorize("hasAuthority('admin:delete')")
    public String delete() {
        return "DELETE:: admin controller";
    }

    @PostMapping("/createUser")
    @PreAuthorize("hasAuthority('admin:create')")
    public ResponseEntity<AuthenticationResponse> createUser(
        @RequestBody RegisterRequest request
    ) {
        return ResponseEntity.ok(service.register(request));
    }


    @GetMapping("/getAllUsers")
    @PreAuthorize("hasAuthority('admin:read')")
    public ResponseEntity<?> getAllUsers() {
     
        List<UserResponse> users = adminService.getAllUsers().stream()
                .map(this::mapToUserResponse)
                .collect(Collectors.toList());
        return ResponseEntity.ok(users);
    
    }


    private UserResponse mapToUserResponse(User user) {
        return UserResponse.builder()
                .id(user.getId().intValue()) 
                .firstname(user.getUsername()) 
                .lastname(user.getLastname()) 
                .email(user.getEmail())
                .role(user.getRole()) 
                .build();
    }


    @DeleteMapping("/deleteUser")
    @PreAuthorize("hasAuthority('admin:delete')")
    public ResponseEntity<?> deleteUser(@RequestBody User user) {
        boolean isDeleteUser = adminService.deleteUserById(user.getId());
       
        if (!isDeleteUser) 
            return ResponseEntity.badRequest().body("Cannot delete user");

        return ResponseEntity.ok("Seccessfully delete user");
    }

    @GetMapping("/search")
    @PreAuthorize("hasAuthority('admin:read')")
    public List<UserResponse> searchUsers(@RequestBody SearchRequest request) {
       
        System.out.println("Request String: " + request.getRequest());
        List<User> users = adminService.searchUsers(request.getRequest());

        List<UserResponse> userResponses = users.stream()
            .map(user -> UserResponse.builder()
                .id(user.getId().intValue())
                .firstname(user.getFirstname())
                .lastname(user.getLastname())
                .email(user.getEmail())
                .role(user.getRole())
                .build())
            .collect(Collectors.toList());

        return userResponses;
    
       
    }
}
