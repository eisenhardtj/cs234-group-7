public class BasketballPlayer {
    private String name;
    private int number;
    private String position;

    public BasketballPlayer(String name, int number, String position) {
        this.name = name;
        this.number = number;
        this.position = position;
    }

    public String toString() {
        return "Name: " + name + ", Number: " + number + ", Position: " + position;
    }
}
