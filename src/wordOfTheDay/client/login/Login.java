package wordOfTheDay.client.login;

import wordOfTheDay.client.Dashboard;
import wordOfTheDay.client.MyPopup.AskServer;
import wordOfTheDay.client.MyPopup.MyPopup;
import wordOfTheDay.client.home.Home;

import com.google.gwt.event.dom.client.ClickEvent;
import com.google.gwt.event.dom.client.ClickHandler;
import com.google.gwt.user.client.ui.Button;
import com.google.gwt.user.client.ui.HTML;
import com.google.gwt.user.client.ui.HorizontalPanel;
import com.google.gwt.user.client.ui.PasswordTextBox;
import com.google.gwt.user.client.ui.RootPanel;
import com.google.gwt.user.client.ui.TextBox;

public class Login {

	public void initiate(RootPanel rootPanel, Home home, Dashboard dashboard,
			boolean loginUpdateNeeded) {
		boolean isHome = (home != null);
		final TextBox login = new TextBox();
		login.setText("youremail@email.com");
		final PasswordTextBox password = new PasswordTextBox();
		password.setText("password");

		Button loginButton = new Button("login");
		Button createAccountButton = new Button("Create an account");
		Button logoutButton = new Button("logout");
		HorizontalPanel loginPanel = new HorizontalPanel();
		loginPanel.setSpacing(5);
		rootPanel.add(loginPanel);
		loginPanel.add(login);
		loginPanel.add(password);
		loginPanel.add(loginButton);
		loginPanel.add(createAccountButton);
		loginPanel.add(logoutButton);
		loginPanel.add(new HTML("<div id='date'>"
				+ (isHome ? "<a href='Dashboard.html'>Dashboard</a></div>"
						: "<a href='WordOfTheDay.html'>Home</a></div>")));
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
				askServerIfLogedIn, null, home, dashboard, loginUpdateNeeded);

		AskServer askServerToLogin = new AskServerToLogin(login, password);
		LoginUpdater loginUpdater = new LoginUpdater(loginNamePanel,
				askServerToLogin, loginButton, home, dashboard, true);

		AskServer askServerToCreateAccount = new AskServerToCreateAccount(login);
		MyPopup myPopup2 = new MyPopup("Create an account",
				askServerToCreateAccount, createAccountButton, true);

		AskServer askServerToLogout = new AskServerToLogout();
		LoginUpdater logoutUpdater = new LoginUpdater(loginNamePanel,
				askServerToLogout, logoutButton, home, dashboard, true);
	}
}