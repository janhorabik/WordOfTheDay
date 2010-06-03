package wordOfTheDay.client;

import wordOfTheDay.client.home.Home;
import wordOfTheDay.client.login.Login;

import com.google.gwt.core.client.EntryPoint;
import com.google.gwt.user.client.ui.HTML;
import com.google.gwt.user.client.ui.RootPanel;

/**
 * Entry point classes define <code>onModuleLoad()</code>.
 */
public class WordOfTheDay implements EntryPoint {

	public void onModuleLoad() {
		final RootPanel wordPanel = RootPanel.get("word");
		Home home = null;
		if (wordPanel != null) {
			home = new Home(wordPanel);
			home.initiate();
		}

		//second change on git
		final RootPanel dashboardPanel = RootPanel.get("dashboard");
		Dashboard dashboard = null;
		if (dashboardPanel != null) {
			dashboard = new Dashboard(dashboardPanel);
			dashboard.initiate("Anonymous");
		}

		final RootPanel loginPanel = RootPanel.get("login");
		final RootPanel loginNamePanel = RootPanel.get("loginName");
		if (loginPanel != null) {
			Login login = new Login();
			login.initiate(loginPanel, loginNamePanel, home, dashboard);
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
