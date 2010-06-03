package wordOfTheDay.client.downloadFile;

import wordOfTheDay.client.MyPopup.AskServer;
import wordOfTheDay.client.MyPopup.ServerResponse;

import com.google.gwt.user.client.ui.FormHandler;
import com.google.gwt.user.client.ui.FormPanel;
import com.google.gwt.user.client.ui.FormSubmitCompleteEvent;
import com.google.gwt.user.client.ui.FormSubmitEvent;

class AskServerToDownloadFile implements AskServer {
	private final FormPanel downloadForm;

	public AskServerToDownloadFile(final FormPanel downloadForm) {
		this.downloadForm = downloadForm;
	}

	public void askServer(final ServerResponse serverResponse) {
		downloadForm.submit();
		downloadForm.addFormHandler(new FormHandler() {
			public void onSubmit(FormSubmitEvent event) {
			}

			public void onSubmitComplete(FormSubmitCompleteEvent event) {
				System.out.println("results: " + event.getResults());
				serverResponse.serverReplied(event.getResults());
			}
		});
	}
}