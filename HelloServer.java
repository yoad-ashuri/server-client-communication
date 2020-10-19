//Yoad_Ashuri_311162606
package net;
import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.OutputStream;
import java.io.PrintStream;
import java.io.PrintWriter;
import java.net.ServerSocket;
import java.net.Socket;
import java.util.List;

public class HelloServer {

	public static final String ERR_MESSAGE = "IO Error!";
	public static final String LISTEN_MESSAGE = "Listening on port: ";
	public static final String HELLO_MESSAGE = "hello ";
	public static final String BYE_MESSAGE = "bye";
	ServerSocket socket;

	public ServerSocket getServerSocket() {
		return this.socket;
	}

	/**
	 * Listen on the first available port in a given list.
	 *
	 * <p>Note: Should not throw exceptions due to ports being unavailable</p>
	 *
	 * @return The port number chosen, or -1 if none of the ports were available.
	 */
	public int listen(List<Integer> portList) throws IOException {

		//init the chosen port
		int chosen = -1;
		//for each port in the list try to creat the socket
		for (int port : portList) {
			try {
				//if the port isn't available exception will be throw and i will pass to the next port
				socket = new ServerSocket(port);
				// save the port and exit
				chosen = port;
				break;
			} catch (IOException e) {
				continue;
			}
		}
		return chosen;
	}


	/**
	 * Listen on an available port.
	 * Any available port may be chosen.
	 *
	 * @return The port number chosen.
	 */
	public int listen() throws IOException {
		int port = -1;
		while (true) {
			try {
				socket = new ServerSocket(0);
				port = socket.getLocalPort();
				break;
			} catch (IOException e) {
				continue;
			}
		}
		return port;
	}


	/**
	 * 1. Start listening on an open port. Write {@link #LISTEN_MESSAGE} followed by the port number (and a newline) to sysout.
	 * If there's an IOException at this stage, exit the method.
	 * <p>
	 * 2. Run in a loop;
	 * in each iteration of the loop, wait for a client to connect,
	 * then read a line of text from the client. If the text is {@link #BYE_MESSAGE},
	 * send {@link #BYE_MESSAGE} to the client and exit the loop. Otherwise, send {@link #HELLO_MESSAGE}
	 * to the client, followed by the string sent by the client (and a newline)
	 * After sending the hello message, close the client connection and wait for the next client to connect.
	 * <p>
	 * If there's an IOException while in the loop, or if the client closes the connection before sending a line of text,
	 * send the text {@link #ERR_MESSAGE} to sysout, but continue to the next iteration of the loop.
	 * <p>
	 * *: in any case, before exiting the method you must close the server socket.
	 *
	 * @param sysout a {@link PrintStream} to which the console messages are sent.
	 */
	public void run(PrintStream sysout) {
		Socket soc = null;
		BufferedReader in = null;
		PrintWriter clintOut = null;
		try {
			this.listen();
			sysout.println(LISTEN_MESSAGE + this.socket.getLocalPort());
		}catch (IOException e) {
			return;
		}
			while (true) {
				try {
				soc = socket.accept();
				in = new BufferedReader(new InputStreamReader(soc.getInputStream()));
				clintOut = new PrintWriter(soc.getOutputStream(), true);
				String message = in.readLine();
				if (message.equalsIgnoreCase(BYE_MESSAGE)) {
					clintOut.println(BYE_MESSAGE);
					break;
				}
				clintOut.println(HELLO_MESSAGE + message);
				} catch (IOException e) {
					sysout.println(ERR_MESSAGE);
				}
				//close the open streams and sockets if there are
				finally {
					try {
						if (soc != null){
							soc.close();
						}
						if (in != null){
							in.close();
						}
						if (clintOut != null){
							clintOut.close();
						}
					}catch (IOException e){
						e.printStackTrace();
					}
				}
			}
	}





	public static void main(String args[]) {
		HelloServer server = new HelloServer();

		server.run(System.err);
	}

}
