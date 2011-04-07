package wordOfTheDay.client.listWords.notesTable;

import java.util.LinkedList;
import java.util.List;

import wordOfTheDay.client.Note;
import wordOfTheDay.client.Word9;

public class LabelBeginFilter implements DataFilter {

	private String search;

	public LabelBeginFilter(String search) {
		this.search = search.toUpperCase();
	}

	public boolean accept(Note note) {
		List<String> labels = note.getLabels();
		if (labels == null) {
			labels = new LinkedList<String>();
		}
		for (String label : labels) {
			if (label.toUpperCase().startsWith(this.search))
				return true;
		}
		return false;
	}
}