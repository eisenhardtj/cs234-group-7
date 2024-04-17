import javax.swing.*;
import javax.swing.table.DefaultTableModel;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;

public class BasketballTeamRosterGUI extends JFrame {

    private ArrayList<Player> players;
    private DefaultTableModel tableModel;

    private JTable playerTable;
    private JTextField firstNameField, lastNameField, positionField, playerNumberField, graduationYearField, searchField;
    private JButton increaseFontSizeButton;
    private JButton decreaseFontSizeButton;
    private JButton searchButton = new JButton("Search");
    private JButton addButton, archiveButton, editButton;
    private JComboBox<String> sortingComboBox; // Added JComboBox for sorting methods
    private Player selectedPlayer;
    private SQLConnection conn;
    private ArchivedPanel archivedPanel;
    private FreeThrowPanel freeThrowPanel;
    private ThreePointPanel threePointPanel;
    private PersistData persistData;
    ChartPanel chartPanel;

    public BasketballTeamRosterGUI() {
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
        searchField = new JTextField(15);
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
                addToTableModel(player);
                conn.addPlayer(firstName, lastName, playerNumber, position, graduationYear);
                clearFields();
            }
        });

        archiveButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                int selectedIndex = playerTable.getSelectedRow();
                if (selectedIndex != -1) {
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

        editButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
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

        increaseFontSizeButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                increaseFontSize();
            }
        });

        decreaseFontSizeButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                decreaseFontSize();
            }
        });

        searchButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                String searchText = searchField.getText();
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
        inputPanel.add(new JLabel("Search:"));
        inputPanel.add(searchField);
        inputPanel.add(new JLabel("Sort By:"));
        inputPanel.add(sortingComboBox);


        JPanel buttonPanel = new JPanel(new FlowLayout(FlowLayout.CENTER));
        buttonPanel.add(addButton);
        buttonPanel.add(archiveButton);
        buttonPanel.add(editButton);
        buttonPanel.add(increaseFontSizeButton);
        buttonPanel.add(decreaseFontSizeButton);
        buttonPanel.add(searchButton);

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

        setContentPane(tabbedPane);

        setSize(3024, 1964);
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        setVisible(true);
    }

    private void addToTableModel(Player player) {
        Object[] rowData = {player.getFirstName(), player.getLastName(), player.getPosition(),
                player.getPlayerNumber(), player.getGraduationYear()};
        tableModel.addRow(rowData);
    }

    private void updateTableModel() {
        tableModel.setRowCount(0); // Clear existing data
        for (Player player : players) {
            Object[] rowData = {player.getFirstName(), player.getLastName(), player.getPosition(),
                    player.getPlayerNumber(), player.getGraduationYear()};
            tableModel.addRow(rowData);
        }
    }

    private void clearFields() {
        firstNameField.setText("");
        lastNameField.setText("");
        positionField.setText("");
        playerNumberField.setText("");
        graduationYearField.setText("");
    }

    private void sortPlayersByLastName() {
        Collections.sort(players, Comparator.comparing(Player::getLastName));
        updateTableModel();
    }

    private void sortPlayersByPlayerNumber() {
        Collections.sort(players, Comparator.comparingInt(Player::getPlayerNumber));
        updateTableModel();
    }

    private void increaseFontSize() {
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
    private void decreaseFontSize() {
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

    private void setFontSize(Font font) {
        playerTable.setFont(font);
        firstNameField.setFont(font);
        lastNameField.setFont(font);
        positionField.setFont(font);
        playerNumberField.setFont(font);
        graduationYearField.setFont(font);
    }

    private void repopulateLists() {
        ArrayList<String[]> data;
        data = persistData.dataToArrayListTeamRoster();
        for (String[] player : data) {
            Player newPlayer = new Player(player[0], player[1], player[3], Integer.parseInt(player[2]), Integer.parseInt(player[4]));
            players.add(newPlayer);
            addToTableModel(newPlayer);
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
