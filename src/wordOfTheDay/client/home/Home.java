package wordOfTheDay.client.home;

import wordOfTheDay.client.DateHelper;
import wordOfTheDay.client.DragAndDrop;
import wordOfTheDay.client.Word9;
import wordOfTheDay.client.dbOnClient.DatabaseOnClient;
import wordOfTheDay.client.dbOnClient.DatabaseUpdatedNotifier;
import wordOfTheDay.client.listWords.ListWordsService;
import wordOfTheDay.client.listWords.ListWordsServiceAsync;

import com.google.gwt.core.client.GWT;
import com.google.gwt.event.dom.client.ClickEvent;
import com.google.gwt.event.dom.client.ClickHandler;
import com.google.gwt.user.client.ui.Anchor;
import com.google.gwt.user.client.ui.HorizontalPanel;
import com.google.gwt.user.client.ui.Image;
import com.google.gwt.user.client.ui.Label;
import com.google.gwt.user.client.ui.RootPanel;

/**
 * Entry point classes define <code>onModuleLoad()</code>.
 */
public class Home implements DatabaseUpdatedNotifier {

	private final ListWordsServiceAsync listWordsService = GWT
			.create(ListWordsService.class);

	public Home(final RootPanel wordPanelArg, DatabaseOnClient database) {
		this.database = database;
		wordPanel = wordPanelArg;
		wordPanel.clear();
		wordPanel.add(image);
		wordPanel.add(titleLabel);
		HorizontalPanel dateLinkPanel = new HorizontalPanel();
		dateLinkPanel.setWidth("30%");
		dateLinkPanel.setHorizontalAlignment(HorizontalPanel.ALIGN_LEFT);
		dateLinkPanel.add(previousLink);
		dateLinkPanel.setHorizontalAlignment(HorizontalPanel.ALIGN_CENTER);
		dateLinkPanel.add(dateLabel);
		dateLinkPanel.setHorizontalAlignment(HorizontalPanel.ALIGN_RIGHT);
		dateLinkPanel.add(nextLink);
		
		wordPanel.add(dateLinkPanel);
		wordPanel.setWidgetPosition(dateLinkPanel, 500, 150);
		wordPanel.add(wordName);
		wordPanel.add(meaningLabel);
		wordPanel.add(meaningValue);
		wordPanel.add(exampleLabel);
		wordPanel.add(exampleValue);

		dateLabel.setStyleName("date");
		titleLabel.setStyleName("h1");
		wordName.setStyleName("name");
		meaningLabel.setStyleName("label");
		meaningValue.setStyleName("explanation");
		exampleLabel.setStyleName("label");
		exampleValue.setStyleName("usageExample");
		previousLink.addClickHandler(new ClickHandler() {
			public void onClick(ClickEvent event) {
				--currentIndexOfWord;
				updateWord();
			}
		});
		nextLink.addClickHandler(new ClickHandler() {
			public void onClick(ClickEvent event) {
				++currentIndexOfWord;
				updateWord();
			}
		});
		setVisible(true);
		setAllVisible(false);
		this.image.setVisible(true);
	}

	private void setAllVisible(boolean visible) {
		this.titleLabel.setVisible(visible);
		this.previousLink.setVisible(visible);
		this.dateLabel.setVisible(visible);
		this.nextLink.setVisible(visible);
		this.wordName.setVisible(visible);
		this.meaningLabel.setVisible(visible);
		this.meaningValue.setVisible(visible);
		this.exampleLabel.setVisible(visible);
		this.exampleValue.setVisible(visible);
		this.image.setVisible(visible);
	}

	public void updateWord() {
		// has some data
		if (currentIndexOfWord != -1) {
			Word9 word9 = database.getWords().get(currentIndexOfWord);
			date = word9.getDate();
			previousLink.setVisible(currentIndexOfWord != 0);
			dateLabel.setText(DateHelper.toString(date));
			nextLink.setVisible(currentIndexOfWord != todayIndex
					&& currentIndexOfWord != database.getWords().size() - 1);
			wordName.setText(word9.getName());
			meaningValue.setText(word9.getExplanation());
			if (!word9.getUsage().isEmpty())
				exampleValue.setText(word9.getUsage().get(0));
		}
	}

	public static int getDate(int year, int month, int day) {
		// 20100523
		return year * 10000 + month * 100 + day;
	}

	// TODO NO COPY-PASTE
	public static int getCurrentDate() {
		java.util.Date d = new java.util.Date();
		return getDate(d.getYear() + 1900, d.getMonth() + 1, d.getDate());
	}

	public void update() {
		setAllVisible(false);
		date = getCurrentDate();
		int i = -1;
		for (Word9 word9 : database.getWords()) {
			++i;
			if (word9.getDate() == date) {
				currentIndexOfWord = i;
				todayIndex = i;
				setAllVisible(true);
				image.setVisible(false);
				updateWord();
				return;
			}
		}
		currentIndexOfWord = -1;
		todayIndex = -1;
	}

	public void initiate() {
		update();
	}

	public void setVisible(boolean isVisible) {
		wordPanel.setVisible(isVisible);
	}

	public void databaseUpdated() {
		update();
	}

	private final RootPanel wordPanel;

	private final Anchor previousLink = new Anchor("previous");

	private final Label titleLabel = new Label("Word of the day");

	private final Label dateLabel = new Label();

	private final Anchor nextLink = new Anchor("next");

	private final Label wordName = new Label();

	private final Label meaningLabel = new Label("Meaning");

	private final Label meaningValue = new Label();

	private final Label exampleLabel = new Label("Example");

	private final Label exampleValue = new Label();

	private final Image image = new Image("Loading.gif");

	private int date = 0;

	DatabaseOnClient database;

	int currentIndexOfWord = -1;

	int todayIndex = -1;

}