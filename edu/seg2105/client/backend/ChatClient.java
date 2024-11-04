// This file contains material supporting section 3.7 of the textbook:
// "Object Oriented Software Engineering" and is issued under the open-source
// license found at www.lloseng.com 

package edu.seg2105.client.backend;

import java.io.IOException;

import edu.seg2105.client.common.ChatIF;
import ocsf.client.AbstractClient;

/**
 * This class overrides some of the methods defined in the abstract superclass
 * in order to give more functionality to the client.
 *
 * @author Dr Timothy C. Lethbridge
 * @author Dr Robert Lagani&egrave;
 * @author Fran&ccedil;ois B&eacute;langer
 */
public class ChatClient extends AbstractClient {
	// Instance variables **********************************************
	
	/**
	 * The interface type variable. It allows the implementation of the display
	 * method in the client.
	 */
	ChatIF clientUI;
	private String loginId;
	
	// Constructors ****************************************************
	
	/**
	 * Constructs an instance of the chat client.
	 *
	 * @param loginId  The loginId of the user.
	 * @param host     The server to connect to.
	 * @param port     The port number to connect on.
	 * @param clientUI The interface type variable.
	 */
	
	public ChatClient(String loginId, String host, int port, ChatIF clientUI) throws IOException {
		super(host, port); // Call the superclass constructor
		this.clientUI = clientUI;
		this.loginId = loginId;
		openConnection();
	}
	
	// Instance methods ************************************************
	
	/**
	 * This method handles all data that comes in from the server.
	 *
	 * @param msg The message from the server.
	 */
	public void handleMessageFromServer(Object msg) {
		clientUI.display(msg.toString());
		
	}
	
	/**
	 * This method handles all data coming from the UI
	 *
	 * @param message The message from the UI.
	 */
	public void handleMessageFromClientUI(String message) {
		try {
			if (message.startsWith("#")) {
				handleCommand(message);
			} else {
				sendToServer(message);
			}
			
		} catch (IOException e) {
			clientUI.display("Could not send message to server.  Terminating client.");
			quit();
		}
	}
	
	public void handleCommand(String command) {
		String[] commandParts = command.split(" ");
		switch (commandParts[0]) {
			case "#quit":
				quit();
				break;
			case "#logoff":
				try {
					closeConnection();
				} catch (IOException e) {}
				break;
			case "#login":
				try {
					openConnection();
				} catch (IOException e) {
					System.out.println("Could not log in");
				}
				break;
			case "#sethost":
				if (!isConnected()) {
					try {
						String host = commandParts[1];
						setHost(host);
					} catch (ArrayIndexOutOfBoundsException e) {
						System.out.println("Invalid input; Specify host");
					}
				} else {
					System.out.println("Disconnect before setting host");
				}
				break;
			case "#setport":
				if (!isConnected()) {
					try {
						int port = Integer.parseInt(commandParts[1]);
						setPort(port);
					} catch (ArrayIndexOutOfBoundsException e) {
						System.out.println("Invalid input; Specify port");
					} catch (NumberFormatException e) {
						System.out.println("Invalid port format");
					}
					
				} else {
					System.out.println("Disconnect before setting port");
				}
				break;
			case "#gethost":
				System.out.println("Current host: " + getHost());
				break;
			case "#getport":
				System.out.println("Current port: " + getPort());
				break;
			default:
				System.out.println("Invalid Command");
		}
	}
	
	/**
	 * This method terminates the client.
	 */
	public void quit() {
		try {
			closeConnection();
		} catch (IOException e) {}
		System.exit(0);
	}
	
	/**
	 * Implements the hook method called each time an exception is thrown by the
	 * client's thread that is waiting for messages from the server. The method may
	 * be overridden by subclasses.
	 * 
	 * @param exception the exception raised.
	 */
	@Override
	protected void connectionException(Exception exception) {
		clientUI.display("The server has shutdown");
		System.exit(0);
	}
	
	/**
	 * Hook method called after the connection has been closed. The default
	 * implementation does nothing. The method may be overriden by subclasses to
	 * perform special processing such as cleaning up and terminating, or attempting
	 * to reconnect.
	 */
	protected void connectionClosed() {
		clientUI.display("The connection has been closed");
	}
	
	/**
	 * Hook method called after a connection has been established. The default
	 * implementation does nothing. It may be overridden by subclasses to do
	 * anything they wish.
	 */
	protected void connectionEstablished() {
		try {
			sendToServer("#login " + loginId);
		} catch (IOException e) {}
		
	}
}
//End of ChatClient class
