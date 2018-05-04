import java.awt.Color;
import java.util.ArrayList;

public class ComputerThread extends Thread {
	/** Diese Klasse implementiert einen Thread, der den Computer spielen l�sst. Hier werden die Methoden der Brain Klasse genutzt um Z�ge
	 * auszuf�hren und auszuwerten.
	 *  
	 */
	private Board board;
	private MuehleGUI gui;
	private Brain brain = new Brain();

	//Konstruktor
	public ComputerThread(MuehleGUI gui, Board board) {
		this.board = board;
		this.gui = gui;
	}

	//Run Methode �berschrieben
	@Override
	public void run() {
		while (true) {

			try {
				Thread.sleep(200);
			} catch (InterruptedException e) {
				e.printStackTrace();
			}
			
			if (gui.getPhase() > 0) { //In der Setzphase
				
				//Der Thread erkennt, ob das Spiel beendet ist
				if (board.getPossibleMovesForColor(new Color(255, 255, 255)).size() == 0) { //Wenn f�r Schwarz (Computer) keine Z�ge mehr m�glich sind 
					gui.addEventToHistory("Der Spieler gewinnt! \nKeine Z�ge f�r Schwarz m�glich.");
					gui.setEnd(true);
					gui.showEndDialog(true);
					return;
				} else if (board.getPossibleMovesForColor(new Color(0, 0, 0)).size() == 0) { //Wenn f�r Wei� (Spieler) keine Z�ge mehr m�glich sind
					gui.addEventToHistory("Der Computer gewinnt! \nKeine Z�ge f�r Wei� m�glich.");
					gui.setEnd(true);
					gui.showEndDialog(false);
					return;
				} else if (board.getNumberOfStonesOfColor(new Color(255, 255, 255)) < 3) { //Wenn Schwarz weniger als drei Steine hat
					gui.addEventToHistory("Der Spieler gewinnt! \nSchwarz hat weniger als drei Steine.");
					gui.setEnd(true);
					gui.showEndDialog(true);
					return;
				} else if (board.getNumberOfStonesOfColor(new Color(0, 0, 0)) < 3) { //Wenn Wei� weniger als drei Steine hat
					gui.addEventToHistory("Der Computer gewinnt! \nWei� hat weniger als drei Steine.");
					gui.setEnd(true);
					gui.showEndDialog(false);
					return;
				}
			}

			if (gui.isCanRemove() == false) { //Wartet, wenn der Spieler einen Stein entfernen kann
				
				//Brett, M�hlen und Zwickm�hlen speichern
				Board before = brain.copyBoard(board);
				ArrayList<Muehle> muehlenComputerBefore = board.getMuehlen(new Color(255, 255, 255));
				ArrayList<ZwickMuehle> zMuehlenComputerBefore = board.getZwickMuehlen(new Color(255, 255, 255));
				
				
				if (gui.getRound() % 2 != 0) { //Computer zieht in den ungeraden Z�gen
					for (int sleep = 2; sleep >= 0; sleep--) { //Wartet drei Sekunden
						try {
							Thread.sleep(1000);
						} catch (InterruptedException e) {
							System.out.println(e.getMessage());
						}
						gui.getTimerLbl().setText("" + sleep); //Countdown runterz�hlen
					}
					
					//Z�ge logisch und grafisch vollziehen
					if (gui.getPhase() == 0) { //Setzphase
						Position p = brain.setBestStone(board);
						int ring = p.getRing();
						int position = p.getPosition();
						StonePanel panel = gui.getStone(ring, position);
						panel.put();
						gui.getTimerLbl().setText("3");
					} else if (gui.getPhase() == 1 || gui.isBlackAllowedToJump() == false) { //Ziehphase
						Position[] move = brain.performBestDraw(gui.getDifficulty(), board);
						gui.setFrom(gui.getStone(move[0].getRing(), move[0].getPosition()));
						gui.setTo(gui.getStone(move[1].getRing(), move[1].getPosition()));
						gui.moveStone();
						gui.getTimerLbl().setText("3");
					} else if (gui.getPhase() == 2 && gui.isBlackAllowedToJump()) { //Sprungphase
						Position[] jump = brain.jump(board);
						gui.setFrom(gui.getStone(jump[0].getRing(), jump[0].getPosition()));
						gui.setTo(gui.getStone(jump[1].getRing(), jump[1].getPosition()));
						gui.addEventToHistory(
								"Computer springt von " + jump[0].toString() + " nach " + jump[1].toString());
						gui.moveStone();
						gui.getTimerLbl().setText("3");
					}
				}
				
				//Brett, M�hlen und Zwickm�hlen danach speichern
				ArrayList<Muehle> muehlenComputerAfter = board.getMuehlen(new Color(255, 255, 255));
				ArrayList<ZwickMuehle> zMuehlenComputerAfter = board.getZwickMuehlen(new Color(255, 255, 255));
				Board after = brain.copyBoard(board);
				
				//Zug auswerten und Stein entfernen, wenn M�hle entstanden ist oder Zwickm�hle genutzt wurde
				if (muehlenComputerAfter.size() > muehlenComputerBefore.size()) {
					gui.addEventToHistory("Computer hat eine M�hle!");
					Position p = brain.removeBestStone(board, new Color(255, 255, 255));
					gui.addEventToHistory("Der Computer entfernt den Stein " + p.getRing() + "," + p.getPosition());
					gui.removeStone(p.getRing(), p.getPosition());
				}
				if (zMuehlenComputerBefore.size() == zMuehlenComputerAfter.size()) {
					for (ZwickMuehle zm : zMuehlenComputerAfter) {
						if (zm.wasUsed(before, after)) {
							gui.addEventToHistory("Computer nutzt eine Zwickm�hle!");
							Position p = brain.removeBestStone(board, new Color(255, 255, 255));
							gui.addEventToHistory(
									"Der Computer entfernt den Stein " + p.getRing() + "," + p.getPosition());
							gui.removeStone(p.getRing(), p.getPosition());
							break;
						}
					}

				}
				
				//Wenn die Zahl der Steine auf dem Brett von einer Farbe kleiner ist als 4, dann darf der Spieler springen (Phase 2 --> Sprungphase)
				if (board.getNumberOfStonesOfColor(new Color(255, 255, 255)) == 3 && gui.getPhase() > 0
						&& !gui.isBlackAllowedToJump()) {
					gui.setBlackAllowedToJump(true);
					gui.addEventToHistory("Der Computer darf nun springen!");
					gui.setPhase(2);
				}
				if (board.getNumberOfStonesOfColor(new Color(0, 0, 0)) == 3 && gui.getPhase() > 0
						&& !gui.isWhiteAllowedToJump()) {
					gui.setWhiteAllowedToJump(true);
					gui.addEventToHistory("Der Spieler darf nun springen!");
					gui.setPhase(2);
				}
			}

		}
	}
}
