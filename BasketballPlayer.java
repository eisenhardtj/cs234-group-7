import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.util.ArrayList;

class BasketballPlayer {
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

class Roster {
    private ArrayList<BasketballPlayer> players = new ArrayList<>();

    public void addPlayer(BasketballPlayer player) {
        players.add(player);
    }

    public void removePlayer(int index) {
        players.remove(index);
    }

    public String displayRoster() {
        StringBuilder roster = new StringBuilder();
        for (BasketballPlayer player : players) {
            roster.append(player.toString()).append("\n");
        }
        return roster.toString();
    }
}

public class BasketballRosterApp extends JFrame implements ActionListener {
    private Roster roster;
    private JTextArea rosterTextArea;
    private JTextField nameField, numberField, positionField;

    public BasketballRosterApp() {
        setTitle("Basketball Roster");
        setSize(400, 300);
        setDefaultCloseOperation(EXIT_ON_CLOSE);
        
        roster = new Roster();

        JPanel inputPanel = new JPanel(new GridLayout(4, 2));
        inputPanel.add(new JLabel("Name:"));
        nameField = new JTextField();
        inputPanel.add(nameField);
        inputPanel.add(new JLabel("Number:"));
        numberField = new JTextField();
        inputPanel.add(numberField);
        inputPanel.add(new JLabel("Position:"));
        positionField = new JTextField();
        inputPanel.add(positionField);

        JButton addButton = new JButton("Add Player");
        addButton.addActionListener(this);
        inputPanel.add(addButton);

        JButton removeButton = new JButton("Remove Player");
        removeButton.addActionListener(this);
        inputPanel.add(removeButton);

        rosterTextArea = new JTextArea();
        rosterTextArea.setEditable(false);

        JScrollPane scrollPane = new JScrollPane(rosterTextArea);

        getContentPane().add(inputPanel, BorderLayout.NORTH);
        getContentPane().add(scrollPane, BorderLayout.CENTER);
    }

    public static void main(String[] args) {
        SwingUtilities.invokeLater(() -> {
            BasketballRosterApp app = new BasketballRosterApp();
            app.setVisible(true);
        });
    }

    @Override
    public void actionPerformed(ActionEvent e) {
        if (e.getActionCommand().equals("Add Player")) {
            String name = nameField.getText();
            int number = Integer.parseInt(numberField.getText());
            String position = positionField.getText();
            BasketballPlayer player = new BasketballPlayer(name, number, position);
            roster.addPlayer(player);
            rosterTextArea.setText(roster.displayRoster());
        } else if (e.getActionCommand().equals("Remove Player")) {
            // Implement removing player functionality
            // You can use roster.removePlayer(index) method
        }
    }
}
