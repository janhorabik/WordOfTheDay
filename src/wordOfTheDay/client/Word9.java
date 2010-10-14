package wordOfTheDay.client;

import java.io.Serializable;
import java.util.List;

import com.google.gwt.user.client.rpc.IsSerializable;

public class Word9 implements IsSerializable, Serializable, Comparable<Word9> {
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
		this.labels = labels;
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
		return "Name: " + name + ", Tags: " + labels + ", Explanation: " + explanation + ", Usage: " + usage + ", Date: "
				+ date + ", Email: " + email;
	}

	public List<String> getLabels() {
		return labels;
	}
}
