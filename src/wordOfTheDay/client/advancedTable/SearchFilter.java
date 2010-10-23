package wordOfTheDay.client.advancedTable;

import java.util.LinkedList;
import java.util.List;

import wordOfTheDay.client.Word9;

public class SearchFilter implements DataFilter {
	private String search;

	public SearchFilter(String search) {
		this.search = search.toUpperCase();
	}

	public boolean accept(Word9 word) {
		String name = word.getName();
		if (name == null) {
			name = "";
		}
		String explanation = word.getExplanation();
		if (explanation == null) {
			explanation = "";
		}
		List<String> labels = word.getLabels();
		if (labels == null) {
			labels = new LinkedList<String>();
		}
		List<String> usage = word.getUsage();
		if (usage == null) {
			usage = new LinkedList<String>();
		}
		if (contains(name, this.search) || contains(explanation, this.search)
				|| contains(labels, search) || contains(usage, search)) {
			return true;
		}
		return false;
	}

	private static boolean contains(String data, String value) {
		return data.toUpperCase().contains(value);
	}

	private static boolean contains(List<String> data, String value) {
		for (String element : data) {
			if (element.toUpperCase().contains(value)) {
				return true;
			}
		}
		return false;
	}

}
