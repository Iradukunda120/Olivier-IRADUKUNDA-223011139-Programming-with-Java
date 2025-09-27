package CONFERENCEFORM;

import javax.swing.*;
import java.awt.event.*;
import java.sql.*;

import CONFERENCECRS.CRS;   // import CRS class
import CONFERENCEdb.DB;     // import DB class

public class LoginCRS extends JFrame implements ActionListener {

    JTextField userTxt = new JTextField();
    JPasswordField passTxt = new JPasswordField();
    JButton loginBtn = new JButton("Login");
    JButton cancelBtn = new JButton("Cancel");

    // Admin credentials
    private final String ADMIN_USER = "admin";
    private final String ADMIN_PASS = "admin123";

    public LoginCRS() {
        setTitle("CRS Login");
        setSize(350, 200);
        setLayout(null);
        setLocationRelativeTo(null); // center window
        setDefaultCloseOperation(EXIT_ON_CLOSE);

        JLabel userLabel = new JLabel("Username:");
        JLabel passLabel = new JLabel("Password:");

        userLabel.setBounds(30, 30, 80, 25);
        passLabel.setBounds(30, 70, 80, 25);

        userTxt.setBounds(120, 30, 160, 25);
        passTxt.setBounds(120, 70, 160, 25);

        loginBtn.setBounds(50, 120, 100, 30);
        cancelBtn.setBounds(180, 120, 100, 30);

        add(userLabel); add(passLabel);
        add(userTxt); add(passTxt);
        add(loginBtn); add(cancelBtn);

        loginBtn.addActionListener(this);
        cancelBtn.addActionListener(this);

        setVisible(true);
    }

    @Override
    public void actionPerformed(ActionEvent e) {
        if (e.getSource() == cancelBtn) {
            System.exit(0);
        } else if (e.getSource() == loginBtn) {
            String username = userTxt.getText();
            String password = new String(passTxt.getPassword());

            // Fixed admin login
            if (username.equals(ADMIN_USER) && password.equals(ADMIN_PASS)) {
                JOptionPane.showMessageDialog(this, "Login Successful as Admin!");
                dispose();
                new CRS("admin", 0); // Open CRS as admin
                return;
            }

            // Database login
            Connection con = null;
            try {
                con = DB.getConnection();
                String sql = "SELECT * FROM Users WHERE Username = ? AND PasswordHash = ?";
                PreparedStatement ps = con.prepareStatement(sql);
                ps.setString(1, username);
                ps.setString(2, password);
                ResultSet rs = ps.executeQuery();

                if (rs.next()) {
                    String role = rs.getString("Role");
                    int userId = rs.getInt("UserID");
                    JOptionPane.showMessageDialog(this, "Login Successful! Role: " + role);
                    dispose();
                    new CRS(role, userId);
                } else {
                    JOptionPane.showMessageDialog(this, "Invalid username or password!", "Error", JOptionPane.ERROR_MESSAGE);
                }

                rs.close();
                ps.close();
            } catch (Exception ex) {
                ex.printStackTrace();
                JOptionPane.showMessageDialog(this, "Database Error: " + ex.getMessage(), "Error", JOptionPane.ERROR_MESSAGE);
            } finally {
                try { if (con != null) con.close(); } catch (SQLException ex2) { ex2.printStackTrace(); }
            }
        }
    }

    public static void main(String[] args) {
        new LoginCRS();
    }
}
