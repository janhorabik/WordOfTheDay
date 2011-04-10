package wordOfTheDay.client.login;

import wordOfTheDay.client.MyPopup.AskServer;
import wordOfTheDay.client.MyPopup.MessageShower;
import wordOfTheDay.client.MyPopup.ServerResponse;
import wordOfTheDay.client.dbOnClient.DatabaseOnClient;

import com.google.gwt.event.dom.client.ClickEvent;
import com.google.gwt.event.dom.client.ClickHandler;
import com.google.gwt.user.client.ui.Button;
import com.google.gwt.user.client.ui.HTML;
import com.google.gwt.user.client.ui.HorizontalPanel;

public class LoginUpdater implements ServerResponse, ClickHandler {

	private Button loginButton;

	public LoginUpdater(HorizontalPanel loginNamePanel2,
			AskServer askServerArg, Button loginButton,
			DatabaseOnClient database) {
		loginNamePanel = loginNamePanel2;
		askServer = askServerArg;
		this.loginButton = loginButton;
		if (loginButton != null) {
			loginButton.addClickHandler(this);
		} else {
			askServer.askServer(this);
		}
		this.database = database;
	}

	public void error(String error) {
		MessageShower.showMessage("Error", error);
	}

	public void serverReplied(String reply) {
		loginNamePanel.clear();
		loginNamePanel
				.add(new HTML("<div id='date'>Hello " + reply + "</div>"));
		database.setLogin(reply);
		database.initiate();
	}

	public void onClick(ClickEvent event) {
		askServer.askServer(this);
	}

	private AskServer askServer;
	private HorizontalPanel loginNamePanel;
	private DatabaseOnClient database;

	@Override
	public void askedServer(String messageAtTheBeginning) {

	}
}
