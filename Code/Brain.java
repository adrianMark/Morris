import java.awt.Color;
import java.util.ArrayList;

public class Brain {

	/**
	 * Diese Klasse bildet die Logik des Computers. Sie bietet Methoden für die
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
	 * Diese Methode setzt einen Stein für den Computer auf Basis des
	 * übergebenen Boards 'b'
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
	 * Diese Funktion wählt aus allen möglichen Positionen, auf die ein Stein
	 * gesetzt werden kann, die Position aus, die den größten Mehrwert für die
	 * Farbe 'c' liefert.
	 * 
	 * @param b
	 *            Das Board, das betrachtet wird
	 * @param c
	 *            Die Farbe, für die die beste Position berechnet werden soll.
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
				// geldaden, aus der am Ende zufällig eine Position ausgewählt
				// wird
			} else {
				continue;
			}
		}
		for (Position pos : same) {
			// Präferiert werden Positionen, die bereits einen gleichfarbigen
			// Nachbarn haben, sodass eine Mühle
			// möglich wird.
			ArrayList<Position> neighbors = b.getAllNeighbors(pos);
			for (Position p : neighbors) {
				if (p.isOccupied() && p.getColor().equals(c)) {
					sameWithNeighbor.add(pos);
				}
			}
		}
		if (sameWithNeighbor.size() > 0) {
			// Eine Zufallsposition mit gleichfarbigem Nachbarn wird ausgewählt
			return sameWithNeighbor.get(generateRandomNumberBetween(0, sameWithNeighbor.size() - 1));
		} else {
			// Gibt es keine Nachbarn, wird eine aus dem Pool der besten
			// Positionen ausgewählt
			return same.get(generateRandomNumberBetween(0, same.size() - 1));
		}
	}

	/**
	 * Diese Funktion berechnet einen Ganzzahlwert, der aussagt wie viel
	 * Mehrwert es fpr eine Farbe 'c' bringen würde einen Stein auf die Position
	 * 'p' des Boards 'b' zu setzen.
	 * 
	 * @param c
	 *            Die betrachtete Farbe
	 * @param b
	 *            Das Board für das die Position bewertet werden soll
	 * @param p
	 *            Die Position, die bewertet wird
	 * @return Ein Integer Wert, der die Position bewertet
	 */
	public int evaluateStoneSetting(Color c, Board b, Position p) {
		int value = 0;
		// Wie viele Mühlen/Zwickmühlen waren vor dem Zug auf dem Feld
		ArrayList<Muehle> myMuehlenBefore = b.getMuehlen(c);
		ArrayList<ZwickMuehle> myZMuehlenBefore = b.getZwickMuehlen(c);

		// Kopie des Boards anfertigen und Zug simulieren
		Board copy = copyBoard(b);
		copy.setPosition(p.getRing(), p.getPosition(), c, true);

		// Wie viele Mühlen Zwickmühlen sind danach auf dem Feld
		ArrayList<Muehle> myMuehlenAfter = copy.getMuehlen(c);
		ArrayList<ZwickMuehle> myZMuehlenAfter = copy.getZwickMuehlen(c);

		// Der Zug hat eine Mühle erzeugt (Plus 20 Punkte)
		if (myMuehlenAfter.size() > myMuehlenBefore.size()) {
			value = value + 20;
		}

		// Der Zug hat eine Zwickmühle erzeugt (Plus 10 Punkte)
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
	 *            Wenn true gesetzt, wird überprüft, ob der Gegner mit seinem
	 *            nächsten Zug eine geöffnete Mühle zerstören kann
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

		// Mühlen/ZWickmühlen vor dem Zug speichern
		ArrayList<ZwickMuehle> zwickMuehlenBeforeThisColor = b.getZwickMuehlen(c);
		ArrayList<Muehle> muehlenBeforeThisColor = b.getMuehlen(c);
		ArrayList<ZwickMuehle> zwickMuehlenBeforeEnemy = b.getZwickMuehlen(enemyColor);
		ArrayList<Muehle> muehlenBeforeEnemy = b.getMuehlen(enemyColor);

		// Board kopieren und Zug simulieren
		Board copy = copyBoard(b);
		copy.performDraw(from, to);

		// Mühlen nach dem Zug speichern
		ArrayList<ZwickMuehle> zwickMuehlenAfterThisColor = copy.getZwickMuehlen(c);
		ArrayList<Muehle> muehlenAfterThisColor = copy.getMuehlen(c);
		ArrayList<ZwickMuehle> zwickMuehlenAfterEnemy = copy.getZwickMuehlen(enemyColor);
		ArrayList<Muehle> muehlenAfterEnemy = copy.getMuehlen(enemyColor);

		// Fall 1: Der Zug hat eine eigene Mühle erstellt (plus 7 Punkte)
		if (muehlenBeforeThisColor.size() < muehlenAfterThisColor.size()) {
			value = value + 7;
		}

		// Fall 2: Der Zug hat eine eigene Zwickmühle erstellt (plus 9 Punkte)
		if (zwickMuehlenBeforeThisColor.size() < zwickMuehlenAfterThisColor.size()) {
			value = value + 9;
		}

		// Fall 3: Der Zug hat eine gegnerische Zwickmühle blockiert (Plus 6
		// Punkte)
		if (zwickMuehlenBeforeEnemy.size() > zwickMuehlenAfterEnemy.size()) {
			value = value + 6;
		}

		// Fall 4: Der Zug hat eine gegnerische Zwickmühle befreit (Minus 20
		// Punkte)
		if (zwickMuehlenAfterEnemy.size() > zwickMuehlenBeforeEnemy.size()) {
			value = value - 20;

		}

		// Fall 5: Der Zug hat eine eigene Zwickmühle zerstört (Minus 20 Punkte)
		if (zwickMuehlenBeforeThisColor.size() > zwickMuehlenAfterThisColor.size()) {
			value = value - 20;
		}

		// Fall 6: Diese Farbe öffnet eine Mühle und im nächsten Zug kann der
		// Gegner eine Mühle schließen
		// --> Gefahr, dass eigene Mühle zerstört wird (Minus 100 Punkte)
		if (recursion) {
			if (muehlenAfterThisColor.size() < muehlenBeforeThisColor.size()) {
				Position[] enemyNextMove = getBestDraw(copy, enemyColor, false);
				int impact = evaluateDraw(copy, enemyNextMove[0], enemyNextMove[1], enemyColor, false);
				if (impact == 7) {
					value = value - 100;
				}
			}
		}

		// Fall 7: Der Zug hat eine gegnerische Mühle blockiert (Plus 30 Punkte)
		for (Muehle mBefore : muehlenBeforeEnemy) {
			for (Muehle mAfter : muehlenAfterEnemy) {
				if (mBefore.equals(mAfter) && mBefore.isFree(b) && !mAfter.isFree(copy)) {
					value = value + 30;
				}
			}
		}

		// Fall 8: Der Zug nutzt eine eigene Zwickmühle (Plus 10 Punkte)
		for (ZwickMuehle zmNow : zwickMuehlenAfterThisColor) {
			if (zmNow.wasUsed(b, copy)) {
				value = value + 10;
			}
		}
		return value;
	}

	/**
	 * Diese Methode iteriert über alle möglichen Züge der Farbe 'c' und gibt
	 * ihnen eine Wertung mit Hilfe der evaluateDraw Methode. So kann der Beste
	 * Zug für die nächste Runde bestimmt werden
	 * 
	 * @param b
	 *            Das Board, für das der beste Zug gesucht wird
	 * @param c
	 *            Die Farbe, für die der beste Zug gesucht wird
	 * @param recursion
	 *            Wird, wenn nötig an die evaluateDraw Methode weitergegeben und
	 *            sorgt dort für zusätzliche Tiefe in der Berechnung
	 * @return Ein Array mit dem besten Zug (Index 0: Ausgangsposition; Index 1:
	 *         Zielposition)
	 */
	public Position[] getBestDraw(Board b, Color c, boolean recursion) {
		Position[] bestDraw = new Position[2];

		// Alle Züge für die Farbe 'c'
		ArrayList<Position> possible = b.getPossibleMovesForColor(c);
		int best = -1000000;

		Position from = null;
		Position to = null;

		// Jedem Möglichen Zug wird ein Wert zugewiesen und der beste Zug wird
		// ausgewählt
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
	 * entscheided sich für den Pfad mit den besten Spielaussichten. Es handelt
	 * sich dabei um einen Minmax Algorithmus, der abwechselnd die Züge vom
	 * Spieler und dem Computer simuliert und auf dieser Basis den besten Zug
	 * auswählt.
	 * 
	 * @param difficulty
	 *            Die Schwierigkeitsstufe und Anzahl der Runden, die der
	 *            Computer simuliert.
	 * @param b
	 *            Das Board, auf dem gespielt werden soll
	 * @return Der ausgeführte Zug (Index 0: Ausgangsposition; Index 1:
	 *         Zielposition)
	 */
	public Position[] performBestDraw(int difficulty, Board b) {
		// Alle eigenen Zugoptionen bestimmen
		ArrayList<Position> nowPossible = b.getPossibleMovesForColor(black);
		Position[] move = new Position[2];

		// Wenn der Computer eine Zwickmühle/Mühle nutzen kann, tut er es sofort
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
			// Andernfalls, führt er den Minmax Algorithmus aus und bestimmt
			// abwechselnd die nächsten Züge für
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
				// Der beste Pfad wird ausgewählt und der Zug ausgeführt
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
	 * springen. Diese Methode hilft dabei, die beste Position für einen solchen
	 * Sprung auszuwählen.
	 * 
	 * @param b
	 *            Das Board auf dem gesprungen wird
	 * @return Der Spielzug, der ausgeführt wurde (Index 0: Ausgangsposition;
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
		// Der beste Sprung wird ausgeführt
		b.performDraw(best[0], best[1]);
		return best;
	}
	// ------------------------------------------------------------------

	/**
	 * Diese Methode wählt für die Farbe 'c' den Stein des Gegners aus, der sich
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
		// Die gegnerischen Steine bestimmen, die entfernt werden dürfen (die
		// nicht in Mühlen sind)
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

			// Können Zwickmühlen des Gegners entfernt werden
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

			// Können gegnerische Mühlen verhindert werden
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
			// In Phase eins (Ziehphase) werden Züge simuliert
			Position nextMoveFrom = getBestDraw(b, enemyColor, true)[0];

			// Der nächste beste Zug des Gegners wird verhindert
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
	 * Diese Methode liefert eine zufällige Zahl in einem Bereich von min bis
	 * max. Sie wird für das zufällige Auswählen von Positionen verwendet
	 * 
	 * @param min
	 *            Untere Grenze des gewünschten Zahlenbereichs
	 * @param max
	 *            Obere Grenze des gewünschten Zahlenbereichts
	 * @return Eine zufällige Zahl im gegenbenen Bereich
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

	/** Methode zum kopieren von Board Instanzen. Wird verwendet um Züge zu simulieren, ohne das eigentliche
	 * Board zu verändern
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