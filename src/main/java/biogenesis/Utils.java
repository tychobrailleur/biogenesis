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

import java.util.*;
import java.util.prefs.*;
import java.awt.*;

import javax.swing.JFrame;
import javax.swing.JOptionPane;

/**
 * This class contains all global program parameters and a few useful methods for
 * reading or writing them from the user preferences.
 */
public final class Utils {
	/**
	 * This value indicates the version of the program.
	 * There are two digits for version, two for subversion and two for revision, so
	 * a value of 400 or 000400 means version 0, subversion 4 and revision 0.
	 * 
	 * All serializable classes use this value as their serialVersionUID.
	 */
	static final int FILE_VERSION = 700;
	static final int VERSION = 800; //two digits for version, subversion and revision

	// Default values for parameters
	final static int DEF_WINDOW_X = 0;
	final static int DEF_WINDOW_Y = 0;
	final static int DEF_WINDOW_WIDTH = 640;
	final static int DEF_WINDOW_HEIGHT = 480;
	final static int DEF_WINDOW_STATE = JFrame.NORMAL;
	/**
	 * This is the default value of organisms that are created when a new world begins.
	 */
	final static int DEF_INITIAL_ORGANISMS = 15;
	/**
	 * This is the default amount of O2 that exists in a newly created world.
	 */
	final static double DEF_INITIAL_O2 = 0;
	/**
	 * This is the default amount of CO2 that exists in a newly created world.
	 */
	final static double DEF_INITIAL_CO2 = 5000;
	/**
	 * This is the initial size of the organisms vector.
	 */
	final static int DEF_ORGANISMS_VECTOR_SIZE = 50;
	/**
	 * This is the default world's width.
	 */
	final static int DEF_WORLD_WIDTH = 1000;
	/**
	 * This is the default world's height.
	 */
	final static int DEF_WORLD_HEIGHT = 1000;
	/**
	 * This is the default maximum age than an organism can achieve. From it,
	 * the organism quickly decays.
	 */
	final static int DEF_MAX_AGE = 30;
	/**
	 * This is the default rubbing coefficient that is applied to movements. This value is
	 * multiplied by the speed at every frame.
	 */
	final static double DEF_RUBBING = 0.98d;
	/**
	 * This is the default probability that an specified gene has to be modified when
	 * reproducing organisms. 
	 */
	final static double DEF_MUTATION_RATE = 0.05d;
	/**
	 * This default value is used to calculate the energy cost that an organism must
	 * pay to maintain a segment. It spends the length of the segment divided by this
	 * number units of energy.  
	 */
	final static int DEF_SEGMENT_COST_DIVISOR = 5000;
	/**
	 * This default value is multiplied by the length of red segments to calculate
	 * the amount of energy that is stolen from an organism when it is touched.
	 */
	final static double DEF_ORGANIC_OBTAINED_ENERGY = 0.5d;
	/**
	 * This default value divides the length of green segments to calculate the
	 * amount of solar energy that the segment can get in a frame.
	 */
	final static int DEF_GREEN_OBTAINED_ENERGY_DIVISOR = 500;
	/**
	 * This is the default value that determines how much energy will be released
	 * to the atmosphere when an organism touches another organism with a red segment.
	 * The energy that the target lost is multiplied by this value and the result
	 * is added to the atmospheric CO2. The rest is added to the attacker's energy.
	 */
	final static double DEF_ORGANIC_SUBS_PRODUCED = 0.1d;
	/**
	 * This is the default energy that is consumed when a red segment is used.
	 */
	final static double DEF_RED_ENERGY_CONSUMPTION = 0d;
	/**
	 * This is the default energy that is consumed when a green segment is used.
	 */
	final static double DEF_GREEN_ENERGY_CONSUMPTION = 0d;
	/**
	 * This is the default energy that is consumed when a blue segment is used.
	 */
	final static double DEF_BLUE_ENERGY_CONSUMPTION = 0d;
	/**
	 * This is the default energy that is consumed when a cyan segment is used.
	 */
	final static double DEF_CYAN_ENERGY_CONSUMPTION = 0d;
	/**
	 * This is the default energy that is consumed when a white segment is used.
	 */
	final static double DEF_WHITE_ENERGY_CONSUMPTION = 1d;
	/**
	 * This is the default energy that is consumed when a gray segment is used.
	 */
	final static double DEF_GRAY_ENERGY_CONSUMPTION = 1d;
	/**
	 * This is the default energy that is consumed when a yellow segment is used.
	 */
	final static double DEF_YELLOW_ENERGY_CONSUMPTION = 0d;
	/**
	 * This is the default probability for a new segment to be red.
	 */
	final static int DEF_RED_PROB = 10;
	/**
	 * This is the default probability for a new segment to be green.
	 */
	final static int DEF_GREEN_PROB = 30;
	/**
	 * This is the default probability for a new segment to be blue.
	 */
	final static int DEF_BLUE_PROB = 10;
	/**
	 * This is the default probability for a new segment to be cyan.
	 */
	final static int DEF_CYAN_PROB = 20;
	/**
	 * This is the default probability for a new segment to be white.
	 */
	final static int DEF_WHITE_PROB = 10;
	/**
	 * This is the default probability for a new segment to be gray.
	 */
	final static int DEF_GRAY_PROB = 10;
	/**
	 * This is the default probability for a new segment to be yellow.
	 */
	final static int DEF_YELLOW_PROB = 10;
	/**
	 * This default value divides the amount of CO2 in the atmosphere to
	 * establish how many CO2 an organism can drain in a frame.
	 */
	final static int DEF_DRAIN_SUBS_DIVISOR = 5000;
	/**
	 * This is the default value for the initial energy that an organism has
	 * if it isn't a child of another organism.
	 */
	final static int DEF_INITIAL_ENERGY = 50;
	/**
	 * This is the default value for the maximum speed that an organism can
	 * achieve.
	 */
	final static double DEF_MAX_VEL = 5d;
	/**
	 * This is the default value for the maximum rotational speed that an organism
	 * can achieve.
	 */
	final static double DEF_MAX_ROT = Math.PI / 16d;
	/**
	 * This is the default elasticity coefficient. This value is used to establish
	 * the energy that a movement keeps after a collision.
	 */
	final static double DEF_ELASTICITY = 0.8d;
	/**
	 * This is the default number of miliseconds that pas between frames.
	 */
	final static int DEF_DELAY = 50;
	/**
	 * This is the default port where the net server will listen for connections.
	 */
	final static int DEF_LOCAL_PORT = 8888;
	/**
	 * This is the default value for the maximum number of network connections allowed.
	 */
	final static int DEF_MAX_CONNECTIONS = 1;
	/**
	 * This is the default value for accepting or not new connections from other hosts.
	 */
	final static boolean DEF_ACCEPT_CONNECTIONS = false;
	/**
	 * This is the default value for using or not a meta-server to find other instances
	 * of biogenesis running. At the moment it is not used.
	 */
	final static boolean DEF_CONNECT_TO_SERVER = false;
	/**
	 * This is the default IP that the meta-server will have. At the moment it is not used.
	 */
	final static String DEF_SERVER_ADDRESS = ""; //$NON-NLS-1$
	/**
	 * This is the default port where the meta-server will listen. At the moment it is not used.
	 */
	final static int DEF_SERVER_PORT = 0;
	/**
	 * This is the default hardware acceleration applied when drawing
	 */
	final static int DEF_HARDWARE_ACCELERATION = 0; //0 none, 1 try opengl, 2 opengl
	
	final static double DEF_DECAY_ENERGY = 0.1d;
	// Effective parameters values
	static int WINDOW_X = DEF_WINDOW_X;
	static int WINDOW_Y = DEF_WINDOW_Y;
	static int WINDOW_WIDTH = DEF_WINDOW_WIDTH;
	static int WINDOW_HEIGHT = DEF_WINDOW_HEIGHT;
	static int WINDOW_STATE = DEF_WINDOW_STATE;
	/**
	 * This is the effective value of organisms that are created when a new world begins.
	 */
	static int INITIAL_ORGANISMS = DEF_INITIAL_ORGANISMS;
	/**
	 * This is the effective amount of O2 that exists in a newly created world.
	 */
	static double INITIAL_O2 = DEF_INITIAL_O2;
	/**
	 * This is the effective amount of CO2 that exists in a newly created world.
	 */
	static double INITIAL_CO2 = DEF_INITIAL_CO2;
	/**
	 * This is the effective size of the organisms vector.
	 */
	static int ORGANISMS_VECTOR_SIZE = DEF_ORGANISMS_VECTOR_SIZE;
	/**
	 * This is the effective world's width for new worlds.
	 */
	static int WORLD_WIDTH = DEF_WORLD_WIDTH;
	/**
	 * This is the effective world's height for new worlds.
	 */
	static int WORLD_HEIGHT = DEF_WORLD_HEIGHT;
	/**
	 * This is the maximum age than an organism can achieve. From it, the organism
	 * quickly decays.
	 */
	static int MAX_AGE = DEF_MAX_AGE;
	/**
	 * This is the rubbing coefficient that is applied to movements. This value is
	 * multiplied by the speed at every frame.
	 */
	static double RUBBING = DEF_RUBBING;
	/**
	 * This is the probability that an specified gene has to be modified when
	 * reproducing organisms. 
	 */
	static double MUTATION_RATE = DEF_MUTATION_RATE;
	/**
	 * This value is used to calculate the energy cost that an organism must
	 * pay to maintain a segment. It spends the length of the segment divided by this
	 * number units of energy.  
	 */
	static int SEGMENT_COST_DIVISOR = DEF_SEGMENT_COST_DIVISOR;
	/**
	 * This value is multiplied by the length of red segments to calculate
	 * the amount of energy that is stolen from an organism when it is touched.
	 */
	static double ORGANIC_OBTAINED_ENERGY = DEF_ORGANIC_OBTAINED_ENERGY;
	/**
	 * This value divides the length of green segments to calculate the
	 * amount of solar energy that the segment can get in a frame.
	 */
	static int GREEN_OBTAINED_ENERGY_DIVISOR = DEF_GREEN_OBTAINED_ENERGY_DIVISOR;
	/**
	 * This is the effective value that determines how much energy will be released
	 * to the atmosphere when an organism touches another organism with a red segment.
	 * The energy that the target lost is multiplied by this value and the result
	 * is added to the atmospheric CO2. The rest is added to the attacker's energy.
	 */
	static double ORGANIC_SUBS_PRODUCED = DEF_ORGANIC_SUBS_PRODUCED;
	/**
	 * This is the energy that is consumed when a red segment is used.
	 */
	static double RED_ENERGY_CONSUMPTION = DEF_RED_ENERGY_CONSUMPTION;
	/**
	 * This is the energy that is consumed when a green segment is used.
	 */
	static double GREEN_ENERGY_CONSUMPTION = DEF_GREEN_ENERGY_CONSUMPTION;
	/**
	 * This is the energy that is consumed when a blue segment is used.
	 */
	static double BLUE_ENERGY_CONSUMPTION = DEF_BLUE_ENERGY_CONSUMPTION;
	/**
	 * This is the energy that is consumed when a cyan segment is used.
	 */
	static double CYAN_ENERGY_CONSUMPTION = DEF_CYAN_ENERGY_CONSUMPTION;
	/**
	 * This is the energy that is consumed when a white segment is used.
	 */
	static double WHITE_ENERGY_CONSUMPTION = DEF_WHITE_ENERGY_CONSUMPTION;
	/**
	 * This is the energy that is consumed when a gray segment is used.
	 */
	static double GRAY_ENERGY_CONSUMPTION = DEF_GRAY_ENERGY_CONSUMPTION;
	/**
	 * This is the energy that is consumed when a yellow segment is used.
	 */
	static double YELLOW_ENERGY_CONSUMPTION = DEF_YELLOW_ENERGY_CONSUMPTION;
	/**
	 * This is the probability for a new segment to be red.
	 */
	static int RED_PROB = DEF_RED_PROB;
	/**
	 * This is the probability for a new segment to be green.
	 */
	static int GREEN_PROB = DEF_GREEN_PROB;
	/**
	 * This is the probability for a new segment to be blue.
	 */
	static int BLUE_PROB = DEF_BLUE_PROB;
	/**
	 * This is the probability for a new segment to be cyan.
	 */
	static int CYAN_PROB = DEF_CYAN_PROB;
	/**
	 * This is the probability for a new segment to be white.
	 */
	static int WHITE_PROB = DEF_WHITE_PROB;
	/**
	 * This is the probability for a new segment to be gray.
	 */
	static int GRAY_PROB = DEF_GRAY_PROB;
	/**
	 * This is the probability for a new segment to be yellow.
	 */
	static int YELLOW_PROB = DEF_YELLOW_PROB;
	/**
	 * This value divides the amount of CO2 in the atmosphere to
	 * establish how many CO2 an organism can drain in a frame.
	 */
	static int DRAIN_SUBS_DIVISOR = DEF_DRAIN_SUBS_DIVISOR;
	/**
	 * This is the value for the initial energy that an organism has
	 * if it isn't a child of another organism.
	 */
	static int INITIAL_ENERGY = DEF_INITIAL_ENERGY;
	/**
	 * This is the value for the maximum speed that an organism can
	 * achieve.
	 */
	static double MAX_VEL = DEF_MAX_VEL;
	/**
	 * This is the value for the maximum rotational speed that an organism
	 * can achieve.
	 */
	static double MAX_ROT = DEF_MAX_ROT;
	/**
	 * This is the elasticity coefficient. This value is used to establish
	 * the energy that a movement keeps after a collision.
	 */
	static double ELASTICITY = DEF_ELASTICITY;
	/**
	 * This is the number of miliseconds that pas between frames.
	 */
	static int DELAY = DEF_DELAY;
	/**
	 * This is the port where the net server will listen for connections.
	 */
	static int LOCAL_PORT = DEF_LOCAL_PORT;
	/**
	 * This is the value for the maximum number of network connections allowed.
	 */
	static int MAX_CONNECTIONS = DEF_MAX_CONNECTIONS;
	/**
	 * This is the value for accepting or not new connections from other hosts.
	 */
	static boolean ACCEPT_CONNECTIONS = DEF_ACCEPT_CONNECTIONS;
	/**
	 * This is the value for using or not a meta-server to find other instances
	 * of biogenesis running. At the moment it is not used.
	 */
	static boolean CONNECT_TO_SERVER = DEF_CONNECT_TO_SERVER;
	/**
	 * This is the IP that the meta-server will have. At the moment it is not used.
	 */
	static String SERVER_ADDRESS = DEF_SERVER_ADDRESS;
	/**
	 * This is the hardware acceleration applied when drawing.
	 * 0 none, 1 try opengl next time, 2 trying opengl, 3 opengl
	 * 4 try opengl without fbobject next time, 5 trying opengl without fbobject,
	 * 6 opengl without fbobject
	 */
	static int HARDWARE_ACCELERATION = DEF_HARDWARE_ACCELERATION;
	/**
	 * This is the port where the meta-server will listen. At the moment it is not used.
	 */
	static int SERVER_PORT = DEF_SERVER_PORT;
	
	static double DECAY_ENERGY = DEF_DECAY_ENERGY;
	/**
	 * Tolerance. Smaller numbers are considered equal to 0.
	 */
	static final double tol = 0.0000001;
	/**
	 * Indicates the eight possible directions. The row is the direction we want, from 0 to 7, first
	 * column is the x coordinate and second column the y coordinate.  
	 */
	static final int side[][] = {{1,0},{1,1},{0,1},{-1,1},{-1,0},{-1,-1},{0,-1},{1,-1}};
	/**
	 * These are the scale factor applied to segments depending on the growth rate of the
	 * organism. Segment length is divided by scale[i], where i is the growth rate.
	 * scale[0] indicates that the organism is fully developed and scale[15] that it has just
	 * been born. 
	 */
	static final double scale[] = {1.00, 1.12, 1.25, 1.40, 1.57, 1.76, 1.97, 2.21,
        2.47, 2.77, 3.11, 3.48, 3.90, 4.36, 4.89, 5.47};
	
	/**
	 * Precalculated dark green color
	 */
	static final Color ColorDARK_GREEN = Color.GREEN.darker();
	/**
	 * Precalculated dark red color
	 */
	static final Color ColorDARK_RED = Color.RED.darker();
	/**
	 * Precalculated dark cyan color
	 */
	static final Color ColorDARK_CYAN = Color.CYAN.darker();
	/**
	 * Precalculated dark blue color
	 */
	static final Color ColorDARK_BLUE = Color.BLUE.darker();
	/**
	 * Precalculated dark magenta color
	 */
	static final Color ColorDARK_MAGENTA = Color.MAGENTA.darker();
	/**
	 * Precalculated dark pink color
	 */
	static final Color ColorDARK_PINK = Color.PINK.darker();
	/**
	 * Precalculated dark orange color
	 */
	static final Color ColorDARK_ORANGE = Color.ORANGE.darker();
	/**
	 * Precalculated dark white color
	 */
	static final Color ColorDARK_WHITE = Color.WHITE.darker();
	/**
	 * Precalculated dark gray color
	 */
	static final Color ColorDARK_GRAY = Color.GRAY.darker();
	/**
	 * Precalculated dark yellow color
	 */
	static final Color ColorDARK_YELLOW = Color.YELLOW.darker();
	/**
	 * Precalculated brown color
	 */
	static final Color ColorBROWN = new Color(150,75,0);
	/**
	 * Precalculated light blue color
	 */
	static final Color ColorLIGHT_BLUE = new Color(0,0,100);
	/**
	 * Precalculated light red color
	 */
	static final Color ColorLIGHT_RED = new Color(100,0,0);
	/**
	 * Used through all program to calculate random numbers
	 */
	public static Random random = new Random();
	/**
	 * Used to get a random -1 or 1 to create numbers with random sign. 
	 * 
	 * @return  a random -1 or 1
	 */
	public static final int randomSign() {
		return (random.nextInt(2)<<1)-1;
	}
	/**
	 * Calculates the minimum of three integers
	 * 
	 * @param a
	 * @param b
	 * @param c
	 * @return  The minimum of a, b, and c
	 */
	public static final int min(int a, int b, int c) {
		return Math.min(Math.min(a,b),c);
	}
	/**
	 * Calculates the minimum of three doubles
	 * 
	 * @param a
	 * @param b
	 * @param c
	 * @return  The minimum of a, b, and c
	 */
	public static final double min(double a, double b, double c) {
		return Math.min(Math.min(a,b),c);
	}
	/**
	 * Calculates the maximum of three integers
	 * 
	 * @param a
	 * @param b
	 * @param c
	 * @return  The maximum of a, b, and c
	 */
	public static final int max(int a, int b, int c) {
		return Math.max(Math.max(a,b),c);
	}
	/**
	 * Calculates the maximum of three doubles 
	 * 
	 * @param a
	 * @param b
	 * @param c
	 * @return  The maximum of a, b, and c
	 */
	public static final double max(double a, double b, double c) {
		return Math.max(Math.max(a,b),c);
	}
	/**
	 * Return min if value<min, max if value>max and value otherwise.
	 * 
	 * @param value
	 * @param min
	 * @param max
	 * @return  min if value<min, max if value>max and value otherwise
	 */
	public static final int between(int value, int min, int max) {
		return Math.max(Math.min(max, value), min);
	}
	/**
	 * Return min if value<min, max if value>max and value otherwise.
	 * 
	 * @param value
	 * @param min
	 * @param max
	 * @return  min if value<min, max if value>max and value otherwise
	 */
	public static final double between(double value, double min, double max) {
		return Math.max(Math.min(max, value), min);
	}
	/**
	 * Check if a mutation is produced or not, using a random number.
	 * 
	 * @return  true if a mutations is produced and false otherwise
	 */
	public static final boolean randomMutation() {
		if (random.nextDouble() < MUTATION_RATE)
			return true;
		return false;
	}
	/**
	 * Return the localized name of a color.
	 * 
	 * @param c  A color
	 * @return  A String representing the name of the color
	 */
	public static final String colorToString(Color c) {
		if (c.equals(Color.RED)) return Messages.getString("T_RED"); //$NON-NLS-1$
		if (c.equals(Color.GREEN)) return Messages.getString("T_GREEN"); //$NON-NLS-1$
		if (c.equals(Color.BLUE)) return Messages.getString("T_BLUE"); //$NON-NLS-1$
		if (c.equals(Color.CYAN)) return Messages.getString("T_CYAN"); //$NON-NLS-1$
		if (c.equals(Color.MAGENTA)) return Messages.getString("T_MAGENTA"); //$NON-NLS-1$
		if (c.equals(Color.PINK)) return Messages.getString("T_PINK"); //$NON-NLS-1$
		if (c.equals(Color.ORANGE)) return Messages.getString("T_ORANGE"); //$NON-NLS-1$
		if (c.equals(Color.WHITE)) return Messages.getString("T_WHITE"); //$NON-NLS-1$
		if (c.equals(Color.GRAY)) return Messages.getString("T_GRAY"); //$NON-NLS-1$
		if (c.equals(Color.YELLOW)) return Messages.getString("T_YELLOW"); //$NON-NLS-1$
		return ""; //$NON-NLS-1$
	}
	/**
	 * Save user preferences to disk
	 */
	public static final void savePreferences() {
		try {
			Preferences prefs = Preferences.userNodeForPackage(Utils.class);
			prefs.putInt("VERSION",FILE_VERSION); //$NON-NLS-1$
			prefs.putInt("INITIAL_ORGANISMS",INITIAL_ORGANISMS); //$NON-NLS-1$
			prefs.putDouble("INITIAL_O2",INITIAL_O2); //$NON-NLS-1$
			prefs.putDouble("INITIAL_CO2",INITIAL_CO2); //$NON-NLS-1$
			prefs.putInt("ORGANISMS_VECTOR_SIZE",ORGANISMS_VECTOR_SIZE); //$NON-NLS-1$
			prefs.putInt("WORLD_WIDTH",WORLD_WIDTH); //$NON-NLS-1$
			prefs.putInt("WORLD_HEIGHT",WORLD_HEIGHT); //$NON-NLS-1$
			prefs.putInt("MAX_AGE",MAX_AGE); //$NON-NLS-1$
			prefs.putDouble("RUBBING",RUBBING); //$NON-NLS-1$
			prefs.putDouble("MUTATION_RATE",MUTATION_RATE); //$NON-NLS-1$
			prefs.putInt("SEGMENT_COST_DIVISOR",SEGMENT_COST_DIVISOR); //$NON-NLS-1$
			prefs.putDouble("ORGANIC_OBTAINED_ENERGY",ORGANIC_OBTAINED_ENERGY); //$NON-NLS-1$
			prefs.putInt("GREEN_OBTAINED_ENERGY_DIVISOR",GREEN_OBTAINED_ENERGY_DIVISOR); //$NON-NLS-1$
			prefs.putDouble("ORGANIC_SUBS_PRODUCED",ORGANIC_SUBS_PRODUCED); //$NON-NLS-1$
			prefs.putDouble("RED_ENERGY_CONSUMPTION",RED_ENERGY_CONSUMPTION); //$NON-NLS-1$
			prefs.putDouble("GREEN_ENERGY_CONSUMPTION",GREEN_ENERGY_CONSUMPTION); //$NON-NLS-1$
			prefs.putDouble("BLUE_ENERGY_CONSUMPTION",BLUE_ENERGY_CONSUMPTION); //$NON-NLS-1$
			prefs.putDouble("CYAN_ENERGY_CONSUMPTION",CYAN_ENERGY_CONSUMPTION); //$NON-NLS-1$
			prefs.putDouble("WHITE_ENERGY_CONSUMPTION",WHITE_ENERGY_CONSUMPTION); //$NON-NLS-1$
			prefs.putDouble("GRAY_ENERGY_CONSUMPTION",GRAY_ENERGY_CONSUMPTION); //$NON-NLS-1$
			prefs.putDouble("YELLOW_ENERGY_CONSUMPTION",YELLOW_ENERGY_CONSUMPTION); //$NON-NLS-1$
			prefs.putInt("RED_PROB",RED_PROB); //$NON-NLS-1$
			prefs.putInt("GREEN_PROB",GREEN_PROB); //$NON-NLS-1$
			prefs.putInt("BLUE_PROB",BLUE_PROB); //$NON-NLS-1$
			prefs.putInt("CYAN_PROB",CYAN_PROB); //$NON-NLS-1$
			prefs.putInt("WHITE_PROB",WHITE_PROB); //$NON-NLS-1$
			prefs.putInt("GRAY_PROB",GRAY_PROB); //$NON-NLS-1$
			prefs.putInt("YELLOW_PROB",YELLOW_PROB); //$NON-NLS-1$
			prefs.putInt("DRAIN_SUBS_DIVISOR",DRAIN_SUBS_DIVISOR); //$NON-NLS-1$
			prefs.putInt("INITIAL_ENERGY",INITIAL_ENERGY); //$NON-NLS-1$
			prefs.putDouble("MAX_VEL",MAX_VEL); //$NON-NLS-1$
			prefs.putDouble("MAX_ROT",MAX_ROT); //$NON-NLS-1$
			prefs.putDouble("ELASTICITY",ELASTICITY); //$NON-NLS-1$
			prefs.putInt("DELAY",DELAY); //$NON-NLS-1$
			prefs.putInt("LOCAL_PORT",LOCAL_PORT); //$NON-NLS-1$
			prefs.putBoolean("ACCEPT_CONNECTIONS",ACCEPT_CONNECTIONS); //$NON-NLS-1$
			prefs.putBoolean("CONNECT_TO_SERVER",CONNECT_TO_SERVER); //$NON-NLS-1$
			prefs.put("SERVER_ADDRESS",SERVER_ADDRESS); //$NON-NLS-1$
			prefs.putInt("SERVER_PORT",SERVER_PORT); //$NON-NLS-1$
			prefs.putInt("MAX_CONNECTIONS",MAX_CONNECTIONS); //$NON-NLS-1$
			prefs.putInt("HARDWARE_ACCELERATION", HARDWARE_ACCELERATION); //$NON-NLS-1$
			prefs.putDouble("DECAY_ENERGY", DECAY_ENERGY); //$NON-NLS-1$
			prefs.put("LOCALE",Messages.getLanguage()); //$NON-NLS-1$
		}
		catch (SecurityException ex) {
			// If we can't write, don't do it
		}
	}
	/**
	 * Read user preferences from disc
	 */
	public static final void readPreferences() {
		try {
			Preferences prefs = Preferences.userNodeForPackage(Utils.class);
			int previous_version = prefs.getInt("VERSION",0); //$NON-NLS-1$
			if (previous_version != FILE_VERSION)
				try {
					prefs.clear();
				} catch (BackingStoreException e) {
					//do nothing
				}
			WINDOW_X = prefs.getInt("WINDOW_X", DEF_WINDOW_X);
			WINDOW_Y = prefs.getInt("WINDOW_Y", DEF_WINDOW_Y);
			WINDOW_WIDTH = prefs.getInt("WINDOW_WIDTH", DEF_WINDOW_WIDTH);
			WINDOW_HEIGHT = prefs.getInt("WINDOW_HEIGHT", DEF_WINDOW_HEIGHT);
			WINDOW_STATE = prefs.getInt("WINDOW_STATE", DEF_WINDOW_STATE);
			INITIAL_ORGANISMS = prefs.getInt("INITIAL_ORGANISMS",DEF_INITIAL_ORGANISMS); //$NON-NLS-1$
			INITIAL_O2 = prefs.getDouble("INITIAL_O2",DEF_INITIAL_O2); //$NON-NLS-1$
			INITIAL_CO2 = prefs.getDouble("INITIAL_CO2",DEF_INITIAL_CO2); //$NON-NLS-1$
			ORGANISMS_VECTOR_SIZE = prefs.getInt("ORGANISMS_VECTOR_SIZE",DEF_ORGANISMS_VECTOR_SIZE); //$NON-NLS-1$
			WORLD_WIDTH = prefs.getInt("WORLD_WIDTH",DEF_WORLD_WIDTH); //$NON-NLS-1$
			WORLD_HEIGHT = prefs.getInt("WORLD_HEIGHT",DEF_WORLD_HEIGHT); //$NON-NLS-1$
			MAX_AGE = prefs.getInt("MAX_AGE",DEF_MAX_AGE); //$NON-NLS-1$
			RUBBING = prefs.getDouble("RUBBING",DEF_RUBBING); //$NON-NLS-1$
			MUTATION_RATE = prefs.getDouble("MUTATION_RATE",DEF_MUTATION_RATE); //$NON-NLS-1$
			SEGMENT_COST_DIVISOR = prefs.getInt("SEGMENT_COST_DIVISOR",DEF_SEGMENT_COST_DIVISOR); //$NON-NLS-1$
			ORGANIC_OBTAINED_ENERGY = prefs.getDouble("ORGANIC_OBTAINED_ENERGY",DEF_ORGANIC_OBTAINED_ENERGY); //$NON-NLS-1$
			GREEN_OBTAINED_ENERGY_DIVISOR = prefs.getInt("GREEN_OBTAINED_ENERGY_DIVISOR",DEF_GREEN_OBTAINED_ENERGY_DIVISOR); //$NON-NLS-1$
			ORGANIC_SUBS_PRODUCED = prefs.getDouble("ORGANIC_SUBS_PRODUCED",DEF_ORGANIC_SUBS_PRODUCED); //$NON-NLS-1$
			RED_ENERGY_CONSUMPTION = prefs.getDouble("RED_ENERGY_CONSUMPTION",DEF_RED_ENERGY_CONSUMPTION); //$NON-NLS-1$
			GREEN_ENERGY_CONSUMPTION = prefs.getDouble("GREEN_ENERGY_CONSUMPTION",DEF_GREEN_ENERGY_CONSUMPTION); //$NON-NLS-1$
			BLUE_ENERGY_CONSUMPTION = prefs.getDouble("BLUE_ENERGY_CONSUMPTION",DEF_BLUE_ENERGY_CONSUMPTION); //$NON-NLS-1$
			CYAN_ENERGY_CONSUMPTION = prefs.getDouble("CYAN_ENERGY_CONSUMPTION",DEF_CYAN_ENERGY_CONSUMPTION); //$NON-NLS-1$
			WHITE_ENERGY_CONSUMPTION = prefs.getDouble("WHITE_ENERGY_CONSUMPTION",DEF_WHITE_ENERGY_CONSUMPTION); //$NON-NLS-1$
			GRAY_ENERGY_CONSUMPTION = prefs.getDouble("GRAY_ENERGY_CONSUMPTION",DEF_GRAY_ENERGY_CONSUMPTION); //$NON-NLS-1$
			YELLOW_ENERGY_CONSUMPTION = prefs.getDouble("YELLOW_ENERGY_CONSUMPTION",DEF_YELLOW_ENERGY_CONSUMPTION); //$NON-NLS-1$
			RED_PROB = prefs.getInt("RED_PROB",DEF_RED_PROB); //$NON-NLS-1$
			GREEN_PROB = prefs.getInt("GREEN_PROB",DEF_GREEN_PROB); //$NON-NLS-1$
			BLUE_PROB = prefs.getInt("BLUE_PROB",DEF_BLUE_PROB); //$NON-NLS-1$
			CYAN_PROB = prefs.getInt("CYAN_PROB",DEF_CYAN_PROB); //$NON-NLS-1$
			WHITE_PROB = prefs.getInt("WHITE_PROB",DEF_WHITE_PROB); //$NON-NLS-1$
			GRAY_PROB = prefs.getInt("GRAY_PROB",DEF_GRAY_PROB); //$NON-NLS-1$
			YELLOW_PROB = prefs.getInt("YELLOW_PROB",DEF_YELLOW_PROB); //$NON-NLS-1$
			DRAIN_SUBS_DIVISOR = prefs.getInt("DRAIN_SUBS_DIVISOR",DEF_DRAIN_SUBS_DIVISOR); //$NON-NLS-1$
			INITIAL_ENERGY = prefs.getInt("INITIAL_ENERGY",DEF_INITIAL_ENERGY); //$NON-NLS-1$
			MAX_VEL = prefs.getDouble("MAX_VEL",DEF_MAX_VEL); //$NON-NLS-1$
			MAX_ROT = prefs.getDouble("MAX_ROT",DEF_MAX_ROT); //$NON-NLS-1$
			ELASTICITY = prefs.getDouble("ELASTICITY",DEF_ELASTICITY); //$NON-NLS-1$
			DELAY = prefs.getInt("DELAY",DEF_DELAY); //$NON-NLS-1$
			LOCAL_PORT = prefs.getInt("LOCAL_PORT",DEF_LOCAL_PORT); //$NON-NLS-1$
			MAX_CONNECTIONS = prefs.getInt("MAX_CONNECTIONS",DEF_MAX_CONNECTIONS); //$NON-NLS-1$
			ACCEPT_CONNECTIONS = prefs.getBoolean("ACCEPT_CONNECTIONS",DEF_ACCEPT_CONNECTIONS); //$NON-NLS-1$
			CONNECT_TO_SERVER = prefs.getBoolean("CONNECT_TO_SERVER",DEF_CONNECT_TO_SERVER); //$NON-NLS-1$
			SERVER_ADDRESS = prefs.get("SERVER_ADDRESS",DEF_SERVER_ADDRESS); //$NON-NLS-1$
			SERVER_PORT = prefs.getInt("SERVER_PORT",DEF_SERVER_PORT); //$NON-NLS-1$
			DECAY_ENERGY = prefs.getDouble("DECAY_ENERGY", DEF_DECAY_ENERGY); //$NON-NLS-1$
			setHardwareAcceleration(prefs.getInt("HARDWARE_ACCELERATION", DEF_HARDWARE_ACCELERATION)); //$NON-NLS-1$
			if (HARDWARE_ACCELERATION == 1 || HARDWARE_ACCELERATION == 4) {
				prefs.putInt("HARDWARE_ACCELERATION", 0); //$NON-NLS-1$
				HARDWARE_ACCELERATION += 1;
			}
			Messages.setLocale(prefs.get("LOCALE",Messages.getLanguage())); //$NON-NLS-1$
			
		} catch (SecurityException ex) {
			Messages.setLocale(Messages.getLanguage());
		}
	}
	public static void quitProgram(MainWindow window) {
		try {
			Preferences prefs = Preferences.userNodeForPackage(Utils.class);
			if (HARDWARE_ACCELERATION == 2 || HARDWARE_ACCELERATION == 5) {
				String[] options = {Messages.getString("T_YES"), Messages.getString("T_NO")}; //$NON-NLS-1$ //$NON-NLS-2$
				int answer = JOptionPane.showOptionDialog(null, Messages.getString("T_DID_OPENGL_WORK_WELL"), Messages.getString("T_OPENGL_CONFIRMATION"), JOptionPane.YES_NO_OPTION, JOptionPane.QUESTION_MESSAGE, null, options, options[1]);  //$NON-NLS-1$//$NON-NLS-2$
				if (answer == JOptionPane.YES_OPTION)
					prefs.putInt("HARDWARE_ACCELERATION", HARDWARE_ACCELERATION+1); //$NON-NLS-1$
			}
		
			prefs.putInt("WINDOW_X", window.getX());
			prefs.putInt("WINDOW_Y", window.getY());
			prefs.putInt("WINDOW_WIDTH", window.getWidth());
			prefs.putInt("WINDOW_HEIGHT", window.getHeight());
			prefs.putInt("WINDOW_STATE", window.getExtendedState());
		} catch (SecurityException ex) {
			// do nothing
		}
		savePreferences();
	}
	
	public static void setHardwareAcceleration(int newValue) {
		try {
			switch (newValue) {
			case 0:
			case 2:
			case 5:
				System.setProperty("sun.java2d.opengl", "false"); //$NON-NLS-1$ //$NON-NLS-2$
				break;
			case 1:
			case 3:
				System.setProperty("sun.java2d.opengl", "True"); //$NON-NLS-1$ //$NON-NLS-2$
				System.setProperty("sun.java2d.noddraw", "true");  //$NON-NLS-1$//$NON-NLS-2$
				break;
			case 4:
			case 6:
				System.setProperty("sun.java2d.opengl", "True"); //$NON-NLS-1$ //$NON-NLS-2$
				System.setProperty("sun.java2d.noddraw", "true");  //$NON-NLS-1$//$NON-NLS-2$
				// Used to workaround problems with some drivers 
				System.setProperty("sun.java2d.opengl.fbobject","false"); //$NON-NLS-1$ //$NON-NLS-2$
			}
			HARDWARE_ACCELERATION = newValue;
		} catch (Exception e) {
			System.err.println(e.getLocalizedMessage());
		}
	}
}
