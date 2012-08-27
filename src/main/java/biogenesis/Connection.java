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

import java.net.InetAddress;
import java.util.Timer;
import java.util.TimerTask;
/**
 * This class implements an application level connection between two instances of biogenesis.
 * The connection will create the biological corridors with the other host when it connects and
 * will remove them when it disconnects.
 */
public class Connection {
	/**
	 * The IP address of the other host
	 */
	protected InetAddress remoteAddress;
	/**
	 * The port of the other host
	 */
	protected int remotePort;
	/**
	 * The state of the connection. Can be STATE_NOT_INITIALIZED for a new connection,
	 * STATE_CONNECTED for a working connection and STATE_DISCONNECTED for an
	 * already finished connection.
	 */
	protected int state;
	/**
	 * A number that identifies this connection to distinguish it from other connections
	 * to the same address. Useful to avoid confusion when connecting through NAT routers.
	 */
	protected int netCode;
	/**
	 * A reference to the main application in order to obtain some its information.
	 */
	protected MainWindow mainWindow;
	/**
	 * The time that the connection will wait without receiving any signal from the
	 * server on the other host before it tries to send a ping signal.
	 */
	private static final long CHECK_CONNECTION_TIME = 30000;
	/**
	 * The time when the last message from the server on the other host was received
	 */
	private long time;
	/**
	 * A timer used to activate ping message to the server on the other host when
	 * necessary
	 */
	private Timer timer;
	/**
	 * The biological corridor used to receive organism from the other host
	 */
	protected InCorridor inCorridor;
	/**
	 * The biological corridor used to send organism to the other host
	 */
	protected OutCorridor outCorridor;
	/**
	 * State of the connection that indicates that the connection has just been created
	 * and no attemp to connect to the other host has been done.
	 */
	public static final int STATE_DISCONNECTED = 50;
	/**
	 * State of the connection that indicates that the connection is active.
	 */
	public static final int STATE_CONNECTED = 51;
	/**
	 * State of the connection that indicates that the connection has been closed.
	 */
	public static final int STATE_NOT_INITIALIZED = 52;
	/**
	 * Return the IP address of the remote host
	 * 
	 * @return  the IP address of the remote host
	 */
	public InetAddress getRemoteAddress() {
		return remoteAddress;
	}
	/**
	 * Return the port of the remote host
	 * 
	 * @return  the port of the remote host
	 */
	public int getRemotePort() {
		return remotePort;
	}
	/**
	 * Create a new connection to the specified IP address and port
	 * 
	 * @param app  Reference to the main application window
	 * @param a  Address of the remote host
	 * @param p  Port of the remote host
	 */	
	public Connection(MainWindow app, InetAddress a, int p) {
		remotePort = p;
		remoteAddress = a;
		state = STATE_NOT_INITIALIZED;
		netCode = Utils.random.nextInt();
		mainWindow = app;
		createCorridors();
	}
	/**
	 * Create a new connection to the specified IP address and port using
	 * the specified netcode to identify the connection. Used to connect this client
	 * to the remote server when this server is already connected with the remote
	 * client.
	 * 
	 * @param app  Reference to the main application window
	 * @param a  Address of the remote host
	 * @param p  Port of the remote host
	 * @param nCode  Identifying code to use for this connection
	 */
	public Connection(MainWindow app, InetAddress a, int p, int nCode) {
		remotePort = p;
		remoteAddress = a;
		state = STATE_NOT_INITIALIZED;
		netCode = nCode;
		mainWindow = app;
		createCorridors();	
	}
	/**
	 * Create the pair of biological corridors, but don't add them to the world yet.
	 */
	private void createCorridors() {
		inCorridor = new InCorridor(mainWindow.getWorld());
		outCorridor = new OutCorridor(mainWindow.getWorld(), this);
	}
	/**
	 * Send a message to the remote host server. The message is send in a new thread and
	 * the method returns immidiately.
	 * 
	 * @param m  the message to send. See {@link NetServerThread} to see the possible
	 * messages to send. 
	 */
	public void send(int m) {
		NetSend n = new NetSend(this, m, null);
		n.start();
	}
	/**
	 * Send a genetic code to the remote host server. The code is send in a new thread
	 * and the method returns immidiately.
	 * 
	 * @param c  the genetic code to send.
	 */
	public void send(GeneticCode c) {
		NetSend n = new NetSend(this, NetServerThread.SEND_CODE, c);
		n.start();
	}
	/**
	 * Inform the connection that its state has changed. Used by {@link NetSend} and
	 * {@link NetServerThread}.
	 * 
	 * @param newState  STATE_CONNECTED if the connection has been stablished or 
	 * STATE_DISCONNECTED if the connection has been closed.
	 */
	public void setState(int newState) {
		if (state != newState) {
			if (newState == STATE_CONNECTED) {
				mainWindow.getWorld().addCorridors(inCorridor, outCorridor);
				keepAliveThread();
				mainWindow.setStatusMessage(Messages.getString("T_CONNECTION_STABLISHED", remoteAddress.toString())); //$NON-NLS-1$
			}
			if (newState == STATE_DISCONNECTED) {
				if (timer != null)
					timer.cancel();
				mainWindow.setStatusMessage(Messages.getString("T_CONNECTION_LOST", remoteAddress.toString())); //$NON-NLS-1$
				mainWindow.getWorld().removeCorridors(inCorridor, outCorridor);
				mainWindow.serverThread.removeConnection(this);
			}
		}
		state = newState;
	}
	/**
	 * Returns the state of the connection.
	 * 
	 * @return  the state of the connection
	 */
	public int getState() {
		return state;
	}
	/**
	 * Send a request connection message to the remote host. This is the same
	 * as calling {@code send(NetServerThread.CONNECT)}.
	 */
	public void connect() {
		// send a connection message
		NetSend n = new NetSend(this, NetServerThread.CONNECT, null);
		n.start();
	}
	/**
	 * Inform the connection that a message from the server has been received in
	 * order to avoid unnecessary ping messages.
	 */
	public void resetTime() {
		time = System.currentTimeMillis();
	}
	/**
	 * Start a new thread that will perform periodical pings to the remote
	 * server in order to keep the connection alive.
	 */
	private void keepAliveThread() {
		if (timer != null)
			timer.cancel();
		resetTime();
		timer = new Timer();
		TimerTask keepAliveTask = new TimerTask() {
		    @Override
			public void run() {
		    	keepAlive();
		    }
		};
		timer.schedule(keepAliveTask, CHECK_CONNECTION_TIME, CHECK_CONNECTION_TIME);
	}
	/**
	 * Make sure the connection is alive. If enough time has passed since the last
	 * message from the remote server, send a ping. This is done in a separated thread
	 * and this method returns immidiately.
	 */
	protected void keepAlive() {
		if (System.currentTimeMillis() - time > CHECK_CONNECTION_TIME) {
			NetSend n = new NetSend(this, NetServerThread.KEEP_ALIVE, null);
			n.start();
		}
	}
}
