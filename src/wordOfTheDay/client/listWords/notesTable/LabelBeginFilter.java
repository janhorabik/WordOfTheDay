package wordOfTheDay.client.listWords.notesTable;

import java.util.LinkedList;
import java.util.List;

import wordOfTheDay.client.Note;

public class LabelBeginFilter implements DataFilter {

	private String[] search;

	public LabelBeginFilter(String search) {
		this.search = search.split(":");
	}

	public boolean accept(Note note) {
		return BeginSearcher.lookFor(this.search, note.getLabels());
	}
}