package najah.stu.service;

import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertTrue;
import static org.mockito.ArgumentMatchers.contains;
import static org.mockito.ArgumentMatchers.eq;
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

@ExtendWith(MockitoExtension.class)
class RentalReminderServiceTest {

    @Mock
    private NotificationService notificationService;

    private RentalReminderService reminderService;

    @BeforeEach
    void setUp() {

        reminderService =
                new RentalReminderService(
                        notificationService
                );
    }

    @Test
    void shouldSendReminderWhenRentalExpiresInTwoDays() {

        LocalDate today =
                LocalDate.of(2026, 7, 20);

        Rental rental = new Rental(
                1,
                10,
                "masoud",
                LocalDate.of(2026, 7, 15),
                LocalDate.of(2026, 7, 22)
        );

        boolean result =
                reminderService.sendExpiryReminder(
                        rental,
                        "masoud@example.com",
                        today
                );

        assertTrue(result);

        verify(notificationService)
                .sendNotification(
                        eq("masoud@example.com"),
                        eq("Vehicle Rental Expiry Reminder"),
                        contains("2026-07-22")
                );
    }

    @Test
    void shouldNotSendReminderWhenExpiryIsMoreThanTwoDaysAway() {

        LocalDate today =
                LocalDate.of(2026, 7, 20);

        Rental rental = new Rental(
                1,
                10,
                "masoud",
                LocalDate.of(2026, 7, 15),
                LocalDate.of(2026, 7, 25)
        );

        boolean result =
                reminderService.sendExpiryReminder(
                        rental,
                        "masoud@example.com",
                        today
                );

        assertFalse(result);

        verify(
                notificationService,
                never()
        ).sendNotification(
                org.mockito.ArgumentMatchers.anyString(),
                org.mockito.ArgumentMatchers.anyString(),
                org.mockito.ArgumentMatchers.anyString()
        );
    }

    @Test
    void shouldNotSendReminderForReturnedRental() {

        LocalDate today =
                LocalDate.of(2026, 7, 20);

        Rental rental = new Rental(
                1,
                10,
                "masoud",
                LocalDate.of(2026, 7, 15),
                LocalDate.of(2026, 7, 21)
        );

        rental.setStatus(RentalStatus.RETURNED);

        boolean result =
                reminderService.sendExpiryReminder(
                        rental,
                        "masoud@example.com",
                        today
                );

        assertFalse(result);

        verify(
                notificationService,
                never()
        ).sendNotification(
                org.mockito.ArgumentMatchers.anyString(),
                org.mockito.ArgumentMatchers.anyString(),
                org.mockito.ArgumentMatchers.anyString()
        );
    }
}