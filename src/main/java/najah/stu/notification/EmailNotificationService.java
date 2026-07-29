package najah.stu.notification;

public class EmailNotificationService implements NotificationService {

    @Override
    public void sendNotification(String recipient,String subject,String message) {       

        System.out.println("======= EMAIL =======");
        System.out.println("To: " + recipient);
        System.out.println("Subject: " + subject);
        System.out.println(message);
        System.out.println("=====================");
    }
}