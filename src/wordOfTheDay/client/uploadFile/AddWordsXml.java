package wordOfTheDay.client.uploadFile;

import wordOfTheDay.client.MyPopup.AskServer;
import wordOfTheDay.client.MyPopup.MyPopup;

import com.google.gwt.core.client.GWT;
import com.google.gwt.user.client.Window;
import com.google.gwt.user.client.ui.Button;
import com.google.gwt.user.client.ui.ClickListener;
import com.google.gwt.user.client.ui.FileUpload;
import com.google.gwt.user.client.ui.FormHandler;
import com.google.gwt.user.client.ui.FormPanel;
import com.google.gwt.user.client.ui.FormSubmitCompleteEvent;
import com.google.gwt.user.client.ui.FormSubmitEvent;
import com.google.gwt.user.client.ui.RootPanel;
import com.google.gwt.user.client.ui.VerticalPanel;
import com.google.gwt.user.client.ui.Widget;

/**
 * Entry point classes define <code>onModuleLoad()</code>.
 */
public class AddWordsXml {

	public void initiateUploadFile(final VerticalPanel rootPanel) {
		// Create a FormPanel and point it at a service.
		final FormPanel uploadForm = new FormPanel();
		uploadForm.setAction(GWT.getModuleBaseURL() + "fileUploadServlet");

		uploadForm.setEncoding(FormPanel.ENCODING_MULTIPART);
		uploadForm.setMethod(FormPanel.METHOD_POST);

		// Create a panel to hold all of the form widgets.
		VerticalPanel panel = new VerticalPanel();
		panel.addStyleName("wordOfTheDay");
		uploadForm.setWidget(panel);

		// Create a FileUpload widget.
		FileUpload upload = new FileUpload();
		upload.setName("uploadFormElement");
		upload.setStyleName("simpleMargin");
		panel.add(upload);

		// Add a 'submit' button.
		Button uploadSubmitButton = new Button("Submit");
		uploadSubmitButton.setStyleName("simpleMargin");

		panel.add(uploadSubmitButton);
		rootPanel.add(uploadForm);

		AskServer askServer = new AskServerToUploadFile(uploadForm);
		MyPopup mypopup = new MyPopup("Send file", askServer,
				uploadSubmitButton);

	}
}
