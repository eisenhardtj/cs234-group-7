import javax.swing.*;

public class BasketballRosterApp {
    public static void main(String[] args) {
        SwingUtilities.invokeLater(() -> {
            BasketballRosterFrame frame = new BasketballRosterFrame();
            frame.setVisible(true);
        });
    }
}
