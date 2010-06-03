package wordOfTheDay.client.login;

import wordOfTheDay.client.Dashboard;
import wordOfTheDay.client.MyPopup.AskServer;
import wordOfTheDay.client.MyPopup.MessageShower;
import wordOfTheDay.client.MyPopup.ServerResponse;
import wordOfTheDay.client.home.Home;

import com.google.gwt.event.dom.client.ClickEvent;
import com.google.gwt.event.dom.client.ClickHandler;
import com.google.gwt.user.client.ui.Button;
import com.google.gwt.user.client.ui.HTML;
import com.google.gwt.user.client.ui.RootPanel;

public class LoginUpdater implements ServerResponse, ClickHandler {
	public LoginUpdater(RootPanel loginNamePanelArg, AskServer askServerArg,
			Button loginButton, Home homeArg, Dashboard dashboardArg) {
		loginNamePanel = loginNamePanelArg;
		askServer = askServerArg;
		if (loginButton != null) {
			loginButton.addClickHandler(this);
		} else {
			askServer.askServer(this);
		}
		home = homeArg;
		dashboard = dashboardArg;
	}

	public void error(String error) {
		MessageShower.showMessage("Error", error);
	}

	public void serverReplied(String reply) {
		loginNamePanel.clear();
		loginNamePanel
				.add(new HTML("<div id='date'>Hello " + reply + "</div>"));
		if (home != null) {
			home.initiate();
		}
		if (dashboard != null) {
			dashboard.initiate(reply);
		}
	}

	public void onClick(ClickEvent event) {
		askServer.askServer(this);

	}

	private AskServer askServer;
	private RootPanel loginNamePanel;
	private Home home;
	private Dashboard dashboard;
}
