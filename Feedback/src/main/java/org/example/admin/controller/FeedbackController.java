package org.example.admin.controller;
 
import org.example.admin.model.FeedbackModel;
import org.example.admin.service.FeedbackService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestParam;
 
@Controller
public class FeedbackController {
 
    private final FeedbackService feedbackService;
    private static Logger logger = LoggerFactory.getLogger(FeedbackController.class);
 
    public FeedbackController(FeedbackService feedbackService) {
        this.feedbackService = feedbackService;
    }

    @GetMapping("/index")
    public String showFeedbacks(Model model) {
        model.addAttribute("feedbacks", feedbackService.getPublicFeedbacks());
        return "index";
    }
 
    @PostMapping("/submitFeedback")
    public String submitFeedback(@RequestParam("feedback") String feedbackContent,
                                 @RequestParam(name = "isPublic", required = false) String isPublic,
                                 @RequestParam("userLogin") String userLogin,
                                 Model model) {
        boolean publicFeedback = "true".equals(isPublic);
        feedbackService.submitFeedback(feedbackContent, publicFeedback, userLogin);
        model.addAttribute("userName", userLogin);
        model.addAttribute("feedbacks", feedbackService.getFeedbacksByUser(userLogin));
        return "userFeedbackDashboard";
    }
 
    @GetMapping("/feedbackDashboard")
    public String feedbackDashboard(Model model) {
        model.addAttribute("feedbacks", feedbackService.getPublicFeedbacks());
        return "feedbackDashboard";
    }
 
    @GetMapping("/userPersonalPage")
    public String userPersonalPage(Model model, @RequestParam("userLogin") String userLogin) {
        model.addAttribute("userName", userLogin);
        model.addAttribute("feedbacks", feedbackService.getFeedbacksByUser(userLogin));
        return "userPersonalPage";
    }
 
    @PostMapping("/deleteFeedback")
    public String deleteFeedback(@RequestParam("id") Long id, @RequestParam("userLogin") String userLogin) {
        feedbackService.deleteFeedback(id);
        return "redirect:/userPersonalPage?userLogin=" + userLogin;
    }
 
    @PostMapping("/toggleVisibility")
    public String toggleVisibility(@RequestParam("id") Long id,
                                   @RequestParam("isPublic") boolean isPublic,
                                   @RequestParam("userLogin") String userLogin) {
        feedbackService.updateFeedbackVisibility(id, isPublic);
        return "redirect:/userPersonalPage?userLogin=" + userLogin;
    }
 
    @PostMapping("/goToUserPersonalPage")
    public String goToUserPersonalPage(@RequestParam("userLogin") String userLogin) {
        return "redirect:/userPersonalPage?userLogin=" + userLogin;
    }
 
    @PostMapping("/toggleEditMode")
    public String toggleEditMode(@RequestParam("id") Long id, @RequestParam("editMode") boolean editMode, @RequestParam("userLogin") String userLogin) {
        FeedbackModel feedback = feedbackService.getFeedbackById(id);
        if (feedback != null) {
            feedback.setEditMode(editMode);
            feedbackService.save(feedback);
        }
        return "redirect:/userPersonalPage?userLogin=" + userLogin;
    }
 
    @PostMapping("/updateFeedback")
    public String updateFeedback(@RequestParam("id") Long id, @RequestParam("editedFeedback") String editedFeedback, @RequestParam("userLogin") String userLogin) {
        FeedbackModel feedback = feedbackService.getFeedbackById(id);
        if (feedback != null) {
            feedback.setFeedbackContent(editedFeedback);
            feedback.setEditMode(false);
            feedbackService.save(feedback);
        }
        return "redirect:/userPersonalPage?userLogin=" + userLogin;
    }
 
    @GetMapping("/generalFeedbackDashboard")
    public String generalFeedbackDashboard(Model model) {
        model.addAttribute("feedbacks", feedbackService.getAllFeedbacks());
        return "generalFeedbackDashboard";
    }
}
 