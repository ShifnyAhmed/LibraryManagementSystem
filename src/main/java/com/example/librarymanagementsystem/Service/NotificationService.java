package com.example.librarymanagementsystem.Service;

import com.example.librarymanagementsystem.Model.Notification;

import java.io.IOException;

public interface NotificationService {

    boolean AddNotification(Notification notification) throws IOException;
}
