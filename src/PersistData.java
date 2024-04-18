import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;

/**
 * This class is responsible for persisting data from the database to an ArrayList.
 * 
 * Author: Cole Aydelotte
 */
public class PersistData 
{
    private SQLConnection conn;
    private Connection connection;

    /**
     * Constructs a new PersistData object.
     */
    public PersistData()
    {
        conn = new SQLConnection();
    }

    /**
     * This method retrieves data from the database and stores it in an ArrayList of Player objects.
     * @return ArrayList<Player> - an ArrayList of Player objects
     */
    public ArrayList<String[]> dataToArrayListTeamRoster()
    {
        ArrayList<String[]> players = new ArrayList<>();
        connection = conn.openConnection();
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
            conn.closeConnection(connection);
        }
        return null;
    }

    /**
     * This method retrieves data from the database and stores it in an ArrayList of ArchivedPlayer objects.
     * @return ArrayList<ArchivedPlayer> - an ArrayList of ArchivedPlayer objects
     */
    public ArrayList<String[]> dataToArrayListArchivePlayers()
    {
        ArrayList<String[]> archivedPlayers = new ArrayList<>();
        connection = conn.openConnection();
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
            conn.closeConnection(connection);
        }
        return null;
    }

    /**
     * This method retrieves data from the database and stores it in an ArrayList of String arrays.
     * @return ArrayList<String[]> - an ArrayList of String arrays
     */
    public ArrayList<String[]> dataToArrayListThreePoints()
    {
        ArrayList<String[]> threePointSessions = new ArrayList<>();
        connection = conn.openConnection();
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
                    String location = tableResultSet.getString("location");

                    String[] playerStat = {firstName, lastName, date, Integer.toString(attempted), Integer.toString(made), location};
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
            conn.closeConnection(connection);
        }
        return null;
    }

    /**
     * This method retrieves data from the database and stores it in an ArrayList of String arrays.
     * @return ArrayList<String[]> - an ArrayList of String arrays
     */
    public ArrayList<String[]> dataToArrayListFreeThrows()
    {
        ArrayList<String[]> freeThrowSessions = new ArrayList<>();
        connection = conn.openConnection();
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
            conn.closeConnection(connection);
        }
        return null;
    }
}
