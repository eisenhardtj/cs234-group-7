import java.awt.BorderLayout;
import java.awt.Font;
import java.awt.GridLayout;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;

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

/**
 * This class represents a panel for displaying free throw information. Has
 * functionality for adding free throw checks, increasing/decreasing font size,
 * editing free throw sessions, and sorting free throw sessions by last name,
 * points made, date, or success rate.
 * 
 * Author: Cole Aydelotte
 */
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

    /**
     * Constructs a new FreeThrowPanel with necessary components and functionality.
     */
    public FreeThrowPanel()
    {
        tableModel = new DefaultTableModel();
        tableModel.addColumn("First Name");
        tableModel.addColumn("Last Name");
        tableModel.addColumn("Date");
        tableModel.addColumn("Attempted");
        tableModel.addColumn("Made");
        tableModel.addColumn("Success Rate");

        freeThrowTable = new JTable(tableModel);
        freeThrowTable.setAutoResizeMode(JTable.AUTO_RESIZE_OFF);

        int[] columnWidths = {150, 150, 150, 150, 150, 150};
        setColumnWidths(freeThrowTable.getColumnModel(), columnWidths);

        freeThrowTable.setSelectionMode(ListSelectionModel.SINGLE_SELECTION);

        firstNameField = new JTextField(15);
        lastNameField = new JTextField(15);
        dateField = new JTextField(15);
        attemptedField = new JTextField(15);
        madeField = new JTextField(15);

        sortingComboBox = new JComboBox<>(new String[]{"Sort by Last Name", "Sort by Points Made", "Sort by Date", "Sort by Success Rate"});
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

        /**
         * Action listener for the addPointCheckButton. Adds a new free throw check to the
         * table and database. Updates the table model and chart panel if it exists.
         */
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

        /**
         * Action listener for the editSessionButton. Edits the selected free throw session
         * with the information in the text fields. Updates the table model and database.
         */
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

        /**
         * Action listener for the increaseFontSizeButton. Increases the font size of the
         * table and text fields by 5.
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
         * Action listener for the decreaseFontSizeButton. Decreases the font size of the
         * table and text fields by 5.
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
         * List selection listener for the freeThrowTable. Displays the selected free throw
         * session in the text fields.
         */
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

        /**
         * Action listener for the sortingComboBox. Sorts the free throw sessions based on
         * the selected sorting option.
         */
        sortingComboBox.addActionListener(new ActionListener()
        {
            @Override
            public void actionPerformed(ActionEvent e)
            {
                String selectedSorting = (String) sortingComboBox.getSelectedItem();
                if (selectedSorting != null)
                {
                    switch (selectedSorting)
                    {
                        case "Sort by Last Name":
                            sortByLastName();
                            break;
                        case "Sort by Points Made":
                            sortByPointsMadeDescending();
                            break;
                        case "Sort by Date":
                            sortByDate();
                            break;
                        case "Sort by Success Rate":
                            sortBySuccessRateDescending();
                            break;
                        default:
                            break;
                    }
                }
            }
        });

        setLayout(new BorderLayout());
        add(inputPanel, BorderLayout.WEST);
        add(new JScrollPane(freeThrowTable), BorderLayout.CENTER);
        add(buttonPanel, BorderLayout.SOUTH);
        repopulateTable();
        updateTableModel();
    }

    /**
     * Adds a free throw session to the list model and updates the table model.
     * @param point The free throw session to add.
     */
    private void addToListModel(Points point)
    {
        freeThrowList.add(point);
        updateTableModel();
    }

    /**
     * Updates the table model with the current free throw session data.
     * Called when the table needs to be repopulated.
     */
    private void updateTableModel()
    {
        tableModel.setRowCount(0);
        for (Points point : freeThrowList)
        {
            double average = (double) point.getFreeThrowsMade() / point.getFreeThrowsAttempted();
            average = Math.round(average * 100.0) / 100.0;
            Object[] rowData = {point.getFirstName(), point.getLastName(), point.getDate(), point.getFreeThrowsAttempted(), point.getFreeThrowsMade(), average * 100 + "%"};
            tableModel.addRow(rowData);
        }
    }

    /**
     * Repopulates the free throw session list from the database.
     */
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

    /**
     * Displays the selected free throw session in the text fields.
     * @param point The free throw session to display.
     */
    private void displaySelectedSession(Points point)
    {
        firstNameField.setText(point.getFirstName());
        lastNameField.setText(point.getLastName());
        dateField.setText(point.getDate());
        attemptedField.setText(String.valueOf(point.getFreeThrowsAttempted()));
        madeField.setText(String.valueOf(point.getFreeThrowsMade()));
    }

    /**
     * Clears the text fields.
     */
    private void clearFields()
    {
        firstNameField.setText("");
        lastNameField.setText("");
        dateField.setText("");
        attemptedField.setText("");
        madeField.setText("");
    }

    /**
     * Sets the column widths of the table.
     * @param columnModel The column model of the table.
     * @param widths The widths of the columns.
     */
    private void setColumnWidths(TableColumnModel columnModel, int[] widths)
    {
        for (int i = 0; i < widths.length; i++)
        {
            columnModel.getColumn(i).setPreferredWidth(widths[i]);
        }
    }

    /**
     * Increases the font size of the table and text fields by 5.
     * Called when the increaseFontSizeButton is clicked.
     */
    private void increaseFontSize()
    {
        Font currentFont = freeThrowTable.getFont();
        Font newFont = currentFont.deriveFont(currentFont.getSize() + 5f);
        setFontSize(newFont);
    }

    /**
     * Decreases the font size of the table and text fields by 5.
     * Called when the decreaseFontSizeButton is clicked.
     */
    private void decreaseFontSize()
    {
        Font currentFont = freeThrowTable.getFont();
        Font newFont = currentFont.deriveFont(currentFont.getSize() - 5f);
        setFontSize(newFont);
    }

    /**
     * Sets the font size of the table and text fields.
     * @param font The font to set.
     */
    private void setFontSize(Font font)
    {
        freeThrowTable.setFont(font);
        firstNameField.setFont(font);
        lastNameField.setFont(font);
        dateField.setFont(font);
        attemptedField.setFont(font);
        madeField.setFont(font);
    }

    /**
     * Sets the chart panel for the free throw panel.
     * @param chartPanel The chart panel to set.
     */
    public void setChartPanel(ChartPanel chartPanel)
    {
        this.chartPanel = chartPanel;
    }

    /**
     * Sorts the free throw sessions by last name.
     */
    private void sortByLastName()
    {
        Collections.sort(freeThrowList, new Comparator<Points>()
        {
            @Override
            public int compare(Points p1, Points p2)
            {
                return p1.getLastName().compareTo(p2.getLastName());
            }
        });
    updateTableModel();
    }

    /**
     * Sorts the free throw sessions by points made in descending order.
     * Called when the "Sort by Points Made" option is selected.
     */
    private void sortByPointsMadeDescending()
    {
        Collections.sort(freeThrowList, new Comparator<Points>()
        {
            @Override
            public int compare(Points p1, Points p2)
            {
                return Integer.compare(p2.getFreeThrowsMade(), p1.getFreeThrowsMade());
            }
        });
        updateTableModel();
    }
    
    /**
     * Sorts the free throw sessions by date.
     * Called when the "Sort by Date" option is selected.
     */
    private void sortByDate()
    {
        Collections.sort(freeThrowList, new Comparator<Points>()
        {
            @Override
            public int compare(Points p1, Points p2)
            {
                return p2.getDate().compareTo(p1.getDate());
            }
        });
        updateTableModel();
    }

    /**
     * Sorts the free throw sessions by success rate in descending order.
     * Called when the "Sort by Success Rate" option is selected.
     */
    private void sortBySuccessRateDescending()
    {
        Collections.sort(freeThrowList, new Comparator<Points>()
        {
            @Override
            public int compare(Points p1, Points p2)
            {
                double successRate1 = (double) p1.getFreeThrowsMade() / p1.getFreeThrowsAttempted();
                double successRate2 = (double) p2.getFreeThrowsMade() / p2.getFreeThrowsAttempted();
                return Double.compare(successRate2, successRate1);
            }
        });
        updateTableModel();
    }    
}
