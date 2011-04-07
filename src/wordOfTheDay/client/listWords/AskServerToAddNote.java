package wordOfTheDay.client.listWords;

import java.util.Collection;
import java.util.LinkedList;
import java.util.List;

import wordOfTheDay.client.DataModel;
import wordOfTheDay.client.Note;
import wordOfTheDay.client.Services;
import wordOfTheDay.client.MyPopup.AskServer;
import wordOfTheDay.client.MyPopup.ServerResponse;
import wordOfTheDay.client.dbOnClient.DatabaseOnClient;
import wordOfTheDay.client.multiFieldsPanels.MultiFieldsPanel;
import wordOfTheDay.client.multiFieldsPanels.MultiFieldsPanelWithSuggest;

import com.google.gwt.event.dom.client.ChangeEvent;
import com.google.gwt.event.dom.client.ChangeHandler;
import com.google.gwt.event.dom.client.ClickEvent;
import com.google.gwt.event.dom.client.ClickHandler;
import com.google.gwt.user.client.rpc.AsyncCallback;
import com.google.gwt.user.client.ui.Button;
import com.google.gwt.user.client.ui.DialogBox;
import com.google.gwt.user.client.ui.FlexTable;
import com.google.gwt.user.client.ui.HorizontalPanel;
import com.google.gwt.user.client.ui.Label;
import com.google.gwt.user.client.ui.ListBox;
import com.google.gwt.user.client.ui.ScrollPanel;
import com.google.gwt.user.client.ui.TextArea;
import com.google.gwt.user.client.ui.VerticalPanel;

public class AskServerToAddNote implements AskServer {
	private DatabaseOnClient database;
	private DialogBox dialogBox;
	private ListBox dropBox;
	private ServerResponse serverResponse;
	protected List<DataModel> models;
	protected DataModel model;
	private FlexTable table;
	private List<TextArea> fieldsOfNote;
	private MultiFieldsPanel multiFieldsPanel;
	private VerticalPanel labelsPanel;
	private ScrollPanel scrollPanel;

	public AskServerToAddNote(final DatabaseOnClient databaseArg) {
		this.database = databaseArg;
	}

	private void createPanel() {
		dialogBox = new DialogBox(true);
		dropBox = new ListBox();
		models = new LinkedList<DataModel>();
		table = new FlexTable();
		fieldsOfNote = new LinkedList<TextArea>();
		multiFieldsPanel = MultiFieldsPanelWithSuggest.create("Labels:",
				"Add Label", database);
		labelsPanel = new VerticalPanel();
		dialogBox.setText("Add new note");
		dialogBox.setAnimationEnabled(true);
		final Button closeButton = new Button("Add note");
		final Button cancelButton = new Button("Cancel");
		// closeButton.getElement().setId("closeButton");
		scrollPanel = new ScrollPanel();
		VerticalPanel dialogVPanel = new VerticalPanel();
		scrollPanel.add(dialogVPanel);
		scrollPanel.setHeight("500px");
		scrollPanel.setWidth("380px");
		// dialogVPanel.setHorizontalAlignment(VerticalPanel.ALIGN_RIGHT);
		Label name = new Label("Model:");
		dialogVPanel.add(name);
		table = new FlexTable();
		dialogVPanel.add(dropBox);
		dialogVPanel.add(table);
		dialogVPanel.add(labelsPanel);
		updateDropBox();
		HorizontalPanel closePanel = new HorizontalPanel();
		closePanel.setSpacing(5);
		closePanel.add(closeButton);
		closePanel.add(cancelButton);
		closePanel.setHorizontalAlignment(VerticalPanel.ALIGN_RIGHT);
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
				final Note note = new Note("", 0, getValues(), model.getSeqNum(),
						getLabels());
				serverResponse.askedServer("Adding new note.");
				Services.noteService.addNote(note, new AsyncCallback<Integer>() {
					public void onSuccess(Integer result) {
						note.setSeqNum(result);
						database.newNoteWasAdded(note);
						serverResponse.serverReplied("Note was added.");
					}

					public void onFailure(Throwable caught) {
						serverResponse.serverReplied(caught.toString());
					}
				});
			}
		});
	}

	private List<String> getValues() {
		List<String> ret = new LinkedList<String>();
		for (TextArea textArea : fieldsOfNote) {
			ret.add(textArea.getText());
		}
		return ret;
	}

	private List<String> getLabels() {
		return multiFieldsPanel.getFields();
	}

	private void dropBoxSelected() {
		model = models.get(dropBox.getSelectedIndex());
		fieldsOfNote.clear();
		table.clear();
		List<String> fields = model.getFields();
		int maxLen = 0;
		for (int i = 0; i < fields.size(); ++i) {
			table.setWidget(i, 0, new Label(fields.get(i) + ":"));
			if (maxLen < fields.get(i).length())
			{
				maxLen = fields.get(i).length();
			}
			TextArea textField = new TextArea();
			textField.addStyleName("textArea");
			fieldsOfNote.add(textField);
			table.setWidget(i, 1, textField);
		}
		scrollPanel.setWidth((320 + maxLen*10) + "px");
		multiFieldsPanel = MultiFieldsPanelWithSuggest.create("Labels:",
				"Add Label", database);
		labelsPanel.clear();
		labelsPanel.add(multiFieldsPanel.getPanel());
	}

	private void updateDropBox() {
		models = database.getModels();
		int indexOfCurrentModel = 0;
		int i = -1;
		for (DataModel dataModel : models) {
			i++;
			dropBox.addItem(dataModel.getName());
			if (dataModel.getSeqNum() == database.getCurrentDataModelSeqNum())
				indexOfCurrentModel = i;
		}
		dropBox.addChangeHandler(new ChangeHandler() {
			public void onChange(ChangeEvent event) {
				dropBoxSelected();
			}
		});
		if (models.size() > 0) {
			dropBox.setItemSelected(indexOfCurrentModel, true);
			dropBoxSelected();
		}
	}

	public void askServer(final ServerResponse serverResponseArg) {
		this.serverResponse = serverResponseArg;
		createPanel();
		this.dialogBox.center();
	}
}