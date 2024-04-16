import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;

public class PersistData 
{
    private SQLConnection conn;
    private Connection connection;

    public PersistData()
    {
        conn = new SQLConnection();
    }

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
            conn.closeConnection(connection);
        }
        return null;
    }

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
