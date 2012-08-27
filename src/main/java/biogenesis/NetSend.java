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

import java.net.*;
import java.io.*;

public class NetSend extends Thread {
	protected Connection connection;
	protected int message;
	protected GeneticCode geneticCode;
	
	private int netCode;
	private int port;
	private int answerMessage;
	private ObjectOutputStream oos;
	private ObjectInputStream ois;
	private Socket socket;
	private InetAddress address;
	
	public NetSend(Connection c, int m, GeneticCode g) {
		connection = c;
		message = m;
		geneticCode = g;
	}
	
	@Override
	public void run() {
		address = connection.remoteAddress;
		port = connection.remotePort;
		netCode = connection.netCode;
		try {
			socket = new Socket(address,port);
			oos = new ObjectOutputStream(socket.getOutputStream());
			ois = new ObjectInputStream(socket.getInputStream());
			System.out.println("->"+NetServerThread.messageToString(message)); //$NON-NLS-1$
			switch (message) {
			case NetServerThread.CONNECT:
				handleConnect();
				break;
			case NetServerThread.SEND_CODE:
				handleSendCode();
				break;
			case NetServerThread.KEEP_ALIVE:
				handleKeepAlive();
				break;
			case NetServerThread.DISCONNECT:
				handleDisconnect();
				break;
			}
			socket.close();
		} catch (ConnectException e) {
			connection.setState(Connection.STATE_DISCONNECTED);
			System.out.println("NetSend, ConnectException: "+e.getMessage()); //$NON-NLS-1$
			System.out.println("Connection closed with "+connection.remoteAddress+":"+connection.remotePort);  //$NON-NLS-1$//$NON-NLS-2$
		} catch (UnknownHostException e) {
			connection.setState(Connection.STATE_DISCONNECTED);
			System.out.println("NetSend, UnknownHostException: "+e.getMessage()); //$NON-NLS-1$
			System.out.println("Connection closed with "+connection.remoteAddress+":"+connection.remotePort);  //$NON-NLS-1$//$NON-NLS-2$
		} catch (IOException e) {
			connection.setState(Connection.STATE_DISCONNECTED);
			System.out.println("NetSend, IOException: "+e.getMessage()); //$NON-NLS-1$
			System.out.println("Connection closed with "+connection.remoteAddress+":"+connection.remotePort);  //$NON-NLS-1$//$NON-NLS-2$
		} finally {
			if (oos != null) { 
				try {
					oos.close();
				} catch (IOException e) {
					System.out.println(e.getMessage());
				}
			}
			if (ois != null) {
				try {
					ois.close();
				} catch (IOException e) {
					System.out.println(e.getMessage());
				}
			}
			if (socket != null) {
				try {
					socket.close();
				} catch (IOException e) {
					System.out.println(e.getMessage());
				}
			}
		}
	}
	
	private void handleConnect() throws ConnectException, IOException, UnknownHostException {
		System.out.println("----- Connecting..."); //$NON-NLS-1$
		// Try to connect
		oos.writeInt(message);
		oos.writeInt(Utils.VERSION);
		oos.writeInt(Utils.LOCAL_PORT);
		oos.writeInt(netCode);
		oos.flush();
		answerMessage = ois.readInt();
		System.out.println(NetServerThread.messageToString(answerMessage));
		switch (answerMessage) {
		case NetServerThread.CONNECTED:
			// Connection successful
			connection.setState(Connection.STATE_CONNECTED);
			break;
		case NetServerThread.ALREADY_CONNECTED:
			connection.setState(Connection.STATE_CONNECTED);
			break;
		default:
			connection.setState(Connection.STATE_DISCONNECTED);
			break;
		}
	}
	
	private void handleSendCode() throws ConnectException, IOException, UnknownHostException {
		System.out.println("----- Sending code..."); //$NON-NLS-1$
		oos.writeInt(message);
		oos.writeInt(netCode);
		oos.flush();
		answerMessage = ois.readInt();
		System.out.println(NetServerThread.messageToString(answerMessage));
		switch (answerMessage) {
		case NetServerThread.WAITING_CODE:
			oos.writeObject(geneticCode);
			System.out.println("->Genetic code"); //$NON-NLS-1$
			answerMessage = ois.readInt();
			System.out.println(NetServerThread.messageToString(answerMessage));
			if (answerMessage == NetServerThread.CODE_RECEIVED) {
				connection.resetTime();
			}
			break;
		case NetServerThread.NOT_CONNECTED:
			connection.setState(Connection.STATE_DISCONNECTED);
			break;
		case NetServerThread.TOO_MANY_CODES:
			break;
		default:
			connection.setState(Connection.STATE_DISCONNECTED);
			break;
		}
	}
	
	private void handleKeepAlive() throws ConnectException, IOException, UnknownHostException {
		System.out.println("----- Checking remote server..."); //$NON-NLS-1$
		oos.writeInt(message);
		oos.writeInt(netCode);
		oos.flush();
		answerMessage = ois.readInt();
		System.out.println(NetServerThread.messageToString(answerMessage));
		switch (answerMessage) {
		case NetServerThread.ACK_KEEP_ALIVE:
			connection.resetTime();
			break;
		default:
			connection.setState(Connection.STATE_DISCONNECTED);
			break;
		}
	}
	
	private void handleDisconnect() throws ConnectException, IOException, UnknownHostException {
		System.out.println("----- Disconnecting..."); //$NON-NLS-1$
		oos.writeInt(message);
		oos.writeInt(netCode);
		oos.flush();
		answerMessage = ois.readInt();
		System.out.println(NetServerThread.messageToString(answerMessage));
		connection.setState(Connection.STATE_DISCONNECTED);
	}
}
