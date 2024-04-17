import java.awt.BorderLayout;
import java.awt.Font;
import java.awt.GridLayout;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.util.ArrayList;
import javax.swing.JButton;
import javax.swing.JComboBox;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.JScrollPane;
import javax.swing.JTable;
import javax.swing.JTextField;
import javax.swing.ListSelectionModel;
import javax.swing.event.ListSelectionEvent;
import javax.swing.event.ListSelectionListener;
import javax.swing.table.DefaultTableModel;
import javax.swing.table.TableColumnModel;

public class FreeThrowPanel extends JPanel
{
    private DefaultTableModel tableModel;
    private JTable freeThrowTable;
    private JComboBox<String> sortingComboBox;
    private JTextField firstNameField, lastNameField, dateField, attemptedField, madeField;
    private JButton addPointCheckButton, increaseFontSizeButton, decreaseFontSizeButton, editSessionButton;
    private Points selectedSession;
    private SQLConnection conn = new SQLConnection();
    private PersistData persistData = new PersistData();
    private ArrayList<Points> freeThrowList;
    private ChartPanel chartPanel;

    public FreeThrowPanel()
    {
        tableModel = new DefaultTableModel();
        tableModel.addColumn("First Name");
        tableModel.addColumn("Last Name");
        tableModel.addColumn("Date");
        tableModel.addColumn("Attempted");
        tableModel.addColumn("Made");

        freeThrowTable = new JTable(tableModel);
        freeThrowTable.setAutoResizeMode(JTable.AUTO_RESIZE_OFF); // Set to OFF to control column widths

        int[] columnWidths = {150, 150, 150, 150, 150};
        setColumnWidths(freeThrowTable.getColumnModel(), columnWidths);

        freeThrowTable.setSelectionMode(ListSelectionModel.SINGLE_SELECTION);

        firstNameField = new JTextField(15);
        lastNameField = new JTextField(15);
        dateField = new JTextField(15);
        attemptedField = new JTextField(15);
        madeField = new JTextField(15);

        sortingComboBox = new JComboBox<>(new String[]{"Sort by Last Name", "Sort by Points Made", "Sort by Date"});
        addPointCheckButton = new JButton("Add Free Throw Check");
        increaseFontSizeButton = new JButton("Increase Font Size");
        decreaseFontSizeButton = new JButton("Decrease Font Size");
        editSessionButton = new JButton("Edit Free Throw Session");

        JPanel inputPanel = new JPanel();
        inputPanel.setLayout(new GridLayout(10, 2));
        inputPanel.add(new JLabel("First Name:"));
        inputPanel.add(firstNameField);
        inputPanel.add(new JLabel("Last Name:"));
        inputPanel.add(lastNameField);
        inputPanel.add(new JLabel("Date (MM/DD/YYYY):"));
        inputPanel.add(dateField);
        inputPanel.add(new JLabel("Attempted:"));
        inputPanel.add(attemptedField);
        inputPanel.add(new JLabel("Made:"));
        inputPanel.add(madeField);
        inputPanel.add(new JLabel("Sort By:"));
        inputPanel.add(sortingComboBox);

        JPanel buttonPanel = new JPanel();
        buttonPanel.add(addPointCheckButton);
        buttonPanel.add(increaseFontSizeButton);
        buttonPanel.add(decreaseFontSizeButton);
        buttonPanel.add(editSessionButton);

        addPointCheckButton.addActionListener(new ActionListener()
        {
            @Override
            public void actionPerformed(ActionEvent e)
            {
                Points point = new Points(firstNameField.getText(), lastNameField.getText(), dateField.getText(), Integer.parseInt(attemptedField.getText()), Integer.parseInt(madeField.getText()));
                conn.addFreeThrow(firstNameField.getText(), lastNameField.getText(), dateField.getText(), Integer.parseInt(attemptedField.getText()), Integer.parseInt(madeField.getText()));
                addToListModel(point);
                if (chartPanel != null)
                {
                    chartPanel.updateCharts();
                }
            }
        });

        editSessionButton.addActionListener(new ActionListener()
        {
            @Override
            public void actionPerformed(ActionEvent e)
            {
                if (selectedSession != null)
                {
                    selectedSession.setFirstName(firstNameField.getText());
                    selectedSession.setLastName(lastNameField.getText());
                    selectedSession.setDate(dateField.getText());
                    selectedSession.setFreeThrowsAttempted(Integer.parseInt(attemptedField.getText()));
                    selectedSession.setFreeThrowsMade(Integer.parseInt(madeField.getText()));
                    updateTableModel();
                    conn.editSession(selectedSession.getFirstName(), selectedSession.getLastName(), firstNameField.getText(), lastNameField.getText(), dateField.getText(), Integer.parseInt(attemptedField.getText()), Integer.parseInt(madeField.getText()), "freethrows");
                }
                clearFields();
            }
        });

        increaseFontSizeButton.addActionListener(new ActionListener()
        {
            @Override
            public void actionPerformed(ActionEvent e)
            {
                increaseFontSize();
            }
        });

        decreaseFontSizeButton.addActionListener(new ActionListener()
        {
            @Override
            public void actionPerformed(ActionEvent e)
            {
                decreaseFontSize();
            }
        });

        freeThrowTable.getSelectionModel().addListSelectionListener(new ListSelectionListener()
        {
            @Override
            public void valueChanged(ListSelectionEvent e)
            {
                int selectedRowIndex = freeThrowTable.getSelectedRow();
                if (selectedRowIndex != -1)
                {
                    selectedSession = freeThrowList.get(selectedRowIndex);
                    displaySelectedSession(selectedSession);
                }
            }
        });

        setLayout(new BorderLayout());
        add(inputPanel, BorderLayout.WEST);
        add(new JScrollPane(freeThrowTable), BorderLayout.CENTER);
        add(buttonPanel, BorderLayout.SOUTH);
        repopulateTable();
    }

    private void addToListModel(Points point)
    {
        freeThrowList.add(point);
        updateTableModel();
    }

    private void updateTableModel()
    {
        // Clear existing table data
        tableModel.setRowCount(0);
        // Add updated data to table model
        for (Points point : freeThrowList)
        {
            Object[] rowData = {point.getFirstName(), point.getLastName(), point.getDate(), point.getFreeThrowsAttempted(), point.getFreeThrowsMade()};
            tableModel.addRow(rowData);
        }
    }

    private void repopulateTable()
    {
        ArrayList<String[]> data = persistData.dataToArrayListFreeThrows();
        freeThrowList = new ArrayList<>();
        for (String[] pointData : data)
        {
            Points point = new Points(pointData[0], pointData[1], pointData[2], Integer.parseInt(pointData[3]), Integer.parseInt(pointData[4]));
            freeThrowList.add(point);
        }
        updateTableModel();
    }

    private void displaySelectedSession(Points point)
    {
        firstNameField.setText(point.getFirstName());
        lastNameField.setText(point.getLastName());
        dateField.setText(point.getDate());
        attemptedField.setText(String.valueOf(point.getFreeThrowsAttempted()));
        madeField.setText(String.valueOf(point.getFreeThrowsMade()));
    }

    private void clearFields()
    {
        firstNameField.setText("");
        lastNameField.setText("");
        dateField.setText("");
        attemptedField.setText("");
        madeField.setText("");
    }

    private void setColumnWidths(TableColumnModel columnModel, int[] widths)
    {
        for (int i = 0; i < widths.length; i++)
        {
            columnModel.getColumn(i).setPreferredWidth(widths[i]);
        }
    }

    private void increaseFontSize()
    {
        Font currentFont = freeThrowTable.getFont();
        Font newFont = currentFont.deriveFont(currentFont.getSize() + 5f);
        setFontSize(newFont);
    }

    private void decreaseFontSize()
    {
        Font currentFont = freeThrowTable.getFont();
        Font newFont = currentFont.deriveFont(currentFont.getSize() - 5f);
        setFontSize(newFont);
    }

    private void setFontSize(Font font)
    {
        freeThrowTable.setFont(font);
        firstNameField.setFont(font);
        lastNameField.setFont(font);
        dateField.setFont(font);
        attemptedField.setFont(font);
        madeField.setFont(font);
    }

    public void setChartPanel(ChartPanel chartPanel)
    {
        this.chartPanel = chartPanel;
    }
}
