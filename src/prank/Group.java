package prank;

import java.util.ArrayList;
import java.util.Collections;
import java.util.Iterator;
import java.util.List;

/**
 * A group of victims.
 * Each group is composed of at least 2 people with one of them designated as sender.
 */
public class Group {
	/**
	 * Randomly build groups from the given list of victims.
	 * There must be at least 2 people per group and at least one group to form.
	 * The last group may contain more people than the others.
	 *
	 * @param count   the number of groups to build
	 * @param victims the list of victims to use
	 */
	public static List<Group> buildGroups(int count, List<String> victims) {
		// Ensure we have at least one group
		if (count < 1) {
			throw new IllegalArgumentException("There must be at least one group to form");
		}

		// Compute victims per groups
		int victims_per_group = victims.size() / count;
		if (victims_per_group < 2) {
			throw new IllegalArgumentException("There must be at least two victims per groups");
		}

		// Shuffle the victims list to make it random
		Collections.shuffle(victims);

		// The generated groups
		List<Group> groups = new ArrayList<>(count);

		// Iterator over victims, allows to "consume" victim one by one
		Iterator<String> it = victims.iterator();

		// Build groups
		Group group = null;
		for (int i = 0; i < count; i++) {
			if (group != null) {
				groups.add(group);
			}

			group = new Group();

			for (int j = 0; j < victims_per_group && it.hasNext(); j++) {
				if (j == 0) {
					group.setSender(it.next());
				} else {
					group.addVictim(it.next());
				}
			}
		}

		//noinspection ConstantConditions
		it.forEachRemaining(group::addVictim);
		groups.add(group);

		return groups;
	}

	/**
	 * The sender from this group
	 */
	private String sender;

	/**
	 * The victims in this group, does not include the sender
	 */
	private List<String> victims = new ArrayList<>();

	public String getSender() {
		return sender;
	}

	public void setSender(String sender) {
		this.sender = sender;
	}

	public List<String> getVictims() {
		return victims;
	}

	public void addVictim(String victim) {
		victims.add(victim);
	}
}
