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

import java.awt.BorderLayout;
import java.awt.event.*;

import javax.swing.*;

public class NetConfigWindow extends JDialog {
	private static final long serialVersionUID = Utils.FILE_VERSION;
	protected JCheckBox acceptConnectionsCheck;
	protected boolean acceptConnections;
	//protected JCheckBox connectToServerCheck;
	//protected boolean connectToServer = Utils.CONNECT_TO_SERVER;
	//protected JTextField serverAddressText;
	//protected JTextField serverPortText;
	protected JTextField localPortText;
	protected JTextField remotePortText;
	protected JTextField remoteAddressText;
	protected JTextField maxConnectionsText;
	protected JButton cancelButton;
	protected JButton okButton;
	
	protected MainWindow mainWindow;
	
	public NetConfigWindow(MainWindow parent) {
		super(parent,Messages.getString("T_NETWORK_CONFIGURATION"),true); //$NON-NLS-1$
		mainWindow = parent;
		acceptConnections = mainWindow.isAcceptingConnections();
		setDefaultCloseOperation(DISPOSE_ON_CLOSE);	
		setComponents();
		pack();
		setResizable(false);
		cancelButton.addActionListener(new ActionListener() {
            public void actionPerformed(ActionEvent evt) {
            	dispose();
            }
            });
		okButton.addActionListener(new ActionListener() {
            public void actionPerformed(ActionEvent evt) {
            	int oldPort = Utils.LOCAL_PORT;
            	checkParams();
            	Utils.savePreferences();
            	if (oldPort != Utils.LOCAL_PORT) {
            		mainWindow.stopServer();
            		if (mainWindow.isAcceptingConnections())
            			mainWindow.startServer();
            	}
            	dispose();
            }
            });
		setVisible(true);
	}

	protected void setComponents() {
		getContentPane().setLayout(new BorderLayout());
		// Set up buttons
		JPanel buttonsPanel = new JPanel();
		okButton = new JButton(Messages.getString("T_OK")); //$NON-NLS-1$
		cancelButton = new JButton(Messages.getString("T_CANCEL")); //$NON-NLS-1$
		buttonsPanel.add(okButton);
		buttonsPanel.add(cancelButton);
		getContentPane().add(buttonsPanel, BorderLayout.SOUTH);
		// Accept connections + use server
		JPanel generalPanel = new JPanel();
		generalPanel.setLayout(new BoxLayout(generalPanel,BoxLayout.Y_AXIS));
		JPanel panel = new JPanel();
		acceptConnectionsCheck = new JCheckBox(Messages.getString("T_ALLOW_CONNECTIONS_FROM_OTHER_USERS"), //$NON-NLS-1$
				acceptConnections);
		acceptConnectionsCheck.addItemListener(new ItemListener() {
			public void itemStateChanged(ItemEvent arg0) {
				acceptConnections = (arg0.getStateChange() == ItemEvent.SELECTED);	
			}
		});
		panel.add(acceptConnectionsCheck);
		generalPanel.add(panel);
		/*panel = new JPanel();
		connectToServerCheck = new JCheckBox("Utilitza el servidor per trobar altres usuaris",
				connectToServer);
		connectToServerCheck.addItemListener(new ItemListener() {
			public void itemStateChanged(ItemEvent arg0) {
				connectToServer = (arg0.getStateChange() == ItemEvent.SELECTED);
			}
		});
		panel.add(connectToServerCheck);
		generalPanel.add(panel);*/
		/*panel = new JPanel();
		JLabel label = new JLabel("Adre�a del servidor: ");
		panel.add(label);
		serverAddressText = new JTextField(Utils.SERVER_ADDRESS,25);
		panel.add(serverAddressText);
		label = new JLabel("Port del servidor: ");
		panel.add(label);
		serverPortText = new JTextField(String.valueOf(Utils.SERVER_PORT),5);
		panel.add(serverPortText);
		generalPanel.add(panel);*/
		panel = new JPanel();
		JLabel label = new JLabel(Messages.getString("T_MAXIMUM_NUMBER_OF_ALLOWED_CONNECTIONS")); //$NON-NLS-1$
		panel.add(label);
		maxConnectionsText = new JTextField(String.valueOf(Utils.MAX_CONNECTIONS),4);
		panel.add(maxConnectionsText);
		generalPanel.add(panel);
		panel = new JPanel();
		label = new JLabel(Messages.getString("T_LOCAL_PORT_TO_RECEIVE_CONNECTIONS")); //$NON-NLS-1$
		panel.add(label);
		localPortText = new JTextField(String.valueOf(Utils.LOCAL_PORT),6);
		panel.add(localPortText);
		generalPanel.add(panel);
		getContentPane().add(generalPanel, BorderLayout.CENTER);
	}
	
	void checkParams() {
		int i;
		mainWindow.setAcceptConnections(acceptConnections);
		
		/*Utils.CONNECT_TO_SERVER = connectToServer;
		Utils.SERVER_ADDRESS = serverAddressText.getText();
		try {
			i = Integer.parseInt(serverPortText.getText());
			if (i>=0) Utils.SERVER_PORT = i;
		} catch (NumberFormatException e) {
			// Keep old value if there is a problem
		}*/
		try {
			i = Integer.parseInt(localPortText.getText());
			if (i>=0) Utils.LOCAL_PORT = i;
		} catch (NumberFormatException e) {
			// Keep old value if there is a problem
		}
		try {
			i = Integer.parseInt(maxConnectionsText.getText());
			if (i>=0) Utils.MAX_CONNECTIONS = i;
		} catch (NumberFormatException e) {
			// Keep old value if there is a problem
		}
	}
	/*
	protected void defaultPreferences() {
		acceptConnectionsCheck.setSelected(Utils.ACCEPT_CONNECTIONS);
		connectToServerCheck.setSelected(Utils.CONNECT_TO_SERVER);
		serverAddressText.setText(Utils.SERVER_ADDRESS);
		serverPortText.setText(String.valueOf(Utils.SERVER_PORT));
		localPortText.setText(String.valueOf(Utils.LOCAL_PORT));
		remotePortText.setText("");
		remoteAddressText.setText("");
		maxConnectionsText.setText(String.valueOf(Utils.MAX_CONNECTIONS));
	}*/
}
