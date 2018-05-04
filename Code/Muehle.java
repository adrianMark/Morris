import java.awt.Color;
import java.util.ArrayList;

public class Muehle {
	
	/**Diese Klasse repräsentiert eine Mühle auf dem Spielfeld. Sie besteht aus drei Positionen, die für die leichtere Verwaltung zusätzlich in einer ArrayList 
	 * ArrayList gespeichert werden. Außerdem gehört sie zu einer Farbe (Schwarz oder Weiß)
	 */
	
	private Position p1;
	private Position p2;
	private Position p3;
	private Color color;
	private ArrayList<Position> positions = new ArrayList<>();
	
	//Konstruktor
	public Muehle(Position p1, Position p2, Position p3, Color color){
		this.color = color;
		this.p1 = p1;
		this.p2 = p2;
		this.p3 = p3;
		positions.add(p1);
		positions.add(p2);
		positions.add(p3);
	}
	
	//Getter und Setter Methoden
	public Position getP1() {
		return p1;
	}

	public void setP1(Position p1) {
		this.p1 = p1;
	}

	public Position getP2() {
		return p2;
	}

	public void setP2(Position p2) {
		this.p2 = p2;
	}

	public Position getP3() {
		return p3;
	}

	public void setP3(Position p3) {
		this.p3 = p3;
	}

	public Color getColor() {
		return color;
	}

	public void setColor(Color color) {
		this.color = color;
	}
	
	public ArrayList<Position> getPositions() {
		return positions;
	}

	public void setPositions(ArrayList<Position> positions) {
		this.positions = positions;
	}
	
	//----------------------------------------------------------
	
	//Equals Methode
	public boolean equals(Muehle muehle){
		return this.getColor().equals(muehle.getColor()) && this.getP1().equals(muehle.getP1()) && this.getP2().equals(muehle.getP2()) && this.getP3().equals(muehle.getP3());
	}
	
	/** Diese Methode überprüft, ob mindestens eine der drei Positionen ein freies Nachbarfeld hat, um die Mühle zu benutzen
	 * 
	 * @param b Das aktuelle logische Spielbrett
	 * @return true, wenn die Mühle beweglich ist und false, wenn sie blockiert ist
	 */
	public boolean isFree(Board b){
		ArrayList<Position> allNeighbors = b.getAllNeighbors(p1);
		allNeighbors.addAll(b.getAllNeighbors(p2));
		allNeighbors.addAll(b.getAllNeighbors(p3));
		boolean oneIsFree = false;
		for(Position p : allNeighbors){
			if(!p.isOccupied()){
				oneIsFree = true;
			}
		}
		return oneIsFree;
		
	}
	
	// toString Methoder
	public String toString(){
		return p1.toString()+"+"+p2.toString()+"+"+p3.toString();
	}

}
