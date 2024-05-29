package org.example.admin.controller;

import org.example.admin.model.UserModel;
import org.example.admin.service.UserService;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestParam;

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
    public String userRegisterPage(@ModelAttribute UserModel userModel) {
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

    @PostMapping("/forgotPassword")
    public String processForgotPassword(@RequestParam("email") String email) {
        userService.sendPasswordResetEmail(email);
        return "redirect:/userLoginPage";
    }

    @GetMapping("/resetPasswordPage")
    public String showResetPasswordPage(@RequestParam("token") String token, Model model) {
        if (userService.validatePasswordResetToken(token)) {
            model.addAttribute("token", token);
            return "resetPasswordPage";
        } else {
            return "errorPage";
        }
    }

    @PostMapping("/resetPassword")
    public String processResetPassword(@RequestParam("token") String token,
                                       @RequestParam("newPassword") String newPassword,
                                       @RequestParam("confirmPassword") String confirmPassword) {
        if (!newPassword.equals(confirmPassword)) {
            return "redirect:/resetPasswordPage?token=" + token + "&error=passwordMismatch";
        }
        if (userService.resetPassword(token, newPassword)) {
            return "redirect:/userLoginPage";
        } else {
            return "errorPage";
        }
    }
}
