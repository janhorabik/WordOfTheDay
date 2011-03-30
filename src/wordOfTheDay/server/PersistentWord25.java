package wordOfTheDay.server;

import java.util.LinkedList;
import java.util.List;

import javax.jdo.annotations.IdentityType;
import javax.jdo.annotations.PersistenceCapable;
import javax.jdo.annotations.Persistent;
import javax.jdo.annotations.PrimaryKey;

import com.google.appengine.api.datastore.Text;

@PersistenceCapable(identityType = IdentityType.APPLICATION)
public class PersistentWord25 {
	@Persistent
	private Text name;

	@Persistent
	private Text explanation;

	@Persistent
	private List<Text> usage;

	@Persistent
	@PrimaryKey
	private String wordKey;

	@Persistent
	private String email;

	@Persistent
	private int date;

	@Persistent
	private List<Text> labele;

	public PersistentWord25(String name, String explanation,
			List<String> usage, int date, String email, List<String> labels) {
		this.name = ValidationManager.validate(name);
		this.explanation = ValidationManager.validate(explanation);
		List<Text> newList = new LinkedList<Text>();
		for (String us : usage) {
			newList.add(ValidationManager.validate(us));
		}
		this.usage = newList;
		this.wordKey = generateKey(email, date);
		this.date = date;
		this.email = email;
		newList = new LinkedList<Text>();
		for (String us : labels) {
			newList.add(ValidationManager.validate(us));
		}
		this.labele = newList;
	}

	public static String generateKey(String email2, int date2) {
		return email2 + "@" + date2;
	}

	public String getKey() {
		return wordKey;
	}

	public String getName() {
		return name.getValue();
	}

	public String getExplanation() {
		return explanation.getValue();
	}

	private static List<String> toString(List<Text> list) {
		List<String> ret = new LinkedList<String>();
		for (Text text : list) {
			ret.add(text.getValue());
		}
		return ret;
	}

	private static List<Text> toText(List<String> list) {
		List<Text> ret = new LinkedList<Text>();
		for (String s : list) {
			ret.add(ValidationManager.validate(s));
		}
		return ret;
	}

	public List<String> getUsage() {
		return toString(this.usage);
	}

	public int getDate() {
		return date;
	}

	public String getEmail() {
		return email;
	}

	public String toString() {
		return name + ", " + labele + ", " + explanation + ", " + usage + ", "
				+ date + ", " + email + ", " + wordKey;
	}

	public List<String> getLabels() {
		return toString(this.labele);
	}

	public void setLabels(List<String> labels) {
		this.labele = toText(labels);
	}
}
