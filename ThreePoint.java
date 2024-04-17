public class ThreePoint 
{
    private int ThreePointsMade;
    private int ThreePointsAttempted;
    private String date;
    private String firstName;
    private String lastName;
    private String location;

    public ThreePoint(String firstName, String lastName, String date, int Attempted, int Made, String location)
    {
        this.firstName = firstName;
        this.lastName = lastName;
        this.date = date;
        this.ThreePointsMade = Made;
        this.ThreePointsAttempted = Attempted;
        this.location = location;
    }

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

    public void setLocation(String location)
    {
        this.location = location;
    }

    public String getLocation()
    {
        return location;
    }

    public int getThreePointsMade()
    {
        return ThreePointsMade;
    }

    public int getThreePointsAttempted()
    {
        return ThreePointsAttempted;
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

    public void setThreePointsMade(int ThreePointsMade)
    {
        this.ThreePointsMade = ThreePointsMade;
    }

    public void setThreePointsAttempted(int ThreePointsAttempted)
    {
        this.ThreePointsAttempted = ThreePointsAttempted;
    }

    public void updateThreePointStats(int made, int attempted)
    {
        this.ThreePointsMade += made;
        this.ThreePointsAttempted += attempted;
    }

    public double getThreePointSuccessRate()
    {
        if (ThreePointsAttempted == 0) return 0;
        
        return Math.round(((double) ThreePointsMade / ThreePointsAttempted) * 100);
    }

    @Override
    public String toString()
    {
        return "Name: " + firstName + " " + lastName + " | Date: " + date + " | Three Points Made: " + ThreePointsMade + " | Three Point Attempted: " + ThreePointsAttempted + " | Three Point Success Rate: " + getThreePointSuccessRate() + "% | Location: " + location;
    }
}
