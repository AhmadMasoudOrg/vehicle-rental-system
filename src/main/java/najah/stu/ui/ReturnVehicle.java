package najah.stu.ui;

import java.awt.Font;
import java.util.List;

import javax.swing.JButton;
import javax.swing.JComboBox;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JOptionPane;
import javax.swing.WindowConstants;

import najah.stu.domain.Customer;
import najah.stu.domain.Rental;
import najah.stu.service.CustomerService;
import najah.stu.service.RentalService;

public class ReturnVehicle extends JFrame {

    private JComboBox<Rental> rentalComboBox;
    private JButton returnButton;

    private RentalService rentalService;
    private CustomerService customerService;

    public ReturnVehicle(RentalService rentalService,
                         CustomerService customerService) {

        this.rentalService = rentalService;
        this.customerService = customerService;

        setTitle("Return Vehicle");
        setSize(500, 240);
        setLayout(null);
        setLocationRelativeTo(null);
        setDefaultCloseOperation(WindowConstants.DISPOSE_ON_CLOSE);

        JLabel title = new JLabel("Return Vehicle");
        title.setFont(new Font("Arial", Font.BOLD, 20));
        title.setBounds(170, 20, 200, 30);
        add(title);

        JLabel rentalLabel = new JLabel("Active Rental:");
        rentalLabel.setBounds(40, 80, 100, 25);
        add(rentalLabel);

        rentalComboBox = new JComboBox<>();
        rentalComboBox.setBounds(140, 80, 300, 25);
        add(rentalComboBox);

        rentalComboBox.setRenderer((list, value, index, isSelected, cellHasFocus) -> {

            JLabel label = new JLabel();

            if (value != null) {
                label.setText(
                        "Rental ID: "
                                + value.getId()
                                + " - Vehicle ID: "
                                + value.getVehicleId()
                                + " - "
                                + value.getStartDate()
                                + " to "
                                + value.getEndDate()
                );
            }

            return label;
        });

        returnButton = new JButton("Return");
        returnButton.setBounds(180, 140, 120, 30);
        add(returnButton);

        loadActiveRentals();

        returnButton.addActionListener(e -> returnVehicle());

        setVisible(true);
    }

    private void loadActiveRentals() {

        Customer customer = customerService.getLoggedCustomer();

        if (customer == null) {

            JOptionPane.showMessageDialog(
                    this,
                    "Customer must login first."
            );

            return;
        }

        List<Rental> rentals = rentalService.getAllRentals();

        for (Rental rental : rentals) {

            boolean belongsToCustomer = rental.getCustomerName().equalsIgnoreCase(customer.getUsername());

            if (belongsToCustomer && rental.isActive()) {
                rentalComboBox.addItem(rental);
            }
        }

        if (rentalComboBox.getItemCount() == 0) {
            returnButton.setEnabled(false);
        }
    }

    private void returnVehicle() {

        try {

            Rental selectedRental = (Rental) rentalComboBox.getSelectedItem();

            if (selectedRental == null) {

                JOptionPane.showMessageDialog(
                        this,
                        "You do not have any active rentals."
                );

                return;
            }

            int rentalId = selectedRental.getId();

            double totalCost = rentalService.calculateRentalCost(rentalId);

            Rental rental = rentalService.returnVehicle(rentalId);

            JOptionPane.showMessageDialog(
                    this,
                    "Vehicle returned successfully.\n"
                            + "Rental ID: "
                            + rental.getId()
                            + "\nTotal cost: $"
                            + totalCost
            );

            dispose();

        } catch (IllegalArgumentException | IllegalStateException exception) {

            JOptionPane.showMessageDialog(
                    this,
                    exception.getMessage()
            );

        } catch (Exception exception) {

            JOptionPane.showMessageDialog(
                    this,
                    "Failed to return vehicle: "
                            + exception.getMessage()
            );
        }
    }
}
