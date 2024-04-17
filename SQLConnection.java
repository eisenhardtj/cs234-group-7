import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;

import javax.naming.spi.DirStateFactory.Result;

/**
 * Series of back-end methods that connect to the database and retrieve or modify 
 * data from the database's tables. The methods in this class are also used to keep 
 * the data inserted on the program persistent.
 * 
 * Author: Cole Aydelotte
 */
public class SQLConnection
{
    Connection connection = null;

    /**
     * Uses class variables to return a connection to the database;
     */
    public Connection openConnection()
    {
        String url = "jdbc:mysql://localhost:3306/moravianwomensteam24";
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

    public void archivePlayer(String firstName, String lastName)
    {
        connection = openConnection();
        try
        {
            String sql = "SELECT * FROM TeamRoster WHERE first_name = ? AND last_name = ?;";
            PreparedStatement archiveQuery = connection.prepareStatement(sql);
            archiveQuery.setString(1, firstName);
            archiveQuery.setString(2, lastName);
            ResultSet rs = archiveQuery.executeQuery();
            while (rs.next())
            {
                String archivedFirstName = rs.getString("first_name");
                String archivedLastName = rs.getString("last_name");
                int archivedNumber = rs.getInt("player_number");
                String archivedPosition = rs.getString("position");
                int archivedGradYear = rs.getInt("expected_graduation_date");
                
                String insertSql = " INSERT INTO ArchivePlayers (first_name, last_name, player_number, position, expected_graduation_date) VALUES (?, ?, ?, ?, ?)";
                PreparedStatement preparedArchiveStatement = connection.prepareStatement(insertSql);

                preparedArchiveStatement.setString(1, archivedFirstName);
                preparedArchiveStatement.setString(2, archivedLastName);
                preparedArchiveStatement.setInt(3, archivedNumber);
                preparedArchiveStatement.setString(4, archivedPosition);
                preparedArchiveStatement.setInt(5, archivedGradYear);

                int rowsAffected = preparedArchiveStatement.executeUpdate();
                System.out.println(rowsAffected + " rows affected.");
                preparedArchiveStatement.close();

                removePlayer(firstName, lastName);
            }
            archiveQuery.close();
        }
        catch (SQLException e)
        {
            e.printStackTrace();
            System.out.println("\nFailed to archive player from database.");
        }
        finally
        {
            closeConnection(connection);
        }
    }

    //Implement remove player function for archive players table/tab
    public void removePlayer(String firstName, String lastName) 
    {
        PreparedStatement removeQuery;
        Connection connection = openConnection();
        try {
            if (connection == null) {
                System.out.println("Failed to establish database connection.");
                return;
            }

            String sql = "DELETE FROM TeamRoster WHERE first_name = ? AND last_name = ?;";
            removeQuery = connection.prepareStatement(sql);
            removeQuery.setString(1, firstName);
            removeQuery.setString(2, lastName);
            int rowsAffected = removeQuery.executeUpdate();
            removeQuery.close();

            if (rowsAffected > 0) {
                System.out.println("Removed " + firstName + ", " + lastName + " from the roster.");
            } else {
                System.out.println("No matching player found in the roster.");
            }

        } catch (SQLException e) {
            e.printStackTrace();
            System.out.println("\nFailed to remove player from database.");
        } finally {
            closeConnection(connection);
        }
    }

    public void removePlayerArchive(String firstName, String lastName) 
    {
        PreparedStatement removeQuery;
        Connection connection = openConnection();
        try {
            if (connection == null) {
                System.out.println("Failed to establish database connection.");
                return;
            }

            String sql = "DELETE FROM archiveplayers WHERE first_name = ? AND last_name = ?;";
            removeQuery = connection.prepareStatement(sql);
            removeQuery.setString(1, firstName);
            removeQuery.setString(2, lastName);
            int rowsAffected = removeQuery.executeUpdate();
            removeQuery.close();

            if (rowsAffected > 0) {
                System.out.println("Removed " + firstName + ", " + lastName + " from the archived players.");
            } else {
                System.out.println("No matching player found in the roster.");
            }

        } catch (SQLException e) {
            e.printStackTrace();
            System.out.println("\nFailed to remove player from database.");
        } finally {
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

    public void editSession(String originalFirstName, String originalLastName, String firstName, String lastName, String date, int attempted, int made, String typeOfSession)
    {
        connection = openConnection();
        try
        {
            String sql = "UPDATE " + typeOfSession + " SET first_name = ?, last_name = ?, date = ?, made = ?, attempted = ? WHERE first_name = ? AND last_name = ?;";
            PreparedStatement editStatement = connection.prepareStatement(sql);
            editStatement.setString(1, firstName);
            editStatement.setString(2, lastName);
            editStatement.setString(3, date);
            editStatement.setInt(4, made);
            editStatement.setInt(5, attempted);
            editStatement.setString(6, originalFirstName);
            editStatement.setString(7, originalLastName);

            int rowsAffected = editStatement.executeUpdate();
            if(rowsAffected == 0)
            {
                System.out.println("No rows affected.");
            }
            else
            {
                System.out.println(rowsAffected + " rows affected.");
                System.out.println("Edited player " + firstName + " " + lastName + " to new values.");
            }
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

    public void addFreeThrow(String firstName, String lastName, String date, int attempted, int made)
    {
        connection = openConnection();
        try
        {
            String sql = "INSERT INTO freethrows (first_name, last_name, date, made, attempted) VALUES (?, ?, ?, ?, ?);";
            PreparedStatement addStatement = connection.prepareStatement(sql);

            addStatement.setString(1, firstName);
            addStatement.setString(2, lastName);
            addStatement.setString(3, date);
            addStatement.setInt(4, made);
            addStatement.setInt(5, attempted);

            addStatement.executeUpdate();
            
            addStatement.close();

            System.out.println("Added " + firstName + ", " + lastName + " to freethrows table.");
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

    public void addThreePoint(String firstName, String lastName, String date, int attempted, int made, String location)
    {
        connection = openConnection();
        try
        {
            String sql = "INSERT INTO threepointshots (first_name, last_name, date, made, attempted, location) VALUES (?, ?, ?, ?, ?, ?);";
            PreparedStatement addStatement = connection.prepareStatement(sql);

            addStatement.setString(1, firstName);
            addStatement.setString(2, lastName);
            addStatement.setString(3, date);
            addStatement.setInt(4, made);
            addStatement.setInt(5, attempted);
            addStatement.setString(6, location);

            addStatement.executeUpdate();
            
            addStatement.close();

            System.out.println("Added " + firstName + ", " + lastName + " to threepointshots table.");
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

    public ResultSet getPlayers()
    {
        connection = openConnection();
        try
        {
            String sql = "SELECT * FROM TeamRoster;";
            PreparedStatement getPlayers = connection.prepareStatement(sql);
            ResultSet rs = getPlayers.executeQuery();
            return rs;
        }
        catch(SQLException e)
        {
            e.printStackTrace();
            System.out.println("\nFailed to get players from database.");
        }
        finally
        {
            closeConnection(connection);
        }
        return null;
    }

    public ResultSet searchByName(String firstName, String lastName)
    {
        connection = openConnection();
        try
        {
            String sql = "SELECT * FROM TeamRoster WHERE first_name = ? AND last_name = ?;";
            PreparedStatement searchQuery = connection.prepareStatement(sql);
            searchQuery.setString(1, firstName);
            searchQuery.setString(2, lastName);
            ResultSet rs = searchQuery.executeQuery();
            return rs;
        }
        catch(SQLException e)
        {
            e.printStackTrace();
            System.out.println("\nFailed to search for player in database.");
        }
        finally
        {
            closeConnection(connection);
        }
        return null;
    }

    public ResultSet searchByDate(String date, String typeOfSession)
    {
        connection = openConnection();
        try
        {
            String sql = "SELECT * FROM " + typeOfSession + " WHERE date = ?;";
            PreparedStatement searchQuery = connection.prepareStatement(sql);
            searchQuery.setString(1, date);
            ResultSet rs = searchQuery.executeQuery();
            return rs;
        }
        catch(SQLException e)
        {
            e.printStackTrace();
            System.out.println("\nFailed to search for player in database.");
        }
        finally
        {
            closeConnection(connection);
        }
        return null;
    }

    public String[] getDates() 
    {
        Connection connection = null;
        try 
        {
            connection = openConnection();
            String sql = "SELECT DISTINCT date FROM freethrows LIMIT 6;";
            PreparedStatement getDates = connection.prepareStatement(sql);
            ResultSet rs = getDates.executeQuery();
            String[] dates = new String[6];
            int x = 0;
            while (rs.next()) 
            {
                dates[x] = rs.getString("date");
                x++;
            }
            return dates;
        }
        catch (SQLException e) 
        {
            e.printStackTrace();
            System.out.println("\nFailed to get dates from the database.");
        } 
        finally 
        {
            if (connection != null) 
            {
                closeConnection(connection);
            }
        }
        return null;
    }

    public Double findStatisticsBasedOnDate(String date, String typeOfSession) 
    {
        connection = openConnection();
        try 
        {
            int attempted = 0, made = 0;
            String sql = "SELECT * FROM " + typeOfSession + " WHERE date = ?";
            PreparedStatement searchQuery = connection.prepareStatement(sql);
            searchQuery.setString(1, date);  // Set the date parameter
            ResultSet rs = searchQuery.executeQuery();
            while(rs.next()) {
                made += rs.getInt("made");
                attempted += rs.getInt("attempted");
            }
            if (attempted == 0) 
            {
                return null;
            }
            return (double) made / (double) attempted;
        } 
        catch(SQLException e) 
        {
            e.printStackTrace();
            System.out.println("\nFailed to search for player in database.");
        }
        finally 
        {
            closeConnection(connection);
        }
        return null;
    }
    
    public String[] searchPlayerDates(String firstName, String lastName, String type)
    {
        connection = openConnection();
        try
        {
            String sql = "SELECT date FROM " + type + " WHERE first_name = ? AND last_name = ? LIMIT 6;";
            PreparedStatement searchQuery = connection.prepareStatement(sql);
            searchQuery.setString(1, firstName);
            searchQuery.setString(2, lastName);
            ResultSet rs = searchQuery.executeQuery();
            ArrayList<String> datesList = new ArrayList<>();
            while(rs.next())
            {
                datesList.add(rs.getString("date"));
            }
            String[] dates = datesList.toArray(new String[0]);

            return dates;
        }
        catch(SQLException e)
        {
            e.printStackTrace();
            System.out.println("\nFailed to search for player in database.");
        }
        finally
        {
            closeConnection(connection);
        }
        return null;
    }


    public double findPlayerStatisticsBasedOnDate(String type, String date, String firstName, String lastName) 
    {
        connection = openConnection();
        try {
            int made = 0, attempted = 0;
            String sql = "SELECT * FROM " + type + " WHERE date = ? AND first_name = ? AND last_name = ?;";
            PreparedStatement searchQuery = connection.prepareStatement(sql);
            searchQuery.setString(1, date);
            System.out.println(date);
            searchQuery.setString(2, firstName);
            System.out.println(firstName);
            searchQuery.setString(3, lastName);
            System.out.println(lastName);
            System.out.println(searchQuery.toString());
            ResultSet rs = searchQuery.executeQuery();
            while(rs.next()) {
                attempted += rs.getInt("attempted");
                made += rs.getInt("made");
            }
            System.out.println((double) made / (double) attempted);
            return (double) made / (double) attempted;
        } 
        catch(SQLException e) 
        {
            e.printStackTrace();
            System.out.println("\nFailed to search for player in database.");
        }
        finally
        {
            closeConnection(connection);
        }
        return 0.0;
    }
}
