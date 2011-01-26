package wordOfTheDay.client;

import java.io.Serializable;
import java.util.LinkedList;
import java.util.List;

import com.google.gwt.user.client.rpc.IsSerializable;

public class Word9 implements IsSerializable, Serializable, Comparable<Word9> {
	public static final int SHORT_LEN = 50;

	private String name;

	private String explanation;

	private List<String> usage;

	private Integer date;

	private boolean previousDayPossible;

	private boolean nextDayPossible;

	private boolean isToday;

	private String email;

	private List<String> labels;

	public Word9() {
	};

	public Word9(String name, String explanation, List<String> usage, int date,
			boolean previousDayPossible, boolean nexDayPossible,
			boolean isToday, String email, List<String> labels) {
		this.name = name;
		this.explanation = explanation;
		this.usage = usage;
		this.date = date;
		this.previousDayPossible = previousDayPossible;
		this.isToday = isToday;
		this.nextDayPossible = nexDayPossible;
		this.email = email;
		this.labels = new LinkedList<String>();
		for (String label : labels) {
			if (!label.trim().equals(""))
				this.labels.add(label);
		}
	}

	public String getName() {
		return name;
	}

	public String getShortName() {
		return getShort(name);
	}

	public String getExplanation() {
		return explanation;
	}

	public String getShortExplanation() {
		return getShort(explanation);
	}

	private String getShort(String field) {
		return getShort(field, Word9.SHORT_LEN);
	}

	private String getShort(String field, int len) {
		String ret = field.substring(0, Math.min(field.length(), len));
		if (ret.length() != field.length())
			ret = ret.concat("...");
		return ret;

	}

	private List<String> getShort(List<String> list) {
		List<String> ret = new LinkedList<String>();
		int len = 0;
		for (String string : list) {
			String v = getShort(string);
			int oldlen = len;
			len += v.length();
			if (len < Word9.SHORT_LEN)
				ret.add(v);
			else {
				ret.add(getShort(v, Word9.SHORT_LEN - oldlen));
				break;
			}
		}
		return ret;
	}

	public List<String> getUsage() {
		return usage;
	}

	public List<String> getShortUsage() {
		return getShort(this.usage);
	}

	public int getDate() {
		return date;
	}

	public int compareTo(Word9 compared) {
		return this.getDate() > compared.getDate() ? 1 : -1;
	}

	public boolean isPreviousDayPossible() {
		return previousDayPossible;
	}

	public boolean isNextDayPossible() {
		return nextDayPossible;
	}

	public boolean getIsToday() {
		return isToday;
	}

	public String getEmail() {
		return email;
	}

	public void setPreviousDayPossible(boolean value) {
		previousDayPossible = value;
	}

	public String toString() {
		return "Name: " + name + ", Tags: " + labels + ", Explanation: "
				+ explanation + ", Usage: " + usage + ", Date: " + date
				+ ", Email: " + email;
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
		return getShort(lastDots);
	}
}
