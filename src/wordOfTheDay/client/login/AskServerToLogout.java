package wordOfTheDay.client.login;

import wordOfTheDay.client.MyPopup.AskServer;
import wordOfTheDay.client.MyPopup.ServerResponse;

import com.google.gwt.core.client.GWT;
import com.google.gwt.user.client.rpc.AsyncCallback;

class AskServerToLogout implements AskServer {

	private final LoginServiceAsync loginService = GWT
			.create(LoginService.class);

	public AskServerToLogout() {
	}

	public void askServer(final ServerResponse serverResponse) {
		loginService.logout(new AsyncCallback<Boolean>() {
			public void onFailure(Throwable caught) {
				serverResponse.error(caught.toString());
			}

			public void onSuccess(Boolean result) {
				if (result) {
					serverResponse.serverReplied("Anonymous");
				} else {
					serverResponse.error("Logout failed");
				}
			}
		});
	}
}