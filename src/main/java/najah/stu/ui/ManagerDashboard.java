package najah.stu.ui;
import javax.swing.*;



public class ManagerDashboard extends JFrame {

    private JButton viewVehiclesButton;
    private JButton logoutButton;

    public ManagerDashboard() {

        setTitle("Manager Dashboard");
        setSize(400,250);
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        setLocationRelativeTo(null);
        setLayout(null);

        JLabel title = new JLabel("Vehicle Rental Management");
        title.setBounds(90,20,250,30);
        add(title);

        viewVehiclesButton = new JButton("View Available Vehicles");
        viewVehiclesButton.setBounds(80,70,220,35);
        viewVehiclesButton.addActionListener(e -> {
        new VehicleList();
        });
        add(viewVehiclesButton);

        logoutButton = new JButton("Logout");
        logoutButton.setBounds(80,130,220,35);
        logoutButton.addActionListener(e -> {

    int choice = JOptionPane.showConfirmDialog(
            this,
            "Are you sure you want to logout?",
            "Logout",
            JOptionPane.YES_NO_OPTION,
            JOptionPane.QUESTION_MESSAGE);

    if (choice == JOptionPane.YES_OPTION) {

        JOptionPane.showMessageDialog(
                this,
                "Logout Successful",
                "Success",
                JOptionPane.INFORMATION_MESSAGE);

        dispose();
        new Login();
    }

});
        add(logoutButton);

        setVisible(true);
    }
}