package CONFERENCEdb;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;

public class DB {
    private static final String URL = "jdbc:mysql://localhost:3306/conferencecrm"; // change DB name
    private static final String USER = "root"; // your DB user
    private static final String PASS = ""; // your DB password

    static {
        try {
            Class.forName("com.mysql.cj.jdbc.Driver"); // MySQL 8+ driver
        } catch (ClassNotFoundException e) {
            e.printStackTrace();
        }
    }

    public static Connection getConnection() throws SQLException {
        return DriverManager.getConnection(URL, USER, PASS);
    }
}
