import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;
import java.sql.*;


public class BasketballTeamRosterGUI extends JFrame {

    private ArrayList<Player> players;
    private DefaultListModel<Player> listModel;
    private JList<Player> playerList;
    private JTextField firstNameField, lastNameField, positionField, playerNumberField, graduationYearField;
    JButton increaseFontSizeButton;
    JButton decreaseFontSizeButton;
    private JButton addButton, removeButton, editButton;
    private JComboBox<String> sortingComboBox; // Added JComboBox for sorting methods
    private Player selectedPlayer;
    private Font defaultFont; // Added default font to store original font
    SQLConnection conn;

    public BasketballTeamRosterGUI() {
        super("Moravian Woman's Basketball Team Roster");
        players = new ArrayList<>();
        listModel = new DefaultListModel<>();
        playerList = new JList<>(listModel);
        firstNameField = new JTextField(15);
        lastNameField = new JTextField(15);
        positionField = new JTextField(15);
        playerNumberField = new JTextField(8);
        graduationYearField = new JTextField(5);
        addButton = new JButton("Add Player"); // Initialized the button to add player
        removeButton = new JButton("Archive Player"); // Initialized the button to remove player
        editButton = new JButton("Edit Player"); // Initialized the button to edit player
        increaseFontSizeButton = new JButton("Increase Font Size"); // Initialized the button
        decreaseFontSizeButton = new JButton("Decrease Font Size"); // Initialized the button
        sortingComboBox = new JComboBox<>(new String[]{"Sort by Last Name", "Sort by Player Number"}); // Initialized JComboBox
        defaultFont = firstNameField.getFont(); // Storing the default font
        conn = new SQLConnection();
        // repopulateLists();

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

        JPanel inputPanel = new JPanel(new GridLayout(10, 2)); //changing this will change how large the input fields are
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
        inputPanel.add(new JLabel("Sort By:")); // Added label for sorting combo box
        inputPanel.add(sortingComboBox); // Added sorting combo box
        

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

        // Creating tabbed pane for different sections
        JTabbedPane tabbedPane = new JTabbedPane();
        tabbedPane.addTab("Roster", mainPanel);
        
        tabbedPane.addTab("Roster", mainPanel);
        getContentPane().add(tabbedPane);
        getContentPane().add(tabbedPane);

        setSize(2160, 1920);
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        setVisible(true);
    }

    private JTextField createTextField(int columns) {
        JTextField textField = new JTextField(columns);
        textField.setPreferredSize(new Dimension(textField.getPreferredSize().width, 20)); // Set preferred height
        return textField;
    }


    private void clearFields() {
        firstNameField.setText("");
        lastNameField.setText("");;
        positionField.setText("");
        playerNumberField.setText("");
        graduationYearField.setText("");
    }

    private void loadPlayersFromDatabase() {
        try {
            Connection connection = DriverManager.getConnection("jdbc:mysql://localhost:3306/MoravianWomensTeam24", "project", "project");
            Statement statement = connection.createStatement();
            ResultSet resultSet = statement.executeQuery("SELECT * FROM players");
            while (resultSet.next()) {
                String firstName = resultSet.getString("firstName");
                String lastName = resultSet.getString("lastName");
                String position = resultSet.getString("position");
                int playerNumber = resultSet.getInt("playerNumber");
                int graduationYear = resultSet.getInt("graduationYear");
                int freeThrowsAttempted = resultSet.getInt("freeThrowsAttempted");
                int freeThrowsMade = resultSet.getInt("freeThrowsMade");
                players.add(new Player(firstName, lastName, position, playerNumber, graduationYear));
            }
            resultSet.close();
            statement.close();
            updateListModel();
        } catch (SQLException e) {
            e.printStackTrace();
            JOptionPane.showMessageDialog(this, "Failed to load players from database: " + e.getMessage(), "Error", JOptionPane.ERROR_MESSAGE);
        }
    }

    private void archivePlayer(Player player) {
        try {
            Connection connection = DriverManager.getConnection("jdbc:mysql://localhost:3306/MoravianWomensTeam24", "project", "project");
            String sql = "UPDATE players SET archived = ? WHERE firstName = ? AND lastName = ?";
            PreparedStatement statement = connection.prepareStatement(sql);
            statement.setBoolean(1, true);
            statement.setString(2, player.getFirstName());
            statement.setString(3, player.getlastName());
            statement.executeUpdate();
            statement.close();
        } catch (SQLException e) {
            e.printStackTrace();
            JOptionPane.showMessageDialog(this, "Failed to archive player: " + e.getMessage(), "Error", JOptionPane.ERROR_MESSAGE);
        }
    }

    private void sortPlayersByLastName() {
        Collections.sort(players, Comparator.comparing(Player::getlastName));
        updateListModel();
    }

    private void sortPlayersByPlayerNumber() {
        Collections.sort(players, Comparator.comparingInt(Player::getPlayerNumber));
        updateListModel();
    }

    private void updateListModel() {
        listModel.clear();
        for (Player player : players) {
            listModel.addElement(player);
        }
    }

    private void setFontSize(Font font) { // Method to set font size
        firstNameField.setText("");
        lastNameField.setText("");
        positionField.setFont(font);
        playerNumberField.setFont(font);
        graduationYearField.setFont(font);
        playerList.setFont(font);
    }

    // private void repopulateLists()
    // {
    //     ArrayList<String[]> data;
    //     data = conn.dataToArrayList();
    //     for(int x = 0; x < data.size(); x++)
    //     {
    //         String[] player = data.get(x);
    //         Player newPlayer = new Player(player[0], player[1], player[3], Integer.parseInt(player[2]), Integer.parseInt(player[4]));
    //         players.add(newPlayer);
    //         listModel.addElement(newPlayer);
    //     }
    // }

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
            return "Name: " + firstName +  ", " + lastName + " | Position: " + position + " | Player Number: " + playerNumber + " | Graduation Year: " + graduationYear;
        }
    }
    public class TabbedPaneExample extends JFrame {
        public TabbedPaneExample() {
            JTabbedPane tabbedPane = new JTabbedPane();
            JTabbedPane freeThrows = new JTabbedPane();
            freeThrows.add(new JLabel("Free Throws Attempted "));
            JPanel threeThrows = new JPanel();
            threeThrows.add(new JLabel("Three Points Attempted "));
            tabbedPane.addTab("Free Throws", freeThrows);
            tabbedPane.addTab("Three Pointers", threeThrows);

            getContentPane().add(tabbedPane);
        
            pack();
            setLocationRelativeTo(null);
        }

                public static void main(String[] args) {
                    SwingUtilities.invokeLater(() -> {
                        new BasketballTeamRosterGUI().new TabbedPaneExample().setVisible(true);
                    });
                }
            }
}