package CONFERENCE;

import javax.swing.*;
import javax.swing.table.DefaultTableModel;
import java.awt.event.*;
import java.sql.*;
import CONFERENCEdb.DB;

public class PARTICIPANTS extends JPanel implements ActionListener {

    // Fields
    private JTextField idtxt = new JTextField();
    private JTextField nametxt = new JTextField();
    private JTextField emailtxt = new JTextField();
    private JTextField phoneTxt = new JTextField();

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

    public PARTICIPANTS(String role, int userid) {
        this.currentRole = role;
        this.currentUserId = userid;
        setLayout(null);

        // Table setup
        String[] columns = {"ParticipantID", "Name", "Email", "Phone"};
        model = new DefaultTableModel(columns, 0);
        table = new JTable(model);
        JScrollPane sp = new JScrollPane(table);
        sp.setBounds(20, 220, 700, 250);
        add(sp);

        // Add input fields
        int y = 20;
        addField("ID", idtxt, y); y += 35;
        addField("Name", nametxt, y); y += 35;
        addField("Email", emailtxt, y); y += 35;
        addField("Phone", phoneTxt, y); y += 35;

        // Add buttons
        addButtons();

        // Button actions
        addBtn.addActionListener(this);
        updateBtn.addActionListener(this);
        deleteBtn.addActionListener(this);
        loadBtn.addActionListener(this);

        // Only Admin can modify participants
        if (!currentRole.equalsIgnoreCase("admin")) {
            addBtn.setEnabled(false);
            updateBtn.setEnabled(false);
            deleteBtn.setEnabled(false);
        }

        // Table selection listener
        table.getSelectionModel().addListSelectionListener(table);
            if (!((ListSelectionModel) table).getValueIsAdjusting() && table.getSelectedRow() != -1) {
                int row = table.getSelectedRow();
                idtxt.setText(model.getValueAt(row, 0).toString());
                nametxt.setText(model.getValueAt(row, 1).toString());
                emailtxt.setText(model.getValueAt(row, 2).toString());
                phoneTxt.setText(model.getValueAt(row, 3).toString());
            }

        // Load participants initially
        loadParticipants();
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

        add(addBtn); add(updateBtn); add(deleteBtn); add(loadBtn);
    }

    @Override
    public void actionPerformed(ActionEvent e) {
        try (Connection con = DB.getConnection()) {
            if (e.getSource() == addBtn) {
                PreparedStatement ps = con.prepareStatement(
                        "INSERT INTO Participants(Name, Email, Phone) VALUES(?,?,?)");
                ps.setString(1, nametxt.getText());
                ps.setString(2, emailtxt.getText());
                ps.setString(3, phoneTxt.getText());
                ps.executeUpdate();
                JOptionPane.showMessageDialog(this, "Participant Added!");
                loadParticipants();

            } else if (e.getSource() == updateBtn) {
                if (idtxt.getText().isEmpty()) return;
                PreparedStatement ps = con.prepareStatement(
                        "UPDATE Participants SET Name=?, Email=?, Phone=? WHERE ParticipantID=?");
                ps.setString(1, nametxt.getText());
                ps.setString(2, emailtxt.getText());
                ps.setString(3, phoneTxt.getText());
                ps.setInt(4, Integer.parseInt(idtxt.getText()));
                ps.executeUpdate();
                JOptionPane.showMessageDialog(this, "Participant Updated!");
                loadParticipants();

            } else if (e.getSource() == deleteBtn) {
                if (idtxt.getText().isEmpty()) return;
                PreparedStatement ps = con.prepareStatement("DELETE FROM Participants WHERE ParticipantID=?");
                ps.setInt(1, Integer.parseInt(idtxt.getText()));
                ps.executeUpdate();
                JOptionPane.showMessageDialog(this, "Participant Deleted!");
                loadParticipants();

            } else if (e.getSource() == loadBtn) {
                loadParticipants();
            }

        } catch (Exception ex) {
            ex.printStackTrace();
            JOptionPane.showMessageDialog(this, "Database Error: " + ex.getMessage());
        }
    }

    private void loadParticipants() {
        try (Connection con = DB.getConnection()) {
            model.setRowCount(0);
            Statement stmt = con.createStatement();
            ResultSet rs = stmt.executeQuery("SELECT * FROM Participants");
            while (rs.next()) {
                model.addRow(new Object[]{
                        rs.getInt("ParticipantID"),
                        rs.getString("Name"),
                        rs.getString("Email"),
                        rs.getString("Phone")
                });
            }
            rs.close();
            stmt.close();
        } catch (Exception ex) {
            ex.printStackTrace();
            JOptionPane.showMessageDialog(this, "Database Error: " + ex.getMessage());
        }
    }
}
