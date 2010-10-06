package wordOfTheDay.server;

import java.util.LinkedList;
import java.util.List;

import javax.jdo.annotations.IdentityType;
import javax.jdo.annotations.PersistenceCapable;
import javax.jdo.annotations.Persistent;
import javax.jdo.annotations.PrimaryKey;

@PersistenceCapable(identityType = IdentityType.APPLICATION)
public class PersistentWord22 {
	@Persistent
	private String name;

	@Persistent
	private String explanation;

	@Persistent
	private List<String> usage;

	@Persistent
	@PrimaryKey
	private String wordKey;

	@Persistent
	private String email;

	@Persistent
	private int date;

	@Persistent
	private List<String> labels;

	public PersistentWord22(String name, String explanation,
			List<String> usage, int date, String email, List<String> labels) {
		this.name = ValidationManager.validate(name);
		this.explanation = ValidationManager.validate(explanation);
		List<String> newList = new LinkedList<String>();
		for (String us : usage) {
			newList.add(ValidationManager.validate(us));
		}
		this.usage = newList;
		this.wordKey = generateKey(email, date);
		this.date = date;
		this.email = email;
		newList = new LinkedList<String>();
		for (String us : labels) {
			newList.add(ValidationManager.validate(us));
		}
		this.labels = newList;
	}

	public static String generateKey(String email2, int date2) {
		return email2 + "@" + date2;
	}

	public String getKey() {
		return wordKey;
	}

	public String getName() {
		return name;
	}

	public String getExplanation() {
		return explanation;
	}

	public List<String> getUsage() {
		return usage;
	}

	public int getDate() {
		return date;
	}

	public String getEmail() {
		return email;
	}

	public String toString() {
		return name + ", " + labels + ", " + explanation + ", " + usage + ", "
				+ date + ", " + email + ", " + wordKey;
	}

	public List<String> getLabels() {
		return labels;
	}
}
