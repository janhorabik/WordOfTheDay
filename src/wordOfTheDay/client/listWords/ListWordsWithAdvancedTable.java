/**
 * Example GWT module that shows how to use the GWT AdvancedTable widget
 * and its data provider - the TableModelService interface.
 * 
 * (c) 2007 by Svetlin Nakov - http://www.nakov.com
 * National Academy for Software Development - http://academy.devbg.org 
 * This software is freeware. Use it at your own risk.
 */

package wordOfTheDay.client.listWords;

import wordOfTheDay.client.MyPopup.AskServer;
import wordOfTheDay.client.MyPopup.MyPopup;
import wordOfTheDay.client.dbOnClient.DatabaseOnClient;
import wordOfTheDay.client.listWords.notesTable.CheckBoxesListener;
import wordOfTheDay.client.listWords.notesTable.DataFilter;
import wordOfTheDay.client.listWords.notesTable.MessagesPanel;
import wordOfTheDay.client.listWords.notesTable.NotesTable;
import wordOfTheDay.client.listWords.notesTable.SearchFilter;

import com.google.gwt.user.client.ui.Button;
import com.google.gwt.user.client.ui.ClickListener;
import com.google.gwt.user.client.ui.DockPanel;
import com.google.gwt.user.client.ui.HasHorizontalAlignment;
import com.google.gwt.user.client.ui.HorizontalPanel;
import com.google.gwt.user.client.ui.TextBox;
import com.google.gwt.user.client.ui.VerticalPanel;
import com.google.gwt.user.client.ui.Widget;
import com.smartgwt.client.widgets.layout.HLayout;

public class ListWordsWithAdvancedTable implements CheckBoxesListener {

	private DatabaseOnClient database;

	private Button buttonRemoveSelected;

	private Button buttonAddNote;

	private VerticalPanel tablePanel;

	private LabelsTree labelsTree;

	private NotesTable notesTable;

	private HorizontalPanel notesTablePanel = new HorizontalPanel();

	private HorizontalPanel labelsPanel = new HorizontalPanel();

	private HorizontalPanel modelsPanel = new HorizontalPanel();

	private HorizontalPanel panelForMessages = new HorizontalPanel();

	private ModelsList modelsList;

	private DockPanel notesPanel = new DockPanel();

	private Button buttonAddModel;

	private MessagesPanel messagesPanel = new MessagesPanel(panelForMessages);

	public void initiate(VerticalPanel rootPanel, ListWords listWords,
			DatabaseOnClient database) {

		database.setListWordsWithAdvancedTable(this);
		this.database = database;
		this.notesTable = new NotesTable(this.database, this.notesTablePanel,
				this);
		this.labelsTree = new LabelsTree(this.database, this.labelsPanel,
				this.messagesPanel);
		this.modelsList = new ModelsList(this.database, this.modelsPanel,
				this.messagesPanel);
		this.database.setLabelsTree(labelsTree);
		this.database.setNotesTable(notesTable);
		this.database.setModelsList(modelsList);
		labelsPanel.clear();
		notesPanel.add(labelsPanel, DockPanel.WEST);
		notesTablePanel.clear();
		notesPanel.add(modelsPanel, DockPanel.EAST);
		notesPanel.setHorizontalAlignment(HorizontalPanel.ALIGN_CENTER);
		notesPanel.add(panelForMessages, DockPanel.NORTH);
		notesPanel.add(notesTablePanel, DockPanel.NORTH);

		this.tablePanel = new VerticalPanel();
		// tablePanel.setStyleName("mainPanel");
		final HorizontalPanel searchPanel = createHorizontalPanel(listWords,
				database);
		this.tablePanel.add(searchPanel);
		this.tablePanel.add(notesPanel);

		rootPanel.add(this.tablePanel);

		update();
		// HLayout layout = new HLayout();
		// layout.setMembers(labelsPanel, modelsPanel);
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

	public void update() {
		this.buttonAddNote.setVisible(database.getModels().size() > 0);
		this.labelsTree.update();
		this.notesTable.drawTable();
		this.modelsList.update();
	}

	public void hideRemoveButton() {
		this.buttonRemoveSelected.setVisible(false);
	}

	public void actNumModels(int size) {
		buttonAddNote.setVisible(size > 0);
	}

}
