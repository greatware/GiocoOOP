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
import java.awt.Image;
import java.awt.Point;
import java.awt.Toolkit;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import java.io.File;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

import javax.sound.sampled.AudioSystem;
import javax.sound.sampled.Clip;
import javax.sound.sampled.FloatControl;
import javax.sound.sampled.LineUnavailableException;
import javax.sound.sampled.UnsupportedAudioFileException;
import javax.swing.BorderFactory;
import javax.swing.ImageIcon;
import javax.swing.JButton;
import javax.swing.JDialog;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JOptionPane;
import javax.swing.JPanel;
import javax.swing.JScrollPane;
import javax.swing.UIManager;
import javax.swing.UnsupportedLookAndFeelException;
import javax.swing.border.EmptyBorder;

/**
 * GUI che permette all'utente di ricercare nuove tecnologie e progredire nel gioco
 * @author Werther e Lorenzo
 *
 */
public class GUIPartitaRicerca extends JDialog {
	
	private Font fontFuturist;
	private GridBagConstraints c;
	private JPanel pnlFooter;
	private JPanel contentPane;
	private JLabel lblTick;
	private JLabel lblCross;
	private int gridXEClassica;
	private int gridXMedioevo;
	private int gridXEVittoriana;
	private RoundedCornerButton btnIndietro;
	private IconeGrafiche iconeGrafiche;
	private Clip audio;
	
	private ValoriDiGioco valoriDiGioco; //Salvato qui per facilitarne l'accesso dalle funzioni di questa classe diverse dal costruttore
	private Partita partita;  //Salvato qui per facilitarne l'accesso dalle funzioni di questa classe diverse dal costruttore
	private GUIPartita guiPartita;
	
	private String proprietario;
	
	/**
	 * Costruisce la GUI di base e inizializza le variabili da mostrare all'utente
	 * @param partita riferimento a Partita
	 * @param guiPartita riferimento a GUIPartita
	 * @param valoriDiGioco riferimento a ValoriDiGioco
	 * @param proprietario Proprietario della GUI
	 */
	GUIPartitaRicerca(Partita partita, GUIPartita guiPartita, ValoriDiGioco valoriDiGioco, String proprietario) {
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
		
		setTitle(Global.getLabels("s121"));
		setBounds(0, 0, 1000, 550);
		setMinimumSize(new Dimension(650, 450));   
		setLocationRelativeTo(null);
		setLayout(new BorderLayout(0, 0));
		contentPane = new JPanel(new GridBagLayout());
		pnlFooter = new JPanel(new BorderLayout(0,0));
		setModal(true);
		
		this.valoriDiGioco = valoriDiGioco;
		this.partita = partita;
		this.guiPartita = guiPartita;
		this.proprietario = proprietario;
		
		iconeGrafiche = new IconeGrafiche();
		
		creaGUI();
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
		pnlFooter.add(btnIndietro, BorderLayout.EAST);
		
		add(new JScrollPane(contentPane, JScrollPane.VERTICAL_SCROLLBAR_AS_NEEDED, JScrollPane.HORIZONTAL_SCROLLBAR_AS_NEEDED), BorderLayout.CENTER);
		add(pnlFooter, BorderLayout.SOUTH);
	}
	
	/**
	 * Crea l'interfaccia grafica
	 */
	public void creaGUI()
	{
		int valX = 1; //dichiarato qui perch� serve solo per il corretto posizionamento orizzontale grafico delle ricerche
		
		//Setting icone tick e cross
		lblTick = new JLabel();
		lblTick.setIcon(iconeGrafiche.newiconTick);
		lblTick.setBorder(BorderFactory.createLineBorder(Color.BLACK));
		lblCross = new JLabel();
		lblCross.setIcon(iconeGrafiche.newiconCross);
		lblCross.setBorder(BorderFactory.createLineBorder(Color.BLACK));
		
		c = new GridBagConstraints();
		c.anchor = new GridBagConstraints().CENTER;
		c.fill = GridBagConstraints.BOTH;
		c.gridx = 0;
		c.gridy = 0;
		
		contentPane.add(new JLabel("      "), c);
		
		c.gridx++;
		
		//Ricerca sentieri
		gridXEClassica = valX;
		inserisci1(valX, 7, Global.getLabels("i77"), iconeGrafiche.newiconSentieri);
		
		from1to3();
		
		//Ricerca abitazioni, caserma, fucina
		valX += 4;
		inserisci3(valX, 4, Global.getLabels("i78"), Global.getLabels("i0"), Global.getLabels("i64"), iconeGrafiche.newiconAbitazioni, 
				iconeGrafiche.newiconCaserma, iconeGrafiche.newiconFucina);
		
		if(partita.getGuiPartita().getIndiceProprietario() != 0) //se la civilt� � diversa da romani
		{
			from3to2();
			
			//Ricerca commercio, fanteria
			valX += 4;
			inserisci2(valX, 4, Global.getLabels("i79"), Global.getLabels("i48"), iconeGrafiche.newiconCommercio, iconeGrafiche.newiconFanteria);
			
			from2to3();
		}
		else
		{
			from3to3();
			
			//Ricerca commercio, fanteria, oreficeria
			valX += 4;
			inserisci3(valX, 4, Global.getLabels("i79"),Global.getLabels("i48"), Global.getLabels("i80"), iconeGrafiche.newiconCommercio, 
					iconeGrafiche.newiconFanteria, iconeGrafiche.newiconOreficeria);
			
			from3to3();
		}
		
		//Ricerca religione, ville, tiro con l'arco
		valX += 4;
		inserisci3(valX, 4, Global.getLabels("i81"), Global.getLabels("i82"),Global.getLabels("i4"), iconeGrafiche.newiconReligione,
				iconeGrafiche.newiconVille, iconeGrafiche.newiconTiroConArco);
		
		if(partita.getGuiPartita().getIndiceProprietario() != 0) //se la civilt� del giocatore � diversa da romani
		{
			from3to3();
			//Ricerca oracolo,citt�, cavalleria
			valX += 4;
			inserisci3(valX, 4, Global.getLabels("i25"), Global.getLabels("i83"), Global.getLabels("i6"), iconeGrafiche.newiconOracolo,
					iconeGrafiche.newiconCitta, iconeGrafiche.newiconCavalleria);
			
			from3to1();
			
			if(partita.getGuiPartita().getIndiceProprietario() == 3) //Se utilizziamo i sassoni
			{
				//Ricerca societ� militare
				valX += 4;
				inserisci1(valX, 7, Global.getLabels("i30"), iconeGrafiche.newiconSocMil);
				
				from1to1();
			}
		}
		else //caso dei romani
		{
			from3to2();
			
			//Ricerca citt� e cavalleria
			valX += 4;
			inserisci2(valX, 4, Global.getLabels("i83"), Global.getLabels("i6"), iconeGrafiche.newiconCitta, iconeGrafiche.newiconCavalleria);
			
			from2to1();

			//Ricerca et� imperiale
			valX += 4;
			inserisci1(valX, 7, Global.getLabels("i28"), iconeGrafiche.newiconEtaImp);
			
			from1to1();
		}
		
		//Ricerca strade lastricate
		valX += 4;
		gridXMedioevo = valX;
		inserisci1(valX, 7, Global.getLabels("i84"), iconeGrafiche.newiconLastr);
		
		from1to3();
		
		//Ricerca case a pi� piani, corazze, balestre
		valX += 4;
		inserisci3(valX, 4, Global.getLabels("i85"), Global.getLabels("i8"), Global.getLabels("i10"), iconeGrafiche.newiconCasaPP,
				iconeGrafiche.newiconCorazze, iconeGrafiche.newiconBalestre);
		
		from3to3();
		
		//Ricerca biblioteca, mercenari, clero
		valX += 4;
		inserisci3(valX, 4, Global.getLabels("i73"),Global.getLabels("i86"), Global.getLabels("i87"), iconeGrafiche.newiconBiblioteca, 
				iconeGrafiche.newiconMercenari, iconeGrafiche.newiconClero);
		
		from3to3();
		
		//Ricerca Ospedale, case a schiera, tattiche di cavalleria
		valX += 4;
		inserisci3(valX, 4, Global.getLabels("i39"), Global.getLabels("i88"), Global.getLabels("i12"), iconeGrafiche.newiconOspedale, 
				iconeGrafiche.newiconCaseASchiera, iconeGrafiche.newiconTatCal);
		
		if(partita.getGuiPartita().getIndiceProprietario() == 0) //se il giocatore ha civilt� romana
		{
			from3to2();
			
			//Ricerca inquisizione, granai
			valX += 4;
			inserisci2(valX, 4, Global.getLabels("i89"), Global.getLabels("i90"), iconeGrafiche.newiconInquisizione, iconeGrafiche.newiconGranai);
			
			from2to2();
			
			//Ricerca fermentazione, polvere da sparo
			valX += 4;
			inserisci2(valX, 4, Global.getLabels("i91"), Global.getLabels("i14"), iconeGrafiche.newiconFermentazione,
					iconeGrafiche.newiconPolvereDaSparo);
			
			from2to1();
		}
		else
		{
			from3to1();
			
			//Ricerca granai
			valX += 4;
			inserisci1(valX, 7, Global.getLabels("i90"), iconeGrafiche.newiconGranai);
			
			from1to3();
			
			//Ricerca fermentazione, banca, polvere da sparo
			valX += 4;
			inserisci3(valX, 4, Global.getLabels("i91"), Global.getLabels("i70"),Global.getLabels("i14"), iconeGrafiche.newiconFermentazione, 
					iconeGrafiche.newiconBanca, iconeGrafiche.newiconPolvereDaSparo);
			
			from3to1();
		}
		
		//Ricerca strade asfaltate
		valX += 4;
		gridXEVittoriana = valX;
		inserisci1(valX, 7, Global.getLabels("i92"), iconeGrafiche.newiconStrAsf);
		
		from1to2();
		
		//Ricerca sistema industriale, scienza
		valX += 4;
		inserisci2(valX, 4, Global.getLabels("i93"), Global.getLabels("i94"), iconeGrafiche.newiconSisInd, iconeGrafiche.newiconScienza);
		
		from2to3();
		
		//Ricerca case con mansarda, fucili, gerarchia militare
		valX += 4;
		inserisci3(valX, 4,Global.getLabels("i95"), Global.getLabels("i16"), Global.getLabels("i96"), iconeGrafiche.newiconCasaMan,
				iconeGrafiche.newiconFucili, iconeGrafiche.newiconGerMil);
		
		from3to3();
		
		//Ricerca Societ� borghese, politica, tattiche in campo aperto
		valX += 4;
		inserisci3(valX, 4, Global.getLabels("i98"), Global.getLabels("i99"), Global.getLabels("i20"), iconeGrafiche.newiconSocBor,
				iconeGrafiche.newiconPolitica, iconeGrafiche.newiconTatCap);
		
		from3to3();
		
		if(partita.getGuiPartita().getIndiceProprietario() == 0 ||
				partita.getGuiPartita().getIndiceProprietario() == 3) //se civilt� � romana o sassone
		{
			//Ricerca musica lirica, villette, balistica
			valX += 4;
			inserisci3(valX, 4, Global.getLabels("i97"), Global.getLabels("i101"), Global.getLabels("i22"), iconeGrafiche.newiconMusLir, 
					iconeGrafiche.newiconVillette, iconeGrafiche.newiconBalistica);
			
			from3to1();
		}
		else
		{
			//Ricerca teatri, villette, balistica
			valX += 4;
			inserisci3(valX, 4, Global.getLabels("i100"), Global.getLabels("i101"), Global.getLabels("i22"), iconeGrafiche.newiconTeatri,
					iconeGrafiche.newiconVillette, iconeGrafiche.newiconBalistica);
			
			from3to1();
		}
		
		switch(partita.getGuiPartita().getIndiceProprietario())
		{
		case 0: //romani
			//Ricerca Carabinieri
			valX += 4;
			inserisci1(valX, 7, Global.getLabels("i43"), iconeGrafiche.newiconCarabinieri);
			break;
		case 1: //britanni
			//Ricerca guardie reali britanniche
			valX += 4;
			inserisci1(valX, 7, Global.getLabels("i44"), iconeGrafiche.newiconGuaRea);
			break;
		case 2: //galli
			//Ricerca galli
			valX += 4;
			inserisci1(valX, 7, Global.getLabels("i46"), iconeGrafiche.newiconLegione);
			break;
		case 3: //sassoni
			//Ricerca gardenkorps
			valX += 4;
			inserisci1(valX, 7, Global.getLabels("i47"), iconeGrafiche.newiconGK);
			break;
		}
		
		c.gridx++;
		contentPane.add(new JLabel("      "), c);
		
		dipingiBackground(gridXEClassica, gridXMedioevo, gridXEVittoriana);
	}
	
	/**
	 * Inserisce il background e i nomi dell'et� nell'interfaccia grafica
	 * @param gridXEClassica Delimita l'inizio dell'Et� classica
	 * @param gridXMedioevo Delimita l'inizio del Medioevo
	 * @param gridXEVittoriana Delimita l'inizio dell'Et� vittoriana
	 */
	public void dipingiBackground(int gridXEClassica, int gridXMedioevo, int gridXEVittoriana)
	{
		c.gridy = 1;
		c.gridx = gridXEClassica;
		JLabel lblECla = new JLabel(Global.getLabels("s114"));
		lblECla.setFont(fontFuturist.deriveFont(13f));
		contentPane.add(lblECla, c);
		c.gridy++;
		contentPane.add(new JLabel("  "), c);
		c.gridy++;
		contentPane.add(new JLabel("  "), c);
		
		c.gridy -= 2;
		c.gridx = gridXMedioevo;
		JLabel lblMed = new JLabel(Global.getLabels("s115"));
		lblMed.setFont(fontFuturist.deriveFont(13f));
		contentPane.add(lblMed, c);
		c.gridy++;
		contentPane.add(new JLabel("  "), c);
		c.gridy++;
		contentPane.add(new JLabel("  "), c);
		
		c.gridy -= 2;
		c.gridx = gridXEVittoriana;
		JLabel lblEVit = new JLabel(Global.getLabels("s116"));
		lblEVit.setFont(fontFuturist.deriveFont(13f));
		contentPane.add(lblEVit, c);
		c.gridy++;
		contentPane.add(new JLabel("  "), c);
		c.gridy++;
		contentPane.add(new JLabel("  "), c);
	}
	
	/**
	 * Costruisce la struttura grafica di collegamento da 1 a 1
	 */
	public void from1to1()
	{
		c.gridx++;
		c.gridy--;
		JLabel lblOriz = new JLabel();
		lblOriz.setIcon(iconeGrafiche.newiconOriz);
		contentPane.add(lblOriz, c);
		
		c.gridx++;
		JLabel lblOriz2 = new JLabel();
		lblOriz2.setIcon(iconeGrafiche.newiconOriz);
		contentPane.add(lblOriz2, c);
	}
	
	/**
	 * Costruisce la struttura grafica di collegamento da 1 a 2
	 */
	public void from1to2()
	{
		c.gridx++;
		c.gridy--;
		JLabel lblOriz = new JLabel();
		lblOriz.setIcon(iconeGrafiche.newiconOriz);
		contentPane.add(lblOriz, c);
		
		c.gridx++;
		JLabel lbl3su = new JLabel();
		lbl3su.setIcon(iconeGrafiche.newicon3su);
		contentPane.add(lbl3su, c);
		
		c.gridy--;
		JLabel lblVert = new JLabel();
		lblVert.setIcon(iconeGrafiche.newiconVert);
		contentPane.add(lblVert, c);
		
		c.gridy--;
		JLabel lblVert2 = new JLabel();
		lblVert2.setIcon(iconeGrafiche.newiconVert);
		contentPane.add(lblVert2, c);
		
		c.gridy--;
		JLabel lbl2dxgiu = new JLabel();
		lbl2dxgiu.setIcon(iconeGrafiche.newicon2dxgiu);
		contentPane.add(lbl2dxgiu, c);
	}
	
	/**
	 * Costruisce la struttura grafica di collegamento da 1 a 3
	 */
	public void from1to3() 
	{	
		c.gridx++;
		c.gridy--;
		JLabel lblOriz = new JLabel();
		lblOriz.setIcon(iconeGrafiche.newiconOriz);
		contentPane.add(lblOriz, c);
		
		c.gridx++;
		JLabel lbl4 = new JLabel();
		lbl4.setIcon(iconeGrafiche.newicon4);
		contentPane.add(lbl4, c);
		
		c.gridy--;
		JLabel lblVert = new JLabel();
		lblVert.setIcon(iconeGrafiche.newiconVert);
		contentPane.add(lblVert, c);
		
		c.gridy--;
		JLabel lblVert2 = new JLabel();
		lblVert2.setIcon(iconeGrafiche.newiconVert);
		contentPane.add(lblVert2, c);
		
		c.gridy--;
		JLabel lbl2dxgiu = new JLabel();
		lbl2dxgiu.setIcon(iconeGrafiche.newicon2dxgiu);
		contentPane.add(lbl2dxgiu, c);
		
		c.gridy += 4;
		JLabel lblVert3 = new JLabel();
		lblVert3.setIcon(iconeGrafiche.newiconVert);
		contentPane.add(lblVert3, c);
		
		c.gridy++;
		JLabel lblVert4 = new JLabel();
		lblVert4.setIcon(iconeGrafiche.newiconVert);
		contentPane.add(lblVert4, c);
		
		c.gridy++;
		JLabel lbl2sudx = new JLabel();
		lbl2sudx.setIcon(iconeGrafiche.newicon2sudx);
		contentPane.add(lbl2sudx, c);
	}
	
	/**
	 * Costruisce la struttura grafica di collegamento da 2 a 1
	 */
	public void from2to1()
	{
		c.gridy -= 4;
		c.gridx++;
		JLabel lblOriz = new JLabel();
		lblOriz.setIcon(iconeGrafiche.newiconOriz);
		contentPane.add(lblOriz, c);
		
		c.gridx++;
		JLabel lbl2giusx = new JLabel();
		lbl2giusx.setIcon(iconeGrafiche.newicon2giusx);
		contentPane.add(lbl2giusx, c);
		
		c.gridy++;
		JLabel lblVert = new JLabel();
		lblVert.setIcon(iconeGrafiche.newiconVert);
		contentPane.add(lblVert, c);
		
		c.gridy++;
		JLabel lblVert2 = new JLabel();
		lblVert2.setIcon(iconeGrafiche.newiconVert);
		contentPane.add(lblVert2, c);
		
		c.gridy++;
		c.gridx--;
		JLabel lblOriz2 = new JLabel();
		lblOriz2.setIcon(iconeGrafiche.newiconOriz);
		contentPane.add(lblOriz2, c);
		
		c.gridx++;
		JLabel lbl3su = new JLabel();
		lbl3su.setIcon(iconeGrafiche.newicon3su);
		contentPane.add(lbl3su, c);
	}
	
	/**
	 * Costruisce la struttura grafica di collegamento da 2 a 2
	 */
	public void from2to2()
	{
		c.gridy -= 4;
		c.gridx++;
		JLabel lblOriz = new JLabel();
		lblOriz.setIcon(iconeGrafiche.newiconOriz);
		contentPane.add(lblOriz, c);
		
		c.gridx++;
		JLabel lbl3giu = new JLabel();
		lbl3giu.setIcon(iconeGrafiche.newicon3giu);
		contentPane.add(lbl3giu, c);
		
		c.gridy++;
		JLabel lblVert = new JLabel();
		lblVert.setIcon(iconeGrafiche.newiconVert);
		contentPane.add(lblVert, c);
		
		c.gridy++;
		JLabel lblVert2 = new JLabel();
		lblVert2.setIcon(iconeGrafiche.newiconVert);
		contentPane.add(lblVert2, c);
		
		c.gridy++;
		c.gridx--;
		JLabel lblOriz2 = new JLabel();
		lblOriz2.setIcon(iconeGrafiche.newiconOriz);
		contentPane.add(lblOriz2, c);
		
		c.gridx++;
		JLabel lbl3su = new JLabel();
		lbl3su.setIcon(iconeGrafiche.newicon3su);
		contentPane.add(lbl3su, c);
	}
	
	/**
	 * Costruisce la struttura grafica di collegamento da 3 a 1
	 */
	public void from3to1()
	{
		c.gridy -= 7;
		c.gridx++;
		JLabel lblOriz = new JLabel();
		lblOriz.setIcon(iconeGrafiche.newiconOriz);
		contentPane.add(lblOriz, c);
		
		c.gridx++;
		JLabel lbl2giusx = new JLabel();
		lbl2giusx.setIcon(iconeGrafiche.newicon2giusx);
		contentPane.add(lbl2giusx, c);
		
		c.gridy++;
		JLabel lblVert = new JLabel();
		lblVert.setIcon(iconeGrafiche.newiconVert);
		contentPane.add(lblVert, c);
		
		c.gridy++;
		JLabel lblVert2 = new JLabel();
		lblVert2.setIcon(iconeGrafiche.newiconVert);
		contentPane.add(lblVert2, c);
		
		c.gridy++;
		c.gridx--;
		JLabel lblOriz2 = new JLabel();
		lblOriz2.setIcon(iconeGrafiche.newiconOriz);
		contentPane.add(lblOriz2, c);
		
		c.gridx++;
		JLabel lbl4 = new JLabel();
		lbl4.setIcon(iconeGrafiche.newicon4);
		contentPane.add(lbl4, c);
		
		c.gridy++;
		JLabel lblVert3 = new JLabel();
		lblVert3.setIcon(iconeGrafiche.newiconVert);
		contentPane.add(lblVert3, c);
		
		c.gridy++;
		JLabel lblVert4 = new JLabel();
		lblVert4.setIcon(iconeGrafiche.newiconVert);
		contentPane.add(lblVert4, c);
		
		c.gridy++;
		c.gridx--;
		JLabel lblOriz3 = new JLabel();
		lblOriz3.setIcon(iconeGrafiche.newiconOriz);
		contentPane.add(lblOriz3, c);
		
		c.gridx++;
		JLabel lbl2sxsu = new JLabel();
		lbl2sxsu.setIcon(iconeGrafiche.newicon2sxsu);
		contentPane.add(lbl2sxsu, c);
	}
	
	/**
	 * Costruisce la struttura grafica di collegamento da 3 a 2
	 */
	public void from3to2()
	{
		c.gridy -= 7;
		c.gridx++;
		JLabel lblOriz = new JLabel();
		lblOriz.setIcon(iconeGrafiche.newiconOriz);
		contentPane.add(lblOriz, c);
		
		c.gridx++;
		JLabel lbl3giu = new JLabel();
		lbl3giu.setIcon(iconeGrafiche.newicon3giu);
		contentPane.add(lbl3giu, c);
		
		c.gridy++;
		JLabel lblVert = new JLabel();
		lblVert.setIcon(iconeGrafiche.newiconVert);
		contentPane.add(lblVert, c);
		
		c.gridy++;
		JLabel lblVert2 = new JLabel();
		lblVert2.setIcon(iconeGrafiche.newiconVert);
		contentPane.add(lblVert2, c);
		
		c.gridy++;
		c.gridx--;
		JLabel lblOriz2 = new JLabel();
		lblOriz2.setIcon(iconeGrafiche.newiconOriz);
		contentPane.add(lblOriz2, c);
		
		c.gridx++;
		JLabel lbl3su = new JLabel();
		lbl3su.setIcon(iconeGrafiche.newicon3su);
		contentPane.add(lbl3su, c);
	}
	
	/**
	 * Costruisce la struttura grafica di collegamento da 2 a 3
	 */
	public void from2to3()
	{
		c.gridy -= 4;
		c.gridx++;
		JLabel lblOriz = new JLabel();
		lblOriz.setIcon(iconeGrafiche.newiconOriz);
		contentPane.add(lblOriz, c);
		
		c.gridx++;
		JLabel lbl3giu = new JLabel();
		lbl3giu.setIcon(iconeGrafiche.newicon3giu);
		contentPane.add(lbl3giu, c);
		
		c.gridy++;
		JLabel lblVert = new JLabel();
		lblVert.setIcon(iconeGrafiche.newiconVert);
		contentPane.add(lblVert, c);
		
		c.gridy++;
		JLabel lblVert2 = new JLabel();
		lblVert2.setIcon(iconeGrafiche.newiconVert);
		contentPane.add(lblVert2, c);
		
		c.gridy++;
		c.gridx--;
		JLabel lblOriz2 = new JLabel();
		lblOriz2.setIcon(iconeGrafiche.newiconOriz);
		contentPane.add(lblOriz2, c);
		
		c.gridx++;
		JLabel lbl4 = new JLabel();
		lbl4.setIcon(iconeGrafiche.newicon4);
		contentPane.add(lbl4, c);
		
		c.gridy++;
		JLabel lblVert3 = new JLabel();
		lblVert3.setIcon(iconeGrafiche.newiconVert);
		contentPane.add(lblVert3, c);
		
		c.gridy++;
		JLabel lblVert4 = new JLabel();
		lblVert4.setIcon(iconeGrafiche.newiconVert);
		contentPane.add(lblVert4, c);
		
		c.gridy++;
		JLabel lbl2sudx = new JLabel();
		lbl2sudx.setIcon(iconeGrafiche.newicon2sudx);
		contentPane.add(lbl2sudx, c);
	}
	
	/**
	 * Costruisce la struttura grafica di collegamento da 3 a 3
	 */
	public void from3to3()
	{
		c.gridy -= 7;
		c.gridx++;
		JLabel lblOriz = new JLabel();
		lblOriz.setIcon(iconeGrafiche.newiconOriz);
		contentPane.add(lblOriz, c);
		
		c.gridx++;
		JLabel lbl3giu = new JLabel();
		lbl3giu.setIcon(iconeGrafiche.newicon3giu);
		contentPane.add(lbl3giu, c);
		
		c.gridy++;
		JLabel lblVert = new JLabel();
		lblVert.setIcon(iconeGrafiche.newiconVert);
		contentPane.add(lblVert, c);
		
		c.gridy++;
		JLabel lblVert2 = new JLabel();
		lblVert2.setIcon(iconeGrafiche.newiconVert);
		contentPane.add(lblVert2, c);
		
		c.gridy++;
		c.gridx--;
		JLabel lblOriz2 = new JLabel();
		lblOriz2.setIcon(iconeGrafiche.newiconOriz);
		contentPane.add(lblOriz2, c);
		
		c.gridx++;
		JLabel lbl4 = new JLabel();
		lbl4.setIcon(iconeGrafiche.newicon4);
		contentPane.add(lbl4, c);
		
		c.gridy++;
		JLabel lblVert3 = new JLabel();
		lblVert3.setIcon(iconeGrafiche.newiconVert);
		contentPane.add(lblVert3, c);
		
		c.gridy++;
		JLabel lblVert4 = new JLabel();
		lblVert4.setIcon(iconeGrafiche.newiconVert);
		contentPane.add(lblVert4, c);
		
		c.gridy++;
		c.gridx--;
		JLabel lblOriz3 = new JLabel();
		lblOriz3.setIcon(iconeGrafiche.newiconOriz);
		contentPane.add(lblOriz3, c);
		
		c.gridx++;
		JLabel lbl3su = new JLabel();
		lbl3su.setIcon(iconeGrafiche.newicon3su);
		contentPane.add(lbl3su, c);
	}
	
	/**
	 * Inserisce 2 tecnologie nell'interfaccia in colonna verticale
	 * @param posx Posizione x iniziale
	 * @param posy Posizione y iniziale
	 * @param nome1 Nome ricerca 1
	 * @param nome2 Nome ricerca 2
	 * @param icona1 Icona ricerca 1
	 * @param icona2 Icona ricerca 2
	 */
	public void inserisci2(int posx, int posy, String nome1, String nome2,
			ImageIcon icona1, ImageIcon icona2) {
		
		inserisci1(posx, posy, nome1, icona1);
		inserisci1(posx, posy+3, nome2, icona2);
	}
	
	/**
	 * Inserisce 3 tecnologie nell'interfaccia in colonna verticale
	 * @param posx Posizione iniziale X
	 * @param posy Posizione iniziale Y
	 * @param nome1 Nome ricerca 1
	 * @param nome2 Nome ricerca 2
	 * @param nome3 Nome ricerca 3
	 * @param icona1 Icona ricerca 1
	 * @param icona2 Icona ricerca 2
	 * @param icona3 Icona ricerca 3
	 */
	public void inserisci3(int posx, int posy, String nome1, String nome2, String nome3, ImageIcon icona1, ImageIcon icona2,
			ImageIcon icona3) {
		inserisci1(posx, posy, nome1, icona1);
		inserisci1(posx, posy+3, nome2, icona2);
		inserisci1(posx, posy+6, nome3, icona3);
	}
	
	/**
	 * Inserisce 4 tecnologie nell'interfaccia in colonna verticale
	 * @param posx Posizione iniziale x
	 * @param posy Posizione iniziale y
	 * @param nome1 Nome ricerca 1
	 * @param nome2 Nome ricerca 2
	 * @param nome3 Nome ricerca 3
	 * @param nome4 Nome ricerca 4
	 * @param icona1 Icona ricerca 1
	 * @param icona2 Icona ricerca 2
	 * @param icona3 Icona ricerca 3
	 * @param icona4 Icona ricerca 4
	 */
	public void inserisci4(int posx, int posy, String nome1, String nome2, String nome3, String nome4, 
			ImageIcon icona1, ImageIcon icona2, ImageIcon icona3, ImageIcon icona4) {
		inserisci1(posx, posy, nome1, icona1);
		inserisci1(posx, posy+3, nome2, icona2);
		inserisci1(posx, posy+6, nome3, icona3);
		inserisci1(posx, posy+9, nome4, icona4);
	}
	
	/**
	 * Inserisce 1 tecnologia nell'interfaccia
	 * @param posx Posizione iniziale x
	 * @param posy Posizione iniziale y
	 * @param nome Nome ricerca
	 * @param icona Icona ricerca
	 */
	public void inserisci1(int posx, int posy, String nome, ImageIcon icona) {

		//Aggiungo il nome della ricerca
		c.gridx = posx;
		c.gridy = posy;
		
		JLabel lblNome = new JLabel("  " + nome + "  ");
		lblNome.setBorder(BorderFactory.createLineBorder(Color.BLACK));
		contentPane.add(lblNome, c);
		
		JLabel lblcpyTick = new JLabel();  //creata una copia della lblTick per poter utilizzare l'icona tick pi� volte
		lblcpyTick.setIcon(lblTick.getIcon());
		lblcpyTick.setBorder(BorderFactory.createLineBorder(Color.BLACK));
		JLabel lblcpyCross = new JLabel(); //creata una copia della lblCross per poter utilizzare l'icona cross pi� volte
		lblcpyCross.setIcon(lblCross.getIcon());
		lblcpyCross.setBorder(BorderFactory.createLineBorder(Color.BLACK));
		
		//Aggiungo l'icona della ricerca
		c.gridx++;
		JLabel lblIcon = new JLabel();
		lblIcon.setIcon(icona);
		lblIcon.setBorder(BorderFactory.createLineBorder(Color.BLACK));
		lblIcon.addMouseListener(new MouseAdapter() {
			@Override
			public void mouseReleased(MouseEvent arg0) {
				if(!partita.getGiocatore().get(partita.getTurnoCorrente()).getProprietario().equals(proprietario)) {
					/*controllo che il turno non sia di un altro giocatore, se � di un altro giocatore non � permesso
					 * ricercare nulla e mostro un avviso che lo comunica.
					 */
					JOptionPane.showMessageDialog(null,Global.getLabels("e15"),Global.getLabels("e7"), JOptionPane.DEFAULT_OPTION);
				}
				else
				{
					if(partita.getGiocatore().get(partita.getGuiPartita().getIndiceProprietario()).getRicercheEffettuate().contains(nome)) //se gi� ricercata
					{
						JOptionPane.showMessageDialog(null, Global.getLabels("e16"),
								Global.getLabels("e7"),JOptionPane.DEFAULT_OPTION);
					}
					else
					{
						if(!isRicercaRicercabile(nome))
						{
							JOptionPane.showMessageDialog(null, Global.getLabels("e17"),
									Global.getLabels("e7"), JOptionPane.DEFAULT_OPTION);
						}
						else
						{
							if(partita.getGiocatore().get(partita.getGuiPartita().getIndiceProprietario()).getPuntiRicerca() < 
									valoriDiGioco.getValoriRicerche().get(nome)) //se i PR del giocatore sono insufficienti
							{
								JOptionPane.showMessageDialog(null,Global.getLabels("e18"),
										Global.getLabels("e7"), JOptionPane.DEFAULT_OPTION);
							}
							else
							{
								int scelta =0;
								Integer rimanente = partita.getGiocatore().get(partita.getGuiPartita().getIndiceProprietario()).getPuntiRicerca() - 
										valoriDiGioco.getValoriRicerche().get(nome);
								scelta = JOptionPane.showConfirmDialog(
										    null, Global.getLabels("s117")+nome+" "+Global.getLabels("s118")+valoriDiGioco.getValoriRicerche().get(nome).toString()+" "+
										    Global.getLabels("s119")+rimanente.toString()+" "+Global.getLabels("s120"), Global.getLabels("a6"),
										    JOptionPane.YES_NO_OPTION, JOptionPane.QUESTION_MESSAGE);
								if(scelta == 0) //se si
								{
									try {
										audio = AudioSystem.getClip();
										audio.open(AudioSystem.getAudioInputStream(new File("media/ricercaeffettuata.wav")));
									} catch (LineUnavailableException | IOException | UnsupportedAudioFileException e1) {
										e1.printStackTrace();
									}
									FloatControl gainControl = (FloatControl) audio.getControl(FloatControl.Type.MASTER_GAIN);
									gainControl.setValue(Global.getLivVolume()); 
									audio.start();
									partita.getGiocatore().get(partita.getGuiPartita().getIndiceProprietario()).setPuntiRicerca(rimanente);
									partita.getGiocatore().get(partita.getGuiPartita().getIndiceProprietario()).getRicercheEffettuate().add(nome);
									contentPane.remove(lblcpyCross);
									c.gridx = posx+1;
									c.gridy = posy+1;
									contentPane.add(lblcpyTick, c);
									guiPartita.aggiornaDatiGUI();
									contentPane.setVisible(false); //fa una sorta di refresh
									contentPane.setVisible(true);
								}
							}
						}
					}
				}
			}
		});
		contentPane.add(lblIcon, c);
		
		//Aggiungo il costo in punti ricerca
		c.gridx = posx;
		c.gridy++;
		JLabel lblCosto = new JLabel("  " + Integer.toString(valoriDiGioco.getValoriRicerche().get(nome)) + "  ");
		lblCosto.setIcon(iconeGrafiche.newiconCosto);
		lblCosto.setBorder(BorderFactory.createLineBorder(Color.BLACK));
		contentPane.add(lblCosto, c);
		
		
		//Aggiungo lo stato corrente della ricerca (tick o cross)
		c.gridx++;
		
		if(partita.getGiocatore().get(partita.getGuiPartita().getIndiceProprietario()).getRicercheEffettuate().contains(nome)) 
		{
			contentPane.add(lblcpyTick, c);
		}
		else
		{
			contentPane.add(lblcpyCross, c);
		}
	}
	
	/**
	 * Controlla se la ricerca selezionabile � ricercabile (cio� solo se tutte le precedenti sono gi� state ricercate)
	 * @param nome Nome della ricerca
	 * @return Ritorna true se � possibile ricercare la tecnologia, false altrimenti
	 */
	public boolean isRicercaRicercabile(String nome)
	{
		List<String> ric = new ArrayList<String>();
		ric = partita.getGiocatore().get(partita.getGuiPartita().getIndiceProprietario()).getRicercheEffettuate();
		int civ = partita.getGuiPartita().getIndiceProprietario();
		
		if(nome.equals(Global.getLabels("i78")) || nome.equals(Global.getLabels("i0")) || nome.equals(Global.getLabels("i64"))) {
			if(!ric.contains(Global.getLabels("i77")))
				return false; }
		else
		if(nome.equals(Global.getLabels("i79")) || nome.equals(Global.getLabels("i48")) || nome.equals(Global.getLabels("i80"))) {
			if(!ric.contains(Global.getLabels("i78")) || !ric.contains(Global.getLabels("i0")) || !ric.contains(Global.getLabels("i64")))
				return false; }
		else
		if(nome.equals(Global.getLabels("i81")) || nome.equals(Global.getLabels("i82")) || nome.equals(Global.getLabels("i4"))) {
			if(!ric.contains(Global.getLabels("i79")) || !ric.contains(Global.getLabels("i48")))
				return false; 
			if(civ == 0 && !ric.contains(Global.getLabels("i80")))
				return false;}
		else
		if(nome.equals(Global.getLabels("i25")) || nome.equals(Global.getLabels("i83")) || nome.equals(Global.getLabels("i6"))) {
			if(!ric.contains(Global.getLabels("i81")) || !ric.contains(Global.getLabels("i82")) || !ric.contains(Global.getLabels("i4")))
				return false; }
		else
		if(nome.equals(Global.getLabels("i84"))) {
			if(!ric.contains(Global.getLabels("i83")) || !ric.contains(Global.getLabels("i6")))
				return false;
			if(civ != 0 && !ric.contains(Global.getLabels("i25")))
				return false; 
			if(civ == 0 && !ric.contains(Global.getLabels("i28")))
				return false; }
		else
		if(nome.equals(Global.getLabels("i85")) || nome.equals(Global.getLabels("i8")) || nome.equals(Global.getLabels("i10"))) {
			if(!ric.contains(Global.getLabels("i84")))
				return false; }
		else
		if(nome.equals(Global.getLabels("i73")) || nome.equals(Global.getLabels("i86")) || nome.equals(Global.getLabels("i87"))) {
			if(!ric.contains(Global.getLabels("i85")) || !ric.contains(Global.getLabels("i8")) || !ric.contains(Global.getLabels("i10")))
				return false; }
		else
		if(nome.equals(Global.getLabels("i39")) || nome.equals(Global.getLabels("i88")) || nome.equals(Global.getLabels("i12"))) {
			if(!ric.contains(Global.getLabels("i73")) || !ric.contains(Global.getLabels("i86")) || !ric.contains(Global.getLabels("i87")))
				return false; }
		else
		if(nome.equals(Global.getLabels("i90")) || nome.equals(Global.getLabels("i89"))) {
			if(!ric.contains(Global.getLabels("i39")) || !ric.contains(Global.getLabels("i88")) || !ric.contains(Global.getLabels("i12")))
				return false; }
		else
		if(nome.equals(Global.getLabels("i91")) || nome.equals(Global.getLabels("i70")) || nome.equals(Global.getLabels("i14"))) {
			if(!ric.contains(Global.getLabels("i90")))
				return false; 
			if(civ == 0 && !ric.contains(Global.getLabels("i89")))
				return false; }
		else
		if(nome.equals(Global.getLabels("i92"))) {
			if(!ric.contains(Global.getLabels("i91")) || !ric.contains(Global.getLabels("i14")))
				return false; 
			if(civ != 0 && !ric.contains(Global.getLabels("i70")))
				return false; }
		else
		if(nome.equals(Global.getLabels("i93")) || nome.equals(Global.getLabels("i94"))) {
			if(!ric.contains(Global.getLabels("i92")))
				return false; }
		else
		if(nome.equals(Global.getLabels("i95")) || nome.equals(Global.getLabels("i16")) || nome.equals(Global.getLabels("i96"))) {
			if(!ric.contains(Global.getLabels("i93")) || !ric.contains(Global.getLabels("i94")))
				return false; }
		else
		if(nome.equals(Global.getLabels("i98")) || nome.equals(Global.getLabels("i99")) || nome.equals(Global.getLabels("i20"))) {
			if(!ric.contains(Global.getLabels("i95")) || !ric.contains(Global.getLabels("i16")) || !ric.contains(Global.getLabels("i96")))
				return false; }
		else
		if(nome.equals(Global.getLabels("i100")) || nome.equals(Global.getLabels("i101")) || nome.equals(Global.getLabels("i22")) || nome.equals(Global.getLabels("i97"))) {
			if(!ric.contains(Global.getLabels("i98")) || !ric.contains(Global.getLabels("i99")) || !ric.contains(Global.getLabels("i20")))
				return false;
		}
		else
		if(nome.equals(Global.getLabels("i43"))) {
			if(!ric.contains(Global.getLabels("i97")) || !ric.contains(Global.getLabels("i101")) || !ric.contains(Global.getLabels("i22")))
				return false;
		}
		if(nome.equals(Global.getLabels("i44"))) {
			if(!ric.contains(Global.getLabels("i100")) || !ric.contains(Global.getLabels("i101")) || !ric.contains(Global.getLabels("i22")))
				return false; }
		else
		if(nome.equals(Global.getLabels("i46"))) {
				if(!ric.contains(Global.getLabels("i100")) || !ric.contains(Global.getLabels("i101")) || !ric.contains(Global.getLabels("i22")))
					return false; }
		else
		if(nome.equals(Global.getLabels("i47"))) {
			if(!ric.contains(Global.getLabels("i97")) || !ric.contains(Global.getLabels("i101")) || !ric.contains(Global.getLabels("i22")))
				return false; }
		return true;
	}
}