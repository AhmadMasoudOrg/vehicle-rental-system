package najah.stu.service;

import najah.stu.domain.Manager;
import najah.stu.repository.ManagerRepository;

public class ManagerService {

    private ManagerRepository managerRepository;
    private Manager loggedInManager;

    public ManagerService() {
        managerRepository = new ManagerRepository();
        loggedInManager = null;
    }

    public boolean login(String username, String password) {

        if (username == null || password == null) {
            return false;
        }

        Manager manager = managerRepository.findByUsername(username);

        if (manager != null &&
                manager.getPassword().equals(password)) {

            loggedInManager = manager;
            return true;
        }

        return false;
    }

    public void logout() {
        loggedInManager = null;
    }

    public boolean isLoggedIn() {
        return loggedInManager != null;
    }

    public void requireLogin() {

        if (!isLoggedIn()) {
            throw new IllegalStateException(
                    "Manager must be logged in to perform this action."
            );
        }
    }

    public Manager getLoggedInManager() {
        return loggedInManager;
    }
}