package message;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.StringReader;
import java.io.UncheckedIOException;

/**
 * A message template that can be used to build concrete messages.
 * A template holds a subject and a body, but no sender or recipients.
 */
public class MessageTemplate {
	private String subject;
	private String body;

	/**
	 * Constructs a MessageTemplate from a raw template source read
	 * from the configuration file.
	 *
	 * @param template template source, from the config file
	 */
	public MessageTemplate(String template) {
		try {
			BufferedReader reader = new BufferedReader(new StringReader(template.trim()));
			this.subject = reader.readLine();

			StringBuilder sb = new StringBuilder();
			reader.lines().forEach(line -> sb.append(line).append("\r\n"));
			this.body = sb.toString().trim();

			reader.close();
		} catch (IOException e) {
			throw new UncheckedIOException(e);
		}
	}

	public String getSubject() {
		return subject;
	}

	public String getBody() {
		return body;
	}
}
