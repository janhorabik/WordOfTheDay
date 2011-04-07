package wordOfTheDay.client;

import wordOfTheDay.client.dbOnClient.DatabaseOnClient;
import wordOfTheDay.client.dbOnClient.DatabaseUpdatedNotifier;
import wordOfTheDay.client.listWords.ListWords;

import com.google.gwt.user.client.ui.DecoratorPanel;
import com.google.gwt.user.client.ui.HTML;
import com.google.gwt.user.client.ui.Label;
import com.google.gwt.user.client.ui.RootPanel;
import com.google.gwt.user.client.ui.VerticalPanel;

/**
 * Entry point classes define <code>onModuleLoad()</code>.
 */
public class Dashboard implements DatabaseUpdatedNotifier {

	private DatabaseOnClient database;

	DecoratorPanel tabPanel = new DecoratorPanel();

	Dashboard(final RootPanel rootPanelArg, DatabaseOnClient database) {
		rootPanel = rootPanelArg;
		this.database = database;
		rootPanel.clear();
		rootPanel.add(notLoggedIn);
		// Create a tab panel

		final VerticalPanel listWordsPanel = new VerticalPanel();
		listWords = new ListWords(listWordsPanel, database);
		listWords.initiate();
		tabPanel.add(listWordsPanel);

		Label l = new Label("Personal Knowledge Base");
		l.setStyleName("h1");
		rootPanel.add(l);
		rootPanel.add(tabPanel);
		setVisible(true);
	}

	public void setVisible(boolean isVisible) {
		rootPanel.setVisible(isVisible);
	}

	private RootPanel rootPanel;

	ListWords listWords = null;

	private HTML notLoggedIn = new HTML("<div id='date'>Please log in.</div>");

	public void databaseUpdated() {
		if (database.getLogin().equals("Anonymous")) {
			tabPanel.setVisible(false);
			notLoggedIn.setVisible(true);
		} else {
			tabPanel.setVisible(true);
			notLoggedIn.setVisible(false);

			if (listWords != null) {
				listWords.update();
			}
		}
	}
}
