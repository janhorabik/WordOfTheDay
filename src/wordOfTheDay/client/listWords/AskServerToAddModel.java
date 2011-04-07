package wordOfTheDay.client.listWords;

import wordOfTheDay.client.DataModel;
import wordOfTheDay.client.Services;
import wordOfTheDay.client.MyPopup.AskServer;
import wordOfTheDay.client.MyPopup.ServerResponse;
import wordOfTheDay.client.dbOnClient.DatabaseOnClient;
import wordOfTheDay.client.multiFieldsPanels.MultiFieldsPanel;

import com.google.gwt.event.dom.client.ClickEvent;
import com.google.gwt.event.dom.client.ClickHandler;
import com.google.gwt.user.client.rpc.AsyncCallback;
import com.google.gwt.user.client.ui.Button;
import com.google.gwt.user.client.ui.DialogBox;
import com.google.gwt.user.client.ui.HorizontalPanel;
import com.google.gwt.user.client.ui.Label;
import com.google.gwt.user.client.ui.ScrollPanel;
import com.google.gwt.user.client.ui.TextArea;
import com.google.gwt.user.client.ui.TextBox;
import com.google.gwt.user.client.ui.VerticalPanel;

public class AskServerToAddModel implements AskServer {
	private DatabaseOnClient database;
	private DialogBox dialogBox = new DialogBox(true);
	private MultiFieldsPanel multiFieldsPanel;
	private VerticalPanel fieldsPanel;
	private ServerResponse serverResponse;
	private TextBox nameField;

	public AskServerToAddModel(final DatabaseOnClient database) {
		this.database = database;
	}

	private void createPanel() {
		multiFieldsPanel = MultiFieldsPanel.create("Fields:", "CreateField");
		this.fieldsPanel = multiFieldsPanel.getPanel();
		nameField = new TextBox();
		nameField.addStyleName("textBox");
		dialogBox.setText("Add new model");
		dialogBox.setAnimationEnabled(true);
		final Button closeButton = new Button("Add Model");
		// closeButton.getElement().setId("closeButton");
		ScrollPanel scrollPanel = new ScrollPanel();
		VerticalPanel dialogVPanel = new VerticalPanel();
		scrollPanel.add(dialogVPanel);
		scrollPanel.setHeight("250px");
		Label nameLabel = new Label("Model name:");
		dialogVPanel.add(nameLabel);
		dialogVPanel.add(this.nameField);
		dialogVPanel.add(fieldsPanel);
		dialogVPanel.add(closeButton);
		final Button cancelButton = new Button("Cancel");
		HorizontalPanel closePanel = new HorizontalPanel();
		closePanel.setHorizontalAlignment(VerticalPanel.ALIGN_RIGHT);
		closePanel.setSpacing(5);
		closePanel.add(closeButton);
		closePanel.add(cancelButton);
		dialogVPanel.add(closePanel);
		dialogBox.setWidget(scrollPanel);

		cancelButton.addClickHandler(new ClickHandler() {
			public void onClick(ClickEvent event) {
				dialogBox.hide();
			}
		});
		closeButton.addClickHandler(new ClickHandler() {
			public void onClick(ClickEvent event) {
				dialogBox.hide();
				final DataModel model = new DataModel("", 0, nameField.getText(),
						multiFieldsPanel.getFields());
				serverResponse.askedServer("Adding new model");
				Services.noteService.addModel(model, new AsyncCallback<Integer>() {

					@Override
					public void onSuccess(Integer result) {
						model.setSeqNum(result);
						database.newModelWasAdded(model);
						serverResponse.serverReplied("Model was added.");
					}

					@Override
					public void onFailure(Throwable caught) {
						serverResponse.serverReplied(caught.toString());
					}
				});
			}
		});
	}

	public void askServer(final ServerResponse serverResponseArg) {
		this.serverResponse = serverResponseArg;
		createPanel();
		this.dialogBox.center();
	}
}