package org.example.admin.repository;

import java.util.Optional;

import org.example.admin.Entity.User;
import org.example.admin.model.UserModel;
import org.springframework.data.jpa.repository.JpaRepository;

public interface UserRepository extends JpaRepository<User, Long> {
    Optional<User> findByEmail(String email);
    Optional<User> findByResetToken(String resetToken); // Assuming you have a resetToken field in User entity

    public UserModel save(UserModel user);
}
