package wordOfTheDay.client.addWord;

import java.util.LinkedList;
import java.util.List;

import wordOfTheDay.client.Word8;
import wordOfTheDay.client.MyPopup.AskServer;
import wordOfTheDay.client.MyPopup.ServerResponse;

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

	public AskServerToAddWord(final TextBox nameField,
			final TextArea explanationField, final TextArea exampleField,
			SuggestBox tagField2) {
		this.nameField = nameField;
		this.explanationField = explanationField;
		this.exampleField = exampleField;
		this.tagField = tagField2;
	}

	private void clearFields() {
		System.out.println("clear fields");
		this.nameField.setText("");
		this.explanationField.setText("");
		this.exampleField.setText("");
		this.tagField.setText("");
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
		tags.add(tag);
		if (name.equals("")) {
			serverResponse.serverReplied("Name of the word cannot be empty");
			return;
		}
		addWordService.addWord(new Word8(name, explanation, example, 0, false,
				false, false, null, tags), new AsyncCallback<String>() {
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