package najah.stu.service;

import java.time.LocalDate;
import java.time.temporal.ChronoUnit;

import najah.stu.domain.Rental;
import najah.stu.notification.NotificationService;

public class RentalReminderService {

    private static final long REMINDER_DAYS = 2;

    private final NotificationService notificationService;

    public RentalReminderService(
            NotificationService notificationService) {

        if (notificationService == null) {
            throw new IllegalArgumentException(
                    "Notification service is required."
            );
        }

        this.notificationService = notificationService;
    }

    public boolean sendExpiryReminder(
            Rental rental,
            String customerEmail) {

        return sendExpiryReminder(
                rental,
                customerEmail,
                LocalDate.now()
        );
    }

    public boolean sendExpiryReminder(
            Rental rental,
            String customerEmail,
            LocalDate currentDate) {

        validateInput(
                rental,
                customerEmail,
                currentDate
        );

        if (!rental.isActive()) {
            return false;
        }

        long remainingDays = ChronoUnit.DAYS.between(
                currentDate,
                rental.getEndDate()
        );

        if (remainingDays < 0
                || remainingDays > REMINDER_DAYS) {

            return false;
        }

        String subject =
                "Vehicle Rental Expiry Reminder";

        String message =
                "Hello "
                        + rental.getCustomerName()
                        + ",\nYour rental for vehicle "+ rental.getVehicleId()+ " expires on "+ rental.getEndDate()
                        + ".\nRemaining days: "+ remainingDays+ ".";

        notificationService.sendNotification(customerEmail,subject,message);

        return true;
    }

    private void validateInput(
            Rental rental,
            String customerEmail,
            LocalDate currentDate) {

        if (rental == null) {
            throw new IllegalArgumentException("Rental is required.");
        }

        if (customerEmail == null|| customerEmail.trim().isEmpty()) {

            throw new IllegalArgumentException("Customer email is required.");
        }

        if (currentDate == null) {
            throw new IllegalArgumentException(
                    "Current date is required."
            );
        }
    }
}