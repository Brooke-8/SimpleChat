package edu.seg2105.edu.server.backend;
// This file contains material supporting section 3.7 of the textbook:

import java.io.EOFException;
import java.io.IOException;

import edu.seg2105.client.common.ChatIF;
// "Object Oriented Software Engineering" and is issued under the open-source
// license found at www.lloseng.com 
import ocsf.server.AbstractServer;
import ocsf.server.ConnectionToClient;

/**
 * This class overrides some of the methods in the abstract superclass in order
 * to give more functionality to the server.
 *
 * @author Dr Timothy C. Lethbridge
 * @author Dr Robert Lagani&egrave;re
 * @author Fran&ccedil;ois B&eacute;langer
 * @author Paul Holden
 */
public class EchoServer extends AbstractServer {
	// Class variables *************************************************
	
	/**
	 * The default port to listen on.
	 */
	final public static int DEFAULT_PORT = 5555;
	ChatIF serverUI;
	private boolean isClosed;
	
	// Constructors ****************************************************
	
	/**
	 * Constructs an instance of the echo server.
	 *
	 * @param port The port number to connect on.
	 */
	public EchoServer(int port, ChatIF serverUI) {
		super(port);
		this.serverUI = serverUI;
		try {
			listen();
			isClosed = false;
		} catch (Exception ex) {
			System.out.println("ERROR - Could not listen for clients");
		}
		
	}
	
	// Instance methods ************************************************
	
	/**
	 * This method handles any messages received from the client.
	 *
	 * @param msg    The message received from the client.
	 * @param client The connection from which the message originated.
	 */
	public void handleMessageFromClient(Object msg, ConnectionToClient client) {
		try {
			String message = msg.toString();
			System.out.println("Message received: " + message + " from " + client.getInfo("loginId"));
			if (message.startsWith("#")) {
				handleLoginId(message, client);
			} else {
				this.sendToAllClients(client.getInfo("loginId") + "> " + message);
			}
		} catch (Exception ex) {
			System.out.println("Exception: " + ex.getMessage());
		}
		
	}
	
	public void handleMessageFromServerUI(String message) {
		try {
			if (message.startsWith("#")) {
				handleCommand(message);
			} else {
				System.out.println("SERVER MSG> " + message);
				this.sendToAllClients("SERVER MSG> " + message);
			}
		} catch (Exception ex) {
			System.out.println("Exception: " + ex.getMessage());
		}
		
	}
	
	public void handleCommand(String command) {
		String[] commandParts = command.split(" ");
		switch (commandParts[0]) {
			case "#quit":
				try {
					close();
				} catch (IOException ex) {}
				System.exit(0);
				break;
			case "#stop":
				stopListening();
				break;
			case "#close":
				try {
					close();
				} catch (IOException ex) {}
				break;
			case "#setport":
				if (isClosed) {
					try {
						int port = Integer.parseInt(commandParts[1]);
						setPort(port);
					} catch (ArrayIndexOutOfBoundsException e) {
						System.out.println("Invalid input; Specify port");
					} catch (NumberFormatException e) {
						System.out.println("Invalid port format");
					}
					
				} else {
					System.out.println("Close before setting port");
				}
				break;
			case "#start":
				if (!isListening()) {
					try {
						listen();
						
					} catch (IOException e) {
						System.out.println("Could not listen");
					}
				} else {
					System.out.println("Already listening");
				}
				break;
			case "#getport":
				System.out.println("Current port: " + getPort());
				break;
			
		}
	}
	
	public void handleLoginId(String command, ConnectionToClient client) {
		String[] commandParts = command.split(" ");
		if (commandParts[0].equals("#login") && client != null) {
			try {
				if (client.getInfo("loggedInBefore").equals(true)) {
					try {
						client.sendToClient("Already recieved loginId");
						client.close();
					} catch (IOException e1) {}
				}
				client.setInfo("loginId", commandParts[1]);
				client.setInfo("loggedInBefore", true);
				System.out.println(commandParts[1] + " has logged on.");
				sendToAllClients(commandParts[1] + " has logged on.");
			} catch (ArrayIndexOutOfBoundsException e) {
				System.out.println("loginId not specified");
				try {
					client.close();
				} catch (IOException e1) {}
			}
		} else {
			System.out.println("Invalid login command");
		}
	}
	
	/**
	 * This method overrides the one in the superclass. Called when the server
	 * starts listening for connections.
	 */
	protected void serverStarted() {
		isClosed = false;
		System.out.println("Server listening for connections on port " + getPort());
	}
	
	/**
	 * This method overrides the one in the superclass. Called when the server stops
	 * listening for connections.
	 */
	protected void serverStopped() {
		System.out.println("Server has stopped listening for connections.");
	}
	
	// Class methods ***************************************************
	
	/**
	 * Hook method called each time a new client connection is accepted. The default
	 * implementation does nothing.
	 * 
	 * @param client the connection connected to the client.
	 */
	protected void clientConnected(ConnectionToClient client) {
		client.setInfo("loggedInBefore", false);
		System.out.println("A new client has connected to the server.");
	}
	
	/**
	 * Hook method called each time a client disconnects. The default implementation
	 * does nothing. The method may be overridden by subclasses but should remains
	 * synchronized.
	 *
	 * @param client the connection with the client.
	 */
	@Override
	synchronized protected void clientDisconnected(ConnectionToClient client) {
		System.out.println(client.getInfo("loginId") + " has disconnected.");
		
	}
	
	/**
	 * Hook method called each time an exception is thrown in a ConnectionToClient
	 * thread. The method may be overridden by subclasses but should remains
	 * synchronized.
	 *
	 * @param client    the client that raised the exception.
	 * @param Throwable the exception thrown.
	 */
	synchronized protected void clientException(ConnectionToClient client, Throwable exception) {
		if (exception instanceof EOFException) {
			clientDisconnected(client);
		} else {
			System.out.println("Client exception: " + exception);
		}
		
	}
	
	/**
	 * Hook method called when the server is closed. The default implementation does
	 * nothing. This method may be overriden by subclasses. When the server is
	 * closed while still listening, serverStopped() will also be called.
	 */
	protected void serverClosed() {
		isClosed = true;
	}
	
}
//End of EchoServer class
