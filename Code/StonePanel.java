import java.awt.Color;
import java.awt.Dimension;
import java.awt.Graphics;
import java.awt.Graphics2D;
import java.awt.RenderingHints;
import javax.swing.JPanel;

@SuppressWarnings("serial")
class StonePanel extends JPanel {
	/** Diese Klasse bildet die Grundlage für die grafische repräsentation der Steine. Sie ist von JPanel abgeleitet
	 * und zeichnet einen Kreis. Sie hat eine Hintergrundfarbe, eine Position (Ring/Position) und kann belegt oder frei sein.
	 */
	private Color backgroundColor = Color.BLACK;
	private int cornerRadius = 15;
	private Color white = new Color(0, 0, 0);
	private Color black = new Color(255, 255, 255);

	private int ring;
	private int position;
	private boolean isOccupied = false;
	private MuehleGUI gui;

	//Konstruktor
	public StonePanel(int ring, int position, int x, int y, MuehleGUI gui) {
		super();
		this.gui = gui;
		cornerRadius = 100;
		this.ring = ring;
		this.position = position;
		setBounds(x, y, 23, 23);
		setOpaque(false);
		
		//Ein Mouse Listener wird hinzugefügt
		StoneListener listener = new StoneListener(this);
		this.addMouseListener(listener);
	}

	/** Diese Methode wird während der Setzphase genutzt und belegt die Position, je nachdem welche Farbe 
	 *  am Zug ist mit der entsprechenden Farbe.
	 */
	public void put() {
		getGui().changeTurn();
		//Macht das Panel sichtbar
		setBounds(getX() - 15, getY() - 15, 50, 50);
		
		//Je nach Zug wird eine andere Farbe verwendet ()Schwarz oder Weiß
		if (getGui().getRound() % 2 == 0) {
			setOccupied(true);
			setBackground(Color.WHITE);
			getGui().addEventToHistory("Spieler setzt auf "+ this.ring+","+this.position+".");
			gui.getBoard().setPosition(this.ring, this.position,white, true);
			getGui().setRound(getGui().getRound() + 1);
			
		} else {
			setOccupied(true);
			setBackground(Color.BLACK);
			getGui().addEventToHistory("Computer setzt auf "+ this.ring+","+this.position+".");
			gui.getBoard().setPosition(this.ring, this.position,black, true);
			getGui().setRound(getGui().getRound() + 1);
		}
		//Wenn Jede Farbe neun Steine gesetzt hat wird die Ziehphase eingeläutet
		if (getGui().getRound() == 19) {
			getGui().getBrain().setPhase(1);
			getGui().addEventToHistory("DIE ZUGPHASE BEGINNT!");
			getGui().setPhase(1);				
		}
		repaint();
	}
	
	/** Diese Methode belegt den Stein mit der Farbe 'c'
	 * 
	 * @param c Farbe die verwendet werden soll
	 */
	public void set(Color c){
		//Macht den Stein sichtbar
		setBounds(getX() - 15, getY() - 15, 50, 50);
		setOccupied(true);
		setBackground(c);
		getGui().setRound(getGui().getRound() + 1);
		getGui().changeTurn();
	}
	
	/**Diese Methode entfernt den sichtbaren Stein und verkleinert das Panel
	 * 
	 */
	public void removeStone(){
		setBounds(getX() + 15, getY() + 15, 23, 23);
		setBackground(Color.BLACK);
		repaint();
		setOccupied(false);
	}

	//Getter und Setter Methoden
	public Color getBackgroundColor() {
		return backgroundColor;
	}

	public int getRing() {
		return ring;
	}

	public void setRing(int ring) {
		this.ring = ring;
	}

	public int getPosition() {
		return position;
	}

	public void setPosition(int position) {
		this.position = position;
	}

	public boolean isOccupied() {
		return isOccupied;
	}

	public void setOccupied(boolean isOccupied) {
		this.isOccupied = isOccupied;
	}

	@Override
	public void setBackground(Color bg) {
		super.setBackground(bg);
		this.backgroundColor = bg;
	}

	public MuehleGUI getGui() {
		return gui;
	}

	public void setGui(MuehleGUI gui) {
		this.gui = gui;
	}
	//----------------------------------------------------------------------------

	
	//Überschriebene Methode, die einen Kreis anstelle eines Vierecks zeichnet
	@Override
	protected void paintComponent(Graphics g) {
		super.paintComponent(g);
		Dimension arcs = new Dimension(cornerRadius, cornerRadius);
		int width = getWidth();
		int height = getHeight();
		Graphics2D graphics = (Graphics2D) g;
		graphics.setRenderingHint(RenderingHints.KEY_ANTIALIASING, RenderingHints.VALUE_ANTIALIAS_ON);

		if (backgroundColor != null) {
			graphics.setColor(backgroundColor);
		} else {
			graphics.setColor(getBackground());
		}
		graphics.fillRoundRect(0, 0, width - 1, height - 1, arcs.width, arcs.height); // Hintergrund
		graphics.setColor(getForeground());
		graphics.drawRoundRect(0, 0, width - 1, height - 1, arcs.width, arcs.height); // Border zeichnen
	}
}
