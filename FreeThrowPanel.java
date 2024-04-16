import java.awt.BorderLayout;
import java.awt.Dimension;
import java.awt.Font;
import java.awt.GridBagConstraints;
import java.awt.GridBagLayout;
import java.awt.Insets;
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

public class FreeThrowPanel extends JPanel {
    private DefaultTableModel tableModel;
    private JTable freeThrowTable;
    private JComboBox<String> sortingComboBox;
    private JButton addPointCheckButton, increaseFontSizeButton, decreaseFontSizeButton, editSessionButton;
    private JTextField firstNameField, lastNameField, dateField, attemptedField, madeField;
    private Points selectedSession;
    private PersistData persistData = new PersistData();
    private SQLConnection conn = new SQLConnection();

    public FreeThrowPanel() {
        tableModel = new DefaultTableModel();
        tableModel.addColumn("First Name");
        tableModel.addColumn("Last Name");
        tableModel.addColumn("Date");
        tableModel.addColumn("Attempted");
        tableModel.addColumn("Made");

        freeThrowTable = new JTable(tableModel);
        freeThrowTable.setAutoResizeMode(JTable.AUTO_RESIZE_OFF); // Set to OFF to control column widths

        // Set preferred column widths
        int[] columnWidths = {325, 325, 325, 325, 325}; // Adjust as needed
        setColumnWidths(freeThrowTable.getColumnModel(), columnWidths);

        sortingComboBox = new JComboBox<>(new String[]{"Sort by Last Name", "Sort by Points Made", "Sort by Date"});
        firstNameField = new JTextField(15);
        lastNameField = new JTextField(15);
        dateField = new JTextField(15);
        attemptedField = new JTextField(15);
        madeField = new JTextField(15);

        addPointCheckButton = new JButton("Add Free Throw Check");
        increaseFontSizeButton = new JButton("Increase Font Size");
        decreaseFontSizeButton = new JButton("Decrease Font Size");
        editSessionButton = new JButton("Edit Free Throw Session");

        repopulateTable();

        addPointCheckButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                Points point = new Points(firstNameField.getText(), lastNameField.getText(), dateField.getText(), Integer.parseInt(attemptedField.getText()), Integer.parseInt(madeField.getText()));
                conn.addFreeThrow(firstNameField.getText(), lastNameField.getText(), dateField.getText(), Integer.parseInt(attemptedField.getText()), Integer.parseInt(madeField.getText()));
                addSessionToTable(point);
            }
        });

        freeThrowTable.getSelectionModel().addListSelectionListener(e -> {
            int selectedRowIndex = freeThrowTable.getSelectedRow();
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
                madeField.setText(String.valueOf(selectedSession.getFreeThrowsMade()));
            }
        });

        editSessionButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                if (selectedSession != null) {
                    selectedSession.setFirstName(firstNameField.getText());
                    selectedSession.setLastName(lastNameField.getText());
                    selectedSession.setDate(dateField.getText());
                    selectedSession.setFreeThrowsAttempted(Integer.parseInt(attemptedField.getText()));
                    selectedSession.setFreeThrowsMade(Integer.parseInt(madeField.getText()));
                    updateSessionInTable(selectedSession);
                    conn.editSession(selectedSession.getFirstName(), selectedSession.getLastName(), firstNameField.getText(), lastNameField.getText(), dateField.getText(), Integer.parseInt(attemptedField.getText()), Integer.parseInt(madeField.getText()), "freethrows");
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

        JScrollPane scrollPane = new JScrollPane(freeThrowTable);
        scrollPane.setPreferredSize(new Dimension(600, 400)); // Adjust as needed
        setLayout(new BorderLayout());
        add(createInputPanel(), BorderLayout.WEST);
        add(scrollPane, BorderLayout.CENTER);
        add(createButtonPanel(), BorderLayout.SOUTH);
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

    private void increaseFontSize() {
        Font currentFont = firstNameField.getFont();
        Font newFont = currentFont.deriveFont(currentFont.getSize() + 5f);
        setFontSize(newFont);
    }

    private void decreaseFontSize() {
        Font currentFont = firstNameField.getFont();
        Font newFont = currentFont.deriveFont(currentFont.getSize() - 5f);
        setFontSize(newFont);
    }

    private void setFontSize(Font font) {
        firstNameField.setFont(font);
        lastNameField.setFont(font);
        dateField.setFont(font);
        attemptedField.setFont(font);
        madeField.setFont(font);
        freeThrowTable.setFont(font);
    }

    private void clearFields() {
        firstNameField.setText("");
        lastNameField.setText("");
        dateField.setText("");
        attemptedField.setText("");
        madeField.setText("");
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
        int selectedRowIndex = freeThrowTable.getSelectedRow();
        if (selectedRowIndex != -1) {
            tableModel.setValueAt(point.getFirstName(), selectedRowIndex, 0);
            tableModel.setValueAt(point.getLastName(), selectedRowIndex, 1);
            tableModel.setValueAt(point.getDate(), selectedRowIndex, 2);
            tableModel.setValueAt(point.getFreeThrowsAttempted(), selectedRowIndex, 3);
            tableModel.setValueAt(point.getFreeThrowsMade(), selectedRowIndex, 4);
        }
    }

    private void setColumnWidths(TableColumnModel columnModel, int[] widths) {
        for (int i = 0; i < widths.length; i++) {
            columnModel.getColumn(i).setPreferredWidth(widths[i]);
        }
    }
}
