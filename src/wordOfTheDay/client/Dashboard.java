package wordOfTheDay.client;

import wordOfTheDay.client.addWord.EditWord;
import wordOfTheDay.client.dbOnClient.DatabaseOnClient;
import wordOfTheDay.client.dbOnClient.DatabaseUpdatedNotifier;
import wordOfTheDay.client.deleteWords.DeleteWords;
import wordOfTheDay.client.downloadFile.DownloadXml;
import wordOfTheDay.client.listWords.ListWords;
import wordOfTheDay.client.tooltip.MobileTooltip;
import wordOfTheDay.client.tooltip.MobileTooltipMouseListener;
import wordOfTheDay.client.uploadFile.AddWordsXml;

import com.google.gwt.event.logical.shared.SelectionEvent;
import com.google.gwt.event.logical.shared.SelectionHandler;
import com.google.gwt.user.client.ui.DecoratedTabPanel;
import com.google.gwt.user.client.ui.DecoratorPanel;
import com.google.gwt.user.client.ui.FocusPanel;
import com.google.gwt.user.client.ui.HTML;
import com.google.gwt.user.client.ui.Label;
import com.google.gwt.user.client.ui.RootPanel;
import com.google.gwt.user.client.ui.VerticalPanel;

/**
 * Entry point classes define <code>onModuleLoad()</code>.
 */
public class Dashboard implements DatabaseUpdatedNotifier {

	private DatabaseOnClient database;

	DecoratorPanel tabPanel = new DecoratorPanel();

	EditWord addWord = null;

	public static MobileTooltip tooltip = new MobileTooltip("");

	public static MobileTooltipMouseListener tooltipListener = new MobileTooltipMouseListener(
			tooltip);

	Dashboard(final RootPanel rootPanelArg, DatabaseOnClient database) {
		rootPanel = rootPanelArg;
		this.database = database;
		rootPanel.clear();
		rootPanel.add(notLoggedIn);
		// Create a tab panel

		tabPanel.setWidth("1000px");
//		tabPanel.setAnimationEnabled(true);

		// Add Words XML tab
//		VerticalPanel addWordsXmlPanel = new VerticalPanel();
//		AddWordsXml addWords = new AddWordsXml();
//		addWords.initiateUploadFile(addWordsXmlPanel, database);
//		tabPanel.add(addWordsXmlPanel, "Add Words from xml");

		// List Words tab

		final VerticalPanel listWordsPanel = new VerticalPanel();
		listWords = new ListWords(listWordsPanel, database);
		listWords.initiate();
		tabPanel.add(listWordsPanel);
//		tabPanel.addSelectionHandler(new SelectionHandler<Integer>() {
//			public void onSelection(SelectionEvent<Integer> event) {
//				if (event.getSelectedItem() == listNum) {
//					listWords.initiate();
//				}
//			}
//		});

		// Download Words tab
//		VerticalPanel downloadWordsPanel = new VerticalPanel();
//		DownloadXml downloadXml = new DownloadXml();
//		downloadXml.initiateDownloadFile(downloadWordsPanel);
//		tabPanel.add(downloadWordsPanel, "Download Words");

//		tabPanel.selectTab(0);
		Label l = new Label("Personal Knowledge Base");
		l.setStyleName("h1");
		rootPanel.add(l);
		rootPanel.add(tabPanel);
		setVisible(true);
	}

	public void setVisible(boolean isVisible) {
		rootPanel.setVisible(isVisible);
	}

	private RootPanel rootPanel;

	ListWords listWords = null;

	private HTML notLoggedIn = new HTML("<div id='date'>Please log in.</div>");

	public static final int listNum = 3;

	public void databaseUpdated() {
		if (database.getLogin().equals("Anonymous")) {
			tabPanel.setVisible(false);
			notLoggedIn.setVisible(true);
		} else {
			tabPanel.setVisible(true);
			notLoggedIn.setVisible(false);
			// initiate();

			if (listWords != null) {
				listWords.update();
			}
			if (addWord != null)
				addWord.update();
		}
	}
}
