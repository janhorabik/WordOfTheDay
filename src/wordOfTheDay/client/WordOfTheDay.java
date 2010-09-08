package wordOfTheDay.client;

import wordOfTheDay.client.advancedTable.AdvancedTable;
import wordOfTheDay.client.advancedTable.GWTAdvancedTableExample;
import wordOfTheDay.client.advancedTable.TableModelServiceAsync;
import wordOfTheDay.client.home.GetTodaysWordService;
import wordOfTheDay.client.home.GetTodaysWordServiceAsync;
import wordOfTheDay.client.home.Home;
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

	private final GetTodaysWordServiceAsync getTodaysService = GWT
			.create(GetTodaysWordService.class);

	public void onModuleLoad() {

		final RootPanel wordPanel = RootPanel.get("word");

		Home home = null;
		boolean loginUpdateNeeded = (Home.dayChoiceFromParam(Window.Location
				.getParameter("dayChoice")) == DayChoice2.TODAY);
		if (wordPanel != null) {
			home = new Home(wordPanel);
			if (!loginUpdateNeeded)
				home.initiate();
		}

		final RootPanel dashboardPanel = RootPanel.get("dashboard");
		Dashboard dashboard = null;
		if (dashboardPanel != null) {
			dashboard = new Dashboard(dashboardPanel);
			dashboard.initiate("Anonymous");
		}

		final RootPanel loginPanel = RootPanel.get("login");
		if (loginPanel != null) {
			Login login = new Login();
			login.initiate(loginPanel, home, dashboard, loginUpdateNeeded);
		}
		initiateFooter();
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
