import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.PreparedStatement;
import java.sql.SQLException;
import java.util.Locale;
import java.util.ResourceBundle;

public class EmployeeManagement extends JFrame {
    private JLabel lblFirstName, lblLastName, lblEmail;
    private JTextField txtFirstName, txtLastName, txtEmail;
    private JButton btnSave;
    private JComboBox<String> languageSelector;
    private ResourceBundle bundle;

    public EmployeeManagement() {
        setTitle("Employee Management");
        setLayout(new GridLayout(5, 2, 10, 10));
        setSize(300, 200);
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);

        languageSelector = new JComboBox<>(new String[]{"English", "Farsi", "Japanese"});
        lblFirstName = new JLabel();
        lblLastName = new JLabel();
        lblEmail = new JLabel();
        txtFirstName = new JTextField();
        txtLastName = new JTextField();
        txtEmail = new JTextField();
        btnSave = new JButton();

        // Add components to frame
        add(new JLabel("Select Language:"));
        add(languageSelector);
        add(lblFirstName);
        add(txtFirstName);
        add(lblLastName);
        add(txtLastName);
        add(lblEmail);
        add(txtEmail);
        add(new JLabel()); // Empty cell
        add(btnSave);

        // Load default language
        loadLanguage("en");

        // Language selector action
        languageSelector.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                String selectedLanguage = (String) languageSelector.getSelectedItem();
                switch (selectedLanguage) {
                    case "Farsi":
                        loadLanguage("fa");
                        break;
                    case "Japanese":
                        loadLanguage("ja");
                        break;
                    default:
                        loadLanguage("en");
                }
            }
        });

        btnSave.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                savetoDB();
            }
        });

        setVisible(true);
    }

    private void loadLanguage(String languageCode) {
        Locale locale = new Locale(languageCode);
        bundle = ResourceBundle.getBundle("Messages", locale);

        setTitle(bundle.getString("title"));
        lblFirstName.setText(bundle.getString("firstName"));
        lblLastName.setText(bundle.getString("lastName"));
        lblEmail.setText(bundle.getString("email"));
        btnSave.setText(bundle.getString("save"));
    }

    private void savetoDB() {
        String first_name = txtFirstName.getText();
        String last_name = txtLastName.getText();
        String email = txtEmail.getText();

        System.out.println("First Name: " + first_name);
        System.out.println("Last Name: " + last_name);
        System.out.println("Email: " + email);

        String url = "jdbc:mariadb://localhost:3306/localizeddatabase";
        String user = "root";
        String password = "1234";

        String selectedLanguage = (String) languageSelector.getSelectedItem();
        String tableName;

        // Determine the table name based on selected language
        switch (selectedLanguage) {
            case "Farsi":
                tableName = "employee_fa";
                break;
            case "Japanese":
                tableName = "employee_ja";
                break;
            default:
                tableName = "employee_en";
                break;
        }

        String query = "INSERT INTO " + tableName + " (first_name, last_name, email) VALUES (?, ?, ?)";

        try (Connection connection = DriverManager.getConnection(url, user, password);
             PreparedStatement preparedStatement = connection.prepareStatement(query)) {

            preparedStatement.setString(1, first_name);
            preparedStatement.setString(2, last_name);
            preparedStatement.setString(3, email);
            preparedStatement.executeUpdate();
            JOptionPane.showMessageDialog(this, "Employee saved successfully!");
        } catch (SQLException e) {
            e.printStackTrace();
            JOptionPane.showMessageDialog(this, "Error saving employee: " + e.getMessage());
        }
    }


    public static void main(String[] args) {
        new EmployeeManagement();
    }
}
