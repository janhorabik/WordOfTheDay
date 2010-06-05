package wordOfTheDay.client.listWords;

import java.util.List;
import java.util.Vector;

import wordOfTheDay.client.DateHelper;
import wordOfTheDay.client.Word4;

import com.google.gwt.core.client.GWT;
import com.google.gwt.user.client.rpc.AsyncCallback;
import com.google.gwt.user.client.ui.FlexTable;
import com.google.gwt.user.client.ui.HTML;
import com.google.gwt.user.client.ui.Label;
import com.google.gwt.user.client.ui.RootPanel;
import com.google.gwt.user.client.ui.ScrollPanel;
import com.google.gwt.user.client.ui.VerticalPanel;

public class ListWords {

	private final ListWordsServiceAsync listWordsService = GWT
			.create(ListWordsService.class);

	public void initiateListWords(final VerticalPanel listWordsPanel) {
		listWordsPanel.clear();
		listWordsPanel.add(new HTML("<div class='wordOfTheDay'>Connecting...</div>"));
		listWordsService.listWords(new AsyncCallback<Vector<Word4>>() {
			public void onFailure(Throwable caught) {
				listWordsPanel.clear();
				listWordsPanel.add(new HTML(
						"Error occured while connecting the service"));
			}

			public void onSuccess(Vector<Word4> result) {

				FlexTable tableWithTitles = new FlexTable();
				Label date = new Label("Date");
				date.setStyleName("wordOfTheDay");
				Label name = new Label("Name");
				name.setStyleName("wordOfTheDay");
				Label meaning = new Label("Meaning");
				meaning.setStyleName("wordOfTheDay");
				Label usage = new Label("Usage");
				usage.setStyleName("wordOfTheDay");

				tableWithTitles.setWidget(0, 0, date);
				tableWithTitles.setWidget(0, 1, name);
				tableWithTitles.setWidget(0, 2, meaning);
				tableWithTitles.setWidget(0, 3, usage);
				int row = 0;
				FlexTable table = new FlexTable();
//				table.setWidth("800px");
				for (Word4 word : result) {

					Label label0 = new Label(DateHelper
							.toStringWithoutSpace(word.getDate()));
					label0.setStyleName("wordOfTheDay");
					Label label1 = new Label(word.getName());
					label1.setStyleName("wordOfTheDay");
					Label label2 = new Label(word.getExplanation());
					label2.setStyleName("wordOfTheDay");
					HTML label3 = new HTML(usageInList(word.getUsage()));
					label3.setStyleName("wordOfTheDay");
					table.setWidget(row, 0, label0);
					table.setWidget(row, 1, label1);
					table.setWidget(row, 2, label2);
					table.setWidget(row, 3, label3);
					row++;
				}
				ScrollPanel scroller = new ScrollPanel(table);
				scroller.setSize("1000px", "400px");
				// listWordsPanel.add(tableWithTitles);
				listWordsPanel.clear();
				listWordsPanel.add(scroller);
			}

			private String usageInList(List<String> usage) {
				String ret = new String();
				ret += "<ul>";
				for (String us : usage) {
					ret += "<li>" + us + "</li>";
				}
				ret += "</ul>";
				return ret;
			}
		});
	}
}
