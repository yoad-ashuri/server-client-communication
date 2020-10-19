//Yoad_Ashuri_311162606
package net;
import java.io.*;
import java.net.Socket;
import java.util.Locale;

public class HelloClient {
	Socket clientSocket;

	public static final int COUNT = 10;

	/**
	 * Connect to a remote host using TCP/IP and set {@link #clientSocket} to be the
	 * resulting socket object.
	 * 
	 * @param host remote host to connect to.
	 * @param port remote port to connect to.
	 * @throws IOException
	 */
	public void connect(String host, int port) throws IOException {
		clientSocket = new Socket(host,port);
	}

	/**
	 * Perform the following actions {@link #COUNT} times in a row: 1. Connect
	 * to the remote server (host:port). 2. Write the string in myname (followed
	 * by newline) to the server 3. Read one line of response from the server,
	 * write it to sysout (without the trailing newline) 4. Close the socket.
	 * 
	 * Then do the following (only once): 1. send
	 * {@link HelloServer#BYE_MESSAGE} to the server (followed by newline). 2.
	 * Read one line of response from the server, write it to sysout (without
	 * the trailing newline)
	 * 
	 * If there are any IO Errors during the execution, output {@link HelloServer#ERR_MESSAGE}
	 * (followed by newline) to sysout. If the error is inside the loop,
	 * continue to the next iteration of the loop. Otherwise exit the method.
	 * 
	 * @param sysout
	 * @param host
	 * @param port
	 * @param myname
	 */
	public void run(PrintStream sysout, String host, int port, String myname) {
		BufferedReader readerSrv = null;
		PrintWriter writerToServ = null;
		for (int i = 0; i < COUNT; i++) {
			try {
				this.connect(host, port);
				readerSrv = new BufferedReader(new InputStreamReader(this.clientSocket.getInputStream()));
				writerToServ = new PrintWriter(this.clientSocket.getOutputStream(), true);
				writerToServ.println(myname);
				String message = readerSrv.readLine();
				sysout.println(message);
				clientSocket.close();
			} catch (IOException e) {
				sysout.println(HelloServer.ERR_MESSAGE);
				continue;
			}
		}
		try {
			this.connect(host, port);
			readerSrv = new BufferedReader(new InputStreamReader(this.clientSocket.getInputStream()));
			writerToServ = new PrintWriter(this.clientSocket.getOutputStream(), true);
			writerToServ.println(HelloServer.BYE_MESSAGE);
			String s = readerSrv.readLine();
			sysout.print(s);
		}catch (IOException e){
			sysout.println(HelloServer.ERR_MESSAGE);
			e.printStackTrace();
			return;
		}
		finally {
			try {
				if (readerSrv != null) {
					readerSrv.close();
				}
				if (writerToServ != null) {
					writerToServ.close();
				}
				if (clientSocket != null) {
					clientSocket.close();
				}
			} catch (IOException e) {
				e.printStackTrace();
			}
		}
	}

	public static void main (String[]args){
		int port = 55832;
		HelloClient c = new HelloClient();
		c.run(System.out,"localhost",port,"Yoad");

	}
}
