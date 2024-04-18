/**
 * Class that represents a player's point session for a given date.
 * 
 * Author: Jalil Rodriquez
 */
public class Points
{

    private int freeThrowsMade;
    private int freeThrowsAttempted;
    private String date;
    private String firstName;
    private String lastName;

    /**
     * Constructs a new Points object.
     * @param firstName
     * @param lastName
     * @param date
     * @param freeThrowsAttempted
     * @param freeThrowsMade
     */
    public Points(String firstName, String lastName, String date, int freeThrowsAttempted, int freeThrowsMade)
    {
        this.firstName = firstName;
        this.lastName = lastName;
        this.date = date;
        this.freeThrowsMade = freeThrowsMade;
        this.freeThrowsAttempted = freeThrowsAttempted;
    }

    // Getters and Setters
    public String getFirstName()
    {
        return firstName;
    }

    public String getLastName()
    {
        return lastName;
    }

    public String getDate()
    {
        return date;
    }

    public int getFreeThrowsMade()
    {
        return freeThrowsMade;
    }

    public int getFreeThrowsAttempted()
    {
        return freeThrowsAttempted;
    }

    public void setFirstName(String firstName)
    {
        this.firstName = firstName;
    }

    public void setLastName(String lastName)
    {
        this.lastName = lastName;
    }

    public void setDate(String date)
    {
        this.date = date;
    }

    public void setFreeThrowsMade(int freeThrowsMade)
    {
        this.freeThrowsMade = freeThrowsMade;
    }

    public void setFreeThrowsAttempted(int freeThrowsAttempted)
    {
        this.freeThrowsAttempted = freeThrowsAttempted;
    }

    public void updateFreeThrowStats(int made, int attempted)
    {
        this.freeThrowsMade += made;
        this.freeThrowsAttempted += attempted;
    }

    public double getFreeThrowSuccessRate()
    {
        if (freeThrowsAttempted == 0) return 0;
        
        return Math.round(((double) freeThrowsMade / freeThrowsAttempted) * 100);
    }

    /**
     * Returns a string representation of the Points object.
     */
    @Override
    public String toString()
    {
        return "Name: " + firstName + " " + lastName + " | Date: " + date + " | Free Throws Made: " + freeThrowsMade + " | Free Throws Attempted: " + freeThrowsAttempted + " | Free Throw Success Rate: " + getFreeThrowSuccessRate() + "%";
    }
}
