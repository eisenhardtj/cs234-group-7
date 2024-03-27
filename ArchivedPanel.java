import java.awt.BorderLayout;
import java.util.ArrayList;

import javax.swing.DefaultListModel;
import javax.swing.JFrame;
import javax.swing.JList;
import javax.swing.JPanel;
import javax.swing.JScrollPane;

public class ArchivedPanel extends JPanel
{
    private DefaultListModel<Player> archivedListModel;
    private JList<Player> archivedPlayerList;
    private ArrayList<Player> archivedPlayersList;

    public ArchivedPanel()
    {
        archivedListModel = new DefaultListModel<>();
        archivedPlayerList = new JList<>(archivedListModel);
        archivedPlayersList = new ArrayList<>();

        JScrollPane scrollPane = new JScrollPane(archivedPlayerList);
        setLayout(new BorderLayout());
        add(scrollPane, BorderLayout.WEST);
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
}
