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

public class Vector2D {
	protected double _modulus = 0;
	protected double _theta = 0;
	
	Vector2D() {
	}
	
	public double getX() {
		return _modulus*Math.cos(_theta);
	}
	
	public void setPolar(double modulus, double theta) {
		_modulus = modulus;
		_theta = theta; 
	}
	
	public void setModulus(double modulus) {
		_modulus = modulus;
	}
	
	public void setTheta(double theta) {
		_theta = theta;
	}
	
	public double getY() {
		return _modulus*Math.sin(_theta);
	}
	
	public double getModulus() {
		return _modulus;
	}
	
	public double getTheta() {
		return _theta;
	}
	
	public void addDegree(double theta) {
		_theta+=theta;
	}
	
	public void invertX() {
		_theta=Math.PI-_theta;
	}
	
	public void invertY() {
		_theta=2.0*Math.PI-_theta;
	}
}
