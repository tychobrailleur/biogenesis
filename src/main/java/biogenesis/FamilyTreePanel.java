/* Copyright (c) 2012 Sebastien Le Callonnec
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

import java.awt.BorderLayout;
import java.awt.Dimension;
import java.util.Iterator;
import java.util.List;
import javax.swing.BoxLayout;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.JScrollPane;
import javax.swing.SwingConstants;
import javax.swing.border.EmptyBorder;

/**
 * This panel displays the family tree for a selected organism.
 * 
 * TODO: display descendants, different colour for alive / dead,
 * possibility to select parent or child, show mutation.
 * @author Sebastien Le Callonnec
 */
public class FamilyTreePanel extends JPanel {
	private final JLabel titleLabel = new JLabel();

	public FamilyTreePanel() {
		titleLabel.setBorder(new EmptyBorder(5, 5, 5, 5));
		setLayout(new BorderLayout(5,5));
		setVisible(false);
	}
	
	/**
	 * shows the family tree for the given {@link Organism}.
	 * Currently it does not display descendants.
	 * @param organism - Organism to display.
	 */
	public void display(final Organism organism) {
		final World world = organism._world;
		final java.util.List<Organism> familyTree = world.getWorldMemory().getFamilyTree(organism.getID());
		removeAll();

		add(titleLabel, BorderLayout.NORTH);
		titleLabel.setText(Messages.getString("T_FAMILY_TREE", String.valueOf(organism.getID())));
		
		add(new JScrollPane(new JPanel() {
			{
				setLayout(new BoxLayout(this, BoxLayout.Y_AXIS));
				Iterator<Organism> iterateFamily = familyTree.iterator();
				while (iterateFamily.hasNext()) {
					Organism parent = iterateFamily.next();
					add(new OrganismEntryPanel(parent));
					if (iterateFamily.hasNext()) {
						add(new JLabel(Messages.getString("T_FATHERED"), SwingConstants.CENTER));
					}
				}
			}
		}), BorderLayout.CENTER);
		
		setVisible(true);
	}
	
	@Override
	public void hide() {
		removeAll();
		super.hide();
	}
	
	
	final class OrganismEntryPanel extends JPanel {
		public OrganismEntryPanel(Organism organism) {
			setLayout(new BorderLayout(5,5));
			setMaximumSize(new Dimension(100, 100));
			add(new GeneticCodePanel(organism.getGeneticCode(), organism._visibleWorld), 
					BorderLayout.CENTER);
			
			String label = String.valueOf(organism.getID());
			if (!organism.isAlive()) 
				label = Messages.getString("T_DEAD", String.valueOf(organism.getID()));
			add(new JLabel(label), BorderLayout.SOUTH);
		}
		
		
	}
}
