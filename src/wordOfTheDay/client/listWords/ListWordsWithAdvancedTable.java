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
import wordOfTheDay.client.advancedTable.AdvancedTable;
import wordOfTheDay.client.advancedTable.CheckBoxesListener;
import wordOfTheDay.client.advancedTable.DataFilter;
import wordOfTheDay.client.advancedTable.LabelExactFilter;
import wordOfTheDay.client.advancedTable.RowSelectionListener;
import wordOfTheDay.client.advancedTable.TableColumn;
import wordOfTheDay.client.advancedTable.TableModelService;
import wordOfTheDay.client.dbOnClient.DatabaseOnClient;

import com.google.gwt.event.logical.shared.SelectionEvent;
import com.google.gwt.event.logical.shared.SelectionHandler;
import com.google.gwt.user.client.rpc.AsyncCallback;
import com.google.gwt.user.client.ui.Anchor;
import com.google.gwt.user.client.ui.Button;
import com.google.gwt.user.client.ui.ClickListener;
import com.google.gwt.user.client.ui.HasHorizontalAlignment;
import com.google.gwt.user.client.ui.HorizontalPanel;
import com.google.gwt.user.client.ui.TextBox;
import com.google.gwt.user.client.ui.Tree;
import com.google.gwt.user.client.ui.TreeItem;
import com.google.gwt.user.client.ui.VerticalPanel;
import com.google.gwt.user.client.ui.Widget;

public class ListWordsWithAdvancedTable implements CheckBoxesListener {

	private DatabaseOnClient database;

	private Button buttonRemoveSelected;

	private AdvancedTable table;

	public void initiate(VerticalPanel rootPanel, ListWords listWords,
			DatabaseOnClient database) {

		this.database = database;
		table = new AdvancedTable();
		table.setSize("990px", "400px");
		table.setPageSize(10);
		table.addCheckBoxesListener(this);
		LocalGetWordsServiceImpl serice = new LocalGetWordsServiceImpl(database);
		table.setTableModelService(serice);
		table.addRowSelectionListener(new RowSelectionListener() {
			public void onRowSelected(AdvancedTable sender, String rowId) {
				System.out.println("Row " + rowId + " selected.");
			}
		});

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
				DataFilter filter = new wordOfTheDay.client.advancedTable.SearchFilter(
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

		rootPanel.add(horizontalPanel);
		HorizontalPanel tablePanel = new HorizontalPanel();
		tablePanel.add(createTree());
		tablePanel.add(table);
		rootPanel.add(tablePanel);
	}

	private Tree createTree() {
		Tree tree = new Tree();
		tree.setStyleName("wordoftheday");
		for (String label : database.getLabels()) {
			TreeItem item = tree.addItem(new Anchor(label));
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

		return tree;
	}

	public void checkBoxesChanged(boolean someRowsSelected) {
		buttonRemoveSelected.setVisible(someRowsSelected);
	}

}
