package com.example.librarymanagementsystem.Service;

import com.example.librarymanagementsystem.Model.Notification;

import java.io.IOException;
import java.util.List;

public interface NotificationService {

    boolean AddNotification(Notification notification) throws IOException;

    List<Notification> ViewAllNotificationByEmail(String email);

    void ClearAllNotificationByEmail(String email);
}
