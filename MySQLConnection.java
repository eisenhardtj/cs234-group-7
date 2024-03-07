/**
 * Uses a MySQL driver and library to establish a connection between the database and the app.
 * 
 * Authors: 
 * Jeffery Eisenhardt - eisenhardtj
 * Christine Colvin - christinecolvin
 * Cole Aydelotte - coleaydelotte
 * Jalil Rodriguez - JalilR08
 */
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.PreparedStatement;
import java.sql.SQLException;
import java.sql.ResultSet;

public class MySQLConnection {
    public static void main(String[] args) {

        PreparedStatement q = null;
        String url = "jdbc:mysql://localhost:3306/MoravianWomensTeam24";
        String user = "project";
        String password = "project";
        Connection connection = null;
        ResultSet results = null;
        try
        {
            connection = DriverManager.getConnection(url, user, password);

            if (connection != null) 
            {
                System.out.println("Successfully connected to the MySQL database.");
                
                String sql = "SELECT * FROM teamroster;";
                q = connection.prepareStatement(sql);
                results = q.executeQuery();
                while(results.next())
                {
                    String field = results.getString("player_id");
                    System.out.println(field);
                    field = results.getString("player_name");
                    System.out.println(field);
                    field = results.getString("player_number");
                    System.out.println(field);
                    field = results.getString("position");
                    System.out.println(field);
                    field = results.getString("expected_graduation_date");
                    System.out.println(field);
                }
            }
        }
        // CREATE TABLE `TeamRoster` (
        // `player_id` int NOT NULL AUTO_INCREMENT,
        // `player_name` varchar(100) DEFAULT NULL,
        // `player_number` int DEFAULT NULL,
        // `position` varchar(100) DEFAULT NULL,
        // `expected_graduation_date` int DEFAULT NULL,
        // `height` varchar(20) DEFAULT NULL,
        // `weight` decimal(5,2) DEFAULT NULL,
        // PRIMARY KEY (`player_id`)
        catch (SQLException e) 
        {
            System.out.println("Error connecting to the database.");
            e.printStackTrace();
        }
        finally
        {
            try
            {
                if (q != null)
                {
                    q.close();
                }

                if (connection != null)
                {
                    connection.close();
                }

                if (results != null)
                {
                    results.close();
                }
            }
            catch (SQLException e)
            {
                System.out.println("Error closing connections.");
                e.printStackTrace();
            }
        }
    }
}
