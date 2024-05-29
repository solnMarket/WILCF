package org.example.admin.model;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.Table;

@Entity
@Table(name = "feedbacks")
public class FeedbackModel {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(nullable = false)
    private String feedbackContent;

    @Column(nullable = false)
    private boolean isPublic;

    @Column(nullable = false)
    private boolean editMode = false;

    @Column(nullable = false)
    private String userLogin;

    // Constructors
    public FeedbackModel() {
    }

    public FeedbackModel(Long id, String feedbackContent, boolean isPublic, String userLogin) {
        this.id = id;
        this.feedbackContent = feedbackContent;
        this.isPublic = isPublic;
        this.userLogin = userLogin;
    }
    

    // Getters and Setters
    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getFeedbackContent() {
        return feedbackContent;
    }

    public void setFeedbackContent(String feedbackContent) {
        this.feedbackContent = feedbackContent;
    }

    public boolean isPublic() {
        return isPublic;
    }

    public void setPublic(boolean isPublic) {
        this.isPublic = isPublic;
    }

    public String getUserLogin() {
        return userLogin;
    }

    public void setUserLogin(String userLogin) {
        this.userLogin = userLogin;
    }

    @Override
    public String toString() {
        return "FeedbackModel{" +
                "id=" + id +
                ", feedbackContent='" + feedbackContent + '\'' +
                ", isPublic=" + isPublic +
                ", userLogin='" + userLogin + '\'' +
                '}';
    }

    public boolean isEditMode() {
        return editMode;
    }

    public void setEditMode(boolean editMode) {
        this.editMode = editMode;
    }

    public String getCensoredName() {
        if (userLogin == null || userLogin.length() < 4) {
            return "****"; // handle short or null names
        }
        String firstTwo = userLogin.substring(0, 2);
        String lastTwo = userLogin.substring(userLogin.length() - 2);
        return firstTwo + "*****" + lastTwo;
    }
}
