package CONFERENCE;

import javax.swing.*;
import javax.swing.table.DefaultTableModel;
import javax.swing.event.ListSelectionEvent;
import javax.swing.event.ListSelectionListener;

import CONFERENCEdb.DB;

import java.awt.event.*;
import java.sql.*;

public class SESSIONS extends JPanel implements ActionListener {

    private JTextField idtxt = new JTextField();
    private JTextField titleTxt = new JTextField();
    private JTextField dateTxt = new JTextField();
    private JTextField statusTxt = new JTextField();

    private JButton addBtn = new JButton("Add");
    private JButton updateBtn = new JButton("Update");
    private JButton deleteBtn = new JButton("Delete");
    private JButton loadBtn = new JButton("Load");

    private JTable table;
    private DefaultTableModel model;

    private String currentRole;
    private int currentUserId;

    public SESSIONS(String role, int userId) {
        this.currentRole = role;
        this.currentUserId = userId;
        setLayout(null);

        // Table setup
        String[] columns = {"SessionID", "Title", "Date", "Status"};
        model = new DefaultTableModel(columns, 0);
        table = new JTable(model);
        JScrollPane sp = new JScrollPane(table);
        sp.setBounds(20, 220, 700, 250);
        add(sp);

        // Add input fields
        int y = 20;
        addField("ID", idtxt, y); y += 35;
        addField("Title", titleTxt, y); y += 35;
        addField("Date", dateTxt, y); y += 35;
        addField("Status", statusTxt, y); y += 35;

        // Add buttons
        addButtons();

        // Only admin can modify sessions
        if (!currentRole.equalsIgnoreCase("admin")) {
            addBtn.setEnabled(false);
            updateBtn.setEnabled(false);
            deleteBtn.setEnabled(false);
        }

        // Table selection listener (CORRECT WAY)
        table.getSelectionModel().addListSelectionListener(new ListSelectionListener() {
            @Override
            public void valueChanged(ListSelectionEvent e) {
                if (!e.getValueIsAdjusting() && table.getSelectedRow() != -1) {
                    int row = table.getSelectedRow();
                    idtxt.setText(model.getValueAt(row, 0).toString());
                    titleTxt.setText(model.getValueAt(row, 1).toString());
                    dateTxt.setText(model.getValueAt(row, 2).toString());
                    statusTxt.setText(model.getValueAt(row, 3).toString());
                }
            }
        });

        // Load sessions initially
        loadSessions();
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
                        "INSERT INTO Sessions(Title, Date, Status) VALUES(?,?,?)");
                ps.setString(1, titleTxt.getText());
                ps.setString(2, dateTxt.getText());
                ps.setString(3, statusTxt.getText());
                ps.executeUpdate();
                JOptionPane.showMessageDialog(this, "Session Added!");
                loadSessions();
            } else if (e.getSource() == updateBtn) {
                if (idtxt.getText().isEmpty()) return;
                PreparedStatement ps = con.prepareStatement(
                        "UPDATE Sessions SET Title=?, Date=?, Status=? WHERE SessionID=?");
                ps.setString(1, titleTxt.getText());
                ps.setString(2, dateTxt.getText());
                ps.setString(3, statusTxt.getText());
                ps.setInt(4, Integer.parseInt(idtxt.getText()));
                ps.executeUpdate();
                JOptionPane.showMessageDialog(this, "Session Updated!");
                loadSessions();
            } else if (e.getSource() == deleteBtn) {
                if (idtxt.getText().isEmpty()) return;
                PreparedStatement ps = con.prepareStatement("DELETE FROM Sessions WHERE SessionID=?");
                ps.setInt(1, Integer.parseInt(idtxt.getText()));
                ps.executeUpdate();
                JOptionPane.showMessageDialog(this, "Session Deleted!");
                loadSessions();
            } else if (e.getSource() == loadBtn) {
                loadSessions();
            }
        } catch (Exception ex) {
            ex.printStackTrace();
            JOptionPane.showMessageDialog(this, "Database Error: " + ex.getMessage());
        }
    }

    private void loadSessions() {
        try (Connection con = DB.getConnection()) {
            model.setRowCount(0);
            Statement stmt = con.createStatement();
            ResultSet rs = stmt.executeQuery("SELECT * FROM Sessions");
            while (rs.next()) {
                model.addRow(new Object[]{
                        rs.getInt("SessionID"),
                        rs.getString("Title"),
                        rs.getString("Date"),
                        rs.getString("Status")
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
