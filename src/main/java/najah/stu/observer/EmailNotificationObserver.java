package najah.stu.observer;

import najah.stu.notification.NotificationService;

public class EmailNotificationObserver implements NotificationObserver {

    private final NotificationService notificationService;

    public EmailNotificationObserver(NotificationService notificationService) {

        if (notificationService == null) {
            throw new IllegalArgumentException("Notification service is required.");
        }

        this.notificationService = notificationService;
    }

    @Override
    public void update(String recipient,String subject,String message) {

        notificationService.sendNotification(recipient,subject,message);
    }
}