package najah.stu.ui;

import java.util.List;

import javax.swing.*;
import javax.swing.table.DefaultTableModel;

import najah.stu.domain.Vehicle;
import najah.stu.service.VehicleService;

public class VehicleList extends JFrame {

    private JTable table;
    private DefaultTableModel model;
    private JButton closeButton;
    private JPanel  panel;
    public VehicleList() {

        VehicleService vehicleService = new VehicleService();

        setTitle("Available Vehicles");
        setSize(600,400);
        setLocationRelativeTo(null);
        setDefaultCloseOperation(JFrame.DISPOSE_ON_CLOSE);
        panel = new JPanel();
        model = new DefaultTableModel();

        model.addColumn("ID");
        model.addColumn("Brand");
        model.addColumn("Model");

        List<Vehicle> vehicles = vehicleService.getAvailableVehicles();

        for (Vehicle vehicle : vehicles) {

            model.addRow(new Object[] {
                    vehicle.getId(),
                    vehicle.getBrand(),
                    vehicle.getModel()
            });

        }

        table = new JTable(model);
        panel.add(new JScrollPane(table));

        closeButton = new JButton("Close");
        closeButton.setBounds(150, 150, 100, 30);
        
        closeButton.addActionListener(e -> {
        dispose();
        });
        panel.add(closeButton);
        add(panel);
        setVisible(true);

    }

}