package wordOfTheDay.client.login;

import wordOfTheDay.client.MyPopup.AskServer;
import wordOfTheDay.client.MyPopup.ServerResponse;

import com.google.gwt.core.client.GWT;
import com.google.gwt.user.client.rpc.AsyncCallback;

class AskServerIfLogedIn implements AskServer {

	private final LoginServiceAsync loginService = GWT
			.create(LoginService.class);

	public AskServerIfLogedIn() {
	}

	public void askServer(final ServerResponse serverResponse) {
		loginService.isUserLogedIn(new AsyncCallback<String>() {
			public void onFailure(Throwable caught) {
				serverResponse.error(caught.toString());
			}

			public void onSuccess(String result) {
				if (result == null) {
					serverResponse.serverReplied("Anonymous");
				} else {
					serverResponse.serverReplied(result);
				}
			}
		});
	}
}