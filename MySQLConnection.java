import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;

public class MySQLConnection {
    public static void main(String[] args) {

        String url = "jdbc:mysql://localhost:3306/MoravianWomensTeam24";
        String user = "project";
        String password = "project";

        try {
            // Load the MySQL JDBC driver
            Class.forName("com.mysql.cj.jdbc.Driver");

            // Establish a connection to the database
            Connection connection = DriverManager.getConnection(url, user, password);

            if (connection != null) {
                System.out.println("Successfully connected to the MySQL database.");
                // Perform database operations here

                // Close the connection
                connection.close();
            }
        } catch (ClassNotFoundException e) {
            System.out.println("MySQL JDBC Driver not found.");
            e.printStackTrace();
        } catch (SQLException e) {
            System.out.println("Error connecting to the database.");
            e.printStackTrace();
        }
    }
}
