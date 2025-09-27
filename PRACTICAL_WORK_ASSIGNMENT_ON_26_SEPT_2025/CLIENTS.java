package CONFERENCECRS;

import javax.swing.*;
import javax.swing.table.DefaultTableModel;
import javax.swing.event.ListSelectionListener;
import javax.swing.event.ListSelectionEvent;
import java.awt.event.*;
import java.sql.*;
import CONFERENCEdb.DB;

public class CLIENTS extends JPanel implements ActionListener {

    // Fields
    private JTextField idtxt = new JTextField();
    private JTextField nametxt = new JTextField();
    private JTextField emailtxt = new JTextField();
    private JPasswordField passtxt = new JPasswordField();

    // Buttons
    private JButton addBtn = new JButton("Add");
    private JButton updateBtn = new JButton("Update");
    private JButton deleteBtn = new JButton("Delete");
    private JButton loadBtn = new JButton("Load");

    // Table
    private JTable table;
    private DefaultTableModel model;

    private String currentRole;
    private int currentUserId;

    public CLIENTS(String role, int userId) {
        this.currentRole = role;
        this.currentUserId = userId;
        setLayout(null);

        // Table setup
        String[] columns = {"ClientID", "Name", "Password", "Email"};
        model = new DefaultTableModel(columns, 0);
        table = new JTable(model);
        JScrollPane sp = new JScrollPane(table);
        sp.setBounds(20, 220, 700, 250);
        add(sp);

        // Input fields
        int y = 20;
        addField("ID", idtxt, y); idtxt.setEditable(false); y += 35;
        addField("Name", nametxt, y); y += 35;
        addField("Password", passtxt, y); y += 35;
        addField("Email", emailtxt, y); y += 35;

        // Buttons
        addButtons();

        if (!currentRole.equalsIgnoreCase("admin")) {
            addBtn.setEnabled(false);
            updateBtn.setEnabled(false);
            deleteBtn.setEnabled(false);
        }

        // Button actions
        addBtn.addActionListener(this);
        updateBtn.addActionListener(this);
        deleteBtn.addActionListener(this);
        loadBtn.addActionListener(this);

        // Table selection listener
        table.getSelectionModel().addListSelectionListener(new ListSelectionListener() {
            public void valueChanged(ListSelectionEvent event) {
                if (!event.getValueIsAdjusting() && table.getSelectedRow() != -1) {
                    int row = table.getSelectedRow();
                    idtxt.setText(model.getValueAt(row, 0).toString());
                    nametxt.setText(model.getValueAt(row, 1).toString());
                    passtxt.setText(model.getValueAt(row, 2).toString());
                    emailtxt.setText(model.getValueAt(row, 3).toString());
                }
            }
        });

        // Load clients initially
        loadClients();
    }

    private void addField(String label, JComponent field, int y) {
        JLabel lbl = new JLabel(label + ":");
        lbl.setBounds(20, y, 80, 25);
        field.setBounds(110, y, 150, 25);
        add(lbl);
        add(field);
    }

    private void addButtons() {
        addBtn.setBounds(300, 20, 100, 30);
        updateBtn.setBounds(300, 60, 100, 30);
        deleteBtn.setBounds(300, 100, 100, 30);
        loadBtn.setBounds(300, 140, 100, 30);

        add(addBtn);
        add(updateBtn);
        add(deleteBtn);
        add(loadBtn);
    }

    @Override
    public void actionPerformed(ActionEvent e) {
        try (Connection con = DB.getConnection()) {
            if (con == null) {
                System.err.println("DB connection is null!");
                return;
            }

            if (e.getSource() == addBtn) {
                PreparedStatement ps = con.prepareStatement(
                        "INSERT INTO Clients(Name, PasswordHash, Email) VALUES(?,?,?)");
                ps.setString(1, nametxt.getText());
                ps.setString(2, new String(passtxt.getPassword()));
                ps.setString(3, emailtxt.getText());
                int rows = ps.executeUpdate();
                System.out.println("Rows added: " + rows);
                loadClients();

            } else if (e.getSource() == updateBtn) {
                if (idtxt.getText().isEmpty()) return;
                PreparedStatement ps = con.prepareStatement(
                        "UPDATE Clients SET Name=?, PasswordHash=?, Email=? WHERE ClientID=?");
                ps.setString(1, nametxt.getText());
                ps.setString(2, new String(passtxt.getPassword()));
                ps.setString(3, emailtxt.getText());
                ps.setInt(4, Integer.parseInt(idtxt.getText()));
                int rows = ps.executeUpdate();
                System.out.println("Rows updated: " + rows);
                loadClients();

            } else if (e.getSource() == deleteBtn) {
                if (idtxt.getText().isEmpty()) return;
                PreparedStatement ps = con.prepareStatement("DELETE FROM Clients WHERE ClientID=?");
                ps.setInt(1, Integer.parseInt(idtxt.getText()));
                int rows = ps.executeUpdate();
                System.out.println("Rows deleted: " + rows);
                loadClients();

            } else if (e.getSource() == loadBtn) {
                loadClients();
            }
        } catch (Exception ex) {
            ex.printStackTrace();
            JOptionPane.showMessageDialog(this, "Database Error: " + ex.getMessage());
        }
    }

    private void loadClients() {
        try (Connection con = DB.getConnection()) {
            if (con == null) {
                System.err.println("DB connection is null!");
                return;
            }

            model.setRowCount(0);
            Statement stmt = con.createStatement();
            ResultSet rs = stmt.executeQuery("SELECT * FROM Clients");

            boolean hasData = false;
            while (rs.next()) {
                hasData = true;
                model.addRow(new Object[]{
                        rs.getInt("ClientID"),
                        rs.getString("Name"),
                        rs.getString("PasswordHash"),
                        rs.getString("Email")
                });
            }
            if (!hasData) {
                System.out.println("No clients found in the database!");
            } else {
                System.out.println("Clients loaded successfully!");
            }
            rs.close();
            stmt.close();
        } catch (Exception ex) {
            ex.printStackTrace();
            JOptionPane.showMessageDialog(this, "Database Error: " + ex.getMessage());
        }
    }
}
