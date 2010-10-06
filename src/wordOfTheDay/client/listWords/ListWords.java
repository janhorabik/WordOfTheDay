package wordOfTheDay.client.listWords;

import com.google.gwt.user.client.ui.VerticalPanel;

public class ListWords {

	public ListWords(final VerticalPanel listWordsPanelArg) {
		listWordsPanel = listWordsPanelArg;
	}

	public void initiate() {
		listWordsPanel.clear();
		ListWordsWithAdvancedTable example = new ListWordsWithAdvancedTable();
		example.onModuleLoad(listWordsPanel, this);

	}

	private VerticalPanel listWordsPanel;

}
