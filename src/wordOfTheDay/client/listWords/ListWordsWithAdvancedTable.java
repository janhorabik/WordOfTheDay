/**
 * Example GWT module that shows how to use the GWT AdvancedTable widget
 * and its data provider - the TableModelService interface.
 * 
 * (c) 2007 by Svetlin Nakov - http://www.nakov.com
 * National Academy for Software Development - http://academy.devbg.org 
 * This software is freeware. Use it at your own risk.
 */

package wordOfTheDay.client.listWords;

import wordOfTheDay.client.Dashboard;
import wordOfTheDay.client.DateHelper;
import wordOfTheDay.client.Word9;
import wordOfTheDay.client.MyPopup.AskServer;
import wordOfTheDay.client.MyPopup.MyPopup;
import wordOfTheDay.client.addWord.AddWordService;
import wordOfTheDay.client.addWord.AddWordServiceAsync;
import wordOfTheDay.client.addWord.AskServerToAddModel;
import wordOfTheDay.client.addWord.AskServerToAddNote;
import wordOfTheDay.client.dbOnClient.DatabaseOnClient;
import wordOfTheDay.client.listWords.advancedTable.AddLabelListener;
import wordOfTheDay.client.listWords.advancedTable.AdvancedTable;
import wordOfTheDay.client.listWords.advancedTable.CheckBoxesListener;
import wordOfTheDay.client.listWords.advancedTable.DataFilter;
import wordOfTheDay.client.listWords.advancedTable.RowSelectionListener;
import wordOfTheDay.client.listWords.advancedTable.SearchFilter;
import wordOfTheDay.client.listWords.notesTable.MessagesPanel;
import wordOfTheDay.client.listWords.notesTable.NotesTable;

import com.google.gwt.core.client.GWT;
import com.google.gwt.user.client.rpc.AsyncCallback;
import com.google.gwt.user.client.ui.Button;
import com.google.gwt.user.client.ui.ClickListener;
import com.google.gwt.user.client.ui.DockPanel;
import com.google.gwt.user.client.ui.FocusPanel;
import com.google.gwt.user.client.ui.HasHorizontalAlignment;
import com.google.gwt.user.client.ui.HorizontalPanel;
import com.google.gwt.user.client.ui.TextBox;
import com.google.gwt.user.client.ui.VerticalPanel;
import com.google.gwt.user.client.ui.Widget;

public class ListWordsWithAdvancedTable implements CheckBoxesListener,
		RowSelectionListener, AddLabelListener {

	private DatabaseOnClient database;

	private Button buttonRemoveSelected;

	private Button buttonAddNote;

	private AdvancedTable table;

	private ListWords listWords;

	private VerticalPanel tablePanel;

	private LabelsTree labelsTree;

	private NotesTable notesTable;

	private HorizontalPanel notesTablePanel = new HorizontalPanel();

	private HorizontalPanel labelsPanel = new HorizontalPanel();

	private HorizontalPanel modelsPanel = new HorizontalPanel();

	private HorizontalPanel panelForMessages = new HorizontalPanel();

	private ModelsList modelsList;

	private DockPanel pairPanel = new DockPanel();

	private Button buttonAddModel;

	private MessagesPanel messagesPanel = new MessagesPanel(panelForMessages);

	// public static Label messagesLabel;

	public void initiate(VerticalPanel rootPanel, ListWords listWords,
			DatabaseOnClient database) {

		database.setListWordsWithAdvancedTable(this);
		// rootPanel.add(example);
		this.listWords = listWords;
		this.database = database;
		// create this.table
		createAdvancedTable(database);

		final FocusPanel focusPanel = new FocusPanel();
		focusPanel.addMouseListener(Dashboard.tooltipListener);
		focusPanel.add(pairPanel);

		this.notesTable = new NotesTable(this.database, this.notesTablePanel,
				this);
		this.labelsTree = new LabelsTree(this.database, this.notesTable,
				this.labelsPanel, this.messagesPanel);
		this.modelsList = new ModelsList(this.database, this.notesTable,
				this.labelsTree, this.modelsPanel, this.messagesPanel);
		this.database.setLabelsTree(labelsTree);
		this.database.setNotesTable(notesTable);
		this.database.setModelsList(modelsList);
		labelsPanel.clear();
		pairPanel.add(labelsPanel, DockPanel.WEST);
		notesTablePanel.clear();
		pairPanel.add(modelsPanel, DockPanel.EAST);
		pairPanel.setHorizontalAlignment(HorizontalPanel.ALIGN_CENTER);
		pairPanel.add(panelForMessages, DockPanel.NORTH);
		pairPanel.add(notesTablePanel, DockPanel.NORTH);

		this.tablePanel = new VerticalPanel();
		final HorizontalPanel searchPanel = createHorizontalPanel(listWords,
				database);
		this.tablePanel.add(searchPanel);
		this.tablePanel.add(focusPanel);

		rootPanel.add(this.tablePanel);

		update();
	}

	private void createAdvancedTable(DatabaseOnClient database) {
		table = new AdvancedTable();
		table.setSize("990px", "400px");
		table.setPageSize(10);
		table.addCheckBoxesListener(this);
		table.addRowSelectionListener(this);
		table.addAddLabelListener(this);
		LocalGetWordsServiceImpl serice = new LocalGetWordsServiceImpl(database);
		table.setTableModelService(serice);
	}

	private HorizontalPanel createHorizontalPanel(ListWords listWords,
			DatabaseOnClient database) {
		final HorizontalPanel horizontalPanel = new HorizontalPanel();
		horizontalPanel.setSpacing(5);
		horizontalPanel.setSize("600px", "23px");

		final TextBox textBoxFilter = new TextBox();
		horizontalPanel.add(textBoxFilter);
		textBoxFilter.setWidth("100%");
		horizontalPanel.setCellWidth(textBoxFilter, "100%");

		addSearchButton(horizontalPanel, textBoxFilter);
		// addNewWordButton(horizontalPanel);

		/*************** Remove *****************/
		buttonRemoveSelected = new Button("Remove");
		buttonRemoveSelected.setVisible(false);
		horizontalPanel.add(buttonRemoveSelected);
		buttonRemoveSelected.setWidth("100");
		AskServer askServer = new AskServerToRemoveSelected(notesTable,
				listWords, database);
		MyPopup myPopup = new MyPopup(askServer, buttonRemoveSelected,
				this.messagesPanel);

		/**************** AddNote ***************/
		buttonAddNote = new Button("Add note");
		buttonAddNote.setVisible(false);
		buttonAddNote.setWidth("100");
		horizontalPanel.add(buttonAddNote);
		AskServer askServerToAddNote = new AskServerToAddNote(database);
		MyPopup addNotePopup = new MyPopup(askServerToAddNote, buttonAddNote,
				this.messagesPanel);

		/**************** AddModel ****************/
		buttonAddModel = new Button("Add model");
		buttonAddModel.setWidth("100");
		horizontalPanel.add(buttonAddModel);
		AskServer askServerToAddModel = new AskServerToAddModel(database);
		MyPopup addModelPopup = new MyPopup(askServerToAddModel,
				buttonAddModel, this.messagesPanel);

		return horizontalPanel;
	}

	private void addNewWordButton(HorizontalPanel horizontalPanel) {
		final Button addWordButton = new Button();
		horizontalPanel.add(addWordButton);
		final ListWords listWords = this.listWords;
		addWordButton.addClickListener(new ClickListener() {
			public void onClick(Widget sender) {
				listWords.showEmptyEdit();
			}
		});
		addWordButton.setWidth("100");
		horizontalPanel.setCellWidth(addWordButton, "100");
		horizontalPanel.setCellHorizontalAlignment(addWordButton,
				HasHorizontalAlignment.ALIGN_RIGHT);
		addWordButton.setText("Add Word");
	}

	@SuppressWarnings("deprecation")
	private void addSearchButton(final HorizontalPanel horizontalPanel,
			final TextBox textBoxFilter) {
		final Button buttonApplyFilter = new Button();
		horizontalPanel.add(buttonApplyFilter);
		buttonApplyFilter.addClickListener(new ClickListener() {
			public void onClick(Widget sender) {
				String filterText = textBoxFilter.getText();
				DataFilter filter = new SearchFilter(filterText);
				notesTable.applyFilter(filter);
			}
		});
		buttonApplyFilter.setWidth("100");
		horizontalPanel.setCellWidth(buttonApplyFilter, "100");
		horizontalPanel.setCellHorizontalAlignment(buttonApplyFilter,
				HasHorizontalAlignment.ALIGN_RIGHT);
		buttonApplyFilter.setText("Search");
	}

	public void checkBoxesChanged(boolean someRowsSelected) {
		buttonRemoveSelected.setVisible(someRowsSelected);
	}

	public void setVisible(boolean visible) {
		this.tablePanel.setVisible(visible);
	}

	public void onRowSelected(String date) {
		Word9 word = this.database.getWord(DateHelper.toIntWithoutSpace(date));
		if (word != null) {
			String label = word.getLabels().isEmpty() ? "" : word.getLabels()
					.get(0);
			String usage = word.getUsage().isEmpty() ? "" : word.getUsage()
					.get(0);
			this.listWords.showEdit(word.getName(), word.getExplanation(),
					usage, label, DateHelper.toIntWithoutSpace(date));
		}
	}

	private final AddWordServiceAsync addWordService = GWT
			.create(AddWordService.class);

	public void addLabel(String date, String label) {
		if (!database.getWord(DateHelper.toIntWithoutSpace(date)).getLabels()
				.contains(label)) {
			addWordService.addLabel(DateHelper.toIntWithoutSpace(date), label,
					new AsyncCallback<Void>() {

						@Override
						public void onSuccess(Void result) {
							// database.update();
						}

						@Override
						public void onFailure(Throwable caught) {
						}
					});
		}
	}

	public void update() {
		this.buttonAddNote.setVisible(database.getModels().size() > 0);
		this.table.updateTableData();
		// this.pairPanel.clear();
		this.labelsTree.draw();
		// labelsPanel.clear();
		// this.pairPanel.add(labelsPanel);
		// this.notesTablePanel.clear();
		this.notesTable.drawTable();
		// this.pairPanel.add(notesTablePanel);
		// this.modelsList.
		this.modelsList.update();
		// this.pairPanel.add(this.modelsList.createTree());
	}

	public void hideRemoveButton() {
		this.buttonRemoveSelected.setVisible(false);
	}

	public void actNumModels(int size) {
		buttonAddNote.setVisible(size > 0);
		
	}
}
