package wordOfTheDay.client.login;

import wordOfTheDay.client.LoginResult;
import wordOfTheDay.client.WordOfTheDay;
import wordOfTheDay.client.MyPopup.AskServer;
import wordOfTheDay.client.MyPopup.ServerResponse;

import com.google.gwt.core.client.GWT;
import com.google.gwt.user.client.rpc.AsyncCallback;
import com.google.gwt.user.client.ui.PasswordTextBox;
import com.google.gwt.user.client.ui.TextBox;

class AskServerToLogin implements AskServer {
	private TextBox loginField;
	private PasswordTextBox passwordField;

	private final LoginServiceAsync loginService = GWT
			.create(LoginService.class);

	public AskServerToLogin(final TextBox loginField,
			final PasswordTextBox passwordField) {
		this.loginField = loginField;
		this.passwordField = passwordField;
	}

	public void askServer(final ServerResponse serverResponse) {
		String login = loginField.getText();
		String password = passwordField.getText();
		loginService.login(login, password, new AsyncCallback<LoginResult>() {
			public void onFailure(Throwable caught) {
				serverResponse.error(caught.toString());
			}

			public void onSuccess(LoginResult result) {
				if (result.isOk())
					serverResponse.serverReplied(result.getMessage());
				else
					serverResponse.error(result.getMessage());
			}
		});
	}
}