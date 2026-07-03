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
}