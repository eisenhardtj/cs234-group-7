import java.awt.BorderLayout;
import java.awt.FlowLayout;
import java.awt.Font;
import java.awt.GridBagConstraints;
import java.awt.GridBagLayout;
import java.awt.GridLayout;
import java.awt.Insets;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.util.ArrayList;

import javax.swing.DefaultListModel;
import javax.swing.JButton;
import javax.swing.JComboBox;
import javax.swing.JLabel;
import javax.swing.JList;
import javax.swing.JPanel;
import javax.swing.JScrollPane;
import javax.swing.JTable;
import javax.swing.JTextField;
import javax.swing.table.DefaultTableModel;
import javax.swing.table.TableColumnModel;


/**
 * This class creates a panel that allows the user to input data for a three point session. 
 * The user can input the first name, last name, date, number of three points attempted, 
 * and number of free throws made. The user can also sort the list of three point sessions 
 * by last name, points made, or date. The user can add a three point session to the list, 
 * increase the font size of the list, decrease the font size of the list, and edit a three 
 * point session.
 * 
 * Author: Cole Aydelotte
 */
public class ThreePointPanel extends JPanel
{
    private JPanel buttonPanel;
    private DefaultListModel<Points> threePointListModel;
    private JList<Points> threePointList;
    private ArrayList<Points> threePointShotsList;
    private JComboBox<String> sortingComboBox;
    private JButton addPointCheckButton, increaseFontSizeButton, decreaseFontSizeButton, editSessionButton;
    private JTextField firstNameField, lastNameField, dateField, attemptedField, madeField, locaField;
    private JPanel inputPanel;
    private Points selectedSession;
    private SQLConnection conn = new SQLConnection();
    private PersistData persistData = new PersistData();
    private DefaultTableModel tableModel;
    private JTable threePointTable;



    public ThreePointPanel()
    {
        tableModel = new DefaultTableModel();
        tableModel.addColumn("First Name");
        tableModel.addColumn("Last Name");
        tableModel.addColumn("Date");
        tableModel.addColumn("Attempted");
        tableModel.addColumn("Location");
        tableModel.addColumn("Made");

        threePointTable = new JTable(tableModel);
        threePointTable.setAutoResizeMode(JTable.AUTO_RESIZE_OFF); // Set to OFF to control column widths

        // Set preferred column widths
        int[] columnWidths = {325, 325, 325, 325, 325}; // Adjust as needed
        setColumnWidths(threePointTable.getColumnModel(), columnWidths);

        buttonPanel = new JPanel();
        threePointListModel = new DefaultListModel<>();
        threePointList = new JList<>(threePointListModel);
        threePointShotsList = new ArrayList<>();

        inputPanel = new JPanel();

        inputPanel.setLayout(new GridLayout(10, 2));

        sortingComboBox = new JComboBox<>(new String[]{"Sort by Last Name", "Sort by Points Made", "Sort by Date"});
        firstNameField = new JTextField(15);
        lastNameField = new JTextField(15);
        dateField = new JTextField(15);
        attemptedField = new JTextField(15);
        madeField = new JTextField(15);
        locaField = new JTextField(15);

        inputPanel.add(new JLabel("First Name:"));
        inputPanel.add(firstNameField);
        inputPanel.add(new JLabel("Last Name:"));
        inputPanel.add(lastNameField);
        inputPanel.add(new JLabel("Date:"));
        inputPanel.add(dateField);
        inputPanel.add(new JLabel("Attempted:"));
        inputPanel.add(attemptedField);
        inputPanel.add(new JLabel("Made:"));
        inputPanel.add(madeField);
        inputPanel.add(new JLabel("Location:"));
        inputPanel.add(locaField);
        inputPanel.add(new JLabel("Sort By:"));
        inputPanel.add(sortingComboBox);

        addPointCheckButton = new JButton("Add Three Point Check");
        increaseFontSizeButton = new JButton("Increase Font Size");
        decreaseFontSizeButton = new JButton("Decrease Font Size");
        editSessionButton = new JButton("Edit Three Point Session");

        repopulateLists();

        buttonPanel.add(addPointCheckButton);
        buttonPanel.add(increaseFontSizeButton);
        buttonPanel.add(decreaseFontSizeButton);
        buttonPanel.add(editSessionButton);
        new FlowLayout(FlowLayout.CENTER);

        addPointCheckButton.addActionListener(new ActionListener()
        {
            @Override
            public void actionPerformed(ActionEvent e)
            {
            Points point = new Points(firstNameField.getText(), lastNameField.getText(), dateField.getText(), Integer.parseInt(attemptedField.getText()), locaField.getText(), Integer.parseInt(madeField.getText()));
            conn.addThreePoint(firstNameField.getText(), lastNameField.getText(), dateField.getText(), Integer.parseInt(attemptedField.getText()), locaField.getText(), Integer.parseInt(madeField.getText()));
            threePointShotsList.add(point);
            updateListModel();
            }
        });

        threePointList.addListSelectionListener(e -> 
        {
            selectedSession = threePointList.getSelectedValue();
            if (selectedSession != null) {
                firstNameField.setText(selectedSession.getFirstName());
                lastNameField.setText(selectedSession.getLastName());
                dateField.setText(selectedSession.getDate());
                attemptedField.setText(String.valueOf(selectedSession.getFreeThrowsAttempted()));
                locaField.setText(selectedSession.getLocation());
                madeField.setText(String.valueOf(selectedSession.getFreeThrowsMade()));
            }
        });

        threePointTable.getSelectionModel().addListSelectionListener(e -> {
            int selectedRowIndex = threePointTable.getSelectedRow();
            if (selectedRowIndex != -1) {
                selectedSession = new Points((String) tableModel.getValueAt(selectedRowIndex, 0),
                        (String) tableModel.getValueAt(selectedRowIndex, 1),
                        (String) tableModel.getValueAt(selectedRowIndex, 2),
                        (int) tableModel.getValueAt(selectedRowIndex, 3),
                        (int) tableModel.getValueAt(selectedRowIndex, 4));
                firstNameField.setText(selectedSession.getFirstName());
                lastNameField.setText(selectedSession.getLastName());
                dateField.setText(selectedSession.getDate());
                attemptedField.setText(String.valueOf(selectedSession.getFreeThrowsAttempted()));
                locaField.setText(selectedSession.getLocation());
                madeField.setText(String.valueOf(selectedSession.getFreeThrowsMade()));
            }
        });

        editSessionButton.addActionListener(new ActionListener()
        {
            @Override
            public void actionPerformed(ActionEvent e)
            {
                if (selectedSession != null) {
                    selectedSession.setFirstName(firstNameField.getText());
                    selectedSession.setLastName(lastNameField.getText());
                    selectedSession.setDate(dateField.getText());
                    selectedSession.setFreeThrowsAttempted(Integer.parseInt(attemptedField.getText()));
                    selectedSession.setlocaField(locaField.getText());
                    selectedSession.setFreeThrowsMade(Integer.parseInt(madeField.getText()));
                    threePointListModel.set(threePointList.getSelectedIndex(), selectedSession);
                    conn.editSession(selectedSession.getFirstName(), selectedSession.getLastName(), firstNameField.getText(), lastNameField.getText(), dateField.getText(), Integer.parseInt(attemptedField.getText()), Integer.parseInt(madeField.getText()), "threepointshots");
                }
                clearFields();
            }
        });

        increaseFontSizeButton.addActionListener(new ActionListener() 
        { 
            @Override
            public void actionPerformed(ActionEvent e) {
                Font currentFont = firstNameField.getFont();
                Font newFont = currentFont.deriveFont(currentFont.getSize() + 5f);
                setFontSize(newFont);
            }
        });

        decreaseFontSizeButton.addActionListener(new ActionListener() 
        { 
            @Override
            public void actionPerformed(ActionEvent e) {
                Font currentFont = firstNameField.getFont();
                Font newFont = currentFont.deriveFont(currentFont.getSize() - 5f);
                setFontSize(newFont);
            }
        });

        JScrollPane scrollPane = new JScrollPane(threePointList);
        setLayout(new BorderLayout());
        scrollPane.setViewportView(threePointList);
        add(inputPanel, BorderLayout.WEST);
        add(scrollPane, BorderLayout.CENTER);
        add(buttonPanel, BorderLayout.SOUTH);
    }
    private JPanel createInputPanel() {
        JPanel inputPanel = new JPanel(new GridBagLayout());
        GridBagConstraints gbc = new GridBagConstraints();
        gbc.anchor = GridBagConstraints.WEST;
        gbc.insets = new Insets(5, 5, 5, 5);

        gbc.gridx = 0;
        gbc.gridy = 0;
        inputPanel.add(new JLabel("First Name:"), gbc);
        gbc.gridy++;
        inputPanel.add(new JLabel("Last Name:"), gbc);
        gbc.gridy++;
        inputPanel.add(new JLabel("Date:"), gbc);
        gbc.gridy++;
        inputPanel.add(new JLabel("Attempted:"), gbc);
        gbc.gridy++;
        inputPanel.add(new JLabel("Made:"), gbc);

        gbc.gridx = 1;
        gbc.gridy = 0;
        inputPanel.add(firstNameField, gbc);
        gbc.gridy++;
        inputPanel.add(lastNameField, gbc);
        gbc.gridy++;
        inputPanel.add(dateField, gbc);
        gbc.gridy++;
        inputPanel.add(attemptedField, gbc);
        gbc.gridy++;
        inputPanel.add(madeField, gbc);

        gbc.gridy++;
        inputPanel.add(new JLabel("Sort By:"), gbc);
        gbc.gridx = 0;
        gbc.gridwidth = 2;
        gbc.gridy++;
        inputPanel.add(sortingComboBox, gbc);

        return inputPanel;
    }

    private JPanel createButtonPanel() {
        JPanel buttonPanel = new JPanel();
        buttonPanel.add(addPointCheckButton);
        buttonPanel.add(increaseFontSizeButton);
        buttonPanel.add(decreaseFontSizeButton);
        buttonPanel.add(editSessionButton);
        return buttonPanel;
    }

    private void updateListModel()
    {
        threePointListModel.clear();
        for (Points point : threePointShotsList) 
        {
            threePointListModel.addElement(point);
        }
    }

    public void addToListModel(Points pointSession)
    {
        threePointShotsList.add(pointSession);
        updateListModel();
    }

    private void repopulateLists()
    {
        ArrayList<String[]> data;
        data = persistData.dataToArrayListThreePoints();
        for(int x = 0; x < data.size(); x++)
        {
            String[] pointData = data.get(x);
            Points point = new Points(pointData[0], pointData[1], pointData[2], Integer.parseInt(pointData[3]), Integer.parseInt(pointData[4]));
            threePointShotsList.add(point);
            threePointListModel.addElement(point);
        }
    }

    private void clearFields()
    {
        firstNameField.setText("");
        lastNameField.setText("");
        dateField.setText("");
        attemptedField.setText("");
        madeField.setText("");
        locaField.setText("");
    }
    private void repopulateTable() {
        ArrayList<String[]> data = persistData.dataToArrayListFreeThrows();
        for (String[] pointData : data) {
            Points point = new Points(pointData[0], pointData[1], pointData[2], Integer.parseInt(pointData[3]), Integer.parseInt(pointData[4]));
            addSessionToTable(point);
        }
    }

    private void addSessionToTable(Points point) {
        Object[] rowData = {point.getFirstName(), point.getLastName(), point.getDate(), point.getFreeThrowsAttempted(), point.getFreeThrowsMade()};
        tableModel.addRow(rowData);
    }

    private void updateSessionInTable(Points point) {
        int selectedRowIndex =  private void repopulateTable() {
        ArrayList<String[]> data = persistData.dataToArrayListFreeThrows();
        for (String[] pointData : data) {
            Points point = new Points(pointData[0], pointData[1], pointData[2], Integer.parseInt(pointData[3]), Integer.parseInt(pointData[4]));
            addSessionToTable(point);
        }
    }

    private void addSessionToTable(Points point) {
        Object[] rowData = {point.getFirstName(), point.getLastName(), point.getDate(), point.getFreeThrowsAttempted(), point.getFreeThrowsMade()};
        tableModel.addRow(rowData);
    }

    private void updateSessionInTable(Points point) {
        int selectedRowIndex = threePointTable.getSelectedRow();
        if (selectedRowIndex != -1) {
            tableModel.setValueAt(point.getFirstName(), selectedRowIndex, 0);
            tableModel.setValueAt(point.getLastName(), selectedRowIndex, 1);
            tableModel.setValueAt(point.getDate(), selectedRowIndex, 2);
            tableModel.setValueAt(point.getFreeThrowsAttempted(), selectedRowIndex, 3);
            tableModel.setValueAt(point.getFreeThrowsMade(), selectedRowIndex, 4);
        }
    }.getSelectedRow();
        if (selectedRowIndex != -1) {
            tableModel.setValueAt(point.getFirstName(), selectedRowIndex, 0);
            tableModel.setValueAt(point.getLastName(), selectedRowIndex, 1);
            tableModel.setValueAt(point.getDate(), selectedRowIndex, 2);
            tableModel.setValueAt(point.getFreeThrowsAttempted(), selectedRowIndex, 3);
            tableModel.setValueAt(point.getFreeThrowsMade(), selectedRowIndex, 4);
        }
    }

    private void setFontSize(Font font) {
        firstNameField.setFont(font);
        lastNameField.setFont(font);
        dateField.setFont(font);
        attemptedField.setFont(font);
        madeField.setFont(font);
        threePointList.setFont(font);
        locaField.setFont(font);
    }
    private void setColumnWidths(TableColumnModel columnModel, int[] widths) {
        for (int i = 0; i < widths.length; i++) {
            columnModel.getColumn(i).setPreferredWidth(widths[i]);
        }
    }
}