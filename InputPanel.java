import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

public class InputPanel extends JPanel implements ActionListener {
    private Roster roster;
    private JTextField nameField, numberField, positionField;

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
            BasketballPlayer player = new BasketballPlayer(name, number, position);
            roster.addPlayer(player);
        } else if (e.getActionCommand().equals("Remove Player")) {
            // Implement removing player functionality
            // You can use roster.removePlayer(index) method
        }
    }
}
