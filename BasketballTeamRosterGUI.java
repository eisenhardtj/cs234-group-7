import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;

/**
 * This class creates a GUI that allows the user to input data for a basketball team roster, this
 * can include the first name, last name, position, player number, and graduation year. The user can
 * also add three point session, archive a player, or add a free throw session. The user can also increase
 * or decrease the font size on any tab.
 * 
 * Authors: Jeffery Eisenhardt, Christine Colvin
 */
public class BasketballTeamRosterGUI extends JFrame {

    private ArrayList<Player> players;
    private DefaultListModel<Player> listModel;
    
    private JList<Player> playerList;
    private JTextField firstNameField, lastNameField, positionField, playerNumberField, graduationYearField;
    private JButton increaseFontSizeButton;
    private JButton decreaseFontSizeButton;
    private JButton addButton, archiveButton, editButton;
    private JComboBox<String> sortingComboBox; // Added JComboBox for sorting methods
    private Player selectedPlayer;
    private SQLConnection conn;
    private ArchivedPanel archivedPanel;
    private FreeThrowPanel freeThrowPanel;
    private ThreePointPanel threePointPanel;
    private PersistData persistData;

     // Added closing curly brace to complete the block

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
        archiveButton = new JButton("Archive Player"); // Initialized the button to remove player
        editButton = new JButton("Edit Player"); // Initialized the button to edit player
        increaseFontSizeButton = new JButton("Increase Font Size"); // Initialized the button
        decreaseFontSizeButton = new JButton("Decrease Font Size"); // Initialized the button
        sortingComboBox = new JComboBox<>(new String[]{"Sort by Last Name", "Sort by Player Number"}); // Initialized JComboBox
        firstNameField.getFont();
        conn = new SQLConnection();
        archivedPanel = new ArchivedPanel();
        freeThrowPanel = new FreeThrowPanel();
        threePointPanel = new ThreePointPanel();
        ChartPanel ChartPanel = new ChartPanel();
        persistData = new PersistData();

        repopulateLists();
        
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

        archiveButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                int selectedIndex = playerList.getSelectedIndex();
                if (selectedIndex != -1) 
                {
                    archivedPanel.addToListModel(players.get(selectedIndex));
                    String firstName = players.get(selectedIndex).getFirstName();
                    String lastName = players.get(selectedIndex).getLastName();
                    players.remove(selectedIndex);
                    listModel.remove(selectedIndex);
                    conn.archivePlayer(firstName, lastName);
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
                    conn.editPlayer(selectedPlayer.getFirstName(), selectedPlayer.getLastName(), firstNameField.getText(), lastNameField.getText(), Integer.parseInt(playerNumberField.getText()), positionField.getText(), Integer.parseInt(graduationYearField.getText()));
                }
                clearFields();
            }
        });

        increaseFontSizeButton.addActionListener(new ActionListener() { 
            @Override
            public void actionPerformed(ActionEvent e) {
                Font currentFont = firstNameField.getFont();
                Font newFont = currentFont.deriveFont(currentFont.getSize() + 5f);
                setFontSize(newFont);
            }
        });

        decreaseFontSizeButton.addActionListener(new ActionListener() { 
            @Override
            public void actionPerformed(ActionEvent e) {
                Font currentFont = firstNameField.getFont();
                Font newFont = currentFont.deriveFont(currentFont.getSize() - 5f);
                setFontSize(newFont);
            }
        });
        
        sortingComboBox.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                String selectedSortOption = (String) sortingComboBox.getSelectedItem();
                if (selectedSortOption.equals("Sort by Last Name")) {
                    sortPlayersByLastName();
                } else if (selectedSortOption.equals("Sort by Player Number")) {
                    sortPlayersByPlayerNumber();
                }
            }
        });

        playerList.addListSelectionListener(e -> {
            selectedPlayer = playerList.getSelectedValue();
            if (selectedPlayer != null) {
                firstNameField.setText(selectedPlayer.getFirstName());
                lastNameField.setText(selectedPlayer.getLastName());
                positionField.setText(selectedPlayer.getPosition());
                playerNumberField.setText(String.valueOf(selectedPlayer.getPlayerNumber()));
                graduationYearField.setText(String.valueOf(selectedPlayer.getGraduationYear()));
            }
        });

        JPanel inputPanel = new JPanel(new GridLayout(10, 2));
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
        inputPanel.add(new JLabel("Sort By:"));
        inputPanel.add(sortingComboBox);

        JPanel buttonPanel = new JPanel(new FlowLayout(FlowLayout.CENTER));
        buttonPanel.add(addButton);
        buttonPanel.add(archiveButton);
        buttonPanel.add(editButton);
        buttonPanel.add(increaseFontSizeButton);
        buttonPanel.add(decreaseFontSizeButton);

        JScrollPane scrollPane = new JScrollPane(playerList);

        JPanel mainPanel = new JPanel(new BorderLayout());
        mainPanel.add(inputPanel, BorderLayout.WEST);
        mainPanel.add(buttonPanel, BorderLayout.SOUTH);
        mainPanel.add(scrollPane, BorderLayout.CENTER);

        JTabbedPane tabbedPane = new JTabbedPane();
        tabbedPane.addTab("Roster", mainPanel);
        tabbedPane.addTab("Archived Players", archivedPanel); 
        tabbedPane.addTab("Free Throws", freeThrowPanel);
        tabbedPane.addTab("Three Pointers", threePointPanel);
        tabbedPane.addTab("Statistices", ChartPanel);
    
        setContentPane(tabbedPane);

        setSize(2160, 1920);
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        setVisible(true);

        JPanel columnPanel = new JPanel(new GridLayout(0, 5));

        columnPanel.add(new JLabel("First Name"));
        columnPanel.add(new JLabel("Last Name"));
        columnPanel.add(new JLabel("Position"));
        columnPanel.add(new JLabel("Player Number"));
        columnPanel.add(new JLabel("Graduation Year"));

        for (Player player : players) {
            columnPanel.add(new JLabel(player.getFirstName()));
            columnPanel.add(new JLabel(player.getLastName()));
            columnPanel.add(new JLabel(player.getPosition()));
            columnPanel.add(new JLabel(String.valueOf(player.getPlayerNumber())));
            columnPanel.add(new JLabel(String.valueOf(player.getGraduationYear())));
        }

        JScrollPane columnScrollPane = new JScrollPane(columnPanel);

        mainPanel.add(columnScrollPane, BorderLayout.NORTH);
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

    private void sortPlayersByLastName() {
        Collections.sort(players, Comparator.comparing(Player::getLastName));
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

    private void setFontSize(Font font) {
        firstNameField.setFont(font);
        lastNameField.setFont(font);
        positionField.setFont(font);
        playerNumberField.setFont(font);
        graduationYearField.setFont(font);
        playerList.setFont(font);
    }

    private void repopulateLists()
    {
        ArrayList<String[]> data;
        data = persistData.dataToArrayListTeamRoster();
        for(int x = 0; x < data.size(); x++)
        {
            String[] player = data.get(x);
            Player newPlayer = new Player(player[0], player[1], player[3], Integer.parseInt(player[2]), Integer.parseInt(player[4]));
            players.add(newPlayer);
            listModel.addElement(newPlayer);
        }
    }

    public static void main(String[] args) {
        SwingUtilities.invokeLater(new Runnable() {
            @Override
            public void run() {
                new BasketballTeamRosterGUI();
            }
        });
        
    }
}







