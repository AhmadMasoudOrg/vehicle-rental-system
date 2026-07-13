package najah.stu.service;
import najah.stu.domain.Manager;
public class ManagerService {
    private Manager manager;
    private boolean loggedIn;

    public ManagerService() {
        manager = new Manager("admin", "1234");
        loggedIn = false;
    }
  
    
    
    public boolean login(String username, String password) {

        if (username == null || password == null) {
            return false;
        }

        if (manager.getUsername().equals(username)
                && manager.getPassword().equals(password)) {

            loggedIn = true;
            return true;
        }

        
        
        
        return false;
    }

    public void logout() {
        loggedIn = false;
    }

    public boolean isLoggedIn() {
        return loggedIn;
    }

    
    public void requireLogin() {
        if (!loggedIn) {
            throw new IllegalStateException("Manager must be logged in to perform this action.");
        }
        
        
    }
    
    
    
}