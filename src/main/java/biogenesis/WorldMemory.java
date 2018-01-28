/* Copyright (c) 2012-2018  Sebastien Le Callonnec
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

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

/**
 * This class keeps track of all the organisms that have lived in the world.
 *
 * @author Sebastien Le Callonnec
 */
public class WorldMemory {
	private final List<Organism> memory = new ArrayList<>();

	/**
	 * Adds an organism to the {@link WorldMemory}.
	 *
	 * @param organism {@link Organism} to remember.
	 */
	public void addOrganism(Organism organism) {
		memory.add(organism.getID(), organism);
	}

	private Organism getOrganismById(int id) {
		return memory.get(id);
	}

	/**
	 * Gives the family tree of a given organism.
	 *
	 * Trees only go upwards, i.e. we only return ancestors, not descendants.
	 *
	 * @param id
	 * @return List&lt;Organism&gt; - List of {@link Organism} ancestry.
	 */
	public List<Organism> getFamilyTree(int id) {
		List<Organism> tree = new ArrayList<>();
		int parentId = id;
		while (parentId >= 0) {
			Organism o = getOrganismById(parentId);
			tree.add(o);
			parentId = o.getParentID();
		}

		// Reverse to have ancestors first.
		Collections.reverse(tree);
		return tree;
	}
}
