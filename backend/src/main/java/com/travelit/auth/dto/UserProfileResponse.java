package com.travelit.auth.dto;

public class UserProfileResponse {

    private Long userId;
    private String name;
    private String email;
    private String role;
    private String profilePicture;

    public UserProfileResponse() {
    }

    public UserProfileResponse(
            Long userId,
            String name,
            String email,
            String role,
            String profilePicture
    ) {
        this.userId = userId;
        this.name = name;
        this.email = email;
        this.role = role;
        this.profilePicture = profilePicture;
    }

    public Long getUserId() {
        return userId;
    }

    public void setUserId(Long userId) {
        this.userId = userId;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public String getRole() {
        return role;
    }

    public void setRole(String role) {
        this.role = role;
    }

    public String getProfilePicture() {
        return profilePicture;
    }

    public void setProfilePicture(String profilePicture) {
        this.profilePicture = profilePicture;
    }
}