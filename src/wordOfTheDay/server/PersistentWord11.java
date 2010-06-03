package wordOfTheDay.server;

import java.util.LinkedList;
import java.util.List;

import javax.jdo.annotations.IdGeneratorStrategy;
import javax.jdo.annotations.IdentityType;
import javax.jdo.annotations.PersistenceCapable;
import javax.jdo.annotations.Persistent;
import javax.jdo.annotations.PrimaryKey;

import com.google.appengine.api.datastore.Key;

@PersistenceCapable(identityType = IdentityType.APPLICATION)
public class PersistentWord11 {
	@PrimaryKey
	@Persistent(valueStrategy = IdGeneratorStrategy.IDENTITY)
	private Key key;

	@Persistent
	private String name;

	@Persistent
	private String explanation;

	@Persistent
	private List<String> usage;

	@Persistent
	private int date;

	@Persistent
	private String email;

	public PersistentWord11(String name, String explanation, List<String> usage,
			int date, String email) {
		this.name = ValidationManager.validate(name);
		this.explanation = ValidationManager.validate(explanation);
		List<String> newList = new LinkedList<String>();
		for (String us : usage) {
			newList.add(ValidationManager.validate(us));
		}
		this.usage = newList;
		this.date = date;
		this.email = email;
	}

	public Key getKey() {
		return key;
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
}
