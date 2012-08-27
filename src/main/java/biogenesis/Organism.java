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
import java.awt.image.*;
import java.awt.geom.*;
/**
 * This class implements an organism.
 * The body of the organism is drawn inside the Rectangle from which it inherits.
 */
public class Organism extends Rectangle {
	/**
	 * The version of this class
	 */
	private static final long serialVersionUID = Utils.FILE_VERSION;
	/**
	 * A reference to the genetic code of this organism
	 */
	protected GeneticCode _geneticCode;
	/**
	 * If this organism has been infected by a white segment, here we have the
	 * genetic code that this organism will reproduce.
	 */
	protected GeneticCode _infectedGeneticCode = null;
	/**
	 * Number of children that this organism will produce at once. This
	 * is the number of yellow segments in its genetic code with a
	 * maximum of 8 and a minimum of 1.
	 */
	protected int _nChildren;
	/**
	 * Reference to the world where the organism lives.
	 */
	protected World _world;
	/**
	 * Reference to the visual part of the world where the organism lives.
	 */
	transient protected VisibleWorld _visibleWorld;
	/**
	 * Identification number of this organism's parent.
	 */
	protected int _parentID;
	/**
	 * Identification number of this organism.
	 */
	protected int _ID;
	/**
	 * Generation number
	 */
	protected int _generation;
	/**
	 * Number of children it has produced.
	 */
	protected int _nTotalChildren=0;
	/**
	 * Number of organism that has killed
	 */
	protected int _nTotalKills=0;
	/**
	 * Number of organism that has infected
	 */
	protected int _nTotalInfected=0;
	/**
	 * X coordinates of the starting point of each organism's segments.
	 */
	protected int[] _startPointX;
	/**
	 * Y coordinates of the starting point of each organism's segments.
	 */
	protected int[] _startPointY;
	/**
	 * X coordinates of the ending point of each organism's segments.
	 */
	protected int[] _endPointX;
	/**
	 * Y coordinates of the ending point of each organism's segments.
	 */
	protected int[] _endPointY;
	/**
	 * Precalculated distance from the origin to the starting point of each segment.
	 * Used to calculate rotations.
	 */
	protected double[] _m1;
	/**
	 * Precalculated distance from the origin to the ending point of each segment.
	 * Used to calculate rotations.
	 */
	protected double[] _m2;
	/**
	 * Precalculated modulus of each segment.
	 */
	protected double[] _m;
	/**
	 * X coordinate of this organim's center of gravity.
	 */
	protected int _centerX;
	/**
	 * Y coordinate of this organim's center of gravity.
	 */
	protected int _centerY;
	/**
	 * Like _centerX but with double precision to be able to make movements slower than a pixel.
	 */
	protected double _dCenterX;
	/**
	 * Like _centerY but with double precision to be able to make movements slower than a pixel.
	 */
	protected double _dCenterY;
	/**
	 * Effective segment colors, taken from the genetic code if alive or brown if dead.
	 */
	protected Color[] _segColor;
	/**
	 * The total number of segments of the organism
	 */
	protected int _segments;
	/**
	 * Growth ratio of the organism. Used to calculate segments when the organism is not
	 * fully grown.
	 */
	protected int _growthRatio;
	/**
	 * Total mass of this organism. The mass is calculated as the sum of all segment lengths.
	 * Used to calculate the effect of collisions.
	 */
	protected double _mass = 0;
	/**
	 * Moment of inertia of this organism, used to calculate the effect of collisions.
	 */
	protected double _I = 0;
	/**
	 * Chemical energy stored by this organism
	 */
	protected double _energy;
	/**
	 * Organism size independent on its position in the world.
	 * Let p be a point in the organism. Then, p.x + x - _sizeRect.x is the x coordinate
	 * of p representation in the world.
	 */
	protected Rectangle _sizeRect = new Rectangle();
	/**
	 * Rotation angle that this organism has at a given moment.
	 */
	protected double _theta;
	/**
	 * Last frame angle, used to avoid calculating point rotations when the angle doesn't
	 * change between two consecutive frames.
	 */
	protected double _lastTheta = -1;
	/**
	 * Rotated segments of the last frame, to use when _theta == _lastTheta
	 */
	protected int x1[],y1[],x2[],y2[];
	/**
	 * Speed. Variation applied to organism coordinates at every frame.
	 */
	protected double dx=0d, dy=0d;
	/**
	 * Angular speed. Organism angle variation at every frame.
	 */
	protected double dtheta = 0d;
	/**
	 * Number of frames of life of this organism
	 */
	protected int _age=0;
	/**
	 * Color used to draw the organism when a collision occurs. We save the color that
	 * should be shown and the number of frames that it should be shown. If the number
	 * if frames is 0, each segment is shown in its color.
	 */
	protected Color _color;
	/**
	 * Number of frames in which the organism will be drawn in _color.
	 */
	protected int _framesColor = 0;
	/**
	 * Number of frame that need to pass between two reproductions, even they are not
	 * successfully.
	 */
	protected int _timeToReproduce = 0;
	/**
	 * Indicates if the organism has grown at the last frame. If it has grown it is
	 * necessary to recalculate its segments.
	 */
	protected int hasGrown;
	/**
	 * Indicates if it has moved at the last frame. If it has moved it is necessary
	 * to repaint it.
	 */
	protected boolean hasMoved = true;
	/**
	 * The place that this organism occupies at the last frame. If the organism
	 * moves, this rectangle must be painted too.
	 */
	protected Rectangle lastFrame = new Rectangle();
	/**
	 * Indicates if the organism is alive.
	 */
	protected boolean alive = true;
	private static transient Vector2D v = new Vector2D();
	/**
	 * Returns true if this organism is alive, false otherwise.
	 * 
	 * @return  true if this organism is alive, false otherwise.
	 */
	public boolean isAlive() {
		return alive;
	}
	/**
	 * Returns the amount of chemical energy stored by this organism.
	 * 
	 * @return  The amount of chemical energy stored by this organism.
	 */
	public double getEnergy() {
		return _energy;
	}
	/**
	 * Returns the identification number of this organism.
	 * 
	 * @return  The identification number of this organism.
	 */
	public int getID() {
		return _ID;
	}
	/**
	 * Returns the identification number of this organism's parent.
	 * 
	 * @return  The identification number of this organism's parent.
	 */
	public int getParentID() {
		return _parentID;
	}
	/**
	 * Returns the generation number of this organism.
	 * 
	 * @return  The generation number of this organism.
	 */
	public int getGeneration() {
		return _generation;
	}
	/**
	 * Returns the age of this organism.
	 * 
	 * @return  The age of this organism, in number of frames.
	 */
	public int getAge() {
		return _age;
	}
	/**
	 * Returns the number of children that this organism produced.
	 * 
	 * @return  The number of children that this organism produced.
	 */
	public int getTotalChildren() {
		return _nTotalChildren;
	}
	/**
	 * Returns the number of organisms killed by this organism.
	 * 
	 * @return  The number of organisms killed by this organism.
	 */
	public int getTotalKills() {
		return _nTotalKills;
	}
	/**
	 * Returns the number of organisms infected by this organism.
	 * 
	 * @return  The number of organisms infected by this organism.
	 */
	public int getTotalInfected() {
		return _nTotalInfected;
	}
	/**
	 * Returns a reference to this organism's genetic code.
	 * 
	 * @return  A reference to this organism's genetic code.
	 */
	public GeneticCode getGeneticCode() {
		return _geneticCode;
	}
	/**
	 * Returns the total mass of this organism.
	 * 
	 * @return  The total mass of this organism calculated as the sum
	 * of all its segments length.
	 */
	public double getMass() {
		return _mass;
	}
	/**
	 * Basic constructor. Doesn't initialize it: use {@link randomCreate}
	 * or {@link inherit} to do this.
	 * 
	 * @param world  A reference to the world where this organism is in.
	 */
	public Organism(World world) {
		_world = world;
		_visibleWorld = world._visibleWorld;
		_theta = Utils.random.nextDouble() * Math.PI * 2d;
	}
	/**
	 * Construct an organism with a given genetic code. Doesn't initialize it:
	 * use {@link pasteOrganism} to do it. Use {@link World.addOrganism} to add
	 * it to the world.
	 * 
	 * @param world  A reference to the world where this organism is in.
	 * @param geneticCode  A reference to the genetic code of this organism.
	 */
	public Organism(World world, GeneticCode geneticCode) {
		_world = world;
		_visibleWorld = world._visibleWorld;
		_theta = Utils.random.nextDouble() * Math.PI * 2d;
		_geneticCode = geneticCode;
	}
	/**
	 * Creates all data structures of this organism. Must be used after the organism
	 * has a genetic code assigned.
	 */
	protected void create() {
		_segments = _geneticCode.getNGenes() * _geneticCode.getSymmetry();
		_segColor = new Color[_segments];
		for (int i = 0; i < _segments; i++)
			_segColor[i] = _geneticCode.getGene(i%_geneticCode.getNGenes()).getColor();
		_startPointX = new int[_segments];
		_startPointY = new int[_segments];
		_endPointX = new int[_segments];
		_endPointY = new int[_segments];
		_m1 = new double[_segments];
		_m2 = new double[_segments];
		_m = new double[_segments];
		x1 = new int[_segments];
		y1 = new int[_segments];
		x2 = new int[_segments];
		y2 = new int[_segments];
	}
	/**
	 * Initializes variables for a new random organism and finds a place
	 * to put it in the world.
	 * 
	 * @return  true if it found a place for this organism or false otherwise.
	 */
	public boolean randomCreate() {
		// Generates a random genetic code
		_geneticCode = new GeneticCode();
		// it has no parent
		_parentID = -1;
		_generation = 1;
		_growthRatio = 16;
		// initial energy
		_energy = Math.min(Utils.INITIAL_ENERGY,_world.getCO2());
		_world.decreaseCO2(_energy);
		_world.addO2(_energy);
		// initialize
		create();
		symmetric();
		// put it in the world
		return placeRandom();
	}
	/**
	 * Initializes variables for a new organism born from an existing
	 * organism. Generates a mutated genetic code based on the parent's one
	 * and finds a place in the world to put it.
	 * 
	 * @param parent  The organism from which this organism is born. 
	 * @return  true if it found a place for this organism or false otherwise.
	 */
	public boolean inherit(Organism parent, boolean first) {
		GeneticCode inheritGeneticCode;
		boolean ok = true;
		
		// Create the inherited genetic code
		if (parent._infectedGeneticCode != null)
			inheritGeneticCode = parent._infectedGeneticCode;
		else
			inheritGeneticCode = parent._geneticCode;
		_geneticCode = new GeneticCode(inheritGeneticCode);
		// Take a reference to the parent
		_parentID = parent.getID();
		_generation = parent.getGeneration() + 1;
		_growthRatio = 16;
		// Initial energy: minimum energy required to reproduce is divided
		// between all children and the parent.
		_energy = Math.min((inheritGeneticCode._reproduceEnergy / (double)(parent._nChildren + 1)), parent._energy);
		if (first || parent._energy >= _energy+Utils.YELLOW_ENERGY_CONSUMPTION) {
			// Initialize
			create();
			symmetric();
			// Put it in the world, near its parent
			ok = placeNear(parent);
			if (ok && !first)
				parent.useEnergy(Utils.YELLOW_ENERGY_CONSUMPTION);
		} else
			ok = false;
		
		return ok;
	}
	/**
	 * Places the organism at the specified position in the world and initializes its
	 * variables. The organism must has an assigned genetic code.
	 * 
	 * @param posx  The x coordinate of the position in the world we want to put this organism.
	 * @param posy  The y coordinate of the position in the world we want to put this organism.
	 * @return  true if there were enough space to put the organism, false otherwise.
	 */
	public boolean pasteOrganism(int posx, int posy) {
		_parentID = -1;
		_generation = 1;
		_growthRatio = 16;
		create();
		symmetric();
		_dCenterX = _centerX = posx;
		_dCenterY = _centerY = posy;
		calculateBounds(true);
		// Check that the position is inside the world
		if (isInsideWorld()) {
			// Check that the organism will not overlap other organisms
			if (_world.fastCheckHit(this) == null) {
				// Generem identificador
				_ID = _world.getNewId();
				_energy = Math.min(Utils.INITIAL_ENERGY,_world.getCO2());
				_world.decreaseCO2(_energy);
				_world.addO2(_energy);
				return true;
			}
		}
		// It can't be placed		
		return false;
	}
	/**
	 * Translates the genetic code of this organism to its segments representation in the world.
	 * Also, calculates some useful information like segments length, inertia, etc.
	 * This method must be called when an organism is firstly displayed on the world and every
	 * time it changes its size.
	 * inherit, randomCreate and pasteOrganism are the standard ways to add an organism to a world
	 * and they already call this method.
	 */
	public void symmetric() {
		int i,j,segment=0;
		int symmetry = _geneticCode.getSymmetry();
		int mirror = _geneticCode.getMirror();
		int sequence = _segments / symmetry;
		int left=0, right=0, top=0, bottom=0;
		int centerX, centerY;
		double cx, cy;

		for (i=0; i<symmetry; i++) {
			for (j=0; j<sequence; j++,segment++) {
				// Here, we take the vector that forms the segment, scale it depending on
				// the relative size of the organism and rotate it depending on the
				// symmetry and mirroring.
				v.setModulus(_geneticCode.getGene(j).getLength()/Utils.scale[_growthRatio-1]);
				if (j==0) {
					_startPointX[segment] = 0;
					_startPointY[segment] = 0;
					if (mirror == 0 || i%2==0)
						v.setTheta(_geneticCode.getGene(j).getTheta()+i*2*Math.PI/symmetry);
					else {
						v.setTheta(_geneticCode.getGene(j).getTheta()+(i-1)*2*Math.PI/symmetry);
						v.invertX();
					}
				} else {
					_startPointX[segment] = _endPointX[segment - 1];
					_startPointY[segment] = _endPointY[segment - 1];
					if (mirror == 0 || i%2==0)
						v.addDegree(_geneticCode.getGene(j).getTheta());
					else
						v.addDegree(-_geneticCode.getGene(j).getTheta());
				}
				// Apply the vector to the starting point to get the ending point.
				_endPointX[segment] = (int) Math.round(v.getX() + _startPointX[segment]);
				_endPointY[segment] = (int) Math.round(v.getY() + _startPointY[segment]);
			    // Calculate the bounding rectangle of this organism
			    left = Math.min(left, _endPointX[segment]);
			    right = Math.max(right, _endPointX[segment]);
			    top = Math.min(top, _endPointY[segment]);
			    bottom = Math.max(bottom, _endPointY[segment]);
			}
		}
		_sizeRect.setBounds(left, top, right-left+1, bottom-top+1);
		// image center
		centerX = (left+right)>>1;
		centerY = (top+bottom)>>1;
		_mass = 0;
		_I = 0;
		for (i=0; i<_segments; i++) {
			// express points relative to the image center
			_startPointX[i]-=centerX;
			_startPointY[i]-=centerY;
			_endPointX[i]-=centerX;
			_endPointY[i]-=centerY;
			// calculate points distance of the origin and modulus
			_m1[i] = Math.sqrt(_startPointX[i]*_startPointX[i]+_startPointY[i]*_startPointY[i]);
			_m2[i] = Math.sqrt(_endPointX[i]*_endPointX[i]+_endPointY[i]*_endPointY[i]);
			_m[i] = Math.sqrt(Math.pow(_endPointX[i]-_startPointX[i],2) + 
					Math.pow(_endPointY[i]-_startPointY[i],2));
			_mass += _m[i];
			// calculate inertia moment
			// the mass center of a segment is its middle point
			cx = (_startPointX[i] + _endPointX[i]) / 2d;
			cy = (_startPointY[i] + _endPointY[i]) / 2d;
			// add the effect of this segment, following the parallel axis theorem
			_I += Math.pow(_m[i],3)/12d +
				_m[i] * cx*cx + cy*cy;// mass * length^2 (center is at 0,0)
		}
	}
	/**
	 * Given a vector, calculates the resulting vector after a rotation, a scalation and possibly
	 * after mirroring it.
	 * The rotation degree and the mirroring is found using the Utils.degree array, where parameter
	 * mirror is the row and step is the column. The step represents the repetition of this vector
	 * following the organism symmetry.
	 * The scalation is calculated using the Utils.scale coefficients, using the organism's
	 * _growthRatio to find the appropriate value. 
	 * 
	 * @param p  The end point of the vector. The starting point is (0,0).
	 * @param step  The repetition of the vectors pattern  we are calculating.
	 * @param mirror  If mirroring is applied to this organism 1, otherwise 0.
	 * @return  The translated vector.
	 */
/*	private Vector2D translate(Point p, int step, int mirror) {
		if (p.x == 0 && p.y == 0)
			return new Vector2D();

		double px = p.x;
		double py = p.y;

		px /= Utils.scale[_growthRatio - 1];
		py /= Utils.scale[_growthRatio - 1];

		Vector2D v = new Vector2D(px,py);
		v.addDegree(Utils.degree[mirror][step]);

		if (Utils.invertX[mirror][step] != 0)
			v.invertX();
		if (Utils.invertY[mirror][step] != 0)
			v.invertY();

		return v;
	}*/
	/**
	 * Tries to find a spare place in the world for this organism and place it.
	 * It also generates an identification number for the organism if it can be placed
	 * somewhere.
	 * 
	 * @return  true if a suitable place has been found, false if not.
	 */
	private boolean placeRandom() {
		/* We try to place the organism in 12 different positions. If all of them
		 * are occupied, we return false.
		 */
		for (int i=12; i>0; i--) {
			/* Get a random point for the top left corner of the organism
			 * making sure it is inside the world.
			 */
			Point origin = new Point(
				Utils.random.nextInt(_world.getWidth()-_sizeRect.width),
				Utils.random.nextInt(_world.getHeight()-_sizeRect.height));
			setBounds(origin.x,origin.y,_sizeRect.width,_sizeRect.height);
			_dCenterX = _centerX = origin.x + (_sizeRect.width>>1);
			_dCenterY = _centerY = origin.y + (_sizeRect.height>>1);
			// Check that the position is not occupied.
			if (_world.fastCheckHit(this) == null) {
				// Generate an identification
				_ID = _world.getNewId();
				return true;
			}
		}
		// If we get here, we haven't find a place for this organism.
		return false;
	}
	/**
	 * Tries to find a spare place near its parent for this organism and place it.
	 * It also generates an identification number for the organism if it can be placed
	 * somewhere and substracts its energy from its parent's energy.
	 * 
	 * @return  true if a suitable place has been found, false if not.
	 */
	private boolean placeNear(Organism parent) {
		int nPos = Utils.random.nextInt(8);
		// Try to put it in any possible position, starting from a randomly chosen one.
		for (int nSide = 0; nSide < 8; nSide++) {
			// Calculate candidate position
			_dCenterX = parent._dCenterX + (parent.width / 2 + width / 2+ 1) * Utils.side[nPos][0]; 
			_dCenterY = parent._dCenterY + (parent.height / 2 + height / 2 + 1) * Utils.side[nPos][1];
			_centerX = (int) _dCenterX;
			_centerY = (int) _dCenterY;
			calculateBounds(true);
			// Check this position is inside the world.
			if (isInsideWorld()) {
				// Check that it doesn't overlap with other organisms.
				if (_world.fastCheckHit(this) == null) {
					if (parent._geneticCode.getDisperseChildren()) {
						dx = Utils.side[nPos][0];
						dy = Utils.side[nPos][1];
					} else {
						dx = parent.dx;
						dy = parent.dy;
					}
					// Generate an identification
					_ID = _world.getNewId();
					// Substract the energy from the parent
					parent._energy -= _energy;
					return true;
				}
			}
			nPos = (nPos + 1) % 8;
		}
		// It can't be placed.
		return false;
	}
	/**
	 * Draws this organism to a graphics context.
	 * The organism is drawn at its position in the world.
	 * 
	 * @param g  The graphics context to draw to.
	 */
	public void draw(Graphics g) {
		int i;
		if (_framesColor > 0) {
			// Draw all the organism in the same color
			g.setColor(_color);
			_framesColor--;
			for (i=0; i<_segments; i++)
				g.drawLine(
					x1[i] + _centerX,
					y1[i] + _centerY,
					x2[i] + _centerX,
					y2[i] + _centerY);
		} else {
			if (alive) {
				for (i=0; i<_segments; i++) {
					g.setColor(_segColor[i]);
					g.drawLine(
							x1[i] + _centerX,
							y1[i] + _centerY,
							x2[i] + _centerX,
							y2[i] + _centerY);
				}
			} else {
				g.setColor(Utils.ColorBROWN);
				for (i=0; i<_segments; i++) {
					g.drawLine(
							x1[i] + _centerX,
							y1[i] + _centerY,
							x2[i] + _centerX,
							y2[i] + _centerY);
				}
			}
		}
	}
	/**
	 * Calculates the position of all organism points in the world, depending on
	 * its rotation. It also calculates the bounding rectangle of the organism.
	 * This method must be called from outside this class only when doing
	 * manual drawing.  
	 * 
	 * @param force  To avoid calculations, segments position are only calculated
	 * if the organism's rotation has changed in the last frame. If it is necessary
	 * to calculate them even when the rotation hasn't changed, assign true to this
	 * parameter.
	 */
	public void calculateBounds(boolean force) {
		double left=java.lang.Double.MAX_VALUE, right=java.lang.Double.MIN_VALUE, 
		top=java.lang.Double.MAX_VALUE, bottom=java.lang.Double.MIN_VALUE;
		
		double theta;
		for (int i=_segments-1; i>=0; i--) {
			/* Save calculation: if rotation hasn't changed and it is not forced,
			 * don't calculate points again.
			 */
			if (_lastTheta != _theta || force) {
				theta=_theta+Math.atan2(_startPointY[i] ,_startPointX[i]);
				x1[i]=(int)(_m1[i]*Math.cos(theta));
				y1[i]=(int)(_m1[i]*Math.sin(theta));
				theta=_theta+Math.atan2(_endPointY[i], _endPointX[i]);
				x2[i]=(int)(_m2[i]*Math.cos(theta));
				y2[i]=(int)(_m2[i]*Math.sin(theta));
			}
			// Finds the rectangle that comprises the organism
			left = Utils.min(left, x1[i]+ _dCenterX, x2[i]+ _dCenterX);
			right = Utils.max(right, x1[i]+ _dCenterX, x2[i]+ _dCenterX);
			top = Utils.min(top, y1[i]+ _dCenterY, y2[i]+ _dCenterY);
			bottom = Utils.max(bottom, y1[i]+ _dCenterY, y2[i]+ _dCenterY);
		}
		setBounds((int)left, (int)top, (int)(right-left+1)+1, (int)(bottom-top+1)+1);
		_lastTheta = _theta;
	}
	/**
	 * If its the time for this organism to grow, calculates its new segments and speed.
	 * An alive organism can grow once every 8 frames until it gets its maximum size.
	 */
	private void grow() {
		if (_growthRatio > 1 && (_age & 0x07) == 0x07 && alive && _energy >= _mass/10) {
			_growthRatio--;
			double m = _mass;
			double I = _I;
			symmetric();
			// Cynetic energy is constant. If mass changes, speed must also change.
			m = Math.sqrt(m/_mass);
			dx *= m;
			dy *= m;
			dtheta *= Math.sqrt(I/_I);
			hasGrown = 1;
		} else {
			if (_growthRatio < 15 && _energy < _mass/12) {
				_growthRatio++;
				double m = _mass;
				double I = _I;
				symmetric();
				// Cynetic energy is constant. If mass changes, speed must also change.
				m = Math.sqrt(m/_mass);
				dx *= m;
				dy *= m;
				dtheta *= Math.sqrt(I/_I);
				hasGrown = -1;
			} else
				hasGrown = 0;
		}
	}
	
	/**
	 * Makes this organism reproduce. It tries to create at least one
	 * child and at maximum 8 (depending on the number of yellow segments
	 * of the organism) and put them in the world.
	 */
	public void reproduce() {
		Organism newOrg;
		
		for (int i=0; i < Utils.between(_nChildren,1,8); i++) {
			newOrg = new Organism(_world);
			if (newOrg.inherit(this, i==0)) {
				// It can be created
				_nTotalChildren++;
				_world.addOrganism(newOrg,this);
				_infectedGeneticCode = null;
			}
			_timeToReproduce = 20;
		}
	}
	/**
	 * Executes the organism's movement for this frame.
	 * This includes segments upkeep and activation,
	 * movement, growth, collision detection, reproduction,
	 * respiration and death.
	 */
	public boolean move() {
		boolean collision = false;
		hasMoved = false;
		lastFrame.setBounds(this);
		if (Math.abs(dx) < Utils.tol) dx = 0;
		if (Math.abs(dy) < Utils.tol) dy = 0;
		if (Math.abs(dtheta) < Utils.tol) dtheta = 0;
		// Apply segment effects for this frame.
		segmentsFrameEffects();
		// Apply rubbing effects
		rubbingFramesEffects();
		// Check if it can grow or shrink
		grow();
		// Movement
		double dxbak=dx, dybak=dy, dthetabak=dtheta;
		offset(dx,dy,dtheta);
		calculateBounds(hasGrown!=0);
		
		if (hasGrown!=0 || dx!=0 || dy!=0 || dtheta!=0) {
			hasMoved = true;
			// Check it is inside the world
			collision = !isInsideWorld();
			// Collision detection with biological corridors
			if (alive) {
				OutCorridor c = _world.checkHitCorridor(this);
				if (c != null && c.canSendOrganism()) {
					if (c.sendOrganism(this))
						return false;
				}
			}
			// Collision detection with other organisms.
			if (_world.checkHit(this) != null)
				collision = true;
			// If there is a collision, undo movement.
			if (collision) {
				hasMoved = false;
				offset(-dxbak,-dybak,-dthetabak);
				if (hasGrown!=0) {
					_growthRatio+=hasGrown;
					symmetric();
				}
				calculateBounds(hasGrown!=0);
			}
		}
		// Substract one to the time needed to reproduce
		if (_timeToReproduce > 0)
			_timeToReproduce--;
		// Check if it can reproduce: it needs enough energy and to be adult
		if (_energy > _geneticCode.getReproduceEnergy() + Utils.YELLOW_ENERGY_CONSUMPTION*(_nChildren-1)
				&& _growthRatio==1 && _timeToReproduce==0 && alive)
			reproduce();
		// Check that it don't exceed the maximum chemical energy
		if (_energy > 2*_geneticCode.getReproduceEnergy())
			useEnergy(_energy - 2*_geneticCode.getReproduceEnergy());
		// Maintenance
		breath();
		// Check that the organism has energy after this frame
		return _energy > Utils.tol;
	}
	/**
	 * Makes the organism spend an amount of energy using the
	 * respiration process.
	 * 
	 * @param q  The quantity of energy to spend.
	 * @return  true if the organism has enough energy and there are
	 * enough oxygen in the atmosphere, false otherwise.
	 */
	public boolean useEnergy(double q) {
		if (_energy < q) {
			return false;
		}
		double respiration = _world.respiration(q);
		_energy -= respiration;
		if (respiration < q)
			return false;
		return true;
	}
	/**
	 * Realize the respiration process to maintain its structure.
	 * Aging is applied here too.
	 */
	public void breath() {
		if (alive) {
			_age++;
			// Respiration process
			boolean canBreath = useEnergy(Math.min(_mass / Utils.SEGMENT_COST_DIVISOR, _energy));
			if ((_age >> 8) > _geneticCode.getMaxAge() || !canBreath) {
				// It's dead, but still may have energy
				die(null);
			} else {
				if (_energy <= Utils.tol) {
					alive = false;
					_world.decreasePopulation();
					_world.organismHasDied(this, null);
				}
			}
		} else {
			// The corpse slowly decays
			useEnergy(Math.min(_energy, Utils.DECAY_ENERGY));
		}
	}
	/**
	 * Kills the organism. Sets its segments to brown and tells the world
	 * about the event.
	 * 
	 * @param killingOrganism  The organism that has killed this organism,
	 * or null if it has died of natural causes.
	 */
	public void die(Organism killingOrganism) {
		alive = false;
		hasMoved = true;
		for (int i=0; i<_segments; i++) {
			_segColor[i] = Utils.ColorBROWN;
		}
		_world.decreasePopulation();
		if (killingOrganism != null)
			killingOrganism._nTotalKills++;
		_world.organismHasDied(this, killingOrganism);
	}
	/**
	 * Infects this organism with a genetic code.
	 * Tells the world about this event.
	 * Not currently used.
	 * 
	 * @param infectingCode  The genetic code that infects this organism.
	 */
	public void infectedBy(GeneticCode infectingCode) {
		_infectedGeneticCode = infectingCode;
		_world.organismHasBeenInfected(this, null);
	}
	/**
	 * Infects this organism with the genetic code of another organism.
	 * Tells the world about this event.
	 * 
	 * @param infectingOrganism  The organism that is infecting this one.
	 */
	public void infectedBy(Organism infectingOrganism) {
		infectingOrganism._nTotalInfected++;
		_infectedGeneticCode = infectingOrganism.getGeneticCode();
		_world.organismHasBeenInfected(this, infectingOrganism);
	}
	/**
	 * Calculates the resulting speeds after a collision between two organisms, following
	 * physical rules.
	 * 
	 * @param org  The other organism in the collision.
	 * @param p  Intersection point between the organisms.
	 * @param l  Line that has collided. Of the two lines, this is the one that collided
	 * on the center, not on the vertex.
	 * @param thisOrganism  true if l is a line of this organism, false if l is a line of org.
	 */
	private final void touchMove(Organism org, Point2D.Double p, Line2D l, boolean thisOrganism) {
		// Distance vector between centers of mass and p
		double rapx = p.x - _dCenterX;
		double rapy = p.y - _dCenterY;
		double rbpx = p.x - org._dCenterX;
		double rbpy = p.y - org._dCenterY;
		// Speeds of point p in the body A and B, before collision.
		double vap1x = dx - dtheta * rapy + hasGrown*rapx/10d;
		double vap1y = dy + dtheta * rapx + hasGrown*rapy/10d;
		double vbp1x = org.dx - org.dtheta * rbpy;
		double vbp1y = org.dy + org.dtheta * rbpx;
		// Relative speeds between the two collision points.
		double vab1x = vap1x - vbp1x;
		double vab1y = vap1y - vbp1y;
		// Normal vector to the impact line
		//First: perpendicular vector to the line
		double nx = l.getY1() - l.getY2();
		double ny = l.getX2() - l.getX1();
		//Second: normalize, modulus 1
		double modn = Math.sqrt(nx * nx + ny * ny);
		nx /= modn;
		ny /= modn;
		/*Third: of the two possible normal vectors we need the one that points to the
		 * outside; we choose the one that its final point is the nearest to the center
		 * of the other line.
		 */
		if (thisOrganism) {
			if ((p.x+nx-org._dCenterX)*(p.x+nx-org._dCenterX)+(p.y+ny-org._dCenterY)*(p.y+ny-org._dCenterY) <
				(p.x-nx-org._dCenterX)*(p.x-nx-org._dCenterX)+(p.y-ny-org._dCenterY)*(p.y-ny-org._dCenterY)) {
				nx = -nx;
				ny = -ny;
			}
		} else {
			if ((p.x+nx-_dCenterX)*(p.x+nx-_dCenterX)+(p.y+ny-_dCenterY)*(p.y+ny-_dCenterY) >
				(p.x-nx-_dCenterX)*(p.x-nx-_dCenterX)+(p.y-ny-_dCenterY)*(p.y-ny-_dCenterY)) {
				nx = -nx;
				ny = -ny;
			}
		}
		// This is the j in the parallel axis theorem
		double j = (-(1+Utils.ELASTICITY) * (vab1x * nx + vab1y * ny)) / 
			(1/_mass + 1/org._mass + Math.pow(rapx * ny - rapy * nx, 2) / _I +
					Math.pow(rbpx * ny - rbpy * nx, 2) / org._I);
		// Final speed
		dx = Utils.between(dx + j*nx/_mass, -Utils.MAX_VEL, Utils.MAX_VEL);
		dy = Utils.between(dy + j*ny/_mass, -Utils.MAX_VEL, Utils.MAX_VEL);
		org.dx = Utils.between(org.dx - j*nx/org._mass, -Utils.MAX_VEL, Utils.MAX_VEL);
		org.dy = Utils.between(org.dy - j*ny/org._mass, -Utils.MAX_VEL, Utils.MAX_VEL);
		dtheta = Utils.between(dtheta + j * (rapx * ny - rapy * nx) / _I, -Utils.MAX_ROT, Utils.MAX_ROT);
		org.dtheta = Utils.between(org.dtheta - j * (rbpx * ny - rbpy * ny) / org._I, -Utils.MAX_ROT, Utils.MAX_ROT);
	}
	/**
	 * Checks if the organism is inside the world. If it is not, calculates its
	 * speed after the collision with the world border.
	 * This calculation should be updated to follow the parallel axis theorem, just
	 * like the collision between two organisms.
	 * 
	 * @return  true if the organism is inside the world, false otherwise.
	 */
	private final boolean isInsideWorld() {
		// Check it is inside the world
		if (x<0 || y<0 || x+width>=_world.getWidth() || y+height>=_world.getHeight()) {
			// Adjust direction
			if (x < 0 || x + width >= _world.getWidth())
				dx = -dx;
			if (y < 0 || y + height >= _world.getHeight())
				dy = -dy;
			dtheta = 0;
			return false;
		}
		return true;
	}
	/**
	 * Moves the organism and rotates it.
	 * 
	 * @param offsetx  displacement on the x axis.
	 * @param offsety  displacement on the y axis.
	 * @param offsettheta  rotation degree.
	 */
	private final void offset(double offsetx, double offsety, double offsettheta) {
		_dCenterX += offsetx; _dCenterY += offsety; _theta += offsettheta;
		_centerX = (int)_dCenterX; _centerY = (int)_dCenterY; 
	}
	/**
	 * Finds if two organism are touching and if so applies the effects of the
	 * collision.
	 * 
	 * @param org  The organism to check for collisions.
	 * @return  true if the two organisms are touching, false otherwise.
	 */
	public final boolean contact(Organism org) {
		int i,j;
		ExLine2DDouble line = new ExLine2DDouble();
		ExLine2DDouble bline = new ExLine2DDouble();
		// Check collisions for all segments
		for (i = _segments-1; i >= 0; i--) {
			// Consider only segments with modulus greater than 1
			if (_m[i]>=1) { 
				line.setLine(x1[i]+_centerX, y1[i]+_centerY, x2[i]+_centerX, y2[i]+_centerY);
				// First check if the line intersects the bounding box of the other organism
				if (org.intersectsLine(line)) {
					// Do the same for the other organism's segments.
					for (j = org._segments-1; j >= 0; j--) {
						if (org._m[j]>=1) {
							bline.setLine(org.x1[j] + org._centerX, org.y1[j] + org._centerY,
									org.x2[j] + org._centerX, org.y2[j] + org._centerY);
							if (intersectsLine(bline) && line.intersectsLine(bline)) {
								// If we found two intersecting segments, apply effects
								touchEffects(org,i,j,true);
								// Intersection point
								Point2D.Double intersec= line.getIntersection(bline);
								/* touchMove needs to know which is the line that collides from the middle (not
								 * from a vertex). Try to guess it by finding the vertex nearest to the
								 * intersection point.
								 */
								double dl1, dl2, dbl1, dbl2;
								dl1 = intersec.distanceSq(line.getP1());
								dl2 = intersec.distanceSq(line.getP2());
								dbl1 = intersec.distanceSq(bline.getP1());
								dbl2 = intersec.distanceSq(bline.getP2());
								// Use this to send the best choice to touchMove
								if (Math.min(dl1, dl2) < Math.min(dbl1, dbl2))
									touchMove(org,intersec,bline,false);
								else
									touchMove(org,intersec,line,true);
								// Find only one collision to speed up.
								return true;
							}
						}
					}
				}
			}
		}
		return false;
	}
	/**
	 * Applies the effects produced by two touching segments.
	 * 
	 * @param org  The organism which is touching.
	 * @param seg  Index of this organism's segment. 
	 * @param oseg  Index of the other organism's segment.
	 * @param firstCall  Indicates if this organism is the one that has detected the collision
	 * or this method is called by this same method in the other organism. 
	 */
	private final void touchEffects(Organism org, int seg, int oseg, boolean firstCall) {
		if ((_parentID == org._ID || _ID == org._parentID) && org.alive)
			return;
		double takenEnergy = 0;
		switch (getTypeColor(_segColor[seg])) {
		case RED:
		// Red segment: try to get energy from the other organism
			// If the other segment is blue, it acts as a shield
			switch (getTypeColor(org._segColor[oseg])) {
			case BLUE:
				if (org.useEnergy(Utils.BLUE_ENERGY_CONSUMPTION)) {
					org.setColor(Color.BLUE);
				} else {
					// Doesn't have energy to use the shield
					if (useEnergy(Utils.RED_ENERGY_CONSUMPTION)) {
						// Get energy depending on segment length
						takenEnergy = Utils.between(_m[seg] * Utils.ORGANIC_OBTAINED_ENERGY, 0, org._energy);
						// The other organism will be shown in yellow
						org.setColor(Color.YELLOW);						
					}	
				}
				break;
			case RED:
				if (useEnergy(Utils.RED_ENERGY_CONSUMPTION)) {
					// Get energy depending on segment length
					takenEnergy = Utils.between(_m[seg] * Utils.ORGANIC_OBTAINED_ENERGY, 0, org._energy);
					// The other organism will be shown in red
					org.setColor(Color.RED);
				}
				break;
			default:
				if (useEnergy(Utils.RED_ENERGY_CONSUMPTION)) {
					// Get energy depending on segment length
					takenEnergy = Utils.between(_m[seg] * Utils.ORGANIC_OBTAINED_ENERGY, 0, org._energy);
					// The other organism will be shown in yellow
					org.setColor(Color.YELLOW);
				}
			}
			// energy interchange
			org._energy -= takenEnergy;
			_energy += takenEnergy;
			double CO2freed = takenEnergy * Utils.ORGANIC_SUBS_PRODUCED;
			useEnergy(CO2freed);
			// This organism will be shown in red
			setColor(Color.RED);
			break;
		case WHITE:
			// White segment: try to infect the other organism
			switch (getTypeColor(org._segColor[oseg])) {
			case BLUE:
				if (org.useEnergy(Utils.BLUE_ENERGY_CONSUMPTION)) {
					setColor(Color.WHITE);
					org.setColor(Color.BLUE);
				} else {
					if (org._infectedGeneticCode != _geneticCode) {
						if (useEnergy(Utils.WHITE_ENERGY_CONSUMPTION)) {
							org.infectedBy(this);
							org.setColor(Color.YELLOW);
							setColor(Color.WHITE);
						}
					}	
				}
				break;
			case BROWN:
				break;
			default:
				if (org._infectedGeneticCode != _geneticCode) {
					if (useEnergy(Utils.WHITE_ENERGY_CONSUMPTION)) {
						org.infectedBy(this);
						org.setColor(Color.YELLOW);
						setColor(Color.WHITE);
					}
				}
			}
			break;
		case GRAY:
			switch (getTypeColor(org._segColor[oseg])) {
			case BLUE:
				if (org.useEnergy(Utils.BLUE_ENERGY_CONSUMPTION)) {
					org.setColor(Color.BLUE);
					setColor(Color.GRAY);
				} else {
					if (useEnergy(Utils.GRAY_ENERGY_CONSUMPTION)) {
						org.die(this);
						setColor(Color.GRAY);
					}
				}
				break;
			case BROWN:
				break;
			default:
				if (useEnergy(Utils.GRAY_ENERGY_CONSUMPTION)) {
					org.die(this);
					setColor(Color.GRAY);
				}
			}
			break;
		}
		// Check if the other organism has died
		if (org.isAlive() && org._energy < Utils.tol) {
			org.die(this);
		}
		if (firstCall)
			org.touchEffects(this, oseg, seg, false);
	}
	
	/*
	 * Perd velocitat pel fregament.
	 */
	private final void rubbingFramesEffects() {
		dx *= Utils.RUBBING;
		if (Math.abs(dx) < Utils.tol) dx=0;
		dy *= Utils.RUBBING;
		if (Math.abs(dy) < Utils.tol) dy = 0;
		dtheta *= Utils.RUBBING;
		if (Math.abs(dtheta) < Utils.tol) dtheta = 0;
	}
	
	/*
	 * Perd el cost de manteniment dels segments
	 * Aplica l'efecte de cadascun dels segments
	 */
	private final void segmentsFrameEffects() {
		if (alive) {
			int i;
			// Energy obtained through photosynthesis
			double photosynthesis = 0;
			_nChildren = 1;
			for (i=_segments-1; i>=0; i--) {
				// Manteniment
				switch (getTypeColor(_segColor[i])) {
				// 	Segments cilis
				case CYAN:
					if (Utils.random.nextInt(100)<8 && useEnergy(Utils.CYAN_ENERGY_CONSUMPTION)) {
						dx=Utils.between(dx+12d*(x2[i]-x1[i])/_mass, -Utils.MAX_VEL, Utils.MAX_VEL);
						dy=Utils.between(dy+12d*(y2[i]-y1[i])/_mass, -Utils.MAX_VEL, Utils.MAX_VEL);
						dtheta=Utils.between(dtheta+Utils.randomSign()*_m[i]*Math.PI/_I, -Utils.MAX_ROT, Utils.MAX_ROT);
					}
					break;
					// Segments fotosint�tics
				case GREEN:
					if (useEnergy(Utils.GREEN_ENERGY_CONSUMPTION))
						photosynthesis += _m[i];
					break;
					// Segments que obtenen energia de subs1
					// 	Segments relacionats amb la fertilitat
				case YELLOW:
					_nChildren++;
					break;
				}
			}
			// Photosynthesis process
			//Get sun's energy
			_energy += _world.photosynthesis(photosynthesis);
		}
	}
	
	private static final int NOCOLOR=-1;
	private static final int GREEN=0;
	private static final int RED=1;
	private static final int CYAN=2;
	private static final int BLUE=3;
	private static final int MAGENTA=4;
	private static final int PINK=5;
	private static final int ORANGE=6;
	private static final int WHITE=7;
	private static final int GRAY=8;
	private static final int YELLOW=9;
	private static final int BROWN=10;
	private static final int getTypeColor(Color c) {
		if (c.equals(Color.RED) || c.equals(Utils.ColorDARK_RED))
			return RED;
		if (c.equals(Color.GREEN) || c.equals(Utils.ColorDARK_GREEN))
			return GREEN;
		if (c.equals(Color.CYAN) || c.equals(Utils.ColorDARK_CYAN))
			return CYAN;
		if (c.equals(Color.BLUE) || c.equals(Utils.ColorDARK_BLUE))
			return BLUE;
		if (c.equals(Color.MAGENTA) || c.equals(Utils.ColorDARK_MAGENTA))
			return MAGENTA;
		if (c.equals(Color.PINK) || c.equals(Utils.ColorDARK_PINK))
			return PINK;
		if (c.equals(Color.ORANGE) || c.equals(Utils.ColorDARK_ORANGE))
			return ORANGE;
		if (c.equals(Color.WHITE) || c.equals(Utils.ColorDARK_WHITE))
			return WHITE;
		if (c.equals(Color.GRAY) || c.equals(Utils.ColorDARK_GRAY))
			return GRAY;
		if (c.equals(Color.YELLOW) || c.equals(Utils.ColorDARK_YELLOW))
			return YELLOW;
		if (c.equals(Utils.ColorBROWN))
			return BROWN;
		return NOCOLOR;
	}
	
	private final void setColor(Color c) {
		_color = c;
		_framesColor = 10;
	}
	
	public BufferedImage getImage() {
		BufferedImage image = new BufferedImage(width,height,BufferedImage.TYPE_INT_RGB);
		Graphics2D g = image.createGraphics();
		g.setBackground(Color.BLACK);
		g.clearRect(0,0,width,height);
		for (int i=_segments-1; i>=0; i--) {
				g.setColor(_segColor[i]);
				g.drawLine(x1[i] -x + _centerX, y1[i] - y + _centerY, x2[i] - x + _centerX, y2[i] - y+_centerY);
		}
		return image;
	}
}
