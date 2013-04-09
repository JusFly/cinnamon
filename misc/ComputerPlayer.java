package misc;

import java.util.ArrayList;
import java.util.Collections;
import java.util.Random;
import java.util.Set;

import misc.Card.CardType;

import board.Board;
import board.BoardCell;
import board.RoomCell;
import java.awt.Graphics;
import java.awt.Color;

public class ComputerPlayer extends Player {

	private char lastRoomVisited;
	private Suggestion accusation;

	public Suggestion createSuggestion(int row, int column, ArrayList<Card> deck, Board board) {
		Suggestion suggestion = new Suggestion();
		String room = board.getRooms().get(board.getRoomCellAt(row, column).getRoomClassifier());
		suggestion.setRoom(new Card (room, CardType.ROOM));
		Collections.shuffle(deck);
		suggestion.setPerson(findValidCard(deck, CardType.PERSON));
		suggestion.setWeapon(findValidCard(deck, CardType.WEAPON));
		return suggestion;
	}

	public Card findValidCard(ArrayList<Card> deck, CardType type) {
		ArrayList<Card> knownCards = this.getKnownCards();
		for (Card x : deck) {
			if (x.getCardType().equals(type) && !knownCards.contains(x)) {
				return x;
			}
		}
		return null;

	}

	public BoardCell pickLocation(Set<BoardCell> targets) {

		for (BoardCell selection : targets) {
			if (selection.isRoom()) {
				RoomCell room = (RoomCell) selection;
				if (room.getRoomClassifier() != lastRoomVisited) {
					return selection;
				}
			}
		}
		Random generator = new Random();
		int random = generator.nextInt();
		random = Math.abs(random % targets.size());
		Object[] targArr =  targets.toArray();
		return (BoardCell) targArr[random];
	}

	public void makeMove (Board board, int roll, ClueGame game) {
		if(!accusation.equals(null)) {
			makeAccusation(game);
		}
		else {
		board.startTargets(board.calcIndex(this.getRow(), this.getColumn()), roll);
		System.out.println(this.getName() + " index " + board.calcIndex(this.getRow(), this.getColumn()) + " roll " + roll); //print
		System.out.println("target size " + board.getTargets().size());
		BoardCell choice = pickLocation(board.getTargets());
		for(int x = 0; x < board.getNumColumns(); x++) {
			for(int y = 0; y < board.getNumRows(); y++) {
				if (board.getCellAt(y, x).equals(choice)) {
					this.setColumn(x);
					this.setRow(y);
				}
			}
		}
		if(board.getCellAt(getRow(), getColumn()).isRoom()) {
			// set flag for last room
			RoomCell room = (RoomCell) board.getCellAt(getRow(), getColumn());
			lastRoomVisited = room.getRoomClassifier();

			// create a suggestion
			Suggestion s = createSuggestion(getRow(), getColumn(), game.getDeck(), board);
			Card disprove = game.handleSuggestion(s.getPerson().getName(), s.getRoom().getName(), s.getWeapon().getName(), this);
			//dave
			if (!this.getKnownCards().contains(disprove)) {
				this.getKnownCards().add(disprove);
			}
			//dave
			System.out.println("number of known cards" + this.knownCards.size());
			
			// update control panel
			game.getControlPanel().getGuesstext().setText(s.getPerson().getName() + " " + s.getRoom().getName() + " " + s.getWeapon().getName());
			if(disprove != null) {
				game.getControlPanel().getResponse().setText(disprove.getName());
			}
			else {
				game.getControlPanel().getResponse().setText("no response");
				accusation = s;
			}


			// call disproveSuggestion
		}
		board.repaint();
		}
	}
	
	public void makeAccusation(ClueGame game) {
		//display computers accusation and if it is correct.
		System.out.println("accusation" + accusation.getPerson() + " " + accusation.getRoom() + " " + accusation.getWeapon());
		Solution solution = new Solution(accusation.getPerson().getName(), accusation.getWeapon().getName(), accusation.getRoom().getName());
		if (game.checkAccusation(solution)) {
			
		}
	}

	public ComputerPlayer(String name, String color, int row, int column) {
		super(name, color, row, column);
		accusation = null;
	}

	public ComputerPlayer() {
		super();
	}

	public void updateSeen(Card seen) {
		knownCards.add(seen);
	}

	public void updateSeen(ArrayList<Card> seen) {
		knownCards.addAll(seen);
	}

	public char getLastRoomVisited() {
		return lastRoomVisited;
	}

	public void setLastRoomVisited(char lastRoomVisited) {
		this.lastRoomVisited = lastRoomVisited;
	}

}
