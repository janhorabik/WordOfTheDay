package wordOfTheDay.client.listWords;

import wordOfTheDay.client.advancedTable.GWTAdvancedTableExample;

import com.google.gwt.core.client.GWT;
import com.google.gwt.user.client.ui.VerticalPanel;

public class ListWords {

	public ListWords(final VerticalPanel listWordsPanelArg) {
		listWordsPanel = listWordsPanelArg;
	}

	public void initiate() {
		listWordsPanel.clear();
		GWTAdvancedTableExample example = new GWTAdvancedTableExample();
		example.onModuleLoad(listWordsPanel, this);

	}

	private VerticalPanel listWordsPanel;

}
