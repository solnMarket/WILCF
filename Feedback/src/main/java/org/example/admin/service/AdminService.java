package org.example.admin.service;

import org.example.admin.model.AdminModel;
import org.example.admin.repository.AdminRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

@Service
public class AdminService {

    private final AdminRepository adminRepository;
    private final PasswordEncoder passwordEncoder;

    @Autowired
    public AdminService(AdminRepository adminRepository, PasswordEncoder passwordEncoder) {
        this.adminRepository = adminRepository;
        this.passwordEncoder = passwordEncoder;
    }

    public AdminModel registerAdmin(String login, String password, String email) {
        if (adminRepository.findByLogin(login) != null || adminRepository.findByEmail(email) != null) {
            return null; // Admin already exists
        }
        String encodedPassword = passwordEncoder.encode(password);
        AdminModel admin = new AdminModel(null, login, encodedPassword, email);
        return adminRepository.save(admin);
    }

    public AdminModel authenticate(String login, String password) {
        AdminModel admin = adminRepository.findByLogin(login);
        if (admin != null && passwordEncoder.matches(password, admin.getPassword())) {
            return admin;
        }
        return null;
    }
}
