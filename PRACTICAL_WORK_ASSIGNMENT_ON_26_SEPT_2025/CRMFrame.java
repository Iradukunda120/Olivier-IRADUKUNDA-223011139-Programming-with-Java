package CONFERENCE;

import javax.swing.*;
import CONFERENCECRS.CLIENTS;  // Make sure your package matches
import CONFERENCE.SPEAKER;
import CONFERENCE.SESSIONS;
import CONFERENCE.REPORTERS;

public class CRMFrame extends JFrame {

    public CRMFrame(String role, int userId) {
        setTitle("Conference CRM");
        setSize(1000, 600);
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        setLocationRelativeTo(null); // Center the frame

        // Create tabbed pane
        JTabbedPane tabs = new JTabbedPane();

        try {
            CLIENTS clientsPanel = new CLIENTS(role, userId);
            tabs.add("Clients", clientsPanel);
        } catch (Exception ex) {
            System.err.println("Error loading CLIENTS: " + ex.getMessage());
        }

        try {
            SPEAKER speakersPanel = new SPEAKER(role, userId);
            tabs.add("Speakers", speakersPanel);
        } catch (Exception ex) {
            System.err.println("Error loading SPEAKER: " + ex.getMessage());
        }

        try {
            SESSIONS sessionsPanel = new SESSIONS(role, userId);
            tabs.add("Sessions", sessionsPanel);
        } catch (Exception ex) {
            System.err.println("Error loading SESSIONS: " + ex.getMessage());
        }

        try {
            REPORTERS reportersPanel = new REPORTERS(role, userId);
            tabs.add("Reporters", reportersPanel);
        } catch (Exception ex) {
            System.err.println("Error loading REPORTERS: " + ex.getMessage());
        }

        // Add tabs to frame
        add(tabs);
        setVisible(true);
    }

    public static void main(String[] args) {
        // Example login info
        String role = "admin";
        int userId = 1;

        // Launch the GUI safely on Event Dispatch Thread
        CRMFrame frame = new CRMFrame(role, userId); frame.setVisible(true);
    }
}
