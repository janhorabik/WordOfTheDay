package wordOfTheDay.client.listWords;

import java.util.LinkedList;
import java.util.List;
import java.util.Set;

import wordOfTheDay.client.Note;
import wordOfTheDay.client.Services;
import wordOfTheDay.client.Word9;
import wordOfTheDay.client.MyPopup.AskServer;
import wordOfTheDay.client.MyPopup.ServerResponse;
import wordOfTheDay.client.dbOnClient.DatabaseOnClient;
import wordOfTheDay.client.deleteWords.DeleteWordsService;
import wordOfTheDay.client.deleteWords.DeleteWordsServiceAsync;
import wordOfTheDay.client.home.Home;
import wordOfTheDay.client.listWords.advancedTable.AdvancedTable;
import wordOfTheDay.client.listWords.notesTable.NotesTable;

import com.google.gwt.core.client.GWT;
import com.google.gwt.user.client.rpc.AsyncCallback;
import com.google.gwt.user.client.ui.SuggestBox;
import com.google.gwt.user.client.ui.TextArea;
import com.google.gwt.user.client.ui.TextBox;

class AskServerToRemoveSelected implements AskServer {
	private NotesTable table;

	private final DeleteWordsServiceAsync deleteWordService = GWT
			.create(DeleteWordsService.class);

	private ListWords listWords;

	private DatabaseOnClient database;

	public AskServerToRemoveSelected(final NotesTable table,
			ListWords listWords, DatabaseOnClient database) {
		System.out.println("const");
		this.table = table;
		this.listWords = listWords;
		this.database = database;
	}

	private void clearFields() {
		System.out.println("clear fields");
//		this.listWords.initiate();
		this.database.update();
	}

	public void askServer(final ServerResponse serverResponse) {
		System.out.println("askServer");
		Set<Note> set = this.table.getSelectedNoteIds();
		Services.noteService.removeNotes(set, new AsyncCallback<Void>() {

			public void onFailure(Throwable caught) {
				serverResponse.error(caught.toString());
				clearFields();
			}

			public void onSuccess(Void result) {
				System.out.println("success");
				clearFields();
				serverResponse.serverReplied("Notes were removed.");
			}
		});
	}
}