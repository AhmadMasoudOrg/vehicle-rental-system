package najah.stu.ui;

import java.util.List;

import javax.swing.JButton;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JOptionPane;

import najah.stu.domain.Customer;
import najah.stu.domain.Rental;
import najah.stu.notification.EmailNotificationService;
import najah.stu.notification.NotificationService;
import najah.stu.repository.RentalRepository;
import najah.stu.repository.VehicleRepository;
import najah.stu.service.CustomerService;
import najah.stu.service.RentalReminderService;
import najah.stu.service.RentalService;
import najah.stu.service.VehicleService;

public class CustomerDashboard extends JFrame {

    private final CustomerService customerService;
    private final RentalService rentalService;
    private final VehicleService vehicleService;
    private final RentalReminderService reminderService;

    private JButton viewVehiclesButton;
    private JButton rentVehicleButton;
    private JButton reminderButton;
    private JButton logoutButton;

    public CustomerDashboard(CustomerService customerService) {

        this.customerService = customerService;

        VehicleRepository vehicleRepository = new VehicleRepository();
        RentalRepository rentalRepository = new RentalRepository();

        this.rentalService = new RentalService(vehicleRepository, rentalRepository, customerService);
        this.vehicleService = new VehicleService(vehicleRepository);

        NotificationService notificationService = new EmailNotificationService();
        this.reminderService = new RentalReminderService(notificationService);

        setTitle("Customer Dashboard");
        setSize(430, 380);
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        setLocationRelativeTo(null);
        setLayout(null);

        Customer customer = customerService.getLoggedCustomer();

        JLabel titleLabel = new JLabel("Welcome, " + customer.getUsername());
        titleLabel.setBounds(120, 20, 220, 30);
        add(titleLabel);

        viewVehiclesButton = new JButton("View Available Vehicles");
        viewVehiclesButton.setBounds(90, 70, 240, 35);
        add(viewVehiclesButton);

        rentVehicleButton = new JButton("Rent Vehicle");
        rentVehicleButton.setBounds(90, 120, 240, 35);
        add(rentVehicleButton);

        reminderButton = new JButton("Check Expiry Reminders");
        reminderButton.setBounds(90, 170, 240, 35);
        add(reminderButton);

        logoutButton = new JButton("Logout");
        logoutButton.setBounds(90, 220, 240, 35);
        add(logoutButton);

        viewVehiclesButton.addActionListener(e -> new VehicleList(vehicleService));

        rentVehicleButton.addActionListener(e -> new RentVehicle(rentalService, customerService));

        reminderButton.addActionListener(e -> checkExpiryReminders());

        logoutButton.addActionListener(e -> logout());

        setVisible(true);
    }

    private void checkExpiryReminders() {

        Customer customer = customerService.getLoggedCustomer();

        if (customer == null) {

            JOptionPane.showMessageDialog(this, "Customer must login first.");
            return;
        }

        List<Rental> rentals = rentalService.getAllRentals();

        int remindersSent = 0;

        for (Rental rental : rentals) {

            boolean belongsToCustomer = rental.getCustomerName().equalsIgnoreCase(customer.getUsername());

            if (!belongsToCustomer) {
                continue;
            }

            boolean sent = reminderService.sendExpiryReminder(rental, customer.getEmail());

            if (sent) {
                remindersSent++;
            }
        }

        if (remindersSent == 0) {

            JOptionPane.showMessageDialog(this, "There are no rentals expiring within 2 days.");

        } else {

            JOptionPane.showMessageDialog(this, remindersSent + " reminder(s) sent.\nCheck the terminal for email details.");
        }
    }

    private void logout() {

        int choice = JOptionPane.showConfirmDialog(this, "Are you sure you want to logout?", "Logout", JOptionPane.YES_NO_OPTION);

        if (choice == JOptionPane.YES_OPTION) {

            customerService.logout();

            JOptionPane.showMessageDialog(this, "Logout Successful");

            dispose();
            new Login();
        }
    }
}