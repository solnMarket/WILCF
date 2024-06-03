package org.example.admin.controller;
 
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.example.admin.model.FeedbackModel;
import org.example.admin.service.FeedbackService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;

 
@Controller
public class FeedbackController {
 
    private final FeedbackService feedbackService;
    private static Logger logger = LoggerFactory.getLogger(FeedbackController.class);
 
    public FeedbackController(FeedbackService feedbackService) {
        this.feedbackService = feedbackService;
    }

    // @GetMapping("/index")
    // public String showFeedbacks(Model model) {
    //     List<FeedbackModel> feedbacks = feedbackService.getAllFeedbacks(); // Corrected line
    //     model.addAttribute("feedbacks", feedbacks);
    //     return "index";
    // }

    @GetMapping("/")
    public String index(Model model) {
        model.addAttribute("selectedFeedbacks", feedbackService.getSelectedFeedbacks());
        return "index";
    }

    @PostMapping("/admin/moveToIndex")
    public String moveToIndex(@RequestParam("selectedFeedbackIds") List<Long> selectedFeedbackIds) {
        for (Long id : selectedFeedbackIds) {
            feedbackService.updateSelectedFeedback(id, true);
        }
        return "redirect:/adminFeedbackDashboard";
    }

    @PostMapping("/admin/toggleFeedbackSelection")
public String toggleFeedbackSelection(@RequestParam("id") Long id, @RequestParam("isSelected") boolean isSelected) {
    feedbackService.updateSelectedFeedback(id, isSelected);
    return "redirect:/adminFeedbackDashboard";
}


    // @GetMapping("/wildcatInnovLab")
    // public String wildcatInnovLab(Model model) {
    //     model.addAttribute("feedbacks", feedbackService.getPublicFeedbacks());
    //     return "wildcatInnovLab";
    // }
 
    @PostMapping("/submitFeedback")
    public String submitFeedback(@RequestParam("feedback") String feedbackContent,
                                 @RequestParam(name = "isPublic", required = false) String isPublic,
                                 @RequestParam("userLogin") String userLogin,
                                 @RequestParam(name = "suggestion", required = false) String suggestion,
                                 Model model) {
        boolean publicFeedback = "true".equals(isPublic);
        feedbackService.submitFeedback(feedbackContent, publicFeedback, userLogin, suggestion);
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
        model.addAttribute("selectedFeedbacks", feedbackService.getSelectedFeedbacks());
        return "userPersonalPage";
    }
 
    @PostMapping("/deleteFeedback")
    public String deleteFeedback(@RequestParam("id") Long id, @RequestParam("userLogin") String userLogin) {
        feedbackService.deleteFeedback(id);
        return "redirect:/userFeedbackDashboard?userLogin=" + userLogin;
    }
 
    @PostMapping("/toggleVisibility")
    public String toggleVisibility(@RequestParam("id") Long id,
                                   @RequestParam("isPublic") boolean isPublic,
                                   @RequestParam("userLogin") String userLogin) {
        feedbackService.updateFeedbackVisibility(id, isPublic);
        return "redirect:/userFeedbackDashboard?userLogin=" + userLogin;
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
        return "redirect:/userFeedbackDashboard?userLogin=" + userLogin;
    }
 
    @PostMapping("/updateFeedback")
public String updateFeedback(@RequestParam("id") Long id,
                             @RequestParam("editedFeedback") String editedFeedback,
                             @RequestParam("editedSuggestion") String editedSuggestion,
                             @RequestParam("userLogin") String userLogin) {
    FeedbackModel feedback = feedbackService.getFeedbackById(id);
    if (feedback != null) {
        feedback.setFeedbackContent(editedFeedback);
        feedback.setSuggestion(editedSuggestion);
        feedback.setEditMode(false);
        feedbackService.save(feedback);
    }
    return "redirect:/userFeedbackDashboard?userLogin=" + userLogin;
}

 
    @GetMapping("/generalFeedbackDashboard")
    public String generalFeedbackDashboard(Model model) {
        model.addAttribute("feedbacks", feedbackService.getAllFeedbacks());
        return "generalFeedbackDashboard";
    }
    
    @GetMapping("/userFeedbackDashboard")
    public String userFeedbackDashboard(Model model, @RequestParam("userLogin") String userLogin) {
        model.addAttribute("userName", userLogin);
        model.addAttribute("feedbacks", feedbackService.getFeedbacksByUser(userLogin));
        return "userFeedbackDashboard";
    }
}
 