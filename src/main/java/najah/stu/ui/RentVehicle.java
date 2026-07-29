package najah.stu.ui;

import java.awt.GridLayout;
import java.time.LocalDate;
import java.time.ZoneId;
import java.util.Date;
import java.util.List;

import javax.swing.JButton;
import javax.swing.JComboBox;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JOptionPane;
import javax.swing.JPanel;
import javax.swing.JSpinner;
import javax.swing.SpinnerDateModel;

import najah.stu.domain.Rental;
import najah.stu.domain.Vehicle;
import najah.stu.service.CustomerService;
import najah.stu.service.RentalService;
import najah.stu.service.VehicleService;

public class RentVehicle extends JFrame {

    private final RentalService rentalService;
    private final CustomerService customerService;
    private final VehicleService vehicleService;

    private JComboBox<Vehicle> vehicleComboBox;
    private JSpinner startDateSpinner;
    private JSpinner endDateSpinner;

    private JButton rentButton;
    private JButton cancelButton;

    public RentVehicle(
            RentalService rentalService,
            CustomerService customerService,
            VehicleService vehicleService) {

        this.rentalService = rentalService;
        this.customerService = customerService;
        this.vehicleService = vehicleService;

        setTitle("Rent Vehicle");
        setSize(500, 300);
        setDefaultCloseOperation(JFrame.DISPOSE_ON_CLOSE);
        setLocationRelativeTo(null);

        initializeUI();

        setVisible(true);
    }

    private void initializeUI() {

        JPanel panel = new JPanel(new GridLayout(5, 2, 10, 10));

        JLabel vehicleLabel = new JLabel("Available Vehicle:");

        vehicleComboBox = new JComboBox<>();

        loadAvailableVehicles();

        vehicleComboBox.setRenderer((list, value, index, isSelected, cellHasFocus) -> {

            JLabel label = new JLabel();

            if (value != null) {
               label.setText(
        value.getBrand()
                + " "
                + value.getModel()
                + " - "
                + value.getType()
                + " (ID: "
                + value.getId()
                + ") - "
                + value.getDailyRate()
                + " per day"
                 );
            }

            return label;
        });

        JLabel startDateLabel = new JLabel("Start Date:");

        SpinnerDateModel startDateModel = new SpinnerDateModel();

        startDateSpinner = new JSpinner(startDateModel);

        JSpinner.DateEditor startDateEditor = new JSpinner.DateEditor(
                startDateSpinner,
                "yyyy-MM-dd"
        );

        startDateSpinner.setEditor(startDateEditor);

        JLabel endDateLabel = new JLabel("End Date:");

        SpinnerDateModel endDateModel = new SpinnerDateModel();

        endDateSpinner = new JSpinner(endDateModel);

        JSpinner.DateEditor endDateEditor = new JSpinner.DateEditor(
                endDateSpinner,
                "yyyy-MM-dd"
        );

        endDateSpinner.setEditor(endDateEditor);

        rentButton = new JButton("Rent");
        cancelButton = new JButton("Cancel");

        panel.add(vehicleLabel);
        panel.add(vehicleComboBox);

        panel.add(startDateLabel);
        panel.add(startDateSpinner);

        panel.add(endDateLabel);
        panel.add(endDateSpinner);

        panel.add(new JLabel());
        panel.add(new JLabel());

        panel.add(rentButton);
        panel.add(cancelButton);

        add(panel);

        rentButton.addActionListener(e -> rentVehicle());

        cancelButton.addActionListener(e -> dispose());
    }

    private void loadAvailableVehicles() {

        List<Vehicle> vehicles = vehicleService.getAvailableVehicles();

        for (Vehicle vehicle : vehicles) {

            vehicleComboBox.addItem(vehicle);
        }
    }

    private void rentVehicle() {

        try {

            Vehicle selectedVehicle = (Vehicle) vehicleComboBox.getSelectedItem();

            if (selectedVehicle == null) {

                JOptionPane.showMessageDialog(
                        this,
                        "There are no available vehicles.",
                        "No Vehicles",
                        JOptionPane.WARNING_MESSAGE
                );

                return;
            }

            Date selectedStartDate = (Date) startDateSpinner.getValue();

            Date selectedEndDate = (Date) endDateSpinner.getValue();

            LocalDate startDate = selectedStartDate.toInstant()
                    .atZone(ZoneId.systemDefault())
                    .toLocalDate();

            LocalDate endDate = selectedEndDate.toInstant()
                    .atZone(ZoneId.systemDefault())
                    .toLocalDate();

            String customerName = customerService
                    .getLoggedCustomer()
                    .getUsername();

            Rental rental = rentalService.rentVehicle(
                    selectedVehicle.getId(),
                    customerName,
                    startDate,
                    endDate
            );

            JOptionPane.showMessageDialog(
                    this,
                    "Vehicle rented successfully!\n"
                            + "Rental ID: "
                            + rental.getId()
                            + "\nVehicle: "
                            + selectedVehicle.getBrand()
                            + " "
                            + selectedVehicle.getModel(),
                    "Rental Successful",
                    JOptionPane.INFORMATION_MESSAGE
            );

            dispose();

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