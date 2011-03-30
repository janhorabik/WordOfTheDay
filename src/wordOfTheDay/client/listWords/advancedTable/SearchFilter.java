package wordOfTheDay.client.listWords.advancedTable;

import java.util.LinkedList;
import java.util.List;

import wordOfTheDay.client.Note;

public class SearchFilter implements DataFilter {
	private String search;

	public SearchFilter(String search) {
		this.search = search.toUpperCase();
	}

	public boolean accept(Note note) {
		List<String> labels = note.getLabels();
		if (labels == null) {
			labels = new LinkedList<String>();
		}
		List<String> fields = note.getFields();
		if (fields == null) {
			fields = new LinkedList<String>();
		}
		if (contains(labels, search) || contains(fields, search)) {
			return true;
		}
		return false;
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
