
import java.awt.Color;
import java.awt.Font;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import java.io.FileWriter;
import java.io.IOException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;

import javax.swing.ImageIcon;
import javax.swing.JButton;
import javax.swing.JEditorPane;
import javax.swing.JFileChooser;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JOptionPane;
import javax.swing.JPanel;
import javax.swing.JScrollPane;
import javax.swing.ScrollPaneConstants;
import javax.swing.SwingConstants;
import javax.swing.border.EmptyBorder;
import javax.swing.text.BadLocationException;
import javax.swing.text.Document;
import javax.swing.text.SimpleAttributeSet;
import javax.swing.JSlider;
import javax.swing.event.ChangeListener;
import javax.swing.event.ChangeEvent;
import java.awt.event.ActionListener;
import java.awt.event.ActionEvent;
import java.awt.Toolkit;

@SuppressWarnings("serial")
public class MuehleGUI extends JFrame {
	
	/**Diese Klasse bildet das Hauptfenster für das Spiel ab. Hier werden die verschiedenen Komponenten zusammengeführt und die Logik mit der GUI verbunden.
	 * 
	 */

	private int round = 1;
	private boolean blackAllowedToJump = false;
	private boolean whiteAllowedToJump = false;
	private int phase = 0;
	private StonePanel from = null;
	private StonePanel to = null;
	private JLabel ComputerTurnLabel;
	private JLabel spielerTurnLabel;
	private JEditorPane editorPane;
	private JLabel timerLbl;
	private ArrayList<ArrayList<StonePanel>> positions = new ArrayList<>();
	private int difficulty = 6;
	private boolean canRemove = false;
	private boolean end = false;
	
	private Board board = new Board();
	private Brain brain = new Brain();
	
	private JPanel contentPane;

	//Konstruktor
	public MuehleGUI() {
		setIconImage(Toolkit.getDefaultToolkit().getImage("Logo.png"));
		setResizable(false);
		setTitle("Muehle");
		setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
		setBounds(100, 100, 958, 606);
		contentPane = new JPanel();
		contentPane.setBorder(new EmptyBorder(5, 5, 5, 5));
		setContentPane(contentPane);
		contentPane.setLayout(null);

		JPanel imagePanel = new JPanel();
		imagePanel.setBounds(0, 0, 569, 567);
		contentPane.add(imagePanel);
		imagePanel.setLayout(null);

		JPanel stoneContainer = new JPanel();
		stoneContainer.setBounds(0, 0, 567, 567);
		stoneContainer.setOpaque(false);
		imagePanel.add(stoneContainer);
		stoneContainer.setLayout(null);

		positions.add(new ArrayList<StonePanel>());
		positions.add(new ArrayList<StonePanel>());
		positions.add(new ArrayList<StonePanel>());

		// AEUSSERER RING
		// -Erzeugen der StonePanel
		StonePanel p00 = new StonePanel(0, 0, 26, 24, this);
		StonePanel p01 = new StonePanel(0, 1, 273, 24, this);
		StonePanel p02 = new StonePanel(0, 2, 521, 24, this);
		StonePanel p03 = new StonePanel(0, 3, 521, 271, this);
		StonePanel p04 = new StonePanel(0, 4, 521, 518, this);
		StonePanel p05 = new StonePanel(0, 5, 273, 518, this);
		StonePanel p06 = new StonePanel(0, 6, 26, 518, this);
		StonePanel p07 = new StonePanel(0, 7, 26, 269, this);

		// Hinzufügen der StonePanel zur GUI
		stoneContainer.add(p00);
		stoneContainer.add(p01);
		stoneContainer.add(p02);
		stoneContainer.add(p03);
		stoneContainer.add(p04);
		stoneContainer.add(p05);
		stoneContainer.add(p06);
		stoneContainer.add(p07);

		// Hinzufügen der Panel in der ArrayList zur effizienten Verwaltung
		positions.get(0).add(p00);
		positions.get(0).add(p01);
		positions.get(0).add(p02);
		positions.get(0).add(p03);
		positions.get(0).add(p04);
		positions.get(0).add(p05);
		positions.get(0).add(p06);
		positions.get(0).add(p07);

		// MITTLERER RING
		// -Erzeugen der StonePanel
		StonePanel p10 = new StonePanel(1, 0, 88, 87, this);
		StonePanel p11 = new StonePanel(1, 1, 273, 85, this);
		StonePanel p12 = new StonePanel(1, 2, 458, 87, this);
		StonePanel p13 = new StonePanel(1, 3, 460, 271, this);
		StonePanel p14 = new StonePanel(1, 4, 460, 454, this);
		StonePanel p15 = new StonePanel(1, 5, 273, 458, this);
		StonePanel p16 = new StonePanel(1, 6, 87, 454, this);
		StonePanel p17 = new StonePanel(1, 7, 88, 269, this);

		// Hinzufügen der StonePanel zur GUI
		stoneContainer.add(p10);
		stoneContainer.add(p11);
		stoneContainer.add(p12);
		stoneContainer.add(p13);
		stoneContainer.add(p14);
		stoneContainer.add(p15);
		stoneContainer.add(p16);
		stoneContainer.add(p17);

		// Hinzufügen der Panel in der ArrayList zur effizienten Verwaltung
		positions.get(1).add(p10);
		positions.get(1).add(p11);
		positions.get(1).add(p12);
		positions.get(1).add(p13);
		positions.get(1).add(p14);
		positions.get(1).add(p15);
		positions.get(1).add(p16);
		positions.get(1).add(p17);

		// INNERER RING
		// -Erzeugen der StonePanel
		StonePanel p20 = new StonePanel(2, 0, 152, 147, this);
		StonePanel p21 = new StonePanel(2, 1, 273, 147, this);
		StonePanel p22 = new StonePanel(2, 2, 396, 147, this);
		StonePanel p23 = new StonePanel(2, 3, 397, 271, this);
		StonePanel p24 = new StonePanel(2, 4, 396, 393, this);
		StonePanel p25 = new StonePanel(2, 5, 273, 394, this);
		StonePanel p26 = new StonePanel(2, 6, 151, 392, this);
		StonePanel p27 = new StonePanel(2, 7, 150, 269, this);

		// Hinzufügen der StonePanel zur GUI
		stoneContainer.add(p20);
		stoneContainer.add(p21);
		stoneContainer.add(p22);
		stoneContainer.add(p23);
		stoneContainer.add(p24);
		stoneContainer.add(p25);
		stoneContainer.add(p26);
		stoneContainer.add(p27);

		// Hinzufügen der Panel in der ArrayList zur effizienten Verwaltung
		positions.get(2).add(p20);
		positions.get(2).add(p21);
		positions.get(2).add(p22);
		positions.get(2).add(p23);
		positions.get(2).add(p24);
		positions.get(2).add(p25);
		positions.get(2).add(p26);
		positions.get(2).add(p27);

		JLabel image = new JLabel("");
		image.setIcon(new ImageIcon("Brett.jpg"));
		image.setBounds(0, 0, 567, 567);
		imagePanel.add(image);

		JPanel menuePanel = new JPanel();
		menuePanel.setBounds(568, 0, 374, 567);
		contentPane.add(menuePanel);
		menuePanel.setLayout(null);

		spielerTurnLabel = new JLabel("Spieler");
		spielerTurnLabel.setFont(new Font("Tahoma", Font.BOLD, 13));
		spielerTurnLabel.setEnabled(false);
		spielerTurnLabel.setHorizontalAlignment(SwingConstants.CENTER);
		spielerTurnLabel.setBounds(199, 23, 117, 31);
		menuePanel.add(spielerTurnLabel);

		ComputerTurnLabel = new JLabel("Computer");
		ComputerTurnLabel.setFont(new Font("Tahoma", Font.BOLD, 13));
		ComputerTurnLabel.setBackground(Color.BLACK);
		ComputerTurnLabel.setHorizontalAlignment(SwingConstants.CENTER);
		ComputerTurnLabel.setBounds(57, 23, 117, 31);
		menuePanel.add(ComputerTurnLabel);

		JScrollPane scrollPane = new JScrollPane();
		scrollPane.setHorizontalScrollBarPolicy(ScrollPaneConstants.HORIZONTAL_SCROLLBAR_NEVER);
		scrollPane.setBounds(35, 92, 312, 348);
		menuePanel.add(scrollPane);

		editorPane = new JEditorPane();
		editorPane.setEditable(false);
		scrollPane.setViewportView(editorPane);

		JLabel lblSpielverlauf = new JLabel("Spielverlauf");
		lblSpielverlauf.setFont(new Font("Tahoma", Font.PLAIN, 11));
		lblSpielverlauf.setHorizontalAlignment(SwingConstants.CENTER);
		lblSpielverlauf.setBounds(35, 77, 314, 14);
		menuePanel.add(lblSpielverlauf);

		JButton button = new JButton("Verlauf exportieren");
		button.setBounds(129, 451, 149, 23);
		menuePanel.add(button);

		timerLbl = new JLabel("3");
		timerLbl.setEnabled(false);
		timerLbl.setHorizontalAlignment(SwingConstants.CENTER);
		timerLbl.setBounds(95, 52, 46, 14);
		menuePanel.add(timerLbl);

		JLabel lblSchwierigkeitsstufe = new JLabel("Schwierigkeitsstufe");
		lblSchwierigkeitsstufe.setFont(new Font("Tahoma", Font.PLAIN, 11));
		lblSchwierigkeitsstufe.setBounds(35, 506, 106, 31);
		menuePanel.add(lblSchwierigkeitsstufe);

		JButton btnNeustart = new JButton("Neustart");
		btnNeustart.setBounds(258, 510, 89, 23);
		menuePanel.add(btnNeustart);

		JSlider slider = new JSlider();

		slider.setFont(new Font("Tahoma", Font.PLAIN, 10));
		slider.setMajorTickSpacing(1);
		slider.setValue(3);
		slider.setPaintLabels(true);
		slider.setSnapToTicks(true);
		slider.setMinorTickSpacing(1);
		slider.setMinimum(1);
		slider.setMaximum(5);
		slider.setBounds(152, 506, 80, 31);
		menuePanel.add(slider);

		//Startet den Computer als Thread
		ComputerThread t = new ComputerThread(this, board);
		t.start();

		//LISTENER
		
		//Export des Spielverlaufs
		button.addMouseListener(new MouseAdapter() {
			@Override
			public void mouseClicked(MouseEvent arg0) {
				JFileChooser fc = new JFileChooser();
				int chose = fc.showSaveDialog(MuehleGUI.this);
				String path = "";
				fc.setVisible(true);
				if (chose == JFileChooser.APPROVE_OPTION) {
					path = fc.getCurrentDirectory() + "\\" + fc.getSelectedFile().getName();
					if (!path.endsWith(".txt")) {
						path = path + ".txt";
					}
					try {
						FileWriter writer = new FileWriter(path);
						writer.write(editorPane.getText());
						writer.close();
					} catch (IOException e) {
						e.printStackTrace();
					}
				}

			}
		});

		//Schwierigkeitsstufe
		slider.addChangeListener(new ChangeListener() {
			public void stateChanged(ChangeEvent arg0) {
				int newDifficulty = slider.getValue();
				int differenceToOne = 5 - newDifficulty;
				difficulty = newDifficulty * (5 - differenceToOne);
				System.out.println(difficulty);
			}
		});
		
		//Neustart Knopf
		btnNeustart.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent arg0) {
				phase = 0;
				round = 1;
				blackAllowedToJump = false;
				whiteAllowedToJump = false;
				end = false;
				from = null;
				to = null;
				canRemove = false;
				board.reset();
				ComputerTurnLabel.setEnabled(true);
				spielerTurnLabel.setEnabled(false);
				for(ArrayList<StonePanel> ring : positions){
					for(StonePanel position : ring){
						if(position.isOccupied()){
							position.removeStone();
						}
					}
				}
				editorPane.setText("");
			}
		});

	}
	
	//Getter und Setter Methoden
	public boolean isBlackAllowedToJump() {
		return blackAllowedToJump;
	}

	public void setBlackAllowedToJump(boolean blackAllowedToJump) {
		this.blackAllowedToJump = blackAllowedToJump;
	}

	public boolean isWhiteAllowedToJump() {
		return whiteAllowedToJump;
	}

	public void setWhiteAllowedToJump(boolean whiteAllowedToJump) {
		this.whiteAllowedToJump = whiteAllowedToJump;
	}

	public JLabel getTimerLbl() {
		return timerLbl;
	}

	public void setTimerLbl(JLabel timerLbl) {
		this.timerLbl = timerLbl;
	}

	public boolean isEnd() {
		return end;
	}

	public void setEnd(boolean end) {
		this.end = end;
	}

	public int getDifficulty() {
		return difficulty;
	}

	public boolean isCanRemove() {
		return canRemove;
	}

	public void setCanRemove(boolean canRemove) {
		this.canRemove = canRemove;
	}

	public void setDifficulty(int difficulty) {
		this.difficulty = difficulty;
	}
	
	public int getRound() {
		return round;
	}

	public void setRound(int round) {
		this.round = round;
	}

	public int getPhase() {
		return phase;
	}

	public void setPhase(int phase) {
		this.phase = phase;
	}

	public StonePanel getFrom() {
		return from;
	}

	public void setFrom(StonePanel from) {
		this.from = from;
	}

	public StonePanel getTo() {
		return to;
	}

	public void setTo(StonePanel to) {
		this.to = to;
	}
	

	public Board getBoard() {
		return board;
	}

	public void setBoard(Board board) {
		this.board = board;
	}

	public Brain getBrain() {
		return brain;
	}

	public void setBrain(Brain brain) {
		this.brain = brain;
	}

	public StonePanel getStone(int ring, int position) {
		return (positions.get(ring).get(position));
	}
	
	//------------------------------------------------------------------------------------

	/** Diese Methode führt einen Spielzug grafisch und logisch auf dem Board aus
	 * 
	 */
	public void moveStone() {
		String akteur = "";
		Color akteurColor;
		//Mitteilung für den Spieler generieren
		if (from.getBackgroundColor().equals(Color.BLACK)) {
			akteur = "Computer";
			akteurColor = new Color(0, 0, 0);
		} else {
			akteur = "Spieler";
			akteurColor = new Color(255, 255, 255);
		}
		//Zug grafisch ausführen
		from.removeStone();
		to.set(akteurColor);
		
		//Spieler informieren
		if ((akteur.equals("Computer") && !blackAllowedToJump) || akteur.equals("Spieler") && !whiteAllowedToJump) {
			addEventToHistory(akteur + ": " + from.getRing() + "," + from.getPosition() + " auf " + to.getRing() + ","
					+ to.getPosition());
		}
		
		//Zug logisch/ auf dem Board ausführen
		board.performDraw(board.getPosition(from.getRing(), from.getPosition()),
				board.getPosition(to.getRing(), to.getPosition()));
		from = null;
		to = null;
	}

	/** Diese Methode zeigt einen Informationsdialog, der den Spieler über Sieg oder Niederlage informiert. Je nach Ausgang werden andere Icons geladen
	 * 
	 * @param playerVictory Boolean der aussagtm ob der Spieler gewonnen oder verloren hat
	 */
	public void showEndDialog(boolean playerVictory) {
		ImageIcon icon;
		String message;
		String title;
		//Nachrichten generieren 
		if (playerVictory) {
			icon = new ImageIcon("Sieg.png");
			message = "Sie haben gewonnen!";
			title = "Sieg";
		}else{
			icon = new ImageIcon("Niederlage.png");
			message = "Sie haben verloren!";
			title = "Niederlage";
			
		}
		// Fenster zeigen
		JOptionPane.showConfirmDialog(this,message, title, JOptionPane.PLAIN_MESSAGE,JOptionPane.INFORMATION_MESSAGE, icon);
	}

	/** Diese Methode ermöglicht es Ereignisse auf der EditorPane (Textfeld) anzuzeigen. Für jede Nachricht wird auch die genaue Uhrzeit angezeigt
	 * 
	 * @param message Die Nachricht, die angezeigt werden soll
	 */
	public void addEventToHistory(String message) {
		SimpleDateFormat sdf = new SimpleDateFormat("hh:mm:ss");
		Document d = editorPane.getDocument();
		SimpleAttributeSet sas = new SimpleAttributeSet();

		try {
			d.insertString(d.getLength(), sdf.format(new Date()) + " Uhr: " + message + "\n", sas);
			editorPane.setCaretPosition(d.getLength());
		} catch (BadLocationException e) {
			e.printStackTrace();
		}
	}

	/**Diese Methode entfernt einen Stein (logisch und grafisch)
	 * 
	 * @param ring Ring auf dem sich der Stein befindet
	 * @param pos Position des Steins auf dem Ring
	 */
	public void removeStone(int ring, int pos) {
		StonePanel sp = positions.get(ring).get(pos);
		sp.removeStone();
	}

	/** Die Methode wechselt die Anzeige, die dem User zeigt ob er oder der Computer am Zug ist
	 * 
	 */
	public void changeTurn() {
		if (ComputerTurnLabel.isEnabled()) {
			ComputerTurnLabel.setEnabled(false);
			spielerTurnLabel.setEnabled(true);
		} else {
			ComputerTurnLabel.setEnabled(true);
			spielerTurnLabel.setEnabled(false);
		}
		repaint();
	}

	/** Die Methode setzt den Hintergrund eines Steins auf Rot, um zu signalisieren, dass er für einen Zug ausgewählt wurde
	 * 
	 * @param p Der Stein der markiert werden soll
	 */
	public void setSelected(StonePanel p) {
		p.setBackground(Color.RED);
	}

	/** Setzt den Hintergrund eines Steines, der selektiert wurde auf den ursprünglichen Zustand zzurück.
	 * 
	 * @param p Der betreffende Stein
	 * @param before Die Farbe, die der Stein vor der Veränderung hatte
	 */
	public void setDeselected(StonePanel p, Color before) {
		int ring = p.getRing();
		int pos = p.getPosition();
		positions.get(ring).get(pos).setBackground(before);
	}
	
	//---------------------------------------------------------------------
	
	//Main Methode - Startet die Anwendung
	public static void main(String[] args) {
		MuehleGUI gui = new MuehleGUI();
		gui.setVisible(true);
	}
}
