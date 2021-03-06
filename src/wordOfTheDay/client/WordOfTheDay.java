package wordOfTheDay.client;

import wordOfTheDay.client.dashboard.Dashboard;
import wordOfTheDay.client.dbOnClient.DatabaseOnClient;
import wordOfTheDay.client.dbOnClient.DatabaseUpdatedNotifier;
import wordOfTheDay.client.login.Login;

import com.google.gwt.core.client.EntryPoint;
import com.google.gwt.user.client.ui.HTML;
import com.google.gwt.user.client.ui.RootPanel;

/**
 * Entry point classes define <code>onModuleLoad()</code>.
 */
public class WordOfTheDay implements EntryPoint, DatabaseUpdatedNotifier {

	private DatabaseOnClient database = new DatabaseOnClient();

	public void onModuleLoad() {
		final RootPanel dashboardPanel = RootPanel.get("dashboard");
		Dashboard dashboard = new Dashboard(dashboardPanel, database);
		database.addNotifier(dashboard);

		final RootPanel loginPanel = RootPanel.get("login");
		Login login = new Login(loginPanel, dashboard, database);
		database.addNotifier(login);
		database.addNotifier(this);

	}

	private void initiate(String id, String content) {
		HTML html = new HTML(content);
		RootPanel panel = RootPanel.get(id);
		if (panel != null) {
			panel.clear();
			panel.add(html);
		}
	}

	private void initiateFooter() {
		initiate(
				"footer",
				"Webmaster: <a href='mailto:janhorabik@gmail.com' title='e-mail me'>Jan Horabik</a> ");
		// &nbsp;&nbsp;&nbsp;&nbsp;&nbsp;Source: <a
		// href='http://dictionary.cambridge.org/'>Cambridge Dictionary</a>
	}

	public void databaseUpdated() {
		initiateFooter();
	}

}
