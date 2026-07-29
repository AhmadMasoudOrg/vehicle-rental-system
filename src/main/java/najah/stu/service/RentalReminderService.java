package najah.stu.service;

import java.time.LocalDate;
import java.time.temporal.ChronoUnit;
import java.util.ArrayList;
import java.util.List;

import najah.stu.domain.Rental;
import najah.stu.notification.NotificationService;
import najah.stu.observer.EmailNotificationObserver;
import najah.stu.observer.NotificationObserver;

/**
 * Sends reminders for rentals that are close to their end date.
 *
 * This service uses the Observer Pattern to notify all registered
 * notification observers.
 */
public class RentalReminderService {

    private static final long REMINDER_DAYS = 2;

    private final List<NotificationObserver> observers;

    /**
     * Creates a rental reminder service.
     *
     * An email observer is registered automatically.
     *
     * @param notificationService service used to send notifications
     * @throws IllegalArgumentException if the notification service is null
     */
    public RentalReminderService(NotificationService notificationService) {

        if (notificationService == null) {
            throw new IllegalArgumentException("Notification service is required.");
        }

        observers = new ArrayList<>();

        addObserver(new EmailNotificationObserver(notificationService));
    }

    /**
     * Adds a notification observer.
     *
     * The observer is added only when it is not already registered.
     *
     * @param observer observer to add
     * @throws IllegalArgumentException if the observer is null
     */
    public void addObserver(NotificationObserver observer) {

        if (observer == null) {
            throw new IllegalArgumentException("Observer is required.");
        }

        if (!observers.contains(observer)) {
            observers.add(observer);
        }
    }

    /**
     * Removes a notification observer.
     *
     * @param observer observer to remove
     */
    public void removeObserver(NotificationObserver observer) {

        observers.remove(observer);
    }

    /**
     * Sends a notification to all registered observers.
     *
     * @param recipient notification recipient
     * @param subject notification subject
     * @param message notification message
     */
    private void notifyObservers(String recipient,String subject,String message) {

        for (NotificationObserver observer : observers) {
            observer.update(recipient,subject,message);
        }
    }

    /**
     * Sends an expiry reminder using the current system date.
     *
     * @param rental rental to check
     * @param customerEmail customer email address
     * @return true when a reminder is sent
     */
    public boolean sendExpiryReminder(Rental rental,String customerEmail) {

        return sendExpiryReminder(rental,customerEmail,LocalDate.now());
    }

    /**
     * Sends an expiry reminder when the rental ends within two days.
     *
     * A reminder is not sent when the rental is returned,
     * already expired or ends after more than two days.
     *
     * @param rental rental to check
     * @param customerEmail customer email address
     * @param currentDate date used to calculate the remaining days
     * @return true when a reminder is sent
     * @throws IllegalArgumentException if any required input is invalid
     */
    public boolean sendExpiryReminder(Rental rental,String customerEmail,LocalDate currentDate) {

        validateInput(rental,customerEmail,currentDate);

        if (!rental.isActive()) {
            return false;
        }

        long remainingDays = ChronoUnit.DAYS.between(currentDate,rental.getEndDate());

        if (remainingDays < 0 || remainingDays > REMINDER_DAYS) {
            return false;
        }

        String subject = "Vehicle Rental Expiry Reminder";

        String message = "Hello "
                + rental.getCustomerName()
                + ",\nYour rental for vehicle "
                + rental.getVehicleId()
                + " expires on "
                + rental.getEndDate()
                + ".\nRemaining days: "
                + remainingDays
                + ".";

        notifyObservers(customerEmail,subject,message);

        return true;
    }

    /**
     * Validates the reminder input.
     *
     * @param rental rental to validate
     * @param customerEmail customer email address
     * @param currentDate current date
     * @throws IllegalArgumentException if any input is invalid
     */
    private void validateInput(Rental rental,String customerEmail,LocalDate currentDate) {

        if (rental == null) {
            throw new IllegalArgumentException("Rental is required.");
        }

        if (customerEmail == null || customerEmail.trim().isEmpty()) {
            throw new IllegalArgumentException("Customer email is required.");
        }

        if (currentDate == null) {
            throw new IllegalArgumentException("Current date is required.");
        }
    }
}