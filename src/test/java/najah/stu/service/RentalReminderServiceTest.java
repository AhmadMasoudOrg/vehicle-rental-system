package najah.stu.service;

import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.junit.jupiter.api.Assertions.assertTrue;
import static org.mockito.ArgumentMatchers.anyString;
import static org.mockito.ArgumentMatchers.contains;
import static org.mockito.ArgumentMatchers.eq;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.never;
import static org.mockito.Mockito.verify;

import java.time.LocalDate;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import najah.stu.domain.Rental;
import najah.stu.domain.RentalStatus;
import najah.stu.notification.NotificationService;
import najah.stu.observer.NotificationObserver;

@ExtendWith(MockitoExtension.class)
class RentalReminderServiceTest {

    @Mock
    private NotificationService notificationService;

    private RentalReminderService reminderService;

    @BeforeEach
    void setUp() {

        reminderService = new RentalReminderService(notificationService);
    }

    @Test
    void constructorShouldThrowWhenNotificationServiceIsNull() {

        assertThrows(
                IllegalArgumentException.class,
                () -> new RentalReminderService(null)
        );
    }

    @Test
    void sendExpiryReminderShouldSendNotificationWhenRentalExpiresWithinTwoDays() {

        LocalDate today = LocalDate.of(2026,7,20);

        Rental rental = new Rental(
                1,
                10,
                "masoud",
                LocalDate.of(2026,7,15),
                LocalDate.of(2026,7,22)
        );

        boolean result = reminderService.sendExpiryReminder(
                rental,
                "masoud@example.com",
                today
        );

        assertTrue(result);

        verify(notificationService).sendNotification(
                eq("masoud@example.com"),
                eq("Vehicle Rental Expiry Reminder"),
                contains("2026-07-22")
        );
    }

    @Test
    void sendExpiryReminderShouldSendNotificationWhenRentalExpiresToday() {

        LocalDate today = LocalDate.of(2026,7,20);

        Rental rental = new Rental(
                1,
                10,
                "masoud",
                LocalDate.of(2026,7,15),
                today
        );

        boolean result = reminderService.sendExpiryReminder(
                rental,
                "masoud@example.com",
                today
        );

        assertTrue(result);

        verify(notificationService).sendNotification(
                eq("masoud@example.com"),
                eq("Vehicle Rental Expiry Reminder"),
                contains("Remaining days: 0")
        );
    }

    @Test
    void sendExpiryReminderShouldReturnFalseWhenExpiryIsMoreThanTwoDaysAway() {

        LocalDate today = LocalDate.of(2026,7,20);

        Rental rental = new Rental(
                1,
                10,
                "masoud",
                LocalDate.of(2026,7,15),
                LocalDate.of(2026,7,25)
        );

        boolean result = reminderService.sendExpiryReminder(
                rental,
                "masoud@example.com",
                today
        );

        assertFalse(result);

        verify(notificationService,never()).sendNotification(
                anyString(),
                anyString(),
                anyString()
        );
    }

    @Test
    void sendExpiryReminderShouldReturnFalseWhenRentalAlreadyExpired() {

        LocalDate today = LocalDate.of(2026,7,20);

        Rental rental = new Rental(
                1,
                10,
                "masoud",
                LocalDate.of(2026,7,10),
                LocalDate.of(2026,7,19)
        );

        boolean result = reminderService.sendExpiryReminder(
                rental,
                "masoud@example.com",
                today
        );

        assertFalse(result);

        verify(notificationService,never()).sendNotification(
                anyString(),
                anyString(),
                anyString()
        );
    }

    @Test
    void sendExpiryReminderShouldReturnFalseWhenRentalIsReturned() {

        LocalDate today = LocalDate.of(2026,7,20);

        Rental rental = new Rental(
                1,
                10,
                "masoud",
                LocalDate.of(2026,7,15),
                LocalDate.of(2026,7,21)
        );

        rental.setStatus(RentalStatus.RETURNED);

        boolean result = reminderService.sendExpiryReminder(
                rental,
                "masoud@example.com",
                today
        );

        assertFalse(result);

        verify(notificationService,never()).sendNotification(
                anyString(),
                anyString(),
                anyString()
        );
    }

    @Test
    void sendExpiryReminderShouldThrowWhenRentalIsNull() {

        assertThrows(
                IllegalArgumentException.class,
                () -> reminderService.sendExpiryReminder(
                        null,
                        "masoud@example.com",
                        LocalDate.now()
                )
        );

        verify(notificationService,never()).sendNotification(
                anyString(),
                anyString(),
                anyString()
        );
    }

    @Test
    void sendExpiryReminderShouldThrowWhenEmailIsEmpty() {

        Rental rental = new Rental(
                1,
                10,
                "masoud",
                LocalDate.of(2026,7,15),
                LocalDate.of(2026,7,21)
        );

        assertThrows(
                IllegalArgumentException.class,
                () -> reminderService.sendExpiryReminder(
                        rental,
                        "",
                        LocalDate.of(2026,7,20)
                )
        );

        verify(notificationService,never()).sendNotification(
                anyString(),
                anyString(),
                anyString()
        );
    }

    @Test
    void sendExpiryReminderShouldThrowWhenCurrentDateIsNull() {

        Rental rental = new Rental(
                1,
                10,
                "masoud",
                LocalDate.of(2026,7,15),
                LocalDate.of(2026,7,21)
        );

        assertThrows(
                IllegalArgumentException.class,
                () -> reminderService.sendExpiryReminder(
                        rental,
                        "masoud@example.com",
                        null
                )
        );

        verify(notificationService,never()).sendNotification(
                anyString(),
                anyString(),
                anyString()
        );
    }

    @Test
    void addObserverShouldNotifyTheNewObserver() {

        NotificationObserver observer = mock(NotificationObserver.class);

        reminderService.addObserver(observer);

        Rental rental = new Rental(
                1,
                1,
                "Masoud",
                LocalDate.of(2026,7,10),
                LocalDate.of(2026,7,20)
        );

        boolean result = reminderService.sendExpiryReminder(
                rental,
                "masoud@gmail.com",
                LocalDate.of(2026,7,18)
        );

        assertTrue(result);

        verify(observer).update(
                "masoud@gmail.com",
                "Vehicle Rental Expiry Reminder",
                "Hello Masoud,\nYour rental for vehicle 1 expires on 2026-07-20.\nRemaining days: 2."
        );
    }

    @Test
    void removeObserverShouldStopNotifications() {

        NotificationObserver observer = mock(NotificationObserver.class);

        reminderService.addObserver(observer);

        reminderService.removeObserver(observer);

        Rental rental = new Rental(
                1,
                1,
                "Masoud",
                LocalDate.of(2026,7,10),
                LocalDate.of(2026,7,20)
        );

        boolean result = reminderService.sendExpiryReminder(
                rental,
                "masoud@gmail.com",
                LocalDate.of(2026,7,18)
        );

        assertTrue(result);

        verify(observer,never()).update(
                "masoud@gmail.com",
                "Vehicle Rental Expiry Reminder",
                "Hello Masoud,\nYour rental for vehicle 1 expires on 2026-07-20.\nRemaining days: 2."
        );
    }

    @Test
    void addObserverShouldThrowWhenObserverIsNull() {

        assertThrows(
                IllegalArgumentException.class,
                () -> reminderService.addObserver(null)
        );
    }
}