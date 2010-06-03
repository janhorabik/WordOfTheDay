package wordOfTheDay.client.login;

import wordOfTheDay.client.MyPopup.AskServer;
import wordOfTheDay.client.MyPopup.ServerResponse;

import com.google.gwt.core.client.GWT;
import com.google.gwt.user.client.rpc.AsyncCallback;
import com.google.gwt.user.client.ui.TextBox;

class AskServerToCreateAccount implements AskServer {
	private TextBox loginField;

	private final LoginServiceAsync loginService = GWT
			.create(LoginService.class);

	public AskServerToCreateAccount(final TextBox loginField) {
		this.loginField = loginField;
	}

	public void askServer(final ServerResponse serverResponse) {
		String login = loginField.getText();
		loginService.newUser(login, new AsyncCallback<String>() {
			public void onFailure(Throwable caught) {
				serverResponse.error(caught.toString());
			}

			public void onSuccess(String result) {
				serverResponse.serverReplied(result);
			}
		});
	}
}