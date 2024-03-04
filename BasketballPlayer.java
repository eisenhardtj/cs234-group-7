public class BasketballPlayer {
    private String name;
    private int number;
    private String position;
    private int expectedGraduationDate;
    private double height;
    private double weight;

    public BasketballPlayer(String name, int number, String position, int expectedGraduationDate, double height, double weight) {
        this.name = name;
        this.number = number;
        this.position = position;
        this.expectedGraduationDate = expectedGraduationDate;
        this.height = height;
        this.weight = weight;
    }

    public String getName() {
        return name;
    }

    public int getNumber() {
        return number;
    }

    public String getPosition() {
        return position;
    }

    public int getExpectedGraduationDate() {
        return expectedGraduationDate;
    }

    public double getHeight() {
        return height;
    }

    public double getWeight() {
        return weight;
    }

    public String toString() {
        return "Name: " + name + ", Number: " + number + ", Position: " + position +
               ", Graduation Date: " + expectedGraduationDate + ", Height: " + height + ", Weight: " + weight;
    }
}
