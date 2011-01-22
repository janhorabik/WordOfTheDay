package wordOfTheDay.client.listWords;

import wordOfTheDay.client.addWord.EditWord;
import wordOfTheDay.client.dbOnClient.DatabaseOnClient;

import com.google.gwt.user.client.ui.Label;
import com.google.gwt.user.client.ui.VerticalPanel;

public class ListWords {

	private DatabaseOnClient database;
	private EditWord editWord;
	private ListWordsWithAdvancedTable listWordsWithAdvancedTable;

	public ListWords(final VerticalPanel listWordsPanelArg,
			DatabaseOnClient database) {
		listWordsPanel = listWordsPanelArg;
		this.database = database;
		this.editWord = new EditWord(listWordsPanel, database, false);
	}

	public void initiate() {
		listWordsPanel.clear();
		this.listWordsWithAdvancedTable = new ListWordsWithAdvancedTable();
		listWordsWithAdvancedTable
				.initiate(listWordsPanel, this, this.database);
		this.editWord = new EditWord(listWordsPanel, database, false);
		this.editWord.setVisible(false);
	}

	public void showEdit(String name, String explanation, String example,
			String tag, int date) {
		this.editWord.setText(name, explanation, example, tag, date);
		this.listWordsWithAdvancedTable.setVisible(false);
		this.editWord.setVisible(true);
	}

	public void showList() {
		this.editWord.setVisible(false);
		this.listWordsWithAdvancedTable.setVisible(true);
	}

	private VerticalPanel listWordsPanel;

}
