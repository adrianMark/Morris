
import java.awt.Color;
import java.util.ArrayList;

public class Board {
	
	/** Diese Klasse ist das logische Spielbrett. Es besteht aus einem zweidimensionalem Array mit 3x8 Positionen. Die Klasse bietet verschiedene Methoden
	 * zur Analyse des Spielbretts. 
	 */

	private Position[][] rings = new Position[3][8];

	//Konstruktor
	public Board() {
		for (int ring = 0; ring <= 2; ring++) {
			for (int position = 0; position <= 7; position++) {
				rings[ring][position] = new Position(position, ring);
			}
		}
	}

	
	//Getter und Setter Methoden
	public void setPosition(int ring, int position, Color c, boolean occupied) {
		rings[ring][position].setOccupied(occupied);
		rings[ring][position].setColor(c);
	}

	public boolean isOccupied(int ring, int position) {
		return rings[ring][position].isOccupied();
	}

	public Position getPosition(int ring, int position) {
		return rings[ring][position];
	}
	
	public Position[][] getBoard() {
		return this.rings;
	}

	public void setBoard(Position[][] rings) {
		this.rings = rings;
	}
	//-------------------------------------------------------

	//toString Methode
	public String toString() {
		StringBuffer sb = new StringBuffer();
		sb.append(checkPosition(0, 0) + "----------" + checkPosition(0, 1) + "----------" + checkPosition(0, 2) + "\n");
		sb.append("|          |          |\n");
		sb.append("|    " + checkPosition(1, 0) + "-----" + checkPosition(1, 1) + "-----" + checkPosition(1, 2)
				+ "    |\n");
		sb.append("|    |     |     |    |\n");
		sb.append("|    |  " + checkPosition(2, 0) + "--" + checkPosition(2, 1) + "--" + checkPosition(2, 2)
				+ "  |    |\n");
		sb.append("|    |  |     |  |    |\n");
		sb.append(checkPosition(0, 7) + "----" + checkPosition(1, 7) + "--" + checkPosition(2, 7) + "     "
				+ checkPosition(2, 3) + "--" + checkPosition(1, 3) + "----" + checkPosition(0, 3) + "\n");
		sb.append("|    |  |     |  |    |\n");
		sb.append("|    |  " + checkPosition(2, 6) + "--" + checkPosition(2, 5) + "--" + checkPosition(2, 4)
				+ "  |    |\n");
		sb.append("|    |     |     |    |\n");
		sb.append("|    " + checkPosition(1, 6) + "-----" + checkPosition(1, 5) + "-----" + checkPosition(1, 4)
				+ "    |\n");
		sb.append("|          |          |\n");
		sb.append(checkPosition(0, 6) + "----------" + checkPosition(0, 5) + "----------" + checkPosition(0, 4) + "\n");
		return sb.toString();

	}

	/**
	 *  Diese Methode gibt einen character zurück, der aussagt, von welcher Farbe die Position besetzt wird oder ob sie frei ist
	 * @param ring Der betroffene Ring auf dem die Position liegt (siehe Dokumentation)
	 * @param position Die Position auf dem Ring (siehe Dokumentation)
	 * @return character ('W' für von Weiß besetzt; 'B' für von Schwarz besetzt; '0' für leer)
	 */
	public char checkPosition(int ring, int position) {
		if (rings[ring][position].isOccupied()) {
			if (rings[ring][position].getColor().equals(new Color(255, 255, 255))) {
				return 'B';
			} else {
				return 'W';
			}
		} else {
			return '0';
		}
	}

	/** Diese Methode liefert alle Positionen, die auf dem Spielbrett frei sind
	 * 
	 * @return ArrayList mit allen freien Positionen auf dem Spielfeld
	 */
	public ArrayList<Position> getFreePositions() {
		ArrayList<Position> freePositions = new ArrayList<>();
		for (int ring = 0; ring <= 2; ring++) {
			for (int position = 0; position <= 7; position++) {
				if (getPosition(ring, position).isOccupied() == false) {
					freePositions.add(getPosition(ring, position));
				}
			}
		}
		return freePositions;
	}

	/** Diese Methode liefert alle Positionen, die von einem schwarzen oder weißen Stein belegt sind
	 * 
	 * @return ArrayList mit allen belegten Positionen auf dem Spielfeld
	 */
	public ArrayList<Position> getOccupiedPositions() {
		ArrayList<Position> occupiedPositions = new ArrayList<>();
		for (int ring = 0; ring <= 2; ring++) {
			for (int position = 0; position <= 7; position++) {
				if (getPosition(ring, position).isOccupied()) {
					occupiedPositions.add(getPosition(ring, position));
				}
			}
		}
		return occupiedPositions;
	}

	/** Diese Methode liefert alle Spielzüge, die für beide Farben möglich sind
	 * 
	 * @return ArrayList mit allen möglichen Spielzügen für beide Farben. Hierbei sind die ungeraden Indizes von denen der Spielzug ausgeht. Die geraden Indizes sind die 
	 * Positionen auf die der Stein mit dem vorherigen Index bewegt wird
	 */
	public ArrayList<Position> getPossibleMoves() {
		ArrayList<Position> moves = new ArrayList<>();
		ArrayList<Position> free = getFreePositions();
		ArrayList<Position> occupied = getOccupiedPositions();
		for (Position p : occupied) {
			if (free.contains(getTopNeighbor(p))) {
				moves.add(p);
				moves.add(getTopNeighbor(p));
			}
			if (free.contains(getRightNeighbor(p))) {
				moves.add(p);
				moves.add(getRightNeighbor(p));
			}
			if (free.contains(getBottomNeighbor(p))) {
				moves.add(p);
				moves.add(getBottomNeighbor(p));
			}
			if (free.contains(getLeftNeighbor(p))) {
				moves.add(p);
				moves.add(getLeftNeighbor(p));
			}
		}
		return moves;
	}
	
	//Methoden, um Nachbarn von Positionen zu finden; liefern null, wenn kein Nachbar auf der gewünschten Seite
	
	/**Liefert den linken Nachbarn der Position p
	 * 
	 * @param p Die betrachtete Position
	 * @return Den linken Nachbarn der Position p; null, wenn nicht vorhanden
	 */
	public Position getLeftNeighbor(Position p) {
		int ring = p.getRing();
		int position = p.getPosition();

		if (position == 0 || position == 6 || (ring == 0 && position == 7) || (ring == 2 && position == 3)) {
			return null;
		} else if (position == 7) {
			return getPosition(ring - 1, position);
		} else if (position == 3) {
			return getPosition(ring + 1, position);
		} else if (position >= 4) {
			return getPosition(ring, position + 1);
		} else {
			return getPosition(ring, position - 1);
		}
	}

	/**Liefert den rechten Nachbarn der Position p
	 * 
	 * @param p Die betrachtete Position
	 * @return Den rechten Nachbarn der Position p; null, wenn nicht vorhanden
	 */
	public Position getRightNeighbor(Position p) {
		int ring = p.getRing();
		int position = p.getPosition();

		if (position == 2 || position == 4 || (ring == 0 && position == 3) || (ring == 2 && position == 7)) {
			return null;
		} else if (position == 7) {
			return getPosition(ring + 1, position);
		} else if (position == 3) {
			return getPosition(ring - 1, position);
		} else if (position >= 4) {
			return getPosition(ring, position - 1);
		} else {
			return getPosition(ring, position + 1);
		}
	}
	
	/**Liefert den oberen Nachbarn der Position p
	 * 
	 * @param p Die betrachtete Position
	 * @return Den oberen Nachbarn der Position p; null, wenn nicht vorhanden
	 */
	public Position getTopNeighbor(Position p) {
		int ring = p.getRing();
		int position = p.getPosition();

		if (position == 0 || position == 2 || (ring == 0 && position == 1) || (ring == 2 && position == 5)) {
			return null;
		} else if (position == 1) {
			return getPosition(ring - 1, position);
		} else if (position == 5) {
			return getPosition(ring + 1, position);
		} else {
			if (position == 7) {
				return getPosition(ring, 0);
			} else if (position == 6) {
				return getPosition(ring, position + 1);
			} else {
				return getPosition(ring, position - 1);
			}
		}
	}
	
	/**Liefert den unteren Nachbarn der Position p
	 * 
	 * @param p Die betrachtete Position
	 * @return Den unteren Nachbarn der Position p; null, wenn nicht vorhanden
	 */
	public Position getBottomNeighbor(Position p) {
		int ring = p.getRing();
		int position = p.getPosition();

		if (position == 6 || position == 4 || (ring == 0 && position == 5) || (ring == 2 && position == 1)) {
			return null;
		} else if (position == 5) {
			return getPosition(ring - 1, position);
		} else if (position == 1) {
			return getPosition(ring + 1, position);
		} else if (position == 0) {
			return getPosition(ring, 7);
		} else if (position == 7) {
			return getPosition(ring, position - 1);
		} else {
			return getPosition(ring, position + 1);
		}

	}

	/**Liefert alle Nachbarn der Position p
	 * 
	 * @param p Die betrachtete Position
	 * @return Eine ArrayList mit allen Nachbarn der Position p; null Werte sind nicht enthalten
	 */
	public ArrayList<Position> getAllNeighbors(Position p) {
		ArrayList<Position> neighbors = new ArrayList<>();
		if (getLeftNeighbor(p) != null) {
			neighbors.add(getLeftNeighbor(p));
		}
		if (getRightNeighbor(p) != null) {
			neighbors.add(getRightNeighbor(p));
		}
		if (getTopNeighbor(p) != null) {
			neighbors.add(getTopNeighbor(p));
		}
		if (getBottomNeighbor(p) != null) {
			neighbors.add(getBottomNeighbor(p));
		}
		return neighbors;
	}
	//--------------------------------------------------------------------------------------------------------------

	
	/** Eine Funktion, die eine ArrayList mit Objekten der Muehle Klasse von der Farbe 'c' liefert. 
	 * 
	 * @param c Die Farbe von der die Mühlen gesucht werden.
	 * @return Eine ArrayList mit allen Mühlen der Farbe 'c' auf dem Spielfeld
	 */
	public ArrayList<Muehle> getMuehlen(Color c) {

		ArrayList<Muehle> muehlen = new ArrayList<>();

		//Schritt 1: Suchen der Mühlen, die auf einem Ring liegen (Drei Steine auf einem Ring nebeneinander)
		for (int ring = 0; ring < 3; ring++) {
			if (getPosition(ring, 0).isOccupied() && getPosition(ring, 0).getColor().equals(c)
					&& getPosition(ring, 1).isOccupied() && getPosition(ring, 1).getColor().equals(c)
					&& getPosition(ring, 2).isOccupied() && getPosition(ring, 2).getColor().equals(c)) {
				muehlen.add(new Muehle(getPosition(ring, 0), getPosition(ring, 1), getPosition(ring, 2), c));
			}
			if (getPosition(ring, 2).isOccupied() && getPosition(ring, 2).getColor().equals(c)
					&& getPosition(ring, 3).isOccupied() && getPosition(ring, 3).getColor().equals(c)
					&& getPosition(ring, 4).isOccupied() && getPosition(ring, 4).getColor().equals(c)) {
				muehlen.add(new Muehle(getPosition(ring, 2), getPosition(ring, 3), getPosition(ring, 4), c));
			}
			if (getPosition(ring, 4).isOccupied() && getPosition(ring, 4).getColor().equals(c)
					&& getPosition(ring, 5).isOccupied() && getPosition(ring, 5).getColor().equals(c)
					&& getPosition(ring, 6).isOccupied() && getPosition(ring, 6).getColor().equals(c)) {
				muehlen.add(new Muehle(getPosition(ring, 4), getPosition(ring, 5), getPosition(ring, 6), c));
			}
			if (getPosition(ring, 6).isOccupied() && getPosition(ring, 6).getColor().equals(c)
					&& getPosition(ring, 7).isOccupied() && getPosition(ring, 7).getColor().equals(c)
					&& getPosition(ring, 0).isOccupied() && getPosition(ring, 0).getColor().equals(c)) {
				muehlen.add(new Muehle(getPosition(ring, 6), getPosition(ring, 7), getPosition(ring, 0), c));
			}
		}

		//Schritt 2: Die ringübergreifenden Mühlen finden (nur möglich auf den Ring Positionen 1, 3, 5 und sieben, da die Ringe nur hier verbunden sind)
		if (getPosition(0, 1).isOccupied() && getPosition(0, 1).getColor().equals(c) && getPosition(1, 1).isOccupied()
				&& getPosition(1, 1).getColor().equals(c) && getPosition(2, 1).isOccupied()
				&& getPosition(2, 1).getColor().equals(c)) {
			muehlen.add(new Muehle(getPosition(0, 1), getPosition(1, 1), getPosition(2, 1), c));
		}
		if (getPosition(0, 7).isOccupied() && getPosition(0, 7).getColor().equals(c) && getPosition(1, 7).isOccupied()
				&& getPosition(1, 7).getColor().equals(c) && getPosition(2, 7).isOccupied()
				&& getPosition(2, 7).getColor().equals(c)) {
			muehlen.add(new Muehle(getPosition(0, 7), getPosition(1, 7), getPosition(2, 7), c));
		}
		if (getPosition(0, 5).isOccupied() && getPosition(0, 5).getColor().equals(c) && getPosition(1, 5).isOccupied()
				&& getPosition(1, 5).getColor().equals(c) && getPosition(2, 5).isOccupied()
				&& getPosition(2, 5).getColor().equals(c)) {
			muehlen.add(new Muehle(getPosition(0, 5), getPosition(1, 5), getPosition(2, 5), c));
		}
		if (getPosition(0, 3).isOccupied() && getPosition(0, 3).getColor().equals(c) && getPosition(1, 3).isOccupied()
				&& getPosition(1, 3).getColor().equals(c) && getPosition(2, 3).isOccupied()
				&& getPosition(2, 3).getColor().equals(c)) {
			muehlen.add(new Muehle(getPosition(0, 3), getPosition(1, 3), getPosition(2, 3), c));
		}
		return muehlen;
	}

	
	/** Diese Methode liefert alle Zwickmühlen der Farbe 'c', die auf dem Spielfeld bestehen als Objekte in einer ArrayList
	 * 
	 * @param c Die Farbe zu der die Zwickmühlen gesucht werden
	 * @return Eine ArryaList mit den bestehenden Zwickmühlen auf dem Feld
	 */
	public ArrayList<ZwickMuehle> getZwickMuehlen(Color c) {
		ArrayList<ZwickMuehle> zwickMuehlen = new ArrayList<>();

		/*Für die Analyse wird das Feld in vier Teile unterteilt und Aáuf jedem Viertel werden jeweils 21 Positionen auf ihre Farbe untersucht. Bestehen dort 
		Zwickmühlen, werden diese zu einer ArrayList hinzugefügt. Nachdem eine Seite anaylsiert wurde, werden die 21 untersuchten Positionen um zwei Positionen nach rechts 
		verschoben und das nächste viertel untersucht*/
		
		//Genauere Beschreibung ist der Dokumentation zu entnehmen
		
		//21 Positionen pro Viertel sind relevant
		Position p00 = getPosition(0, 0);
		Position p01 = getPosition(0, 1);
		Position p02 = getPosition(0, 2);
		Position p10 = getPosition(1, 0);
		Position p11 = getPosition(1, 1);
		Position p12 = getPosition(1, 2);
		Position p20 = getPosition(2, 0);
		Position p21 = getPosition(2, 1);
		Position p22 = getPosition(2, 2);
		Position p07 = getPosition(0, 7);
		Position p17 = getPosition(1, 7);
		Position p27 = getPosition(2, 7);
		Position p23 = getPosition(2, 3);
		Position p13 = getPosition(1, 3);
		Position p03 = getPosition(0, 3);
		Position p06 = getPosition(0, 6);
		Position p04 = getPosition(0, 4);
		Position p16 = getPosition(1, 6);
		Position p14 = getPosition(1, 4);
		Position p26 = getPosition(2, 6);
		Position p24 = getPosition(2, 4);

		//Diese werden für alle vier Seiten untersucht
		for (int side = 1; side <= 4; side++) {

			//pro Seite sind 16 Positionen möglich, die zu einer Zwickmühle führen; Insgesamt sind also 64 Stellungen der Steine zu untersuchen
			if (positionBelongsTo(c, p01) && positionBelongsTo(c, p02) && positionBelongsTo(c, p17)
					&& positionBelongsTo(c, p27) && ((positionBelongsTo(c, p00) && !p07.isOccupied()
							|| (positionBelongsTo(c, p07) && !p00.isOccupied())))) {
				zwickMuehlen.add(new ZwickMuehle(p01, p02, p17, p27, p07, p00, c));
			}
			if (positionBelongsTo(c, p00) && positionBelongsTo(c, p02) && positionBelongsTo(c, p10)
					&& positionBelongsTo(c, p12) && ((positionBelongsTo(c, p01) && !p11.isOccupied()
							|| (positionBelongsTo(c, p11) && !p01.isOccupied())))) {
				zwickMuehlen.add(new ZwickMuehle(p00, p02, p10, p12, p01, p11, c));
			}
			if (positionBelongsTo(c, p00) && positionBelongsTo(c, p01) && positionBelongsTo(c, p23)
					&& positionBelongsTo(c, p13) && ((positionBelongsTo(c, p02) && !p03.isOccupied()
							|| (positionBelongsTo(c, p03) && !p02.isOccupied())))) {
				zwickMuehlen.add(new ZwickMuehle(p00, p01, p23, p13, p02, p03, c));
			}
			if (positionBelongsTo(c, p11) && positionBelongsTo(c, p12) && positionBelongsTo(c, p07)
					&& positionBelongsTo(c, p27) && ((positionBelongsTo(c, p10) && !p17.isOccupied()
							|| (positionBelongsTo(c, p17) && !p10.isOccupied())))) {
				zwickMuehlen.add(new ZwickMuehle(p11, p12, p07, p27, p10, p17, c));
			}
			if (positionBelongsTo(c, p10) && positionBelongsTo(c, p12) && positionBelongsTo(c, p00)
					&& positionBelongsTo(c, p02) && ((positionBelongsTo(c, p11) && !p01.isOccupied()
							|| (positionBelongsTo(c, p01) && !p11.isOccupied())))) {
				zwickMuehlen.add(new ZwickMuehle(p10, p12, p00, p02, p11, p01, c));
			}
			if (positionBelongsTo(c, p10) && positionBelongsTo(c, p12) && positionBelongsTo(c, p20)
					&& positionBelongsTo(c, p22) && ((positionBelongsTo(c, p11) && !p21.isOccupied()
							|| (positionBelongsTo(c, p21) && !p11.isOccupied())))) {
				zwickMuehlen.add(new ZwickMuehle(p10, p12, p20, p22, p11, p21, c));
			}
			if (positionBelongsTo(c, p10) && positionBelongsTo(c, p11) && positionBelongsTo(c, p23)
					&& positionBelongsTo(c, p03) && ((positionBelongsTo(c, p12) && !p13.isOccupied()
							|| (positionBelongsTo(c, p13) && !p12.isOccupied())))) {
				zwickMuehlen.add(new ZwickMuehle(p10, p11, p23, p03, p12, p13, c));
			}
			if (positionBelongsTo(c, p21) && positionBelongsTo(c, p22) && positionBelongsTo(c, p07)
					&& positionBelongsTo(c, p17) && ((positionBelongsTo(c, p20) && !p27.isOccupied()
							|| (positionBelongsTo(c, p27) && !p20.isOccupied())))) {
				zwickMuehlen.add(new ZwickMuehle(p21, p22, p07, p17, p20, p27, c));
			}
			if (positionBelongsTo(c, p20) && positionBelongsTo(c, p22) && positionBelongsTo(c, p10)
					&& positionBelongsTo(c, p12) && ((positionBelongsTo(c, p21) && !p11.isOccupied()
							|| (positionBelongsTo(c, p11) && !p21.isOccupied())))) {
				zwickMuehlen.add(new ZwickMuehle(p20, p22, p10, p12, p21, p11, c));
			}
			if (positionBelongsTo(c, p20) && positionBelongsTo(c, p21) && positionBelongsTo(c, p13)
					&& positionBelongsTo(c, p03) && ((positionBelongsTo(c, p22) && !p23.isOccupied()
							|| (positionBelongsTo(c, p23) && !p22.isOccupied())))) {
				zwickMuehlen.add(new ZwickMuehle(p20, p21, p13, p03, p22, p23, c));
			}
			if (positionBelongsTo(c, p11) && positionBelongsTo(c, p21) && positionBelongsTo(c, p07)
					&& positionBelongsTo(c, p06) && ((positionBelongsTo(c, p01) && !p00.isOccupied()
							|| (positionBelongsTo(c, p00) && !p01.isOccupied())))) {
				zwickMuehlen.add(new ZwickMuehle(p11, p21, p07, p06, p01, p00, c));
			}
			if (positionBelongsTo(c, p11) && positionBelongsTo(c, p21) && positionBelongsTo(c, p03)
					&& positionBelongsTo(c, p04) && ((positionBelongsTo(c, p01) && !p02.isOccupied()
							|| (positionBelongsTo(c, p02) && !p01.isOccupied())))) {
				zwickMuehlen.add(new ZwickMuehle(p11, p21, p03, p04, p01, p02, c));
			}
			if (positionBelongsTo(c, p01) && positionBelongsTo(c, p21) && positionBelongsTo(c, p17)
					&& positionBelongsTo(c, p16) && ((positionBelongsTo(c, p11) && !p10.isOccupied()
							|| (positionBelongsTo(c, p10) && !p11.isOccupied())))) {
				zwickMuehlen.add(new ZwickMuehle(p01, p21, p17, p16, p11, p10, c));
			}
			if (positionBelongsTo(c, p01) && positionBelongsTo(c, p21) && positionBelongsTo(c, p13)
					&& positionBelongsTo(c, p14) && ((positionBelongsTo(c, p11) && !p12.isOccupied()
							|| (positionBelongsTo(c, p12) && !p11.isOccupied())))) {
				zwickMuehlen.add(new ZwickMuehle(p01, p21, p13, p14, p11, p12, c));
			}
			if (positionBelongsTo(c, p01) && positionBelongsTo(c, p11) && positionBelongsTo(c, p27)
					&& positionBelongsTo(c, p26) && ((positionBelongsTo(c, p21) && !p20.isOccupied()
							|| (positionBelongsTo(c, p20) && !p21.isOccupied())))) {
				zwickMuehlen.add(new ZwickMuehle(p01, p11, p27, p26, p21, p20, c));
			}
			if (positionBelongsTo(c, p01) && positionBelongsTo(c, p11) && positionBelongsTo(c, p23)
					&& positionBelongsTo(c, p24) && ((positionBelongsTo(c, p21) && !p22.isOccupied()
							|| (positionBelongsTo(c, p22) && !p21.isOccupied())))) {
				zwickMuehlen.add(new ZwickMuehle(p01, p21, p17, p16, p11, p10, c));
			}

			//Nach jedem Viertel werden werden die relevanten Positionen um 2 nach rechts verschoben
			p00 = getPosition(0, p00.shift(2));
			p01 = getPosition(0, p01.shift(2));
			p02 = getPosition(0, p02.shift(2));
			p10 = getPosition(1, p10.shift(2));
			p11 = getPosition(1, p11.shift(2));
			p12 = getPosition(1, p12.shift(2));
			p20 = getPosition(2, p20.shift(2));
			p21 = getPosition(2, p21.shift(2));
			p22 = getPosition(2, p22.shift(2));
			p07 = getPosition(0, p07.shift(2));
			p17 = getPosition(1, p17.shift(2));
			p27 = getPosition(2, p27.shift(2));
			p23 = getPosition(2, p23.shift(2));
			p13 = getPosition(1, p13.shift(2));
			p03 = getPosition(0, p03.shift(2));
			p06 = getPosition(0, p06.shift(2));
			p04 = getPosition(0, p04.shift(2));
			p16 = getPosition(1, p16.shift(2));
			p14 = getPosition(1, p14.shift(2));
			p26 = getPosition(2, p26.shift(2));
			p24 = getPosition(2, p24.shift(2));
		}

		return zwickMuehlen;
	}

	/** Diese Methode liefert alle möglichen Spielzüge für die Farbe 'c' auf dem aktuellen Spielfeld
	 * 
	 * @param c Zu untersuchende Farbe
	 * @return Eine ArrayList mit allen Spielzügen für Farbe 'c'. Die ungeraden Indizes sind die Ausgangspositionen und die geraden Indizes sind die Zielpositionen 
	 * des Zuges
	 */
	public ArrayList<Position> getPossibleMovesForColor(Color c) {
		ArrayList<Position> moves = new ArrayList<>();
		ArrayList<Position> free = getFreePositions();
		ArrayList<Position> occupied = getOccupiedPositions();
		for (Position p : occupied) {
			if (p.getColor().equals(c)) {
				for (Position pos : free) {
					if (getTopNeighbor(p) != null) {
						if (pos.equals(getTopNeighbor(p))) {
							moves.add(p);
							moves.add(getTopNeighbor(p));
						}
					}
				}
				if (getRightNeighbor(p) != null) {
					for (Position pos : free) {
						if (pos.equals(getRightNeighbor(p))) {
							moves.add(p);
							moves.add(getRightNeighbor(p));
						}
					}
				}
				if (getBottomNeighbor(p) != null) {
					for (Position pos : free) {
						if (pos.equals(getBottomNeighbor(p))) {
							moves.add(p);
							moves.add(getBottomNeighbor(p));
						}
					}
				}
				if (getLeftNeighbor(p) != null) {
					for (Position pos : free) {
						if (pos.equals(getLeftNeighbor(p))) {
							moves.add(p);
							moves.add(getLeftNeighbor(p));
						}
					}
				}
			}
		}
		return moves;
	}

	/** Gibt einen Boolean Wert zurück, der aussagt, ob die Position 'p' zu der Farbe 'c' gehört oder nicht
	 *  
	 * @param c Farbe, zu der der Stein gehören soll
	 * @param p Position, die zu untersuchen ist
	 * @return Boolean; true, wenn 'p' von Farbe 'c' besetzt wird; false, wenn nicht
	 */
	public boolean positionBelongsTo(Color c, Position p) {
		return getPosition(p.getRing(), p.getPosition()).isOccupied()
				&& getPosition(p.getRing(), p.getPosition()).getColor().equals(c);
	}

	/** Führt einen Zug auf dem Spielbrett durch
	 * 
	 * @param from Ausgangsposition des Spielzuges
	 * @param to Zielposition des Spielzuges
	 */
	public void performDraw(Position from, Position to) {
		if (from != null) {
			setPosition(from.getRing(), from.getPosition(), from.getColor(), false);
		}
		if (to != null) {
			setPosition(to.getRing(), to.getPosition(), from.getColor(), true);
		}
	}
	
	/** Diese Funktion liefert die Anzahl der Steine, die auf dem Spielfeld von der Farbe 'c' zu finden sind
	 * 
	 * @param c Die betrachtete Farbe
	 * @return Die Anzahl der Steine
	 */
	public int getNumberOfStonesOfColor(Color c){
		int amount = 0;
		for(Position p : getOccupiedPositions()){
			if(p.getColor().equals(c)){
				amount++;
			}
		}
		return amount;
	}

	/**
	 * Diese Methode setzt das Spielfeld zurück; leert es
	 */
	public void reset(){
		for (int ring = 0; ring <= 2; ring++) {
			for (int position = 0; position <= 7; position++) {
				rings[ring][position] = new Position(position, ring);
			}
		}
	}

}
