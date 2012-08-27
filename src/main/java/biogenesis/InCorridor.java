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
import java.util.LinkedList;
import java.util.Queue;

public class InCorridor extends Corridor {
	private static final long serialVersionUID = Utils.FILE_VERSION;
	private Queue<GeneticCode> pendingOrganisms = new LinkedList<GeneticCode>();
	private Organism nextOrganism = null;
	
	public InCorridor(World w) {
		super(w);
	}

	public synchronized void receiveOrganism(GeneticCode code) {
		pendingOrganisms.add(code);
	}
	
	public void frame() {
		if (nextOrganism == null) {
			GeneticCode nextCode;
			synchronized(pendingOrganisms) {
				nextCode = pendingOrganisms.poll();
			}
			if (nextCode != null)
				nextOrganism = new Organism(world, nextCode);
		}
		if (nextOrganism != null) {
			if (nextOrganism.pasteOrganism(x+Utils.random.nextInt(width), 
					y+Utils.random.nextInt(height))) {
				world.addOrganism(nextOrganism, null);
				nextOrganism = null;
			}
		}
	}
	
	@Override
	public void draw(Graphics g) {
		g.setColor(Color.RED);
		g.drawRect(x,y,width,height);
		g.setColor(Utils.ColorLIGHT_RED);
		g.fillRect(x+1,y+1,width-1,height-1);
	}

}
