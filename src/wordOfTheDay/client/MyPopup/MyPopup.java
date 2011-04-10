package wordOfTheDay.client.MyPopup;

import wordOfTheDay.client.dashboard.notesTable.MessagesPanel;

import com.google.gwt.event.dom.client.ClickEvent;
import com.google.gwt.event.dom.client.ClickHandler;
import com.google.gwt.user.client.ui.Button;
import com.google.gwt.user.client.ui.DialogBox;
import com.google.gwt.user.client.ui.HTML;
import com.google.gwt.user.client.ui.VerticalPanel;

public class MyPopup {
	private final DialogBox dialogBox = new DialogBox();
	private final HTML serverResponseLabel = new HTML();
	private final boolean showPopup;

	public MyPopup(final AskServer askServer, final Button sendButton, final MessagesPanel messagePanel)
	{
		showPopup = true;
		class MyHandler implements ClickHandler, ServerResponse {
			public void onClick(ClickEvent event) {
				System.out.println("handler called");
				askServer.askServer(this);
			}

			public void error(String error) {
				messagePanel.showMessage(error);
			}

			public void serverReplied(String reply) {
				messagePanel.showMessage(reply);
			}

			@Override
			public void askedServer(String messageAtTheBeginning) {
				messagePanel.showMessage(messageAtTheBeginning);
			}
		}
		MyHandler handler = new MyHandler();
		sendButton.addClickHandler(handler);
	}
	
	public MyPopup(String title, final AskServer askServer,
			final Button sendButton, boolean showPopupArg) {
		System.out.println("popup");
		showPopup = showPopupArg;
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
				System.out.println("handler called");
				askServer.askServer(this);
			}

			public void error(String error) {
				serverResponseLabel
						.setHTML("An error occured while connecting the service: "
								+ error);
				serverResponseLabel.addStyleName("serverResponseLabelError");
				if (showPopup) {
					dialogBox.center();
				}
			}

			public void serverReplied(String reply) {
				serverResponseLabel.setHTML(reply);
				// ListWordsWithAdvancedTable.messagesLabel.setText(reply);
				if (showPopup) {
					dialogBox.center();
				}
			}

			@Override
			public void askedServer(String messageAtTheBeginning) {
				// TODO Auto-generated method stub
				
			}
		}
		MyHandler handler = new MyHandler();
		sendButton.addClickHandler(handler);
	}
}
