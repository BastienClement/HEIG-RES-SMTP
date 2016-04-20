package config;

import message.MessageTemplate;

import java.io.BufferedReader;
import java.io.FileInputStream;
import java.io.FileReader;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.Properties;
import java.util.stream.Collectors;

public class ConfigManager {
	private String serverAddress;
	private int serverPort;
	private int groups;

	private List<String> victims = new ArrayList<>();
	private List<MessageTemplate> messages = new ArrayList<>();
	private List<String> witnesses = new ArrayList<>();

	private StringBuilder sb;

	public ConfigManager() throws IOException {
		readConfig("./config/config.properties");
		readVictims("./config/victims.txt");
		readMessages("./config/messages.txt");
	}

	private void readConfig(String file) throws IOException {
		FileInputStream fileInputStream = new FileInputStream(file);
		Properties properties = new Properties();
		properties.load(fileInputStream);

		this.serverAddress = properties.getProperty("serverAddress");
		this.serverPort = Integer.parseInt(properties.getProperty("serverPort"));
		this.groups = Integer.parseInt(properties.getProperty("groups"));

		Arrays.stream(properties.getProperty("witnesses").split(","))
		      .forEach(witnesses::add);
	}

	private void readVictims(String file) throws IOException {
		BufferedReader reader = new BufferedReader(new FileReader(file));
		victims = reader.lines().filter(line -> !line.isEmpty()).collect(Collectors.toList());
		reader.close();
	}

	private void readMessages(String file) throws IOException {
		BufferedReader reader = new BufferedReader(new FileReader(file));
		sb = new StringBuilder();
		reader.lines()
		      .filter(line -> {
			      if (line.equals("::END::")) {
				      messages.add(new MessageTemplate(sb.toString()));
				      sb = new StringBuilder();
				      return false;
			      }
			      return true;
		      })
		      .forEach(line -> {
			      sb.append(line);
			      sb.append("\r\n");
		      });
		reader.close();
	}

	public String getServerAddress() {
		return serverAddress;
	}

	public int getServerPort() {
		return serverPort;
	}

	public int getGroups() {
		return groups;
	}

	public List<MessageTemplate> getMessages() {
		return messages;
	}

	public List<String> getVictims() {
		return victims;
	}

	public List<String> getWitnesses() {
		return witnesses;
	}
}
