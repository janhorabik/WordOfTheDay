package wordOfTheDay.client.deleteWords;

import wordOfTheDay.client.MyPopup.AskServer;
import wordOfTheDay.client.MyPopup.ServerResponse;

import com.google.gwt.core.client.GWT;
import com.google.gwt.user.client.rpc.AsyncCallback;

class AskServerToDeleteWords implements AskServer {
	private final DeleteWordsServiceAsync deleteWordsService = GWT
			.create(DeleteWordsService.class);

	public void askServer(final ServerResponse serverResponse) {
		deleteWordsService.deleteWords(new AsyncCallback<String>() {
			public void onFailure(Throwable caught) {
				serverResponse.error(caught.toString());
			}

			public void onSuccess(String result) {
				serverResponse.serverReplied(result);
			}
		});
	}
}