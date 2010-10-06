package wordOfTheDay.client.listWords;

import java.util.LinkedList;
import java.util.List;
import java.util.Set;

import wordOfTheDay.client.Word8;
import wordOfTheDay.client.MyPopup.AskServer;
import wordOfTheDay.client.MyPopup.ServerResponse;
import wordOfTheDay.client.advancedTable.AdvancedTable;
import wordOfTheDay.client.deleteWords.DeleteWordsService;
import wordOfTheDay.client.deleteWords.DeleteWordsServiceAsync;

import com.google.gwt.core.client.GWT;
import com.google.gwt.user.client.rpc.AsyncCallback;
import com.google.gwt.user.client.ui.SuggestBox;
import com.google.gwt.user.client.ui.TextArea;
import com.google.gwt.user.client.ui.TextBox;

class AskServerToRemoveSelected implements AskServer {
	private AdvancedTable table;

	private final DeleteWordsServiceAsync deleteWordService = GWT
			.create(DeleteWordsService.class);

	private ListWords listWords;

	public AskServerToRemoveSelected(final AdvancedTable table, ListWords listWords) {
		System.out.println("const");
		this.table = table;
		this.listWords = listWords;
	}

	private void clearFields() {
		System.out.println("clear fields");
		this.listWords.initiate();
	}

	public void askServer(final ServerResponse serverResponse) {
		System.out.println("askServer");
		Set set = this.table.getMarkedRows();
		List<String> dates = new LinkedList<String>();
		int i = 1;
		for (Object o : set) {
			dates.add((String)o);
		}
		System.out.println(set);
		deleteWordService.deleteWords(dates, new AsyncCallback<String>() {
			public void onFailure(Throwable caught) {
				serverResponse.error(caught.toString());
				clearFields();
			}

			public void onSuccess(String result) {
				System.out.println("success");
				serverResponse.serverReplied(result);
				clearFields();
			}
		});
	}
}