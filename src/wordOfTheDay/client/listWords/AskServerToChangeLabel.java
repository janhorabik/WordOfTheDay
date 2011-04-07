package wordOfTheDay.client.listWords;

import wordOfTheDay.client.Services;
import wordOfTheDay.client.MyPopup.AskServer;
import wordOfTheDay.client.MyPopup.ServerResponse;
import wordOfTheDay.client.dbOnClient.DatabaseOnClient;

import com.google.gwt.user.client.rpc.AsyncCallback;

class AskServerToChangeLabel implements AskServer {

	private DatabaseOnClient database;

	private String label;

	private String newLabel;

	private boolean remove;

	public AskServerToChangeLabel(DatabaseOnClient database) {
		this.database = database;
	}

	public void setLabel(String labelToChange, String newLabelArg,
			boolean removeArg) {
		this.label = labelToChange;
		this.newLabel = newLabelArg;
		this.remove = removeArg;
	}

	public void askServer(final ServerResponse serverResponse) {
		System.out.println("askServer");
		serverResponse.askedServer((remove ? "Removing" : "Renaming")
				+ " the label");
		if (remove)
			Services.noteService.removeLabel(label,
					database.getCurrentDataModelSeqNum(),
					new AsyncCallback<Void>() {
						public void onSuccess(Void result) {
							System.out.println("success");
							database.labelWasRemoved(label);
							serverResponse.serverReplied("Label was removed.");
						}

						public void onFailure(Throwable caught) {
							serverResponse.error(caught.toString());
						}
					});
		else {
			Services.noteService.renameLabel(label, newLabel,
					database.getCurrentDataModelSeqNum(),
					new AsyncCallback<Void>() {
						public void onSuccess(Void result) {
							System.out.println("success");
							database.labelWasRenamed(label, newLabel);
							serverResponse.serverReplied("Label was renamed.");
						}

						public void onFailure(Throwable caught) {
							serverResponse.error(caught.toString());
						}
					});

		}
	}
}