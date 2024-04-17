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
import javax.swing.table.DefaultTableModel;
import javax.swing.table.TableColumnModel;

public class ThreePointPanel extends JPanel {
    private DefaultTableModel tableModel;
    private JTable threePointTable;
    private JComboBox<String> sortingComboBox;
    private JTextField firstNameField, lastNameField, dateField, attemptedField, madeField, locaField;
    private JButton addPointCheckButton, increaseFontSizeButton, decreaseFontSizeButton, editSessionButton;
    private ThreePoint selectedSession;
    private SQLConnection conn = new SQLConnection();
    private PersistData persistData = new PersistData();
    private ArrayList<ThreePoint> threePointShotsList;

    public ThreePointPanel() {
        // Initialize table model and table
        tableModel = new DefaultTableModel();
        tableModel.addColumn("First Name");
        tableModel.addColumn("Last Name");
        tableModel.addColumn("Date");
        tableModel.addColumn("Attempted");
        tableModel.addColumn("Made");
        tableModel.addColumn("Location");

        threePointTable = new JTable(tableModel);
        threePointTable.setAutoResizeMode(JTable.AUTO_RESIZE_OFF); // Set to OFF to control column widths

        // Set preferred column widths
        int[] columnWidths = {150, 150, 150, 150, 150, 150};
        setColumnWidths(threePointTable.getColumnModel(), columnWidths);

        // Initialize input fields
        firstNameField = new JTextField(15);
        lastNameField = new JTextField(15);
        dateField = new JTextField(15);
        attemptedField = new JTextField(15);
        madeField = new JTextField(15);
        locaField = new JTextField(15);

        // Initialize buttons and combo box
        sortingComboBox = new JComboBox<>(new String[]{"Sort by Last Name", "Sort by Points Made", "Sort by Date"});
        addPointCheckButton = new JButton("Add Three Point Check");
        increaseFontSizeButton = new JButton("Increase Font Size");
        decreaseFontSizeButton = new JButton("Decrease Font Size");
        editSessionButton = new JButton("Edit Three Point Session");

        // Initialize panel for input fields
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

        // Initialize button panel
        JPanel buttonPanel = new JPanel();
        buttonPanel.add(addPointCheckButton);
        buttonPanel.add(increaseFontSizeButton);
        buttonPanel.add(decreaseFontSizeButton);
        buttonPanel.add(editSessionButton);

        // Add action listeners
        addPointCheckButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                ThreePoint point = new ThreePoint(firstNameField.getText(), lastNameField.getText(), dateField.getText(), Integer.parseInt(attemptedField.getText()), Integer.parseInt(madeField.getText()), locaField.getText());
                conn.addThreePoint(firstNameField.getText(), lastNameField.getText(), dateField.getText(), Integer.parseInt(attemptedField.getText()), Integer.parseInt(madeField.getText()), locaField.getText());
                addToListModel(point);
            }
        });

        editSessionButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                if (selectedSession != null) {
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
        setLayout(new BorderLayout());
        add(inputPanel, BorderLayout.WEST);
        add(new JScrollPane(threePointTable), BorderLayout.CENTER);
        add(buttonPanel, BorderLayout.SOUTH);

        // Populate table data
        repopulateTable();
    }

    private void addToListModel(ThreePoint pointSession) {
        threePointShotsList.add(pointSession);
        updateTableModel();
    }

    private void updateTableModel() {
        // Clear existing table data
        tableModel.setRowCount(0);
        // Add updated data to table model
        for (ThreePoint point : threePointShotsList) {
            Object[] rowData = {point.getFirstName(), point.getLastName(), point.getDate(), point.getThreePointsAttempted(), point.getThreePointsMade(), point.getLocation()};
            tableModel.addRow(rowData);
        }
    }

    private void repopulateTable() {
        ArrayList<String[]> data = persistData.dataToArrayListThreePoints();
        threePointShotsList = convertToThreePointList(data);
        updateTableModel();
    }

    private ArrayList<ThreePoint> convertToThreePointList(ArrayList<String[]> data) {
        ArrayList<ThreePoint> threePoints = new ArrayList<>();
        for (String[] rowData : data) {
            ThreePoint point = new ThreePoint(rowData[0], rowData[1], rowData[2], Integer.parseInt(rowData[3]), Integer.parseInt(rowData[4]), rowData[5]);
            threePoints.add(point);
        }
        return threePoints;
    }

    private void clearFields() {
        firstNameField.setText("");
        lastNameField.setText("");
        dateField.setText("");
        attemptedField.setText("");
        madeField.setText("");
        locaField.setText("");
    }

    private void setColumnWidths(TableColumnModel columnModel, int[] widths) {
        for (int i = 0; i < widths.length; i++) {
            columnModel.getColumn(i).setPreferredWidth(widths[i]);
        }
    }

    private void increaseFontSize() {
        Font currentFont = threePointTable.getFont();
        Font newFont = currentFont.deriveFont(currentFont.getSize() + 5f);
        setFontSize(newFont);
    }

    private void decreaseFontSize() {
        Font currentFont = threePointTable.getFont();
        Font newFont = currentFont.deriveFont(currentFont.getSize() - 5f);
        setFontSize(newFont);
    }

    private void setFontSize(Font font) {
        threePointTable.setFont(font);
        firstNameField.setFont(font);
        lastNameField.setFont(font);
        dateField.setFont(font);
        attemptedField.setFont(font);
        madeField.setFont(font);
        locaField.setFont(font);
    }
}
