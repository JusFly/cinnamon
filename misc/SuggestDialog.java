package misc;

import java.awt.Dimension;
import java.awt.GridLayout;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

import javax.swing.JButton;
import javax.swing.JComboBox;
import javax.swing.JDialog;
import javax.swing.JLabel;


public class SuggestDialog extends JDialog {
	public enum SuggestType {
		SUGGESTION, ACCUSATION;
	}
	JButton submit, cancel;
	ClueGame game;
	JComboBox<String> room;
	JComboBox<String> person;
	JComboBox<String> weapon;
	
	public SuggestDialog(SuggestType t, ClueGame g) {
		room = new JComboBox<String>();
		person = new JComboBox<String>();
		weapon = new JComboBox<String>();
		game = g;
		createLayout(t, g);
		setSize(new Dimension(300,200));
		setVisible(true);
	}
	
	public void createLayout(SuggestType t, ClueGame g) {
		setLayout(new GridLayout(4,2));
		add(new JLabel("Room"));
		if(t == SuggestType.ACCUSATION) {
			setTitle("Make an Accusation");
			room.addItem("Conservatory");
			room.addItem("Kitchen");
			room.addItem("Ballroom");
			room.addItem("Billiard Room");
			room.addItem("Library");
			room.addItem("Study");
			room.addItem("Dining Room");
			room.addItem("Lounge");
			room.addItem("Walkway");
			room.addItem("Closet");
			room.addItem("Hall");
			add(room);
		}
		else { 
			setTitle("Make a Guess");
			room.addItem(g.getBoard().getRooms().get(g.getBoard().getRoomCellAt(g.getHumanPlayer().getRow(), g.getHumanPlayer().getColumn()).getRoomClassifier()));
		}
		add(room);
		add(new JLabel("Person"));
		person.addItem("Miss Scarlet");
		person.addItem("Mr. Green");
		person.addItem("Mrs. White");
		person.addItem("Colonel Mustard");
		person.addItem("Professor Plum");
		add(person);
		add(new JLabel("Weapon"));
		weapon.addItem("Revolver");
		weapon.addItem("Knife");
		weapon.addItem("Rope");
		weapon.addItem("Lead Pipe");
		weapon.addItem("Candlestick");
		weapon.addItem("Wrench");
		add(weapon);
		add(submit());
		add(cancel());
	}
	
	public JButton submit() {
		submit = new JButton("Submit");
		submit.addActionListener(new ButtonListener());
		return submit;
	}
	
	public JButton cancel() {
		cancel = new JButton("Cancel");
		cancel.addActionListener(new ButtonListener());
		return cancel;
	}
	class ButtonListener implements ActionListener {

		@Override
		public void actionPerformed(ActionEvent e) {
			if(e.getSource() == submit) {
				Solution s = new Solution(person.getSelectedItem().toString(), weapon.getSelectedItem().toString(), room.getSelectedItem().toString());
				game.checkAccusation(s);
			}
			setVisible(false);
		}
		
	}
	
}
