import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;

import javax.swing.JTextField;

import java.sql.Array;

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

    public ArrayList<String[]> dataToArrayListTeamRoster()
    {
        ArrayList<String[]> players = new ArrayList<>();
        connection = openConnection();
        try
        {
            ResultSet rs = connection.getMetaData().getTables(null, null, "TeamRoster", null);
            while (rs.next()) 
            {
                PreparedStatement preparedStatement = connection.prepareStatement("SELECT * FROM teamroster;");
                ResultSet tableResultSet = preparedStatement.executeQuery();
                while (tableResultSet.next()) 
                {
                    String firstName = tableResultSet.getString("first_name");
                    String lastName = tableResultSet.getString("last_name");
                    int playerNum = tableResultSet.getInt("player_number");
                    String playerPosition = tableResultSet.getString("position");
                    int gradYear = tableResultSet.getInt("expected_graduation_date");

                    String[] playerStat = {firstName, lastName, Integer.toString(playerNum), playerPosition, Integer.toString(gradYear)};
                    players.add(playerStat);
                }
                preparedStatement.close();
            }
            return players;
        }
        catch(SQLException e)
        {
            e.printStackTrace();
            System.out.println("\nFailed to load the TeamRoster table.");
        }
        finally
        {
            closeConnection(connection);
        }
        return null;
    }

    public ArrayList<String[]> dataToArrayListArchivePlayers()
    {
        ArrayList<String[]> archivedPlayers = new ArrayList<>();
        connection = openConnection();
        try
        {
            ResultSet rs = connection.getMetaData().getTables(null, null, "TeamRoster", null);
            while (rs.next()) 
            {
                PreparedStatement preparedStatement = connection.prepareStatement("SELECT * FROM archiveplayers;");
                ResultSet tableResultSet = preparedStatement.executeQuery();
                while (tableResultSet.next()) 
                {
                    String firstName = tableResultSet.getString("first_name");
                    String lastName = tableResultSet.getString("last_name");
                    int playerNum = tableResultSet.getInt("player_number");
                    String playerPosition = tableResultSet.getString("position");
                    int gradYear = tableResultSet.getInt("expected_graduation_date");

                    String[] archivedPlayerStat = {firstName, lastName, Integer.toString(playerNum), playerPosition, Integer.toString(gradYear)};
                    archivedPlayers.add(archivedPlayerStat);
                }
                preparedStatement.close();
            }
            return archivedPlayers;
        }
        catch(SQLException e)
        {
            e.printStackTrace();
            System.out.println("\nFailed to load the TeamRoster table.");
        }
        finally
        {
            closeConnection(connection);
        }
        return null;
    }

    public ArrayList<String[]> dataToArrayListThreePoints()
    {
        ArrayList<String[]> threePointSessions = new ArrayList<>();
        connection = openConnection();
        try
        {
            ResultSet rs = connection.getMetaData().getTables(null, null, "threepointshots", null);
            while (rs.next()) 
            {
                PreparedStatement preparedStatement = connection.prepareStatement("SELECT * FROM threepointshots;");
                ResultSet tableResultSet = preparedStatement.executeQuery();
                while (tableResultSet.next()) 
                {
                    String firstName = tableResultSet.getString("first_name");
                    String lastName = tableResultSet.getString("last_name");
                    String date = tableResultSet.getString("date");
                    int made = tableResultSet.getInt("made");
                    int attempted = tableResultSet.getInt("attempted");

                    String[] playerStat = {firstName, lastName, date, Integer.toString(attempted), Integer.toString(made)};
                    threePointSessions.add(playerStat);
                }
                preparedStatement.close();
            }
            return threePointSessions;
        }
        catch(SQLException e)
        {
            e.printStackTrace();
            System.out.println("\nFailed to load the threepoints table.");
        }
        finally
        {
            closeConnection(connection);
        }
        return null;
    }

    public ArrayList<String[]> dataToArrayListFreeThrows()
    {
        ArrayList<String[]> freeThrowSessions = new ArrayList<>();
        connection = openConnection();
        try
        {
            ResultSet rs = connection.getMetaData().getTables(null, null, "threepointshots", null);
            while (rs.next()) 
            {
                PreparedStatement preparedStatement = connection.prepareStatement("SELECT * FROM freethrows;");
                ResultSet tableResultSet = preparedStatement.executeQuery();
                while (tableResultSet.next()) 
                {
                    String firstName = tableResultSet.getString("first_name");
                    String lastName = tableResultSet.getString("last_name");
                    String date = tableResultSet.getString("date");
                    int made = tableResultSet.getInt("made");
                    int attempted = tableResultSet.getInt("attempted");

                    String[] sessionStat = {firstName, lastName, date, Integer.toString(attempted), Integer.toString(made)};
                   freeThrowSessions.add(sessionStat);
                }
                preparedStatement.close();
            }
            return freeThrowSessions;
        }
        catch(SQLException e)
        {
            e.printStackTrace();
            System.out.println("\nFailed to load the freethrows table.");
        }
        finally
        {
            closeConnection(connection);
        }
        return null;
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

    public void addThreePoint(String firstName, String lastName, String date, int attempted, int made)
    {
        connection = openConnection();
        try
        {
            String sql = "INSERT INTO threepointshots (first_name, last_name, date, made, attempted) VALUES (?, ?, ?, ?, ?);";
            PreparedStatement addStatement = connection.prepareStatement(sql);

            addStatement.setString(1, firstName);
            addStatement.setString(2, lastName);
            addStatement.setString(3, date);
            addStatement.setInt(4, made);
            addStatement.setInt(5, attempted);

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
}
