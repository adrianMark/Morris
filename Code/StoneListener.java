import java.awt.Color;
import java.awt.event.MouseEvent;
import java.awt.event.MouseListener;
import java.util.ArrayList;

public class StoneListener implements MouseListener {

	/** Diese Klasse leitet den MouseListener ab und regiert je nach Spielphase und Berechtigungen des Spielers anders.
	 * Sie wird jeweils einem StonePanel zugewiesen.
	 */
	private StonePanel stone;

	private Color white = new Color(255, 255, 255);

	//Konstruktor
	public StoneListener(StonePanel stone) {
		this.stone = stone;
	}

	@Override
	public void mouseClicked(MouseEvent e) {
		//Spielphase(Setzen - 0/ Ziehen - 1/ Springen - 2) bestimmen
		int phase = stone.getGui().getPhase();
		//Zug - Gerade Zahl Spieler Zug; ungerade Zahl Computer Zug
		int round = stone.getGui().getRound();
		
		if (!stone.getGui().isEnd()) { //Solange das Spiel läuft
			if (stone.getGui().isCanRemove()) { //Wenn der Spieler einen Stein entfernen darf
				if (stone.isOccupied()) {
					ArrayList<Muehle> muehlen = stone.getGui().getBoard().getMuehlen(new Color(255,255,255));
					ArrayList<Position> notRemovable = new ArrayList<>();
					for(Muehle m : muehlen){ //Steine, die sich in Mühlen befinden dürfen nicht entfernt werden
						for(Position p : m.getPositions()){
							notRemovable.add(p);
						}
					}
					if (stone.getBackgroundColor().equals(white)) { //Es handelt sich um einen eigenen Stein
						stone.getGui().addEventToHistory(
								"Der Stein " + stone.getRing() + "," + stone.getPosition() + " gehört Ihnen");
					} else {
						for(Position p : notRemovable){ //Wenn der Stein in einer Mühle verankert ist
							if(p.getRing() == stone.getRing() && p.getPosition() == stone.getPosition()){
								stone.getGui().addEventToHistory("Der Stein "+stone.getRing()+","+stone.getPosition()+" befindet sich in einer Mühle");
								return;
							}
						}
						//Entfernen eines Steines, der entfernt werden darf
						stone.getGui().removeStone(stone.getRing(), stone.getPosition());
						stone.getGui().getBoard().setPosition(stone.getRing(), stone.getPosition(), white, false);
						stone.getGui().setCanRemove(false);
						stone.getGui().addEventToHistory(
								"Sie entfernen den Stein " + stone.getRing() + "," + stone.getPosition());
						
						//Wenn durch das entfernen nur noch drei Steine von einer Farbe auf dem Spielfeld sind, darf dieser Spieler springen (Phase 2)
						if (stone.getGui().getBoard().getNumberOfStonesOfColor(new Color(255, 255, 255)) == 3
								&& stone.getGui().getPhase() > 0 && !stone.getGui().isBlackAllowedToJump()) {
							stone.getGui().setBlackAllowedToJump(true);
							stone.getGui().addEventToHistory("Der Computer darf nun springen!");
							stone.getGui().setPhase(2);
						}
						if (stone.getGui().getBoard().getNumberOfStonesOfColor(new Color(0, 0, 0)) == 3
								&& stone.getGui().getPhase() > 0 && !stone.getGui().isWhiteAllowedToJump()) {
							stone.getGui().setWhiteAllowedToJump(true);
							stone.getGui().addEventToHistory("Der Spieler darf nun springen!");
							stone.getGui().setPhase(2);
						}
					}
				}
			} else {
				if (round % 2 == 0) { //Wenn der Spieler am Zug ist
					
					//Board kopieren und Mühlen/Zwickmühlen speichern
					Board before = stone.getGui().getBrain().copyBoard(stone.getGui().getBoard());
					ArrayList<Muehle> muehlenSpielerBefore = stone.getGui().getBoard().getMuehlen(new Color(0, 0, 0));
					ArrayList<ZwickMuehle> zMuehlenSpielerBefore = stone.getGui().getBoard()
							.getZwickMuehlen(new Color(0, 0, 0));

					if (phase == 0 && !stone.isOccupied()) { //In Phase 0 --> Setzphase; Position muss unbesetzt sein
						stone.put();
					} else if (phase > 0 && stone.getGui().getFrom() == null && stone.isOccupied() //In Phase 1 --> Zugphase; 
							&& stone.getBackgroundColor().equals(new Color(255, 255, 255))) {
						
						/* In dieser Phase muss erst ein Stein ausgewählt werden der verschoben werden soll. Dieser muss von der Farbe des Spielers sein,
						 * besetzt sein und in der Gui Klasse darf noch kein Stein als 'From' (Ausgangsposition des Zugs) gesetzt sein
						*/
						
						stone.getGui().setFrom(stone);
						stone.getGui().setSelected(stone);
						
					} else if (phase > 0 && stone.getGui().getFrom() != null) { //In diesem Fall ist from bereits gesetzt und der User clickt die Zielposition an (dort wo der Stein hingesetzt werden soll)
						if (stone.getRing() == stone.getGui().getFrom().getRing()
								&& stone.getPosition() == stone.getGui().getFrom().getPosition()) { //Wenn es sich um den zuvor markierten Stein handelt(From), so soll er deselektiert werden
							stone.getGui().setDeselected(stone, white);
							stone.getGui().setFrom(null);
						} else if (!stone.isOccupied()) { 
							ArrayList<Position> neighborsOfFrom = stone.getGui().getBoard().getAllNeighbors(
									stone.getGui().getBoard().getPosition(stone.getGui().getFrom().getRing(),
											stone.getGui().getFrom().getPosition()));
							boolean isAllowed = false;
							if (phase != 2 || !stone.getGui().isWhiteAllowedToJump()) {
								/*//Ansonsten, wenn die Position frei ist und der Spieler nicht springen darf, wird überrpüft ob die Zielposition ('To')
								 * ein Nachbar von 'From' ist und ob der Zug somit gültig ist
								 */
								for (Position p : neighborsOfFrom) {
									int ring = p.getRing();
									int pos = p.getPosition();
									if (ring == stone.getRing() && pos == stone.getPosition()) {
										stone.getGui().setTo(stone);
										stone.getGui().moveStone();
										isAllowed = true;
									}
								}
								if (!isAllowed) { //Wenn der Zug gültig ist, wird er logisch und grafisch vollzogen und das Event auf der GUI geloggt
									stone.getGui().addEventToHistory("Der Zug " + stone.getGui().getFrom().getRing()
											+ "," + stone.getGui().getFrom().getPosition() + " nach " + stone.getRing()
											+ "," + stone.getPosition() + " ist nicht erlaubt!");
								}
							} else if (phase == 2 && stone.getGui().isWhiteAllowedToJump()) { //Wenn die Sprungphase erreicht ist und der Spieler springen darf, wird eine Nachbarschaftsprüfung nicht durchgeführt
								stone.getGui().setTo(stone);
								stone.getGui().addEventToHistory(
										"Der Spieler springt von " + stone.getGui().getFrom().getRing() + ","
												+ stone.getGui().getFrom().getPosition() + " nach " + stone.getRing()
												+ "," + stone.getPosition());
								stone.getGui().moveStone();
							}

						}
					}
					ArrayList<ZwickMuehle> zMuehlenSpielerAfter = stone.getGui().getBoard()
							.getZwickMuehlen(new Color(0, 0, 0));
					ArrayList<Muehle> muehlenSpielerAfter = stone.getGui().getBoard().getMuehlen(new Color(0, 0, 0));
					Board after = stone.getGui().getBrain().copyBoard(stone.getGui().getBoard());

					//Wenn durch den Zug eine Mühle entstanden ist oder eine Zwickmühle genutzt wurde, darf der Spieler einen Stein entfernen
					if (muehlenSpielerAfter.size() > muehlenSpielerBefore.size()) {
						stone.getGui().addEventToHistory("Spieler hat eine Mühle!");
						stone.getGui().addEventToHistory("Entfernen Sie einen gegnerischen Stein.");
						stone.getGui().setCanRemove(true);
					}
					if (zMuehlenSpielerBefore.size() == zMuehlenSpielerAfter.size()) {
						for (ZwickMuehle zm : zMuehlenSpielerAfter) {
							if (zm.wasUsed(before, after)) {
								stone.getGui().addEventToHistory("Spieler nutzt eine Zwickmühle!");
								stone.getGui().addEventToHistory("Entfernen Sie einen gegnerischen Stein.");
								stone.getGui().setCanRemove(true);
								break;
							}
						}
					}
				}
			}
		}
	}

	@Override
	public void mouseEntered(MouseEvent e) {

	}

	@Override
	public void mouseExited(MouseEvent e) {

	}

	@Override
	public void mousePressed(MouseEvent e) {

	}

	@Override
	public void mouseReleased(MouseEvent e) {

	}

}
