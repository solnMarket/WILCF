package org.example.admin.service;

import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

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

    public List<FeedbackModel> getAllFeedbacks() {
        return feedbackRepository.findAll();
    }

    public void deleteFeedback(Long id) {
        feedbackRepository.deleteById(id);
    }

    public String censorFeedbackContent(String feedbackContent) {
        if (feedbackContent == null || feedbackContent.length() <= 4) {
            String firstTwo = feedbackContent.substring(0, 2);
            String lastTwo = feedbackContent.substring(feedbackContent.length() - 2);
            return firstTwo + "***" + lastTwo; // Not enough characters to censor

        }
        String firstTwo = feedbackContent.substring(0, 2);
        String lastTwo = feedbackContent.substring(feedbackContent.length() - 2);
        return firstTwo + "*****" + lastTwo;
    }

     public List<FeedbackModel> censorPrivateFeedbacks(List<FeedbackModel> feedbacks) {
        return feedbacks.stream()
                .peek(feedback -> {
                    if (!feedback.isPublic()) {
                        feedback.setFeedbackContent(censorFeedbackContent(feedback.getFeedbackContent()));
                    }
                })
                .collect(Collectors.toList());
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
}
