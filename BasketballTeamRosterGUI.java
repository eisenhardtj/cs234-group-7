import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

public class BasketballTeamRosterGUI extends JFrame {
    private DefaultListModel<String> rosterListModel;
    private JList<String> rosterList;

    public BasketballTeamRosterGUI() {
        setTitle("Basketball Team Roster");
        setSize(getMaximumSize());
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);

        JPanel mainPanel = new JPanel(new BorderLayout());
        JPanel inputPanel = new JPanel(new GridLayout(5, 2));
        JPanel buttonPanel = new JPanel(new FlowLayout());

        JLabel nameLabel = new JLabel("Name:");
        JLabel playerNumberLabel = new JLabel("Player Number:");
        JLabel positionLabel = new JLabel("Position:");
        JLabel gradYearLabel = new JLabel("Graduation Year:");

        JTextField nameField = new JTextField();
        JTextField playerNumberField = new JTextField();
        JTextField positionField = new JTextField();
        JTextField gradYearField = new JTextField();

        JButton addButton = new JButton("Add Player");
        addButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                addPlayer(nameField.getText(), playerNumberField.getText(), positionField.getText(), gradYearField.getText());
            }
        });

        JButton removeButton = new JButton("Remove Player");
        removeButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                removePlayer();
            }
        });

        JButton editButton = new JButton("Edit Player");
        editButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                editPlayer();
            }
        });

        JButton statsButton = new JButton("Player Stats");
        statsButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                openPlayerStatsFrame();
            }
        });

        rosterListModel = new DefaultListModel<>();
        rosterList = new JList<>(rosterListModel);
        JScrollPane scrollPane = new JScrollPane(rosterList);

        inputPanel.add(nameLabel);
        inputPanel.add(nameField);
        inputPanel.add(playerNumberLabel);
        inputPanel.add(playerNumberField);
        inputPanel.add(positionLabel);
        inputPanel.add(positionField);
        inputPanel.add(gradYearLabel);
        inputPanel.add(gradYearField);

        buttonPanel.add(addButton);
        buttonPanel.add(removeButton);
        buttonPanel.add(editButton);
        buttonPanel.add(statsButton);

        mainPanel.add(inputPanel, BorderLayout.NORTH);
        mainPanel.add(scrollPane, BorderLayout.CENTER);
        mainPanel.add(buttonPanel, BorderLayout.SOUTH);

        add(mainPanel);
    }

    private void addPlayer(String name, String playerNumber, String position, String gradYear) {
        if (!name.isEmpty() && !playerNumber.isEmpty() && !position.isEmpty() && !gradYear.isEmpty()) {
            String playerInfo = String.format("Name: %s, Player Number: %s, Position: %s, Graduation Year: %s",
                    name, playerNumber, position, gradYear);
            rosterListModel.addElement(playerInfo);
        } else {
            JOptionPane.showMessageDialog(this, "Please fill in all fields", "Error", JOptionPane.ERROR_MESSAGE);
        }
    }

    private void removePlayer() {
        int selectedIndex = rosterList.getSelectedIndex();
        if (selectedIndex != -1) {
            rosterListModel.remove(selectedIndex);
        } else {
            JOptionPane.showMessageDialog(this, "Please select a player to remove", "Error", JOptionPane.ERROR_MESSAGE);
        }
    }

    private void editPlayer() {
        // Implement editPlayer method as needed
    }

    private void openPlayerStatsFrame() {
        PlayerStatsFrame statsFrame = new PlayerStatsFrame();
        statsFrame.setVisible(true);
    }

    public static void main(String[] args) {
        SwingUtilities.invokeLater(new Runnable() {
            @Override
            public void run() {
                new BasketballTeamRosterGUI().setVisible(true);
            }
        });
    }
}

class PlayerStatsFrame extends JFrame {
    private JTextField attemptedField, madeField, percentageField;

    public PlayerStatsFrame() {
        setTitle("Player Stats");
        setSize(300, 200);
        setDefaultCloseOperation(JFrame.DISPOSE_ON_CLOSE);

        JPanel panel = new JPanel(new GridLayout(3, 2));

        JLabel attemptedLabel = new JLabel("3 Points Attempted:");
        JLabel madeLabel = new JLabel("3 Points Made:");
        JLabel percentageLabel = new JLabel("Percentage:");

        attemptedField = new JTextField();
        madeField = new JTextField();
        percentageField = new JTextField();
        percentageField.setEditable(false);

        JButton calculateButton = new JButton("Calculate");
        calculateButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                calculatePercentage();
            }
        });

        panel.add(attemptedLabel);
        panel.add(attemptedField);
        panel.add(madeLabel);
        panel.add(madeField);
        panel.add(percentageLabel);
        panel.add(percentageField);
        panel.add(new JLabel());
        panel.add(calculateButton);

        add(panel);
    }

    private void calculatePercentage() {
        try {
            int attempted = Integer.parseInt(attemptedField.getText());
            int made = Integer.parseInt(madeField.getText());
            double percentage = (double) made / attempted * 100;
            percentageField.setText(String.format("%.2f%%", percentage));
        } catch (NumberFormatException ex) {
            JOptionPane.showMessageDialog(this, "Please enter valid numbers", "Error", JOptionPane.ERROR_MESSAGE);
        }
    }
}
