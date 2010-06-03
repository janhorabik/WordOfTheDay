package wordOfTheDay.client.addWord;

import wordOfTheDay.client.MyPopup.AskServer;
import wordOfTheDay.client.MyPopup.MyPopup;

import com.google.gwt.user.client.ui.Button;
import com.google.gwt.user.client.ui.FlexTable;
import com.google.gwt.user.client.ui.HTML;
import com.google.gwt.user.client.ui.Label;
import com.google.gwt.user.client.ui.RootPanel;
import com.google.gwt.user.client.ui.TextArea;
import com.google.gwt.user.client.ui.TextBox;
import com.google.gwt.user.client.ui.VerticalPanel;

/**
 * Entry point classes define <code>onModuleLoad()</code>.
 */
public class AddWord {

	public void initiateAddWord(final VerticalPanel addWordPanel) {
		// name
		final Label nameLabel = new Label();
		nameLabel.setText("Word:");
		nameLabel.addStyleName("wordOfTheDay");
		final TextBox nameField = new TextBox();
		nameField.addStyleName("textBox");

		// explanation
		final Label explanationLabel = new Label();
		explanationLabel.setText("Meaning:");
		explanationLabel.addStyleName("wordOfTheDay");
		final TextArea explanationField = new TextArea();
		explanationField.addStyleName("textArea");

		// example
		final Label exampleLabel = new Label();
		exampleLabel.setText("Example:");
		exampleLabel.addStyleName("wordOfTheDay");
		final TextArea exampleField = new TextArea();
		exampleField.addStyleName("textArea");

		// submit
		final Button sendButton = new Button("send");
		sendButton.addStyleName("submit");

		FlexTable table = new FlexTable();

		table.setWidget(0, 0, nameLabel);
		table.setWidget(0, 1, nameField);
		table.setWidget(1, 0, explanationLabel);
		table.setWidget(1, 1, explanationField);
		table.setWidget(2, 0, exampleLabel);
		table.setWidget(2, 1, exampleField);
		table.setWidget(3, 0, sendButton);
		addWordPanel.add(table);
		// Create the popup dialog box
		AskServer askServer = new AskServerToAddWord(nameField,
				explanationField, exampleField);
		MyPopup myPopup = new MyPopup("Add Word", askServer, sendButton);
	}
}
