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

import java.awt.Graphics;
import java.awt.Rectangle;

public abstract class Corridor extends Rectangle {
	private static final long serialVersionUID = Utils.FILE_VERSION;
	protected World world;
	protected Organism travellingOrganism = null;
	protected Connection connection;
	
	public Corridor(World w) {
		world = w;
		width = Math.min(100, world.getWidth());
		height = Math.min(100, world.getHeight());
		x = Utils.random.nextInt(world.getWidth()-width);
		y = Utils.random.nextInt(world.getHeight()-height);
	}
	public Organism getTravellingOrganism() {
		return travellingOrganism;
	}
	
	public abstract void draw(Graphics g);
}
