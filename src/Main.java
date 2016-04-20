import config.ConfigManager;
import mail.Sender;
import message.Message;
import message.MessageTemplate;
import prank.Group;

import java.io.IOException;
import java.net.InetSocketAddress;
import java.util.List;
import java.util.Random;

/**
 * Main class of the application
 */
public class Main {
	/**
	 * Random number generator used to select the message to send.
	 */
	private static Random rand = new Random();

	public static void main(String... args) throws IOException {
		// Create the config manager and read configuration files
		ConfigManager config = new ConfigManager();

		// Display config for debugging
		System.out.printf("Server: %s %s\n", config.getServerAddress(), config.getServerPort());
		System.out.printf("Groups: %s\n\n", config.getGroups());

		// Create the sender instance
		String address = config.getServerAddress();
		int port = config.getServerPort();
		InetSocketAddress server = new InetSocketAddress(address, port);
		Sender sender = new Sender(server);

		// Build groups from victims and group count
		List<Group> groups = Group.buildGroups(config.getGroups(), config.getVictims());

		// Get list of message templates
		List<MessageTemplate> templates = config.getMessages();

		// For each group, send a mail
		for (Group group : groups) {
			// Select template and create message instance
			MessageTemplate template = randomTemplate(templates);
			Message m = Message.fromTemplate(template);

			// Set properties
			m.setFrom(group.getSender());
			group.getVictims().forEach(m::addTo);
			config.getWitnesses().forEach(m::addCc);

			// Send
			sender.send(m);
		}
	}

	/**
	 * Selects a random template for the given list of templates
	 *
	 * @param templates a list of mail templates
	 */
	private static MessageTemplate randomTemplate(List<MessageTemplate> templates) {
		return templates.get(rand.nextInt(templates.size()));
	}
}
