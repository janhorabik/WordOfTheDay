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
import wordOfTheDay.client.dbOnClient.DatabaseOnClient;
import wordOfTheDay.client.listWords.advancedTable.AddLabelListener;
import wordOfTheDay.client.listWords.advancedTable.AdvancedTable;
import wordOfTheDay.client.listWords.advancedTable.CheckBoxesListener;
import wordOfTheDay.client.listWords.advancedTable.DataFilter;
import wordOfTheDay.client.listWords.advancedTable.RowSelectionListener;
import wordOfTheDay.client.test.MobileTooltip;
import wordOfTheDay.client.test.MobileTooltipMouseListener;

import com.google.gwt.core.client.GWT;
import com.google.gwt.event.dom.client.MouseDownEvent;
import com.google.gwt.event.dom.client.MouseDownHandler;
import com.google.gwt.event.dom.client.MouseUpEvent;
import com.google.gwt.event.dom.client.MouseUpHandler;
import com.google.gwt.user.client.rpc.AsyncCallback;
import com.google.gwt.user.client.ui.Anchor;
import com.google.gwt.user.client.ui.Button;
import com.google.gwt.user.client.ui.ClickListener;
import com.google.gwt.user.client.ui.HasHorizontalAlignment;
import com.google.gwt.user.client.ui.HorizontalPanel;
import com.google.gwt.user.client.ui.Label;
import com.google.gwt.user.client.ui.TextBox;
import com.google.gwt.user.client.ui.VerticalPanel;
import com.google.gwt.user.client.ui.Widget;

public class ListWordsWithAdvancedTable implements CheckBoxesListener,
		RowSelectionListener, AddLabelListener {

	private DatabaseOnClient database;

	private Button buttonRemoveSelected;

	private AdvancedTable table;

	private ListWords listWords;

	private VerticalPanel tablePanel;

	public void initiate(VerticalPanel rootPanel, ListWords listWords,
			DatabaseOnClient database) {

		this.listWords = listWords;
		this.database = database;
		// create this.table
		createAdvancedTable(database);

		final HorizontalPanel searchPanel = createHorizontalPanel(listWords,
				database);

		HorizontalPanel pairPanel = new HorizontalPanel();
		LabelsTree labelsTree = new LabelsTree(this.database, this.table);
		pairPanel.add(labelsTree.createTree());
		pairPanel.add(table);

		this.tablePanel = new VerticalPanel();
		this.tablePanel.add(searchPanel);
		this.tablePanel.add(pairPanel);

		rootPanel.add(this.tablePanel);
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
		horizontalPanel.setSize("402px", "23px");

		final TextBox textBoxFilter = new TextBox();
		horizontalPanel.add(textBoxFilter);
		textBoxFilter.setWidth("100%");
		horizontalPanel.setCellWidth(textBoxFilter, "100%");

		addSearchButton(horizontalPanel, textBoxFilter);
		// addNewWordButton(horizontalPanel);

		buttonRemoveSelected = new Button("Remove");
		buttonRemoveSelected.addMouseUpHandler(new MouseUpHandler() {
			public void onMouseUp(MouseUpEvent event) {
				buttonRemoveSelected.setText(event.getSource().toString());
			}
		});
		buttonRemoveSelected.setVisible(false);
		horizontalPanel.add(buttonRemoveSelected);
		buttonRemoveSelected.setWidth("100");
		AskServer askServer = new AskServerToRemoveSelected(table, listWords,
				database);
		MyPopup myPopup = new MyPopup("Remove words", askServer,
				buttonRemoveSelected, false);
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

	private void addSearchButton(final HorizontalPanel horizontalPanel,
			final TextBox textBoxFilter) {
		final Button buttonApplyFilter = new Button();
		horizontalPanel.add(buttonApplyFilter);
		buttonApplyFilter.addClickListener(new ClickListener() {
			public void onClick(Widget sender) {
				String filterText = textBoxFilter.getText();
				DataFilter filter = new wordOfTheDay.client.listWords.advancedTable.SearchFilter(
						filterText);
				DataFilter[] filters = { filter };
				table.applyFilters(filters);
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
							database.update();
						}

						@Override
						public void onFailure(Throwable caught) {
						}
					});
		}
	}
}
