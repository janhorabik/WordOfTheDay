/**
 * Example GWT module that shows how to use the GWT AdvancedTable widget
 * and its data provider - the TableModelService interface.
 * 
 * (c) 2007 by Svetlin Nakov - http://www.nakov.com
 * National Academy for Software Development - http://academy.devbg.org 
 * This software is freeware. Use it at your own risk.
 */

package wordOfTheDay.client.listWords;

import wordOfTheDay.client.DateHelper;
import wordOfTheDay.client.TooltipListener;
import wordOfTheDay.client.Word9;
import wordOfTheDay.client.MyPopup.AskServer;
import wordOfTheDay.client.MyPopup.MyPopup;
import wordOfTheDay.client.dbOnClient.DatabaseOnClient;
import wordOfTheDay.client.listWords.advancedTable.AdvancedTable;
import wordOfTheDay.client.listWords.advancedTable.CheckBoxesListener;
import wordOfTheDay.client.listWords.advancedTable.DataFilter;
import wordOfTheDay.client.listWords.advancedTable.LabelExactFilter;
import wordOfTheDay.client.listWords.advancedTable.RowSelectionListener;
import wordOfTheDay.client.listWords.advancedTable.TableColumn;
import wordOfTheDay.client.listWords.advancedTable.TableModelService;

import com.google.gwt.event.dom.client.MouseDownEvent;
import com.google.gwt.event.dom.client.MouseDownHandler;
import com.google.gwt.event.dom.client.MouseUpEvent;
import com.google.gwt.event.dom.client.MouseUpHandler;
import com.google.gwt.event.logical.shared.SelectionEvent;
import com.google.gwt.event.logical.shared.SelectionHandler;
import com.google.gwt.user.client.rpc.AsyncCallback;
import com.google.gwt.user.client.ui.Anchor;
import com.google.gwt.user.client.ui.Button;
import com.google.gwt.user.client.ui.ClickListener;
import com.google.gwt.user.client.ui.HasHorizontalAlignment;
import com.google.gwt.user.client.ui.HorizontalPanel;
import com.google.gwt.user.client.ui.MouseListener;
import com.google.gwt.user.client.ui.TextBox;
import com.google.gwt.user.client.ui.Tree;
import com.google.gwt.user.client.ui.TreeItem;
import com.google.gwt.user.client.ui.VerticalPanel;
import com.google.gwt.user.client.ui.Widget;

public class ListWordsWithAdvancedTable implements CheckBoxesListener,
		RowSelectionListener {

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
		pairPanel.add(createTree());
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

		buttonRemoveSelected = new Button("Remove");
		buttonRemoveSelected.setVisible(false);
		horizontalPanel.add(buttonRemoveSelected);
		buttonRemoveSelected.setWidth("100");
		AskServer askServer = new AskServerToRemoveSelected(table, listWords,
				database);
		MyPopup myPopup = new MyPopup("Remove words", askServer,
				buttonRemoveSelected, false);
		return horizontalPanel;
	}

	private static int MAX_LABEL_LEN = 13;

	private Tree createTree() {
		Tree tree = new Tree();
		tree.setStyleName("wordoftheday");
		for (final String label : database.getLabels()) {
			String shortLabel = label.length() > MAX_LABEL_LEN ? label
					.substring(0, MAX_LABEL_LEN) : label;
			final Anchor anchor = new Anchor(shortLabel);
			if (shortLabel.length() != label.length())
				anchor.addMouseListener(new TooltipListener(label, 4000));
			TreeItem item = tree.addItem(anchor);
			item.addItem("sub");
		}
		tree.addSelectionHandler(new SelectionHandler<TreeItem>() {
			public void onSelection(SelectionEvent<TreeItem> event) {
				String labelToFilter = event.getSelectedItem().getText();
				DataFilter filter = new LabelExactFilter(labelToFilter);
				DataFilter[] filters = { filter };
				table.applyFilters(filters);
			}
		});

		tree.addMouseDownHandler(new MouseDownHandler() {

			public void onMouseDown(MouseDownEvent event) {
				event.getSource().toString();
			}
		});
		return tree;
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

}
