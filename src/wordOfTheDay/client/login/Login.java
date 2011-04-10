package wordOfTheDay.client.login;

import wordOfTheDay.client.MyPopup.AskServer;
import wordOfTheDay.client.MyPopup.MyPopup;
import wordOfTheDay.client.dashboard.Dashboard;
import wordOfTheDay.client.dbOnClient.DatabaseOnClient;
import wordOfTheDay.client.dbOnClient.DatabaseUpdatedNotifier;

import com.google.gwt.event.dom.client.ClickEvent;
import com.google.gwt.event.dom.client.ClickHandler;
import com.google.gwt.user.client.ui.Button;
import com.google.gwt.user.client.ui.HorizontalPanel;
import com.google.gwt.user.client.ui.PasswordTextBox;
import com.google.gwt.user.client.ui.RootPanel;
import com.google.gwt.user.client.ui.TextBox;

public class Login implements DatabaseUpdatedNotifier {

	private RootPanel rootPanel;
	private Dashboard dashboard;
	private HorizontalPanel loginPanel;

	public Login(RootPanel rootPanelArg, Dashboard dashboardArg,
			DatabaseOnClient database) {
		rootPanel = rootPanelArg;
		dashboard = dashboardArg;
		final TextBox login = new TextBox();
		login.setText("youremail@email.com");
		final PasswordTextBox password = new PasswordTextBox();
		password.setText("password");

		Button loginButton = new Button("login");
		Button createAccountButton = new Button("Create an account");
		Button logoutButton = new Button("logout");

		loginPanel = new HorizontalPanel();
		// loginPanel.setWidth("100%");
		loginPanel.setSpacing(5);
		rootPanel.add(loginPanel);
		loginPanel.add(login);
		loginPanel.add(password);
		loginPanel.add(loginButton);
		loginPanel.add(createAccountButton);
		loginPanel.add(logoutButton);
		HorizontalPanel loginNamePanel = new HorizontalPanel();
		loginPanel.add(loginNamePanel);
		login.addClickHandler(new ClickHandler() {
			public void onClick(ClickEvent event) {
				login.setText("");
			}
		});

		password.addClickHandler(new ClickHandler() {
			public void onClick(ClickEvent event) {
				password.setText("");
			}
		});

		AskServer askServerIfLogedIn = new AskServerIfLogedIn();
		LoginUpdater loginCheckUpdater = new LoginUpdater(loginNamePanel,
				askServerIfLogedIn, null, database);

		AskServer askServerToLogin = new AskServerToLogin(login, password);
		LoginUpdater loginUpdater = new LoginUpdater(loginNamePanel,
				askServerToLogin, loginButton, database);

		AskServer askServerToCreateAccount = new AskServerToCreateAccount(login);
		MyPopup myPopup2 = new MyPopup("Create an account",
				askServerToCreateAccount, createAccountButton, true);

		AskServer askServerToLogout = new AskServerToLogout();
		LoginUpdater logoutUpdater = new LoginUpdater(loginNamePanel,
				askServerToLogout, logoutButton, database);

//		setVisible(false);
	}

	private void update() {
	}

	public void databaseUpdated() {
		update();
		setVisible(true);
	}

	public void setVisible(boolean visible) {
		loginPanel.setVisible(visible);
	}

}