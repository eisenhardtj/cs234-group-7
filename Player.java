/**
 * Player class that creates a player object with a first name, last name, position, 
 * player number, and graduation year.
 * 
 * Authors: Jeffery Eisenhardt, Christine Colvin
 */
public class Player {
    private String firstName;
    private String lastName;
    private String position;
    private int playerNumber;
    private int graduationYear;

    public Player(String firstName, String lastName, String position, int playerNumber, int graduationYear) {
        this.firstName = firstName;
        this.lastName = lastName;
        this.position = position;
        this.playerNumber = playerNumber;
        this.graduationYear = graduationYear;
    }

    public String getFirstName() {
        return firstName;
    }

    public String getLastName() {
        return lastName;
    }

    public void setFirstName(String firstName) {
        this.firstName = firstName;
    }

    public void setLastName(String lastName) {
        this.lastName = lastName;
    }

    public String getPosition() {
        return position;
    }

    public void setPosition(String position) {
        this.position = position;
    }

    public int getPlayerNumber() {
        return playerNumber;
    }

    public void setPlayerNumber(int playerNumber) {
        this.playerNumber = playerNumber;
    }

    public int getGraduationYear() {
        return graduationYear;
    }

    public void setGraduationYear(int graduationYear) {
        this.graduationYear = graduationYear;
    }

    @Override
    public String toString() {
        return "Name: " + firstName +  ", " + lastName + " | Position: " + position + " | Player Number: " + playerNumber + " | Graduation Year: " + graduationYear;
    }
}