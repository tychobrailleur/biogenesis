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
import javax.swing.border.EtchedBorder;

import java.io.*;
import java.lang.reflect.InvocationTargetException;
import java.awt.*;
import java.awt.event.*;
import java.text.NumberFormat;
import java.util.*;

public class MainWindow extends JFrame {
	private static final long serialVersionUID = Utils.FILE_VERSION;

	protected VisibleWorld _visibleWorld;
	protected World _world;
	protected boolean _isProcessActive=false;
	protected transient java.util.Timer _timer;
	protected transient TimerTask updateTask = null;
	protected JFileChooser worldChooser = new JFileChooser();
	protected JFileChooser geneticCodeChooser = new JFileChooser();
	protected File _gameFile = null;
	
	protected JScrollPane scrollPane;
	protected StdAction newGameAction;
	protected StartStopAction startStopAction;
	protected StdAction saveGameAction;
	protected StdAction increaseCO2Action;
	protected StdAction decreaseCO2Action;
	protected StdAction manageConnectionsAction;
	protected StdAction abortTrackingAction;
	protected StdAction openGameAction;
	protected StdAction saveGameAsAction;
	protected StdAction quitAction;
	protected StdAction statisticsAction;
	protected StdAction labAction;
	protected StdAction killAllAction;
	protected StdAction disperseAllAction;
	protected StdAction parametersAction;
	protected StdAction aboutAction;
	protected StdAction manualAction;
	protected StdAction checkLastVersionAction;
	protected StdAction netConfigAction;
	
	protected JMenuItem _menuStartStopGame;
	protected JMenu _menuGame;
	protected JMenu _menuWorld;
	protected JMenu _menuHelp;
	protected JMenu _menuNet;
	protected NumberFormat _nf;
	protected JLabel _statusLabel;
	protected JToolBar toolBar = new JToolBar(Messages.getString("T_PROGRAM_NAME")); //$NON-NLS-1$
	
	protected InfoToolbar infoToolbar;
	
	private String statusMessage=""; //$NON-NLS-1$
	private StringBuilder statusLabelText = new StringBuilder(100);
	protected Organism _trackedOrganism = null;
	
	protected transient NetServerThread serverThread = null;
	
	
	protected StatisticsWindow _statisticsWindow = null;
//	 Comptador de frames, per saber quan actualitzar la finestra d'informaci�
	protected long nFrames=0;
	private ImageIcon imageIcon = new ImageIcon(getClass().getResource("images/bullet.jpg")); //$NON-NLS-1$

	public JFileChooser getWorldChooser() {
		return worldChooser;
	}
	public JFileChooser getGeneticCodeChooser() {
		return geneticCodeChooser;
	}
	
	public void setStatusMessage(String str) {
		statusMessage = str;
		updateStatusLabel();
	}
	public String getStatusMessage() {
		return statusMessage;
	}
	
	public World getWorld() {
		return _world;
	}
	
	public VisibleWorld getVisibleWorld() {
        return _visibleWorld;
    }
	
	public boolean isProcessActive() {
		return _isProcessActive;
	}
	
	public InfoToolbar getInfoPanel() {
		return infoToolbar;
	}
	
	public MainWindow() {
		createActions();
		createMenu();
		createToolBar();
		setControls();
		configureApp();
		_world = new World(_visibleWorld);
		startApp();
		_world.genesis();
		scrollPane.setViewportView(_visibleWorld);
	}
	
	/**
	 * @param args
	 */
	public static void main(String[] args) {
		if (args.length > 1) {
			System.err.println("java -jar biogenesis.jar [random seed]");
		} else if (args.length == 1) {
			try {
				long seed = Long.parseLong(args[0]);
				Utils.random.setSeed(seed);
			} catch (NumberFormatException e) {
				System.err.println("java -jar biogenesis.jar [random seed]");
			}
		}
		Utils.readPreferences();
		new MainWindow();
	}
	
	private void createActions() {
		newGameAction = new NewGameAction("T_NEW", "images/menu_new.png", //$NON-NLS-1$ //$NON-NLS-2$
				"T_NEW_WORLD"); //$NON-NLS-1$
		startStopAction = new StartStopAction("T_RESUME","T_PAUSE","images/menu_start.png",  //$NON-NLS-1$ //$NON-NLS-2$//$NON-NLS-3$
				"images/menu_stop.png","T_RESUME_PROCESS","T_PAUSE_PROCESS"); //$NON-NLS-1$ //$NON-NLS-2$ //$NON-NLS-3$
		saveGameAction = new SaveGameAction("T_SAVE", "images/menu_save.png",  //$NON-NLS-1$//$NON-NLS-2$
				"T_SAVE_WORLD"); //$NON-NLS-1$
		increaseCO2Action = new IncreaseCO2Action("T_INCREASE_CO2", "images/menu_increase_co2.png", //$NON-NLS-1$ //$NON-NLS-2$
				"T_INCREASE_CO2"); //$NON-NLS-1$
		decreaseCO2Action = new DecreaseCO2Action("T_DECREASE_CO2", "images/menu_decrease_co2.png", //$NON-NLS-1$ //$NON-NLS-2$
				"T_DECREASE_CO2"); //$NON-NLS-1$
		manageConnectionsAction = new ManageConnectionsAction("T_MANAGE_CONNECTIONS", "images/menu_manage_connections.png",  //$NON-NLS-1$//$NON-NLS-2$
				"T_MANAGE_CONNECTIONS"); //$NON-NLS-1$
		abortTrackingAction = new AbortTrackingAction("T_ABORT_TRACKING", "images/menu_stop_tracking.png",  //$NON-NLS-1$//$NON-NLS-2$
				"T_ABORT_TRACKING"); //$NON-NLS-1$
		openGameAction = new OpenGameAction("T_OPEN", null, "T_OPEN_WORLD");  //$NON-NLS-1$//$NON-NLS-2$
		saveGameAsAction = new SaveGameAsAction("T_SAVE_AS", null, "T_SAVE_WORLD_AS");  //$NON-NLS-1$//$NON-NLS-2$
		quitAction = new QuitAction("T_QUIT", null, "T_QUIT_PROGRAM"); //$NON-NLS-1$ //$NON-NLS-2$
		statisticsAction = new StatisticsAction("T_STATISTICS", null, "T_WORLD_STATISTICS"); //$NON-NLS-1$ //$NON-NLS-2$
		labAction = new LabAction("T_GENETIC_LABORATORY", null, "T_GENETIC_LABORATORY"); //$NON-NLS-1$ //$NON-NLS-2$
		killAllAction = new KillAllAction("T_KILL_ALL", null, "T_KILL_ALL_ORGANISMS"); //$NON-NLS-1$ //$NON-NLS-2$
		disperseAllAction = new DisperseAllAction("T_DISPERSE_ALL", null, "T_DISPERSE_ALL_DEAD_ORGANISMS"); //$NON-NLS-1$ //$NON-NLS-2$
		parametersAction = new ParametersAction("T_PARAMETERS", null, "T_EDIT_PARAMETERS"); //$NON-NLS-1$ //$NON-NLS-2$
		aboutAction = new AboutAction("T_ABOUT", null, "T_ABOUT");  //$NON-NLS-1$//$NON-NLS-2$
		manualAction = new ManualAction("T_USER_MANUAL", null, "T_USER_MANUAL");  //$NON-NLS-1$//$NON-NLS-2$
		checkLastVersionAction = new CheckLastVersionAction("T_CHECK_LAST_VERSION", null, "T_CHECK_LAST_VERSION"); //$NON-NLS-1$ //$NON-NLS-2$
		netConfigAction = new NetConfigAction("T_CONFIGURE_NETWORK", null, "T_CONFIGURE_NETWORK"); //$NON-NLS-1$ //$NON-NLS-2$
	}

	public void createToolBar() {
		toolBar.removeAll();
		toolBar.add(newGameAction);
		toolBar.add(startStopAction);
		toolBar.add(saveGameAction);
		toolBar.add(increaseCO2Action);
		toolBar.add(decreaseCO2Action);
		toolBar.add(manageConnectionsAction);
		toolBar.add(abortTrackingAction);
		abortTrackingAction.setEnabled(_trackedOrganism != null);
		toolBar.invalidate();
		toolBar.repaint();
	}
	
	private void createMenu() {
		JMenuItem menuItem;
		JMenuBar menuBar = new JMenuBar();
		_menuGame = new JMenu(Messages.getString("T_GAME")); //$NON-NLS-1$
		_menuGame.setMnemonic(Messages.getMnemonic("T_GAME").intValue()); //$NON-NLS-1$
		menuBar.add(_menuGame);
		menuItem = new JMenuItem(newGameAction);
		menuItem.setIcon(null);
		_menuGame.add(menuItem);
		_menuStartStopGame = new JMenuItem(startStopAction);
		_menuStartStopGame.setIcon(null);
		_menuGame.add(_menuStartStopGame);
		_menuGame.add(new JMenuItem(openGameAction));
		menuItem = new JMenuItem(saveGameAction);
		menuItem.setIcon(null);
		_menuGame.add(menuItem);
		_menuGame.add(new JMenuItem(saveGameAsAction));
		_menuGame.add(new JMenuItem(quitAction));
		_menuWorld = new JMenu(Messages.getString("T_WORLD")); //$NON-NLS-1$
		_menuWorld.setMnemonic(Messages.getMnemonic("T_WORLD").intValue()); //$NON-NLS-1$
		menuBar.add(_menuWorld);
		_menuWorld.add(new JMenuItem(statisticsAction));
		_menuWorld.add(new JMenuItem(labAction));
		menuItem = new JMenuItem(increaseCO2Action);
		menuItem.setIcon(null);
		_menuWorld.add(menuItem);
		menuItem = new JMenuItem(decreaseCO2Action);
		menuItem.setIcon(null);
		_menuWorld.add(menuItem);
		_menuWorld.add(new JMenuItem(killAllAction));
		_menuWorld.add(new JMenuItem(disperseAllAction));
		_menuWorld.add(new JMenuItem(parametersAction));
		_menuNet = new JMenu(Messages.getString("T_NETWORK")); //$NON-NLS-1$
		_menuNet.setMnemonic(Messages.getMnemonic("T_NETWORK").intValue()); //$NON-NLS-1$
		menuBar.add(_menuNet);
		_menuNet.add(new JMenuItem(netConfigAction));
		menuItem = new JMenuItem(manageConnectionsAction);
		menuItem.setIcon(null);
		_menuNet.add(menuItem);
		_menuHelp = new JMenu(Messages.getString("T_HELP")); //$NON-NLS-1$
		_menuHelp.setMnemonic(Messages.getMnemonic("T_HELP").intValue()); //$NON-NLS-1$
		menuBar.add(_menuHelp);
		_menuHelp.add(new JMenuItem(manualAction));
		_menuHelp.add(new JMenuItem(checkLastVersionAction));
		_menuHelp.add(new JMenuItem(aboutAction));
		// Only enable file management menu options if at least there is 
		//permission to read user's home directory
		SecurityManager sec = System.getSecurityManager();
		try {
			if (sec != null)
				sec.checkPropertyAccess("user.home"); //$NON-NLS-1$
		} catch (SecurityException ex) {
			openGameAction.setEnabled(false);
			saveGameAsAction.setEnabled(false);
			saveGameAction.setEnabled(false);
			manualAction.setEnabled(false);
			netConfigAction.setEnabled(false);
			manageConnectionsAction.setEnabled(false);
		}
		setJMenuBar(menuBar);
	}
	
	protected void checkLastVersion() {
		CheckVersionThread thread = new CheckVersionThread(this);
		thread.start();
	}

	protected void netConfig() {
		new NetConfigWindow(this);
	}
	
	public void setAcceptConnections(boolean newAcceptConnections) {
		if (newAcceptConnections != Utils.ACCEPT_CONNECTIONS) {
			Utils.ACCEPT_CONNECTIONS = newAcceptConnections;
			if (newAcceptConnections)
				startServer();
			else
				if (serverThread.getConnections().isEmpty())
					stopServer();
		}
	}
	public boolean isAcceptingConnections() {
		return Utils.ACCEPT_CONNECTIONS;
	}
	
	public NetServerThread getNetServer() {
		return serverThread;
	}
	
	public NetServerThread startServer() {
		if (serverThread == null || !serverThread.isActive()) {
			serverThread = new NetServerThread(this);
			serverThread.start();
		}
		return serverThread;
	}
	
	public void stopServer() {
		if (serverThread != null) {
			serverThread.closeServer();
		}
	}
	
	class NewGameAction extends StdAction {
		private static final long serialVersionUID = 1L;
		public NewGameAction(String text_key, String icon_path, String desc) {
			super(text_key, icon_path, desc);
		}
		
		public void actionPerformed(ActionEvent e) {
			_trackedOrganism = null;
			_world.genesis();
			scrollPane.setViewportView(_visibleWorld);
			_isProcessActive = true;
			startStopAction.setActive(true);
			_menuStartStopGame.setIcon(null);
			setStatusMessage(Messages.getString("T_NEW_WORLD_CREATED")); //$NON-NLS-1$
		}
	}
	
	class StartStopAction extends StdAction {
		private static final long serialVersionUID = 1L;
		protected String name2;
		protected String description2;
		protected Integer mnemonic2;
		protected ImageIcon icon2;
		
		protected String name2_key;
		protected String desc2_key;
		
		protected boolean active;
		
		public StartStopAction(String text1, String text2, String icon_path1,
				String icon_path2, String desc1, String desc2) {
			super(text1, icon_path1, desc1); 
			name2 = Messages.getString(text2);
			description2 = Messages.getString(desc2);
			icon2 = createIcon(icon_path2);
			mnemonic2 = Messages.getMnemonic(text2);
			name2_key = text2;
			desc2_key = desc2;
			active = false;
			
			putValue(ACCELERATOR_KEY, KeyStroke.getKeyStroke(Messages.getString("T_PAUSE_ACCELERATOR")));
		}
		
		public void actionPerformed(ActionEvent e) {
			if (_isProcessActive)
				pauseGame();
			else
				playGame();
		}
		
		public void toogle() {
			String aux;
			ImageIcon auxicon;
			Integer auxmnemonic;
			aux = (String) getValue(NAME);
			putValue(NAME, name2);
			name2 = aux;
			aux = (String) getValue(SHORT_DESCRIPTION);
			putValue(SHORT_DESCRIPTION, description2);
			description2 = aux;
			auxicon = (ImageIcon) getValue(SMALL_ICON);
			putValue(SMALL_ICON, icon2);
			icon2 = auxicon;
			auxmnemonic = (Integer) getValue(MNEMONIC_KEY);
			putValue(MNEMONIC_KEY, mnemonic2);
			mnemonic2 = auxmnemonic;
			active = !active;
		}
		
		@Override
		public void changeLocale() {
			super.changeLocale();
			name2 = Messages.getString(name2_key);
			description2 = Messages.getString(desc2_key);
			putValue(ACCELERATOR_KEY, KeyStroke.getKeyStroke(Messages.getString("T_PAUSE_ACCELERATOR")));
		}
		
		public void setActive(boolean newState) {
			if (newState != active) {
				toogle();
				active = newState;
			}
		}
		
		public boolean isActive() {
			return active;
		}
	}
	
	class SaveGameAction extends StdAction {
		private static final long serialVersionUID = 1L;
		public SaveGameAction(String text, String icon_path, String desc) {
			super(text, icon_path, desc);
			putValue(ACCELERATOR_KEY, KeyStroke.getKeyStroke(Messages.getString("T_SAVE_ACCELERATOR")));
		}
		
		public void actionPerformed(ActionEvent e) {
			if (_gameFile != null)
				saveObject(_world, _gameFile);
			else
				saveGameAs();
		}
		
		@Override
		public void changeLocale() {
			super.changeLocale();
			putValue(ACCELERATOR_KEY, KeyStroke.getKeyStroke(Messages.getString("T_SAVE_ACCELERATOR")));
		}
	}
	
	class IncreaseCO2Action extends StdAction {
		private static final long serialVersionUID = 1L;
		public IncreaseCO2Action(String text, String icon_path, String desc) {
			super(text, icon_path, desc);
		}
		
		public void actionPerformed(ActionEvent e) {
			_world.addCO2(500);
		}
	}
	
	class DecreaseCO2Action extends StdAction {
		private static final long serialVersionUID = 1L;
		public DecreaseCO2Action(String text, String icon_path, String desc) {
			super(text, icon_path, desc);
		}
		
		public void actionPerformed(ActionEvent e) {
			_world.decreaseCO2(500);
		}
	}
	
	class ManageConnectionsAction extends StdAction {
		private static final long serialVersionUID = 1L;
		public ManageConnectionsAction(String text, String icon_path, String desc) {
			super(text, icon_path, desc);
		}
		
		public void actionPerformed(ActionEvent e) {
			netConnectionsWindow();
		}
	}
	
	class AbortTrackingAction extends StdAction {
		private static final long serialVersionUID = 1L;
		public AbortTrackingAction(String text, String icon_path, String desc) {
			super(text, icon_path, desc);
		}
		
		public void actionPerformed(ActionEvent e) {
			setTrackedOrganism(null);
		}
	}
	
	class OpenGameAction extends StdAction {
		private static final long serialVersionUID = 1L;
		public OpenGameAction(String text, String icon_path, String desc) {
			super(text, icon_path, desc);
		}
		
		public void actionPerformed(ActionEvent e) {
			boolean processState = _isProcessActive;
			_isProcessActive = false;
			try {
				int returnVal = getWorldChooser().showOpenDialog(null);
				if (returnVal == JFileChooser.APPROVE_OPTION) {
					// Elimina les finestres antigues
					if (_statisticsWindow != null) {
						_statisticsWindow.dispose();
						_statisticsWindow = null;
					}
					//_world.clean();
					ObjectInputStream inputStream;
					try {
						File f = getWorldChooser().getSelectedFile();
						FileInputStream fileStream = new FileInputStream(f);
						inputStream = new ObjectInputStream(fileStream);
						_world = (World) inputStream.readObject();
						inputStream.close();
						_gameFile = f;
						_trackedOrganism = null;
						processState = true;
						setStatusMessage(Messages.getString("T_WORLD_LOADED_SUCCESSFULLY")); //$NON-NLS-1$
					} catch (IOException ex) {
						System.err.println(ex.getMessage());
						JOptionPane.showMessageDialog(null,Messages.getString("T_CANT_READ_FILE"),Messages.getString("T_READ_ERROR"),JOptionPane.ERROR_MESSAGE); //$NON-NLS-1$ //$NON-NLS-2$
					} catch (ClassNotFoundException ex) {
						System.err.println(ex.getMessage());
						JOptionPane.showMessageDialog(null,Messages.getString("T_WRONG_FILE_TYPE"),Messages.getString("T_READ_ERROR"),JOptionPane.ERROR_MESSAGE); //$NON-NLS-1$ //$NON-NLS-2$
					} catch (ClassCastException ex) {
						System.err.println(ex.getMessage());
						JOptionPane.showMessageDialog(null,Messages.getString("T_WRONG_FILE_VERSION"),Messages.getString("T_READ_ERROR"),JOptionPane.ERROR_MESSAGE); //$NON-NLS-1$ //$NON-NLS-2$
					}
					// Torna a assignar els valors dels camps no guardats a l'objecte world
					_world.init(_visibleWorld);
					scrollPane.setViewportView(_visibleWorld);
					// Assegurem que s'ha dibuixat el m�n
					_visibleWorld.repaint();
				}
			} catch (SecurityException ex) {
				System.err.println(ex.getMessage());
				JOptionPane.showMessageDialog(null,Messages.getString("T_PERMISSION_DENIED"),Messages.getString("T_PERMISSION_DENIED"),JOptionPane.ERROR_MESSAGE); //$NON-NLS-1$ //$NON-NLS-2$
			}
			_isProcessActive = processState;
		}
	}
	
	class SaveGameAsAction extends StdAction {
		private static final long serialVersionUID = 1L;
		public SaveGameAsAction(String text, String icon_path, String desc) {
			super(text, icon_path, desc);
		}
		
		public void actionPerformed(ActionEvent e) {
			saveGameAs();
		}
	}
	
	class QuitAction extends StdAction {
		private static final long serialVersionUID = 1L;
		public QuitAction(String text, String icon_path, String desc) {
			super(text, icon_path, desc);
		}
		
		public void actionPerformed(ActionEvent e) {
			quit();
		}
	}
	
	public void quit() {
		int save = JOptionPane.showConfirmDialog(this,Messages.getString("T_SAVE_BEFORE_QUIT"), //$NON-NLS-1$
				Messages.getString("T_SAVE_WORLD"),JOptionPane.YES_NO_CANCEL_OPTION,JOptionPane.QUESTION_MESSAGE); //$NON-NLS-1$
		
		if (save != JOptionPane.CANCEL_OPTION) {
			if (save == JOptionPane.YES_OPTION) {
				if (_gameFile != null)
					saveObject(_world, _gameFile);
				else
					if (saveGameAs() == null)
						return;
			}
			Utils.quitProgram(this);
			if (serverThread != null)
				serverThread.closeServer();
			try {
				System.exit(0);
			} catch (SecurityException ex) {
				dispose();
			}
		}
	}
	
	class StatisticsAction extends StdAction {
		private static final long serialVersionUID = 1L;
		public StatisticsAction(String text, String icon_path, String desc) {
			super(text, icon_path, desc);
		}
		
		public void actionPerformed(ActionEvent e) {
			if (_statisticsWindow != null)
				_statisticsWindow.dispose();
			_statisticsWindow = _world.createStatisticsWindow();
		}
	}
	
	class LabAction extends StdAction {
		private static final long serialVersionUID = 1L;
		public LabAction(String text, String icon_path, String desc) {
			super(text, icon_path, desc);
		}
		
		public void actionPerformed(ActionEvent e) {
			new LabWindow(MainWindow.this);
		}
	}
	
	class KillAllAction extends StdAction {
		private static final long serialVersionUID = 1L;
		public KillAllAction(String text, String icon_path, String desc) {
			super(text, icon_path, desc);
		}
		
		public void actionPerformed(ActionEvent e) {
			_world.killAll();
		}
	}
	
	class DisperseAllAction extends StdAction {
		private static final long serialVersionUID = 1L;
		public DisperseAllAction(String text, String icon_path, String desc) {
			super(text, icon_path, desc);
		}
		
		public void actionPerformed(ActionEvent e) {
			_world.disperseAll();
		}
	}
	
	class ParametersAction extends StdAction {
		private static final long serialVersionUID = 1L;
		public ParametersAction(String text, String icon_path, String desc) {
			super(text, icon_path, desc);
		}
		
		public void actionPerformed(ActionEvent e) {
			paramDialog();
		}
	}
	
	class ManualAction extends StdAction {
		private static final long serialVersionUID = 1L;
		public ManualAction(String text, String icon_path, String desc) {
			super(text, icon_path, desc);
		}
		
		public void actionPerformed(ActionEvent e) {
			BareBonesBrowserLaunch.openURL("http://biogenesis.sourceforge.net/manual.php."+Messages.getLanguage()); //$NON-NLS-1$
		}
	}
	
	class CheckLastVersionAction extends StdAction {
		private static final long serialVersionUID = 1L;
		public CheckLastVersionAction(String text, String icon_path, String desc) {
			super(text, icon_path, desc);
		}
		
		public void actionPerformed(ActionEvent e) {
			checkLastVersion();
		}
	}
	
	class AboutAction extends StdAction {
		private static final long serialVersionUID = 1L;
		public AboutAction(String text, String icon_path, String desc) {
			super(text, icon_path, desc);
		}
		
		public void actionPerformed(ActionEvent e) {
			about();
		}
	}
	
	class NetConfigAction extends StdAction {
		private static final long serialVersionUID = 1L;
		public NetConfigAction(String text, String icon_path, String desc) {
			super(text, icon_path, desc);
		}
		
		public void actionPerformed(ActionEvent e) {
			netConfig();
		}
	}
	
	protected void paramDialog() {
		new ParamDialog(this);
	}
	
	public void setTrackedOrganism(Organism org) {
		_trackedOrganism = org;
		abortTrackingAction.setEnabled(_trackedOrganism != null);
	}
	
	protected void netConnectionsWindow() {
		new NetConnectionsWindow(this);
	}
	
	public void pauseGame() {
		_isProcessActive = false;
		startStopAction.toogle();
		_menuStartStopGame.setIcon(null);
		setStatusMessage(Messages.getString("T_GAME_PAUSED")); //$NON-NLS-1$
	}
	
	public void playGame() {
		_isProcessActive = true;
		startStopAction.toogle();
		_menuStartStopGame.setIcon(null);
    	setStatusMessage(Messages.getString("T_GAME_RESUMED")); //$NON-NLS-1$
	}
	
	protected File saveGameAs() {
		File savedFile = saveObjectAs(_world);
		if (savedFile != null)
			_gameFile = savedFile;
		return savedFile;
	}
	
	protected void about() {
		String aboutString = Messages.getString("T_PROGRAM_NAME")+"http://biogenesis.sourceforge.net/"  //$NON-NLS-1$//$NON-NLS-2$
		+Messages.getString("T_VERSION")+ //$NON-NLS-1$ 
				Messages.getString("T_COPYRIGHT")+"joanq@users.sourceforge.net\n"+ //$NON-NLS-1$ //$NON-NLS-2$
				Messages.getString("T_ARTWORK_BY")+" Ananda Daydream, Florian Haag (http://toolbaricons.sourceforge.net/)";  //$NON-NLS-1$//$NON-NLS-2$
		JOptionPane.showMessageDialog(this, aboutString, Messages.getString("T_ABOUT"), //$NON-NLS-1$
				JOptionPane.INFORMATION_MESSAGE,imageIcon);
	}
	
	private void setControls () {
		setIconImage(imageIcon.getImage());
		JPanel centralPanel = new JPanel();
		centralPanel.setLayout(new BorderLayout());
		
        _visibleWorld=new VisibleWorld(this);
        scrollPane = new JScrollPane(_visibleWorld);
        scrollPane.getHorizontalScrollBar().setUnitIncrement(10);
        scrollPane.getVerticalScrollBar().setUnitIncrement(10);
        setLocation(Utils.WINDOW_X, Utils.WINDOW_Y);
        setExtendedState(Utils.WINDOW_STATE);
        getContentPane().setLayout(new BorderLayout());
        
        infoToolbar = new InfoToolbar(null, this);
        centralPanel.add(scrollPane, BorderLayout.CENTER);
        centralPanel.add(infoToolbar, BorderLayout.SOUTH);
        
        getContentPane().add(centralPanel, BorderLayout.CENTER);
        
        _statusLabel = new JLabel(" "); //$NON-NLS-1$
        _statusLabel.setBorder(new EtchedBorder());
        _nf = NumberFormat.getInstance();
		_nf.setMaximumFractionDigits(1);
        getContentPane().add(_statusLabel, BorderLayout.SOUTH);
        getContentPane().add(toolBar, BorderLayout.NORTH);
        
        worldChooser.setFileFilter(new BioFileFilter(BioFileFilter.WORLD_EXTENSION));
        geneticCodeChooser.setFileFilter(new BioFileFilter(BioFileFilter.GENETIC_CODE_EXTENSION));
    }
		
	public File saveObjectAs(Object obj) {
		File resultFile = null;
		boolean processState = _isProcessActive;
		_isProcessActive = false;
		
		try {
			JFileChooser chooser;
			if (obj instanceof GeneticCode)
				chooser = getGeneticCodeChooser();
			else
				chooser = getWorldChooser();
			int returnVal = chooser.showSaveDialog(null);
			if(returnVal == JFileChooser.APPROVE_OPTION) {
				int canWrite = JOptionPane.YES_OPTION;
				File f = chooser.getSelectedFile();
				String filename = f.getName();
				String ext = (filename.lastIndexOf(".")==-1)?"":filename.substring(filename.lastIndexOf(".")+1,filename.length());
				if (ext.equals("")) {
					f = new File(f.getAbsolutePath()+"."+((BioFileFilter)chooser.getFileFilter()).getValidExtension());
					chooser.setSelectedFile(f);
				}
				if (f.exists()) {
					canWrite = JOptionPane.showConfirmDialog(null,Messages.getString("T_CONFIRM_FILE_OVERRIDE"), //$NON-NLS-1$
						Messages.getString("T_FILE_EXISTS"),JOptionPane.YES_NO_OPTION,JOptionPane.WARNING_MESSAGE); //$NON-NLS-1$
				}
				if (canWrite == JOptionPane.YES_OPTION) {
					if (obj instanceof GeneticCode) {
						try {
							BioXMLParser.writeGeneticCode(new PrintStream(f),(GeneticCode)obj);
							resultFile = f;
						} catch (FileNotFoundException ex) {
							System.err.println(ex.getLocalizedMessage());
						}
					} else 							
						if (saveObject(obj, f))
							resultFile = f;
				}
			}
		} catch (SecurityException ex) {
			System.err.println(ex.getMessage());
			JOptionPane.showMessageDialog(null,Messages.getString("T_PERMISSION_DENIED"),Messages.getString("T_PERMISSION_DENIED"),JOptionPane.ERROR_MESSAGE); //$NON-NLS-1$ //$NON-NLS-2$
		}
	    _isProcessActive = processState;
	    return resultFile;
	}
	
	public boolean saveObject(Object obj, File f) {
		ObjectOutputStream outputStream;
		try {
			FileOutputStream fileStream = new FileOutputStream(f);
			outputStream = new ObjectOutputStream(fileStream);
			outputStream.writeObject(obj);
			outputStream.close();
			setStatusMessage(Messages.getString("T_WRITING_COMPLETED")); //$NON-NLS-1$
			return true;
		} catch (FileNotFoundException e) {
			System.err.println(e.getMessage());
		} catch (IOException e) {
			System.err.println(e.getMessage());
		} catch (SecurityException ex) {
			System.err.println(ex.getMessage());
			JOptionPane.showMessageDialog(null,Messages.getString("T_PERMISSION_DENIED"),Messages.getString("T_PERMISSION_DENIED"),JOptionPane.ERROR_MESSAGE); //$NON-NLS-1$ //$NON-NLS-2$
		}
		return false;
	}
	
	public void configureApp() {
		setDefaultCloseOperation(WindowConstants.DO_NOTHING_ON_CLOSE);
		addWindowListener(new WindowAdapter() {
			@Override
			public void windowClosing(WindowEvent e) {
				quit();
			}
		});
		setTitle(Messages.getString("T_BIOGENESIS")); //$NON-NLS-1$
		UIManager.put("OptionPane.yesButtonText", Messages.getString("T_YES"));
		UIManager.put("OptionPane.noButtonText", Messages.getString("T_NO"));
		UIManager.put("OptionPane.cancelButtonText", Messages.getString("T_CANCEL"));
	}
	
	public void updateStatusLabel() {
		statusLabelText.setLength(0);
		statusLabelText.append(Messages.getString("T_FPS")); //$NON-NLS-1$
		statusLabelText.append(getFPS());
		statusLabelText.append("     "); //$NON-NLS-1$
		statusLabelText.append(Messages.getString("T_TIME")); //$NON-NLS-1$
		statusLabelText.append(_world.getTime());
		statusLabelText.append("     "); //$NON-NLS-1$
		statusLabelText.append(Messages.getString("T_CURRENT_POPULATION")); //$NON-NLS-1$
		statusLabelText.append(_world.getPopulation());
		statusLabelText.append("     "); //$NON-NLS-1$
		statusLabelText.append(Messages.getString("T_O2")); //$NON-NLS-1$
		statusLabelText.append(_nf.format(_world.getO2()));
		statusLabelText.append("     "); //$NON-NLS-1$
		statusLabelText.append(Messages.getString("T_CO2")); //$NON-NLS-1$
		statusLabelText.append(_nf.format(_world.getCO2()));
		statusLabelText.append("     "); //$NON-NLS-1$
		statusLabelText.append(getStatusMessage());
		_statusLabel.setText(statusLabelText.toString());
	}
	/**
	 * Returns the actual Frame Per Second rate of the program.
	 * 
	 * @return  The actual FPS.
	 */
	public int getFPS() {
		return 1000/currentDelay;
	}
	
	final transient Runnable lifeProcess = new Runnable() {
	    public void run() {
	    	if (_isProcessActive) {
	    		// executa un torn
	    		_world.time();
	    		nFrames++;
	    		if (nFrames % 20 == 0) {
	    			//if (_statisticsWindow != null)
	    			//	_statisticsWindow.recalculate();
	    			updateStatusLabel();
	    		}
	    		// dibuixa de nou si cal
	    		_world.setPaintingRegion();
	    		// tracking
	    		if (_trackedOrganism != null) {
	    			if (!_trackedOrganism.isAlive()) {
	    				_trackedOrganism = null;
	    				abortTrackingAction.setEnabled(false);
	    			}
	    			else {
	    				JScrollBar bar = scrollPane.getHorizontalScrollBar();
	    				bar.setValue(Utils.between(_trackedOrganism._centerX - scrollPane.getWidth()/2,
	    						bar.getValue()-2*(int)Utils.MAX_VEL,bar.getValue()+2*(int)Utils.MAX_VEL));
	    				bar = scrollPane.getVerticalScrollBar();
	    				bar.setValue(Utils.between(_trackedOrganism._centerY - scrollPane.getHeight()/2,
	    						bar.getValue()-2*(int)Utils.MAX_VEL,bar.getValue()+2*(int)Utils.MAX_VEL));
	    			}
	    		}
	    	}
	    }
	};
	
	public void startApp() {
		/*GraphicsDevice gd = GraphicsEnvironment.getLocalGraphicsEnvironment().getDefaultScreenDevice();
		if (gd.isFullScreenSupported()) {
			setResizable(false);
			setUndecorated(true);
			gd.setFullScreenWindow(this);
			validate();
		} else {*/
			setResizable(true);
			setSize(new Dimension(Utils.WINDOW_WIDTH,Utils.WINDOW_HEIGHT));
			setVisible(true);
		//}
		
		_timer = new java.util.Timer();
		startLifeProcess(Utils.DELAY);
		if (isAcceptingConnections())
			startServer();
	}
	/**
	 * Number of milliseconds between two invocations to the lifeProcess invocation.
	 * It starts as the user's preference DELAY but is adapted to the current situation
	 * in the world and the machine speed.
	 */
	protected int currentDelay = Utils.DELAY;
	/**
	 * If positive, the consecutive number of frames that haven't accomplished the expected time contract.
	 * If negative, the consecutive number of frame that have accomplished the expected time contract.
	 * Used to adapt the program's speed.
	 */
	protected int failedTime=0;
	/**
	 * The moment when the last painting of this visual world started. Used to control
	 * the program's speed.
	 */
	protected long lastPaintTime;
	public void startLifeProcess(int delay) {
		if (updateTask != null)
			updateTask.cancel();
		updateTask = new TimerTask() {
		    @Override
			public void run() {
		    	try {
					EventQueue.invokeAndWait(lifeProcess);
					/**
					 * Checks the actual drawing speed and increases or decreases the speed
					 * of the program in order to keep it running smoothly.
					 */
					long actualTime = System.currentTimeMillis();
					if (actualTime - lastPaintTime > currentDelay*1.5 || currentDelay < Utils.DELAY) {
		    			// We can't run so fast
		    			failedTime=Math.max(failedTime+1, 0);
		    			if (failedTime >= 2) {
		    				failedTime = 0;
		    				currentDelay*=1.5;
		    				startLifeProcess(currentDelay);
		    			}
		    		} else {
		    			if (actualTime - lastPaintTime < currentDelay*1.2 && currentDelay > Utils.DELAY) {
		    				// We can run faster
		    				failedTime=Math.min(failedTime-1, 0);
		    				if (failedTime <= -10) {
		    					currentDelay = Math.max(Utils.DELAY, currentDelay-1);
		    					startLifeProcess(currentDelay);
		    				}
		    			} else
		    				// Normal situation: we run at the expected speed
		    				failedTime = 0;
		    		}
		    		if (currentDelay > 1000) {
		    			pauseGame();
		    		}
		    		lastPaintTime = actualTime;
				} catch (InterruptedException e) {
					e.printStackTrace();
				} catch (InvocationTargetException e) {
					e.printStackTrace();
				}
		    }
		};
		_timer.schedule(updateTask, delay, delay);
	}
	
	public void changeLocale() {
		UIManager.put("OptionPane.yesButtonText", Messages.getString("T_YES"));
		UIManager.put("OptionPane.noButtonText", Messages.getString("T_NO"));
		UIManager.put("OptionPane.cancelButtonText", Messages.getString("T_CANCEL"));
		_menuGame.setText(Messages.getString("T_GAME")); //$NON-NLS-1$
		_menuGame.setMnemonic(Messages.getMnemonic("T_GAME").intValue()); //$NON-NLS-1$
		newGameAction.changeLocale();
		startStopAction.changeLocale();
		openGameAction.changeLocale();
		saveGameAction.changeLocale();
		saveGameAsAction.changeLocale();
		quitAction.changeLocale();
		_menuWorld.setText(Messages.getString("T_WORLD")); //$NON-NLS-1$
		_menuWorld.setMnemonic(Messages.getMnemonic("T_WORLD").intValue()); //$NON-NLS-1$
		statisticsAction.changeLocale();
		increaseCO2Action.changeLocale();
		decreaseCO2Action.changeLocale();
		parametersAction.changeLocale();
		labAction.changeLocale();
		killAllAction.changeLocale();
		disperseAllAction.changeLocale();
		_menuHelp.setText(Messages.getString("T_HELP")); //$NON-NLS-1$
		_menuHelp.setMnemonic(Messages.getMnemonic("T_HELP").intValue()); //$NON-NLS-1$
		manualAction.changeLocale();
		checkLastVersionAction.changeLocale();
		aboutAction.changeLocale();
		_menuNet.setText(Messages.getString("T_NETWORK")); //$NON-NLS-1$
		_menuNet.setMnemonic(Messages.getMnemonic("T_NETWORK").intValue()); //$NON-NLS-1$
		netConfigAction.changeLocale();
		manageConnectionsAction.changeLocale();
		setTitle(Messages.getString("T_BIOGENESIS")); //$NON-NLS-1$
		createMenu();
		_visibleWorld.changeLocale();
		infoToolbar.changeLocale();
	}
}
