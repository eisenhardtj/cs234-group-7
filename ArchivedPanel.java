import java.awt.BorderLayout;
import java.awt.Font;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;
import javax.swing.JButton;
import javax.swing.JComboBox;
import javax.swing.JPanel;
import javax.swing.JScrollPane;
import javax.swing.JTable;
import javax.swing.table.DefaultTableModel;

/**
 * This class represents a panel for displaying archived player information. Has 
 * functionality for increasing/decreasing font size, deleting players, and sorting
 * players by name or graduation year.
 * 
 * Author: Cole Aydelotte
 */
public class ArchivedPanel extends JPanel
{
    private DefaultTableModel archivedTableModel;
    private JTable archivedPlayerTable;
    private ArrayList<Player> archivedPlayersList;
    private JPanel buttonPanel;
    private JButton increaseFontSizeButton;
    private JButton decreaseFontSizeButton;
    private JButton deletePlayerButton;
    private JComboBox<String> sortingComboBox;
    private SQLConnection conn = new SQLConnection();
    private PersistData persistData = new PersistData();

    /**
     * Constructs a new ArchivedPanel with necessary components and functionality.
     * Contains a table for displaying archived player information, buttons for
     * increasing/decreasing font size, deleting players, and sorting players by name
     * or graduation year. Contains action listeners for each button, table, or combo box.
     */
    public ArchivedPanel() {
        archivedTableModel = new DefaultTableModel();
        archivedPlayerTable = new JTable(archivedTableModel);
        archivedPlayersList = new ArrayList<>();
        buttonPanel = new JPanel();
        increaseFontSizeButton = new JButton("Increase Font Size");
        decreaseFontSizeButton = new JButton("Decrease Font Size");
        deletePlayerButton = new JButton("Delete Player");
        sortingComboBox = new JComboBox<>(new String[]{"Sort by Name", "Sort by Graduation Year"});

        buttonPanel.add(increaseFontSizeButton);
        buttonPanel.add(decreaseFontSizeButton);
        buttonPanel.add(deletePlayerButton);
        buttonPanel.add(sortingComboBox);

        JScrollPane scrollPane = new JScrollPane(archivedPlayerTable);
        setLayout(new BorderLayout());
        add(buttonPanel, BorderLayout.SOUTH);
        add(scrollPane, BorderLayout.CENTER);

        archivedTableModel.addColumn("First Name");
        archivedTableModel.addColumn("Last Name");
        archivedTableModel.addColumn("Position");
        archivedTableModel.addColumn("Player Number");
        archivedTableModel.addColumn("Graduation Year");

        repopulateLists();

        /**
         * Action listener for the increase font size button. Increases the font size of the
         * archived player table.
         */
        increaseFontSizeButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                Font currentFont = archivedPlayerTable.getFont();
                Font newFont = currentFont.deriveFont(currentFont.getSize() + 5f);
                setFontSize(newFont);
            }
        });


        /**
         * Action listener for the decrease font size button. Decreases the font size of the
         * archived player table.
         */
        decreaseFontSizeButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                Font currentFont = archivedPlayerTable.getFont();
                Font newFont = currentFont.deriveFont(currentFont.getSize() - 5f);
                setFontSize(newFont);
            }
        });


        /**
         * Action listener for the delete player button. Deletes the selected player from
         * the archived players list and updates the table model.
         */
        deletePlayerButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                int selectedRowIndex = archivedPlayerTable.getSelectedRow();
                if (selectedRowIndex != -1) {
                    Player player = archivedPlayersList.get(selectedRowIndex);
                    archivedPlayersList.remove(player);
                    conn.removePlayerArchive(player.getFirstName(), player.getLastName());
                    updateTableModel();
                }
            }
        });

        /**
         * Action listener for the sorting combo box. Sorts the archived players list
         * based on the selected sorting option.
         */
        sortingComboBox.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                String selectedSorting = (String) sortingComboBox.getSelectedItem();
                if (selectedSorting != null) {
                    switch (selectedSorting) {
                        case "Sort by Name":
                            sortByName();
                            break;
                        case "Sort by Graduation Year":
                            sortByGraduationYear();
                            break;
                        default:
                            break;
                    }
                }
            }
        });
    }

    /**
     * Updates the table model with the current archived player data.
     */
    private void updateTableModel() {
        archivedTableModel.setRowCount(0);
        for (Player player : archivedPlayersList) {
            Object[] rowData = { player.getFirstName(), player.getLastName(), player.getPosition(),
                    player.getPlayerNumber(), player.getGraduationYear() };
            archivedTableModel.addRow(rowData);
        }
    }

    /**
     * Adds a player to the archived players list and updates the table model.
     * 
     * @param player The player to add to the list.
     */
    public void addToListModel(Player player) {
        archivedPlayersList.add(player);
        updateTableModel();
    }

    /**
     * Repopulates the archived players list from the database.
     */
    private void repopulateLists() {
        ArrayList<String[]> data = persistData.dataToArrayListArchivePlayers();
        for (String[] player : data) {
            Player newPlayer = new Player(player[0], player[1], player[3], Integer.parseInt(player[2]),
                    Integer.parseInt(player[4]));
            archivedPlayersList.add(newPlayer);
        }
        updateTableModel();
    }

    /**
     * Sets the font size for the archived player table.
     * 
     * @param font The font to set.
     */
    private void setFontSize(Font font) {
        archivedPlayerTable.setFont(font);
    }

    /**
     * Sorts the archived players list by name and updates the table model.
     */
    private void sortByName() {
        Collections.sort(archivedPlayersList, new Comparator<Player>() {
            @Override
            public int compare(Player p1, Player p2) {
                String name1 = p1.getLastName() + ", " + p1.getFirstName();
                String name2 = p2.getLastName() + ", " + p2.getFirstName();
                return name1.compareTo(name2);
            }
        });
        updateTableModel();
    }

    /**
     * Sorts the archived players list by graduation year and updates the table model.
     */
    private void sortByGraduationYear() {
        Collections.sort(archivedPlayersList, new Comparator<Player>() {
            @Override
            public int compare(Player p1, Player p2) {
                return Integer.compare(p1.getGraduationYear(), p2.getGraduationYear());
            }
        });
        updateTableModel();
    }
}
