package wordOfTheDay.client;

import wordOfTheDay.client.advancedTable.AdvancedTable;
import wordOfTheDay.client.advancedTable.TableModelServiceAsync;
import wordOfTheDay.client.dbOnClient.DatabaseOnClient;
import wordOfTheDay.client.home.GetTodaysWordService;
import wordOfTheDay.client.home.GetTodaysWordServiceAsync;
import wordOfTheDay.client.home.Home;
import wordOfTheDay.client.listWords.ListWordsWithAdvancedTable;
import wordOfTheDay.client.login.Login;

import com.google.gwt.core.client.EntryPoint;
import com.google.gwt.core.client.GWT;
import com.google.gwt.user.client.Window;
import com.google.gwt.user.client.ui.HTML;
import com.google.gwt.user.client.ui.RootPanel;

/**
 * Entry point classes define <code>onModuleLoad()</code>.
 */
public class WordOfTheDay implements EntryPoint {

	private DatabaseOnClient database = new DatabaseOnClient();

	public void onModuleLoad() {
		final RootPanel homePanel = RootPanel.get("home");
		Home home = new Home(homePanel, database);
		database.addNotifier(home);
		// home.initiate();

		final RootPanel dashboardPanel = RootPanel.get("dashboard");
		Dashboard dashboard = new Dashboard(dashboardPanel, database);
		database.addNotifier(dashboard);
		// dashboard.initiate("Anonymous", home);

		final RootPanel loginPanel = RootPanel.get("login");
		Login login = new Login(loginPanel, home, dashboard, database);
		database.addNotifier(login);

		initiateFooter();
//		database.update();
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

}
