package wordOfTheDay.client.deleteWords;

import wordOfTheDay.client.MyPopup.AskServer;
import wordOfTheDay.client.MyPopup.MyPopup;
import wordOfTheDay.client.dbOnClient.DatabaseOnClient;

import com.google.gwt.user.client.ui.Button;
import com.google.gwt.user.client.ui.RootPanel;
import com.google.gwt.user.client.ui.VerticalPanel;

/**
 * Entry point classes define <code>onModuleLoad()</code>.
 */
public class DeleteWords {

	public void initiateDeleteWords(VerticalPanel deleteWordsPanel,
			DatabaseOnClient database) {
		final Button deleteButton = new Button("Delete Words");
		deleteWordsPanel.add(deleteButton);

		AskServer askServer = new AskServerToDeleteWords(database);
		MyPopup myPopup = new MyPopup("Delete words", askServer, deleteButton,
				true);
	}
}
