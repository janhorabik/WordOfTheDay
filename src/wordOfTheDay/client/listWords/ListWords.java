package wordOfTheDay.client.listWords;

import wordOfTheDay.client.dbOnClient.DatabaseOnClient;

import com.google.gwt.user.client.ui.VerticalPanel;

public class ListWords {

	private DatabaseOnClient database;
	private ListWordsWithAdvancedTable listWordsWithAdvancedTable;
	private VerticalPanel listWordsPanel;

	public ListWords(final VerticalPanel listWordsPanelArg,
			DatabaseOnClient database) {
		listWordsPanel = listWordsPanelArg;
		this.database = database;
	}

	public void initiate() {
		listWordsPanel.clear();
		this.listWordsWithAdvancedTable = new ListWordsWithAdvancedTable();
		listWordsWithAdvancedTable
				.initiate(listWordsPanel, this, this.database);
	}

	public void showEdit(String name, String explanation, String example,
			String tag, int date) {
		this.listWordsWithAdvancedTable.setVisible(false);
	}


	public void update() {
		this.listWordsWithAdvancedTable.update();
	}

	public void hideRemoveButton() {
		this.listWordsWithAdvancedTable.hideRemoveButton();
	}

}
