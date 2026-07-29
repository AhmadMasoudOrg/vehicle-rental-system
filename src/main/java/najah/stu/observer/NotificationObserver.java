package najah.stu.observer;

/**
 * Defines an observer that receives notification updates.
 */
public interface NotificationObserver {

    /**
     * Receives a notification from the subject.
     *
     * @param recipient notification recipient
     * @param subject notification subject
     * @param message notification message
     */
    void update(String recipient,String subject,String message);
}