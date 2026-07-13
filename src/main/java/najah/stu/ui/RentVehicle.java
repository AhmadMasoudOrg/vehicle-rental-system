package najah.stu.ui;

import java.awt.GridLayout;
import java.time.LocalDate;
import java.time.format.DateTimeParseException;

import javax.swing.JButton;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JOptionPane;
import javax.swing.JPanel;
import javax.swing.JTextField;

import najah.stu.domain.Rental;
import najah.stu.service.CustomerService;
import najah.stu.service.RentalService;

public class RentVehicle extends JFrame {

    private final RentalService rentalService;
    private final CustomerService customerService;

    private JTextField vehicleIdField;
    private JTextField startDateField;
    private JTextField endDateField;

    private JButton rentButton;
    private JButton cancelButton;

    public RentVehicle(
            RentalService rentalService,
            CustomerService customerService) {

        this.rentalService = rentalService;
        this.customerService = customerService;

        setTitle("Rent Vehicle");
        setSize(450, 300);
        setDefaultCloseOperation(JFrame.DISPOSE_ON_CLOSE);
        setLocationRelativeTo(null);

        initializeUI();

        setVisible(true);
    }

    private void initializeUI() {

        JPanel panel = new JPanel(new GridLayout(5, 2, 10, 10));

        JLabel vehicleIdLabel = new JLabel("Vehicle ID:");
        vehicleIdField = new JTextField();

        JLabel startDateLabel =
                new JLabel("Start Date (yyyy-MM-dd):");

        startDateField = new JTextField();

        JLabel endDateLabel =
                new JLabel("End Date (yyyy-MM-dd):");

        endDateField = new JTextField();

        rentButton = new JButton("Rent");
        cancelButton = new JButton("Cancel");

        panel.add(vehicleIdLabel);
        panel.add(vehicleIdField);

        panel.add(startDateLabel);
        panel.add(startDateField);

        panel.add(endDateLabel);
        panel.add(endDateField);

        panel.add(new JLabel());
        panel.add(new JLabel());

        panel.add(rentButton);
        panel.add(cancelButton);

        add(panel);

        rentButton.addActionListener(e -> rentVehicle());

        cancelButton.addActionListener(e -> dispose());
    }

    private void rentVehicle() {

        try {

            String vehicleIdText = vehicleIdField.getText().trim();

            String startDateText = startDateField.getText().trim();

            String endDateText = endDateField.getText().trim();

            if (vehicleIdText.isEmpty()|| startDateText.isEmpty()|| endDateText.isEmpty()) {

                JOptionPane.showMessageDialog(
                        this,
                        "Please fill in all fields.",
                        "Missing Information",
                        JOptionPane.WARNING_MESSAGE
                );

                return;
            }

            int vehicleId = Integer.parseInt(vehicleIdText);

            LocalDate startDate = LocalDate.parse(startDateText);

            LocalDate endDate = LocalDate.parse(endDateText);

            String customerName = customerService.getLoggedCustomer().getUsername();

            Rental rental = rentalService.rentVehicle(vehicleId,customerName,startDate,endDate);

            JOptionPane.showMessageDialog(
                    this,
                    "Vehicle rented successfully!\n"
                            + "Rental ID: "
                            + rental.getId()
                            + "\nVehicle ID: "
                            + rental.getVehicleId(),
                    "Rental Successful",
                    JOptionPane.INFORMATION_MESSAGE
            );

            dispose();

        } catch (NumberFormatException exception) {

            JOptionPane.showMessageDialog(
                    this,
                    "Vehicle ID must be a number.",
                    "Invalid Vehicle ID",
                    JOptionPane.ERROR_MESSAGE
            );

        } catch (DateTimeParseException exception) {

            JOptionPane.showMessageDialog(
                    this,
                    "Enter dates using this format:\n"
                            + "yyyy-MM-dd\n"
                            + "Example: 2026-07-20",
                    "Invalid Date",
                    JOptionPane.ERROR_MESSAGE
            );

        } catch (IllegalStateException exception) {

            JOptionPane.showMessageDialog(
                    this,
                    exception.getMessage(),
                    "Login Error",
                    JOptionPane.ERROR_MESSAGE
            );

        } catch (RuntimeException exception) {

            JOptionPane.showMessageDialog(
                    this,
                    exception.getMessage(),
                    "Rental Failed",
                    JOptionPane.ERROR_MESSAGE
            );
        }
    }
}