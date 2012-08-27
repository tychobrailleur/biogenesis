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

import java.net.URL;

import javax.swing.AbstractAction;
import javax.swing.ImageIcon;

public abstract class StdAction extends AbstractAction {
	private static final long serialVersionUID = 1L;
	protected String name_key;
	protected String desc_key;
	
	protected ImageIcon createIcon(String imageName) {
		ImageIcon icon = null;
		if (imageName != null) {
			URL url = getClass().getResource(imageName);
			if (url != null)
				icon = new ImageIcon(url);
		}
		return icon;
	}
	
	public void changeLocale() {
		putValue(NAME, Messages.getString(name_key));
		putValue(SHORT_DESCRIPTION, Messages.getString(desc_key));
		putValue(MNEMONIC_KEY, Messages.getMnemonic(name_key));
	}
	
	public StdAction(String key, String icon_path, String desc) {
		super(Messages.getString(key));
		name_key = key;
		desc_key = desc;
		putValue(SMALL_ICON, createIcon(icon_path));
		putValue(SHORT_DESCRIPTION, Messages.getString(desc));
		putValue(MNEMONIC_KEY, Messages.getMnemonic(key));
	}
}
