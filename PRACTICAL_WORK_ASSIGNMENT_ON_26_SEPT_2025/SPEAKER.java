package CONFERENCE;

import javax.swing.*;
import javax.swing.event.ListSelectionEvent;
import javax.swing.event.ListSelectionListener;
import javax.swing.table.DefaultTableModel;

import CONFERENCEdb.DB;

import java.awt.event.*;
import java.sql.*;

public class SPEAKER extends JPanel implements ActionListener {

    private JTextField idtxt = new JTextField();
    private JTextField nameTxt = new JTextField();
    private JTextField emailTxt = new JTextField();
    private JTextField organizationTxt = new JTextField();

    private JButton addBtn = new JButton("Add");
    private JButton updateBtn = new JButton("Update");
    private JButton deleteBtn = new JButton("Delete");
    private JButton loadBtn = new JButton("Load");

    private JTable table;
    private DefaultTableModel model;

    private String currentRole;
    private int currentUserId;

    public SPEAKER(String role, int userId) {
        this.currentRole = role;
        this.currentUserId = userId;
        setLayout(null);

        // Table
        String[] columns = {"SpeakerID", "Name", "Email", "Organization"};
        model = new DefaultTableModel(columns, 0);
        table = new JTable(model);
        JScrollPane sp = new JScrollPane(table);
        sp.setBounds(20, 220, 700, 250);
        add(sp);

        int y = 20;
        addField("ID", idtxt, y); y += 35;
        addField("Name", nameTxt, y); y += 35;
        addField("Email", emailTxt, y); y += 35;
        addField("Organization", organizationTxt, y); y += 35;

        addButtons();

        if (!role.equalsIgnoreCase("admin")) {
            addBtn.setEnabled(false);
            updateBtn.setEnabled(false);
            deleteBtn.setEnabled(false);
        }

        table.getSelectionModel().addListSelectionListener(new ListSelectionListener() {
            @Override
            public void valueChanged(ListSelectionEvent e) {
                if (!e.getValueIsAdjusting() && table.getSelectedRow() != -1) {
                    int row = table.getSelectedRow();
                    idtxt.setText(model.getValueAt(row, 0).toString());
                    nameTxt.setText(model.getValueAt(row, 1).toString());
                    emailTxt.setText(model.getValueAt(row, 2).toString());
                    organizationTxt.setText(model.getValueAt(row, 3).toString());
                }
            }
        });

        loadSpeakers();
    }

    private void addField(String label, JComponent field, int y) {
        JLabel lbl = new JLabel(label + ":");
        lbl.setBounds(20, y, 100, 25);
        field.setBounds(130, y, 150, 25);
        add(lbl);
        add(field);
    }

    private void addButtons() {
        addBtn.setBounds(300, 20, 100, 30);
        updateBtn.setBounds(300, 60, 100, 30);
        deleteBtn.setBounds(300, 100, 100, 30);
        loadBtn.setBounds(300, 140, 100, 30);
        add(addBtn); add(updateBtn); add(deleteBtn); add(loadBtn);

        addBtn.addActionListener(this);
        updateBtn.addActionListener(this);
        deleteBtn.addActionListener(this);
        loadBtn.addActionListener(this);
    }

    @Override
    public void actionPerformed(ActionEvent e) {
        try (Connection con = DB.getConnection()) {
            if (e.getSource() == addBtn) {
                PreparedStatement ps = con.prepareStatement(
                        "INSERT INTO Speakers(Name, Email, Organization) VALUES(?,?,?)");
                ps.setString(1, nameTxt.getText());
                ps.setString(2, emailTxt.getText());
                ps.setString(3, organizationTxt.getText());
                ps.executeUpdate();
                JOptionPane.showMessageDialog(this, "Speaker Added!");
                loadSpeakers();
            } else if (e.getSource() == updateBtn) {
                if (idtxt.getText().isEmpty()) return;
                PreparedStatement ps = con.prepareStatement(
                        "UPDATE Speakers SET Name=?, Email=?, Organization=? WHERE SpeakerID=?");
                ps.setString(1, nameTxt.getText());
                ps.setString(2, emailTxt.getText());
                ps.setString(3, organizationTxt.getText());
                ps.setInt(4, Integer.parseInt(idtxt.getText()));
                ps.executeUpdate();
                JOptionPane.showMessageDialog(this, "Speaker Updated!");
                loadSpeakers();
            } else if (e.getSource() == deleteBtn) {
                if (idtxt.getText().isEmpty()) return;
                PreparedStatement ps = con.prepareStatement("DELETE FROM Speakers WHERE SpeakerID=?");
                ps.setInt(1, Integer.parseInt(idtxt.getText()));
                ps.executeUpdate();
                JOptionPane.showMessageDialog(this, "Speaker Deleted!");
                loadSpeakers();
            } else if (e.getSource() == loadBtn) {
                loadSpeakers();
            }
        } catch (Exception ex) {
            ex.printStackTrace();
            JOptionPane.showMessageDialog(this, "Database Error: " + ex.getMessage());
        }
    }

    private void loadSpeakers() {
        try (Connection con = DB.getConnection()) {
            model.setRowCount(0);
            Statement stmt = con.createStatement();
            ResultSet rs = stmt.executeQuery("SELECT * FROM Speakers");
            while (rs.next()) {
                model.addRow(new Object[]{
                        rs.getInt("SpeakerID"),
                        rs.getString("Name"),
                        rs.getString("Email"),
                        rs.getString("Organization")
                });
            }
            rs.close();
            stmt.close();
        } catch (Exception ex) {
            ex.printStackTrace();
        }
    }
}
