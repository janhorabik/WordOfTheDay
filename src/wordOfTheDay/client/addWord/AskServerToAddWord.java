package wordOfTheDay.client.addWord;

import java.util.LinkedList;
import java.util.List;

import wordOfTheDay.client.Word4;
import wordOfTheDay.client.MyPopup.AskServer;
import wordOfTheDay.client.MyPopup.ServerResponse;

import com.google.gwt.core.client.GWT;
import com.google.gwt.user.client.rpc.AsyncCallback;
import com.google.gwt.user.client.ui.TextArea;
import com.google.gwt.user.client.ui.TextBox;

class AskServerToAddWord implements AskServer {
	private TextBox nameField;
	private TextArea explanationField;
	private TextArea exampleField;
	private final AddWordServiceAsync addWordService = GWT
			.create(AddWordService.class);

	public AskServerToAddWord(final TextBox nameField,
			final TextArea explanationField, final TextArea exampleField) {
		this.nameField = nameField;
		this.explanationField = explanationField;
		this.exampleField = exampleField;
	}

	public void askServer(final ServerResponse serverResponse) {
		String name = nameField.getText();
		String explanation = explanationField.getText();
		String usage = exampleField.getText();
		List<String> example = new LinkedList<String>();
		example.add(usage);
		addWordService.addWord(new Word4(name, explanation, example, 0, false,
				false, null), new AsyncCallback<String>() {
			public void onFailure(Throwable caught) {
				serverResponse.error(caught.toString());
			}

			public void onSuccess(String result) {
				serverResponse.serverReplied(result);
			}
		});
	}
}