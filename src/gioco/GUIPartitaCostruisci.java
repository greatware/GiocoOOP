package gioco;

import java.awt.BorderLayout;
import java.awt.Color;
import java.awt.Cursor;
import java.awt.Dimension;
import java.awt.Font;
import java.awt.FontFormatException;
import java.awt.GraphicsEnvironment;
import java.awt.GridBagConstraints;
import java.awt.GridBagLayout;
import java.awt.GridLayout;
import java.awt.Image;
import java.awt.Point;
import java.awt.Toolkit;
import java.awt.event.ItemEvent;
import java.awt.event.ItemListener;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import java.io.File;
import java.io.IOException;

import javax.sound.sampled.AudioSystem;
import javax.sound.sampled.Clip;
import javax.sound.sampled.FloatControl;
import javax.sound.sampled.LineUnavailableException;
import javax.sound.sampled.UnsupportedAudioFileException;
import javax.swing.AbstractButton;
import javax.swing.ButtonGroup;
import javax.swing.ImageIcon;
import javax.swing.JButton;
import javax.swing.JDialog;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JOptionPane;
import javax.swing.JPanel;
import javax.swing.JRadioButton;
import javax.swing.JScrollPane;
import javax.swing.UIManager;
import javax.swing.UnsupportedLookAndFeelException;

/**
 * GUI che mostra gli elementi attualmente ricercati dal giocatore e costruibili
 * @author Werther e Lorenzo
 *
 */
public class GUIPartitaCostruisci extends JDialog
{
	private Font fontFuturist;
	private JPanel contentPane;
	private JPanel pnlTop;
	private JPanel pnlMid;
	private JPanel pnlBot;
	
	private JRadioButton rdoClassica;
	private JRadioButton rdoMedioevo;
	private JRadioButton rdoVittoriana;
	private ButtonGroup groupRdoEta;
	private JLabel lblEta;
	
	private RoundedCornerButton btnMuovi;
	private RoundedCornerButton btnVendi;
	private RoundedCornerButton btnInfo;
	private RoundedCornerButton btnCompra;
	private RoundedCornerButton btnIndietro;
	
	private Partita partita;
	private GUIPartita guiPartita;
	private IconeGrafiche iconeGrafiche;
	private ValoriDiGioco valoriDiGioco;
	
	private String selezionato; //Opzione al momento selezionata
	private Clip audio;
	
	/**
	 * Inizializza tutte le variabili da presentare all'utente e costruisce l'interfaccia grafica
	 * @param partita riferimento a Partita
	 * @param valoriDiGioco riferimento a Valori di gioco
	 * @param guiPartita riferimento a GUIPartita
	 */
	GUIPartitaCostruisci(Partita partita, ValoriDiGioco valoriDiGioco, GUIPartita guiPartita)
	{
		
		ImageIcon icona = new ImageIcon("media/Icona.png");                  //Carichiamo l'icona personalizzata
		Image scaledicona = icona.getImage().getScaledInstance(80, 60, Image.SCALE_SMOOTH);
		setIconImage(scaledicona);  

		Toolkit t1 = Toolkit.getDefaultToolkit();                                 //Cursore personalizzato
		Image img = t1.getImage("media/cursore.png");
		Point point = new Point(0,0);
		Cursor cursor = t1.createCustomCursor(img, point, "Cursore Personalizzato");
		setCursor(cursor); 
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
		
		setTitle(Global.getLabels("s75"));
		setBounds(0, 0, 650, 450);
		setMinimumSize(new Dimension(650, 450));   
		setLocationRelativeTo(null);
		setLayout(new BorderLayout(0, 0));
		contentPane = new JPanel(new BorderLayout(0,0));
		setModal(true);
		
		this.partita = partita;
		this.valoriDiGioco = valoriDiGioco;
		this.guiPartita = guiPartita;
		
		iconeGrafiche = new IconeGrafiche();
		selezionato = null;
		
		//costruzione pannello top
		rdoClassica = new JRadioButton(Global.getLabels("s76"));
		rdoMedioevo = new JRadioButton(Global.getLabels("s77"));
		rdoVittoriana = new JRadioButton(Global.getLabels("s78"));
		
		groupRdoEta = new ButtonGroup();
		groupRdoEta.add(rdoClassica);
		groupRdoEta.add(rdoMedioevo);
		groupRdoEta.add(rdoVittoriana);
		
		pnlTop = new JPanel(new GridLayout2(1, 4, 0, 0));
		lblEta = new JLabel(Global.getLabels("s79"));
		lblEta.setFont(fontFuturist.deriveFont(13f));
		pnlTop.add(lblEta);
		
		
		
		ItemListener itemListener = new ItemListener() 
		{
			String lastSelected;
			public void itemStateChanged(ItemEvent itemEvent) {
		    AbstractButton aButton = (AbstractButton)itemEvent.getSource();
		    int state = itemEvent.getStateChange();
		    String label = aButton.getText(); //label rappresenta la label selezionata al momento
		    
		    if (state == ItemEvent.SELECTED) 
		    {
		    	popolaNegozio(label, null);
		    } 
		  }
		};
		
		rdoClassica.addItemListener(itemListener);
		rdoMedioevo.addItemListener(itemListener);
		rdoVittoriana.addItemListener(itemListener);
		
	    pnlTop.add(rdoClassica);
		pnlTop.add(rdoMedioevo);
		pnlTop.add(rdoVittoriana);
		contentPane.add(pnlTop, BorderLayout.NORTH);
		
		pnlMid = new JPanel(new GridLayout(11, 3));
		
		contentPane.add(new JScrollPane(pnlMid, JScrollPane.VERTICAL_SCROLLBAR_AS_NEEDED, 
				JScrollPane.HORIZONTAL_SCROLLBAR_AS_NEEDED), BorderLayout.CENTER);
		
		pnlBot = new JPanel(new GridLayout2(1, 5, 0, 0));
		
		btnMuovi = new RoundedCornerButton();
		btnMuovi.setFont(fontFuturist.deriveFont(13f));
		btnMuovi.setText(Global.getLabels("s48"));
		btnMuovi.addMouseListener(new MouseAdapter() {
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
				guiPartita.muoviCostruzione();
				dispose();
			}
		});
		pnlBot.add(btnMuovi);
		
		btnVendi = new RoundedCornerButton();
		btnVendi.setFont(fontFuturist.deriveFont(13f));
		btnVendi.setText(Global.getLabels("s80"));
		btnVendi.addMouseListener(new MouseAdapter() {
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
				guiPartita.vendiCostruzione();
				dispose();
			}
		});
		pnlBot.add(btnVendi);
		
		btnInfo = new RoundedCornerButton();
		btnInfo.setFont(fontFuturist.deriveFont(13f));
		btnInfo.setText(Global.getLabels("a5"));
		btnInfo.addMouseListener(new MouseAdapter() {
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
				daiInformazioni(selezionato);
			}
		});
		pnlBot.add(btnInfo);
		
		btnCompra = new RoundedCornerButton();
		btnCompra.setFont(fontFuturist.deriveFont(13f));
		btnCompra.setText(Global.getLabels("s81"));
		btnCompra.addMouseListener(new MouseAdapter() {
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
				if(selezionato == null || !valoriDiGioco.getValoriOro().containsKey(selezionato))
				{
					JOptionPane.showMessageDialog(null, Global.getLabels("s82"),Global.getLabels("e7"), JOptionPane.DEFAULT_OPTION);
				}
				else
				if(partita.getGiocatore().get(guiPartita.getIndiceProprietario()).getOro() < 
						valoriDiGioco.getValoriOro().get(selezionato)) //il giocatore non possiede abbastanza oro
				{
					JOptionPane.showMessageDialog(null, Global.getLabels("e13"),Global.getLabels("e7"), JOptionPane.DEFAULT_OPTION);
				}
				else
				if(partita.getGiocatore().get(guiPartita.getIndiceProprietario()).getMateriali() < 
						valoriDiGioco.getValoriMat().get(selezionato)) //il giocatore non possiede abbastanza materiali
				{
					JOptionPane.showMessageDialog(null, Global.getLabels("e14"),Global.getLabels("e7"), JOptionPane.DEFAULT_OPTION);
				}
				else
				{
					guiPartita.posizionaECompra(selezionato);
					dispose();
				}
			}
		});
		pnlBot.add(btnCompra);
		
		btnIndietro = new RoundedCornerButton();
		btnIndietro.setFont(fontFuturist.deriveFont(13f));
		btnIndietro.setText(Global.getLabels("a0"));
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
				dispose();
			}
		});
		pnlBot.add(btnIndietro);
		
		contentPane.add(pnlBot, BorderLayout.SOUTH);
		
		add(contentPane);
	}
	
	/**
	 * D� informazioni all'utente mediante JDialog in base alla costruzione selezionata
	 * @param nome Nome della costruzione
	 */
	public void daiInformazioni(String nome)
	{
		if(nome == null)
			JOptionPane.showMessageDialog(null, Global.getLabels("s83"), Global.getLabels("e7"), JOptionPane.DEFAULT_OPTION);
		else
		if(nome.equals( Global.getLabels("i53")) || nome.equals( Global.getLabels("i62")) || nome.equals( Global.getLabels("i63")))
			JOptionPane.showMessageDialog(null, nome+" "+Global.getLabels("s84"),Global.getLabels("a7"), JOptionPane.DEFAULT_OPTION);
		else
		if(nome.equals(Global.getLabels("i56")) || nome.equals(Global.getLabels("i57")) || nome.equals(Global.getLabels("i58")) || nome.equals(Global.getLabels("i59"))
				|| nome.equals(Global.getLabels("i60")) || nome.equals(Global.getLabels("i61")))
			JOptionPane.showMessageDialog(null, nome+" "+Global.getLabels("s85"),Global.getLabels("a7"), JOptionPane.DEFAULT_OPTION);
		else
		if(nome.equals(Global.getLabels("i0")) || nome.equals(Global.getLabels("i32")) || nome.equals(Global.getLabels("i41")))
			JOptionPane.showMessageDialog(null, nome+" "+Global.getLabels("s86"),Global.getLabels("a7"), JOptionPane.DEFAULT_OPTION);
		else
		if(nome.equals(Global.getLabels("i64")) || nome.equals(Global.getLabels("i65")) || nome.equals(Global.getLabels("i66")) || nome.equals(Global.getLabels("i42")))
			JOptionPane.showMessageDialog(null, nome+" "+Global.getLabels("s87"),Global.getLabels("a7"), JOptionPane.DEFAULT_OPTION);
		else
		if(nome.equals(Global.getLabels("i67")) || nome.equals(Global.getLabels("i68")) || nome.equals(Global.getLabels("i69")) || nome.equals(Global.getLabels("i70"))
				|| nome.equals(Global.getLabels("i71")) || nome.equals(Global.getLabels("i72")))
			JOptionPane.showMessageDialog(null, nome+" "+Global.getLabels("s88"),Global.getLabels("a7"), JOptionPane.DEFAULT_OPTION);
		else
		if(nome.equals(Global.getLabels("i24")))
			JOptionPane.showMessageDialog(null, nome+" "+Global.getLabels("s89"),Global.getLabels("a7"), JOptionPane.DEFAULT_OPTION);
		else
		if(nome.equals(Global.getLabels("i27")))
			JOptionPane.showMessageDialog(null, nome+" "+Global.getLabels("s90"),Global.getLabels("a7"), JOptionPane.DEFAULT_OPTION);
		else
		if(nome.equals(Global.getLabels("i73")) || nome.equals(Global.getLabels("i75")) || nome.equals(Global.getLabels("i76")))
			JOptionPane.showMessageDialog(null, nome+" "+Global.getLabels("s91"),Global.getLabels("a7"), JOptionPane.DEFAULT_OPTION);
		else
		if(nome.equals(Global.getLabels("32")))
			JOptionPane.showMessageDialog(null, nome+" "+Global.getLabels("s92"),Global.getLabels("a7"), JOptionPane.DEFAULT_OPTION);
		else
		if(nome.equals(Global.getLabels("i35")))
			JOptionPane.showMessageDialog(null, nome+" "+Global.getLabels("s93"),Global.getLabels("a7"), JOptionPane.DEFAULT_OPTION);
		else
		if(nome.equals(Global.getLabels("i39")))
			JOptionPane.showMessageDialog(null, nome+" "+Global.getLabels("s94"),Global.getLabels("a7"), JOptionPane.DEFAULT_OPTION);
		else
		if(nome.equals(Global.getLabels("i74")))
			JOptionPane.showMessageDialog(null, nome+" "+Global.getLabels("s95"),Global.getLabels("a7"), JOptionPane.DEFAULT_OPTION);
	}
	

	/**
	 * Riempie la pagina negozio in base all'et� selezionata
	 * @param eta Et� selezionata
	 * @param evidenzia In caso una voce sia stata selezionata in precedenza
	 */
	public void popolaNegozio(String eta, String evidenzia)
	{
		int aggiunte = 0;
		
		if(eta == Global.getLabels("s76"))
		{
			pnlMid.removeAll();
			
			
			for(String obj: partita.getGiocatore().get(partita.getGuiPartita().getIndiceProprietario()).getRicercheEffettuate()) //per ogni ricerca
			{
				if(obj.equals(Global.getLabels("i77")))
				{
					aggiungiVoce(Global.getLabels("i53"), iconeGrafiche.newiconSentieri, eta, evidenzia);
					aggiunte++;
				}
				else
				if(obj.equals(Global.getLabels("i78")))
				{
					aggiungiVoce(Global.getLabels("i56"), iconeGrafiche.newiconAbitazioni, eta, evidenzia);
					aggiunte++;
				}
				else
				if(obj.equals(Global.getLabels("i0")))
				{
					aggiungiVoce(Global.getLabels("i0"), iconeGrafiche.newiconCaserma, eta, evidenzia);
					aggiunte++;
				}
				else
				if(obj.equals(Global.getLabels("i64")))
				{
					aggiungiVoce(Global.getLabels("i64"), iconeGrafiche.newiconFucina, eta, evidenzia);
					aggiunte++;
				}
				else
				if(obj.equals(Global.getLabels("i79")))
				{
					aggiungiVoce(Global.getLabels("i67"), iconeGrafiche.newiconCommercio, eta, evidenzia);
					aggiunte++;
				}
				else
				if(obj.equals(Global.getLabels("i80")))
				{
					aggiungiVoce(Global.getLabels("i68"), iconeGrafiche.newiconOreficeria, eta, evidenzia);
					aggiunte++;
				}
				else
				if(obj.equals(Global.getLabels("i81")))
				{
					aggiungiVoce(Global.getLabels("i24"), iconeGrafiche.newiconReligione, eta, evidenzia);
					aggiunte++;
				}
				else
				if(obj.equals(Global.getLabels("i82")))
				{
					aggiungiVoce(Global.getLabels("i57"), iconeGrafiche.newiconVille, eta, evidenzia);
					aggiunte++;
				}
				else
				if(obj.equals(Global.getLabels("i83")))
				{
					aggiungiVoce(Global.getLabels("i27"), iconeGrafiche.newiconCitta, eta, evidenzia);
					aggiunte++;
				}
			}
			
			for(int i = aggiunte; i < 11; i++)
			{
				pnlMid.add(new JLabel(" "));
				pnlMid.add(new JLabel(" "));
				pnlMid.add(new JLabel(" "));
			}
			
			pnlMid.setVisible(false);
			pnlMid.setVisible(true);
		}
		
		if(eta == Global.getLabels("s77"))
		{
			pnlMid.removeAll();
			
			
			for(String obj: partita.getGiocatore().get(partita.getGuiPartita().getIndiceProprietario()).getRicercheEffettuate()) //per ogni ricerca
			{
				if(obj.equals(Global.getLabels("i84")))
				{
					aggiungiVoce(Global.getLabels("i62"), iconeGrafiche.newiconLastr, eta, evidenzia);
					aggiunte++;
				}
				else
				if(obj.equals(Global.getLabels("i85")))
				{
					aggiungiVoce(Global.getLabels("i58"), iconeGrafiche.newiconCasaPP, eta, evidenzia);
					aggiunte++;
				}
				else
				if(obj.equals(Global.getLabels("i73")))
				{
					aggiungiVoce(Global.getLabels("i73"), iconeGrafiche.newiconBiblioteca, eta, evidenzia);
					aggiunte++;
				}
				else
				if(obj.equals(Global.getLabels("i86")))
				{
					aggiungiVoce(Global.getLabels("i32"), iconeGrafiche.newiconMercenari, eta, evidenzia);
					aggiunte++;
				}
				else
				if(obj.equals(Global.getLabels("i87")))
				{
					aggiungiVoce(Global.getLabels("i35"), iconeGrafiche.newiconClero, eta, evidenzia);
					aggiunte++;
				}
				else
				if(obj.equals(Global.getLabels("i39")))
				{
					aggiungiVoce(Global.getLabels("i39"), iconeGrafiche.newiconOspedale, eta, evidenzia);
					aggiunte++;
				}
				else
				if(obj.equals(Global.getLabels("i88")))
				{
					aggiungiVoce(Global.getLabels("i59"), iconeGrafiche.newiconCaseASchiera, eta, evidenzia);
					aggiunte++;
				}
				else
				if(obj.equals(Global.getLabels("i89")))
				{
					aggiungiVoce(Global.getLabels("i75"), iconeGrafiche.newiconInquisizione, eta, evidenzia);
					aggiunte++;
				}
				else
				if(obj.equals(Global.getLabels("i90")))
				{
					aggiungiVoce(Global.getLabels("i65"), iconeGrafiche.newiconGranai, eta, evidenzia);
					aggiunte++;
				}
				else
				if(obj.equals(Global.getLabels("i91")))
				{
					aggiungiVoce(Global.getLabels("i69"), iconeGrafiche.newiconFermentazione, eta, evidenzia);
					aggiunte++;
				}
				else
				if(obj.equals(Global.getLabels("i70")))
				{
					aggiungiVoce(Global.getLabels("i70"), iconeGrafiche.newiconBanca, eta, evidenzia);
					aggiunte++;
				}
			}
			
			for(int i = aggiunte; i < 11; i++)
			{
				pnlMid.add(new JLabel(" "));
				pnlMid.add(new JLabel(" "));
				pnlMid.add(new JLabel(" "));
			}
			
			pnlMid.setVisible(false);
			pnlMid.setVisible(true);
		}
		
		if(eta == Global.getLabels("s78"))
		{
			pnlMid.removeAll();
			
			
			for(String obj: partita.getGiocatore().get(partita.getGuiPartita().getIndiceProprietario()).getRicercheEffettuate()) //per ogni ricerca
			{
				if(obj.equals(Global.getLabels("i92")))
				{
					aggiungiVoce(Global.getLabels("i63"), iconeGrafiche.newiconStrAsf, eta, evidenzia);
					aggiunte++;
				}
				else
				if(obj.equals(Global.getLabels("i93")))
				{
					aggiungiVoce(Global.getLabels("i66"), iconeGrafiche.newiconSisInd, eta, evidenzia);
					aggiunte++;
				}
				else
				if(obj.equals(Global.getLabels("i94")))
				{
					aggiungiVoce(Global.getLabels("i76"), iconeGrafiche.newiconScienza, eta, evidenzia);
					aggiunte++;
				}
				else
				if(obj.equals(Global.getLabels("i95")))
				{
					aggiungiVoce(Global.getLabels("i60"), iconeGrafiche.newiconCasaMan, eta, evidenzia);
					aggiunte++;
				}
				else
				if(obj.equals(Global.getLabels("i96")))
				{
					aggiungiVoce(Global.getLabels("i41"), iconeGrafiche.newiconGerMil, eta, evidenzia);
					aggiunte++;
				}
				else
				if(obj.equals(Global.getLabels("i97")))
				{
					aggiungiVoce(Global.getLabels("i74"), iconeGrafiche.newiconMusLir, eta, evidenzia);
					aggiunte++;
				}
				else
				if(obj.equals(Global.getLabels("i98")))
				{
					aggiungiVoce(Global.getLabels("i71"), iconeGrafiche.newiconSocBor, eta, evidenzia);
					aggiunte++;
				}
				else
				if(obj.equals(Global.getLabels("i99")))
				{
					aggiungiVoce(Global.getLabels("i42"), iconeGrafiche.newiconPolitica, eta, evidenzia);
					aggiunte++;
				}
				else
				if(obj.equals(Global.getLabels("i100")))
				{
					aggiungiVoce(Global.getLabels("i72"), iconeGrafiche.newiconTeatri, eta, evidenzia);
					aggiunte++;
				}
				else
				if(obj.equals(Global.getLabels("i101")))
				{
					aggiungiVoce(Global.getLabels("i61"), iconeGrafiche.newiconVillette, eta, evidenzia);
					aggiunte++;
				}
			}
			
			for(int i = aggiunte; i < 11; i++)
			{
				pnlMid.add(new JLabel(" "));
				pnlMid.add(new JLabel(" "));
				pnlMid.add(new JLabel(" "));
			}
			
			pnlMid.setVisible(false);
			pnlMid.setVisible(true);
		}
	}
	
	/**
	 * Aggiunge voce all'elenco
	 * @param nome Nome della costruzione
	 * @param icona Icona della costruzione
	 * @param eta Et� della costruzione
	 * @param evidenzia Indica se la voce deve essere evidenziata (se selezionata) o no
	 */
	public void aggiungiVoce(String nome, ImageIcon icona, String eta, String evidenzia)
	{
		JLabel lblVoce = new JLabel(nome);
		lblVoce.setIcon(icona);
		pnlMid.add(lblVoce);
		
		JLabel lblOro = new JLabel();
		lblOro.setText(valoriDiGioco.getValoriOro().get(nome).toString());
		lblOro.setIcon(iconeGrafiche.newiconOro);
		pnlMid.add(lblOro);
		
		JLabel lblMat = new JLabel();
		lblMat.setText(valoriDiGioco.getValoriMat().get(nome).toString());
		lblMat.setIcon(iconeGrafiche.newiconMat);
		pnlMid.add(lblMat);
		
		if(nome.equals(evidenzia))
		{
			lblVoce.setOpaque(true);
			lblVoce.setBackground(Color.LIGHT_GRAY);
			lblOro.setOpaque(true);
			lblOro.setBackground(Color.LIGHT_GRAY);
			lblMat.setOpaque(true);
			lblMat.setBackground(Color.LIGHT_GRAY);
		}
		
		lblVoce.addMouseListener(new MouseAdapter() {
			@Override
			public void mouseReleased(MouseEvent arg0) {
				if(selezionato != nome)
				{
					selezionato = nome;
					popolaNegozio(eta, nome);
				}
			}
		});
		lblOro.addMouseListener(new MouseAdapter() {
			@Override
			public void mouseReleased(MouseEvent arg0) {
				if(selezionato != nome)
				{
					selezionato = nome;
					popolaNegozio(eta, nome);
				}
			}
		});
		lblMat.addMouseListener(new MouseAdapter() {
			@Override
			public void mouseReleased(MouseEvent arg0) {
				if(selezionato != nome)
				{
					selezionato = nome;
					popolaNegozio(eta, nome);
				}
			}
		});
	}
}
