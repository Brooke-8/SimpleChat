package edu.seg2105.edu.server.ui;

import java.util.Scanner;

import edu.seg2105.client.common.ChatIF;
import edu.seg2105.edu.server.backend.EchoServer;

public class ServerConsole implements ChatIF {
	
	/**
	 * The default port number.
	 */
	final public static int DEFAULT_PORT = 5555;
	/**
	 * The server instance.
	 */
	EchoServer server;
	/**
	 * The scanner used for reading from the console.
	 */
	Scanner fromConsole;
	
	/**
	 * Constructor for the ServerConsole class.
	 * 
	 * @param host
	 * @param port
	 */
	public ServerConsole(int port) {
		server = new EchoServer(port, this);
		fromConsole = new Scanner(System.in);
	}
	
	/**
	 * This method waits for input from the console. Once it is received, it sends
	 * it to the server's message handler.
	 */
	public void accept() {
		try {
			String message;
			while (true) {
				message = fromConsole.nextLine();
				server.handleMessageFromServerUI(message);
			}
		} catch (Exception ex) {
			System.out.println("Unexpected error while reading from console");
		}
	}
	
	/**
	 * This method overrides the method in the ChatIF interface. It displays a
	 * message onto the screen.
	 * 
	 * @param mesesage The string to be displayed.
	 */
	@Override
	public void display(String message) {
		System.out.println("> " + message);
	}
	
	/**
	 * This method is responsible for the creation of the server instance UI
	 *
	 * @param args[0] The port number to listen on. Defaults to 5555 if no argument
	 *                is entered.
	 */
	public static void main(String[] args) {
		int port = 0;
		try {
			port = Integer.parseInt(args[0]);
		} catch (Throwable e) {
			port = DEFAULT_PORT;
		}
		ServerConsole sv = new ServerConsole(port);
		sv.accept();
	}
	
}
