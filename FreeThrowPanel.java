import java.awt.BorderLayout;
import java.awt.FlowLayout;
import java.awt.Font;
import java.awt.GridLayout;
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
import javax.swing.JTextField;

/**
 * This class creates a panel that allows the user to input data for a free throw session. 
 * The user can input the first name, last name, date, number of free throws attempted, 
 * and number of free throws made. The user can also sort the list of free throw sessions 
 * by last name, points made, or date. The user can add a free throw session to the list, 
 * increase the font size of the list, decrease the font size of the list, and edit a free 
 * throw session.
 * 
 * Author: Cole Aydelotte
 */
public class FreeThrowPanel extends JPanel
{
    private JPanel buttonPanel;
    private DefaultListModel<Points> freeThrowPointListModel;
    private JList<Points> freeThrowList;
    private ArrayList<Points> freeThrowShotsList;
    private JComboBox<String> sortingComboBox;
    private JButton addPointCheckButton, increaseFontSizeButton, decreaseFontSizeButton, editSessionButton;
    private JTextField firstNameField, lastNameField, dateField, attemptedField, madeField;
    private Points selectedSession;
    JPanel inputPanel;
    SQLConnection conn = new SQLConnection();

    public FreeThrowPanel()
    {
        buttonPanel = new JPanel();
        freeThrowPointListModel = new DefaultListModel<>();
        freeThrowList = new JList<>(freeThrowPointListModel);
        freeThrowShotsList = new ArrayList<>();

        inputPanel = new JPanel();

        inputPanel.setLayout(new GridLayout(10, 2));

        sortingComboBox = new JComboBox<>(new String[]{"Sort by Last Name", "Sort by Points Made", "Sort by Date"});
        firstNameField = new JTextField(15);
        lastNameField = new JTextField(15);
        dateField = new JTextField(15);
        attemptedField = new JTextField(15);
        madeField = new JTextField(15);

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
        inputPanel.add(new JLabel("Sort By:"));
        inputPanel.add(sortingComboBox);

        addPointCheckButton = new JButton("Add Free Throw Check");
        increaseFontSizeButton = new JButton("Increase Font Size");
        decreaseFontSizeButton = new JButton("Decrease Font Size");
        editSessionButton = new JButton("Edit Free Throw Session");

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
                Points point = new Points(firstNameField.getText(), lastNameField.getText(), dateField.getText(), Integer.parseInt(attemptedField.getText()), Integer.parseInt(madeField.getText()));
                conn.addFreeThrow(firstNameField.getText(), lastNameField.getText(), dateField.getText(), Integer.parseInt(attemptedField.getText()), Integer.parseInt(madeField.getText()));
                freeThrowShotsList.add(point);
                updateListModel();
            }
        });

        freeThrowList.addListSelectionListener(e -> 
        {
            selectedSession = freeThrowList.getSelectedValue();
            if (selectedSession != null) {
                firstNameField.setText(selectedSession.getFirstName());
                lastNameField.setText(selectedSession.getLastName());
                dateField.setText(selectedSession.getDate());
                attemptedField.setText(String.valueOf(selectedSession.getFreeThrowsAttempted()));
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
                    selectedSession.setFreeThrowsMade(Integer.parseInt(madeField.getText()));
                    freeThrowPointListModel.set(freeThrowList.getSelectedIndex(), selectedSession);
                    conn.editSession(selectedSession.getFirstName(), selectedSession.getLastName(), firstNameField.getText(), lastNameField.getText(), dateField.getText(), Integer.parseInt(attemptedField.getText()), Integer.parseInt(madeField.getText()), "freethrows");
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

        JScrollPane scrollPane = new JScrollPane(freeThrowList);
        setLayout(new BorderLayout());
        scrollPane.setViewportView(freeThrowList);
        add(inputPanel, BorderLayout.WEST);
        add(scrollPane, BorderLayout.CENTER);
        add(buttonPanel, BorderLayout.SOUTH);
    }

    private void updateListModel()
    {
        freeThrowPointListModel.clear();
        for (Points point : freeThrowShotsList) 
        {
            freeThrowPointListModel.addElement(point);
        }
    }

    public void addToListModel(Points pointSession)
    {
        freeThrowShotsList.add(pointSession);
        updateListModel();
    }

    private void repopulateLists()
    {
        ArrayList<String[]> data;
        data = conn.dataToArrayListFreeThrows();
        for(int x = 0; x < data.size(); x++)
        {
            String[] pointData = data.get(x);
            Points point = new Points(pointData[0], pointData[1], pointData[2], Integer.parseInt(pointData[3]), Integer.parseInt(pointData[4]));
            freeThrowShotsList.add(point);
            freeThrowPointListModel.addElement(point);
        }
    }

    private void setFontSize(Font font) 
    {
        firstNameField.setFont(font);
        lastNameField.setFont(font);
        dateField.setFont(font);
        attemptedField.setFont(font);
        madeField.setFont(font);
        freeThrowList.setFont(font);
    }

    private void clearFields()
    {
        firstNameField.setText("");
        lastNameField.setText("");
        dateField.setText("");
        attemptedField.setText("");
        madeField.setText("");
    }
}