package wordOfTheDay.client.MyPopup;

import com.google.gwt.event.dom.client.ClickEvent;
import com.google.gwt.event.dom.client.ClickHandler;
import com.google.gwt.user.client.ui.Button;
import com.google.gwt.user.client.ui.DialogBox;
import com.google.gwt.user.client.ui.HTML;
import com.google.gwt.user.client.ui.VerticalPanel;

public class MyPopup {
	private final DialogBox dialogBox = new DialogBox();
	private final HTML serverResponseLabel = new HTML();

	public MyPopup(String title, final AskServer askServer,
			final Button sendButton) {
		dialogBox.setText(title);
		dialogBox.setAnimationEnabled(true);
		final Button closeButton = new Button("Close");
		// closeButton.getElement().setId("closeButton");
		VerticalPanel dialogVPanel = new VerticalPanel();
		dialogVPanel.add(serverResponseLabel);
		dialogVPanel.setHorizontalAlignment(VerticalPanel.ALIGN_RIGHT);
		dialogVPanel.add(closeButton);
		dialogBox.setWidget(dialogVPanel);

		closeButton.addClickHandler(new ClickHandler() {
			public void onClick(ClickEvent event) {
				dialogBox.hide();
			}
		});

		class MyHandler implements ClickHandler, ServerResponse {
			public void onClick(ClickEvent event) {
				askServer.askServer(this);
			}

			public void error(String error) {
				serverResponseLabel
						.setHTML("An error occured while connecting the service: " + error);
				serverResponseLabel.addStyleName("serverResponseLabelError");
				dialogBox.center();
			}

			public void serverReplied(String reply) {
				serverResponseLabel.setHTML(reply);
				dialogBox.center();
			}
		}
		MyHandler handler = new MyHandler();
		sendButton.addClickHandler(handler);
	}
}
