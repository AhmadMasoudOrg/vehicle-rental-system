package najah.stu.repository;
import static org.junit.jupiter.api.Assertions.*;
import najah.stu.domain.Manager;
import org.junit.jupiter.api.Test;
class ManagerRepositoryTest {
    private ManagerRepository managerRepository = new ManagerRepository();
    @Test
    void findByUsernameShouldReturnNullWhenUsernameDoesNotExist() {
        Manager result = managerRepository.findByUsername("thisUsernameDoesNotExist999");
        assertNull(result);
    }
    @Test
    void findByUsernameShouldReturnNullForEmptyUsername() {
        Manager result = managerRepository.findByUsername("");
        assertNull(result);
    }
    
}