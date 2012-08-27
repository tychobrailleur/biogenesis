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
import javax.swing.event.ChangeEvent;
import javax.swing.event.ChangeListener;

import java.awt.*;
import java.awt.event.*;

public class ParamDialog extends JDialog {
	private static final long serialVersionUID = Utils.FILE_VERSION;
	private JComboBox localeCombo = null;
	private JTextField widthText = null;
	private JTextField heightText = null;
	private JTextField delayText = null;
	protected JRadioButton hardwareNoneRadio = null;
	protected JRadioButton hardwareOpenGLRadio = null;
	private ButtonGroup hardwareGroup = null;
	protected JCheckBox hardwareFBObjectCheck = null;
	private JTextField rubbingText = null;
	private JTextField elasticityText = null;
	private JTextField initialnumberText = null;
	private JTextField initialenergyText = null;
	private JTextField initialO2Text = null;
	private JTextField initialCO2Text = null;
	private JTextField maxageText = null;
	private JTextField mutationrateText = null;
	private JTextField redcostText = null;
	private JTextField greencostText = null;
	private JTextField bluecostText = null;
	private JTextField cyancostText = null;
	private JTextField whitecostText = null;
	private JTextField graycostText = null;
	private JTextField yellowcostText = null;
	private JTextField segmentcostText = null;
	private JTextField drainText = null;
	private JTextField greenenergyText = null;
	private JTextField organicenergyText = null;
	private JTextField organicsubsText = null;
	private JTextField redprobText = null;
	private JTextField greenprobText = null;
	private JTextField blueprobText = null;
	private JTextField cyanprobText = null;
	private JTextField whiteprobText = null;
	private JTextField grayprobText = null;
	private JTextField yellowprobText = null;
	private JTextField decayenergyText = null;
	private JButton OKButton = null;
	private JButton cancelButton = null;
	private JButton defaultButton = null;
	
	protected MainWindow mainWindow;
	
	public ParamDialog(MainWindow parent) {
		// Configurem les caracter�stiques generals
		super(parent,Messages.getString("T_PARAMETERS_CONFIGURATION"),true); //$NON-NLS-1$
		mainWindow = parent;
		setDefaultCloseOperation(DISPOSE_ON_CLOSE);
		setComponents();
		pack();
		setResizable(false);
		// Configurem les accions dels butons
		OKButton.addActionListener(new ActionListener() {
                    public void actionPerformed(ActionEvent evt) {
                    	checkParams();
                    	Utils.savePreferences();
                    	dispose();
                    }
                    });
		cancelButton.addActionListener(new ActionListener() {
                    public void actionPerformed(ActionEvent evt) {
                    	dispose();
                    }
                    });
		defaultButton.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent evt) {
				defaultPreferences();
			}
		});
		// Ja est� tot apunt
		setVisible(true);
	}
	
	protected void defaultPreferences() {
		widthText.setText(String.valueOf(Utils.DEF_WORLD_WIDTH));
		heightText.setText(String.valueOf(Utils.DEF_WORLD_HEIGHT));
		delayText.setText(String.valueOf(Utils.DEF_DELAY));
		rubbingText.setText(String.valueOf(Utils.DEF_RUBBING));
		elasticityText.setText(String.valueOf(Utils.DEF_ELASTICITY));
		initialnumberText.setText(String.valueOf(Utils.DEF_INITIAL_ORGANISMS));
		initialenergyText.setText(String.valueOf(Utils.DEF_INITIAL_ENERGY));
		initialO2Text.setText(String.valueOf(Utils.DEF_INITIAL_O2));
		initialCO2Text.setText(String.valueOf(Utils.DEF_INITIAL_CO2));
		maxageText.setText(String.valueOf(Utils.DEF_MAX_AGE));
		mutationrateText.setText(String.valueOf(Utils.DEF_MUTATION_RATE));
		segmentcostText.setText(String.valueOf(Utils.DEF_SEGMENT_COST_DIVISOR));
		drainText.setText(String.valueOf(Utils.DEF_DRAIN_SUBS_DIVISOR));
		greenenergyText.setText(String.valueOf(Utils.DEF_GREEN_OBTAINED_ENERGY_DIVISOR));
		organicenergyText.setText(String.valueOf(Utils.DEF_ORGANIC_OBTAINED_ENERGY));
		organicsubsText.setText(String.valueOf(Utils.DEF_ORGANIC_SUBS_PRODUCED));
		redcostText.setText(String.valueOf(Utils.DEF_RED_ENERGY_CONSUMPTION));
		greencostText.setText(String.valueOf(Utils.DEF_GREEN_ENERGY_CONSUMPTION));
		bluecostText.setText(String.valueOf(Utils.DEF_BLUE_ENERGY_CONSUMPTION));
		cyancostText.setText(String.valueOf(Utils.DEF_CYAN_ENERGY_CONSUMPTION));
		whitecostText.setText(String.valueOf(Utils.DEF_WHITE_ENERGY_CONSUMPTION));
		graycostText.setText(String.valueOf(Utils.DEF_GRAY_ENERGY_CONSUMPTION));
		yellowcostText.setText(String.valueOf(Utils.DEF_YELLOW_ENERGY_CONSUMPTION));
		redprobText.setText(String.valueOf(Utils.DEF_RED_PROB));
		greenprobText.setText(String.valueOf(Utils.DEF_GREEN_PROB));
		blueprobText.setText(String.valueOf(Utils.DEF_BLUE_PROB));
		cyanprobText.setText(String.valueOf(Utils.DEF_CYAN_PROB));
		whiteprobText.setText(String.valueOf(Utils.DEF_WHITE_PROB));
		grayprobText.setText(String.valueOf(Utils.DEF_GRAY_PROB));
		yellowprobText.setText(String.valueOf(Utils.DEF_YELLOW_PROB));
		decayenergyText.setText(String.valueOf(Utils.DEF_DECAY_ENERGY));
		switch (Utils.DEF_HARDWARE_ACCELERATION) {
		case 0:
		case 2:
		case 5:
			hardwareNoneRadio.setSelected(true);
			hardwareFBObjectCheck.setSelected(false);
			hardwareFBObjectCheck.setEnabled(false);
			break;
		case 1:
		case 3:
			hardwareOpenGLRadio.setSelected(true);
			hardwareFBObjectCheck.setSelected(false);
			break;
		case 6:
			hardwareOpenGLRadio.setSelected(true);
			hardwareFBObjectCheck.setSelected(true);
			break;
		}
	}
	
	protected void setComponents() {
		getContentPane().setLayout(new BorderLayout());
		// Set up buttons
		JPanel buttonsPanel = new JPanel();
		OKButton = new JButton(Messages.getString("T_OK")); //$NON-NLS-1$
		cancelButton = new JButton(Messages.getString("T_CANCEL")); //$NON-NLS-1$
		defaultButton = new JButton(Messages.getString("T_DEFAULT_VALUES")); //$NON-NLS-1$
		buttonsPanel.add(OKButton);
		buttonsPanel.add(cancelButton);
		buttonsPanel.add(defaultButton);
		getContentPane().add(buttonsPanel, BorderLayout.SOUTH);
		JTabbedPane tabbedPane = new JTabbedPane();
		// Set up general tab
		tabbedPane.addTab(Messages.getString("T_GENERAL"), setGeneralTab()); //$NON-NLS-1$
		// Set up world tab
		tabbedPane.addTab(Messages.getString("T_WORLD"), setWorldTab()); //$NON-NLS-1$
		// Set up organisms tab
		tabbedPane.addTab(Messages.getString("T_ORGANISMS"), setOrganismsTab()); //$NON-NLS-1$
		// Set up metabolism tab
		tabbedPane.addTab(Messages.getString("T_METABOLISM"), setMetabolismTab()); //$NON-NLS-1$
		// Set up genes tab
		tabbedPane.addTab(Messages.getString("T_GENES"), setGenesTab()); //$NON-NLS-1$
		getContentPane().add(tabbedPane, BorderLayout.CENTER);
	}
	
	protected JPanel setGeneralTab() {
		JPanel generalPanel = new JPanel();
		generalPanel.setLayout(new BoxLayout(generalPanel,BoxLayout.Y_AXIS));
		// Language
		JPanel panel = new JPanel();
		JLabel label = new JLabel(Messages.getString("T_LANGUAGE")); //$NON-NLS-1$
		panel.add(label);
		localeCombo = new JComboBox(Messages.getSupportedLocalesNames());
		localeCombo.setSelectedIndex(Messages.getLocaleIndex());
		panel.add(localeCombo);
		generalPanel.add(panel);
		//Frames per second
		panel = new JPanel();
		label = new JLabel(Messages.getString("T_TIME_PER_FRAME")); //$NON-NLS-1$
		panel.add(label);
		delayText = new JTextField(Integer.toString(Utils.DELAY),6);
		panel.add(delayText);
		label = new JLabel(Messages.getString("T_MILLISECONDS")); //$NON-NLS-1$
		panel.add(label);
		generalPanel.add(panel);
		// OpenGL
		panel = new JPanel();
		panel.setLayout(new GridLayout(5,1));
		panel.setBorder(BorderFactory.createTitledBorder(BorderFactory.createEtchedBorder(EtchedBorder.LOWERED),
				Messages.getString("T_HARDWARE_ACCELERATION"))); //$NON-NLS-1$
		hardwareNoneRadio = new JRadioButton(Messages.getString("T_NONE")); //$NON-NLS-1$
		hardwareOpenGLRadio = new JRadioButton(Messages.getString("T_OPENGL")); //$NON-NLS-1$
		hardwareFBObjectCheck = new JCheckBox(Messages.getString("T_DISABLE_FBOBJECT")); //$NON-NLS-1$
		switch (Utils.HARDWARE_ACCELERATION) {
		case 0:
			hardwareNoneRadio.setSelected(true);
			hardwareFBObjectCheck.setEnabled(false);
			break;
		case 1:
		case 2:
		case 3:
			hardwareOpenGLRadio.setSelected(true);
			break;
		case 4:
		case 5:
		case 6:
			hardwareOpenGLRadio.setSelected(true);
			hardwareFBObjectCheck.setSelected(true);
			break;
		}
		hardwareGroup = new ButtonGroup();
		hardwareGroup.add(hardwareNoneRadio);
		hardwareGroup.add(hardwareOpenGLRadio);
		hardwareNoneRadio.addChangeListener(new ChangeListener() {
			public void stateChanged(ChangeEvent e) {
				if (hardwareNoneRadio.isSelected())
					hardwareFBObjectCheck.setEnabled(false);
				else
					hardwareFBObjectCheck.setEnabled(true);				
			}
		});
		panel.add(hardwareNoneRadio);
		panel.add(hardwareOpenGLRadio);
		panel.add(hardwareFBObjectCheck);
		panel.add(new JLabel(Messages.getString("T_DIRECTX_IS_AUTOMATICALLY_DETECTED_AND_INITIALIZED"))); //$NON-NLS-1$
		panel.add(new JLabel(Messages.getString("T_APPLICATION_MUST_BE_RESTARTED_TO_APPLY_CHANGES"))); //$NON-NLS-1$
		generalPanel.add(panel);
		return generalPanel;
	}
	
	protected JPanel setWorldTab() {
		JPanel worldPanel = new JPanel();
		worldPanel.setLayout(new BoxLayout(worldPanel,BoxLayout.Y_AXIS));
		// World size
		JPanel panel = new JPanel();
		JLabel label = new JLabel(Messages.getString("T_WIDTH")); //$NON-NLS-1$
		panel.add(label);
		widthText = new JTextField(Integer.toString(Utils.WORLD_WIDTH),6);
		panel.add(widthText);
		label = new JLabel(Messages.getString("T_HEIGHT")); //$NON-NLS-1$
		panel.add(label);
		heightText = new JTextField(Integer.toString(Utils.WORLD_HEIGHT),6);
		panel.add(heightText);
		worldPanel.add(panel);
		// Initial O2 - initial CO2
		panel = new JPanel();
		label = new JLabel(Messages.getString("T_INITIAL_OXYGEN")); //$NON-NLS-1$
		panel.add(label);
		initialO2Text = new JTextField(Double.toString(Utils.INITIAL_O2),6);
		panel.add(initialO2Text);
		label = new JLabel(Messages.getString("T_INITIAL_CARBON_DIOXIDE")); //$NON-NLS-1$
		panel.add(label);
		initialCO2Text = new JTextField(Double.toString(Utils.INITIAL_CO2),6);
		panel.add(initialCO2Text);
		worldPanel.add(panel);
		// Rubbing - Elasticity
		panel = new JPanel();
		label = new JLabel(Messages.getString("T_RUBBING_COEFFICIENT")); //$NON-NLS-1$
		panel.add(label);
		rubbingText = new JTextField(Double.toString(Utils.RUBBING),6);
		panel.add(rubbingText);
		worldPanel.add(panel);
		panel = new JPanel();
		label = new JLabel(Messages.getString("T_ELASTICITY_COEFFICIENT")); //$NON-NLS-1$
		panel.add(label);
		elasticityText = new JTextField(Double.toString(Utils.ELASTICITY),6);
		panel.add(elasticityText);
		worldPanel.add(panel);
		
		return worldPanel;
	}
	
	protected JPanel setOrganismsTab() {
		JPanel organismsPanel = new JPanel();
		organismsPanel.setLayout(new BoxLayout(organismsPanel,BoxLayout.Y_AXIS));
		// Initial number - initial energy
		JPanel panel = new JPanel();
		JLabel label = new JLabel(Messages.getString("T_INITIAL_NUMBER")); //$NON-NLS-1$
		panel.add(label);
		initialnumberText = new JTextField(Integer.toString(Utils.INITIAL_ORGANISMS),6);
		panel.add(initialnumberText);
		label = new JLabel(Messages.getString("T_INITIAL_ENERGY")); //$NON-NLS-1$
		panel.add(label);
		initialenergyText = new JTextField(Integer.toString(Utils.INITIAL_ENERGY),6);
		panel.add(initialenergyText);
		organismsPanel.add(panel);
		// Max age - Mutation rate
		panel = new JPanel();
		label = new JLabel(Messages.getString("T_LIFE_EXPECTANCY")); //$NON-NLS-1$
		panel.add(label);
		maxageText = new JTextField(Integer.toString(Utils.MAX_AGE),6);
		panel.add(maxageText);
		label = new JLabel(Messages.getString("T_MUTATION_PERCENTAGE")); //$NON-NLS-1$
		panel.add(label);
		mutationrateText = new JTextField(Double.toString(Utils.MUTATION_RATE),6);
		panel.add(mutationrateText);
		organismsPanel.add(panel);
		// Upkeep cost
		panel = new JPanel();
		label = new JLabel(Messages.getString("T_UPKEEP_COST_DIVISOR")); //$NON-NLS-1$
		panel.add(label);
		segmentcostText = new JTextField(Integer.toString(Utils.SEGMENT_COST_DIVISOR),6);
		panel.add(segmentcostText);
		organismsPanel.add(panel);
		// Drain substance
		panel = new JPanel();
		label = new JLabel(Messages.getString("T_SUBSTANCE_DRAINAGE_DIVISOR")); //$NON-NLS-1$
		panel.add(label);
		drainText = new JTextField(Integer.toString(Utils.DRAIN_SUBS_DIVISOR),6);
		panel.add(drainText);
		organismsPanel.add(panel);
		// Decay energy
		panel = new JPanel();
		label = new JLabel(Messages.getString("T_DECAY_ENERGY")); //$NON-NLS-1$
		panel.add(label);
		decayenergyText = new JTextField(Double.toString(Utils.DECAY_ENERGY),6);
		panel.add(decayenergyText);
		organismsPanel.add(panel);
		return organismsPanel;
	}
	
	protected JPanel setMetabolismTab() {
		JPanel metabolismPanel = new JPanel();
		metabolismPanel.setLayout(new BoxLayout(metabolismPanel,BoxLayout.Y_AXIS));
		// Photosynthetic metabolism
		JPanel panel = new JPanel();
		JLabel label = new JLabel(Messages.getString("T_PHOTOSYNTHETIC_METABOLISM")); //$NON-NLS-1$
		label.setAlignmentX(Component.CENTER_ALIGNMENT);
		metabolismPanel.add(label);
		metabolismPanel.add(Box.createVerticalStrut(10));
		// Obtained energy
		label = new JLabel(Messages.getString("T_OBTAINED_ENERGY_DIVISOR")); //$NON-NLS-1$
		panel.add(label);
		greenenergyText = new JTextField(Integer.toString(Utils.GREEN_OBTAINED_ENERGY_DIVISOR),6);
		panel.add(greenenergyText);
		metabolismPanel.add(panel);
		// Chemoorganotrophic metabolism
		label = new JLabel(Messages.getString("T_CHEMOORGANOTROPHIC_METABOLISM")); //$NON-NLS-1$
		label.setAlignmentX(Component.CENTER_ALIGNMENT);
		metabolismPanel.add(Box.createVerticalStrut(10));
		metabolismPanel.add(label);
		metabolismPanel.add(Box.createVerticalStrut(10));
		// Obtained energy
		panel = new JPanel();
		label = new JLabel(Messages.getString("T_OBTAINED_ENERGY")); //$NON-NLS-1$
		panel.add(label);
		organicenergyText = new JTextField(Double.toString(Utils.ORGANIC_OBTAINED_ENERGY),6);
		panel.add(organicenergyText);
		metabolismPanel.add(panel);
		// Released energy
		panel = new JPanel();
		label = new JLabel(Messages.getString("T_RELEASED_ENERGY_PROPORTION")); //$NON-NLS-1$
		panel.add(label);
		organicsubsText = new JTextField(Double.toString(Utils.ORGANIC_SUBS_PRODUCED),6);
		panel.add(organicsubsText);
		metabolismPanel.add(panel);
		
		return metabolismPanel;
	}
	
	protected JPanel setGenesTab() {
		JPanel genesPanel = new JPanel();
		genesPanel.setLayout(new GridLayout(8,3));
		JLabel label;

		genesPanel.add(new JLabel(Messages.getString("T_COLOR2"),SwingConstants.CENTER)); //$NON-NLS-1$
		genesPanel.add(new JLabel(Messages.getString("T_PROBABILITY"),SwingConstants.CENTER)); //$NON-NLS-1$
		genesPanel.add(new JLabel(Messages.getString("T_COST"),SwingConstants.CENTER)); //$NON-NLS-1$
		
		label = new JLabel(Messages.getString("T_RED"),SwingConstants.CENTER); //$NON-NLS-1$
		genesPanel.add(label);
		redprobText = new JTextField(Integer.toString(Utils.RED_PROB));
		genesPanel.add(redprobText);
		redcostText = new JTextField(Double.toString(Utils.RED_ENERGY_CONSUMPTION));
		genesPanel.add(redcostText);
		
		label = new JLabel(Messages.getString("T_GREEN"),SwingConstants.CENTER); //$NON-NLS-1$
		genesPanel.add(label);
		greenprobText = new JTextField(Integer.toString(Utils.GREEN_PROB));
		genesPanel.add(greenprobText);
		greencostText = new JTextField(Double.toString(Utils.GREEN_ENERGY_CONSUMPTION));
		genesPanel.add(greencostText);
		
		label = new JLabel(Messages.getString("T_BLUE"),SwingConstants.CENTER); //$NON-NLS-1$
		genesPanel.add(label);
		blueprobText = new JTextField(Integer.toString(Utils.BLUE_PROB));
		genesPanel.add(blueprobText);
		bluecostText = new JTextField(Double.toString(Utils.BLUE_ENERGY_CONSUMPTION));
		genesPanel.add(bluecostText);
		
		label = new JLabel(Messages.getString("T_CYAN"),SwingConstants.CENTER); //$NON-NLS-1$
		genesPanel.add(label);
		cyanprobText = new JTextField(Integer.toString(Utils.CYAN_PROB));
		genesPanel.add(cyanprobText);
		cyancostText = new JTextField(Double.toString(Utils.CYAN_ENERGY_CONSUMPTION));
		genesPanel.add(cyancostText);
		
		label = new JLabel(Messages.getString("T_WHITE"),SwingConstants.CENTER); //$NON-NLS-1$
		genesPanel.add(label);
		whiteprobText = new JTextField(Integer.toString(Utils.WHITE_PROB));
		genesPanel.add(whiteprobText);
		whitecostText = new JTextField(Double.toString(Utils.WHITE_ENERGY_CONSUMPTION));
		genesPanel.add(whitecostText);
		
		label = new JLabel(Messages.getString("T_GRAY"),SwingConstants.CENTER); //$NON-NLS-1$
		genesPanel.add(label);
		grayprobText = new JTextField(Integer.toString(Utils.GRAY_PROB));
		genesPanel.add(grayprobText);
		graycostText = new JTextField(Double.toString(Utils.GRAY_ENERGY_CONSUMPTION));
		genesPanel.add(graycostText);
	
		label = new JLabel(Messages.getString("T_YELLOW"),SwingConstants.CENTER); //$NON-NLS-1$
		genesPanel.add(label);
		yellowprobText = new JTextField(Integer.toString(Utils.YELLOW_PROB));
		genesPanel.add(yellowprobText);
		yellowcostText = new JTextField(Double.toString(Utils.YELLOW_ENERGY_CONSUMPTION));
		genesPanel.add(yellowcostText);
		
		return genesPanel;
	}
	
	void checkParams() {
		int i;
		double d;
		if (Messages.getLocaleIndex() != localeCombo.getSelectedIndex()) {
			Messages.setLocale(localeCombo.getSelectedIndex());
			mainWindow.changeLocale();
		}
		try {
			i = Integer.parseInt(widthText.getText());
			if (i > 0) Utils.WORLD_WIDTH = i;
		} catch (NumberFormatException ex) {
			// Keep old value if there is a problem
		}
		try {
			i = Integer.parseInt(heightText.getText());
			if (i > 0) Utils.WORLD_HEIGHT = i;
		} catch (NumberFormatException ex) {
			// Keep old value if there is a problem
		}
		try {
			i = Integer.parseInt(delayText.getText());
			if (i > 0) {
				Utils.DELAY = i;
			}
		} catch (NumberFormatException ex) {
			// Keep old value if there is a problem
		}
		try {
			d = Double.parseDouble(initialO2Text.getText());
			if (d >= 0) Utils.INITIAL_O2 = d;
		} catch (NumberFormatException ex) {
			// Keep old value if there is a problem
		}
		try {
			d = Double.parseDouble(initialCO2Text.getText());
			if (d >= 0) Utils.INITIAL_CO2 = d;
		} catch (NumberFormatException ex) {
			// Keep old value if there is a problem
		}
		try {
			d = Double.parseDouble(rubbingText.getText());
			if (d >= 0 && d <= 1) Utils.RUBBING = d;
		} catch (NumberFormatException ex) {
			// Keep old value if there is a problem
		}
		try {
			d = Double.parseDouble(elasticityText.getText());
			if (d >= 0 && d <= 1) Utils.ELASTICITY = d;
		} catch (NumberFormatException ex) {
			// Keep old value if there is a problem
		}
		try {
			i = Integer.parseInt(initialnumberText.getText());
			if (i >= 0) Utils.INITIAL_ORGANISMS = i;
		} catch (NumberFormatException ex) {
			// Keep old value if there is a problem
		}
		try {
			i = Integer.parseInt(initialenergyText.getText());
			if (i > 0) Utils.INITIAL_ENERGY = i;
		} catch (NumberFormatException ex) {
			// Keep old value if there is a problem
		}
		try {
			i = Integer.parseInt(maxageText.getText());
			if (i > 0) Utils.MAX_AGE = i;
		} catch (NumberFormatException ex) {
			// Keep old value if there is a problem
		}
		try {
			d = Double.parseDouble(mutationrateText.getText());
			if (d >= 0 && d <= 100) Utils.MUTATION_RATE = d;
		} catch (NumberFormatException ex) {
			// Keep old value if there is a problem
		}
		try {
			d = Double.parseDouble(redcostText.getText());
			if (d >= 0) Utils.RED_ENERGY_CONSUMPTION = d;
		} catch (NumberFormatException ex) {
			// Keep old value if there is a problem
		}
		try {
			d = Double.parseDouble(greencostText.getText());
			if (d >= 0) Utils.GREEN_ENERGY_CONSUMPTION = d;
		} catch (NumberFormatException ex) {
			// Keep old value if there is a problem
		}
		try {
			d = Double.parseDouble(bluecostText.getText());
			if (d >= 0) Utils.BLUE_ENERGY_CONSUMPTION = d;
		} catch (NumberFormatException ex) {
			// Keep old value if there is a problem
		}
		try {
			d = Double.parseDouble(cyancostText.getText());
			if (d >= 0) Utils.CYAN_ENERGY_CONSUMPTION = d;
		} catch (NumberFormatException ex) {
			// Keep old value if there is a problem
		}
		try {
			d = Double.parseDouble(whitecostText.getText());
			if (d >= 0) Utils.WHITE_ENERGY_CONSUMPTION = d;
		} catch (NumberFormatException ex) {
			// Keep old value if there is a problem
		}
		try {
			d = Double.parseDouble(graycostText.getText());
			if (d >= 0) Utils.GRAY_ENERGY_CONSUMPTION = d;
		} catch (NumberFormatException ex) {
			// Keep old value if there is a problem
		}
		try {
			d = Double.parseDouble(yellowcostText.getText());
			if (d >= 0) Utils.YELLOW_ENERGY_CONSUMPTION = d;
		} catch (NumberFormatException ex) {
			// Keep old value if there is a problem
		}
		try {
			i = Integer.parseInt(redprobText.getText());
			if (i >= 0) Utils.RED_PROB = i;
		} catch (NumberFormatException ex) {
			// Keep old value if there is a problem
		}
		try {
			i = Integer.parseInt(greenprobText.getText());
			if (i > 0) Utils.GREEN_PROB = i;
		} catch (NumberFormatException ex) {
			// Keep old value if there is a problem
		}
		try {
			i = Integer.parseInt(blueprobText.getText());
			if (i >= 0) Utils.BLUE_PROB = i;
		} catch (NumberFormatException ex) {
			// Keep old value if there is a problem
		}
		try {
			i = Integer.parseInt(cyanprobText.getText());
			if (i >= 0) Utils.CYAN_PROB = i;
		} catch (NumberFormatException ex) {
			// Keep old value if there is a problem
		}
		try {
			i = Integer.parseInt(whiteprobText.getText());
			if (i >= 0) Utils.WHITE_PROB = i;
		} catch (NumberFormatException ex) {
			// Keep old value if there is a problem
		}
		try {
			i = Integer.parseInt(grayprobText.getText());
			if (i >= 0) Utils.GRAY_PROB = i;
		} catch (NumberFormatException ex) {
			// Keep old value if there is a problem
		}
		try {
			i = Integer.parseInt(yellowprobText.getText());
			if (i >= 0) Utils.YELLOW_PROB = i;
		} catch (NumberFormatException ex) {
			// Keep old value if there is a problem
		}
		try {
			i = Integer.parseInt(segmentcostText.getText());
			if (i > 0) Utils.SEGMENT_COST_DIVISOR = i;
		} catch (NumberFormatException ex) {
			// Keep old value if there is a problem
		}
		try {
			i = Integer.parseInt(drainText.getText());
			if (i > 0) Utils.DRAIN_SUBS_DIVISOR = i;
		} catch (NumberFormatException ex) {
			// Keep old value if there is a problem
		}
		
		try {
			i = Integer.parseInt(greenenergyText.getText());
			if (i > 0) Utils.GREEN_OBTAINED_ENERGY_DIVISOR = i;
		} catch (NumberFormatException ex) {
			// Keep old value if there is a problem
		}
		try {
			d = Double.parseDouble(organicenergyText.getText());
			if (d >= 0) Utils.ORGANIC_OBTAINED_ENERGY = d;
		} catch (NumberFormatException ex) {
			// Keep old value if there is a problem
		}
		try {
			d = Double.parseDouble(organicsubsText.getText());
			if (d >= 0 && d <= 1) Utils.ORGANIC_SUBS_PRODUCED = d;
		} catch (NumberFormatException ex) {
			// Keep old value if there is a problem
		}
		if (hardwareNoneRadio.isSelected()) {
			Utils.setHardwareAcceleration(0);
		}
		if (hardwareOpenGLRadio.isSelected()) {
			if (hardwareFBObjectCheck.isSelected()) {
				if (Utils.HARDWARE_ACCELERATION < 4)
					Utils.setHardwareAcceleration(4);
			} else {
				if (Utils.HARDWARE_ACCELERATION != 0 && Utils.HARDWARE_ACCELERATION > 3)
					Utils.setHardwareAcceleration(1);
			}
		}
	}
}
