/**
 * JPanel that contains a series of input fields that are used to add or remove players from the database.
 * 
 * Authors: 
 * Jeffery Eisenhardt - eisenhardtj
 * Christine Colvin - christinecolvin
 * Cole Aydelotte - coleaydelotte
 * Jalil Rodriguez - JalilR08
 */
import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

public class InputPanel extends JPanel implements ActionListener {
    private Roster roster;
    private JTextField nameField, numberField, positionField, gradField, heightField, weightField;

    /**
     * Series of input text fields that are added to the GUI that are used to take a input and create 
     * or remove a player from the Moravianwomensteam24 database.
     */
    public InputPanel(Roster roster) {
        this.roster = roster;

        setLayout(new GridLayout(4, 2));
        add(new JLabel("Name:"));
        nameField = new JTextField();
        add(nameField);
        add(new JLabel("Number:"));
        numberField = new JTextField();
        add(numberField);
        add(new JLabel("Position:"));
        positionField = new JTextField();
        add(positionField);
        gradField = new JTextField();
        add(new JLabel("Expected graduation date:"));
        add(gradField);
        heightField = new JTextField();
        add(new JLabel("Height in feet:"));
        add(heightField);
        weightField = new JTextField();
        add(new JLabel("Weight:"));
        add(weightField);

        //add and remove player buttons.
        JButton addButton = new JButton("Add Player");
        addButton.addActionListener(this);
        add(addButton);

        JButton removeButton = new JButton("Remove Player");
        removeButton.addActionListener(this);
        add(removeButton);
    }

    /**
     * Action listener that is executed when the add or remove player buttons are pushed,
     * when pushed the buttons grab parameters from the text fields and pass them to the
     * corresponding function in the Roster.java file.
     */
    @Override
    public void actionPerformed(ActionEvent e) {
        if (e.getActionCommand().equals("Add Player")) {
            String name = nameField.getText();
            int number = Integer.parseInt(numberField.getText());
            String position = positionField.getText();
            int year = Integer.parseInt(gradField.getText());
            String height = heightField.getText();
            int weight = Integer.parseInt(weightField.getText());

            BasketballPlayer player = new BasketballPlayer(name, number, position, year, height, weight);
            roster.addPlayer(player);
        } else if (e.getActionCommand().equals("Remove Player")) {
            String name = nameField.getText();
            roster.removePlayer(name);
        }
    }
}
