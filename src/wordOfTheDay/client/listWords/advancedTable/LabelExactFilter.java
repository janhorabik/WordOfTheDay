package wordOfTheDay.client.listWords.advancedTable;

import java.util.LinkedList;
import java.util.List;

import wordOfTheDay.client.Word9;

public class LabelExactFilter implements DataFilter {

	private String search;

	public LabelExactFilter(String search) {
		this.search = search.toUpperCase();
	}

	public boolean accept(Word9 word) {
		List<String> labels = word.getLabels();
		if (labels == null) {
			labels = new LinkedList<String>();
		}
		for (String label : labels) {
			if (label.toUpperCase().equals(this.search))
				return true;
		}
		return false;
	}
}