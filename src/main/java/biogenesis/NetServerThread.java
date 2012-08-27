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

import java.io.*;
import java.net.*;
import java.util.*;

public class NetServerThread extends Thread {
	protected InetAddress address;
	protected int port;
	protected ServerSocket serverSocket;
	protected Socket listenSocket;
	protected int state;
	protected int netCode = 0;
	protected int receivedMessage;
	protected boolean isActive;
	protected GeneticCode code;
	protected List<Connection> connections = Collections.synchronizedList(new ArrayList<Connection>());
	protected MainWindow mainWindow;
	private ObjectInputStream ois;
	private ObjectOutputStream oos;
	
	public static final int CONNECT = 1;
	public static final int CONNECTED = 2;
	public static final int ALREADY_CONNECTED = 3;
	public static final int TOO_MANY_CONNECTIONS = 4;
	public static final int DISCONNECT = 5;
	public static final int NOT_ACCEPTING_CONNECTIONS = 6;
	public static final int NOT_CONNECTED = 7;
	public static final int SEND_CODE = 8;
	public static final int WAITING_CODE = 9;
	public static final int CODE_RECEIVED = 10;
	public static final int TOO_MANY_CODES = 11;
	public static final int GAME_PAUSED = 12;
	public static final int KEEP_ALIVE = 13;
	public static final int ACK_KEEP_ALIVE = 14;
	public static final int DISCONNECTED = 15;
	public static final int INCOMPATIBLE_PROGRAM_VERSION = 16;
	
	public static final int STATE_DISCONNECTED = 50;
	public static final int STATE_CONNECTED = 51;
	
	public static String messageToString(int message) {
		switch (message) {
		case CONNECT: return "CONNECT"; //$NON-NLS-1$
		case CONNECTED: return "CONNECTED"; //$NON-NLS-1$
		case ALREADY_CONNECTED: return "ALREADY_CONNECTED"; //$NON-NLS-1$
		case TOO_MANY_CONNECTIONS: return "TOO_MANY_CONNECTIONS"; //$NON-NLS-1$
		case DISCONNECT: return "DISCONNECT"; //$NON-NLS-1$
		case NOT_ACCEPTING_CONNECTIONS: return "NOT_ACCEPTING_CONNECTIONS"; //$NON-NLS-1$
		case NOT_CONNECTED: return "NOT_CONNECTED"; //$NON-NLS-1$
		case SEND_CODE: return "SEND_CODE"; //$NON-NLS-1$
		case WAITING_CODE: return "WAITING_CODE"; //$NON-NLS-1$
		case CODE_RECEIVED: return "CODE_RECEIVED"; //$NON-NLS-1$
		case TOO_MANY_CODES: return "TOO_MANY_CODES"; //$NON-NLS-1$
		case GAME_PAUSED: return "GAME_PAUSED"; //$NON-NLS-1$
		case KEEP_ALIVE: return "KEEP_ALIVE"; //$NON-NLS-1$
		case ACK_KEEP_ALIVE: return "ACK_KEEP_ALIVE"; //$NON-NLS-1$
		case DISCONNECTED: return "DISCONNECTED"; //$NON-NLS-1$
		case INCOMPATIBLE_PROGRAM_VERSION: return "INCOMPATIBLE_PROGRAM_VERSION"; //$NON-NLS-1$
		default: return "Non existant code"; //$NON-NLS-1$
		}
	}
	
	public boolean isActive() {
		return isActive;
	}
	
	public NetServerThread(MainWindow app) {
		mainWindow = app;
	}
	
	public List<Connection> getConnections() {
		return connections;
	}
	
	public Connection newConnection(InetAddress remoteAddress, int remotePort) {
		address = remoteAddress;
		port = remotePort;
		Connection c = checkConnectionDuplicity();
		if (c == null) {
			c = new Connection(mainWindow, remoteAddress, remotePort);
			connections.add(c);
		} else 
			return null;
		return c;
	}
	
	private Connection newConnection() {
		Connection c = checkConnectionDuplicity();
		if (c == null) {
			c = new Connection(mainWindow, address, port, netCode);
			connections.add(c);
		} else
			return null;
		return c;
	}
	
	public void removeConnection(Connection c) {
		connections.remove(c);
	}

	public void closeServer() {
		isActive = false;
		// Remove all connections
		Connection c;
		synchronized (connections) {
			for (Iterator<Connection> it = connections.iterator();it.hasNext();) {
				c = it.next();
				c.send(DISCONNECT);
				c.setState(DISCONNECTED);
			}
		}
		connections = Collections.synchronizedList(new ArrayList<Connection>());
	}
	
	@Override
	public void run() {
		isActive = true;
		try {
			serverSocket=new ServerSocket(Utils.LOCAL_PORT);
			mainWindow.setStatusMessage(Messages.getString("T_NET_SERVER_LISTENING_ON_PORT", Integer.toString(Utils.LOCAL_PORT))); //$NON-NLS-1$
		} catch (IOException e) {
			if (e instanceof BindException) {
				mainWindow.setStatusMessage(Messages.getString("T_PORT_ALREADY_IN_USE", Integer.toString(Utils.LOCAL_PORT))); //$NON-NLS-1$
			} else
				e.printStackTrace();
			isActive = false;
		}
		while (isActive) {
			try {
				listenSocket = serverSocket.accept();
				ois = new ObjectInputStream(listenSocket.getInputStream());
				oos = new ObjectOutputStream(listenSocket.getOutputStream());
				receivedMessage = ois.readInt();
				System.out.println(messageToString(receivedMessage));
				switch (receivedMessage) {
				case CONNECT:
					handleConnect();
					break;
				case SEND_CODE:
					handleSendCode();
					break;
				case KEEP_ALIVE:
					handleKeepAlive();
					break;
				case DISCONNECT:
					handleDisconnect();
					break;
				}
				ois.close();
				oos.close();
			} catch (IOException e) {
				System.out.println(e.getMessage());
			} finally {
				if (listenSocket != null)
					try {
						listenSocket.close();
					} catch (IOException e) {
						e.printStackTrace();
					}
			}	
		}
	}
	
	private void handleDisconnect() {
		Connection c = null;
		try {
			netCode = ois.readInt();
			c = checkConnectionNetCode();
			if (c != null && c.getState() == Connection.STATE_CONNECTED) {
				oos.writeInt(DISCONNECTED);
				oos.flush();
				System.out.println("->DISCONNECTED"); //$NON-NLS-1$
				c.setState(Connection.STATE_DISCONNECTED);
			} else {
				System.out.println("->NOT_CONNECTED"); //$NON-NLS-1$
				oos.writeInt(NOT_CONNECTED);
				oos.flush();
			}
		} catch (IOException e) {
			System.out.println("handleDisconnect: "+e.getMessage()); //$NON-NLS-1$
			if (c!=null) {
				c.setState(Connection.STATE_DISCONNECTED);
				System.out.println("Connection closed with "+c.remoteAddress+":"+c.remotePort);  //$NON-NLS-1$//$NON-NLS-2$
			}
		}
	}
	
	private void handleKeepAlive() {
		Connection c = null;
		try {
			netCode = ois.readInt();
			c = checkConnectionNetCode();
			if (c != null && c.getState() == Connection.STATE_CONNECTED) {
				oos.writeInt(ACK_KEEP_ALIVE);
				oos.flush();
				System.out.println("->ACK_KEEP_ALIVE"); //$NON-NLS-1$
			} else {
				System.out.println("->NOT_CONNECTED"); //$NON-NLS-1$
				oos.writeInt(NOT_CONNECTED);
				oos.flush();
			}
		} catch (IOException e) {
			System.out.println("handleKeepAlive: "+e.getMessage()); //$NON-NLS-1$
			if (c!=null) {
				c.setState(Connection.STATE_DISCONNECTED);
				System.out.println("Connection closed with "+c.remoteAddress+":"+c.remotePort); //$NON-NLS-1$ //$NON-NLS-2$
			}
		}
	}
	
	private void handleConnect() {
		try {
			int program_version = ois.readInt();
			port = ois.readInt();
			address = listenSocket.getInetAddress();
			netCode = ois.readInt();
			Connection c = checkConnectionNetCode();
			if (c != null) {
				oos.writeInt(ALREADY_CONNECTED);
				oos.flush();
				System.out.println("->ALREADY_CONNECTED"); //$NON-NLS-1$
			} else {
				if (mainWindow.isAcceptingConnections()) {
					if (connections.size() < Utils.MAX_CONNECTIONS) {
						if (Utils.VERSION == program_version) {
							Connection newConnection = newConnection();
							if (newConnection != null) {
								oos.writeInt(CONNECTED);
								newConnection.setState(Connection.STATE_CONNECTED);
								System.out.println("->CONNECTED"); //$NON-NLS-1$
							} else {
								oos.writeInt(ALREADY_CONNECTED);
								System.out.println("->ALREADY_CONNECTED"); //$NON-NLS-1$
							}
							oos.flush();
						} else {
							oos.writeInt(INCOMPATIBLE_PROGRAM_VERSION);
							oos.flush();
							System.out.println("->INCOMPATIBLE_PROGRAM_VERSION"); //$NON-NLS-1$
						}
					} else {
						oos.writeInt(TOO_MANY_CONNECTIONS);
						oos.flush();
						System.out.println("->TOO_MANY_CONNECTIONS"); //$NON-NLS-1$
					}
				} else {
					oos.writeInt(NOT_ACCEPTING_CONNECTIONS);
					oos.flush();
					System.out.println("->NOT_ACCEPTING_CONNECTIONS"); //$NON-NLS-1$
				}
			}
		} catch (IOException ex) {
			System.out.println(ex.getMessage());
		}
	}
	
	private void handleSendCode() {
		Connection c = null;
		try {
			netCode = ois.readInt();
			c = checkConnectionNetCode();
			if (c != null && c.getState() == Connection.STATE_CONNECTED) {
				oos.writeInt(WAITING_CODE);
				oos.flush();
				System.out.println("->WAITING_CODE"); //$NON-NLS-1$
				code = (GeneticCode) ois.readObject();
				System.out.println("Genetic code"); //$NON-NLS-1$
				oos.writeInt(CODE_RECEIVED);
				oos.flush();
				System.out.println("->CODE_RECEIVED"); //$NON-NLS-1$
				c.inCorridor.receiveOrganism(code);
			} else {
				System.out.println("->NOT_CONNECTED"); //$NON-NLS-1$
				oos.writeInt(NOT_CONNECTED);
				oos.flush();
			}
		} catch (IOException e) {
			System.out.println("handleSendCode: "+e.getMessage()); //$NON-NLS-1$
			if (c!=null) {
				c.setState(Connection.STATE_DISCONNECTED);
				System.out.println("Connection closed with "+c.remoteAddress+":"+c.remotePort);  //$NON-NLS-1$//$NON-NLS-2$
			}
		} catch (ClassNotFoundException e) {
			System.out.println("handleSendCode: "+e.getMessage()); //$NON-NLS-1$
			if (c!=null) {
				c.setState(Connection.STATE_DISCONNECTED);
				System.out.println("Connection closed with "+c.remoteAddress+":"+c.remotePort);  //$NON-NLS-1$//$NON-NLS-2$
			}
		}
	}
	
	private Connection checkConnectionNetCode() {
		Connection c;
		synchronized (connections) {
			for (Iterator<Connection> it = connections.iterator();it.hasNext();) {
				c = it.next();
				if (c.netCode == netCode)
					return c;
			}
		}
		return null;
	}
	
	private Connection checkConnectionDuplicity() {
		Connection c;
		synchronized (connections) {
			for (Iterator<Connection> it = connections.iterator();it.hasNext();) {
				c = it.next();
				if (c.remoteAddress.equals(address) && c.remotePort == port) {
					System.out.println("Duplicated connection"); //$NON-NLS-1$
					return c;
				}
			}
		}
		return null;
	}
}
