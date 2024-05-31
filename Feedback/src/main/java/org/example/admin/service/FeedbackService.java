package org.example.admin.service;

import java.util.List;
import java.util.Optional;

import org.example.admin.model.FeedbackModel;
import org.example.admin.repository.FeedbackRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service
public class FeedbackService {

    private final FeedbackRepository feedbackRepository;

    @Autowired
    public FeedbackService(FeedbackRepository feedbackRepository) {
        this.feedbackRepository = feedbackRepository;
    }

    public List<FeedbackModel> getFeedbacksByUser(String userLogin) {
        return feedbackRepository.findByUserLogin(userLogin);
    }

    public void submitFeedback(String feedbackContent, boolean isPublic, String userLogin) {
        FeedbackModel feedback = new FeedbackModel(null, feedbackContent, isPublic, userLogin);
        feedbackRepository.save(feedback);
    }

    public List<FeedbackModel> getPublicFeedbacks() {
        return feedbackRepository.findByIsPublicTrue();
    }

    public List<FeedbackModel> getPublicFeedbacksTrue() {
        return feedbackRepository.findByIsPublic(true);
    }

    public List<FeedbackModel> getAllFeedbacks() {
        return feedbackRepository.findAll();
    }

    public void deleteFeedback(Long id) {
        feedbackRepository.deleteById(id);
    }

    public void updateFeedbackVisibility(Long id, boolean isPublic) {
        Optional<FeedbackModel> feedbackOpt = feedbackRepository.findById(id);
        if (feedbackOpt.isPresent()) {
            FeedbackModel feedback = feedbackOpt.get();
            feedback.setPublic(isPublic);
            feedbackRepository.save(feedback);
        }
    }

    public FeedbackModel getFeedbackById(Long id) {
        return feedbackRepository.findById(id).orElse(null);
    }

    public void save(FeedbackModel feedback) {
        feedbackRepository.save(feedback);
    }

    public void updateSelectedFeedback(Long id, boolean isSelected) {
        FeedbackModel feedback = feedbackRepository.findById(id)
                .orElseThrow(() -> new IllegalArgumentException("Invalid feedback ID: " + id));
        feedback.setSelected(isSelected);
        feedbackRepository.save(feedback);
    }
    
    public void updateSelectedFeedbacks(List<Long> selectedFeedbackIds) {
        List<FeedbackModel> allFeedbacks = feedbackRepository.findAll();
        for (FeedbackModel feedback : allFeedbacks) {
            feedback.setSelected(selectedFeedbackIds.contains(feedback.getId()));
        }
        feedbackRepository.saveAll(allFeedbacks);
    }
    
    public List<FeedbackModel> getSelectedFeedbacks() {
        return feedbackRepository.findByIsSelected(true);
    }
}
