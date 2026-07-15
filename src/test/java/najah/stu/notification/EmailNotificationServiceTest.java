package najah.stu.notification;
import static org.junit.jupiter.api.Assertions.*;
import java.io.ByteArrayOutputStream;
import java.io.PrintStream;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
class EmailNotificationServiceTest {
    private EmailNotificationService emailService;
    private final PrintStream originalOut = System.out;
    private ByteArrayOutputStream outputCapture;
    @BeforeEach
    void setUp() {
        emailService = new EmailNotificationService();
        outputCapture = new ByteArrayOutputStream();
        System.setOut(new PrintStream(outputCapture));
    }
    @AfterEach
    void tearDown() {
        System.setOut(originalOut);
    }
    @Test
    void sendNotificationShouldNotThrowException() {
        assertDoesNotThrow(() -> {
            emailService.sendNotification("ali@example.com", "Rental Confirmed", "Your rental is confirmed.");
        }
        
        		
        		);
    }
    @Test
    void sendNotificationShouldPrintRecipientAndSubjectAndMessage() {
        emailService.sendNotification("ali@example.com", "Rental Confirmed", "Your rental is confirmed.");
        String output = outputCapture.toString();
        assertTrue(output.contains("ali@example.com"));
        assertTrue(output.contains("Rental Confirmed"));
        assertTrue(output.contains("Your rental is confirmed.")
        		);
    }
    @Test
    void sendNotificationShouldWorkWithEmptyMessage() {
        assertDoesNotThrow(() -> {
            emailService.sendNotification("ali@example.com", "Empty Test", "");
        }
        
        		
        		
        		);
    }

    @Test
    void emailServiceShouldImplementNotificationServiceInterface() {
        assertTrue(emailService instanceof NotificationService);
  
    
    
    
    }
}