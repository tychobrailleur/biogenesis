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

import java.io.Serializable;
import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;

public class WorldStatistics implements Serializable {
	private static final long serialVersionUID = Utils.FILE_VERSION;

	private long time;

	private int maxPopulation;

	private int maxBirths = 0;

	private int maxDeaths = 0;

	private long maxPopulationTime;

	private int minPopulation = Utils.INITIAL_ORGANISMS;

	private long minPopulationTime;

	// private int lastTimePopulation;
	private int massExtintions;

	private int massExtintionState;

	private static final int EXTINTION_NO = 0;

	private static final int EXTINTION_POSSIBLE = 1;

	private static final int EXTINTION_CONFIRMED = 2;

	private static final int EXTINTION_FINISHING = 3;

	private int createdOrganisms;

	private long populationSum;

	private long deathSum;

	private int deathLastTime;

	private long birthSum;

	private int birthLastTime;

	private long infectionsSum;

	private double maxOxygen = Utils.INITIAL_O2;

	private long maxOxygenTime;

	private double minOxygen = Utils.INITIAL_O2;

	private long minOxygenTime;

	private double maxCarbonDioxide = Utils.INITIAL_CO2;

	private long maxCarbonDioxideTime;

	private double minCarbonDioxide = Utils.INITIAL_CO2;

	private long minCarbonDioxideTime;

	private GeneticCode aliveBeingMostChildren;

	private int aliveBeingMostChildrenNumber;

	private GeneticCode aliveBeingMostKills;

	private int aliveBeingMostKillsNumber;

	private GeneticCode aliveBeingMostInfections;

	private int aliveBeingMostInfectionsNumber;

	private GeneticCode beingMostChildren;

	private int beingMostChildrenNumber;

	private long beingMostChildrenTime;

	private GeneticCode beingMostKills;

	private int beingMostKillsNumber;

	private long beingMostKillsTime;

	private GeneticCode beingMostInfections;

	private int beingMostInfectionsNumber;

	private long beingMostInfectionsTime;

	private GeneticCode lastBornBeing;

	private GeneticCode lastDeadBeing;

	private GeneticCode lastInfectedBeing;

	private List<Double> populationList = new ArrayList<Double>(100);

	private List<Double> deathList = new ArrayList<Double>(100);

	private List<Double> birthList = new ArrayList<Double>(100);

	private List<Double> oxygenList = new ArrayList<Double>(100);

	private List<Double> carbonDioxideList = new ArrayList<Double>(100);

	public long getTime() {
		return time;
	}
	
	public int getMaxPopulation() {
		return maxPopulation;
	}

	public int getMaxBirth() {
		return maxBirths;
	}

	public int getMaxDeaths() {
		return maxDeaths;
	}

	public long getMaxPopulationTime() {
		return maxPopulationTime;
	}

	public int getMinPopulation() {
		return minPopulation;
	}

	public long getMinPopulationTime() {
		return minPopulationTime;
	}

	public int getMassExtintions() {
		return massExtintions;
	}

	public int getCreatedOrganisms() {
		return createdOrganisms;
	}

	public double getAveragePopulation() {
		if (time > 0)
			return populationSum / (double) time;
		return 0;
	}

	public double getAverageDeaths() {
		if (time > 0)
			return deathSum / (double) time;
		return 0;
	}

	public double getAverageBirths() {
		if (time > 0)
			return birthSum / (double) time;
		return 0;
	}

	public double getAverageInfections() {
		if (time > 0)
			return infectionsSum / (double) time;
		return 0;
	}

	public double getMaxOxygen() {
		return maxOxygen;
	}

	public long getMaxOxygenTime() {
		return maxOxygenTime;
	}

	public double getMinOxygen() {
		return minOxygen;
	}

	public long getMinOxygenTime() {
		return minOxygenTime;
	}

	public double getMaxCarbonDioxide() {
		return maxCarbonDioxide;
	}

	public long getMaxCarbonDioxideTime() {
		return maxCarbonDioxideTime;
	}

	public double getMinCarbonDioxide() {
		return minCarbonDioxide;
	}

	public long getMinCarbonDioxideTime() {
		return minCarbonDioxideTime;
	}

	public GeneticCode getAliveBeingMostChildren() {
		return aliveBeingMostChildren;
	}

	public int getAliveBeingMostChildrenNumber() {
		return aliveBeingMostChildrenNumber;
	}

	public GeneticCode getAliveBeingMostKills() {
		return aliveBeingMostKills;
	}

	public int getAliveBeingMostKillsNumber() {
		return aliveBeingMostKillsNumber;
	}

	public GeneticCode getAliveBeingMostInfections() {
		return aliveBeingMostInfections;
	}

	public int getAliveBeingMostInfectionsNumber() {
		return aliveBeingMostInfectionsNumber;
	}

	public GeneticCode getBeingMostChildren() {
		return beingMostChildren;
	}

	public int getBeingMostChildrenNumber() {
		return beingMostChildrenNumber;
	}

	public long getBeingMostChildrenTime() {
		return beingMostChildrenTime;
	}

	public GeneticCode getBeingMostKills() {
		return beingMostKills;
	}

	public int getBeingMostKillsNumber() {
		return beingMostKillsNumber;
	}

	public long getBeingMostKillsTime() {
		return beingMostKillsTime;
	}

	public GeneticCode getBeingMostInfections() {
		return beingMostInfections;
	}

	public int getBeingMostInfectionsNumber() {
		return beingMostInfectionsNumber;
	}

	public long getBeingMostInfectionsTime() {
		return beingMostInfectionsTime;
	}

	public GeneticCode getLastBornBeing() {
		return lastBornBeing;
	}

	public GeneticCode getLastDeadBeing() {
		return lastDeadBeing;
	}

	public GeneticCode getLastInfectedBeing() {
		return lastInfectedBeing;
	}

	public List<Double> getPopulationList() {
		return populationList;
	}

	public List<Double> getDeathList() {
		return deathList;
	}

	public List<Double> getBirthList() {
		return birthList;
	}
	
	public List<Double> getOxygenList() {
		return oxygenList;
	}
	
	public List<Double> getCarbonDioxideList() {
		return carbonDioxideList;
	}

	public void eventPopulationIncrease(int newPopulation) {
		if (newPopulation > maxPopulation) {
			maxPopulation = newPopulation;
			maxPopulationTime = time;
		}
	}

	public void eventPopulationDecrease(int newPopulation) {
		if (newPopulation < minPopulation) {
			minPopulation = newPopulation;
			minPopulationTime = time;
		}
	}

	public void eventOrganismCreated() {
		createdOrganisms++;
	}

	public void eventOrganismBorn(Organism newOrganism, Organism parent) {
		lastBornBeing = newOrganism.getGeneticCode();
		if (parent.getTotalChildren() > beingMostChildrenNumber) {
			beingMostChildren = parent.getGeneticCode();
			beingMostChildrenNumber = parent.getTotalChildren();
			beingMostChildrenTime = time;
		}
		birthSum++;
		birthLastTime++;
	}

	public void eventOrganismDie(Organism dyingOrganism,
			Organism killingOrganism) {
		lastDeadBeing = dyingOrganism.getGeneticCode();
		if (killingOrganism != null
				&& killingOrganism.getTotalKills() > beingMostKillsNumber) {
			beingMostKills = killingOrganism.getGeneticCode();
			beingMostKillsNumber = killingOrganism.getTotalKills();
			beingMostKillsTime = time;
		}
		deathSum++;
		deathLastTime++;
	}

	public void eventOrganismInfects(Organism infectedOrganism,
			Organism infectingOrganism) {
		lastInfectedBeing = infectedOrganism.getGeneticCode();
		if (infectingOrganism != null
				&& infectingOrganism.getTotalInfected() > beingMostInfectionsNumber) {
			beingMostInfections = infectingOrganism.getGeneticCode();
			beingMostInfectionsNumber = infectingOrganism.getTotalInfected();
			beingMostInfectionsTime = time;
		}
		infectionsSum++;
	}

	public void eventTime(int population, double O2, double CO2) {
		time++;
		if (deathLastTime > 1.5 * getAverageDeaths()) {
			if (deathLastTime > 3 * getAverageDeaths()) {
				if (massExtintionState != EXTINTION_CONFIRMED
						&& massExtintionState != EXTINTION_FINISHING)
					massExtintions++;
				massExtintionState = EXTINTION_CONFIRMED;
			} else {
				switch (massExtintionState) {
				case (EXTINTION_NO):
					massExtintionState = EXTINTION_POSSIBLE;
					break;
				case (EXTINTION_POSSIBLE):
					massExtintionState = EXTINTION_CONFIRMED;
					massExtintions++;
					break;
				case (EXTINTION_FINISHING):
					massExtintionState = EXTINTION_CONFIRMED;
					break;
				}
			}
		} else {
			switch (massExtintionState) {
			case (EXTINTION_POSSIBLE):
			case (EXTINTION_FINISHING):
				massExtintionState = EXTINTION_NO;
				break;
			case (EXTINTION_CONFIRMED):
				massExtintionState = EXTINTION_FINISHING;
				break;
			}
		}
		//System.out.println(deathLastTime + " " + getAverageDeaths() + " " +
		//massExtintionState + " " + massExtintions);
		// lastTimePopulation = population;
		populationSum += population;

		if (O2 > maxOxygen) {
			maxOxygen = O2;
			maxOxygenTime = time;
		}
		if (O2 < minOxygen) {
			minOxygen = O2;
			minOxygenTime = time;
		}
		if (CO2 > maxCarbonDioxide) {
			maxCarbonDioxide = CO2;
			maxCarbonDioxideTime = time;
		}
		if (CO2 < minCarbonDioxide) {
			minCarbonDioxide = CO2;
			minCarbonDioxideTime = time;
		}
		if (birthLastTime > maxBirths)
			maxBirths = birthLastTime;
		if (deathLastTime > maxDeaths)
			maxDeaths = deathLastTime;
		if (populationList.size() == 100)
			populationList.remove(0);
		populationList.add(Double.valueOf(population));
		if (deathList.size() == 100)
			deathList.remove(0);
		deathList.add(Double.valueOf(deathLastTime));
		if (birthList.size() == 100)
			birthList.remove(0);
		birthList.add(Double.valueOf(birthLastTime));
		if (oxygenList.size() == 100)
			oxygenList.remove(0);
		oxygenList.add(Double.valueOf(O2));
		if (carbonDioxideList.size() == 100)
			carbonDioxideList.remove(0);
		carbonDioxideList.add(Double.valueOf(CO2));
		deathLastTime = 0;
		birthLastTime = 0;
	}

	public void findBestAliveBeings(List<Organism> organisms) {
		Organism org;
		aliveBeingMostChildren = null;
		aliveBeingMostChildrenNumber = 0;
		aliveBeingMostKills = null;
		aliveBeingMostKillsNumber = 0;
		aliveBeingMostInfections = null;
		aliveBeingMostInfectionsNumber = 0;
		synchronized(organisms) {
			for (Iterator<Organism> it = organisms.iterator(); it.hasNext();) {
				org = it.next();
				if (org.isAlive()) {
					if (org.getTotalChildren() > aliveBeingMostChildrenNumber) {
						aliveBeingMostChildrenNumber = org.getTotalChildren();
						aliveBeingMostChildren = org.getGeneticCode();
					}
					if (org.getTotalKills() > aliveBeingMostKillsNumber) {
						aliveBeingMostKillsNumber = org.getTotalKills();
						aliveBeingMostKills = org.getGeneticCode();
					}
					if (org.getTotalInfected() > aliveBeingMostInfectionsNumber) {
						aliveBeingMostInfectionsNumber = org.getTotalInfected();
						aliveBeingMostInfections = org.getGeneticCode();
					}
				}
			}
		}
	}
}
