import java.awt.BorderLayout;
import java.awt.Font;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.util.ArrayList;
import javax.swing.JButton;
import javax.swing.JPanel;
import javax.swing.JScrollPane;
import javax.swing.JTable;
import javax.swing.table.DefaultTableModel;

public class ArchivedPanel extends JPanel
{
    private DefaultTableModel archivedTableModel;
    private JTable archivedPlayerTable;
    private ArrayList<Player> archivedPlayersList;
    private JPanel buttonPanel;
    private JButton increaseFontSizeButton, decreaseFontSizeButton, deletePlayerButton;
    private SQLConnection conn = new SQLConnection();
    private PersistData persistData = new PersistData();

    public ArchivedPanel()
    {
        archivedTableModel = new DefaultTableModel();
        archivedPlayerTable = new JTable(archivedTableModel);
        archivedPlayersList = new ArrayList<>();
        buttonPanel = new JPanel();
        increaseFontSizeButton = new JButton("Increase Font Size");
        decreaseFontSizeButton = new JButton("Decrease Font Size");
        deletePlayerButton = new JButton("Delete Player");

        buttonPanel.add(increaseFontSizeButton);
        buttonPanel.add(decreaseFontSizeButton);
        buttonPanel.add(deletePlayerButton);

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

        increaseFontSizeButton.addActionListener(new ActionListener()
        {
            @Override
            public void actionPerformed(ActionEvent e) {
                Font currentFont = archivedPlayerTable.getFont();
                Font newFont = currentFont.deriveFont(currentFont.getSize() + 5f);
                setFontSize(newFont);
            }
        });

        decreaseFontSizeButton.addActionListener(new ActionListener()
        {
            @Override
            public void actionPerformed(ActionEvent e)
            {
                Font currentFont = archivedPlayerTable.getFont();
                Font newFont = currentFont.deriveFont(currentFont.getSize() - 5f);
                setFontSize(newFont);
            }
        });

        deletePlayerButton.addActionListener(new ActionListener()
        {
            @Override
            public void actionPerformed(ActionEvent e)
            {
                int selectedRowIndex = archivedPlayerTable.getSelectedRow();
                if (selectedRowIndex != -1)
                {
                    Player player = archivedPlayersList.get(selectedRowIndex);
                    archivedPlayersList.remove(player);
                    conn.removePlayerArchive(player.getFirstName(), player.getLastName());
                    updateTableModel();
                }
            }
        });
    }

    private void updateTableModel()
    {
        archivedTableModel.setRowCount(0);
        for (Player player : archivedPlayersList) {
            Object[] rowData = { player.getFirstName(), player.getLastName(), player.getPosition(),
                    player.getPlayerNumber(), player.getGraduationYear() };
            archivedTableModel.addRow(rowData);
        }
    }

    public void addToListModel(Player player)
    {
        archivedPlayersList.add(player);
        updateTableModel();
    }

    private void repopulateLists()
    {
        ArrayList<String[]> data;
        data = persistData.dataToArrayListArchivePlayers();
        for (String[] player : data)
        {
            Player newPlayer = new Player(player[0], player[1], player[3], Integer.parseInt(player[2]),
                    Integer.parseInt(player[4]));
            archivedPlayersList.add(newPlayer);
        }
        updateTableModel();
    }

    private void setFontSize(Font font)
    {
        archivedPlayerTable.setFont(font);
    }
}
