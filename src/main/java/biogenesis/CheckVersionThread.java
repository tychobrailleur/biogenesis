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

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.net.MalformedURLException;
import java.net.URL;

import javax.swing.JFrame;
import javax.swing.JOptionPane;

public class CheckVersionThread extends Thread {
	private JFrame parentFrame;
	
	public static String translateVersion(String str) {
		String zero = "000000"; //$NON-NLS-1$
		String v;
		int version, subversion, revision;
		try {
			v = zero.substring(0, 6 - str.length()) + str;
		} catch (IndexOutOfBoundsException e) {
			return str;
		}
		try {
			version = Integer.parseInt(v.substring(0, 2));
			subversion = Integer.parseInt(v.substring(2, 4));
			revision = Integer.parseInt(v.substring(4, 6));
		} catch (NumberFormatException e) {
			return str;
		}
		String result;
		if (revision == 0)
			result = version + "." + subversion; //$NON-NLS-1$
		else
			result = version + "." + subversion + "." + revision; //$NON-NLS-1$ //$NON-NLS-2$
		return result; 
	}
	
	public CheckVersionThread(JFrame frame) {
		parentFrame = frame;
	}
	
	@Override
	public void run() {
		URL url;
		BufferedReader br = null;
		try {
			url = new URL("http://biogenesis.sourceforge.net/lastversion.txt"); //$NON-NLS-1$
			InputStreamReader inr = new InputStreamReader(url.openStream());
			br = new BufferedReader(inr);
			String s = translateVersion(br.readLine());
			JOptionPane.showMessageDialog(parentFrame,Messages.getString("T_YOUR_VERSION")+translateVersion(String.valueOf(Utils.VERSION))+"\n"+Messages.getString("T_LAST_VERSION")+s,  //$NON-NLS-1$//$NON-NLS-2$ //$NON-NLS-3$ 
					Messages.getString("T_VERSION_TITLE"), JOptionPane.INFORMATION_MESSAGE);   //$NON-NLS-1$
			br.close();
		} catch (MalformedURLException e) {
			e.printStackTrace();
		} catch (IOException e) {
			JOptionPane.showMessageDialog(parentFrame,Messages.getString("T_CANT_CHECK_CURRENT_VERSION")+e.getLocalizedMessage(), //$NON-NLS-1$ 
					Messages.getString("T_VERSION_TITLE"), JOptionPane.ERROR_MESSAGE); //$NON-NLS-1$
		} finally {
			if (br != null)
				try {
					br.close();
				} catch (IOException e) {
					e.printStackTrace();
				}
		}
	}
}
