package FirstWeek.July25;

import java.sql.*;

public class JDBC {
    // JDBC bağlantı bilgileri
    private static final String url = "jdbc:mysql://localhost:3307/java_ignite";
    private static final String username = "root";
    private static final String password = "password";

    public JDBC() {
    }

    public static void run(){


        String query = "SELECT * FROM java_ignite.jss_2025";

        try (Connection conn = DriverManager.getConnection(url, username, password);
             Statement stmt = conn.createStatement();
             ResultSet rs = stmt.executeQuery(query)) {

            ResultSetMetaData metaData = rs.getMetaData();
            int columnCount = metaData.getColumnCount();

            while (rs.next()) {
                for (int i = 1; i <= columnCount; i++) {
                    System.out.print(metaData.getColumnName(i) + ": " + rs.getString(i) + " | ");
                }
                System.out.println();
            }

        } catch (SQLException e) {
            e.printStackTrace();
        }

    }
}
