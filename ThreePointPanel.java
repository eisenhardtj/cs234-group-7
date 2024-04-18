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
import javax.swing.table.DefaultTableModel;
import javax.swing.table.TableColumnModel;
import java.text.DecimalFormat;

/**
 * This class represents a panel for displaying three point shot information. Has
 * functionality for adding a new three point session, increasing/decreasing font size,
 * and editing a session. Contains a table for displaying three point shot information,
 * text fields for inputting new data, buttons for adding a new session, increasing/decreasing
 * font size, and editing a session. Contains action listeners for each button, table, or combo box.
 * 
 * Author: Cole Aydelotte
 */
public class ThreePointPanel extends JPanel
{
    private DefaultTableModel tableModel;
    private JTable threePointTable;
    private JComboBox<String> sortingComboBox;
    private JTextField firstNameField, lastNameField, dateField, attemptedField, madeField, locaField;
    private JButton addPointCheckButton, increaseFontSizeButton, decreaseFontSizeButton, editSessionButton;
    private ThreePoint selectedSession;
    private SQLConnection conn = new SQLConnection();
    private PersistData persistData = new PersistData();
    private ArrayList<ThreePoint> threePointShotsList;
    private ChartPanel chartPanel;

    /**
     * Constructs a new ThreePointPanel with necessary components and functionality.
     * Contains a table for displaying three point shot information, text fields for
     * inputting new data, buttons for adding a new session, increasing/decreasing font size,
     * and editing a session. Contains action listeners for each button, table, or combo box.
     * @param chartPanel
     */
    public ThreePointPanel(ChartPanel chartPanel)
    {
        this.chartPanel = chartPanel;
        tableModel = new DefaultTableModel();
        tableModel.addColumn("First Name");
        tableModel.addColumn("Last Name");
        tableModel.addColumn("Date");
        tableModel.addColumn("Attempted");
        tableModel.addColumn("Made");
        tableModel.addColumn("Shot %");
        tableModel.addColumn("Location");

        threePointTable = new JTable(tableModel);
        threePointTable.setAutoResizeMode(JTable.AUTO_RESIZE_OFF);
        int[] columnWidths = {150, 150, 150, 150, 150, 150, 150};
        setColumnWidths(threePointTable.getColumnModel(), columnWidths);

        firstNameField = new JTextField(15);
        lastNameField = new JTextField(15);
        dateField = new JTextField(15);
        attemptedField = new JTextField(15);
        madeField = new JTextField(15);
        locaField = new JTextField(15);

        sortingComboBox = new JComboBox<>(new String[]{"Sort by Last Name", "Sort by Points Made", "Sort by Date", "Sort by Shot %", "Sort by Location"});
        addPointCheckButton = new JButton("Add Three Point Check");
        increaseFontSizeButton = new JButton("Increase Font Size");
        decreaseFontSizeButton = new JButton("Decrease Font Size");
        editSessionButton = new JButton("Edit Three Point Session");

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
        inputPanel.add(new JLabel("Location:"));
        inputPanel.add(locaField);
        inputPanel.add(new JLabel("Sort By:"));
        inputPanel.add(sortingComboBox);

        JPanel buttonPanel = new JPanel();
        buttonPanel.add(addPointCheckButton);
        buttonPanel.add(increaseFontSizeButton);
        buttonPanel.add(decreaseFontSizeButton);
        buttonPanel.add(editSessionButton);

        /**
         * List selection listener for the three point shots table. Sets the selected session
         * to the session at the selected row and sets the text fields to the values of the
         * selected session.
         */
        threePointTable.getSelectionModel().addListSelectionListener(e ->
        {
            int selectedRow = threePointTable.getSelectedRow();
            if (selectedRow != -1) {
                selectedSession = threePointShotsList.get(selectedRow);
                setFieldsFromSelectedSession();
            }
        });

        /**
         * Action listener for the add point check button. Adds a new ThreePoint object to the
         * list model and updates the charts.
         */
        addPointCheckButton.addActionListener(new ActionListener()
        {
            @Override
            public void actionPerformed(ActionEvent e)
            {
                ThreePoint point = new ThreePoint(firstNameField.getText(), lastNameField.getText(), dateField.getText(), Integer.parseInt(attemptedField.getText()), Integer.parseInt(madeField.getText()), locaField.getText());
                conn.addThreePoint(firstNameField.getText(), lastNameField.getText(), dateField.getText(), Integer.parseInt(attemptedField.getText()), Integer.parseInt(madeField.getText()), locaField.getText());
                addToListModel(point);
                updateCharts();
            }
        });

        /**
         * Action listener for the edit session button. Edits the selected session with the
         * values in the text fields.
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
                    selectedSession.setThreePointsAttempted(Integer.parseInt(attemptedField.getText()));
                    selectedSession.setThreePointsMade(Integer.parseInt(madeField.getText()));
                    updateTableModel();
                    conn.editSession(selectedSession.getFirstName(), selectedSession.getLastName(), firstNameField.getText(), lastNameField.getText(), dateField.getText(), Integer.parseInt(attemptedField.getText()), Integer.parseInt(madeField.getText()), "threepointshots");
                }
                clearFields();
            }
        });

        /**
         * Action listener for the sorting combo box. Sorts the three point shots list
         * based on the selected sorting option.
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
         * Action listener for the sorting combo box. Sorts the three point shots list
         * based on the selected sorting option.
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
         * Action listener for the sorting combo box. Sorts the three point shots list
         * based on the selected sorting option.
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
                        case "Sort by Shot %":
                            sortByShotPercentageDescending();
                            break;
                        case "Sort by Location":
                            sortByLocation();
                            break;
                        default:
                            break;
                    }
                }
            }
        });

        setLayout(new BorderLayout());
        add(inputPanel, BorderLayout.WEST);
        add(new JScrollPane(threePointTable), BorderLayout.CENTER);
        add(buttonPanel, BorderLayout.SOUTH);
        repopulateTable();
    }

    /**
     * Sets the text fields to the values of the selected session.
     */
    private void setFieldsFromSelectedSession()
    {
        if (selectedSession != null)
        {
            firstNameField.setText(selectedSession.getFirstName());
            lastNameField.setText(selectedSession.getLastName());
            dateField.setText(selectedSession.getDate());
            attemptedField.setText(String.valueOf(selectedSession.getThreePointsAttempted()));
            madeField.setText(String.valueOf(selectedSession.getThreePointsMade()));
            locaField.setText(selectedSession.getLocation());
        }
    }

    /**
     * Adds a ThreePoint object to the list model and updates the table model.
     * @param pointSession
     */
    private void addToListModel(ThreePoint pointSession)
    {
        threePointShotsList.add(pointSession);
        updateTableModel();
    }

    /**
     * Updates the table model with the current three point data.
     */
    private void updateTableModel()
    {
        tableModel.setRowCount(0);
        DecimalFormat df = new DecimalFormat("#.##");
        for (ThreePoint point : threePointShotsList)
        {
            double shotPercentage = calculateShotPercentage(point.getThreePointsAttempted(), point.getThreePointsMade());
            Object[] rowData = {point.getFirstName(), point.getLastName(), point.getDate(), point.getThreePointsAttempted(), point.getThreePointsMade(), df.format(shotPercentage), point.getLocation()};
            tableModel.addRow(rowData);
        }
    }

    /**
     * Calculates the shot percentage based on the number of attempted and made shots.
     * @param attempted
     * @param made
     * @return
     */
    private double calculateShotPercentage(int attempted, int made)
    {
        return (attempted == 0) ? 0.0 : ((double) made / attempted) * 100;
    }

    /**
     * Repopulates the table with data from the database.
     */
    private void repopulateTable()
    {
        ArrayList<String[]> data = persistData.dataToArrayListThreePoints();
        threePointShotsList = convertToThreePointList(data);
        updateTableModel();
    }

    /**
     * Converts an ArrayList of String arrays to an ArrayList of ThreePoint objects.
     * @param data
     * @return
     */
    private ArrayList<ThreePoint> convertToThreePointList(ArrayList<String[]> data)
    {
        ArrayList<ThreePoint> threePoints = new ArrayList<>();
        for (String[] rowData : data)
        {
            ThreePoint point = new ThreePoint(rowData[0], rowData[1], rowData[2], Integer.parseInt(rowData[3]), Integer.parseInt(rowData[4]), rowData[5]);
            threePoints.add(point);
        }
        return threePoints;
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
        locaField.setText("");
    }

    /**
     * Sets the column widths for the table.
     * @param columnModel
     * @param widths
     */
    private void setColumnWidths(TableColumnModel columnModel, int[] widths)
    {
        for (int i = 0; i < widths.length; i++)
        {
            columnModel.getColumn(i).setPreferredWidth(widths[i]);
        }
    }

    /**
     * Increases the font size of the table and text fields.
     */
    private void increaseFontSize()
    {
        Font currentFont = threePointTable.getFont();
        Font newFont = currentFont.deriveFont(currentFont.getSize() + 5f);
        setFontSize(newFont);
    }

    /**
     * Decreases the font size of the table and text fields.
     */
    private void decreaseFontSize()
    {
        Font currentFont = threePointTable.getFont();
        Font newFont = currentFont.deriveFont(currentFont.getSize() - 5f);
        setFontSize(newFont);
    }

    /**
     * Sets the font size for the table and text fields.
     * @param font The font to set.
     */
    private void setFontSize(Font font)
    {
        threePointTable.setFont(font);
        firstNameField.setFont(font);
        lastNameField.setFont(font);
        dateField.setFont(font);
        attemptedField.setFont(font);
        madeField.setFont(font);
        locaField.setFont(font);
    }

    /**
     * Updates the charts in the chart panel.
     */
    private void updateCharts()
    {
        if (chartPanel != null)
        {
            chartPanel.updateCharts();
        }
    }

    /**
     * Sorts the three point shots list by last name and updates the table model.
     */
    private void sortByLastName()
    {
        Collections.sort(threePointShotsList, new Comparator<ThreePoint>()
        {
            @Override
            public int compare(ThreePoint p1, ThreePoint p2)
            {
                return p1.getLastName().compareTo(p2.getLastName());
            }
        });
        updateTableModel();
    }

    /**
     * Sorts the three point shots list by points made and updates the table model.
     */
    private void sortByPointsMadeDescending()
    {
        Collections.sort(threePointShotsList, new Comparator<ThreePoint>()
        {
            @Override
            public int compare(ThreePoint p1, ThreePoint p2)
            {
                return Integer.compare(p2.getThreePointsMade(), p1.getThreePointsMade());
            }
        });
        updateTableModel();
    }

    /**
     * Sorts the three point shots list by date and updates the table model.
     */
    private void sortByDate()
    {
        Collections.sort(threePointShotsList, new Comparator<ThreePoint>()
        {
            @Override
            public int compare(ThreePoint p1, ThreePoint p2)
            {
                return p2.getDate().compareTo(p1.getDate());
            }
        });
        updateTableModel();
    }

    /**
     * Sorts the three point shots list by shot percentage and updates the table model.
     */
    private void sortByShotPercentageDescending()
    {
        Collections.sort(threePointShotsList, new Comparator<ThreePoint>()
        {
            @Override
            public int compare(ThreePoint p1, ThreePoint p2)
            {
                double p1Percentage = calculateShotPercentage(p1.getThreePointsAttempted(), p1.getThreePointsMade());
                double p2Percentage = calculateShotPercentage(p2.getThreePointsAttempted(), p2.getThreePointsMade());
                return Double.compare(p2Percentage, p1Percentage);
            }
        });
        updateTableModel();
    }

    /**
     * Sorts the three point shots list by location and updates the table model.
     */
    private void sortByLocation()
    {
        Collections.sort(threePointShotsList, new Comparator<ThreePoint>()
        {
            @Override
            public int compare(ThreePoint p1, ThreePoint p2)
            {
                return p1.getLocation().compareTo(p2.getLocation());
            }
        });
        updateTableModel();
    }
}
