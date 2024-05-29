package org.example.admin.repository;

import java.util.List;

import org.example.admin.model.FeedbackModel;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface FeedbackRepository extends JpaRepository<FeedbackModel, Long> {
    List<FeedbackModel> findByIsPublicTrue();
    List<FeedbackModel> findByUserLogin(String userLogin);
}
