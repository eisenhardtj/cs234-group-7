import javax.swing.*;
import javax.swing.event.ListSelectionEvent;
import javax.swing.event.ListSelectionListener;
import javax.swing.table.DefaultTableModel;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;

/**
 * This class represents the main GUI for the basketball team roster. It contains
 * functionality for adding, archiving, and editing players, as well as sorting
 * players by last name or player number. It also contains functionality for
 * increasing and decreasing the font size of the GUI.
 * 
 * Authors: Jeffery Eisenhardt, Christine Colvin
 */
public class BasketballTeamRosterGUI extends JFrame {

    private ArrayList<Player> players;
    private DefaultTableModel tableModel;

    private JTable playerTable;
    private JTextField firstNameField, lastNameField, positionField, playerNumberField, graduationYearField;
    private JButton increaseFontSizeButton;
    private JButton decreaseFontSizeButton;
    private JButton addButton, archiveButton, editButton;
    private JComboBox<String> sortingComboBox; // Added JComboBox for sorting methods
    private SQLConnection conn;
    private ArchivedPanel archivedPanel;
    private FreeThrowPanel freeThrowPanel;
    private ThreePointPanel threePointPanel;
    private PersistData persistData;
    ChartPanel chartPanel;

    /**
     * Constructs a new BasketballTeamRosterGUI with necessary components and functionality.
     * Contains a table for displaying player information, text fields for inputting player
     * information, buttons for adding, archiving, and editing players, and a combo box for
     * sorting players by last name or player number. Contains action listeners for each button,
     * table, or combo box.
     */
    public BasketballTeamRosterGUI()
    {
        super("Moravian Woman's Basketball Team Roster");
        players = new ArrayList<>();
        tableModel = new DefaultTableModel();
        tableModel.addColumn("First Name");
        tableModel.addColumn("Last Name");
        tableModel.addColumn("Position");
        tableModel.addColumn("Player Number");
        tableModel.addColumn("Graduation Year");

        playerTable = new JTable(tableModel);
        playerTable.setSelectionMode(ListSelectionModel.SINGLE_SELECTION);

        JScrollPane scrollPane = new JScrollPane(playerTable);
        firstNameField = new JTextField(15);
        lastNameField = new JTextField(15);
        positionField = new JTextField(15);
        playerNumberField = new JTextField(8);
        graduationYearField = new JTextField(5);
        addButton = new JButton("Add Player");
        archiveButton = new JButton("Archive Player");
        editButton = new JButton("Edit Player");
        increaseFontSizeButton = new JButton("Increase Font Size");
        decreaseFontSizeButton = new JButton("Decrease Font Size");
        sortingComboBox = new JComboBox<>(new String[]{"Sort by Last Name", "Sort by Player Number"});
        firstNameField.getFont();
        conn = new SQLConnection();
        archivedPanel = new ArchivedPanel();
        freeThrowPanel = new FreeThrowPanel();
        chartPanel = new ChartPanel();
        threePointPanel = new ThreePointPanel(chartPanel);
        persistData = new PersistData();

        repopulateLists();

        /**
         * Action listener for the player table. When a row is selected, the text fields
         * are populated with the selected player's information.
         */
        addButton.addActionListener(new ActionListener()
        {
            @Override
            public void actionPerformed(ActionEvent e)
            {
                String firstName = firstNameField.getText();
                String lastName = lastNameField.getText();
                String position = positionField.getText();
                int playerNumber = Integer.parseInt(playerNumberField.getText());
                int graduationYear = Integer.parseInt(graduationYearField.getText());
                Player player = new Player(firstName, lastName, position, playerNumber, graduationYear);
                players.add(player);
                addToTableModel(player);
                conn.addPlayer(firstName, lastName, playerNumber, position, graduationYear);
                clearFields();
            }
        });

        /**
         * Action listener for the player table. When a row is selected, the text fields
         * are populated with the selected player's information.
         */
        archiveButton.addActionListener(new ActionListener()
        {
            @Override
            public void actionPerformed(ActionEvent e)
            {
                int selectedIndex = playerTable.getSelectedRow();
                if (selectedIndex != -1)
                {
                    archivedPanel.addToListModel(players.get(selectedIndex));
                    String firstName = (String) tableModel.getValueAt(selectedIndex, 0);
                    String lastName = (String) tableModel.getValueAt(selectedIndex, 1);
                    players.remove(selectedIndex);
                    tableModel.removeRow(selectedIndex);
                    conn.archivePlayer(firstName, lastName);
                    clearFields();
                }
            }
        });

        /**
         * Action listener for the player table. When a row is selected, the text fields
         * are populated with the selected player's information.
         */
        editButton.addActionListener(new ActionListener()
        {
            @Override
            public void actionPerformed(ActionEvent e)
            {
                int selectedIndex = playerTable.getSelectedRow();
                if (selectedIndex != -1) {
                    String firstName = firstNameField.getText();
                    String lastName = lastNameField.getText();
                    String position = positionField.getText();
                    int playerNumber = Integer.parseInt(playerNumberField.getText());
                    int graduationYear = Integer.parseInt(graduationYearField.getText());
                    Player updatedPlayer = new Player(firstName, lastName, position, playerNumber, graduationYear);
                    players.set(selectedIndex, updatedPlayer);
                    updateTableModel();
                    conn.editPlayer((String) tableModel.getValueAt(selectedIndex, 0),
                            (String) tableModel.getValueAt(selectedIndex, 1),
                            firstName, lastName, playerNumber, position, graduationYear);
                    clearFields();
                }
            }
        });

        /**
         * Action listener for the player table. When a row is selected, the text fields
         * are populated with the selected player's information.
         */
        increaseFontSizeButton.addActionListener(new ActionListener()
        {
            @Override
            public void actionPerformed(ActionEvent e)
            {
                increaseFontSize();
            }
        });

        /**
         * Action listener for the player table. When a row is selected, the text fields
         * are populated with the selected player's information.
         */
        decreaseFontSizeButton.addActionListener(new ActionListener()
        {
            @Override
            public void actionPerformed(ActionEvent e)
            {
                decreaseFontSize();
            }
        });

        /**
         * Action listener for the player table. When a row is selected, the text fields
         * are populated with the selected player's information.
         */
        sortingComboBox.addActionListener(new ActionListener()
        {
            @Override
            public void actionPerformed(ActionEvent e)
            {
                String selectedSortOption = (String) sortingComboBox.getSelectedItem();
                if (selectedSortOption.equals("Sort by Last Name"))
                {
                    sortPlayersByLastName();
                } else if (selectedSortOption.equals("Sort by Player Number"))
                {
                    sortPlayersByPlayerNumber();
                }
            }
        });

        /**
         * List selection listener for the player table. When a row is selected, the text fields
         * are populated with the selected player's information.
         */
        playerTable.getSelectionModel().addListSelectionListener(new ListSelectionListener() {
            @Override
            public void valueChanged(ListSelectionEvent e) {
                int selectedRow = playerTable.getSelectedRow();
                if (selectedRow != -1) {
                    Player selectedPlayer = players.get(selectedRow);
                    firstNameField.setText(selectedPlayer.getFirstName());
                    lastNameField.setText(selectedPlayer.getLastName());
                    positionField.setText(selectedPlayer.getPosition());
                    playerNumberField.setText(String.valueOf(selectedPlayer.getPlayerNumber()));
                    graduationYearField.setText(String.valueOf(selectedPlayer.getGraduationYear()));
                }
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

        // Add the panels to the frame
        JPanel buttonPanel = new JPanel(new FlowLayout(FlowLayout.CENTER));
        buttonPanel.add(addButton);
        buttonPanel.add(archiveButton);
        buttonPanel.add(editButton);
        buttonPanel.add(increaseFontSizeButton);
        buttonPanel.add(decreaseFontSizeButton);

        JPanel mainPanel = new JPanel(new BorderLayout());
        mainPanel.add(inputPanel, BorderLayout.WEST);
        mainPanel.add(buttonPanel, BorderLayout.SOUTH);
        mainPanel.add(scrollPane, BorderLayout.CENTER);

        JTabbedPane tabbedPane = new JTabbedPane();
        freeThrowPanel.setChartPanel(chartPanel);
        tabbedPane.addTab("Roster", mainPanel);
        tabbedPane.addTab("Archived Players", archivedPanel);
        tabbedPane.addTab("Free Throws", freeThrowPanel);
        tabbedPane.addTab("Three Pointers", threePointPanel);
        tabbedPane.addTab("Statistics", chartPanel);
        chartPanel.updateCharts();

        setContentPane(tabbedPane);

        setSize(3024, 1964);
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        setVisible(true);
    }

    /**
     * Adds a player to the table model.
     * @param player the player to add.
     */
    private void addToTableModel(Player player)
    {
        Object[] rowData = {player.getFirstName(), player.getLastName(), player.getPosition(),
                player.getPlayerNumber(), player.getGraduationYear()};
        tableModel.addRow(rowData);
    }

    /**
     * Updates the table model with the current player data.
     */
    private void updateTableModel()
    {
        tableModel.setRowCount(0); // Clear existing data
        for (Player player : players) {
            Object[] rowData = {player.getFirstName(), player.getLastName(), player.getPosition(),
                    player.getPlayerNumber(), player.getGraduationYear()};
            tableModel.addRow(rowData);
        }
    }

    /**
     * Clears the text fields.
     */
    private void clearFields()
    {
        firstNameField.setText("");
        lastNameField.setText("");
        positionField.setText("");
        playerNumberField.setText("");
        graduationYearField.setText("");
    }

    /**
     * Sorts the players by last name.
     */
    private void sortPlayersByLastName()
    {
        Collections.sort(players, Comparator.comparing(Player::getLastName));
        updateTableModel();
    }

    /**
     * Sorts the players by player number.
     */
    private void sortPlayersByPlayerNumber()
    {
        Collections.sort(players, Comparator.comparingInt(Player::getPlayerNumber));
        updateTableModel();
    }

    /**
     * Increases the font size of the GUI components.
     */
    private void increaseFontSize()
    {
        Font currentFont = playerTable.getFont();
        float currentSize = currentFont.getSize2D();
        float newSize = currentSize + 5f;
    
        // Scale the font size
        Font newFont = currentFont.deriveFont(newSize);
    
        // Scale the size of the components
        playerTable.setFont(newFont);
        firstNameField.setFont(newFont);
        lastNameField.setFont(newFont);
        positionField.setFont(newFont);
        playerNumberField.setFont(newFont);
        graduationYearField.setFont(newFont);
        increaseFontSizeButton.setFont(newFont);
        decreaseFontSizeButton.setFont(newFont);
        addButton.setFont(newFont);
        archiveButton.setFont(newFont);
        editButton.setFont(newFont);
        sortingComboBox.setFont(newFont);
    
        Dimension newSizeDimension = new Dimension((int) (getWidth() * 1.1), (int) (getHeight() * 1.1));
        setPreferredSize(newSizeDimension);
    }

    /**
     * Decreases the font size of the GUI components.
     */
    private void decreaseFontSize()
    {
        Font currentFont = playerTable.getFont();
        float currentSize = currentFont.getSize2D();
        float newSize = currentSize - 5f;
    
        // Scale down the font size
        Font newFont = currentFont.deriveFont(newSize);
    
        // Scale down the size of the components
        playerTable.setFont(newFont);
        firstNameField.setFont(newFont);
        lastNameField.setFont(newFont);
        positionField.setFont(newFont);
        playerNumberField.setFont(newFont);
        graduationYearField.setFont(newFont);
        increaseFontSizeButton.setFont(newFont);
        decreaseFontSizeButton.setFont(newFont);
        addButton.setFont(newFont);
        archiveButton.setFont(newFont);
        editButton.setFont(newFont);
        sortingComboBox.setFont(newFont);
    
        Dimension newSizeDimension = new Dimension((int) (getWidth() * 0.9), (int) (getHeight() * 0.9));
        setPreferredSize(newSizeDimension);
    }

    /**
     * Repopulates the player list from the database.
     */
    private void repopulateLists()
    {
        ArrayList<String[]> data;
        data = persistData.dataToArrayListTeamRoster();
        for (String[] player : data)
        {
            Player newPlayer = new Player(player[0], player[1], player[3], Integer.parseInt(player[2]), Integer.parseInt(player[4]));
            players.add(newPlayer);
            addToTableModel(newPlayer);
        }
    }

    /**
     * Main method to run the GUI.
     * @param args command line arguments
     */
    public static void main(String[] args)
    {
        SwingUtilities.invokeLater(new Runnable()
        {
            @Override
            public void run()
            {
                new BasketballTeamRosterGUI();
            }
        });
    }
}
