package wordOfTheDay.client.addWord;

import java.util.LinkedList;
import java.util.List;

import wordOfTheDay.client.Word6;
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

	private void clearFields() {
		System.out.println("clear fields");
		this.nameField.setText("");
		this.explanationField.setText("");
		this.exampleField.setText("");
	}

	public void askServer(final ServerResponse serverResponse) {
		System.out.println("askServer");
		String name = nameField.getText();
		String explanation = explanationField.getText();
		String usage = exampleField.getText();
		List<String> example = new LinkedList<String>();
		example.add(usage);
		addWordService.addWord(new Word6(name, explanation, example, 0, false,
				false, false, null), new AsyncCallback<String>() {
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