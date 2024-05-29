package org.example.admin.controller;

import org.example.admin.model.AdminModel;
import org.example.admin.service.AdminService;
import org.example.admin.service.FeedbackService;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestParam;

@Controller
public class AdminController {

    private final AdminService adminService;
    private final FeedbackService feedbackService;

    public AdminController(AdminService adminService, FeedbackService feedbackService) {
        this.adminService = adminService;
        this.feedbackService = feedbackService;
    }


    @GetMapping("/WILAdmin")
    public String index(Model model) {
        return "WILAdmin";
    }

    @GetMapping("/adminRegisterPage")
    public String getAdminRegisterPage(Model model) {
        model.addAttribute("registerRequest", new AdminModel());
        return "adminRegisterPage";
    }

    @GetMapping("/adminLoginPage")
    public String getAdminLoginPage(Model model) {
        model.addAttribute("loginRequest", new AdminModel());
        return "adminLoginPage";
    }

    @PostMapping("/adminRegisterPage")
    public String adminRegisterPage(@ModelAttribute AdminModel adminModel) {
        System.out.println("register request: " + adminModel);
        AdminModel registerUser = adminService.registerAdmin(adminModel.getLogin(), adminModel.getPassword(), adminModel.getEmail());
        return registerUser == null ? "errorPage" : "redirect:/adminLoginPage";
    }

    @PostMapping("/adminLoginPage")
    public String adminLoginPage(@ModelAttribute AdminModel adminModel, Model model) {
        System.out.println("login request: " + adminModel);
        AdminModel authenticated = adminService.authenticate(adminModel.getLogin(), adminModel.getPassword());
        if(authenticated != null){
            model.addAttribute("adminLogin", authenticated.getLogin());
            return "redirect:/adminFeedbackDashboard";
        } else {        
            return "errorPage";
        }
    }

    @GetMapping("/adminFeedbackDashboard")
    public String adminFeedbackDashboard(Model model) {
        model.addAttribute("feedbacks", feedbackService.getAllFeedbacks());
        return "adminFeedbackDashboard";
    }

    @GetMapping("/refreshFeedbacks")
    public String refreshFeedbacks(Model model) {
        model.addAttribute("feedbacks", feedbackService.getAllFeedbacks());
        return "redirect:/adminFeedbackDashboard";
    }

    @PostMapping("/admin/deleteFeedback")
    public String deleteFeedback(@RequestParam("id") Long id) {
        feedbackService.deleteFeedback(id);
        return "redirect:/adminFeedbackDashboard";
    }

    @PostMapping("/admin/moveToIndex")
    public String moveToIndex(@RequestParam("id") Long id) {
        feedbackService.updateFeedbackVisibility(id, true);
        return "redirect:/adminFeedbackDashboard";
    }
}
