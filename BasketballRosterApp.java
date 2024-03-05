/**
 * Main class, when ran instatiates a gui panel using java swing.
 * 
 * Authors: 
 * Jeffery Eisenhardt - eisenhardtj
 * Christine Colvin - christinecolvin
 * Cole Aydelotte - coleaydelotte
 * Jalil Rodriguez - JalilR08
 */
import javax.swing.*;

public class BasketballRosterApp {
    public static void main(String[] args) {
        SwingUtilities.invokeLater(() -> {
            BasketballRosterFrame frame = new BasketballRosterFrame();
            frame.setVisible(true);
        });
    }
}
