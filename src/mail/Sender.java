package mail;

import message.Message;

import java.io.*;
import java.net.InetSocketAddress;
import java.net.Socket;
import java.util.Arrays;
import java.util.List;

/**
 * A mail sender using the given SMTP server to send mails.
 */
public class Sender {
	/**
	 * Server address
	 */
	private InetSocketAddress server;

	/**
	 * Socket writer
	 */
	private PrintWriter writer;

	/**
	 * Socket reader
	 */
	private BufferedReader reader;

	/**
	 * Last line from the socket
	 */
	private String line;

	public Sender(InetSocketAddress server) {
		this.server = server;
	}

	/**
	 * Reads one line from the socket.
	 * Also prints it to stdout for debugging.
	 *
	 * @throws IOException
	 */
	private String read() throws IOException {
		line = reader.readLine();
		System.out.println("<  " + line);
		return line;
	}

	/**
	 * Writes one line to the socket.
	 * Also prints it to stdout for debugging.
	 *
	 * @param format format string
	 * @param args   arguments for format
	 */
	private void write(String format, Object... args) {
		String line = String.format(format, args);
		writer.printf(line + "\r\n", args);
		writer.flush();
		System.out.println(">  " + line);
	}

	/**
	 * Add commas between elements of a list.
	 *
	 * @param list a list of string to separate with commas
	 */
	private String commatize(List<String> list) {
		StringBuilder sb = new StringBuilder();
		list.stream().forEach(item -> sb.append(item).append(", "));
		String commatized = sb.toString();
		return commatized.substring(0, commatized.length() - 2);
	}

	/**
	 * Sends a message, using the sender's SMTP server.
	 *
	 * @param message the message to send
	 * @throws IOException if an error occur while sending the message
	 */
	public synchronized void send(Message message) throws IOException {
		// Open socket
		Socket socket = new Socket(server.getAddress(), server.getPort());

		// Create reader and writer
		writer = new PrintWriter(new OutputStreamWriter(socket.getOutputStream(), "UTF-8"));
		reader = new BufferedReader(new InputStreamReader(socket.getInputStream(), "UTF-8"));

		// Handshake
		read();
		write("EHLO localhost");

		read();
		if (!line.startsWith("250")) throw new IOException(line);
		while (line.startsWith("250-")) read();

		// Recipients
		write("MAIL FROM:%s", message.getFrom());
		read();

		for (List<String> list : Arrays.asList(message.getTo(), message.getCc(), message.getBcc())) {
			if (list.isEmpty()) continue;
			for (String to : list) {
				write("RCPT TO:%s", to);
				read();
			}
		}

		// Begin message
		write("DATA");
		read();

		// Headers
		write("Content-Type: text/plain; charset=\"utf-8\"");
		write("From: %s", message.getFrom());
		write("To: %s", commatize(message.getTo()));
		if (message.hasCc())
			write("Cc: %s", commatize(message.getCc()));
		write("Subject: %s", message.getSubject());
		write("");

		// Body
		write(message.getBody());

		// End
		write(".");
		read();

		// Quit
		write("QUIT");

		reader.close();
		writer.close();
		socket.close();
	}
}
