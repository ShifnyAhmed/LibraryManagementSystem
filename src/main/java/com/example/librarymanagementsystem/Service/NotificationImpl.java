package com.example.librarymanagementsystem.Service;

import com.example.librarymanagementsystem.Model.Notification;
import com.example.librarymanagementsystem.Repository.NotificationRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import javax.transaction.Transactional;
import java.io.IOException;
import java.util.List;

@Service
@Transactional
public class NotificationImpl implements NotificationService{

    @Autowired
    NotificationRepository notificationRepository;

    @Override
    public boolean AddNotification(Notification notification) throws IOException {
        try
        {
            if(notification!=null)
            {
                notificationRepository.save(notification);
                return true;
            }
        }
        catch (Exception e)
        {
            e.printStackTrace();
            return false;
        }
        return false;
    }

    @Override
    public List<Notification> ViewAllNotificationByEmail(String email) {
        return notificationRepository.findNotificationByEmailOrderByIdDesc(email);
    }

    @Override
    public void ClearAllNotificationByEmail(String email) {
        notificationRepository.deleteAllByEmail(email);
    }
}
