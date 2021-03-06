package gioco;

import java.awt.BorderLayout;
import java.awt.Color;
import java.awt.Font;
import java.awt.FontFormatException;
import java.awt.Frame;
import java.awt.GraphicsEnvironment;
import java.awt.GridBagConstraints;
import java.awt.GridBagLayout;
import java.awt.Window;
import java.awt.event.ItemEvent;
import java.awt.event.ItemListener;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import java.io.File;
import java.io.IOException;
import java.sql.*;
import java.util.ArrayList;
import java.util.List;
import java.util.Locale;

import javax.sound.sampled.AudioSystem;
import javax.sound.sampled.Clip;
import javax.sound.sampled.FloatControl;
import javax.sound.sampled.LineUnavailableException;
import javax.sound.sampled.UnsupportedAudioFileException;
import javax.swing.JCheckBox;
import javax.swing.JComboBox;
import javax.swing.JLabel;
import javax.swing.JOptionPane;
import javax.swing.JPanel;
import javax.swing.JTextField;
import javax.swing.UIManager;
import javax.swing.UnsupportedLookAndFeelException;

/**
 * Questa classe si occupa di organizzare e definire l'interfaccia grafica
 * del sottomen� 'Carica Partita', contiene anche i mouseListener dei relativi
 * pulsanti in essa contenuti e le azioni da compiere.
 * @author Werther e Lorenzo
 *
 */
public class GUICaricaPartita extends JPanel {
	
	private JPanel pnlMenu;
	private JPanel pnlerror;
	private GridBagConstraints c, d;
	private Font fontFuturist;
	private JLabel lblData,lblDatav;
	private JLabel lblNumSalvataggio;
	private JLabel lblNomeGiocatore, lblNomeGiocatorev;
	private JLabel lblTutorial, lblTutorialv;
	private JLabel lblDifficolta, lblDifficoltav;
	private JLabel lblMappa, lblMappav;
	private JLabel lblCivilta, lblCiviltav;
	private JLabel lblTurno, lblTurnov;
	private JLabel lblEpoca, lblEpocav;
	private JLabel lblOro, lblOrov;
	private JLabel lblMateriali, lblMaterialiv;
	private JLabel lblPuntiRicerca, lblPuntiRicercav;
	private JPanel pnlInfoPartita;
	private JComboBox<Integer> cmbSalvataggi;
	private RoundedCornerButton btnIndietro;
	private RoundedCornerButton btnCarica;
	private RoundedCornerButton btnElimina;
	private ConnectionDB connessione1 = null;
	private ConnectionDB connessione2 = null;
	private ConnectionDB connessione3 = null;
	private String stringa;
	private String nomeGiocatore;
	private ResultSet rs;
	private Clip audio;
	private int dialogResult;
	private int index;
	private String linea;
	private Partita partita;
	private Window[] finestreAttive;
	private int val;
	private int i=0;
	private int k=0;
	private int tutorial,mappa,civilta,difficolta;
	private GUIMenuPrincipale guiMenuPrincipale; //Inserito per avere un riferimento a menu principale
	private GUICaricaPartita guiCaricaPartita;


	/**
	 * Si occupa di definire tutti gli elementi dell'interfaccia grafica e azioni dei pulsanti
	 * @param guiMenuPrincipale riferimento a GUIMenuPrincipale
	 */
	GUICaricaPartita(GUIMenuPrincipale guiMenuPrincipale) {
		try {
			//Creo un font custom
			fontFuturist = Font.createFont(Font.TRUETYPE_FONT, new File("media\\font_futurist_fixed.ttf")).deriveFont(12f);
			GraphicsEnvironment ge = GraphicsEnvironment.getLocalGraphicsEnvironment();
			//registro il font
			ge.registerFont(Font.createFont(Font.TRUETYPE_FONT, new File("media\\font_futurist_fixed.ttf")));
		} catch (IOException e) {
			e.printStackTrace();
		} catch(FontFormatException e) {
			e.printStackTrace();
		}
		try {
			UIManager.setLookAndFeel(UIManager.getSystemLookAndFeelClassName());
		} catch (ClassNotFoundException | InstantiationException | UnsupportedLookAndFeelException e1) {
			// TODO Auto-generated catch block
			e1.printStackTrace();
		} catch (IllegalAccessException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}

		pnlMenu = new JPanel(new GridBagLayout());
		setBackground(Color.LIGHT_GRAY);
		setLayout(new BorderLayout(0, 0));
		pnlMenu.setBackground(Color.WHITE);
		this.guiMenuPrincipale = guiMenuPrincipale;
		guiCaricaPartita = this;

		lblNumSalvataggio=new JLabel(Global.getLabels("s18"));
		lblNumSalvataggio.setFont(lblNumSalvataggio.getFont().deriveFont(20f));
		lblData=new JLabel(Global.getLabels("s19"));
		lblData.setFont(lblData.getFont().deriveFont(20f));
		lblNomeGiocatore = new JLabel(Global.getLabels("s20"));
		lblNomeGiocatore.setFont(lblNomeGiocatore.getFont().deriveFont(20f));
		lblTutorial = new JLabel(Global.getLabels("s21"));
		lblTutorial.setFont(lblTutorial.getFont().deriveFont(20f));
		lblDifficolta = new JLabel(Global.getLabels("s22"));
		lblDifficolta.setFont(lblDifficolta.getFont().deriveFont(20f));
		lblMappa = new JLabel(Global.getLabels("s23"));
		lblMappa.setFont(lblMappa.getFont().deriveFont(20f));
		lblCivilta = new JLabel(Global.getLabels("s24"));
		lblCivilta.setFont(lblCivilta.getFont().deriveFont(20f));
		lblTurno = new JLabel(Global.getLabels("s25"));
		lblTurno.setFont(lblTurno.getFont().deriveFont(20f));
		lblEpoca = new JLabel(Global.getLabels("s26"));
		lblEpoca.setFont(lblEpoca.getFont().deriveFont(20f));
		lblOro = new JLabel(Global.getLabels("s27"));
		lblOro.setFont(lblOro.getFont().deriveFont(20f));
		lblMateriali = new JLabel(Global.getLabels("s28"));
		lblMateriali.setFont(lblMateriali.getFont().deriveFont(20f));
		lblPuntiRicerca = new JLabel(Global.getLabels("s29"));
		lblPuntiRicerca.setFont(lblPuntiRicerca.getFont().deriveFont(20f));

		pnlInfoPartita = new JPanel(new GridBagLayout());

		c = new GridBagConstraints();
		c.anchor = new GridBagConstraints().LINE_END;
		c.gridx = 0;
		c.gridy = 0;

		pnlInfoPartita.add(lblData,c);
		c.gridy ++;
		pnlInfoPartita.add(lblNomeGiocatore, c);
		c.gridy ++;
		pnlInfoPartita.add(lblTutorial, c);
		c.gridy ++;
		pnlInfoPartita.add(lblDifficolta, c);
		c.gridy ++;
		pnlInfoPartita.add(lblMappa, c);
		c.gridy ++;
		pnlInfoPartita.add(lblCivilta, c);
		c.gridy ++;
		pnlInfoPartita.add(lblTurno, c);
		c.gridy ++;
		pnlInfoPartita.add(lblEpoca, c);
		c.gridy ++;
		pnlInfoPartita.add(lblOro, c);
		c.gridy ++;
		pnlInfoPartita.add(lblMateriali, c);
		c.gridy ++;
		pnlInfoPartita.add(lblPuntiRicerca, c);

		lblDatav=new JLabel("");
		lblDatav.setFont(lblDatav.getFont().deriveFont(16f));
		lblNomeGiocatorev = new JLabel("");
		lblNomeGiocatorev.setFont(lblNomeGiocatorev.getFont().deriveFont(16f));
		lblTutorialv = new JLabel("");
		lblTutorialv.setFont(lblTutorialv.getFont().deriveFont(16f));
		lblDifficoltav = new JLabel("");
		lblDifficoltav.setFont(lblDifficoltav.getFont().deriveFont(16f));
		lblMappav = new JLabel("");
		lblMappav.setFont(lblMappav.getFont().deriveFont(16f));
		lblCiviltav = new JLabel("");
		lblCiviltav.setFont(lblCiviltav.getFont().deriveFont(16f));
		lblTurnov = new JLabel("");
		lblTurnov.setFont(lblTurnov.getFont().deriveFont(16f));
		lblEpocav = new JLabel("");
		lblEpocav.setFont(lblEpocav.getFont().deriveFont(16f));
		lblOrov = new JLabel("");
		lblOrov.setFont(lblOrov.getFont().deriveFont(16f));
		lblMaterialiv = new JLabel("");
		lblMaterialiv.setFont(lblMaterialiv.getFont().deriveFont(16f));
		lblPuntiRicercav = new JLabel("");
		lblPuntiRicercav.setFont(lblPuntiRicercav.getFont().deriveFont(16f));



		c.anchor = new GridBagConstraints().LINE_START;
		c.gridx = 1;
		c.gridy = 0;

		pnlInfoPartita.add(lblDatav,c);
		c.gridy ++;
		pnlInfoPartita.add(lblNomeGiocatorev, c);
		c.gridy ++;
		pnlInfoPartita.add(lblTutorialv, c);
		c.gridy ++;
		pnlInfoPartita.add(lblDifficoltav, c);
		c.gridy ++;
		pnlInfoPartita.add(lblMappav, c);
		c.gridy ++;
		pnlInfoPartita.add(lblCiviltav, c);
		c.gridy ++;
		pnlInfoPartita.add(lblTurnov, c);
		c.gridy ++;
		pnlInfoPartita.add(lblEpocav, c);
		c.gridy ++;
		pnlInfoPartita.add(lblOrov, c);
		c.gridy ++;
		pnlInfoPartita.add(lblMaterialiv, c);
		c.gridy ++;
		pnlInfoPartita.add(lblPuntiRicercav, c);

		d = new GridBagConstraints();
		d.gridx = 2;
		d.gridy = 0;
		pnlMenu.add(pnlInfoPartita,  d);

		d.gridy ++;
		pnlMenu.add(new JLabel(" "), d);
		d.gridy ++;
		pnlMenu.add(new JLabel(" "), d);
		d.gridy ++;

		cmbSalvataggi = new JComboBox<Integer>();
		cmbSalvataggi.addItemListener(new ItemListener() {
			public void itemStateChanged(ItemEvent event) {
				if(event.getStateChange() == ItemEvent.SELECTED)
					caricaInfoFile();
			}
		});
		d.gridx--;
		pnlMenu.add(lblNumSalvataggio,d);
		d.gridx++;
		pnlMenu.add(cmbSalvataggi, d);
		d.gridy ++;
		pnlMenu.add(new JLabel(" "), d);
		d.gridy ++;
		pnlMenu.add(new JLabel(" "), d);
		d.gridy ++;

		d.gridx = 0;
		btnIndietro = new RoundedCornerButton(Global.getLabels("a0"));
		btnIndietro.setFont(fontFuturist.deriveFont(16f));
		btnIndietro.addMouseListener(new MouseAdapter() {
			@Override
			public void mouseReleased(MouseEvent arg0) {
				try {
					audio = AudioSystem.getClip();
					audio.open(AudioSystem.getAudioInputStream(new File("media/suonoindietro.wav")));
				} catch (LineUnavailableException | IOException | UnsupportedAudioFileException e1) {
					e1.printStackTrace();
				}
				FloatControl gainControl = (FloatControl) audio.getControl(FloatControl.Type.MASTER_GAIN);
				gainControl.setValue(Global.getLivVolume()); 
				audio.start();
				setVisible(false);
				guiMenuPrincipale.ripristinaPanelDx((int)guiCaricaPartita.getSize().getWidth(), (int)guiCaricaPartita.getSize().getHeight());
			}
		});
		pnlMenu.add(btnIndietro, d);

		d.gridx ++;
		pnlMenu.add(new JLabel("     "), d);
		d.gridx ++;

		btnCarica = new RoundedCornerButton(Global.getLabels("a2"));
		btnCarica.setFont(fontFuturist.deriveFont(16f));
		btnCarica.addMouseListener(new MouseAdapter() {
			@Override
			public void mouseReleased(MouseEvent arg0) {
				dialogResult = JOptionPane.showConfirmDialog (pnlerror, Global.getLabels("a3"),"Warning",JOptionPane.YES_NO_OPTION);
				if(dialogResult== JOptionPane.YES_OPTION){
					try {
						audio = AudioSystem.getClip();
						audio.open(AudioSystem.getAudioInputStream(new File("media/suonoiniziopartita.wav")));
					}catch (LineUnavailableException | IOException | UnsupportedAudioFileException e1) {
						e1.printStackTrace();
					}
					FloatControl gainControl = (FloatControl) audio.getControl(FloatControl.Type.MASTER_GAIN);
					gainControl.setValue(Global.getLivVolume()); 
					Music.stopSound();
					audio.start();
					try{                                      //Otteniamo il codice del salvataggio e carichiamo la partita parsando la stringa associata al codice
						connessione3=new ConnectionDB();
						index=(cmbSalvataggi.getItemAt(cmbSalvataggi.getSelectedIndex()));  
						stringa=connessione3.caricaStringa(index);
						if(stringa==null){	
							pnlerror = new JPanel();
							pnlerror.setBackground(Color.WHITE);
							JOptionPane.showMessageDialog(pnlerror, Global.getLabels("e3"),Global.getLabels("e2"), JOptionPane.ERROR_MESSAGE);
						}
						
						
						//partita = new Partita(null, nomeGiocatore, tutorial, difficolta, mappa, civilta);      //Creiamo una partita nuova basandoci sui dati raccolti dal salvataggio
						//partita.setSituazioneDiGioco(stringa);                          //Impostiamo la stringa nella partita
						 
						partita = new Partita(stringa, nomeGiocatore, tutorial, difficolta, mappa, civilta);                                                //Oppure creiamo una partita partendo solo dalla stringa
						
						finestreAttive=Frame.getWindows();      //Ritorna un array con tutte le finestre attive
						finestreAttive[0].setVisible(false);
						
					} catch (ClassNotFoundException | SQLException e) {
						e.printStackTrace();
						pnlerror = new JPanel();
						pnlerror.setBackground(Color.WHITE);
						JOptionPane.showMessageDialog(pnlerror, Global.getLabels("e3"),Global.getLabels("e2"), JOptionPane.ERROR_MESSAGE);
						if(connessione3!=null)
							connessione3.closeConnection();
					} catch(NullPointerException e){
						e.printStackTrace();
						pnlerror = new JPanel();
						pnlerror.setBackground(Color.WHITE);
						JOptionPane.showMessageDialog(pnlerror,Global.getLabels("e4"),Global.getLabels("e2"), JOptionPane.ERROR_MESSAGE);
						if(connessione3!=null)
							connessione3.closeConnection();
					} finally{
						if(connessione3!=null)
							connessione3.closeConnection();
					}
				}
			}
		});
		pnlMenu.add(btnCarica, d);

		d.gridx ++;
		pnlMenu.add(new JLabel("     "), d);
		d.gridx ++;

		btnElimina = new RoundedCornerButton(Global.getLabels("a4"));
		btnElimina.setFont(fontFuturist.deriveFont(16f));
		btnElimina.addMouseListener(new MouseAdapter() {
			@Override
			public void mouseReleased(MouseEvent arg0) {
				try {
					audio = AudioSystem.getClip();
					audio.open(AudioSystem.getAudioInputStream(new File("media/bottonepremuto.wav")));
				} catch (LineUnavailableException | IOException | UnsupportedAudioFileException e1) {
					e1.printStackTrace();
				}
				FloatControl gainControl = (FloatControl) audio.getControl(FloatControl.Type.MASTER_GAIN);
				gainControl.setValue(Global.getLivVolume()); 
				audio.start();
				dialogResult = JOptionPane.showConfirmDialog (pnlerror, Global.getLabels("s0"),"Warning",JOptionPane.YES_NO_OPTION);
				if(dialogResult== JOptionPane.YES_OPTION){
					try {    //Otteniamo l'indice del salvataggio da cancellare e chiamiamo la funzione apposita
						connessione3=new ConnectionDB();
						index=(cmbSalvataggi.getItemAt(cmbSalvataggi.getSelectedIndex()));  
						connessione3.elimina(index);
						creaComboBox();
					} catch (ClassNotFoundException | SQLException  | NullPointerException e) {
						e.printStackTrace();
						pnlerror = new JPanel();
						pnlerror.setBackground(Color.WHITE);
						JOptionPane.showMessageDialog(pnlerror,Global.getLabels("e5"),Global.getLabels("e2"), JOptionPane.ERROR_MESSAGE);
						if(connessione3!=null)
							connessione3.closeConnection();
					} finally{
						if(connessione3!=null)
							connessione3.closeConnection();
					}
				}
			}
		});
		pnlMenu.add(btnElimina, d);
		add(pnlMenu, BorderLayout.CENTER);
	}

	/**
	 * Cerca tutti i salvataggi nel database e li carica nella ComboBox
	 */
	public void creaComboBox(){
		cmbSalvataggi.removeAllItems();	

		try {	
			connessione2 = new ConnectionDB();           //Connessione al database
			connessione2.crea();                         //Creiamo la tabella salvataggi se non esiste
			connessione2.creaTabellaPartita();           //Creiamo la tabella per le stringhe partita se non esiste
			rs=connessione2.executeQuery();              //Lettura di tutti i salvataggi
			for(i=0;rs.next();++i){                      //Conteggio delle righe 
			}
			for(k=0;k<i;++k)                             //Riempiamo la ComboBox con i vari indici
				cmbSalvataggi.addItem(k+1);
			if(cmbSalvataggi.getItemCount() == 0){       //Errore se non vengono trovati salvataggi. Non mostriamo nessuna informazione
				lblDatav.setText("");
				lblNomeGiocatorev.setText("");
				lblTutorialv.setText("");
				lblDifficoltav.setText("");
				lblMappav.setText("");
				lblCiviltav.setText("");					
				lblTurnov.setText("");
				lblEpocav.setText("");					
				lblOrov.setText("");					
				lblMaterialiv.setText("");				
				lblPuntiRicercav.setText("");
				pnlerror = new JPanel();
				pnlerror.setBackground(Color.WHITE);
				JOptionPane.showMessageDialog(pnlerror, Global.getLabels("e6"),Global.getLabels("e7"), JOptionPane.WARNING_MESSAGE);
			}
		} catch(SQLException |ClassNotFoundException e) {
			e.printStackTrace();
			if(connessione2!=null)
				connessione2.closeConnection();
		}  finally{
			if(connessione2!=null)
				connessione2.closeConnection();
		}
	}

	/**
	 * Controlla il salvataggio selezionato e visualizza le relative informazioni
	 */
	private void caricaInfoFile(){
		try {			
			connessione1 = new ConnectionDB();             //Crea la connessione al DB
			rs=connessione1.executeQuery();                
			index=(cmbSalvataggi.getItemAt(cmbSalvataggi.getSelectedIndex()));     //Otteniamo l'indice selezionato dall'utente
			while(rs.getRow()!=index)     //Cerchiamo la riga corrispondente all'indice e leggiamo le varie informazioni
				rs.next();                         
			linea=rs.getString("Data");
			linea=linea.substring(linea.indexOf("_:")+1);
			lblDatav.setText(linea);
			linea=rs.getString("NomeGiocatore");
			linea=linea.substring(linea.indexOf("_:")+1);
			lblNomeGiocatorev.setText(linea);
			linea=rs.getString("Tutorial");
			linea=linea.substring(linea.indexOf("_:")+1);
			lblTutorialv.setText(linea);
			linea=rs.getString("Difficolt�");
			linea=linea.substring(linea.indexOf("_:")+1);
			lblDifficoltav.setText(linea);
			linea=rs.getString("Mappa");
			linea=linea.substring(linea.indexOf("_:")+1);
			lblMappav.setText(linea);
			linea=rs.getString("Civilt�");
			linea=linea.substring(linea.indexOf("_:")+1);
			lblCiviltav.setText(linea);
			val=rs.getInt("Turno");						
			lblTurnov.setText(Integer.toString(val));
			linea=rs.getString("Epoca");
			linea=linea.substring(linea.indexOf("_:")+1);
			lblEpocav.setText(linea);
			val=rs.getInt("Oro");						
			lblOrov.setText(Integer.toString(val));
			val=rs.getInt("Materiali");						
			lblMaterialiv.setText(Integer.toString(val));
			val=rs.getInt("Punti_ricerca");						
			lblPuntiRicercav.setText(Integer.toString(val));
			
			
			if(rs.getString("Tutorial").equals(Global.getLabels("s30")))
				tutorial=1;
			else
				tutorial=0;
			if(rs.getString("Difficolt�").equals(Global.getLabels("s6")))
				difficolta=0;
			else if(rs.getString("Difficolt�").equals(Global.getLabels("s7")))
				difficolta=1;
			else if(rs.getString("Difficolt�").equals(Global.getLabels("s8")))
				difficolta=2;
			else if(rs.getString("Difficolt�").equals(Global.getLabels("s9")))
				difficolta=3;
			if(rs.getString("Mappa").equals(Global.getLabels("s10")))
				mappa=0;
			else if(rs.getString("Mappa").equals(Global.getLabels("s11")))
				mappa=1;
			else if(rs.getString("Mappa").equals(Global.getLabels("s12")))
				mappa=2;
			else if(rs.getString("Mappa").equals(Global.getLabels("s13")));
				mappa=3;
			if(rs.getString("Civilt�").equals(Global.getLabels("s14")))
				civilta=0;
			else if(rs.getString("Civilt�").equals(Global.getLabels("s15")))
				civilta=1;
			else if(rs.getString("Civilt�").equals(Global.getLabels("s16")))
				civilta=2;
			else if(rs.getString("Civilt�").equals(Global.getLabels("s17")))
				civilta=3;
			rs.getString("NomeGiocatore");
			
			
		} catch(SQLException | ClassNotFoundException e) {
			e.printStackTrace();
			if(connessione1!=null)
				connessione1.closeConnection();
		}  finally{
			if(connessione1!=null)
				connessione1.closeConnection();
		}
	}			
}


