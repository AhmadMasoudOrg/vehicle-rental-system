package najah.stu.ui;

import java.awt.BorderLayout;
import java.util.List;

import javax.swing.*;
import javax.swing.table.DefaultTableModel;

import najah.stu.domain.Vehicle;
import najah.stu.service.VehicleService;

public class VehicleList extends JFrame {

    private JTable table;
    private DefaultTableModel model;
    private JButton closeButton;

    public VehicleList() {

        VehicleService vehicleService =
                new VehicleService();

        setTitle("Available Vehicles");
        setSize(600, 400);
        setLocationRelativeTo(null);
        setDefaultCloseOperation(JFrame.DISPOSE_ON_CLOSE);
        setLayout(new BorderLayout(10, 10));

        model = new DefaultTableModel(
                new Object[]{"ID", "Brand", "Model"},
                0
        ) {
            @Override
            public boolean isCellEditable(
                    int row,
                    int column) {

                return false;
            }
        };

        List<Vehicle> vehicles =
                vehicleService.getAvailableVehicles();

        for (Vehicle vehicle : vehicles) {

            model.addRow(new Object[]{
                    vehicle.getId(),
                    vehicle.getBrand(),
                    vehicle.getModel()
            });
        }

        table = new JTable(model);

        add(
                new JScrollPane(table),
                BorderLayout.CENTER
        );

        closeButton = new JButton("Close");

        closeButton.addActionListener(e -> dispose());

        JPanel buttonPanel = new JPanel();
        buttonPanel.add(closeButton);

        add(buttonPanel, BorderLayout.SOUTH);

        setVisible(true);
    }
}