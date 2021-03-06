package gioco;

import java.awt.BorderLayout;
import java.awt.Color;
import java.awt.Font;
import java.awt.FontFormatException;
import java.awt.GraphicsEnvironment;
import java.awt.GridBagConstraints;
import java.awt.GridBagLayout;
import java.awt.Image;
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
import javax.swing.BorderFactory;
import javax.swing.ImageIcon;
import javax.swing.JComboBox;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.UIManager;
import javax.swing.UnsupportedLookAndFeelException;

/**
 * GUI che definisce gli obiettivi raggiungibili in gioco dal giocatore e tiene traccia degli obiettivi gi� raggiunti
 * @author Werther e Lorenzo
 *
 */
public class GUIObiettivi extends JPanel {
	private JPanel pnlMenu;
	private GridBagConstraints c;
	private RoundedCornerButton btnIndietro;
	private Font fontFuturist;
	private Clip audio;
	private GUIMenuPrincipale guiMenuPrincipale; //Inserito per avere un riferimento a menu principale
	private GUIObiettivi guiObiettivi; //Utilizzato per mandare indietro a GUIMenuPrincipale la dimensione del jframe
	
	/**
	 * Modella la GUI Obiettivi
	 * @param guiMenuPrincipale riferimento a GUIMenuPrincipale
	 */
	GUIObiettivi(GUIMenuPrincipale guiMenuPrincipale) {
		
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
			guiObiettivi = this;
			
			c = new GridBagConstraints(); 
		
			//Prima Colonna
			c.gridx = 1;
			c.gridy = 0;
			c.anchor = GridBagConstraints.LINE_END;


			pnlMenu.add(new JLabel("Coming soon"), c);
			c.gridy ++;
			pnlMenu.add(new JLabel(" "), c);
			c.gridy ++;
			pnlMenu.add(new JLabel("         "), c);
			c.gridy ++;
			pnlMenu.add(new JLabel(" "), c);
			c.gridy ++;
			pnlMenu.add(new JLabel("          "), c);
			c.gridy ++;
			pnlMenu.add(new JLabel(" "), c);
			c.gridy ++;
			pnlMenu.add(new JLabel("         "), c);
			c.gridy ++;
			pnlMenu.add(new JLabel(" "), c);
			c.gridy ++;
			pnlMenu.add(new JLabel("           "), c);
			
			//Seconda colonna
			c.gridy = 0;
			c.gridx = 2;
			c.anchor = GridBagConstraints.LINE_START;


		
			pnlMenu.add(new JLabel("     "), c);
			c.gridy ++;
			pnlMenu.add(new JLabel(" "), c);
			c.gridy ++;
			pnlMenu.add(new JLabel("      "), c);
			c.gridy ++;
			pnlMenu.add(new JLabel(" "), c);
			c.gridy ++;
			pnlMenu.add(new JLabel(" "), c);
			c.gridy ++;
			pnlMenu.add(new JLabel(" "), c);
			c.gridy ++;
			pnlMenu.add(new JLabel(" "), c);
			c.gridy ++;
			pnlMenu.add(new JLabel(" "), c);
			c.gridy ++;
			pnlMenu.add(new JLabel(" "), c);
			//Fondo
			c.gridx = 0;
			c.gridy ++;
			pnlMenu.add(new JLabel(" "), c);
			c.gridy ++;
			pnlMenu.add(new JLabel(" "), c);
			c.gridy ++;

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
					guiMenuPrincipale.ripristinaPanelDx(guiObiettivi.getWidth(), guiObiettivi.getHeight());
				}
			});
			pnlMenu.add(btnIndietro, c);

			c.gridy -= 2;
			c.gridx += 3;
			pnlMenu.add(new JLabel(" "), c);
			c.gridy ++;
			pnlMenu.add(new JLabel(" "), c);
			c.gridy ++;
			pnlMenu.add(new JLabel("                                                                                "),c); //Stringa vuota per modificare la struttura generale

			add(pnlMenu, BorderLayout.CENTER);
			
		}
}

