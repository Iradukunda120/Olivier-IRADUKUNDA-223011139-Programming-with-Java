package CONFERENCE;

import javax.swing.*;

public class CRM extends JFrame {

    private JTabbedPane tabs;

    public CRM(String role, int userId) {
        setTitle("Conference Registration System - CRM");
        setSize(900, 600);
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        setLocationRelativeTo(null);

        tabs = new JTabbedPane();

        // Integrate panels depending on role
        if (role.equalsIgnoreCase("admin")) {
            tabs.add("Clients", new CLIENTCRM(role, userId));
            tabs.add("Speakers", new SPEAKERS(role, userId));
            tabs.add("Reporters", new REPORTERS(role, userId));
            tabs.add("Sessions", new SESSIONS(role, userId));
            tabs.add("Attendees", new ATTENDEES(role, userId));
        } else if (role.equalsIgnoreCase("reporter")) {
            tabs.add("Sessions", new SESSIONS(role, userId));
            tabs.add("Speakers", new SPEAKERS(role, userId));
        } else if (role.equalsIgnoreCase("speaker")) {
            tabs.add("My Sessions", new SESSIONS(role, userId));
        } else if (role.equalsIgnoreCase("attendee")) {
            tabs.add("Register for Sessions", new ATTENDEES(role, userId));
        }

        add(tabs);
    }
}
