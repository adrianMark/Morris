import java.awt.Color;
import java.util.ArrayList;

public class ZwickMuehle {
	
	/** Diese Klasse repräsentiert den Grundaufbau einer Zwickmühle. Sie besteht aus vier fixen, unveränderlichen Positionen und zwei variablen Positionen, von denen 
	 * eine zwingend frei sein muss, damit sich ein Stein zwischen den beiden Mühlen bewegen kann. Die fixen und flxiblen Positionen werden in ArrayListen gespeichert,
	 * um sie besser verarbeiten zu können. Eine Zwickmühle hat außerdem eine Farbe(Schwarz/Weiß).
	 */

	private Position fixedPosition1;
	private Position fixedPosition2;
	private Position flexiblePosition1;
	private Position flexiblePosition2;
	private Position fixedPosition3;
	private Position fixedPosition4;
	private Color color;

	ArrayList<Position> fixedPositions = new ArrayList<>();
	ArrayList<Position> flexiblePositions = new ArrayList<>();

	
	//Konstruktor
	public ZwickMuehle(Position fixedPosition1, Position fixedPosition2, Position fixedPosition3,
			Position fixedPosition4, Position flexiblePosition1, Position flexiblePosition2, Color color) {
		this.fixedPosition1 = fixedPosition1;
		this.fixedPosition2 = fixedPosition2;
		this.flexiblePosition1 = flexiblePosition1;
		this.flexiblePosition2 = flexiblePosition2;
		this.fixedPosition3 = fixedPosition3;
		this.fixedPosition4 = fixedPosition4;
		this.color = color;
		fixedPositions.add(fixedPosition1);
		fixedPositions.add(fixedPosition2);
		fixedPositions.add(fixedPosition3);
		fixedPositions.add(fixedPosition4);
		flexiblePositions.add(flexiblePosition1);
		flexiblePositions.add(flexiblePosition2);
	}

	//Getter und Setter Methoden
	public Position getFixedPosition1() {
		return fixedPosition1;
	}

	public void setFixedPosition1(Position fixedPosition1) {
		this.fixedPosition1 = fixedPosition1;
	}

	public Position getFixedPosition2() {
		return fixedPosition2;
	}

	public void setFixedPosition2(Position fixedPosition2) {
		this.fixedPosition2 = fixedPosition2;
	}

	public Position getFlexiblePosition1() {
		return flexiblePosition1;
	}

	public void setFlexiblePosition1(Position flexiblePosition1) {
		this.flexiblePosition1 = flexiblePosition1;
	}

	public Position getFlexiblePosition2() {
		return flexiblePosition2;
	}

	public void setFlexiblePosition2(Position flexiblePosition2) {
		this.flexiblePosition2 = flexiblePosition2;
	}

	public Position getFixedPosition3() {
		return fixedPosition3;
	}

	public void setFixedPosition3(Position fixedPosition3) {
		this.fixedPosition3 = fixedPosition3;
	}

	public Position getFixedPosition4() {
		return fixedPosition4;
	}

	public void setFixedPosition4(Position fixedPosition4) {
		this.fixedPosition4 = fixedPosition4;
	}

	public Color getColor() {
		return color;
	}

	public void setColor(Color color) {
		this.color = color;
	}

	public ArrayList<Position> getFixedPositions() {
		return fixedPositions;
	}

	public void setFixedPositions(ArrayList<Position> fixedPositions) {
		this.fixedPositions = fixedPositions;
	}

	public ArrayList<Position> getFlexiblePositions() {
		return flexiblePositions;
	}

	public void setFlexiblePositions(ArrayList<Position> flexiblePositions) {
		this.flexiblePositions = flexiblePositions;
	}
	
	//----------------------------------------------------------------------------------

	//Überschriebene equals Methode
	public boolean equals(ZwickMuehle zm) {
		for (int i = 0; i < 4; i++) {
			Position p = this.getFixedPositions().get(i);
			if(!zm.getFixedPositions().contains(p)){
				return false;
			}
		}
		for (int i = 0; i < 2; i++) {
			Position p = this.getFlexiblePositions().get(i);
			if(!zm.getFlexiblePositions().contains(p)){
				return false;
			}
		}
		if (this.getColor().equals(zm.getColor())) {
			return true;
			
		} else {
			return false;
		}
	};

	
	/** Diese Methode erlaubt es per Aufruf, die Zwickmühle zu benutzen und den variablen Stein zwischen den beiden Mühlen zu benutzen
	 * 
	 * @param b Das Board auf dem die Bewegung durchgeführt wird
	 * @return Der Zug als Array (Index 0: Ausgangsposition; Index 1: Zielposition)
	 */
	public Position[] use(Board b){
		Position[] move = new Position[2];
		if(flexiblePosition1.isOccupied()){
			b.performDraw(b.getPosition(this.flexiblePosition1.getRing(), this.flexiblePosition1.getPosition()), b.getPosition(this.flexiblePosition2.getRing(), this.flexiblePosition2.getPosition()));
			move[0] = this.flexiblePosition1;
			move[1] = this.flexiblePosition2;
			return move;
		}else{
			b.performDraw(b.getPosition(this.flexiblePosition2.getRing(), this.flexiblePosition2.getPosition()), b.getPosition(this.flexiblePosition1.getRing(), this.flexiblePosition1.getPosition()));
			move[0] = this.flexiblePosition2;
			move[1] = this.flexiblePosition1;
			return move;
		}
	}
	
	/** Diese Methode überprüft anhand zweier Zustände des Boards (before und now) ob dieses Zwickmühlen Objekt genutzt wurde. D.h. ob der variable Stein 
	 * zwischen den beiden möglichen Positionen gewechselt hat.
	 * 
	 * @param before Das zu untersuchende Board vor dem Zug
	 * @param now Das zu untersuchende Board nach dem Zug
	 * @return Ein boolean Wert, der bestimmt, ob diese Mühle genutzt wurde(true) oder nicht (false)
	 */
	public boolean wasUsed(Board before, Board now){
		Position flexBefore = before.getPosition(this.flexiblePosition1.getRing(), this.flexiblePosition1.getPosition());
		Position flexAfter = now.getPosition(this.flexiblePosition1.getRing(), this.flexiblePosition1.getPosition());
		
		if((flexBefore.isOccupied() && !flexAfter.isOccupied()) || (!flexBefore.isOccupied() && flexAfter.isOccupied())){
			return true;
		}else{
			return false;
		}
	}

	//toString Methode
	public String toString(){
		return fixedPosition1.toString()+" + "+fixedPosition2.toString()+" + "+fixedPosition3.toString()+" + "+fixedPosition4.toString()+ " and "+ flexiblePosition1.toString()+" + "+flexiblePosition2.toString();
	}

}
