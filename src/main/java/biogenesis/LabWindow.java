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
import javax.swing.event.ChangeEvent;
import javax.swing.event.ChangeListener;

import org.xml.sax.SAXException;

import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.ItemEvent;
import java.awt.event.ItemListener;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;

public class LabWindow extends JDialog implements ActionListener, ChangeListener {
	private static final long serialVersionUID = Utils.FILE_VERSION;
	protected MainWindow mainWindow;
	protected List<Gene> genesList;
	protected JButton cancelButton;
	protected JButton okButton;
	protected JPanel genesPanel;
	protected JPanel drawPanel;
	protected JScrollPane genesScroll;
	protected JLabel energyLabel;
	protected JLabel segmentsLabel;
	protected JComboBox symmetryCombo;
	protected JComboBox mirrorCombo;
	protected JComboBox disperseCombo;
	
	protected int symmetry=2;
	protected int energy=40;
	protected int mirror=0;
	protected boolean disperseChildren = false;
	
	public LabWindow(MainWindow v, GeneticCode gc) {
		super(v, Messages.getString("T_GENETIC_LABORATORY")); //$NON-NLS-1$
		init(v, gc);
	}
	
	public LabWindow(MainWindow v) {
		super(v, Messages.getString("T_GENETIC_LABORATORY")); //$NON-NLS-1$
		init(v, v.getVisibleWorld().clippedGeneticCode);
	}
	
	private void init(MainWindow v, GeneticCode gc) {
		mainWindow = v;
		genesList = new ArrayList<Gene>();
		if (gc != null)
			importGeneticCode(gc);
		setDefaultCloseOperation(DISPOSE_ON_CLOSE);	
		setComponents();
		pack();
		//setResizable(false);
		cancelButton.addActionListener(new ActionListener() {
            public void actionPerformed(ActionEvent evt) {
            	dispose();
            }
            });
		okButton.addActionListener(new ActionListener() {
            public void actionPerformed(ActionEvent evt) {
            	if (genesList.size() > 0)
            		mainWindow.getVisibleWorld().setClippedGeneticCode(new GeneticCode(genesList,symmetry, mirror, disperseChildren));
            	dispose();
            }
            });
		setVisible(true);
	}
	
	/* Initialize the variables of this dialog using a previously existing genetic code */
	private void importGeneticCode(GeneticCode g) {
		energy = g.getReproduceEnergy();
		disperseChildren = g.getDisperseChildren();
		mirror = g.getMirror();
		symmetry = g.getSymmetry();
		for (int i=0; i<g.getNGenes(); i++)
			genesList.add((Gene)g.getGene(i).clone());
	}
	
	/* Initialize all the components of the dialog */
	protected void setComponents() {
		getContentPane().setLayout(new BorderLayout());
		JPanel generalPanel = new JPanel();
		generalPanel.setLayout(new GridBagLayout());
		GridBagConstraints gridBagConstraints = new GridBagConstraints();
		gridBagConstraints.gridx = 0;
		gridBagConstraints.gridy = 0;
		gridBagConstraints.anchor = GridBagConstraints.WEST;
		gridBagConstraints.weightx = 1.0;
		generalPanel.add(new JLabel(Messages.getString("T_SEGMENTS"),SwingConstants.CENTER), gridBagConstraints); //$NON-NLS-1$
		gridBagConstraints.gridx = 1;
		gridBagConstraints.gridy = 0;
		segmentsLabel = new JLabel(Integer.toString(genesList.size() * symmetry));
		generalPanel.add(segmentsLabel, gridBagConstraints);
		gridBagConstraints.gridx = 2;
		gridBagConstraints.gridy = 0;
		generalPanel.add(new JLabel(Messages.getString("T_ENERGY_TO_REPRODUCE"),SwingConstants.CENTER), gridBagConstraints); //$NON-NLS-1$
		gridBagConstraints.gridx = 3;
		gridBagConstraints.gridy = 0;
		energyLabel = new JLabel(Integer.toString(energy));
		generalPanel.add(energyLabel, gridBagConstraints);
		gridBagConstraints.gridx = 4;
		gridBagConstraints.gridy = 0;
		generalPanel.add(new JLabel(Messages.getString("T_LIFE_EXPECTANCY"),SwingConstants.CENTER), gridBagConstraints); //$NON-NLS-1$
		gridBagConstraints.gridx = 5;
		gridBagConstraints.gridy = 0;
		JLabel life = new JLabel(Integer.toString(Utils.MAX_AGE));
		generalPanel.add(life, gridBagConstraints);
		gridBagConstraints.gridx = 0;
		gridBagConstraints.gridy = 1;
		generalPanel.add(new JLabel(Messages.getString("T_SYMMETRY"),SwingConstants.CENTER), gridBagConstraints); //$NON-NLS-1$
		gridBagConstraints.gridx = 1;
		gridBagConstraints.gridy = 1;
		String[] symmetryValues = {"1","2","3","4","5","6","7","8"};  //$NON-NLS-1$//$NON-NLS-2$ //$NON-NLS-3$
		symmetryCombo = new JComboBox(symmetryValues);
		symmetryCombo.setSelectedItem(Integer.toString(symmetry));
		symmetryCombo.addItemListener(new ItemListener() {
			public void itemStateChanged(ItemEvent evt) {
				if (evt.getStateChange() == ItemEvent.SELECTED) {
					symmetry = Integer.parseInt((String)symmetryCombo.getSelectedItem());
					energy = 40 + 3 * symmetry * genesList.size();
					energyLabel.setText(Integer.toString(energy));
					segmentsLabel.setText(Integer.toString(genesList.size() * symmetry));
					drawPanel.repaint();
				}
			}
		});
		generalPanel.add(symmetryCombo, gridBagConstraints);
		gridBagConstraints.gridx = 2;
		gridBagConstraints.gridy = 1;
		generalPanel.add(new JLabel(Messages.getString("T_MIRROR"),SwingConstants.CENTER), gridBagConstraints); //$NON-NLS-1$
		gridBagConstraints.gridx = 3;
		gridBagConstraints.gridy = 1;
		String[] noyesValues = {Messages.getString("T_NO"), Messages.getString("T_YES")}; //$NON-NLS-1$ //$NON-NLS-2$
		mirrorCombo = new JComboBox(noyesValues);
		mirrorCombo.setSelectedIndex(mirror);
		mirrorCombo.addItemListener(new ItemListener() {
			public void itemStateChanged(ItemEvent evt) {
				if (evt.getStateChange() == ItemEvent.SELECTED) {
					mirror = mirrorCombo.getSelectedIndex();
					drawPanel.repaint();
				}
			}
		});
		generalPanel.add(mirrorCombo, gridBagConstraints);
		gridBagConstraints.gridx = 4;
		gridBagConstraints.gridy = 1;
		generalPanel.add(new JLabel(Messages.getString("T_DISPERSE_CHILDREN"),SwingConstants.CENTER), gridBagConstraints); //$NON-NLS-1$
		gridBagConstraints.gridx = 5;
		gridBagConstraints.gridy = 1;
		disperseCombo = new JComboBox(noyesValues);
		disperseCombo.setSelectedIndex(disperseChildren==false?0:1);
		disperseCombo.addItemListener(new ItemListener() {
			public void itemStateChanged(ItemEvent evt) {
				if (evt.getStateChange() == ItemEvent.SELECTED)
					disperseChildren = disperseCombo.getSelectedIndex()==0? false: true; 
			}
		});
		generalPanel.add(disperseCombo, gridBagConstraints);
		
		getContentPane().add(generalPanel,BorderLayout.NORTH);
		genesPanel = new JPanel();
		genesScroll = new JScrollPane(genesPanel);
		genesScroll.setPreferredSize(new Dimension(440,300));
		refreshGenesPanel();
		getContentPane().add(genesScroll,BorderLayout.WEST);
		
		drawPanel = new JPanel() {
			private static final long serialVersionUID = 1L;
			@Override
			public void paintComponent (Graphics g) {
				super.paintComponent(g);
		        draw(g);
		    }
		};
		drawPanel.setPreferredSize(new Dimension(200,200));
		drawPanel.setBackground(Color.BLACK);
		getContentPane().add(drawPanel,BorderLayout.CENTER);
		
		JPanel buttonsPanel = new JPanel();
		okButton = new JButton(Messages.getString("T_COPY_TO_CLIPBOARD")); //$NON-NLS-1$

		buttonsPanel.add(okButton);
		cancelButton = new JButton(Messages.getString("T_CANCEL")); //$NON-NLS-1$
		buttonsPanel.add(cancelButton);
		JButton clearButton = new JButton(Messages.getString("T_CLEAR")); //$NON-NLS-1$
		clearButton.addActionListener(new ActionListener() {
            public void actionPerformed(ActionEvent evt) {
            	genesList.clear();
            	refreshGenesPanel();
            }
            });
		buttonsPanel.add(clearButton);
		JButton importButton = new JButton(Messages.getString("T_IMPORT")); //$NON_NLS-1$
		importButton.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				GeneticCode g;
	    		try {
	    			JFileChooser chooser = mainWindow.getGeneticCodeChooser();
	    			chooser.setFileFilter(new BioFileFilter(BioFileFilter.GENETIC_CODE_EXTENSION));
	    			int returnVal = chooser.showOpenDialog(null);
	    			if (returnVal == JFileChooser.APPROVE_OPTION) {
	    				try {
	    					// Read XML code from file
	    					BioXMLParser parser = new BioXMLParser();
							g = parser.parseGeneticCode(chooser.getSelectedFile());
							genesList.clear();
							importGeneticCode(g);
							refreshGenesPanel();
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
			}
		});
		buttonsPanel.add(importButton);
		getContentPane().add(buttonsPanel,BorderLayout.SOUTH);
		
		getRootPane().setDefaultButton(okButton);
	}
	
	public void actionPerformed(ActionEvent evt) {
		if (evt.getActionCommand().startsWith("c")) {
			int modifiedGene = Integer.parseInt(evt.getActionCommand().substring(1));
			ColorComboBox colorCombo = (ColorComboBox) evt.getSource();
			genesList.get(modifiedGene).setColor(colorCombo.getSelectedColor());
			drawPanel.repaint();
		}
		// Add a new gene after the last one
		if (evt.getActionCommand().equals("add")) { //$NON-NLS-1$
			Gene gene = new Gene(2.0,0.0,Color.GREEN);
			genesList.add(gene);
			refreshGenesPanel();
		}
		// Deletes an existing gene
		if (evt.getActionCommand().startsWith("d")) { //$NON-NLS-1$
			int deletedGene = Integer.parseInt(evt.getActionCommand().substring(1));
			genesList.remove(deletedGene);
			refreshGenesPanel();
		}
		// Insert a new gene before the selected position
		if (evt.getActionCommand().startsWith("i")) { //$NON-NLS-1$
			Gene gene = new Gene(2.0,0.0,Color.GREEN);
			int insertPosition = Integer.parseInt(evt.getActionCommand().substring(1));
			genesList.add(insertPosition, gene);
			refreshGenesPanel();
		}
	}
	
	protected void refreshGenesPanel() {
		genesPanel.removeAll();
		GridBagLayout gridbag = new GridBagLayout();
		GridBagConstraints constraints = new GridBagConstraints();
		genesPanel.setLayout(gridbag);
		Iterator<Gene> it;
		int i;
		Gene gene;
		constraints.gridx = 1;
		constraints.gridy = 0;
		genesPanel.add(new JLabel(Messages.getString("T_GENE")), constraints); //$NON-NLS-1$
		constraints.gridx = 2;
		genesPanel.add(new JLabel(Messages.getString("T_LENGTH")), constraints); //$NON-NLS-1$
		constraints.gridx = 3;
		genesPanel.add(new JLabel(Messages.getString("T_ROTATION")), constraints); //$NON-NLS-1$
		constraints.gridx = 4;
		genesPanel.add(new JLabel(Messages.getString("T_COLOR2")), constraints); //$NON-NLS-1$
		for (it = genesList.iterator(), i=0; it.hasNext(); i++) {
			gene = it.next();
			constraints.gridx = 1;
			constraints.gridy = i+1;
			JLabel label = new JLabel(i+": "); //$NON-NLS-1$
			gridbag.setConstraints(label,constraints);
			genesPanel.add(label);
			
			constraints.gridx = 2;
			LengthSpinner lengthSpinner = new LengthSpinner(gene);
			lengthSpinner.addChangeListener(this);
			genesPanel.add(lengthSpinner, constraints);
			
			constraints.gridx = 3;
			ThetaSpinner thetaSpinner = new ThetaSpinner(gene);
			thetaSpinner.addChangeListener(this);
			genesPanel.add(thetaSpinner, constraints);
			
			constraints.gridx = 4;
			ColorComboBox colorCombo = new ColorComboBox(gene.getColor());
			colorCombo.addActionListener(this);
			colorCombo.setActionCommand("c"+i);
			genesPanel.add(colorCombo, constraints);
			
			constraints.gridx = 5;
			JButton insertButton = new JButton(Messages.getString("T_INSERT")); //$NON-NLS-1$
			insertButton.setActionCommand("i"+i); //$NON-NLS-1$
			gridbag.setConstraints(insertButton,constraints);
			genesPanel.add(insertButton);
			insertButton.addActionListener(this);
			
			constraints.gridx = 6;
			JButton deleteButton = new JButton(Messages.getString("T_DELETE")); //$NON-NLS-1$
			deleteButton.setActionCommand("d"+i); //$NON-NLS-1$
			gridbag.setConstraints(deleteButton,constraints);
			genesPanel.add(deleteButton);
			deleteButton.addActionListener(this);
		}
		constraints.gridx = 1;
		constraints.gridwidth = 2;
		constraints.gridy = i+1;
		JButton addButton = new JButton(Messages.getString("T_ADD")); //$NON-NLS-1$
		addButton.setActionCommand("add"); //$NON-NLS-1$
		gridbag.setConstraints(addButton,constraints);
		genesPanel.add(addButton);
		addButton.addActionListener(this);
		
		energy = 40 + 3 * symmetry * genesList.size();
		energyLabel.setText(Integer.toString(energy));
		segmentsLabel.setText(Integer.toString(genesList.size() * symmetry));
		
		validate();
		repaint();
	}
	
	protected void draw(Graphics g) {
		GeneticCode code = new GeneticCode(genesList, symmetry, mirror, false);
		code.draw(g, drawPanel.getSize().width, drawPanel.getSize().height);
	}

	@Override
	public void stateChanged(ChangeEvent evt) {
		if (evt.getSource() instanceof LengthSpinner) {
			LengthSpinner spinner = (LengthSpinner) evt.getSource();
			spinner.getGene().setLength(spinner.getLength());
		}
		if (evt.getSource() instanceof ThetaSpinner) {
			ThetaSpinner spinner = (ThetaSpinner) evt.getSource();
			spinner.getGene().setTheta(spinner.getTheta());
		}
		drawPanel.repaint();
	}
}

class LengthSpinner extends JSpinner {
	private static final long serialVersionUID = Utils.VERSION;
	private Gene _gene;

	public LengthSpinner(Gene gene) {
		super();
		_gene = gene;
		setModel(new SpinnerNumberModel(_gene.getLength(), 2.0, 18.0, 0.1));
		setEditor(new JSpinner.NumberEditor(this, "#0.0"));
	}

	public Gene getGene() {
		return _gene;
	}
	
	public void setGene(Gene gene) {
		_gene = gene;
	}
	
	public double getLength() {
		return ((SpinnerNumberModel)getModel()).getNumber().doubleValue();
	}
}

class ThetaSpinnerModel extends SpinnerNumberModel {
	private static final long serialVersionUID = Utils.VERSION;
	int firstValue, lastValue;

	public ThetaSpinnerModel(int value, int minimum, int maximum, int stepSize) {
		super(value, minimum, maximum, stepSize);
		firstValue = minimum;
		lastValue = maximum;
	}

	@Override
	public Object getNextValue() {
		Object value = super.getNextValue();
		if (value == null)
			value = firstValue;
		return value;
	}

	@Override
	public Object getPreviousValue() {
		Object value = super.getPreviousValue();
		if (value == null)
			value = lastValue;
		return value;
	}
}

class ThetaSpinner extends JSpinner {
	private static final long serialVersionUID = Utils.VERSION;
	private Gene _gene;

	public ThetaSpinner(Gene gene) {
		super();
		_gene = gene;
		int initialValue = (int)Math.round(_gene.getTheta()*180.0/Math.PI);
		while (initialValue < 0) initialValue+=360;
		while (initialValue > 359) initialValue-=360;
		setModel(new ThetaSpinnerModel(initialValue, 0, 359, 1));
		setEditor(new JSpinner.NumberEditor(this, "##0"));
	}

	public Gene getGene() {
		return _gene;
	}
	
	public void setGene(Gene gene) {
		_gene = gene;
	}
	
	public double getTheta() {
		return ((SpinnerNumberModel)getModel()).getNumber().doubleValue()*Math.PI/180.0;
	}
}
