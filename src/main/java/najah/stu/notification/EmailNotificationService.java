package najah.stu.notification;

import java.util.logging.Logger;

public class EmailNotificationService implements NotificationService {

    private static final Logger LOGGER = Logger.getLogger(EmailNotificationService.class.getName());

    @Override
    public void sendNotification(String recipient,String subject,String message) {
        LOGGER.info("======= EMAIL =======");
        LOGGER.info(() -> "To: " + recipient);
        LOGGER.info(() -> "Subject: " + subject);
        LOGGER.info(message);
        LOGGER.info("=====================");
    }
}
