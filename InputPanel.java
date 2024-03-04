import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

public class InputPanel extends JPanel implements ActionListener {
    private Roster roster;
    private JTextField nameField, numberField, positionField, gradField, heightField, weightField;

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
        add(new JLabel("Height:"));
        add(heightField);
        weightField = new JTextField();
        add(new JLabel("Weight:"));
        add(weightField);

        JButton addButton = new JButton("Add Player");
        addButton.addActionListener(this);
        add(addButton);

        JButton removeButton = new JButton("Remove Player");
        removeButton.addActionListener(this);
        add(removeButton);
    }

    @Override
    public void actionPerformed(ActionEvent e) {
        if (e.getActionCommand().equals("Add Player")) {
            String name = nameField.getText();
            int number = Integer.parseInt(numberField.getText());
            String position = positionField.getText();
            int year = Integer.parseInt(gradField.getText());
            Double height = Double.parseDouble(heightField.getText());
            int weight = Integer.parseInt(weightField.getText());

            BasketballPlayer player = new BasketballPlayer(name, number, position, year, height, weight);
            roster.addPlayer(player);
        } else if (e.getActionCommand().equals("Remove Player")) {
            String name = nameField.getText();
            
            // Implement removing player functionality
            // You can use roster.removePlayer(index) method
        }
    }
}

