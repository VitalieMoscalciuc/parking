package com.vmoscalciuc.parkinglot.services;

public interface EmailNotificationService {
    void sendNotificationAboutGrantedAdminRole(final String userEmail);

    void sendNewPassword(String email,String newPassword);

    void sendNotificationAboutDeletionFromParkingLot(String email,String parkingLotName);
}
