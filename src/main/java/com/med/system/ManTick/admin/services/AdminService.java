
package com.med.system.ManTick.admin.services;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.med.system.ManTick.Users.User;
import com.med.system.ManTick.Users.UserRepository;
import com.med.system.ManTick.admin.repository.AdminRepository;

import lombok.AllArgsConstructor;

@Service
@AllArgsConstructor
public class AdminService {

    private final AdminRepository adminRepository;


    public List<User> getAllUsers() {
        return adminRepository.findAll();
    }
}
