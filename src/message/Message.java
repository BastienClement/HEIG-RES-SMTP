package message;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

/**
 * A single mail message
 */
public class Message {
	/**
	 * Construct a new message from a message template.
	 *
	 * @param template the template of the new message
	 */
	public static Message fromTemplate(MessageTemplate template) {
		return new Message(template.getSubject(), template.getBody());
	}

	/**
	 * Sender's address
	 */
	private String from;

	/**
	 * Mail subject
	 */
	private String subject;

	/**
	 * Mail body
	 */
	private String body;

	/**
	 * List of To recipients
	 */
	private List<String> to = new ArrayList<>();

	/**
	 * List of Cc recipients
	 */
	private List<String> cc = new ArrayList<>();

	/**
	 * List of Bcc recipients
	 */
	private List<String> bcc = new ArrayList<>();

	/**
	 * Constructs a new message with the given subject and body.
	 * Private, must be called from static method fromTemplate().
	 *
	 * @param subject the message subject
	 * @param body    the message body
	 */
	private Message(String subject, String body) {
		this.subject = subject;
		this.body = body;
	}

	public String getFrom() {
		return from;
	}

	public Message setFrom(String from) {
		this.from = from;
		return this;
	}

	public String getSubject() {
		return subject;
	}

	public String getBody() {
		return body;
	}

	public List<String> getTo() {
		return to;
	}

	public Message addTo(String... to) {
		this.to.addAll(Arrays.asList(to));
		return this;
	}

	public List<String> getCc() {
		return cc;
	}

	public boolean hasCc() {
		return cc.size() > 0;
	}

	public Message addCc(String... cc) {
		this.cc.addAll(Arrays.asList(cc));
		return this;
	}

	public List<String> getBcc() {
		return bcc;
	}

	public boolean hasBcc() {
		return bcc.size() > 0;
	}

	public Message addBcc(String... bcc) {
		this.bcc.addAll(Arrays.asList(bcc));
		return this;
	}
}
