// Add daily tracking if needed, such as a Map<Date, FreeThrowStats> 
public class Points {

    private int freeThrowsMade;
    private int freeThrowsAttempted;
    private String date;
    private String firstName;
    private String lastName;

    public Points(String firstName, String lastName, String date, int freeThrowsMade, int freeThrowsAttempted) {
        this.firstName = firstName;
        this.lastName = lastName;
        this.date = date;
        this.freeThrowsMade = freeThrowsMade;
        this.freeThrowsAttempted = freeThrowsAttempted;
    }

    public String getFirstName() {
        return firstName;
    }

    public String getLastName() {
        return lastName;
    }

    public String getDate() {
        return date;
    }

    public int getFreeThrowsMade() {
        return freeThrowsMade;
    }

    public int getFreeThrowsAttempted() {
        return freeThrowsAttempted;
    }

    public void setFreeThrowsMade(int freeThrowsMade) {
        this.freeThrowsMade = freeThrowsMade;
    }

    public void setFreeThrowsAttempted(int freeThrowsAttempted) {
        this.freeThrowsAttempted = freeThrowsAttempted;
    }

    public void updateFreeThrowStats(int made, int attempted) {
        this.freeThrowsMade += made;
        this.freeThrowsAttempted += attempted;
    }

    public double getFreeThrowSuccessRate() {
        if (freeThrowsAttempted == 0) return 0;
        
        return Math.round(((double)freeThrowsAttempted / freeThrowsMade) * 100);
    }

    @Override
    public String toString()
    {
        return "Name: " + firstName + " " + lastName + " | Date: " + date + " | Free Throws Made: " + freeThrowsMade + " | Free Throws Attempted: " + freeThrowsAttempted + " | Free Throw Success Rate: " + getFreeThrowSuccessRate() + "%";
    }
}


