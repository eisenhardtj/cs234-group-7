import java.util.ArrayList;

public class Roster {
    private ArrayList<BasketballPlayer> players = new ArrayList<>();

    public void addPlayer(BasketballPlayer player) {
        players.add(player);
    }

    public void removePlayer(int index) {
        players.remove(index);
    }

    public String displayRoster() {
        StringBuilder roster = new StringBuilder();
        for (BasketballPlayer player : players) {
            roster.append(player).append("\n");
        }
        return roster.toString();
    }
}
