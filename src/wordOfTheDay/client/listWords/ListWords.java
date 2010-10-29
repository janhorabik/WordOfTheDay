package wordOfTheDay.client.listWords;

import wordOfTheDay.client.home.Home;

import com.google.gwt.user.client.ui.VerticalPanel;

public class ListWords {

	public ListWords(final VerticalPanel listWordsPanelArg) {
		listWordsPanel = listWordsPanelArg;
	}

	public void initiate(Home home) {
		listWordsPanel.clear();
		ListWordsWithAdvancedTable example = new ListWordsWithAdvancedTable();
		example.onModuleLoad(listWordsPanel, this, home);

	}

	private VerticalPanel listWordsPanel;

}
