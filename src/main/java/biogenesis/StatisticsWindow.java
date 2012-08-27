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

import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.text.NumberFormat;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Iterator;
import java.util.List;

import javax.swing.*;
import javax.swing.border.*;

public class StatisticsWindow extends JDialog implements ActionListener {
	private static final long serialVersionUID = Utils.FILE_VERSION;
	
	private JButton updateButton;
	private JButton closeButton;
	
	private World world;
	private VisibleWorld visibleWorld;
	private WorldStatistics worldStatistics;
	private List<Organism> organisms;
	
	public StatisticsWindow(MainWindow w, WorldStatistics ws, List<Organism> os) {
		super(w);
		world = w.getWorld();
		visibleWorld = w.getVisibleWorld();
		worldStatistics = ws;
		organisms = os;
		setDefaultCloseOperation(DISPOSE_ON_CLOSE);	
		setTitle(Messages.getString("T_STATISTICS")); //$NON-NLS-1$
		setComponents();
		pack();
		setResizable(false);
		setVisible(true);
	}
	
	private void setComponents() {
		// Prepare number format
		NumberFormat nf = NumberFormat.getInstance();
		nf.setMaximumFractionDigits(1);
		
		// Population graphic
		GraphPanel populationGraphPanel = new GraphPanel(100, 80);
		populationGraphPanel.addGraph(worldStatistics.getDeathList(), Math.max(worldStatistics.getAveragePopulation(), worldStatistics.getMaxDeaths()),
				0, Color.RED, Messages.getString("T_DEATHS")); //$NON-NLS-1$
		populationGraphPanel.addGraph(worldStatistics.getBirthList(), Math.max(worldStatistics.getAveragePopulation(), worldStatistics.getMaxBirth()),
				0, Color.GREEN, Messages.getString("T_BIRTHS")); //$NON-NLS-1$
		populationGraphPanel.addGraph(worldStatistics.getPopulationList(), worldStatistics.getMaxPopulation(),
				0, Color.WHITE, Messages.getString("T_POPULATION")); //$NON-NLS-1$
		populationGraphPanel.updateLegend();
		
		
		// Population statistics
		JPanel popStatsPanel = new JPanel();
		popStatsPanel.setLayout(new BoxLayout(popStatsPanel,BoxLayout.Y_AXIS));
		popStatsPanel.add(new JLabel(Messages.getString("T_AVERAGE_POPULATION")+ //$NON-NLS-1$
				nf.format(worldStatistics.getAveragePopulation())));
		popStatsPanel.add(new JLabel(Messages.getString("T_AVERAGE_BIRTH_RATE")+ //$NON-NLS-1$
				nf.format(worldStatistics.getAverageBirths())));
		popStatsPanel.add(new JLabel(Messages.getString("T_AVERAGE_MORTALITY_RATE")+ //$NON-NLS-1$
				nf.format(worldStatistics.getAverageDeaths())));
		popStatsPanel.add(new JLabel(Messages.getString("T_AVERAGE_INFECTIONS_RATE")+ //$NON-NLS-1$
				nf.format(worldStatistics.getAverageInfections())));
		popStatsPanel.add(new JLabel(Messages.getString("T_GENERATED_ORGANISMS")+ //$NON-NLS-1$
				nf.format(worldStatistics.getCreatedOrganisms())));
		popStatsPanel.add(new JLabel(Messages.getString("T_MAXIMUM_POPULATION")+ //$NON-NLS-1$
				nf.format(worldStatistics.getMaxPopulation())));
		popStatsPanel.add(new JLabel(Messages.getString("T_AT_TIME")+ //$NON-NLS-1$
				nf.format(worldStatistics.getMaxPopulationTime())));
		popStatsPanel.add(new JLabel(Messages.getString("T_MINIMUM_POPULATION")+ //$NON-NLS-1$
				nf.format(worldStatistics.getMinPopulation())));
		popStatsPanel.add(new JLabel(Messages.getString("T_AT_TIME")+ //$NON-NLS-1$
				nf.format(worldStatistics.getMinPopulationTime())));
		popStatsPanel.add(new JLabel(Messages.getString("T_MASS_EXTINTIONS")+ //$NON-NLS-1$
				nf.format(worldStatistics.getMassExtintions())));

		// Population = population graph + population stats
		JPanel populationPanel = new JPanel();
		populationPanel.setLayout(new BorderLayout());
		populationPanel.add(populationGraphPanel, BorderLayout.CENTER);
		populationPanel.add(popStatsPanel, BorderLayout.SOUTH);
		Border title = BorderFactory.createTitledBorder(BorderFactory.createBevelBorder(BevelBorder.RAISED),
				Messages.getString("T_POPULATION"), TitledBorder.LEFT, TitledBorder.TOP); //$NON-NLS-1$
		populationPanel.setBorder(title);
		
		// Atmosphere graphic
		GraphPanel atmosphereGraphPanel = new GraphPanel(100,80);
		atmosphereGraphPanel.addGraph(worldStatistics.getOxygenList(), worldStatistics.getMaxOxygen(),
				worldStatistics.getMinOxygen(), Color.BLUE, Messages.getString("T_OXYGEN")); //$NON-NLS-1$
		atmosphereGraphPanel.addGraph(worldStatistics.getCarbonDioxideList(), worldStatistics.getMaxCarbonDioxide(),
				worldStatistics.getMinCarbonDioxide(), Color.WHITE, Messages.getString("T_CARBON_DIOXIDE")); //$NON-NLS-1$
		atmosphereGraphPanel.updateLegend();
		
		//Atmosphere statistics
		JPanel atmosphereStatsPanel = new JPanel();
		atmosphereStatsPanel.setLayout(new BoxLayout(atmosphereStatsPanel,BoxLayout.Y_AXIS));
		atmosphereStatsPanel.add(new JLabel(Messages.getString("T_MAXIMUM_CARBON_DIOXIDE")+nf.format(worldStatistics.getMaxCarbonDioxide()))); //$NON-NLS-1$
		atmosphereStatsPanel.add(new JLabel(Messages.getString("T_AT_TIME")+nf.format(worldStatistics.getMaxCarbonDioxideTime()))); //$NON-NLS-1$
		atmosphereStatsPanel.add(new JLabel(Messages.getString("T_MINIMUM_CARBON_DIOXIDE")+nf.format(worldStatistics.getMinCarbonDioxide()))); //$NON-NLS-1$
		atmosphereStatsPanel.add(new JLabel(Messages.getString("T_AT_TIME")+nf.format(worldStatistics.getMinCarbonDioxideTime()))); //$NON-NLS-1$
		atmosphereStatsPanel.add(new JLabel(Messages.getString("T_MAXIMUM_OXYGEN")+nf.format(worldStatistics.getMaxOxygen()))); //$NON-NLS-1$
		atmosphereStatsPanel.add(new JLabel(Messages.getString("T_AT_TIME")+nf.format(worldStatistics.getMaxOxygenTime()))); //$NON-NLS-1$
		atmosphereStatsPanel.add(new JLabel(Messages.getString("T_MINIMUM_OXYGEN")+nf.format(worldStatistics.getMinOxygenTime()))); //$NON-NLS-1$
		atmosphereStatsPanel.add(new JLabel(Messages.getString("T_AT_TIME")+nf.format(worldStatistics.getMinOxygenTime()))); //$NON-NLS-1$
		
//		 Population = population graph + population stats
		JPanel atmospherePanel = new JPanel();
		atmospherePanel.setLayout(new BorderLayout());
		atmospherePanel.add(atmosphereGraphPanel, BorderLayout.CENTER);
		atmospherePanel.add(atmosphereStatsPanel, BorderLayout.SOUTH);
		title = BorderFactory.createTitledBorder(BorderFactory.createBevelBorder(BevelBorder.RAISED),
				Messages.getString("T_ATMOSPHERE"), TitledBorder.LEFT, TitledBorder.TOP); //$NON-NLS-1$
		atmospherePanel.setBorder(title);
		
		// World history: population + atmosphere
		JPanel worldHistoryPanel = new JPanel();
		worldHistoryPanel.setLayout(new BoxLayout(worldHistoryPanel,BoxLayout.Y_AXIS));
		worldHistoryPanel.add(populationPanel);
		worldHistoryPanel.add(atmospherePanel);
		title = BorderFactory.createTitledBorder(BorderFactory.createEtchedBorder(EtchedBorder.LOWERED),
				Messages.getString("T_WORLD_HISTORY"), TitledBorder.LEFT, TitledBorder.TOP); //$NON-NLS-1$
		worldHistoryPanel.setBorder(title);
		
		// Current state
		GridBagConstraints gbc = new GridBagConstraints();
		JPanel currentStatePanel = new JPanel();
		currentStatePanel.setLayout(new GridBagLayout());
		gbc.gridx = 1;
		gbc.gridy = 1;
		gbc.anchor = GridBagConstraints.CENTER;
		currentStatePanel.add(new JLabel(Messages.getString("T_TIME")+world.getTime()), gbc); //$NON-NLS-1$
		gbc.gridx = 2;
		currentStatePanel.add(new JLabel(Messages.getString("T_OXYGEN2")+nf.format(world.getO2())), gbc); //$NON-NLS-1$
		gbc.gridx = 1;
		gbc.gridy = 2;
		currentStatePanel.add(new JLabel(Messages.getString("T_POPULATION2")+world.getPopulation()), gbc); //$NON-NLS-1$
		gbc.gridx = 2;
		currentStatePanel.add(new JLabel(Messages.getString("T_CARBON_DIOXIDE2")+nf.format(world.getCO2())), gbc); //$NON-NLS-1$
		gbc.gridx = 1;
		gbc.gridy = 3;
		currentStatePanel.add(new JLabel(Messages.getString("T_REMAINS_OF_BEINGS")+world.getNCorpses()), gbc); //$NON-NLS-1$
		gbc.gridx = 1;
		gbc.gridy = 4;
		gbc.anchor = GridBagConstraints.WEST;
		currentStatePanel.add(new JLabel(Messages.getString("T_COLOR_PROPORTION")), gbc); //$NON-NLS-1$
		ColorPanel colorPanel = createColorPanel();
		gbc.gridx = 2;
		currentStatePanel.add(colorPanel, gbc);
		title = BorderFactory.createTitledBorder(BorderFactory.createEtchedBorder(EtchedBorder.LOWERED),
				Messages.getString("T_CURRENT_STATE"), TitledBorder.LEFT, TitledBorder.TOP); //$NON-NLS-1$
		currentStatePanel.setBorder(title);

		// Notable beings
		worldStatistics.findBestAliveBeings(organisms);
		JPanel notableBeingsPanel = new JPanel();
		notableBeingsPanel.setLayout(new GridBagLayout());
		gbc.gridx = 1;
		gbc.gridy = 1;
		notableBeingsPanel.add(new JLabel(Messages.getString("T_ALIVE_BEING_HAVING_THE_MOST_CHILDREN")), gbc); //$NON-NLS-1$
		gbc.gridx = 2;
		notableBeingsPanel.add(new JLabel(Messages.getString("T_BEING_HAVING_THE_MOST_CHILDREN")), gbc); //$NON-NLS-1$
		gbc.gridx = 1;
		gbc.gridy = 2;
		GeneticCodePanel aliveMostChildrenPanel = new GeneticCodePanel(worldStatistics.getAliveBeingMostChildren(),
				visibleWorld);
		notableBeingsPanel.add(aliveMostChildrenPanel, gbc);
		gbc.gridx = 2;
		GeneticCodePanel mostChildrenPanel = new GeneticCodePanel(worldStatistics.getBeingMostChildren(),
				visibleWorld); 
		notableBeingsPanel.add(mostChildrenPanel, gbc);
		gbc.gridx = 1;
		gbc.gridy = 3;
		notableBeingsPanel.add(new JLabel(Messages.getString("T_NUMBER_OF_CHILDREN")+worldStatistics.getAliveBeingMostChildrenNumber()),gbc); //$NON-NLS-1$
		gbc.gridx = 2;
		notableBeingsPanel.add(new JLabel(Messages.getString("T_NUMBER_OF_CHILDREN")+worldStatistics.getBeingMostChildrenNumber()),gbc); //$NON-NLS-1$
		gbc.gridy = 4;
		notableBeingsPanel.add(new JLabel(Messages.getString("T_TIME")+worldStatistics.getBeingMostChildrenTime()),gbc); //$NON-NLS-1$
		gbc.gridx = 1;
		gbc.gridy = 5;
		notableBeingsPanel.add(new JLabel(Messages.getString("T_ALIVE_BEING_HAVING_THE_MOST_VICTIMS")), gbc); //$NON-NLS-1$
		gbc.gridx = 2;
		notableBeingsPanel.add(new JLabel(Messages.getString("T_BEING_HAVING_THE_MOST_VICTIMS")), gbc); //$NON-NLS-1$
		gbc.gridx = 1;
		gbc.gridy = 6;
		GeneticCodePanel aliveMostKillsPanel = new GeneticCodePanel(worldStatistics.getAliveBeingMostKills(),
				visibleWorld);
		notableBeingsPanel.add(aliveMostKillsPanel, gbc);
		gbc.gridx = 2;
		GeneticCodePanel mostKillsPanel = new GeneticCodePanel(worldStatistics.getBeingMostKills(),
				visibleWorld); 
		notableBeingsPanel.add(mostKillsPanel, gbc);
		gbc.gridx = 1;
		gbc.gridy = 7;
		notableBeingsPanel.add(new JLabel(Messages.getString("T_NUMBER_OF_VICTIMS")+worldStatistics.getAliveBeingMostKillsNumber()),gbc);		 //$NON-NLS-1$
		gbc.gridx = 2;
		notableBeingsPanel.add(new JLabel(Messages.getString("T_NUMBER_OF_VICTIMS")+worldStatistics.getBeingMostKillsNumber()),gbc); //$NON-NLS-1$
		gbc.gridy = 8;
		notableBeingsPanel.add(new JLabel(Messages.getString("T_TIME")+worldStatistics.getBeingMostKillsTime()),gbc);		 //$NON-NLS-1$
		gbc.gridx = 1;
		gbc.gridy = 9;
		notableBeingsPanel.add(new JLabel(Messages.getString("T_ALIVE_BEING_HAVING_THE_MOST_INFECTED")), gbc); //$NON-NLS-1$
		gbc.gridx = 2;
		notableBeingsPanel.add(new JLabel(Messages.getString("T_BEING_HAVING_THE_MOST_INFECTED")), gbc); //$NON-NLS-1$
		gbc.gridx = 1;
		gbc.gridy = 10;
		GeneticCodePanel aliveMostInfectionsPanel = new GeneticCodePanel(worldStatistics.getAliveBeingMostInfections(),
				visibleWorld);
		notableBeingsPanel.add(aliveMostInfectionsPanel, gbc);
		gbc.gridx = 2;
		GeneticCodePanel mostInfectionsPanel = new GeneticCodePanel(worldStatistics.getBeingMostInfections(),
				visibleWorld); 
		notableBeingsPanel.add(mostInfectionsPanel, gbc);
		gbc.gridx = 1;
		gbc.gridy = 11;
		notableBeingsPanel.add(new JLabel(Messages.getString("T_NUMBER_OF_INFECTED")+worldStatistics.getAliveBeingMostInfectionsNumber()),gbc);		 //$NON-NLS-1$
		gbc.gridx = 2;
		notableBeingsPanel.add(new JLabel(Messages.getString("T_NUMBER_OF_INFECTED")+worldStatistics.getBeingMostInfectionsNumber()),gbc); //$NON-NLS-1$
		gbc.gridy = 12;
		notableBeingsPanel.add(new JLabel(Messages.getString("T_TIME")+worldStatistics.getBeingMostInfectionsTime()),gbc); //$NON-NLS-1$
		title = BorderFactory.createTitledBorder(BorderFactory.createEtchedBorder(EtchedBorder.LOWERED),
				Messages.getString("T_REMARKABLE_ORGANISMS"), TitledBorder.LEFT, TitledBorder.TOP); //$NON-NLS-1$
		notableBeingsPanel.setBorder(title);
		
		
		// Buttons
		JPanel buttonsPanel = new JPanel();
		updateButton = new JButton(Messages.getString("T_UPDATE")); //$NON-NLS-1$
		closeButton = new JButton(Messages.getString("T_CLOSE")); //$NON-NLS-1$
		buttonsPanel.add(updateButton);
		buttonsPanel.add(closeButton);
		updateButton.addActionListener(this);
		closeButton.addActionListener(this);
		
		// Add all components to the content pane
		JPanel leftPanel = new JPanel();
		leftPanel.setLayout(new BoxLayout(leftPanel, BoxLayout.Y_AXIS));
		leftPanel.add(currentStatePanel);
		leftPanel.add(notableBeingsPanel);
		JPanel rightPanel = new JPanel();
		rightPanel.setLayout(new BoxLayout(rightPanel, BoxLayout.Y_AXIS));
		rightPanel.add(worldHistoryPanel);
		rightPanel.add(buttonsPanel);
		
		getContentPane().setLayout(new BorderLayout());
		getContentPane().add(leftPanel, BorderLayout.WEST);
		getContentPane().add(rightPanel, BorderLayout.EAST);
	}
	
	private ColorPanel createColorPanel() {
		ColorPanel colorPanel = new ColorPanel();
		colorPanel.setPreferredSize(new Dimension(100,20));
		GeneticCode gc;
		InfoAndColor[] colorCounter = new InfoAndColor[7];
		colorCounter[0] = new InfoAndColor(0, Color.GREEN);
		colorCounter[1] = new InfoAndColor(0, Color.RED);
		colorCounter[2] = new InfoAndColor(0, Color.BLUE);
		colorCounter[3] = new InfoAndColor(0, Color.CYAN);
		colorCounter[4] = new InfoAndColor(0, Color.WHITE);
		colorCounter[5] = new InfoAndColor(0, Color.GRAY);
		colorCounter[6] = new InfoAndColor(0, Color.YELLOW);
		
		int i,j;
		Color c;
		synchronized(organisms) {
			for (Iterator<Organism> it = organisms.iterator(); it.hasNext();) {
				gc = it.next().getGeneticCode();
				for (i=0; i<gc.getNGenes(); i++) {
					c = gc.getGene(i).getColor();
					for (j=0; j<7; j++) {
						if (c.equals(colorCounter[j].color))
							colorCounter[j].info++;
					}
				}
			}
		}
		Arrays.sort(colorCounter);
		for (j=6; j>=0; j--)
			colorPanel.addColor(colorCounter[j].info, colorCounter[j].color);
		
		return colorPanel;
	}

	public void actionPerformed(ActionEvent e) {
		if (e.getSource() == updateButton) {
			getContentPane().removeAll();
			setComponents();
			pack();
			invalidate();
		}
		if (e.getSource() == closeButton) {
			dispose();
		}
	}
}

class ColorPanel extends JPanel {
	private static final long serialVersionUID = Utils.FILE_VERSION;
	
	private List<InfoAndColor> infoList = new ArrayList<InfoAndColor>();
	private int total=0;
	
	public void addColor(int info, Color color) {
		infoList.add(new InfoAndColor(info, color));
		total += info;
	}
	
	@Override
	public void paintComponent(Graphics g) {
		super.paintComponent(g);
		int width = getSize().width;
		int height = getSize().height;
		int x, lastX=0;
		InfoAndColor infoAndColor;
		for (Iterator<InfoAndColor> it = infoList.iterator(); it.hasNext();) {
			infoAndColor = it.next();
			x = width * infoAndColor.info / total;
			g.setColor(infoAndColor.color);
			g.fillRect(lastX, 0, x, height);
			lastX += x;
		}
	}
}

class InfoAndColor implements Comparable<InfoAndColor> {
	public int info;
	public Color color;
	
	public InfoAndColor(int i, Color c) {
		info = i;
		color = c;
	}

	public int compareTo(InfoAndColor o) {
		if (info < o.info) return -1;
		if (info > o.info) return 1;
		return 0;
	}
}

class GraphPanel extends JPanel {
	private static final long serialVersionUID = Utils.FILE_VERSION;
	
	private List<GraphInfo> graphList = new ArrayList<GraphInfo>();
	private int width;
	private int height;
	private JPanel centralPanel;
	
	public void addGraph(List<Double> info, double max, double min, Color color, String name) {
		graphList.add(new GraphInfo(info, max, min, width, height, color, name));
	}
	
	public void clear() {
		graphList.clear();
	}
	
	public void updateLegend() {
		JPanel legendPanel = new JPanel();
		legendPanel.setBackground(Color.BLACK);
		legendPanel.setLayout(new GridLayout(graphList.size(),1));
		JLabel label;
		GraphInfo graph;
		for (Iterator<GraphInfo> it = graphList.iterator(); it.hasNext();) {
			graph = it.next();
			label = new JLabel(graph.name);
			label.setForeground(graph.color);
			legendPanel.add(label);
		}
		add(legendPanel, BorderLayout.EAST);
	}
	
	public GraphPanel(int w, int h) {
		setBackground(Color.BLACK);
		width = w;
		height = h;
		setLayout(new BorderLayout());
		centralPanel = new JPanel();
		centralPanel.setPreferredSize(new Dimension(width, height));
		centralPanel.setBackground(Color.BLACK);
		centralPanel.setOpaque(false);
		add(centralPanel, BorderLayout.CENTER);
		
		JPanel southPanel = new JPanel();
		southPanel.setLayout(new GridLayout(1,2));
		southPanel.setPreferredSize(new Dimension(width, 20));
		southPanel.add(new JLabel("0",SwingConstants.LEFT)); //$NON-NLS-1$
		southPanel.add(new JLabel("100",SwingConstants.RIGHT)); //$NON-NLS-1$
		add(southPanel, BorderLayout.SOUTH);
	}
	
	@Override
	public void paintComponent(Graphics g) {
		super.paintComponent(g);
		GraphInfo graph;
		for (Iterator<GraphInfo> it = graphList.iterator(); it.hasNext();) {
			graph = it.next();
			graph.draw(g);
		}
	}
}

class GraphInfo {
	public Color color;
	public String name;
	public List<Double> info;
	public double max;
	public double min;
	
	private int[] xPoints;
	private int[] yPoints;
	private int nPoints;
	
	public void draw(Graphics g) {
		g.setColor(color);
		g.drawPolyline(xPoints, yPoints, nPoints);
	}
	
	public GraphInfo(List<Double> datum, double maxValue, double minValue, int width, int height, Color graphColor, String graphName) {
		info = datum;
		max = maxValue;
		min = minValue;
		color = graphColor;
		name = graphName;
		int x = 0;
		double y;
		nPoints = Math.min(info.size(), width);
		xPoints = new int[nPoints];
		yPoints = new int[nPoints];
		for (Iterator<Double> it = info.iterator(); it.hasNext() && x < width; x++) {
			y = height-(it.next().doubleValue() - min)*height/(max-min);
			xPoints[x] = x;
			yPoints[x] = (int)y; 	
		}
		
	}
}
