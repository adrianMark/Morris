import java.awt.Color;

public class Position {
	/** Diese Klasse repräsentiert eine Position auf dem Spielfeld. Insgesamt besteht das Feld aus 24 dieser Positionen
	 * Ihre Position auf dem Spielfeld wird durch einen Ring und eine Position auf diesem Ring festgelegt (genaueres ist der Dokumentation zu entnehmen).
	 * Desweiteren haben sie eine Farbe (Schwarz oder Weiß) und sind durch einen Stein belegt (occupied) oder frei.
	 */
	
	private int position; 
	private int ring;
	private Color color = new Color(0,0,0);
	private boolean isOccupied = false;

	//Konstruktor - Alle Positionen werden als frei und schwarz initialisiert
	public Position(int position, int ring) {
		this.position = position;
		this.ring = ring;
	}

	//Getter und Setter Methoden
	public int getPosition() {
		return position;
	}

	public void setPosition(int position) {
		this.position = position;
	}

	public int getRing() {
		return ring;
	}

	public void setRing(int ring) {
		this.ring = ring;
	}

	public Color getColor() {
		return color;
	}

	public void setColor(Color color) {
		this.color = color;
	}

	public boolean isOccupied() {
		return isOccupied;
	}

	public void setOccupied(boolean isOccupied) {
		this.isOccupied = isOccupied;
	}
	//---------------------------------------------------------
	
	/** Diese Methode gibt die Position zurück, die sich 'shift'-Positionen neben dieser Instanz auf dem Board befindet.
	 * Sie wird genutzt, um Zwickmühlen auf dem Spielfeld zu finden.
	 * @param shift Den gewünschten Abstand zu dieser Instanz 
	 * @return Die Position mit dem Abstand 'shift' zu dieser Position 
	 */
	public int shift(int shift){
		int newPos = this.position;
		for (int counter = 1; counter <= shift; counter++){
			newPos++;
			if(newPos == 8){
				newPos = 0;
			}
		}
		return newPos;
	}
	
	//toString Methode
	public String toString(){
		return this.ring +","+this.position;
	}
	
	//Equals Methode
	public boolean equals(Position p){
		return this.getColor().equals(p.getColor()) && this.ring == p.ring && this.position == p.position && this.isOccupied == p.isOccupied;
	}
	
}

