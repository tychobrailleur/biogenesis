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
import java.awt.Dimension;
import java.awt.Graphics;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;

import javax.swing.JMenuItem;
import javax.swing.JPanel;
import javax.swing.JPopupMenu;

public class GeneticCodePanel extends JPanel {
	private static final long serialVersionUID = Utils.FILE_VERSION;
	private static final Dimension defaultSize = new Dimension(100,100);
	protected GeneticCode code;
	protected VisibleWorld visible;
	protected JPopupMenu popup;
	
	public GeneticCodePanel(GeneticCode geneticCode, VisibleWorld visibleWorld) {
		setBackground(Color.BLACK);
		setPreferredSize(defaultSize);
		code = geneticCode;
		visible = visibleWorld;
		popup = new JPopupMenu();
	    JMenuItem menuCopy = new JMenuItem(Messages.getString("T_COPY")); //$NON-NLS-1$
	    menuCopy.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				visible.setClippedGeneticCode(code);
			}
	    	
	    });
	    popup.add(menuCopy);
		addMouseListener(new MouseAdapter() {
			@Override
			public void mousePressed(MouseEvent e) {
				if (e.isPopupTrigger()) {
					popup.show(e.getComponent(), e.getX(), e.getY());
				}
			}
			@Override
			public void mouseReleased(MouseEvent e) {
				if (e.isPopupTrigger()) {
					popup.show(e.getComponent(), e.getX(), e.getY());
				}
			}
		});
	}
	
	public void setGeneticCode(GeneticCode geneticCode) {
		code = geneticCode;
	}
	
	@Override
	public void paintComponent(Graphics g) {
		super.paintComponent(g);
		if (code != null)
			code.draw(g, getWidth(), getHeight());
	}
}
