package org.example.admin.controller;

import org.example.admin.model.UserModel;
import org.example.admin.service.UserService;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestParam;

import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpSession;

@Controller
public class UserController {

    private final UserService userService;

    public UserController(UserService userService) {
        this.userService = userService;
    }

    @GetMapping("/userRegisterPage")
    public String getUserRegisterPage(Model model) {
        model.addAttribute("registerRequest", new UserModel());
        return "userRegisterPage";
    }

    @GetMapping("/userLoginPage")
    public String getUserLoginPage(Model model) {
        model.addAttribute("loginRequest", new UserModel());
        return "userLoginPage";
    }

    @PostMapping("/userRegisterPage")
    public String userRegisterPage(@ModelAttribute UserModel userModel,
                                   @RequestParam("confirmPassword") String confirmPassword,
                                   Model model) {
        if (!userModel.getPassword().equals(confirmPassword)) {
            model.addAttribute("registerRequest", userModel);
            model.addAttribute("errorMessage", "Passwords do not match!");
            return "userRegisterPage";
        }

        System.out.println("register request: " + userModel);
        UserModel registerUser = userService.registerUser(userModel.getName(), userModel.getEmail(), userModel.getPassword());
        return registerUser == null ? "errorPage" : "redirect:/userLoginPage";
    }
    

    @PostMapping("/userLoginPage")
    public String userLoginPage(@ModelAttribute UserModel userModel, Model model) {
        System.out.println("login request: " + userModel);
        UserModel authenticated = userService.authenticate(userModel.getEmail(), userModel.getPassword());
        if (authenticated != null) {
            return "redirect:/userPersonalPage?userLogin=" + authenticated.getName();
        } else {
            model.addAttribute("error", "Invalid email or password");
            return "userLoginErrorPage";
        }
    }

    @GetMapping("/forgotPasswordPage")
    public String showForgotPasswordPage() {
        return "forgotPasswordPage";
    }

    @PostMapping("/verifyCurrentPassword")
    public String verifyCurrentPassword(@RequestParam("email") String email,
                                        @RequestParam("currentPassword") String currentPassword,
                                        Model model) {
        if (userService.authenticate(email, currentPassword) != null) {
            model.addAttribute("notVerified", false);
        } else {
            model.addAttribute("notVerified", true);
            model.addAttribute("errorMessage", "Invalid email or password");
        }
        return "changePasswordPage";
    }

    @PostMapping("/changePassword")
    public String changePassword(@RequestParam("newPassword") String newPassword,
                                 @RequestParam("confirmPassword") String confirmPassword,
                                 @RequestParam("userLogin") String userName,
                                 Model model) {
        if (!newPassword.equals(confirmPassword)) {
            model.addAttribute("notVerified", false);
            model.addAttribute("passwordErrorMessage", "Passwords do not match");
        } else if (userService.changePassword(userName, newPassword)) {
            model.addAttribute("passwordSuccessMessage", "Password changed successfully");
        } else {
            model.addAttribute("notVerified", false);
            model.addAttribute("passwordErrorMessage", "Failed to change password");
        }
        return "changePasswordPage";
    }

    @GetMapping("/Logout")
    public String logout(HttpServletRequest request) {
        HttpSession session = request.getSession(false);
        if (session != null) {
            session.invalidate();
        }
        return "redirect:/";
    }
}
