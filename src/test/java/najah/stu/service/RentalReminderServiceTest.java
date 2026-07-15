package najah.stu.service;

import static org.junit.jupiter.api.Assertions.*;
import java.time.LocalDate;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import najah.stu.domain.Rental;
import najah.stu.domain.RentalStatus;
import najah.stu.notification.NotificationService;
class RentalReminderServiceTest {
    
    private static class FakeNotificationService implements NotificationService {
        private boolean wasCalled = false;
        private String lastRecipient;
        @Override
        public void sendNotification(String recipient, String subject, String message) {
            wasCalled = true;
            lastRecipient = recipient;
        }
        boolean wasCalled() {
            return wasCalled;
        }

        String getLastRecipient() {
            return lastRecipient;
        }
    
    
    
    
    }
    private FakeNotificationService fakeNotificationService;
    private RentalReminderService reminderService;
    @BeforeEach
    void setUp() {
        fakeNotificationService = new FakeNotificationService();
        reminderService = new RentalReminderService(fakeNotificationService);
    }
    @Test
    void constructorShouldThrowWhenNotificationServiceIsNull() {
        assertThrows(IllegalArgumentException.class, () -> {
            new RentalReminderService(null);
        });
    }

    @Test
    void sendExpiryReminderShouldReturnTrueWhenRentalExpiresWithinTwoDays() {
        LocalDate today = LocalDate.of(2026, 1, 1);
        LocalDate endDate = LocalDate.of(2026, 1, 2);
        Rental rental = new Rental(1, 1, "Ali", LocalDate.of(2025, 12, 25), endDate);
        boolean result = reminderService.sendExpiryReminder(rental, "ali@example.com", today);

        assertTrue(result);
        assertTrue(fakeNotificationService.wasCalled());
        assertEquals("ali@example.com", fakeNotificationService.getLastRecipient()
        		
        		
        		
        		);
    }

    @Test
    void sendExpiryReminderShouldReturnFalseWhenRentalExpiresInMoreThanTwoDays() 
    {
        LocalDate today = LocalDate.of(2026, 1, 1);
        LocalDate endDate = LocalDate.of(2026, 1, 10);
        Rental rental = new Rental(1, 1, "Ali", LocalDate.of(2025, 12, 25), endDate);

        boolean result = reminderService.sendExpiryReminder(rental, "ali@example.com", today);

        assertFalse(result);
        assertFalse(fakeNotificationService.wasCalled()
        		
        		
        		
        		);
    }

    @Test
    void sendExpiryReminderShouldReturnFalseWhenRentalAlreadyExpired()
    {
        LocalDate today = LocalDate.of(2026, 1, 5);
        LocalDate endDate = LocalDate.of(2026, 1, 1);
        Rental rental = new Rental(1, 1, "Ali", LocalDate.of(2025, 12, 25), endDate);

        boolean result = reminderService.sendExpiryReminder(rental, "ali@example.com", today);

        assertFalse(result);
    }

    @Test
    void sendExpiryReminderShouldReturnFalseWhenRentalIsNotActive()
    {
        LocalDate today = LocalDate.of(2026, 1, 1);
        LocalDate endDate = LocalDate.of(2026, 1, 2);
        Rental rental = new Rental(1, 1, "Ali", LocalDate.of(2025, 12, 25), endDate);
        rental.setStatus(RentalStatus.RETURNED);

        boolean result = reminderService.sendExpiryReminder(rental, "ali@example.com", today);

        assertFalse(result);
        assertFalse(fakeNotificationService.wasCalled()
        		
        		
        		
        		);
    }

    @Test
    void sendExpiryReminderShouldThrowWhenRentalIsNull() {
        assertThrows(IllegalArgumentException.class, () -> {
            reminderService.sendExpiryReminder(null, "ali@example.com", LocalDate.now());
        }
        
        		
        		
        		);
    }

    @Test
    void sendExpiryReminderShouldThrowWhenEmailIsEmpty() {
        Rental rental = new Rental(1, 1, "Ali", LocalDate.of(2025, 12, 25), LocalDate.of(2026, 1, 2));

        assertThrows(IllegalArgumentException.class, () -> {
            reminderService.sendExpiryReminder(rental, "", LocalDate.of(2026, 1, 1));
        }
        );
    }

    @Test
    void twoArgOverloadShouldWorkUsingCurrentDate() {
       
        Rental rental = new Rental(1, 1, "Ali", LocalDate.now().minusDays(5), LocalDate.now().plusDays(1));

        boolean result = reminderService.sendExpiryReminder(rental, "ali@example.com");

        assertTrue(result);
    }
}