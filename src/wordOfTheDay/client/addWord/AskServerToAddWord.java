package wordOfTheDay.client.addWord;

import java.util.LinkedList;
import java.util.List;

import wordOfTheDay.client.Word9;
import wordOfTheDay.client.MyPopup.AskServer;
import wordOfTheDay.client.MyPopup.ServerResponse;
import wordOfTheDay.client.dbOnClient.DatabaseOnClient;
import wordOfTheDay.client.home.Home;

import com.google.gwt.core.client.GWT;
import com.google.gwt.user.client.rpc.AsyncCallback;
import com.google.gwt.user.client.ui.SuggestBox;
import com.google.gwt.user.client.ui.TextArea;
import com.google.gwt.user.client.ui.TextBox;

class AskServerToAddWord implements AskServer {
	private TextBox nameField;
	private TextArea explanationField;
	private TextArea exampleField;
	private SuggestBox tagField;
	private final AddWordServiceAsync addWordService = GWT
			.create(AddWordService.class);
	private DatabaseOnClient database;
	private boolean add;
	private int date;

	void setDate(int date) {
		this.date = date;
	}

	public AskServerToAddWord(final TextBox nameField,
			final TextArea explanationField, final TextArea exampleField,
			SuggestBox tagField2, int date, final DatabaseOnClient database,
			boolean add) {
		this.nameField = nameField;
		this.explanationField = explanationField;
		this.exampleField = exampleField;
		this.tagField = tagField2;
		this.database = database;
		this.add = add;
		this.date = date;
	}

	private void clearFields() {
		System.out.println("clear fields");
		this.nameField.setText("");
		this.explanationField.setText("");
		this.exampleField.setText("");
		this.tagField.setText("");
//		this.database.update();
	}

	public void askServer(final ServerResponse serverResponse) {
		System.out.println("askServer");
		String name = nameField.getText();
		String explanation = explanationField.getText();
		String usage = exampleField.getText();
		List<String> example = new LinkedList<String>();
		example.add(usage);
		String tag = tagField.getText();
		List<String> tags = new LinkedList<String>();
		if (!tag.equals(""))
			tags.add(tag);
		if (name.equals("")) {
			serverResponse.serverReplied("Name of the word cannot be empty");
			return;
		}
		addWordService.addWord(new Word9(name, explanation, example, date,
				false, false, false, null, tags), add,
				new AsyncCallback<String>() {
					public void onFailure(Throwable caught) {
						serverResponse.error(caught.toString());
						clearFields();
					}

					public void onSuccess(String result) {
						System.out.println("success");
						serverResponse.serverReplied(result);
						clearFields();
					}
				});
	}
}