import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.PreparedStatement;
import java.sql.SQLException;

import javax.swing.JTextField;

public class SQLConnection
{
    Connection connection = null;

    /**
     * Uses class variables to return a connection to the database;
     */
    public Connection openConnection()
    {
        String url = "jdbc:mysql://localhost:3306/MoravianWomensTeam24";
        String user = "project";
        String password = "project";
        try
        {
            Connection connection = DriverManager.getConnection(url, user, password);
            return connection;
        }
        catch(SQLException e)
        {
            e.printStackTrace();
            System.out.println("\nError while connecting to the database.");
            return null;
        }
    }

    public void closeConnection(Connection conn)
    {
        try
        {
            conn.close();
        } 
        catch(SQLException e)
        {
            e.printStackTrace();
            System.out.println("\nFailed to close Connection.");

        }
    }

    public void addPlayer(String firstName, String lastName, int number, String position, int gradYear) 
    {
        connection = openConnection();
        try
        {
            String sql = "INSERT INTO TeamRoster (first_name, last_name, player_number, position, expected_graduation_date) VALUES (?, ?, ?, ?, ?)";
            PreparedStatement addStatement = connection.prepareStatement(sql);

            addStatement.setString(1, firstName);
            addStatement.setString(2, lastName);
            addStatement.setInt(3, number);
            addStatement.setString(4, position);
            addStatement.setInt(5, gradYear);

            addStatement.executeUpdate();
            
            addStatement.close();

            System.out.println("Added " + firstName + ", " + lastName + " to database.");
        }
        catch (SQLException e) {
            e.printStackTrace();
            System.out.println("\nFailed to add player to database.");
        }
        finally
        {
            closeConnection(connection);
        }
    }

    public void removePlayer(String firstName, String lastName)
    {
        PreparedStatement removeQuery;
        connection = openConnection();
        try
        {
            String sql = "DELETE FROM TeamRoster WHERE first_name = ? AND last_name = ?;";
            removeQuery = connection.prepareStatement(sql);
            removeQuery.setString(1, firstName);
            removeQuery.setString(2, lastName);
            removeQuery.executeUpdate();
            removeQuery.close();
            
            System.out.println("Removed " + firstName + ", " + lastName + " from the roster.");

        }
        catch (SQLException e) 
        {
            e.printStackTrace();
            System.out.println("\nFailed to remove player from database.");
        }
        finally
        {
            closeConnection(connection);
        }
    }

    public void archivePlayer(String firstName, String lastName, int number, String position, int gradYear)
    {
        connection = openConnection();
        try 
        {
            String sql = "INSERT INTO ArchivedPlayers (first_name, last_name, player_number, position, expected_graduation_date) VALUES (?, ?, ?, ?, ?)";
            PreparedStatement addStatement = connection.prepareStatement(sql);

            addStatement.setString(1, firstName);
            addStatement.setString(2, lastName);
            addStatement.setInt(3, number);
            addStatement.setString(4, position);
            addStatement.setInt(5, gradYear);

            addStatement.executeUpdate();
            
            addStatement.close();

            System.out.println("Added " + firstName + ", " + lastName + " to archived player database.");

            removePlayer(firstName, lastName);
        }
        catch (SQLException e) 
        {
            e.printStackTrace();
            System.out.println("\nFailed to add player to database.");
        }
        finally
        {
            closeConnection(connection);
        }
    }

    public void editPlayer(String originalFirstName, String originalLastName, String firstName, String lastName, int number, String position, int gradYear)
    {
        connection = openConnection();
        try
        {
        String sql = "UPDATE teamroster SET first_name = ?, last_name = ?, player_number = ?, position = ?, expected_graduation_date = ? WHERE first_name = ? AND last_name = ?;";
        PreparedStatement editStatement = connection.prepareStatement(sql);
        editStatement.setString(1, firstName);
        editStatement.setString(2, lastName);
        editStatement.setInt(3, number);
        editStatement.setString(4, position);
        editStatement.setInt(5, gradYear);
        editStatement.setString(6, originalFirstName);
        editStatement.setString(7, originalLastName);

        editStatement.executeUpdate();

        System.out.println("Edited player " + firstName + " " + lastName + " to new values.");

        }
        catch(SQLException e)
        {
            e.printStackTrace();
            System.out.println("\nFailed to edit the values for the current player " + firstName + ", " + lastName + ".");
        }
        finally
        {
            closeConnection(connection);
        }
    }
}
