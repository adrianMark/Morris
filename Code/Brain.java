import java.awt.Color;
import java.util.ArrayList;

public class Brain {

	/**
	 * Diese Klasse bildet die Logik des Computers. Sie bietet Methoden f�r die
	 * drei Phasen des Spiels an (Setzen, Ziehen, Springen) und nutzt dabei die
	 * Analysemethoden der Board Klasse.
	 */

	private Color white = new Color(0, 0, 0);
	private Color black = new Color(255, 255, 255);
	private int phase = 0;
	private MuehleGUI gui;

	// Getter und Setter Methoden
	public int getPhase() {
		return phase;
	}

	public void setPhase(int phase) {
		this.phase = phase;
	}

	public MuehleGUI getGui() {
		return gui;
	}

	public void setGui(MuehleGUI gui) {
		this.gui = gui;
	}
	// ------------------------------------------------------------------

	// ###################SETZPHASE (PHASE 1)############################
	/**
	 * Diese Methode setzt einen Stein f�r den Computer auf Basis des
	 * �bergebenen Boards 'b'
	 * 
	 * @param b
	 *            Eine Board Instanz
	 * @return Die Position auf die der Stein gesetzt wurde
	 */
	public Position setBestStone(Board b) {
		// Berechnet die beste Position um einen eigenen Stein zu setzen
		Position my = getBestPositionToSetStone(b, black);
		// Berechnet die beste Position auf die der Gegner einen Stein setzen
		// kann
		Position enemy = getBestPositionToSetStone(b, white);

		// Beiden Positionen werden Werte zugewiesen
		int myValue = evaluateStoneSetting(black, b, my);
		int enemyValue = evaluateStoneSetting(white, b, enemy);

		// Wenn der gegnerische Zug mehr Wert hat als der eigene, dann wird
		// diese Stelle besetzt,
		// ansonsten die eigene
		if (myValue < enemyValue) {
			b.setPosition(enemy.getRing(), enemy.getPosition(), black, true);
			return enemy;
		} else {
			b.setPosition(my.getRing(), my.getPosition(), black, true);
			return my;
		}

	}

	/**
	 * Diese Funktion w�hlt aus allen m�glichen Positionen, auf die ein Stein
	 * gesetzt werden kann, die Position aus, die den gr��ten Mehrwert f�r die
	 * Farbe 'c' liefert.
	 * 
	 * @param b
	 *            Das Board, das betrachtet wird
	 * @param c
	 *            Die Farbe, f�r die die beste Position berechnet werden soll.
	 * @return Die Position, die sich am besten eignet, um einen Stein zu platzieren
	 */
	public Position getBestPositionToSetStone(Board b, Color c) {
		ArrayList<Position> free = b.getFreePositions();
		ArrayList<Position> same = new ArrayList<>();
		ArrayList<Position> sameWithNeighbor = new ArrayList<>();
		int bestValue = -1000;
		for (Position p : free) {
			// Jeder freien Position wird ein Wert zugewiesen
			int value = evaluateStoneSetting(c, b, p);
			if (value > bestValue) {
				bestValue = value;
				same = new ArrayList<>();
				same.add(p);
			} else if (value == bestValue) {
				same.add(p);
				// wenn sie den selben Wert, wie die beste Position aufweist,
				// wird sie in eine ArrayList
				// geldaden, aus der am Ende zuf�llig eine Position ausgew�hlt
				// wird
			} else {
				continue;
			}
		}
		for (Position pos : same) {
			// Pr�feriert werden Positionen, die bereits einen gleichfarbigen
			// Nachbarn haben, sodass eine M�hle
			// m�glich wird.
			ArrayList<Position> neighbors = b.getAllNeighbors(pos);
			for (Position p : neighbors) {
				if (p.isOccupied() && p.getColor().equals(c)) {
					sameWithNeighbor.add(pos);
				}
			}
		}
		if (sameWithNeighbor.size() > 0) {
			// Eine Zufallsposition mit gleichfarbigem Nachbarn wird ausgew�hlt
			return sameWithNeighbor.get(generateRandomNumberBetween(0, sameWithNeighbor.size() - 1));
		} else {
			// Gibt es keine Nachbarn, wird eine aus dem Pool der besten
			// Positionen ausgew�hlt
			return same.get(generateRandomNumberBetween(0, same.size() - 1));
		}
	}

	/**
	 * Diese Funktion berechnet einen Ganzzahlwert, der aussagt wie viel
	 * Mehrwert es fpr eine Farbe 'c' bringen w�rde einen Stein auf die Position
	 * 'p' des Boards 'b' zu setzen.
	 * 
	 * @param c
	 *            Die betrachtete Farbe
	 * @param b
	 *            Das Board f�r das die Position bewertet werden soll
	 * @param p
	 *            Die Position, die bewertet wird
	 * @return Ein Integer Wert, der die Position bewertet
	 */
	public int evaluateStoneSetting(Color c, Board b, Position p) {
		int value = 0;
		// Wie viele M�hlen/Zwickm�hlen waren vor dem Zug auf dem Feld
		ArrayList<Muehle> myMuehlenBefore = b.getMuehlen(c);
		ArrayList<ZwickMuehle> myZMuehlenBefore = b.getZwickMuehlen(c);

		// Kopie des Boards anfertigen und Zug simulieren
		Board copy = copyBoard(b);
		copy.setPosition(p.getRing(), p.getPosition(), c, true);

		// Wie viele M�hlen Zwickm�hlen sind danach auf dem Feld
		ArrayList<Muehle> myMuehlenAfter = copy.getMuehlen(c);
		ArrayList<ZwickMuehle> myZMuehlenAfter = copy.getZwickMuehlen(c);

		// Der Zug hat eine M�hle erzeugt (Plus 20 Punkte)
		if (myMuehlenAfter.size() > myMuehlenBefore.size()) {
			value = value + 20;
		}

		// Der Zug hat eine Zwickm�hle erzeugt (Plus 10 Punkte)
		if (myZMuehlenBefore.size() < myZMuehlenAfter.size()) {
			value = value + 10;
		}

		return value;
	}

	// ------------------------------------------------------------------

	// ###################ZIEHPHASE (PHASE 2)############################
	/**
	 * Diese Methode bewertet den Zug von Position 'from' nach Position 'to' aus
	 * der Perspektive von Farbe 'c'
	 * 
	 * @param b
	 *            Das Board auf dem der Zug bewertet werden soll
	 * @param from
	 *            Die Augangsposition des Spielzuges
	 * @param to
	 *            Die Zielposition des Spielzuges
	 * @param c
	 *            Die Farbe aus deren Perspektive der Zug bewertet werden soll.
	 * @param recursion
	 *            Wenn true gesetzt, wird �berpr�ft, ob der Gegner mit seinem
	 *            n�chsten Zug eine ge�ffnete M�hle zerst�ren kann
	 * @return Eine Ganzzahl, die den Zug bewertet
	 */
	public int evaluateDraw(Board b, Position from, Position to, Color c, boolean recursion) {
		Color enemyColor;
		// Gegnerische Farbe feststellen
		if (c.equals(black)) {
			enemyColor = white;
		} else {
			enemyColor = black;
		}

		int value = 0;

		// M�hlen/ZWickm�hlen vor dem Zug speichern
		ArrayList<ZwickMuehle> zwickMuehlenBeforeThisColor = b.getZwickMuehlen(c);
		ArrayList<Muehle> muehlenBeforeThisColor = b.getMuehlen(c);
		ArrayList<ZwickMuehle> zwickMuehlenBeforeEnemy = b.getZwickMuehlen(enemyColor);
		ArrayList<Muehle> muehlenBeforeEnemy = b.getMuehlen(enemyColor);

		// Board kopieren und Zug simulieren
		Board copy = copyBoard(b);
		copy.performDraw(from, to);

		// M�hlen nach dem Zug speichern
		ArrayList<ZwickMuehle> zwickMuehlenAfterThisColor = copy.getZwickMuehlen(c);
		ArrayList<Muehle> muehlenAfterThisColor = copy.getMuehlen(c);
		ArrayList<ZwickMuehle> zwickMuehlenAfterEnemy = copy.getZwickMuehlen(enemyColor);
		ArrayList<Muehle> muehlenAfterEnemy = copy.getMuehlen(enemyColor);

		// Fall 1: Der Zug hat eine eigene M�hle erstellt (plus 7 Punkte)
		if (muehlenBeforeThisColor.size() < muehlenAfterThisColor.size()) {
			value = value + 7;
		}

		// Fall 2: Der Zug hat eine eigene Zwickm�hle erstellt (plus 9 Punkte)
		if (zwickMuehlenBeforeThisColor.size() < zwickMuehlenAfterThisColor.size()) {
			value = value + 9;
		}

		// Fall 3: Der Zug hat eine gegnerische Zwickm�hle blockiert (Plus 6
		// Punkte)
		if (zwickMuehlenBeforeEnemy.size() > zwickMuehlenAfterEnemy.size()) {
			value = value + 6;
		}

		// Fall 4: Der Zug hat eine gegnerische Zwickm�hle befreit (Minus 20
		// Punkte)
		if (zwickMuehlenAfterEnemy.size() > zwickMuehlenBeforeEnemy.size()) {
			value = value - 20;

		}

		// Fall 5: Der Zug hat eine eigene Zwickm�hle zerst�rt (Minus 20 Punkte)
		if (zwickMuehlenBeforeThisColor.size() > zwickMuehlenAfterThisColor.size()) {
			value = value - 20;
		}

		// Fall 6: Diese Farbe �ffnet eine M�hle und im n�chsten Zug kann der
		// Gegner eine M�hle schlie�en
		// --> Gefahr, dass eigene M�hle zerst�rt wird (Minus 100 Punkte)
		if (recursion) {
			if (muehlenAfterThisColor.size() < muehlenBeforeThisColor.size()) {
				Position[] enemyNextMove = getBestDraw(copy, enemyColor, false);
				int impact = evaluateDraw(copy, enemyNextMove[0], enemyNextMove[1], enemyColor, false);
				if (impact == 7) {
					value = value - 100;
				}
			}
		}

		// Fall 7: Der Zug hat eine gegnerische M�hle blockiert (Plus 30 Punkte)
		for (Muehle mBefore : muehlenBeforeEnemy) {
			for (Muehle mAfter : muehlenAfterEnemy) {
				if (mBefore.equals(mAfter) && mBefore.isFree(b) && !mAfter.isFree(copy)) {
					value = value + 30;
				}
			}
		}

		// Fall 8: Der Zug nutzt eine eigene Zwickm�hle (Plus 10 Punkte)
		for (ZwickMuehle zmNow : zwickMuehlenAfterThisColor) {
			if (zmNow.wasUsed(b, copy)) {
				value = value + 10;
			}
		}
		return value;
	}

	/**
	 * Diese Methode iteriert �ber alle m�glichen Z�ge der Farbe 'c' und gibt
	 * ihnen eine Wertung mit Hilfe der evaluateDraw Methode. So kann der Beste
	 * Zug f�r die n�chste Runde bestimmt werden
	 * 
	 * @param b
	 *            Das Board, f�r das der beste Zug gesucht wird
	 * @param c
	 *            Die Farbe, f�r die der beste Zug gesucht wird
	 * @param recursion
	 *            Wird, wenn n�tig an die evaluateDraw Methode weitergegeben und
	 *            sorgt dort f�r zus�tzliche Tiefe in der Berechnung
	 * @return Ein Array mit dem besten Zug (Index 0: Ausgangsposition; Index 1:
	 *         Zielposition)
	 */
	public Position[] getBestDraw(Board b, Color c, boolean recursion) {
		Position[] bestDraw = new Position[2];

		// Alle Z�ge f�r die Farbe 'c'
		ArrayList<Position> possible = b.getPossibleMovesForColor(c);
		int best = -1000000;

		Position from = null;
		Position to = null;

		// Jedem M�glichen Zug wird ein Wert zugewiesen und der beste Zug wird
		// ausgew�hlt
		for (int i = 0; i < possible.size(); i = i + 2) {
			int drawValue = evaluateDraw(b, possible.get(i), possible.get(i + 1), c, recursion);
			if (drawValue > best) {
				best = drawValue;
				from = b.getPosition(possible.get(i).getRing(), possible.get(i).getPosition());
				to = b.getPosition(possible.get(i + 1).getRing(), possible.get(i + 1).getPosition());
			}
		}

		bestDraw[0] = from;
		bestDraw[1] = to;
		return bestDraw;
	}

	/**
	 * Diese Methode berechnet Zugszenarien bis zu einer variablen Tiefe und
	 * entscheided sich f�r den Pfad mit den besten Spielaussichten. Es handelt
	 * sich dabei um einen Minmax Algorithmus, der abwechselnd die Z�ge vom
	 * Spieler und dem Computer simuliert und auf dieser Basis den besten Zug
	 * ausw�hlt.
	 * 
	 * @param difficulty
	 *            Die Schwierigkeitsstufe und Anzahl der Runden, die der
	 *            Computer simuliert.
	 * @param b
	 *            Das Board, auf dem gespielt werden soll
	 * @return Der ausgef�hrte Zug (Index 0: Ausgangsposition; Index 1:
	 *         Zielposition)
	 */
	public Position[] performBestDraw(int difficulty, Board b) {
		// Alle eigenen Zugoptionen bestimmen
		ArrayList<Position> nowPossible = b.getPossibleMovesForColor(black);
		Position[] move = new Position[2];

		// Wenn der Computer eine Zwickm�hle/M�hle nutzen kann, tut er es sofort
		if (b.getZwickMuehlen(black).size() > 0) {
			move = b.getZwickMuehlen(black).get(0).use(b);
			return move;
		} else {
			for (int i = 0; i < nowPossible.size() - 1; i = i + 2) {
				Board copy = copyBoard(b);
				int before = copy.getMuehlen(black).size();
				copy.performDraw(nowPossible.get(i), nowPossible.get(i + 1));
				int after = copy.getMuehlen(black).size();
				if (after > before) {
					b.performDraw(nowPossible.get(i), nowPossible.get(i + 1));
					move[0] = nowPossible.get(i);
					move[1] = nowPossible.get(i + 1);
					return move;
				}
			}
			// Andernfalls, f�hrt er den Minmax Algorithmus aus und bestimmt
			// abwechselnd die n�chsten Z�ge f�r
			// Computer und Spieler (Spieler Werte werden subtrahiert, da sie
			// schaden; eigene Werte werden addiert)
			// Das ganze geschieht bis zu einer gewissen Tiefe (Difficulty)
			int bestPath = -10000000;
			Position bestFrom = null;
			Position bestTo = null;
			for (int i = 0; i < nowPossible.size() - 1; i = i + 2) {
				int value = 0;
				Position evaluateFrom = nowPossible.get(i);
				Position evaluateTo = nowPossible.get(i + 1);
				value = value + evaluateDraw(b, evaluateFrom, evaluateTo, black, true);
				Board simulation = copyBoard(b);
				simulation.performDraw(evaluateFrom, evaluateTo);
				for (int depth = 1; depth <= difficulty; depth++) {
					Position[] enemyNext = getBestDraw(simulation, white, true);
					if (enemyNext[0] != null && enemyNext[1] != null) {
						Position enemyFrom = enemyNext[0];
						Position enemyTo = enemyNext[1];
						value = value - evaluateDraw(simulation, enemyFrom, enemyTo, white, true);
						simulation.performDraw(enemyFrom, enemyTo);
					} else {
						break;
					}

					Position[] myNext = getBestDraw(simulation, black, true);
					if (myNext[0] != null && myNext[1] != null) {
						Position myFrom = myNext[0];
						Position myTo = myNext[1];
						value = value + evaluateDraw(simulation, myFrom, myTo, black, true);
						simulation.performDraw(myFrom, myTo);
					} else {
						break;
					}
				}
				// Der beste Pfad wird ausgew�hlt und der Zug ausgef�hrt
				if (value > bestPath) {
					bestPath = value;
					bestFrom = evaluateFrom;
					bestTo = evaluateTo;
				}
			}
			move[0] = bestFrom;
			move[1] = bestTo;
			b.performDraw(b.getPosition(bestFrom.getRing(), bestFrom.getPosition()),
					b.getPosition(bestTo.getRing(), bestTo.getPosition()));
			return move;
		}
	}

	// ------------------------------------------------------------------

	// ###################SPRINGPHASE (PHASE 3)############################
	/**
	 * Wenn ein Spieler nur noch drei Spielsteine hat, darf er mit ihnen
	 * springen. Diese Methode hilft dabei, die beste Position f�r einen solchen
	 * Sprung auszuw�hlen.
	 * 
	 * @param b
	 *            Das Board auf dem gesprungen wird
	 * @return Der Spielzug, der ausgef�hrt wurde (Index 0: Ausgangsposition;
	 *         Index 1: Zielposition)
	 */
	public Position[] jump(Board b) {
		ArrayList<Position> free = b.getFreePositions();
		ArrayList<Position> occupied = b.getOccupiedPositions();
		ArrayList<Position> computerPosition = new ArrayList<>();
		// Eigene Steine aus allen besetzten Positionen filtern
		for (Position p : occupied) {
			if (p.getColor().equals(black)) {
				computerPosition.add(p);
			}
		}

		int value = -10000;
		Position[] best = getBestDraw(b, black, true);

		// Jeden Sprung von einer belegten auf eine freie Position bewerten und
		// den Besten speichern
		for (Position p : computerPosition) {
			for (Position possible : free) {
				int thisValue = evaluateDraw(b, p, possible, black, true);
				if (thisValue > value) {
					value = thisValue;
					best[0] = p;
					best[1] = possible;
				}
			}
		}
		// Der beste Sprung wird ausgef�hrt
		b.performDraw(best[0], best[1]);
		return best;
	}
	// ------------------------------------------------------------------

	/**
	 * Diese Methode w�hlt f�r die Farbe 'c' den Stein des Gegners aus, der sich
	 * am besten eignet um ihn zu entfernen
	 * 
	 * @param b
	 *            Das Board im aktuellen Zustand
	 * @param c
	 *            Die Farbe / Perspektive
	 * @return Die Position des entfernten Steins
	 */
	public Position removeBestStone(Board b, Color c) {

		// Gegnerische Farbe bestimmen
		Color enemyColor;

		if (c.equals(black)) {
			enemyColor = white;
		} else {
			enemyColor = black;
		}

		ArrayList<Position> occupied = b.getOccupiedPositions();
		ArrayList<Position> enemyPositions = new ArrayList<>();
		ArrayList<Position> removableEnemies = new ArrayList<>();

		for (Position posi : occupied) {
			if (posi.getColor().equals(enemyColor)) {
				enemyPositions.add(posi);
			}
		}
		// Die gegnerischen Steine bestimmen, die entfernt werden d�rfen (die
		// nicht in M�hlen sind)
		for (Position pos : enemyPositions) {
			boolean removable = true;
			for (Muehle m : b.getMuehlen(enemyColor)) {
				for (Position p : m.getPositions()) {
					if (p.equals(pos)) {
						removable = false;
					}
				}
			}
			if (removable) {
				removableEnemies.add(pos);
			}
		}

		// Je nach Spielphase ist die Entscheidung verschieden
		if (phase == 0) {
			// Setzphase!
			Position enemyPos = getBestPositionToSetStone(b, enemyColor);
			Board simulation = copyBoard(b);
			simulation.setPosition(enemyPos.getRing(), enemyPos.getPosition(), enemyColor, true);

			ArrayList<Muehle> muehlen = simulation.getMuehlen(enemyColor);
			ArrayList<ZwickMuehle> zMuehlen = simulation.getZwickMuehlen(enemyColor);

			// K�nnen Zwickm�hlen des Gegners entfernt werden
			for (ZwickMuehle zm : zMuehlen) {
				ArrayList<Position> fixedZMPos = zm.getFixedPositions();
				for (Position po : fixedZMPos) {
					for (Position p : removableEnemies) {
						if (p.equals(po)) {
							b.setPosition(po.getRing(), po.getPosition(), black, false);
							return po;
						}
					}
				}
			}

			// K�nnen gegnerische M�hlen verhindert werden
			for (Muehle m : muehlen) {
				ArrayList<Position> millPositions = m.getPositions();
				for (Position po : millPositions) {
					for (Position pos : removableEnemies) {
						if (pos.equals(po)) {
							b.setPosition(pos.getRing(), pos.getPosition(), pos.getColor(), false);
							return pos;
						}
					}
				}
			}
			Position toRemove = removableEnemies.get(generateRandomNumberBetween(0, removableEnemies.size() - 1));
			b.setPosition(toRemove.getRing(), toRemove.getPosition(), toRemove.getColor(), false);
			return toRemove;

		} else if (phase == 1) {
			// In Phase eins (Ziehphase) werden Z�ge simuliert
			Position nextMoveFrom = getBestDraw(b, enemyColor, true)[0];

			// Der n�chste beste Zug des Gegners wird verhindert
			for (Position p : removableEnemies) {
				if (p.equals(nextMoveFrom)) {
					b.setPosition(nextMoveFrom.getRing(), nextMoveFrom.getPosition(), black, false);
					return nextMoveFrom;
				}
			}
		}
		Position toRemove = removableEnemies.get(generateRandomNumberBetween(0, removableEnemies.size() - 1));
		b.setPosition(toRemove.getRing(), toRemove.getPosition(), toRemove.getColor(), false);
		return toRemove;
	}

	// Hilfsmethoden
	/**
	 * Diese Methode liefert eine zuf�llige Zahl in einem Bereich von min bis
	 * max. Sie wird f�r das zuf�llige Ausw�hlen von Positionen verwendet
	 * 
	 * @param min
	 *            Untere Grenze des gew�nschten Zahlenbereichs
	 * @param max
	 *            Obere Grenze des gew�nschten Zahlenbereichts
	 * @return Eine zuf�llige Zahl im gegenbenen Bereich
	 */
	public int generateRandomNumberBetween(int min, int max) {
		int multi = 10;
		int rand = -1;
		if (max < 10) {
			multi = 10;
		}
		if (max >= 10) {
			multi = 100;
		}
		while (rand > max || rand < min) {
			rand = (int) (Math.random() * multi);
		}
		return rand;
	}

	/** Methode zum kopieren von Board Instanzen. Wird verwendet um Z�ge zu simulieren, ohne das eigentliche
	 * Board zu ver�ndern
	 * @param b Das zu kopierende Board
	 * @return Eine neue Instanz der Board Klasse mit der selben Belegung der Positionen wie 'b'
	 */
	public Board copyBoard(Board b) {
		Board copy = new Board();
		for (Position p : b.getOccupiedPositions()) {
			copy.setPosition(p.getRing(), p.getPosition(), p.getColor(), true);
		}
		return copy;
	}

}