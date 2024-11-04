# Simple Chat Program

This is a simple chat program implemented in Java. The program consists of a server and multiple clients that can connect to the server and send messages to each other. The server listens for incoming client connections on a specified port and echoes back any messages it receives. The server also supports various commands that can be sent to it to control its behavior.


### Server
To run the server, you need to provide the port number on which the server should listen for incoming connections. 

The server supports the following commands:
- #quit: Terminate the server.
- #stop: Stop listening for incoming connections.
- #close: Close all existing connections and terminate the server.
- #setport: Set the port number on which the server should listen for incoming connections.
- #start: Start listening for incoming connections.
- #getport: Get the current port number on which the server is listening for incoming connections.
- #login: Login to the server using a specified login ID.

### Client
To run the server, you need to provide a loginID, and optionally a host name and port number to connect to
(defaults to  host: "localhost", port: 5555 )  

The client supports the following commands:
- #quit: Terminate the client.
- #logoff: Log off from the server.
- #login: Login to the server using a specified login ID.
- #setport: Sets port number to connect to.
- #sethost: Sets host name.
- #getport: returns port number.
- #gethost: returns host name.

Dependancy: Requires the Object Client-Server Framework (OCSF)
