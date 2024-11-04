package edu.seg2105.client.ui;
// This file contains material supporting section 3.7 of the textbook:

// "Object Oriented Software Engineering" and is issued under the open-source
// license found at www.lloseng.com 
import java.io.IOException;
import java.util.Scanner;

import edu.seg2105.client.backend.ChatClient;
import edu.seg2105.client.common.ChatIF;

/**
 * This class constructs the UI for a chat client. It implements the chat
 * interface in order to activate the display() method. Warning: Some of the
 * code here is cloned in ServerConsole
 *
 * @author Fran&ccedil;ois B&eacute;langer
 * @author Dr Timothy C. Lethbridge
 * @author Dr Robert Lagani&egrave;re
 */
public class ClientConsole implements ChatIF {
	// Class variables *************************************************
	
	/**
	 * The default port to connect on.
	 */
	final public static int DEFAULT_PORT = 5555;
	
	// Instance variables **********************************************
	
	/**
	 * The instance of the client that created this ConsoleChat.
	 */
	ChatClient client;
	
	/**
	 * Scanner to read from the console
	 */
	Scanner fromConsole;
	
	// Constructors ****************************************************
	
	/**
	 * Constructs an instance of the ClientConsole UI.
	 *
	 * @param loginId The ID of the client.
	 * @param host    The host to connect to.
	 * @param port    The port to connect on.
	 */
	public ClientConsole(String loginId, String host, int port) {
		try {
			if (loginId.equals("")) {
				System.out.println("Error: No login ID specified. Connection aborted.");
				System.exit(1);
			}
			client = new ChatClient(loginId, host, port, this);
			
		} catch (IOException exception) {
			System.out.println("Error: Can't setup connection!" + " Terminating client.");
			System.exit(1);
		}
		
		// Create scanner object to read from console
		fromConsole = new Scanner(System.in);
	}
	
	// Instance methods ************************************************
	
	/**
	 * This method waits for input from the console. Once it is received, it sends
	 * it to the client's message handler.
	 */
	public void accept() {
		try {
			
			String message;
			
			while (true) {
				message = fromConsole.nextLine();
				client.handleMessageFromClientUI(message);
			}
		} catch (Exception ex) {
			System.out.println("Unexpected error while reading from console!");
		}
	}
	
	/**
	 * This method overrides the method in the ChatIF interface. It displays a
	 * message onto the screen.
	 *
	 * @param message The string to be displayed.
	 */
	public void display(String message) {
		System.out.println(message);
	}
	
	// Class methods ***************************************************
	
	/**
	 * This method is responsible for the creation of the Client UI.
	 *
	 * @param args[0] The loginId of the user.
	 * @param args[1] The host to connect to.
	 * @param args[2] The port to connect on.
	 */
	public static void main(String[] args) {
		String loginId = "";
		String host = "";
		int port = 0;
		
		if (args.length == 0) {
			loginId = "";
			host = "localhost";
			port = 5555;
		} else if (args.length == 1) {
			loginId = args[0];
			host = "localhost";
			port = 5555;
		} else if (args.length == 2) {
			loginId = args[0];
			host = args[1];
			port = 5555;
		} else if (args.length == 3) {
			loginId = args[0];
			host = args[1];
			try {
				port = Integer.parseInt(args[2]);
			} catch (NumberFormatException e) {
				host = "localhost";
				port = DEFAULT_PORT;
			}
		}
		
		ClientConsole chat = new ClientConsole(loginId, host, port);
		chat.accept(); // Wait for console data
	}
}
//End of ConsoleChat class
