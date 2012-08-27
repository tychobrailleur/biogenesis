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

import java.awt.Dimension;
import java.awt.GridBagConstraints;
import java.awt.GridBagLayout;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.text.NumberFormat;

import javax.swing.JButton;
import javax.swing.JLabel;
import javax.swing.JToolBar;

public class InfoToolbar extends JToolBar {
	private static final long serialVersionUID = Utils.FILE_VERSION;

	protected Organism _selOrganism;
	protected JLabel _lEnergy, _lID, _lGeneration, _lAge, _lChildren, _lKills, _lInfected, _lMass, _lReproduceEnergy;
	protected JButton _buttonGenes;
	protected GeneticCodePanel _geneticCodePanel;
	static private NumberFormat _nf = NumberFormat.getInstance();
	protected MainWindow _mainWindow;

	private JLabel _lT_REPRODUCTION;

	private JLabel _lT_ID;

	private JLabel _lT_GENERATION;

	private JLabel _lT_AGE;

	private JLabel _lT_ENERGY;

	private JLabel _lT_CHILDREN;

	private JLabel _lT_VICTIMS;

	private JLabel _lT_INFECTED;

	private JLabel _lT_MASS;
	
	public void setSelectedOrganism(Organism selectedOrganism) {
		_selOrganism = selectedOrganism;
		_lID.setText(_selOrganism!=null?_nf.format(_selOrganism.getID()):"-1");
		_lGeneration.setText(_selOrganism!=null?_nf.format(_selOrganism.getGeneration()):"0");
		_lReproduceEnergy.setText(_selOrganism!=null?_nf.format(_selOrganism.getGeneticCode().getReproduceEnergy()):"0");
		recalculate();
		changeNChildren();
		changeNKills();
		changeNInfected();
		_buttonGenes.setEnabled(_selOrganism != null);
		_geneticCodePanel.setGeneticCode(_selOrganism!=null?_selOrganism.getGeneticCode():null);
		_geneticCodePanel.repaint();
		setVisible(_selOrganism != null);
	}
	
	// Recalculate continuously changing parameters
	public void recalculate() {
		_lEnergy.setText(_selOrganism!=null?_nf.format(_selOrganism.getEnergy()):"0"); //$NON-NLS-1$
		_lAge.setText(_selOrganism!=null?_nf.format(_selOrganism.getAge()>>8):"0"); //$NON-NLS-1$
		_lMass.setText(_selOrganism!=null?_nf.format(_selOrganism.getMass()):"0"); //$NON-NLS-1$
	}
	
	// Notify panel of important events
	public void changeNChildren() {
		_lChildren.setText(_selOrganism!=null?_nf.format(_selOrganism.getTotalChildren()):"0"); //$NON-NLS-1$
	}
	
	public void changeNKills() {
		_lKills.setText(_selOrganism!=null?_nf.format(_selOrganism.getTotalKills()):"0"); //$NON-NLS-1$
	}
	
	public void changeNInfected() {
		_lInfected.setText(_selOrganism!=null?_nf.format(_selOrganism.getTotalInfected()):"0"); //$NON-NLS-1$
	}
	
	public InfoToolbar(Organism selectedOrganism, MainWindow mainWindow) {
		Dimension dimension = new Dimension(60,10);
		_selOrganism = selectedOrganism;
		_mainWindow = mainWindow;
		// Prepare number format
		_nf.setMaximumFractionDigits(1);
		// Create components
		setLayout(new GridBagLayout());
		GridBagConstraints gridBagConstraints;
		// ID
		gridBagConstraints = new GridBagConstraints();
	    gridBagConstraints.gridx = 0;
	    gridBagConstraints.gridy = 0;
	    gridBagConstraints.anchor = GridBagConstraints.WEST;
	    gridBagConstraints.weightx = 1.0;
	    gridBagConstraints.gridheight=3;
	    _geneticCodePanel = new GeneticCodePanel(_selOrganism!=null?_selOrganism.getGeneticCode():null, _mainWindow.getVisibleWorld());
	    _geneticCodePanel.setPreferredSize(new Dimension(50,50));
	    add(_geneticCodePanel, gridBagConstraints);
	    
	    gridBagConstraints.gridheight=1;
	    gridBagConstraints.gridx = 1;
	    gridBagConstraints.gridy = 0;

	    _lT_ID = new JLabel(Messages.getString("T_ID"), JLabel.CENTER); //$NON-NLS-1$
		add(_lT_ID, gridBagConstraints);
		_lID = new JLabel(_selOrganism!=null?_nf.format(_selOrganism.getID()):"-1",JLabel.CENTER); //$NON-NLS-1$
		_lID.setPreferredSize(dimension);
		gridBagConstraints.gridx = 2;
		gridBagConstraints.gridy = 0;
		add(_lID, gridBagConstraints);
		// Generation
		gridBagConstraints.gridx = 3;
		gridBagConstraints.gridy = 0;
		_lT_GENERATION = new JLabel(Messages.getString("T_GENERATION"),JLabel.CENTER); //$NON-NLS-1$
		add(_lT_GENERATION, gridBagConstraints);
		_lGeneration = new JLabel(_selOrganism!=null?_nf.format(_selOrganism.getGeneration()):"0",JLabel.CENTER); //$NON-NLS-1$
		_lGeneration.setPreferredSize(dimension);
		gridBagConstraints.gridx = 4;
		gridBagConstraints.gridy = 0;
		add(_lGeneration, gridBagConstraints);
		// Age
		gridBagConstraints.gridx = 5;
		gridBagConstraints.gridy = 0;
		_lT_AGE = new JLabel(Messages.getString("T_AGE"),JLabel.CENTER); //$NON-NLS-1$
		add(_lT_AGE, gridBagConstraints);
		_lAge = new JLabel(_selOrganism!=null?_nf.format(_selOrganism.getAge()>>8):"0",JLabel.CENTER); //$NON-NLS-1$
		_lAge.setPreferredSize(dimension);
		gridBagConstraints.gridx = 6;
		gridBagConstraints.gridy = 0;
		add(_lAge, gridBagConstraints);
		// Energy
		gridBagConstraints.gridx = 7;
		gridBagConstraints.gridy = 0;
		_lT_ENERGY = new JLabel(Messages.getString("T_ENERGY"),JLabel.CENTER); //$NON-NLS-1$
		add(_lT_ENERGY, gridBagConstraints);
		_lEnergy = new JLabel(_selOrganism!=null?_nf.format(_selOrganism.getEnergy()):"0", JLabel.CENTER); //$NON-NLS-1$
		_lEnergy.setPreferredSize(dimension);
		gridBagConstraints.gridx = 8;
		gridBagConstraints.gridy = 0;
		add(_lEnergy, gridBagConstraints);
		// Number of sons
		gridBagConstraints.gridx = 1;
		gridBagConstraints.gridy = 1;
		_lT_CHILDREN = new JLabel(Messages.getString("T_CHILDREN"),JLabel.CENTER); //$NON-NLS-1$
		add(_lT_CHILDREN, gridBagConstraints);
		_lChildren = new JLabel(_selOrganism!=null?_nf.format(_selOrganism.getTotalChildren()):"0",JLabel.CENTER); //$NON-NLS-1$
		_lChildren.setPreferredSize(dimension);
		gridBagConstraints.gridx = 2;
		gridBagConstraints.gridy = 1;
		add(_lChildren, gridBagConstraints);
		// Number of killed organisms
		gridBagConstraints.gridx = 3;
		gridBagConstraints.gridy = 1;
		_lT_VICTIMS =new JLabel(Messages.getString("T_VICTIMS"), JLabel.CENTER); //$NON-NLS-1$
		add(_lT_VICTIMS, gridBagConstraints);
		_lKills = new JLabel(_selOrganism!=null?_nf.format(_selOrganism.getTotalKills()):"0", JLabel.CENTER); //$NON-NLS-1$
		_lKills.setPreferredSize(dimension);
		gridBagConstraints.gridx = 4;
		gridBagConstraints.gridy = 1;
		add(_lKills, gridBagConstraints);
		// Number of infected organisms
		gridBagConstraints.gridx = 5;
		gridBagConstraints.gridy = 1;
		_lT_INFECTED = new JLabel(Messages.getString("T_INFECTED"), JLabel.CENTER); //$NON-NLS-1$
		add(_lT_INFECTED, gridBagConstraints);
		_lInfected = new JLabel(_selOrganism!=null?_nf.format(_selOrganism.getTotalInfected()):"0", JLabel.CENTER); //$NON-NLS-1$
		_lInfected.setPreferredSize(dimension);
		gridBagConstraints.gridx = 6;
		gridBagConstraints.gridy = 1;
		add(_lInfected, gridBagConstraints);
		// Total mass
		gridBagConstraints.gridx = 7;
		gridBagConstraints.gridy = 1;
		_lT_MASS = new JLabel(Messages.getString("T_MASS"),JLabel.CENTER); //$NON-NLS-1$
		add(_lT_MASS, gridBagConstraints);
		_lMass = new JLabel(_selOrganism!=null?_nf.format(_selOrganism.getMass()):"0",JLabel.CENTER); //$NON-NLS-1$
		_lMass.setPreferredSize(dimension);
		gridBagConstraints.gridx = 8;
		gridBagConstraints.gridy = 1;
		add(_lMass, gridBagConstraints);
		// Button to view genes
		_buttonGenes = new JButton(Messages.getString("T_EXAMINE_GENES")); //$NON-NLS-1$
		_buttonGenes.addActionListener(new ActionListener() {
            public void actionPerformed(ActionEvent evt) {
            	new LabWindow(_mainWindow, _selOrganism.getGeneticCode());
            }
		});
		_buttonGenes.setEnabled(_selOrganism != null);
		gridBagConstraints.gridx = 1;
		gridBagConstraints.gridy = 2;
		gridBagConstraints.gridwidth = 2;
		add(_buttonGenes, gridBagConstraints);
		// Reproduce energy
		gridBagConstraints.gridx = 3;
		gridBagConstraints.gridy = 2;
		gridBagConstraints.gridwidth = 1;
		_lT_REPRODUCTION = new JLabel(Messages.getString("T_REPRODUCTION"), JLabel.CENTER); //$NON-NLS-1$
		add(_lT_REPRODUCTION, gridBagConstraints);
		_lReproduceEnergy = new JLabel(_selOrganism!=null?_nf.format(_selOrganism.getGeneticCode().getReproduceEnergy()):"0",JLabel.CENTER);
		_lReproduceEnergy.setPreferredSize(dimension);
		gridBagConstraints.gridx = 4;
		gridBagConstraints.gridy = 2;
		add(_lReproduceEnergy, gridBagConstraints);
		
		setSize(200,200);
		setVisible(_selOrganism != null);
	}
	
	public void changeLocale() {
		_lT_ID.setText(Messages.getString("T_ID")); //$NON-NLS-1$
		_lT_GENERATION.setText(Messages.getString("T_GENERATION")); //$NON-NLS-1$
		_lT_AGE.setText(Messages.getString("T_AGE"));  //$NON-NLS-1$
		_lT_ENERGY.setText(Messages.getString("T_ENERGY"));  //$NON-NLS-1$
		_lT_CHILDREN.setText(Messages.getString("T_CHILDREN"));  //$NON-NLS-1$
		_lT_VICTIMS.setText(Messages.getString("T_VICTIMS"));  //$NON-NLS-1$
		_lT_INFECTED.setText(Messages.getString("T_INFECTED"));  //$NON-NLS-1$
		_lT_MASS.setText(Messages.getString("T_MASS"));  //$NON-NLS-1$
		_buttonGenes.setText(Messages.getString("T_EXAMINE_GENES")); //$NON-NLS-1$
		_lT_REPRODUCTION.setText(Messages.getString("T_REPRODUCTION"));  //$NON-NLS-1$
	}
}
