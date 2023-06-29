package com.endava.parkinglot.services;

public interface EmailNotificationService {
    void sendNotificationAboutGrantedAdminRole(final String userEmail);

    void sendNewPassword(String email,String newPassword);
}
