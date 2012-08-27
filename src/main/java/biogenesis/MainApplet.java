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

import java.awt.event.*;
import javax.swing.*;

public class MainApplet extends JApplet implements ActionListener {
	private static final long serialVersionUID = Utils.FILE_VERSION;

	protected MainWindow mainWindow = null;
	protected JButton launchButton = null;
	
	@Override
	public void init() {
		launchButton = new JButton(Messages.getString("T_START")); //$NON-NLS-1$
		getContentPane().add(launchButton);
		launchButton.addActionListener(this);
	}
	
	public void kill() {
		releaseApplication();
	}
	
	protected void releaseApplication() {
		if (mainWindow != null) {
			mainWindow._timer.cancel();
			mainWindow.dispose();
			mainWindow = null;
		}
	}
	
	public void actionPerformed(ActionEvent evt) {
		if (mainWindow == null) {
			mainWindow = new MainWindow();
			launchButton.setText(Messages.getString("T_STOP")); //$NON-NLS-1$
			mainWindow.addWindowListener(new WindowAdapter() {
            @Override
			public void windowClosed(WindowEvent wevt) {
            	releaseApplication();
            	launchButton.setText(Messages.getString("T_START")); //$NON-NLS-1$
            	launchButton.setEnabled(true);
            }
         });
		}
		else {
			launchButton.setEnabled(false);
			releaseApplication();
		}
	}  
}
