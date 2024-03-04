import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.PreparedStatement;
import java.sql.SQLException;

import java.util.ArrayList;

public class Roster {
    private ArrayList<BasketballPlayer> players = new ArrayList<>();
    private Connection connection;

    public Roster()
    {
        try
        {
            String databaseUrl = "jdbc:mysql://localhost:3306/MoravianWomensTeam24";
            String username = "project";
            String password = "project";
            connection = DriverManager.getConnection(databaseUrl, username, password);
        }
        catch (SQLException e)
        {
            System.out.println("Invalid SQL credentials.");
            e.printStackTrace();
        }
    }

    public void addPlayer(BasketballPlayer player) {
        try {
            String sql = "INSERT INTO TeamRoster (player_name, player_number, position, expected_graduation_date, height, weight) VALUES (?, ?, ?, ?, ?, ?)";
            PreparedStatement addStatement = connection.prepareStatement(sql);

            addStatement.setString(1, player.getName());
            addStatement.setInt(2, player.getNumber());
            addStatement.setString(3, player.getPosition());
            addStatement.setInt(4, player.getExpectedGraduationDate());
            addStatement.setDouble(5, player.getHeight());
            addStatement.setDouble(6, player.getWeight());

            addStatement.executeUpdate();
            
            addStatement.close();
            players.add(player);
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

    public void removePlayer(String playerName) {
        PreparedStatement removeQuery;
        try {
            String sql = "DELETE FROM TeamRoster WHERE player_name = ?";
            removeQuery = connection.prepareStatement(sql);
            removeQuery.setString(1, playerName);
            removeQuery.executeUpdate();
            removeQuery.close();

            for (BasketballPlayer player : players) {
                if (player.getName().equals(playerName)) {
                    players.remove(player);
                    break;
                }
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }
    
    public String displayRoster() {
        StringBuilder roster = new StringBuilder();
        for (BasketballPlayer player : players) {
            roster.append(player).append("\n");
        }
        return roster.toString();
    }
}
