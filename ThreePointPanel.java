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
    private DefaultListModel<ThreePoint> threePointListModel;
    private JList<ThreePoint> threePointList;
    private ArrayList<ThreePoint> threePointShotsList;
    private JComboBox<String> sortingComboBox;
    private JButton addPointCheckButton, increaseFontSizeButton, decreaseFontSizeButton, editSessionButton;
    private JTextField firstNameField, lastNameField, dateField, attemptedField, madeField, locaField;
    private JPanel inputPanel;
    private ThreePoint selectedSession;
    private SQLConnection conn = new SQLConnection();
    private PersistData persistData = new PersistData();

    public ThreePointPanel()
    {
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
            ThreePoint point = new ThreePoint(firstNameField.getText(), lastNameField.getText(), dateField.getText(), Integer.parseInt(attemptedField.getText()), Integer.parseInt(madeField.getText()), locaField.getText());
            conn.addThreePoint(firstNameField.getText(), lastNameField.getText(), dateField.getText(), Integer.parseInt(attemptedField.getText()), Integer.parseInt(madeField.getText()), locaField.getText());
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
                attemptedField.setText(String.valueOf(selectedSession.getThreePointsAttempted()));
                madeField.setText(String.valueOf(selectedSession.getThreePointsMade()));
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
                    selectedSession.setThreePointsAttempted(Integer.parseInt(attemptedField.getText()));
                    selectedSession.setThreePointsMade(Integer.parseInt(madeField.getText()));
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

    private void updateListModel()
    {
        threePointListModel.clear();
        for (ThreePoint point : threePointShotsList) 
        {
            threePointListModel.addElement(point);
        }
    }

    public void addToListModel(ThreePoint pointSession)
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
            ThreePoint point = new ThreePoint(pointData[0], pointData[1], pointData[2], Integer.parseInt(pointData[3]), Integer.parseInt(pointData[4]), pointData[5]);
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

    private void setFontSize(Font font) {
        firstNameField.setFont(font);
        lastNameField.setFont(font);
        dateField.setFont(font);
        attemptedField.setFont(font);
        madeField.setFont(font);
        threePointList.setFont(font);
        locaField.setFont(font);
    }
}