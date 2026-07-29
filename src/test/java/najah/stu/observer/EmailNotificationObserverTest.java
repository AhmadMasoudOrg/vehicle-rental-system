package najah.stu.observer;

import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.verifyNoInteractions;

import org.junit.jupiter.api.Test;

import najah.stu.notification.NotificationService;

import static org.mockito.Mockito.mock;

public class EmailNotificationObserverTest {

    @Test
    public void constructorShouldThrowWhenNotificationServiceIsNull() {

        assertThrows(
                IllegalArgumentException.class,
                () -> new EmailNotificationObserver(null)
        );
    }

    @Test
    public void updateShouldSendNotification() {

        NotificationService notificationService = mock(NotificationService.class);

        EmailNotificationObserver observer =
                new EmailNotificationObserver(notificationService);

        observer.update(
                "customer@gmail.com",
                "Rental Reminder",
                "Your rental will expire soon."
        );

        verify(notificationService).sendNotification(
                "customer@gmail.com",
                "Rental Reminder",
                "Your rental will expire soon."
        );
    }

    @Test
    public void observerShouldNotSendBeforeUpdateIsCalled() {

        NotificationService notificationService = mock(NotificationService.class);

        new EmailNotificationObserver(notificationService);

        verifyNoInteractions(notificationService);
    }
}