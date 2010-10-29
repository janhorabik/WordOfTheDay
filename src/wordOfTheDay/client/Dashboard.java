package wordOfTheDay.client;

import wordOfTheDay.client.addWord.AddWord;
import wordOfTheDay.client.deleteWords.DeleteWords;
import wordOfTheDay.client.downloadFile.DownloadXml;
import wordOfTheDay.client.home.Home;
import wordOfTheDay.client.listWords.ListWords;
import wordOfTheDay.client.uploadFile.AddWordsXml;

import com.google.gwt.event.logical.shared.SelectionEvent;
import com.google.gwt.event.logical.shared.SelectionHandler;
import com.google.gwt.user.client.ui.DecoratedTabPanel;
import com.google.gwt.user.client.ui.HTML;
import com.google.gwt.user.client.ui.RootPanel;
import com.google.gwt.user.client.ui.VerticalPanel;

/**
 * Entry point classes define <code>onModuleLoad()</code>.
 */
public class Dashboard {

	Dashboard(final RootPanel rootPanelArg) {
		rootPanel = rootPanelArg;
	}

	public void setVisible(boolean isVisible) {
		rootPanel.setVisible(isVisible);
	}

	private RootPanel rootPanel;

	public static final int listNum = 3;

	public void initiate(String reply, final Home home) {
		rootPanel.clear();
		if (reply.equals("Anonymous")) {
			rootPanel.add(new HTML("<div id='date'>Please log in.</div>"));
			return;
		}
		// Create a tab panel
		DecoratedTabPanel tabPanel = new DecoratedTabPanel();
		tabPanel.setWidth("1000px");
		tabPanel.setAnimationEnabled(true);

		// Add Word tab
		VerticalPanel addWordPanel = new VerticalPanel();
		AddWord addWord = new AddWord();
		addWord.initiateAddWord(addWordPanel, home);
		tabPanel.add(addWordPanel, "Add Word");

		// Add Words XML tab
		VerticalPanel addWordsXmlPanel = new VerticalPanel();
		AddWordsXml addWords = new AddWordsXml();
		addWords.initiateUploadFile(addWordsXmlPanel);
		tabPanel.add(addWordsXmlPanel, "Add Words from xml");

		// Delete Words tab
		VerticalPanel deleteWordsPanel = new VerticalPanel();
		DeleteWords deleteWords = new DeleteWords();
		deleteWords.initiateDeleteWords(deleteWordsPanel);
		tabPanel.add(deleteWordsPanel, "Delete Words");

		// List Words tab
		final VerticalPanel listWordsPanel = new VerticalPanel();
		final ListWords listWords = new ListWords(listWordsPanel);
		listWords.initiate(home);
		tabPanel.add(listWordsPanel, "List Words");
		tabPanel.addSelectionHandler(new SelectionHandler<Integer>() {
			public void onSelection(SelectionEvent<Integer> event) {
				if (event.getSelectedItem() == listNum) {
					listWords.initiate(home);
				}
			}
		});

		// Download Words tab
		VerticalPanel downloadWordsPanel = new VerticalPanel();
		DownloadXml downloadXml = new DownloadXml();
		downloadXml.initiateDownloadFile(downloadWordsPanel);
		tabPanel.add(downloadWordsPanel, "Download Words");

		tabPanel.selectTab(0);
		rootPanel.add(tabPanel);
	}
}
