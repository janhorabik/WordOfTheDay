package wordOfTheDay.server;

import java.util.LinkedList;
import java.util.List;

import javax.jdo.annotations.IdentityType;
import javax.jdo.annotations.PersistenceCapable;
import javax.jdo.annotations.Persistent;
import javax.jdo.annotations.PrimaryKey;

import com.google.appengine.api.datastore.Text;

@PersistenceCapable(identityType = IdentityType.APPLICATION)
public class PersistentDataModel {
	public String getEmail() {
		return email;
	}

	public int getSeqNum() {
		return seqNum;
	}

	public List<String> getFields() {
		return toString(fields);
	}

	@Persistent
	private String email;

	@Persistent
	private int seqNum;

	@PrimaryKey
	private String key;

	@Persistent
	private List<Text> fields;

	@Persistent
	private String name;

	public PersistentDataModel(String emailArg, int seqNumArg, String name,
			List<String> fieldsArg) {
		this.email = ValidationManager.validate(emailArg).getValue();
		this.seqNum = seqNumArg;
		this.name = name;
		this.key = generateKey(emailArg, seqNumArg);

		List<Text> newList = new LinkedList<Text>();
		for (String field : fieldsArg) {
			newList.add(ValidationManager.validate(field));
		}
		this.fields = newList;
	}

	public static String generateKey(String email2, int seqNum) {
		return email2 + "@" + seqNum;
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

	public String toString() {
		return email + ", " + seqNum + ", " + fields + ", " + key;
	}

	public String getName() {
		return name;
	}

	public void setName(String name2) {
		this.name = name2;
	}

}
