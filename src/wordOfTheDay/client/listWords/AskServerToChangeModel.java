package wordOfTheDay.client.listWords;

import wordOfTheDay.client.DataModel;
import wordOfTheDay.client.Services;
import wordOfTheDay.client.MyPopup.AskServer;
import wordOfTheDay.client.MyPopup.ServerResponse;
import wordOfTheDay.client.dbOnClient.DatabaseOnClient;
import wordOfTheDay.client.deleteWords.DeleteWordsService;

import com.google.gwt.core.client.GWT;
import com.google.gwt.user.client.rpc.AsyncCallback;

class AskServerToChangeModel implements AskServer {

	private DatabaseOnClient database;

	private String newName;

	private boolean remove;

	private DataModel model;

	public AskServerToChangeModel(DatabaseOnClient database) {
		this.database = database;
	}

	public void setModel(DataModel modelArg, String newNameArg,
			boolean removeArg) {
		this.model = modelArg;
		this.newName = newNameArg;
		this.remove = removeArg;
	}

	public void askServer(final ServerResponse serverResponse) {
		System.out.println("askServer");
		serverResponse.askedServer((remove ? "Removing" : "Renaming")
				+ " the model");
		if (remove)
			Services.noteService.removeModel(model.getSeqNum(),
					new AsyncCallback<Void>() {
						public void onSuccess(Void result) {
							System.out.println("success");
							database.modelWasRemoved(model.getSeqNum());
							serverResponse.serverReplied("Model was removed.");
						}

						public void onFailure(Throwable caught) {
							serverResponse.error(caught.toString());
						}
					});
		else {
			model.setName(newName);
			Services.noteService.changeModel(model, new AsyncCallback<Void>() {
				public void onSuccess(Void result) {
					System.out.println("success");
					database.modelWasRenamed(model.getSeqNum(), newName);
					serverResponse.serverReplied("Model was renamed.");
				}

				public void onFailure(Throwable caught) {
					serverResponse.error(caught.toString());
				}
			});

		}
	}
}