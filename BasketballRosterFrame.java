import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

public class BasketballRosterFrame extends JFrame {
    private Roster roster;

    public BasketballRosterFrame() {
        setTitle("Basketball Roster");
        setSize(400, 300);
        setDefaultCloseOperation(EXIT_ON_CLOSE);

        roster = new Roster();

        JPanel inputPanel = new InputPanel(roster);
        JTextArea rosterTextArea = new JTextArea();
        JScrollPane scrollPane = new JScrollPane(rosterTextArea);

        getContentPane().add(inputPanel, BorderLayout.NORTH);
        getContentPane().add(scrollPane, BorderLayout.CENTER);
    }
}
