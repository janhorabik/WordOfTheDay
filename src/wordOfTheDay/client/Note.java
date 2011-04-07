package wordOfTheDay.client;

import java.io.Serializable;
import java.util.LinkedList;
import java.util.List;

import com.google.gwt.user.client.rpc.IsSerializable;

public class Note implements IsSerializable, Serializable, Comparable<Note> {
	public int getDataModelSeqNum() {
		return dataModelSeqNum;
	}

	public static final int SHORT_LEN = 45;

	public static final int SHORT_LEN_LABELS = 20;

	private String email;

	private int seqNum;

	private List<String> fields;

	private int dataModelSeqNum;

	private List<String> labels;

	public Note() {
	};

	public Note(String emailArg, int seqNumArg, List<String> fieldsArg,
			int dataModelSeqNumArg, List<String> labelsArg) {
		this.email = emailArg;
		this.seqNum = seqNumArg;
		this.fields = fieldsArg;
		this.dataModelSeqNum = dataModelSeqNumArg;
		this.labels = new LinkedList<String>();
		for (String label : labelsArg) {
			if (!label.trim().equals(""))
				this.labels.add(label);
		}

	}

	public String getField(int i) {
		if (i < this.fields.size()) {
			return this.fields.get(i);
		}
		return "";
	}

	public String getShortField(int i) {
		return getShort(getField(i));
	}

	public List<String> getFields() {
		return this.fields;
	}

	private String getShort(String field) {
		return getShort(field, Note.SHORT_LEN);
	}

	private String getShort(String field, int len) {
		String ret = field.substring(0, Math.min(field.length(), len));
		if (ret.length() != field.length())
			ret = ret.concat("...");
		return ret;
	}

	private List<String> getShort(List<String> list, int maxLen) {
		List<String> ret = new LinkedList<String>();
		int len = 0;
		for (String string : list) {
			String v = getShort(string);
			int oldlen = len;
			len += v.length();
			if (len < maxLen)
				ret.add(v);
			else {
				ret.add(getShort(v, maxLen - oldlen));
				break;
			}
		}
		return ret;
	}

	public int compareTo(Note compared) {
		return this.getSeqNum() > compared.getSeqNum() ? 1 : -1;
	}

	public int getSeqNum() {
		return this.seqNum;
	}

	public String getEmail() {
		return email;
	}

	public String toString() {
		return email + ", " + seqNum + ", " + dataModelSeqNum + ", " + fields
				+ labels;
	}

	public List<String> getLabels() {
		return labels;
	}

	private List<String> getLastDots(List<String> list) {
		List<String> ret = new LinkedList<String>();
		for (String string : list) {
			String[] elements = string.split(":");
			String v = elements[elements.length - 1];
			ret.add(v);
		}
		return ret;
	}

	public List<String> getShortLabels() {
		List<String> lastDots = getLastDots(labels);
		return getShort(lastDots, Note.SHORT_LEN_LABELS);
	}

	public void setField(int j, String value) {
		fields.remove(j);
		fields.add(j, value);
	}

	public void addLabel(String string) {
		if (!labels.contains(string))
			labels.add(string);
	}

	public String getLabelsAsString() {
		String ret = "";
		for (String label : labels) {
			ret += "," + label;
		}
		return ret;
	}

	public void removeLabel(String label) {
		labels.remove(label);
	}

	public void setSeqNum(Integer result) {
		this.seqNum = result;
	}

	public void setFields(List<String> fields2) {
		this.fields = fields2;
		
	}

	public void setLabels(List<String> labels2) {
		this.labels = labels2;
	}
}
