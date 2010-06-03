package wordOfTheDay.client.uploadFile;

import wordOfTheDay.client.MyPopup.AskServer;
import wordOfTheDay.client.MyPopup.ServerResponse;

import com.google.gwt.user.client.ui.FormHandler;
import com.google.gwt.user.client.ui.FormPanel;
import com.google.gwt.user.client.ui.FormSubmitCompleteEvent;
import com.google.gwt.user.client.ui.FormSubmitEvent;

class AskServerToUploadFile implements AskServer {
	private final FormPanel uploadForm;

	public AskServerToUploadFile(final FormPanel uploadForm) {
		this.uploadForm = uploadForm;
	}

	public void askServer(final ServerResponse serverResponse) {
		uploadForm.submit();
		uploadForm.addFormHandler(new FormHandler() {
			public void onSubmit(FormSubmitEvent event) {
			}

			public void onSubmitComplete(FormSubmitCompleteEvent event) {
				System.out.println("results: " + event.getResults());
				serverResponse.serverReplied(event.getResults());
			}
		});
	}
}