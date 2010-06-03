package wordOfTheDay.client;

import java.io.Serializable;
import java.util.List;

import com.google.gwt.user.client.rpc.IsSerializable;

public class Word4 implements IsSerializable, Serializable, Comparable<Word4> {
	private String name;

	private String explanation;

	private List<String> usage;

	private int date;

	private boolean previousDayPossible;

	private boolean nextDayPossible;

	private String email;

	public Word4() {
	};

	public Word4(String name, String explanation, List<String> usage, int date,
			boolean previousDayPossible, boolean nexDayPossible, String email) {
		this.name = name;
		this.explanation = explanation;
		this.usage = usage;
		this.date = date;
		this.previousDayPossible = previousDayPossible;
		this.nextDayPossible = nexDayPossible;
		this.email = email;
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

	public int compareTo(Word4 compared) {
		return this.getDate() > compared.getDate() ? 1 : -1;
	}

	public boolean isPreviousDayPossible() {
		return previousDayPossible;
	}

	public boolean isNextDayPossible() {
		return nextDayPossible;
	}

	public String getEmail() {
		return email;
	}
}
