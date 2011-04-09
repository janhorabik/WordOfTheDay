package wordOfTheDay.client.listWords.notesTable;

import java.util.LinkedList;
import java.util.List;

import wordOfTheDay.client.Note;

class BeginSearcher {
	public static boolean lookFor(String[] search, List<String> labels) {
		if (labels == null) {
			labels = new LinkedList<String>();
		}
		for (String label : labels) {
			String[] labelCut = label.split(":");
			if (search.length > labelCut.length)
				continue;
			if (checkIn(search, labelCut))
				return true;
		}
		return false;

	}

	private static boolean checkIn(String[] search, String[] labelCut) {
		for (int i = 0; i < search.length; ++i) {
			if (!labelCut[i].equals(search[i]))
				return false;
		}
		return true;
	}
}

public class LabelBeginFilter implements DataFilter {

	private String[] search;

	public LabelBeginFilter(String search) {
		this.search = search.split(":");
	}

	public boolean accept(Note note) {
		return BeginSearcher.lookFor(this.search, note.getLabels());
	}
}