package wordOfTheDay.client.listWords;

import wordOfTheDay.client.dbOnClient.DatabaseOnClient;
import wordOfTheDay.client.home.Home;

import com.google.gwt.user.client.ui.VerticalPanel;

public class ListWords {

	private DatabaseOnClient database;

	public ListWords(final VerticalPanel listWordsPanelArg,
			DatabaseOnClient database) {
		listWordsPanel = listWordsPanelArg;
		this.database = database;
	}

	public void initiate() {
		listWordsPanel.clear();
		ListWordsWithAdvancedTable example = new ListWordsWithAdvancedTable();
		example.initiate(listWordsPanel, this, this.database);
	}

	private VerticalPanel listWordsPanel;

}
