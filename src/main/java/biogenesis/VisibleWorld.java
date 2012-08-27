/* Copyright (C) 2006-2010  Joan Queralt Molina
 *
 * This program is free software; you can redistribute it and/or
 * modify it under the terms of the GNU General Public License
 * as published by the Free Software Foundation; either version 2
 * of the License, or (at your option) any later version.
 *
 * This program is distributed in the hope that it will be useful,
 * but WITHOUT ANY WARRANTY; without even the implied warranty of
 * MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the
 * GNU General Public License for more details.
 *
 * You should have received a copy of the GNU General Public License
 * along with this program; if not, write to the Free Software
 * Foundation, Inc., 51 Franklin Street, Fifth Floor, Boston, MA  02110-1301, USA.
 *
 */
package biogenesis;

import javax.swing.*;

import java.awt.*;
import java.awt.event.*;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.IOException;

import javax.imageio.*;

import org.xml.sax.SAXException;

import java.awt.image.*;
/**
 * This class is associated with a {@link World} and represents its visible
 * part: it's the place where organisms are drawn and is in charge of
 * the context menus management.
 */
public class VisibleWorld extends JPanel {
	/**
	 * The version of this class
	 */
	private static final long serialVersionUID = Utils.FILE_VERSION;
	/**
	 * A reference to the {@link MainWindow} where the VisibleWorld is.
	 */
	protected MainWindow _mainWindow;
	/**
	 * The context menu showed when right clicking on an alive organism.
	 */
	protected JPopupMenu popupAlive;
	/**
	 * Menu option in {@link popupAlive} used to keep an organism on the center of the view. 
	 */
	protected TrackAction trackAction;
	protected StdAction feedAction;
	protected StdAction weakenAction;
	protected StdAction killAction;
	protected StdAction copyAction;
	protected StdAction saveImageAction;
	protected StdAction reviveAction;
	protected StdAction disperseAction;
	/**
	 * Menu option in {@link popupAlive} used to give extra energy to an organism.
	 */
	/**
	 * Menu option in {@link popupAlive} used to force the reproduction of the organism,
	 * even if it dies.
	 */
	protected StdAction reproduceAction;
	/**
	 * Menu option in {@link popupAlive} used to kill an organism.
	 */
	/**
	 * Menu option in {@link popupAlive} used to set the age counter of the organism back to 0.
	 */
	protected StdAction rejuvenateAction;
	/**
	 * Menu option in {@link popupAlive} used to remove energy from an organism.
	 */
	/**
	 * Menu option in {@link popupAlive} used to copy an organism genetic code
	 * to use it to create new organisms or to edit it in the genetic lab.
	 */
	/**
	 * Menu option in {@link popupAlive} used to save the genetic code of
	 * an organism to a file.
	 */
	protected StdAction exportAction;
	/**
	 * Menu option in {@link popupAlive} used to save an image of an organism
	 * to a jpg file.
	 */
	/**
	 * The context menu showed when right clicking on a dead organism.
	 */
	protected JPopupMenu popupDead;
	/**
	 * Menu option in {@link popupDead} used to give live to a dead organism.
	 */
	/**
	 * Menu option in {@link popupDead} used to return all its carbony back to
	 * the atmosphere, in form of CO2, and make the corpse disappear.
	 */
	/**
	 * The context menu showed when right clicking on a void plave in the world.
	 */
	protected JPopupMenu popupVoid;
	/**
	 * Menu option in {@link popupVoid} used to create a new organism with the
	 * genetic code previously copies with the Copy option.
	 */
	protected StdAction pasteAction;
	/**
	 * Menu option in {@link popupVoid} used to create a new organism randomly.
	 */
	protected StdAction randomCreateAction;
	/**
	 * Menu option in {@link popupVoid} used to create a new organism with a 
	 * genetic code obtained from a file.
	 */
	protected StdAction importAction;
	/**
	 * This is the selected organism. It is drawn with an orange bounding rectangle
	 * and, if there is an {@link InfoWindow}, it shows information about this organism. 
	 */
	protected Organism _selectedOrganism = null;
	/**
	 * This is the last genetic code obtained using a Copy option. It is used when
	 * pasting new organisms or in the genetic lab. 
	 */
	protected GeneticCode clippedGeneticCode = null;
	/**
	 * X coordinate of the mouse pointer when the user clicks or right clicks on the
	 * visible world.
	 */
	protected int mouseX;
	/**
	 * Y coordinate of the mouse pointer when the user clicks or right clicks on the
	 * visible world.
	 */
	protected int mouseY;
	/**
	 * A reference to the {@link InfoWindow}, that is created from this class.
	 */
	//transient protected InfoWindow _infoWindow = null;
	
	class TrackAction extends StdAction {
		private static final long serialVersionUID = 1L;
		protected String name_key2;
		protected String desc_key2;
		
		public TrackAction(String text_key, String text_key2, String icon_path, String desc, String desc2) {
			super(text_key, icon_path, desc);
			name_key2 = text_key2;
			desc_key2 = desc2;
		}
		
		public void actionPerformed(ActionEvent e) {
			Organism b = getSelectedOrganism();
			if (b != null && b.isAlive()) {
				if (_mainWindow._trackedOrganism == b)
					_mainWindow.setTrackedOrganism(null);
				else
					_mainWindow.setTrackedOrganism(b);
			}
		}
		
		public void setTracking(boolean isTracking) {
			if (isTracking) {
				putValue(NAME, Messages.getString(name_key2));
				putValue(SHORT_DESCRIPTION, Messages.getString(desc_key2));
				putValue(MNEMONIC_KEY, Messages.getMnemonic(name_key2));
			} else {
				putValue(NAME, Messages.getString(name_key));
				putValue(SHORT_DESCRIPTION, Messages.getString(desc_key));
				putValue(MNEMONIC_KEY, Messages.getMnemonic(name_key));
			}
		}
	}
	
	class FeedAction extends StdAction {
		private static final long serialVersionUID = 1L;
		public FeedAction(String text_key, String icon_path, String desc) {
			super(text_key, icon_path, desc);
		}
		
		public void actionPerformed(ActionEvent e) {
			Organism b = getSelectedOrganism();
			if (b != null && b.isAlive()) {
				double q = Math.min(10, _mainWindow.getWorld().getCO2());
				_mainWindow.getWorld().addCO2(q);
				b._energy += q;
				_mainWindow.getWorld().addO2(q);
			}
		}
	}
	
	class WeakenAction extends StdAction {
		private static final long serialVersionUID = 1L;
		public WeakenAction(String text_key, String icon_path, String desc) {
			super(text_key, icon_path, desc);
		}
		
		public void actionPerformed(ActionEvent e) {
			Organism b = getSelectedOrganism();
			if (b != null && b.isAlive()) {
				b.useEnergy(b.getEnergy()/2);
			}
		}
	}
	
	class KillAction extends StdAction {
		private static final long serialVersionUID = 1L;
		public KillAction(String text_key, String icon_path, String desc) {
			super(text_key, icon_path, desc);
		}
		
		public void actionPerformed(ActionEvent e) {
			Organism b = getSelectedOrganism();
			if (b != null && b.isAlive()) {
				b.die(null);
			}
		}
	}
	
	class CopyAction extends StdAction {
		private static final long serialVersionUID = 1L;
		public CopyAction(String text_key, String icon_path, String desc) {
			super(text_key, icon_path, desc);
		}
		
		public void actionPerformed(ActionEvent e) {
			Organism b = getSelectedOrganism();
			if (b != null && b.isAlive()) {
				clippedGeneticCode = b.getGeneticCode();
			}
		}
	}
	
	class SaveImageAction extends StdAction {
		private static final long serialVersionUID = 1L;
		public SaveImageAction(String text_key, String icon_path, String desc) {
			super(text_key, icon_path, desc);
		}
		
		public void actionPerformed(ActionEvent e) {
			Organism b = getSelectedOrganism();
			if (b != null && b.isAlive()) {
				boolean processState = _mainWindow._isProcessActive;
				// Stop time while asking for a file name
				_mainWindow._isProcessActive = false;
				// Get the image to save
				BufferedImage image = b.getImage();
				try {
					// Ask for file name
					JFileChooser chooser = new JFileChooser();
					chooser.setFileFilter(new BioFileFilter("png")); //$NON-NLS-1$
					int returnVal = chooser.showSaveDialog(null);
					if(returnVal == JFileChooser.APPROVE_OPTION) {
						int canWrite = JOptionPane.YES_OPTION;
						File f = chooser.getSelectedFile();
						// Check if file already exists and ask for confirmation
						if (f.exists()) {
							canWrite = JOptionPane.showConfirmDialog(null,Messages.getString("T_CONFIRM_FILE_OVERRIDE"), //$NON-NLS-1$
									Messages.getString("T_FILE_EXISTS"),JOptionPane.YES_NO_OPTION,JOptionPane.WARNING_MESSAGE); //$NON-NLS-1$
						}
						if (canWrite == JOptionPane.YES_OPTION) {
							// Write image to file
							try {
								ImageIO.write(image,"PNG",f); //$NON-NLS-1$
							} catch (FileNotFoundException ex) {
								System.err.println(ex.getMessage());
							} catch (IOException ex) {
								System.err.println(ex.getMessage());
							}
						}
					}
				} catch (SecurityException ex) {
					System.err.println(ex.getMessage());
					JOptionPane.showMessageDialog(null,Messages.getString("T_PERMISSION_DENIED"),Messages.getString("T_PERMISSION_DENIED"),JOptionPane.ERROR_MESSAGE); //$NON-NLS-1$ //$NON-NLS-2$
				}
				_mainWindow._isProcessActive = processState;
			}
		}
	}
	
	class ReviveAction extends StdAction {
		private static final long serialVersionUID = 1L;
		public ReviveAction(String text_key, String icon_path, String desc) {
			super(text_key, icon_path, desc);
		}
		
		public void actionPerformed(ActionEvent e) {
			Organism b = getSelectedOrganism();
			if (b != null && !b.isAlive()) {
				for (int i = 0; i < b._segments; i++)
					b._segColor[i] = b.getGeneticCode().getGene(i%b.getGeneticCode().getNGenes()).getColor();
				b.alive = true;
				b.hasMoved = true;
				b._age = 0;
				_mainWindow.getWorld().increasePopulation();
				showAliveToolbar();
			}
		}
	}
	
	class DisperseAction extends StdAction {
		private static final long serialVersionUID = 1L;
		public DisperseAction(String text_key, String icon_path, String desc) {
			super(text_key, icon_path, desc);
		}
		
		public void actionPerformed(ActionEvent e) {
			Organism b = getSelectedOrganism();
			if (b != null && !b.isAlive()) {
				b.useEnergy(b.getEnergy());
			}
		}
	}
	
	class ReproduceAction extends StdAction {
		private static final long serialVersionUID = 1L;
		public ReproduceAction(String text_key, String icon_path, String desc) {
			super(text_key, icon_path, desc);
		}
		
		public void actionPerformed(ActionEvent e) {
			Organism b = getSelectedOrganism();
			if (b != null && b.isAlive()) {
				b.reproduce();
			}
		}
	}
	
	class RejuvenateAction extends StdAction {
		private static final long serialVersionUID = 1L;
		public RejuvenateAction(String text_key, String icon_path, String desc) {
			super(text_key, icon_path, desc);
		}
		
		public void actionPerformed(ActionEvent e) {
			Organism b = getSelectedOrganism();
			if (b != null && b.isAlive()) {
				b._age = 0;
			}
		}
	}
	
	class ExportAction extends StdAction {
		private static final long serialVersionUID = 1L;
		public ExportAction(String text_key, String icon_path, String desc) {
			super(text_key, icon_path, desc);
		}
		
		public void actionPerformed(ActionEvent e) {
			Organism b = getSelectedOrganism();
			if (b != null && b.isAlive()) {
				_mainWindow.saveObjectAs(b.getGeneticCode());
			}
		}
	}
	
	class PasteAction extends StdAction {
		private static final long serialVersionUID = 1L;
		public PasteAction(String text_key, String icon_path, String desc) {
			super(text_key, icon_path, desc);
		}
		
		public void actionPerformed(ActionEvent e) {
			if (clippedGeneticCode != null) {
				pasteGeneticCode(clippedGeneticCode, mouseX, mouseY);
			}
		}
	}
	
	class RandomCreateAction extends StdAction {
		private static final long serialVersionUID = 1L;
		public RandomCreateAction(String text_key, String icon_path, String desc) {
			super(text_key, icon_path, desc);
		}
		
		public void actionPerformed(ActionEvent e) {
			GeneticCode g = new GeneticCode();
			Organism newBiot = new Organism(_mainWindow.getWorld(), g);
			if (newBiot.pasteOrganism(mouseX, mouseY))
				_mainWindow.getWorld().addOrganism(newBiot, null);
		}
	}
	
	class ImportAction extends StdAction {
		private static final long serialVersionUID = 1L;
		public ImportAction(String text_key, String icon_path, String desc) {
			super(text_key, icon_path, desc);
		}
		
		public void actionPerformed(ActionEvent e) {
			GeneticCode g;
			Organism newBiot;
			boolean processState = _mainWindow._isProcessActive;
			// Stop time
    		_mainWindow._isProcessActive = false;
    		try {
    			JFileChooser chooser = _mainWindow.getGeneticCodeChooser();
    			int returnVal = chooser.showOpenDialog(null);
    			if (returnVal == JFileChooser.APPROVE_OPTION) {
    				try {
    					// Read XML code from file
    					BioXMLParser parser = new BioXMLParser();
						g = parser.parseGeneticCode(chooser.getSelectedFile());
						// Create organism
    					newBiot = new Organism(_mainWindow.getWorld(), g);
    					if (newBiot.pasteOrganism(mouseX, mouseY))
    						_mainWindow.getWorld().addOrganism(newBiot, null);
    				} catch (SAXException ex) {
    					System.err.println(ex.getMessage());
    					JOptionPane.showMessageDialog(null,Messages.getString("T_WRONG_FILE_VERSION"),Messages.getString("T_READ_ERROR"),JOptionPane.ERROR_MESSAGE); //$NON-NLS-1$ //$NON-NLS-2$
    				} catch (IOException ex) {
    					System.err.println(ex.getMessage());
    					JOptionPane.showMessageDialog(null,Messages.getString("T_CANT_READ_FILE"),Messages.getString("T_READ_ERROR"),JOptionPane.ERROR_MESSAGE); //$NON-NLS-1$ //$NON-NLS-2$
					}
    			}
    		} catch (SecurityException ex) {
    			System.err.println(ex.getMessage());
    			JOptionPane.showMessageDialog(null,Messages.getString("T_PERMISSION_DENIED"),Messages.getString("T_PERMISSION_DENIED"),JOptionPane.ERROR_MESSAGE); //$NON-NLS-1$ //$NON-NLS-2$
    		}
    		_mainWindow._isProcessActive = processState;
		}
	}
	
	/**
	 * Sets an organism as the selected organism. If required, it creates an
	 * {@link InfoWindow} with information of this organism.
	 * 
	 * @param b  The new selected organism
	 * @param showInfo  true if an InfoWindow should be created
	 */
	public void setSelectedOrganism(Organism b) {
		Organism lastSelectedOrganism = _selectedOrganism;
		_selectedOrganism = b;
		if (lastSelectedOrganism != null)
			repaint(lastSelectedOrganism);
		_mainWindow.getInfoPanel().setSelectedOrganism(b);
		if (_selectedOrganism != null)
			repaint(_selectedOrganism);
		// Make sure to don't create the tool bar twice when starting the program
		// because this causes spurious exceptions.
		if (_selectedOrganism != lastSelectedOrganism) {
			if (_selectedOrganism != null) {
				if (_selectedOrganism.isAlive())
					showAliveToolbar();
				else
					showDeadToolbar();
			}
			else
				_mainWindow.createToolBar();
		}
	}
	/**
	 * Return the selected organism. 
	 * 
	 * @return  The selected organism, if any.
	 */
	public Organism getSelectedOrganism() {
		return _selectedOrganism;
	}
	/**
	 * Set a genetic code as the clipped genetic code, that will be used when
	 * pasting a new organism or in the genetic lab as the staring genetic code.
	 * 
	 * @param gc  The clipped genetic code
	 */
	public void setClippedGeneticCode(GeneticCode gc) {
		if (gc != null)
			clippedGeneticCode = gc;
	}
	/**
	 * Creates a new VisibleWorld associated with a {@link MainWindow}.
	 * Creates the menus and the MouseAdapter.
	 * 
	 * @param mainWindow  The MainWindow associated with this VisibleWorld.
	 */
	public VisibleWorld(MainWindow mainWindow) {
		_mainWindow = mainWindow;
		setPreferredSize(new Dimension(Utils.WORLD_WIDTH,Utils.WORLD_HEIGHT));
		setBackground(Color.BLACK);
		createActions();
		createPopupMenu();
		addMouseListener(new MouseAdapter() {
			@Override
			public void mouseClicked(MouseEvent e) {
				if (e.getButton() == MouseEvent.BUTTON1) {
					setSelectedOrganism(findOrganismFromPosition(e.getX(),e.getY()));
				}
			}
			@Override
			public void mousePressed(MouseEvent e) {
				maybeShowPopupMenu(e);
			}
			@Override
			public void mouseReleased(MouseEvent e) {
				maybeShowPopupMenu(e);
			}
		});
	}
	/**
	 * Finds an organism that has the given coordinates inside its bounding box and
	 * returns a reference to it. If more than on organism satisfies this condition,
	 * if possible, an alive organism is returned. If non organism satisfies this
	 * condition, this method returns null.
	 * 
	 * @param x  X coordinate
	 * @param y  Y coordinate
	 * @return  An organism with the point (x,y) inside its bounding box, or null
	 * if such organism doesn't exist.
	 */
	Organism findOrganismFromPosition(int x, int y) {
		return _mainWindow.getWorld().findOrganismFromPosition(x, y);
	}
	/**
	 * Calls World.draw to draw all world elements and paints the bounding rectangle
	 * of the selected organism.
	 *
	 * @see javax.swing.JComponent#paintComponent(java.awt.Graphics)
	 */
	@Override
	public void paintComponent (Graphics g) {
		super.paintComponent(g);
		_mainWindow.getWorld().draw(g);
		if (getSelectedOrganism() != null) {
			g.setColor(Color.ORANGE);
			g.drawRect(_selectedOrganism.x, _selectedOrganism.y,
					_selectedOrganism.width-1, _selectedOrganism.height-1);
		}
    }
	
	private void createActions() {
		trackAction = new TrackAction("T_TRACK", "T_ABORT_TRACKING", "images/menu_track.png", //$NON-NLS-1$ //$NON-NLS-2$ //$NON-NLS-3$
				"T_TRACK_ORGANISM", "T_ABORT_TRACKING_ORGANISM"); //$NON-NLS-1$ //$NON-NLS-2$
		feedAction = new FeedAction("T_FEED", "images/menu_feed.png",  //$NON-NLS-1$//$NON-NLS-2$
				"T_FEED_ORGANISM"); //$NON-NLS-1$
		weakenAction = new WeakenAction("T_WEAKEN", "images/menu_weaken.png",  //$NON-NLS-1$//$NON-NLS-2$
				"T_WEAKEN_ORGANISM"); //$NON-NLS-1$
		killAction = new KillAction("T_KILL", "images/menu_kill.png",  //$NON-NLS-1$//$NON-NLS-2$
				"T_KILL_ORGANISM"); //$NON-NLS-1$
		copyAction = new CopyAction("T_COPY", "images/menu_copy.png",  //$NON-NLS-1$//$NON-NLS-2$
				"T_COPY_GENETIC_CODE"); //$NON-NLS-1$
		saveImageAction = new SaveImageAction("T_SAVE_IMAGE", "images/menu_save_image.png",  //$NON-NLS-1$//$NON-NLS-2$
				"T_SAVE_IMAGE"); //$NON-NLS-1$
		reviveAction = new ReviveAction("T_REVIVE", "images/menu_revive.png", //$NON-NLS-1$ //$NON-NLS-2$
				"T_REVIVE_ORGANISM"); //$NON-NLS-1$
		disperseAction = new DisperseAction("T_DISPERSE", "images/menu_disperse.png", //$NON-NLS-1$ //$NON-NLS-2$
				"T_DISPERSE_ORGANISM"); //$NON-NLS-1$
		reproduceAction = new ReproduceAction("T_FORCE_REPRODUCTION", null, "T_FORCE_REPRODUCTION"); //$NON-NLS-1$ //$NON-NLS-2$
		rejuvenateAction = new RejuvenateAction("T_REJUVENATE", null, "T_REJUVENATE"); //$NON-NLS-1$ //$NON-NLS-2$
		exportAction = new ExportAction("T_EXPORT", null, "T_EXPORT_GENETIC_CODE"); //$NON-NLS-1$ //$NON-NLS-2$
		pasteAction = new PasteAction("T_PASTE", null, "T_PASTE_GENETIC_CODE"); //$NON-NLS-1$ //$NON-NLS-2$
		randomCreateAction = new RandomCreateAction("T_CREATE_RANDOMLY", null, "T_CREATE_RANDOMLY"); //$NON-NLS-1$ //$NON-NLS-2$
		importAction = new ImportAction("T_IMPORT", null, "T_IMPORT_GENETIC_CODE"); //$NON-NLS-1$ //$NON-NLS-2$
	}

	public void showAliveToolbar() {
		JToolBar toolBar = _mainWindow.toolBar;
		toolBar.removeAll();
		toolBar.add(feedAction);
		toolBar.add(weakenAction);
		toolBar.add(killAction);
		toolBar.add(copyAction);
		toolBar.add(saveImageAction);
		toolBar.add(trackAction);
		toolBar.add(_mainWindow.abortTrackingAction);
		toolBar.invalidate();
		toolBar.repaint();
	}
	
	public void showDeadToolbar() {
		JToolBar toolBar = _mainWindow.toolBar;
		toolBar.removeAll();
		toolBar.add(reviveAction);
		toolBar.add(disperseAction);
		toolBar.invalidate();
		toolBar.repaint();
	}
	
	/**
	 * Creates all popup menus.
	 */
	private void createPopupMenu() {
		JMenuItem menuItem;
	    popupAlive = new JPopupMenu();
	    menuItem = new JMenuItem(trackAction);
	    menuItem.setIcon(null);
	    popupAlive.add(menuItem);
	    menuItem = new JMenuItem(feedAction);
	    menuItem.setIcon(null);
	    popupAlive.add(menuItem);
	    menuItem = new JMenuItem(weakenAction);
	    menuItem.setIcon(null);
	    popupAlive.add(menuItem);
	    popupAlive.add(new JMenuItem(reproduceAction));
	    popupAlive.add(new JMenuItem(rejuvenateAction));
	    menuItem = new JMenuItem(killAction);
	    menuItem.setIcon(null);
	    popupAlive.add(menuItem);
	    menuItem = new JMenuItem(copyAction);
	    menuItem.setIcon(null);
	    popupAlive.add(menuItem);
	    popupAlive.add(new JMenuItem(exportAction));
	    menuItem = new JMenuItem(saveImageAction);
	    menuItem.setIcon(null);
	    popupAlive.add(menuItem);
	    popupDead = new JPopupMenu();
	    menuItem = new JMenuItem(reviveAction);
	    menuItem.setIcon(null);
	    popupDead.add(menuItem);
	    menuItem = new JMenuItem(disperseAction);
	    menuItem.setIcon(null);
	    popupDead.add(menuItem);
	    popupVoid = new JPopupMenu();
	    popupVoid.add(new JMenuItem(pasteAction));
	    popupVoid.add(new JMenuItem(randomCreateAction));
	    popupVoid.add(new JMenuItem(importAction));
	    // Only enable file management menu options if at least there is 
		//permission to read user's home directory
		SecurityManager sec = System.getSecurityManager();
		try {
			if (sec != null)
				sec.checkPropertyAccess("user.home"); //$NON-NLS-1$
		} catch (SecurityException ex) {
			exportAction.setEnabled(false);
			importAction.setEnabled(false);
			saveImageAction.setEnabled(false);
		}
	}
	/**
	 * Called from MainWindow when the locale is changed in order to update the menu
	 * entries to the new language.
	 */
	public void changeLocale() {
		trackAction.changeLocale();
	    feedAction.changeLocale();
	    weakenAction.changeLocale();
	    reproduceAction.changeLocale();
	    rejuvenateAction.changeLocale();
	    killAction.changeLocale();
	    copyAction.changeLocale();
	    exportAction.changeLocale();
	    saveImageAction.changeLocale();
	    reviveAction.changeLocale();	    
	    disperseAction.changeLocale();
	    pasteAction.changeLocale();
	    randomCreateAction.changeLocale();
	    importAction.changeLocale();
	    createPopupMenu();
	}
	/**
	 * Creates a new organism with the given genetic code and puts it in the world,
	 * at the specified position.
	 * 
	 * @param gc  The genetic code for the new organism
	 * @param x  X coordinate
	 * @param y  Y coordinate
	 * @return  true if the organism has been created (if there is space for it), false otherwise
	 */	
	public boolean pasteGeneticCode(GeneticCode gc, int x, int y) {
		Organism newOrganism = new Organism(_mainWindow.getWorld(), gc);
		if (newOrganism.pasteOrganism(x, y)) {
			_mainWindow.getWorld().addOrganism(newOrganism, null);
			return true;
		}
		return false;
	}
	/**
	 * This method is called when a mouse event occurs. If the mouse event is
	 * a popup trigger, this method decide which popup menu is shown, based on
	 * the position of the mouse.
	 * 
	 * @param e
	 */
	void maybeShowPopupMenu(MouseEvent e) {
		if (e.isPopupTrigger()) {
			mouseX = e.getX();
			mouseY = e.getY();
			Organism b = findOrganismFromPosition(mouseX,mouseY);
			if (b != null) {
				setSelectedOrganism(b);
				if (b.isAlive()) {
					trackAction.setTracking(_mainWindow._trackedOrganism == b);
					popupAlive.show(e.getComponent(), mouseX, mouseY);
				}
				else
					popupDead.show(e.getComponent(), mouseX, mouseY);
			} else
				popupVoid.show(e.getComponent(), mouseX, mouseY);
		}
	}
}
