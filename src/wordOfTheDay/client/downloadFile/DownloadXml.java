package wordOfTheDay.client.downloadFile;

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
import com.google.gwt.user.client.ui.HorizontalPanel;
import com.google.gwt.user.client.ui.RootPanel;
import com.google.gwt.user.client.ui.VerticalPanel;
import com.google.gwt.user.client.ui.Widget;

/**
 * Entry point classes define <code>onModuleLoad()</code>.
 */
public class DownloadXml {

	public void initiateDownloadFile(final VerticalPanel rootPanel) {
		final FormPanel downloadForm = new FormPanel();
		downloadForm.setStyleName("wordofTheDay");
		downloadForm.setAction(GWT.getModuleBaseURL() + "fileDownloadServlet");
		downloadForm.setEncoding(FormPanel.ENCODING_MULTIPART);
		downloadForm.setMethod(FormPanel.METHOD_POST);

		Button downloadSubmitButton = new Button("Get XML File");
		downloadSubmitButton.setStyleName("simpleMargin");
		downloadForm.setWidget(downloadSubmitButton);

		rootPanel.add(downloadForm);

		AskServer askServer = new AskServerToDownloadFile(downloadForm);
		MyPopup mypopup = new MyPopup("Send file", askServer,
				downloadSubmitButton);

	}
}
