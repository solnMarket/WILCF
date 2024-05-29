package org.example.admin.repository;

import org.example.admin.model.AdminModel;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface AdminRepository extends JpaRepository<AdminModel, Long> {
    AdminModel findByLogin(String login);
    AdminModel findByEmail(String email);
}
