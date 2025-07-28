package FirstWeek.July26SelfLearning.SqlInjection;

import java.sql.*;
import java.util.concurrent.ExecutionException;

public class SqlInjection {
    private static final String url = "jdbc:mysql://localhost:3307/java_ignite";
    private static final String username = "root";
    private static final String password = "password";

    public void run(){
        // correct values
        unSafeConnection("Furkan", "1234");
        // injections
        unSafeConnection("Furkan' -- ", "dummy");
        unSafeConnection("Furkan' OR '1'='1 ", "dummy");
        unSafeConnection("Furkan' AND '1'='1' -- ", "dummy");
        unSafeConnection("Furkan' AND SLEEP(5) -- ", "dummy");
        //unSafeConnection("Furkan'; DROP TABLE auth; -- ", "dummy");

        safeConnection("Furkan", "1234");

        safeConnection("Furkan' -- ", "dummy");
        safeConnection("Furkan' OR '1'='1 ", "dummy");
        safeConnection("Furkan' AND '1'='1' -- ", "dummy");
        safeConnection("Furkan' AND SLEEP(5) -- ", "dummy");
        //safeConnection("Furkan'; DROP TABLE auth; -- ", "dummy");
    }

    private void safeConnection(String name, String pw){
        try(Connection conn = DriverManager.getConnection(url, username, password)){
            String query = "SELECT * FROM auth WHERE user = ? AND password = ?";
            PreparedStatement pstmt = conn.prepareStatement(query);
            pstmt.setString(1, name);
            pstmt.setString(2, pw);

            System.out.println("Prepared Query: " + query);
            System.out.println("Parametreler: " + name + ", " + pw);

            ResultSet rs = pstmt.executeQuery();

            if (rs.next()) {
                System.out.println("Connected" + rs.getString("user"));
            } else {
                System.out.println("Connection failed. ");
            }
        }catch (Exception e){
            e.printStackTrace();
        }

    }

    private void unSafeConnection(String name, String pw){
        try (Connection conn = DriverManager.getConnection(url, username, password)) {
            Statement stmt = conn.createStatement();

            String query = "SELECT * FROM auth WHERE user = '" + name +
                    "' AND password = '" + pw + "'";

            System.out.println("Query: " + query);

            ResultSet rs = stmt.executeQuery(query);

            if (rs.next()) {
                System.out.println("Connected" + rs.getString("user"));
            } else {
                System.out.println("Connection failed. ");
            }
        }catch (Exception e){
            e.printStackTrace();
        }

    }

}
