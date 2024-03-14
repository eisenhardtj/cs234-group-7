import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.util.ArrayList;

public class BasketballTeamRosterGUI extends JFrame {

    private ArrayList<Player> players;
    private DefaultListModel<Player> listModel;
    private JList<Player> playerList;
    private JTextField firstNameField, lastNameField, positionField, playerNumberField, graduationYearField;
    JButton increaseFontSizeButton;
    JButton decreaseFontSizeButton;
    private JButton addButton, removeButton, editButton;
    private Player selectedPlayer;
    private Font defaultFont; // Added default font to store original font
    SQLConnection conn;

    public BasketballTeamRosterGUI() {
        super("Basketball Team Roster");
        players = new ArrayList<>();
        listModel = new DefaultListModel<>();
        playerList = new JList<>(listModel);
        firstNameField = new JTextField(15);
        lastNameField = new JTextField(15);
        positionField = new JTextField(15);
        playerNumberField = new JTextField(8);
        graduationYearField = new JTextField(5);
        addButton = new JButton("Add Player"); // Initialized the button to add player
        removeButton = new JButton("Remove Player"); // Initialized the button to remove player
        editButton = new JButton("Edit Player"); // Initialized the button to edit player
        increaseFontSizeButton = new JButton("Increase Font Size"); // Initialized the button
        decreaseFontSizeButton = new JButton("Decrease Font Size"); // Initialized the button
        defaultFont = firstNameField.getFont(); // Storing the default font
        conn = new SQLConnection();

        // Action listener for the add, remove, edit, increase font size and decrease font size buttons

        addButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                String firstName = firstNameField.getText();
                String lastName = lastNameField.getText();
                String position = positionField.getText();
                int playerNumber = Integer.parseInt(playerNumberField.getText());
                int graduationYear = Integer.parseInt(graduationYearField.getText());
                Player player = new Player(firstName, lastName, position, playerNumber, graduationYear);
                players.add(player);
                listModel.addElement(player);
                conn.addPlayer(firstName, lastName, playerNumber, position, graduationYear);
                clearFields();
            }
        });

        removeButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                int selectedIndex = playerList.getSelectedIndex();
                if (selectedIndex != -1) {
                    String firstName = players.get(selectedIndex).getFirstName();
                    String lastName = players.get(selectedIndex).getlastName();
                    players.remove(selectedIndex);
                    listModel.remove(selectedIndex);
                    conn.removePlayer(firstName, lastName);
                    clearFields();
                }
            }
        });

        editButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                if (selectedPlayer != null) {
                    selectedPlayer.setFirstName(firstNameField.getText());
                    selectedPlayer.setLastName(lastNameField.getText());
                    selectedPlayer.setPosition(positionField.getText());
                    selectedPlayer.setPlayerNumber(Integer.parseInt(playerNumberField.getText()));
                    selectedPlayer.setGraduationYear(Integer.parseInt(graduationYearField.getText()));
                    listModel.set(playerList.getSelectedIndex(), selectedPlayer);
                    conn.editPlayer(selectedPlayer.getFirstName(), selectedPlayer.getlastName(), firstNameField.getText(), lastNameField.getText(), Integer.parseInt(playerNumberField.getText()), positionField.getText(), Integer.parseInt(graduationYearField.getText()));
                }
                clearFields();
            }
        });

        increaseFontSizeButton.addActionListener(new ActionListener() { 
            @Override
            public void actionPerformed(ActionEvent e) {
                Font currentFont = firstNameField.getFont();
                Font newFont = currentFont.deriveFont(currentFont.getSize() + 2f);
                setFontSize(newFont);
            }
        });

        decreaseFontSizeButton.addActionListener(new ActionListener() { 
            @Override
            public void actionPerformed(ActionEvent e) {
                Font currentFont = firstNameField.getFont();
                Font newFont = currentFont.deriveFont(currentFont.getSize() - 2f);
                setFontSize(newFont);
            }
        });


        playerList.addListSelectionListener(e -> {
            selectedPlayer = playerList.getSelectedValue();
            if (selectedPlayer != null) {
                firstNameField.setText(selectedPlayer.getFirstName());
                lastNameField.setText(selectedPlayer.getlastName());
                positionField.setText(selectedPlayer.getPosition());
                playerNumberField.setText(String.valueOf(selectedPlayer.getPlayerNumber()));
                graduationYearField.setText(String.valueOf(selectedPlayer.getGraduationYear()));
                
            }
        });

        JPanel inputPanel = new JPanel(new GridLayout(5, 2));
        inputPanel.add(new JLabel("First Name:"));
        inputPanel.add(firstNameField);
        inputPanel.add(new JLabel("Last Name:"));
        inputPanel.add(lastNameField);
        inputPanel.add(new JLabel("Position:"));
        inputPanel.add(positionField);
        inputPanel.add(new JLabel("Player Number:"));
        inputPanel.add(playerNumberField);
        inputPanel.add(new JLabel("Graduation Year:"));
        inputPanel.add(graduationYearField);
        

        JPanel buttonPanel = new JPanel(new FlowLayout(FlowLayout.CENTER));
        buttonPanel.add(addButton); // Added button to add player
        buttonPanel.add(removeButton); // Added button to remove player
        buttonPanel.add(editButton); // Added button to edit player
        buttonPanel.add(increaseFontSizeButton); // Added button to increase font size
        buttonPanel.add(decreaseFontSizeButton); // Added button to decrease font size

        JScrollPane scrollPane = new JScrollPane(playerList);

        JPanel mainPanel = new JPanel(new BorderLayout());
        mainPanel.add(inputPanel, BorderLayout.WEST);
        mainPanel.add(buttonPanel, BorderLayout.SOUTH);
        mainPanel.add(scrollPane, BorderLayout.CENTER);

        getContentPane().add(mainPanel);

        setSize(getMaximumSize());
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        setVisible(true);
    }

    private JTextField createTextField(int columns) {
        JTextField textField = new JTextField(columns);
        textField.setPreferredSize(new Dimension(textField.getPreferredSize().width, 10)); // Set preferred height
        return textField;
    }

    private void clearFields() {
        firstNameField.setText("");
        lastNameField.setText("");;
        positionField.setText("");
        playerNumberField.setText("");
        graduationYearField.setText("");
    }
    private void setFontSize(Font font) { // Method to set font size
        firstNameField.setText("");
        lastNameField.setText("");
        positionField.setFont(font);
        playerNumberField.setFont(font);
        graduationYearField.setFont(font);
        playerList.setFont(font);
    }

    public static void main(String[] args) {
        SwingUtilities.invokeLater(new Runnable() {
            @Override
            public void run() {
                new BasketballTeamRosterGUI();
            }
        });
    }

    private static class Player {
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

        public String getlastName() {
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
            return firstName +  ", " + lastName + " | Position: " + position + " | Player Number: " + playerNumber + " | Graduation Year: " + graduationYear;
        }
    }
}