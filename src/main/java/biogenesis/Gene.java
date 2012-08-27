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

import java.io.*;
import java.awt.*;

/**
 * This class implements a single organism's gene. A gene is a colored segment.
 * This segment is part of the organism's body and will be drawn several times
 * depending on the symmetry of the organism. Genes are always segments starting
 * at (0,0). The position in the organism's body depends on their gene neighbors
 * and the organism's symmetry and mirroring.
 */
public class Gene implements Cloneable, Serializable {
	/**
	 * The version number of this class
	 */
	private static final long serialVersionUID = Utils.FILE_VERSION;
	private double _length = 0;
	private double _theta = 0;
	/**
	 * Segment's color
	 */
	private Color _color;

	/**
	 * Void constructor. Creates the gene but leave it uninitialized.
	 */
	public Gene() {
	}

	/**
	 * Creates a gene with the specified final point and color.
	 * 
	 * @param x
	 *            x coordinate of the final point
	 * @param y
	 *            t coordinate of the final point
	 * @param color
	 *            segment's color
	 */
	public Gene(double length, double theta, Color color) {
		_length = length;
		_theta = theta;
		_color = color;
	}

	public void randomizeColor() {
		int max_prob = Utils.RED_PROB + Utils.GREEN_PROB + Utils.BLUE_PROB
				+ Utils.CYAN_PROB + Utils.WHITE_PROB + Utils.GRAY_PROB
				+ Utils.YELLOW_PROB;
		int prob = Utils.random.nextInt(max_prob);
		int ac_prob = Utils.RED_PROB;
		if (prob < ac_prob) {
			_color = Color.RED;
			return;
		}
		ac_prob += Utils.GREEN_PROB;
		if (prob < ac_prob) {
			_color = Color.GREEN;
			return;
		}
		ac_prob += Utils.BLUE_PROB;
		if (prob < ac_prob) {
			_color = Color.BLUE;
			return;
		}
		ac_prob += Utils.CYAN_PROB;
		if (prob < ac_prob) {
			_color = Color.CYAN;
			return;
		}
		ac_prob += Utils.WHITE_PROB;
		if (prob < ac_prob) {
			_color = Color.WHITE;
			return;
		}
		ac_prob += Utils.GRAY_PROB;
		if (prob < ac_prob) {
			_color = Color.GRAY;
			return;
		}
		_color = Color.YELLOW;
	}

	public void randomizeLength() {
		_length = 2.0 + Utils.random.nextDouble() * 16.0;
	}

	public void randomizeTheta() {
		_theta = Utils.random.nextDouble() * 2.0 * Math.PI;
	}

	/**
	 * Randomize the component of this gene: final point and color. Coordinates
	 * are given a random number between -13 and -2 or 2 and 13. Color is given
	 * a random color. The probability of each color is taken from user
	 * preferences.
	 */
	public void randomize() {
		randomizeLength();
		randomizeTheta();
		randomizeColor();
	}

	/**
	 * Return an exact copy of this gene.
	 */
	@Override
	public Object clone() {
		Gene newGen = null;
		try {
			newGen = (Gene) super.clone();
		} catch (CloneNotSupportedException e) {// We should never reach this
		}
		return newGen;
	}

	public double getLength() {
		return _length;
	}

	public double getTheta() {
		return _theta;
	}

	/**
	 * Returns the segment's color.
	 * 
	 * @return the segment's color
	 */
	public Color getColor() {
		return _color;
	}

	/**
	 * Assign a color to the segment.
	 * 
	 * @param color
	 *            The color to assign
	 */
	public void setColor(Color color) {
		_color = color;
	}

	public void setLength(double length) {
		_length = length;
	}

	public void setTheta(double theta) {
		_theta = theta;
	}
}
