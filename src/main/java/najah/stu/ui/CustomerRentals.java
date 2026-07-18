package najah.stu.ui;

import java.util.List;
import java.util.Optional;

import javax.swing.JFrame;
import javax.swing.JScrollPane;
import javax.swing.JTable;
import javax.swing.table.DefaultTableModel;

import najah.stu.domain.Customer;
import najah.stu.domain.Rental;
import najah.stu.domain.Vehicle;
import najah.stu.repository.VehicleRepository;
import najah.stu.service.CustomerService;
import najah.stu.service.RentalService;

public class CustomerRentals extends JFrame {

    private final RentalService rentalService;
    private final CustomerService customerService;
    private final VehicleRepository vehicleRepository;

    private JTable rentalsTable;
    private DefaultTableModel tableModel;

    public CustomerRentals(RentalService rentalService,CustomerService customerService) {

        this.rentalService = rentalService;
        this.customerService = customerService;
        this.vehicleRepository = new VehicleRepository();

        setTitle("My Rentals");
        setSize(950,400);
        setDefaultCloseOperation(JFrame.DISPOSE_ON_CLOSE);
        setLocationRelativeTo(null);
        setLayout(null);

        String[] columns = {
                "Rental ID",
                "Vehicle",
                "Type",
                "Daily Rate",
                "Customer",
                "Start Date",
                "End Date",
                "Return Date",
                "Status"
        };

        tableModel = new DefaultTableModel(columns,0) {

            @Override
            public boolean isCellEditable(int row,int column) {
                return false;
            }
        };

        rentalsTable = new JTable(tableModel);

        JScrollPane scrollPane = new JScrollPane(rentalsTable);
        scrollPane.setBounds(20,20,895,320);
        add(scrollPane);

        loadCustomerRentals();

        setVisible(true);
    }

    private void loadCustomerRentals() {

        Customer customer = customerService.getLoggedCustomer();

        if (customer == null) {
            return;
        }

        List<Rental> rentals = rentalService.getAllRentals();

        for (Rental rental : rentals) {

            boolean belongsToCustomer = rental.getCustomerName().equalsIgnoreCase(customer.getUsername());

            if (belongsToCustomer) {

                Optional<Vehicle> vehicleOptional = vehicleRepository.findById(rental.getVehicleId());

                String vehicleName = "Vehicle not found";
                String vehicleType = "Unknown";
                double dailyRate = 0.0;

                if (vehicleOptional.isPresent()) {

                    Vehicle vehicle = vehicleOptional.get();

                    vehicleName = vehicle.getBrand() + " " + vehicle.getModel();

                    vehicleType = vehicle.getType().toString();

                    dailyRate = vehicle.getDailyRate();
                }

                Object[] row = {
                        rental.getId(),
                        vehicleName,
                        vehicleType,
                        "$" + dailyRate,
                        rental.getCustomerName(),
                        rental.getStartDate(),
                        rental.getEndDate(),
                        rental.getReturnDate(),
                        rental.getStatus()
                };

                tableModel.addRow(row);
            }
        }
    }
}