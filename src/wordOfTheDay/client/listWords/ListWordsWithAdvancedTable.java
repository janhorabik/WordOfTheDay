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
import wordOfTheDay.client.advancedTable.RowSelectionListener;
import wordOfTheDay.client.advancedTable.ServiceUtils;
import wordOfTheDay.client.advancedTable.TableModelServiceAsync;
import wordOfTheDay.client.home.Home;

import com.google.gwt.user.client.ui.Button;
import com.google.gwt.user.client.ui.ClickListener;
import com.google.gwt.user.client.ui.HasHorizontalAlignment;
import com.google.gwt.user.client.ui.HorizontalPanel;
import com.google.gwt.user.client.ui.TextBox;
import com.google.gwt.user.client.ui.VerticalPanel;
import com.google.gwt.user.client.ui.Widget;

public class ListWordsWithAdvancedTable implements CheckBoxesListener {

	public void onModuleLoad(VerticalPanel rootPanel, ListWords listWords, Home home) {

		final AdvancedTable table = new AdvancedTable();
		table.addCheckBoxesListener(this);
		TableModelServiceAsync usersTableService = ServiceUtils
				.getTableModelServiceAsync();
		table.setTableModelService(usersTableService);
		table.addRowSelectionListener(new RowSelectionListener() {
			public void onRowSelected(AdvancedTable sender, String rowId) {
				System.out.println("Row " + rowId + " selected.");
			}
		});

		table.setSize("990px", "400px");
		table.setPageSize(10);

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
				DataFilter filter = new DataFilter("keyword", filterText);
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
		AskServer askServer = new AskServerToRemoveSelected(table, listWords, home);
		MyPopup myPopup = new MyPopup("Remove words", askServer,
				buttonRemoveSelected, false);

		rootPanel.add(horizontalPanel);
		rootPanel.add(table);
	}

	public void checkBoxesChanged(boolean someRowsSelected) {
		buttonRemoveSelected.setVisible(someRowsSelected);
	}

	private Button buttonRemoveSelected;
}
