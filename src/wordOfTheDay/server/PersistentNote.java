package wordOfTheDay.server;

import java.util.LinkedList;
import java.util.List;

import javax.jdo.annotations.IdentityType;
import javax.jdo.annotations.PersistenceCapable;
import javax.jdo.annotations.Persistent;
import javax.jdo.annotations.PrimaryKey;

import com.google.appengine.api.datastore.Text;

@PersistenceCapable(identityType = IdentityType.APPLICATION)
public class PersistentNote {
	@Persistent
	private String email;

	public String getEmail() {
		return email;
	}

	public int getSeqNum() {
		return seqNum;
	}

	public List<String> getFields() {
		return toString(fields);
	}

	public int getDataModelSeqNum() {
		return dataModelSeqNum;
	}

	public List<String> getLabels() {
		return toString(labels);
	}

	@Persistent
	private int seqNum;

	@PrimaryKey
	private String key;

	@Persistent
	private List<Text> fields;

	@Persistent
	private int dataModelSeqNum;

	@Persistent
	private List<Text> labels;

	public PersistentNote(String emailArg, int seqNumArg,
			List<String> fieldsArg, int dataModelSeqNumArg,
			List<String> labelsArg) {
		this.email = ValidationManager.validate(emailArg).getValue();
		this.seqNum = seqNumArg;
		this.key = generateKey(emailArg, seqNumArg);

		List<Text> newList = new LinkedList<Text>();
		for (String field : fieldsArg) {
			newList.add(ValidationManager.validate(field));
		}
		this.fields = newList;
		this.dataModelSeqNum = dataModelSeqNumArg;
		List<Text> labelsList = new LinkedList<Text>();
		for (String label : labelsArg) {
			labelsList.add(ValidationManager.validate(label));
		}
		this.labels = labelsList;
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
		return email + ", " + seqNum + ", " + fields + ", " + key + ","
				+ dataModelSeqNum + "," + labels;
	}

	public void setLabels(List<String> labels2) {
		this.labels = toText(labels2);

	}
	
	public void setFields(List<String> fieldsNew)
	{
		this.fields = toText(fieldsNew);
	}

}
