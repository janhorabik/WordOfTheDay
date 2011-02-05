package wordOfTheDay.client.addWord;

import wordOfTheDay.client.MyPopup.AskServer;
import wordOfTheDay.client.MyPopup.MyPopup;
import wordOfTheDay.client.dbOnClient.DatabaseOnClient;

import com.google.gwt.user.client.ui.Button;
import com.google.gwt.user.client.ui.FlexTable;
import com.google.gwt.user.client.ui.Label;
import com.google.gwt.user.client.ui.MultiWordSuggestOracle;
import com.google.gwt.user.client.ui.SuggestBox;
import com.google.gwt.user.client.ui.TextArea;
import com.google.gwt.user.client.ui.TextBox;
import com.google.gwt.user.client.ui.VerticalPanel;

/**
 * Entry point classes define <code>onModuleLoad()</code>.
 */
public class EditWord {

	MultiWordSuggestOracle oracle = new MultiWordSuggestOracle(" :");

	DatabaseOnClient database;

	private VerticalPanel addWordPanel;

	private Label nameLabel;

	private TextBox nameField;

	private Label explanationLabel;

	private TextArea explanationField;

	private Label exampleLabel;

	private TextArea exampleField;

	private Label tagLabel;

	private Button sendButton;

	private SuggestBox tagField;

	private AskServerToAddWord askServer;

	private int date;

	public void init() {
		this.init("", "", "", "");
	}

	public void setText(String name, String explanation, String example,
			String tag, int date) {
		this.nameField.setText(name);
		this.explanationField.setText(explanation);
		this.exampleField.setText(example);
		this.tagField.setText(tag);
		this.date = date;
		this.askServer.setDate(date);
	}

	public void init(String name, String explanation, String example, String tag) {
		this.nameLabel = new Label();
		nameLabel.setText("Word:");
		nameLabel.addStyleName("wordOfTheDay");
		this.nameField = new TextBox();
		nameField.setText(name);
		nameField.addStyleName("textBox");

		// explanation
		this.explanationLabel = new Label();
		explanationLabel.setText("Meaning:");
		explanationLabel.addStyleName("wordOfTheDay");
		this.explanationField = new TextArea();
		explanationField.setText(explanation);
		explanationField.addStyleName("textArea");

		// example
		this.exampleLabel = new Label();
		exampleLabel.setText("Example:");
		exampleLabel.addStyleName("wordOfTheDay");
		this.exampleField = new TextArea();
		exampleField.addStyleName("textArea");
		exampleField.setText(explanation);

		// tag
		this.tagLabel = new Label();
		tagLabel.setText("Label:");
		tagLabel.addStyleName("wordOfTheDay");

		oracle.clear();
		oracle.addAll(database.getLabels());
		this.tagField = new SuggestBox(oracle);
		tagField.setText(tag);

		// submit
		this.sendButton = new Button("Save");
		sendButton.addStyleName("submit");

		FlexTable table = new FlexTable();

		table.setWidget(0, 0, nameLabel);
		table.setWidget(0, 1, nameField);
		table.setWidget(1, 0, explanationLabel);
		table.setWidget(1, 1, explanationField);
		table.setWidget(2, 0, exampleLabel);
		table.setWidget(2, 1, exampleField);
		table.setWidget(3, 0, tagLabel);
		table.setWidget(3, 1, tagField);
		table.setWidget(4, 0, sendButton);
		addWordPanel.add(table);

		// Create the popup dialog box
	}

	public EditWord(final VerticalPanel addWordPanelArg,
			final DatabaseOnClient database, boolean add) {
		this.database = database;
		this.addWordPanel = new VerticalPanel();
		addWordPanelArg.add(this.addWordPanel);
		this.init();
		this.askServer = new AskServerToAddWord(nameField, explanationField,
				exampleField, tagField, date, database, add);
		MyPopup myPopup = new MyPopup("Add Word", askServer, sendButton, true);
	}

	public void update() {
		oracle.clear();
		oracle.addAll(database.getLabels());
	}

	public void setVisible(boolean visible) {
		this.addWordPanel.setVisible(visible);
	}

	public void setEmptyFields() {
		this.nameField.setText("");
		this.explanationField.setText("");
		this.exampleField.setText("");
		this.tagField.setText("");
		this.askServer = new AskServerToAddWord(nameField, explanationField,
				exampleField, tagField, date, database, true);
		MyPopup myPopup = new MyPopup("Add Word", askServer, sendButton, true);
	}
}
