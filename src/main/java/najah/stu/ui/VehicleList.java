package najah.stu.ui;

import java.awt.BorderLayout;
import java.util.List;

import javax.swing.JButton;
import javax.swing.JFrame;
import javax.swing.JPanel;
import javax.swing.JScrollPane;
import javax.swing.JTable;
import javax.swing.table.DefaultTableModel;

import najah.stu.domain.Vehicle;
import najah.stu.service.VehicleService;

public class VehicleList extends JFrame {

    private JTable table;
    private DefaultTableModel model;
    private JButton closeButton;

    public VehicleList(VehicleService vehicleService) {

        setTitle("Available Vehicles");
        setSize(700,400);
        setLocationRelativeTo(null);
        setDefaultCloseOperation(JFrame.DISPOSE_ON_CLOSE);
        setLayout(new BorderLayout(10,10));

        model = new DefaultTableModel(
                new Object[]{"ID","Brand","Model","Type","Daily Rate"},
                0
        ) {

            @Override
            public boolean isCellEditable(int row,int column) {

                return false;
            }
        };

        List<Vehicle> vehicles = vehicleService.getAvailableVehicles();

        for (Vehicle vehicle : vehicles) {

            model.addRow(
                    new Object[]{
                            vehicle.getId(),
                            vehicle.getBrand(),
                            vehicle.getModel(),
                            vehicle.getType(),
                            vehicle.getDailyRate()
                    }
            );
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

        add(
                buttonPanel,
                BorderLayout.SOUTH
        );

        setVisible(true);
    }
}