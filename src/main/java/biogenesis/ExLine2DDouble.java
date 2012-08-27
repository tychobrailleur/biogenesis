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

import java.awt.geom.*;

/**
 * An extention of the Line2D.Double class that can find the intersection point
 * with another line.
 */
public class ExLine2DDouble extends Line2D.Double {
	private static final long serialVersionUID = Utils.FILE_VERSION;

	/**
	 * Return the intersection of this line with l.
	 *  
	 * @param l  The line to which the intersecion is to be calculated
	 * @return  A Point2D.Double indicating the point of intersection or
	 * the middle point between the two segments if they are parallel.
	 */
	public Point2D.Double getIntersection(Line2D.Double l)
	{
		double x,y;
		double d;
		double t;
		d = (l.x1-l.x2)*(y2-y1)+(x2-x1)*(l.y2-l.y1);
		if (d < Utils.tol) {
			// Parallel or the same straigh line
			return new Point2D.Double((x1+x2+l.x1+l.x2)/4d,(y1+y2+l.y1+l.y2)/4d);
		}
		t = ((l.x1-x1)*(y2-y1)+(x2-x1)*(y1-l.y1))/d;
		x = l.x1 + t*(l.x2-l.x1);
		y = l.y1 + t*(l.y2-l.y1);

		return new Point2D.Double(x,y);
	}
}
