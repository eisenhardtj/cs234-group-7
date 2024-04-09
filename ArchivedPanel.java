import java.awt.BorderLayout;
import java.awt.Font;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.util.ArrayList;

import javax.swing.DefaultListModel;
import javax.swing.JButton;
import javax.swing.JList;
import javax.swing.JPanel;
import javax.swing.JScrollPane;


/**
 * This class creates a panel that displays a list of archived players. The
 * User can view the list in various ways using the increase and decrease
 * font size buttons. The user can edit a archived player's information.
 * 
 * Author: Cole Aydelotte
 */
public class ArchivedPanel extends JPanel
{
    private DefaultListModel<Player> archivedListModel;
    private JList<Player> archivedPlayerList;
    private ArrayList<Player> archivedPlayersList;
    private JPanel buttonPanel;
    private JButton increaseFontSizeButton, decreaseFontSizeButton, deletePlayerButton;
    private SQLConnection conn = new SQLConnection();

    public ArchivedPanel()
    {
        archivedListModel = new DefaultListModel<>();
        archivedPlayerList = new JList<>(archivedListModel);
        archivedPlayersList = new ArrayList<>();
        buttonPanel = new JPanel();

        increaseFontSizeButton = new JButton("Increase Font Size");
        decreaseFontSizeButton = new JButton("Decrease Font Size");
        deletePlayerButton = new JButton("Delete Player");

        buttonPanel.add(increaseFontSizeButton);
        buttonPanel.add(decreaseFontSizeButton);
        buttonPanel.add(deletePlayerButton);

        JScrollPane scrollPane = new JScrollPane(archivedPlayerList);
        setLayout(new BorderLayout());
        add(buttonPanel, BorderLayout.SOUTH);
        add(scrollPane, BorderLayout.CENTER);

        repopulateLists();

        increaseFontSizeButton.addActionListener(new ActionListener() 
        { 
            @Override
            public void actionPerformed(ActionEvent e) {
                Font currentFont = archivedPlayerList.getFont();
                Font newFont = currentFont.deriveFont(currentFont.getSize() + 5f);
                setFontSize(newFont);
            }
        });

        decreaseFontSizeButton.addActionListener(new ActionListener() 
        { 
            @Override
            public void actionPerformed(ActionEvent e) {
                Font currentFont = archivedPlayerList.getFont();
                Font newFont = currentFont.deriveFont(currentFont.getSize() - 5f);
                setFontSize(newFont);
            }
        });

        deletePlayerButton.addActionListener(new ActionListener() 
        { 
            @Override
            public void actionPerformed(ActionEvent e) {
                Player player = archivedPlayerList.getSelectedValue();
                archivedPlayersList.remove(player);
                System.out.println(player);
                conn.removePlayer(player.getFirstName(), player.getLastName());
                updateListModel();
            }
        });
    }

    private void updateListModel()
    {
        archivedListModel.clear();
        for (Player player : archivedPlayersList) 
        {
            archivedListModel.addElement(player);
        }
    }

    public void addToListModel(Player player)
    {
        archivedPlayersList.add(player);
        updateListModel();
    }

    private void repopulateLists()
    {
        ArrayList<String[]> data;
        data = conn.dataToArrayListArchivePlayers();
        for(int x = 0; x < data.size(); x++)
        {
            String[] player = data.get(x);
            Player newPlayer = new Player(player[0], player[1], player[3], Integer.parseInt(player[2]), Integer.parseInt(player[4]));
            archivedPlayersList.add(newPlayer);
            archivedListModel.addElement(newPlayer);
        }
    }

    private void setFontSize(Font font) 
    {
        archivedPlayerList.setFont(font);
    }
}
