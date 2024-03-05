/**
 * BasketballPlayer with the private variables: name, number, position, expectedGradutationDate, height, and weight.
 * 
 * Authors: 
 * Jeffery Eisenhardt - eisenhardtj
 * Christine Colvin - christinecolvin
 * Cole Aydelotte - coleaydelotte
 * Jalil Rodriguez - JalilR08
 */
public class BasketballPlayer {
    private String name;
    private int number;
    private String position;
    private int expectedGraduationDate;
    private String height;
    private double weight;

    public BasketballPlayer(String name, int number, String position, int expectedGraduationDate, String height, double weight) {
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

    public String getHeight() {
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
