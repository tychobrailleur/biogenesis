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

import java.util.prefs.BackingStoreException;
import java.util.prefs.Preferences;

import javax.swing.JOptionPane;

public class Cleaner {
	private static final String[] success={"Uninstalled","Se ha desinstalado","S'ha desinstal·lat"};  //$NON-NLS-1$//$NON-NLS-2$ //$NON-NLS-3$
	private static final String[] failed={"Uninstallation failed","Ha fallado la desinstalación","Ha fallat la desinstal·lació"};  //$NON-NLS-1$ //$NON-NLS-2$//$NON-NLS-3$
	/**
	 * @param args
	 */
	public static void main(String[] args) {
		Preferences prefs = Preferences.userNodeForPackage(Utils.class);
		String locale = prefs.get("LOCALE","en"); //$NON-NLS-1$ //$NON-NLS-2$
		int pos=0;
		if (locale.equals("es")) //$NON-NLS-1$
			pos = 1;
		if (locale.equals("ca")) //$NON-NLS-1$
			pos = 2;
		try {
			prefs.removeNode();
			prefs.flush();
			JOptionPane.showMessageDialog(null, success[pos], "Biogenesis", JOptionPane.INFORMATION_MESSAGE); //$NON-NLS-1$
		} catch (BackingStoreException e) {
			JOptionPane.showMessageDialog(null, failed[pos], "Biogenesis", JOptionPane.INFORMATION_MESSAGE); //$NON-NLS-1$
		}
	}

}
