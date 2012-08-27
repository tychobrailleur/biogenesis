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

import java.awt.Color;
import java.awt.Graphics;

public class OutCorridor extends Corridor {
	private static final long serialVersionUID = Utils.FILE_VERSION;
	
	public boolean canSendOrganism() {
		return travellingOrganism == null ? true : false;
	}
	
	public OutCorridor(World w, Connection c) {
		super(w);
		connection = c;
	}
	
	public boolean sendOrganism(Organism org) {
		if (travellingOrganism == null && connection.state == Connection.STATE_CONNECTED) {
			connection.send(org.getGeneticCode());
			travellingOrganism = org;
			org.useEnergy(org.getEnergy());
			org.alive = false;
			world.decreasePopulation();
			return true;
		}
		return false;
	}
	
	@Override
	public void draw(Graphics g) {
		g.setColor(Color.BLUE);
		g.drawRect(x,y,width,height);
		g.setColor(Utils.ColorLIGHT_BLUE);
		g.fillRect(x+1,y+1,width-1,height-1);
		if (travellingOrganism != null) {
			if (travellingOrganism._growthRatio < 16) {
				travellingOrganism._growthRatio++;
				travellingOrganism._theta += Math.PI / 6;
				travellingOrganism.symmetric();
				travellingOrganism.calculateBounds(true);
				travellingOrganism.draw(g);
			} else {
				travellingOrganism=null;
			}
		}
	}
	
	
}
