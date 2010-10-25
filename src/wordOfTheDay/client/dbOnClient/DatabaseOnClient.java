package wordOfTheDay.client.dbOnClient;

import java.util.HashSet;
import java.util.LinkedList;
import java.util.List;
import java.util.Set;
import java.util.Vector;

import wordOfTheDay.client.Word9;
import wordOfTheDay.client.listWords.ListWordsService;
import wordOfTheDay.client.listWords.ListWordsServiceAsync;

import com.google.gwt.core.client.GWT;
import com.google.gwt.user.client.rpc.AsyncCallback;

public class DatabaseOnClient {

	private List<DatabaseUpdatedNotifier> notifiers = new LinkedList<DatabaseUpdatedNotifier>();

	private String login = "Anonymous";

	public DatabaseOnClient() {
		update();
	}

	public void addNotifier(DatabaseUpdatedNotifier notifier) {
		this.notifiers.add(notifier);
	}

	public void setLogin(String login) {
		this.login = login;
	}

	public String getLogin() {
		return this.login;
	}

	public void update() {
		listWordsService.listWords(new AsyncCallback<Vector<Word9>>() {
			public void onFailure(Throwable caught) {
				System.out.println(caught);
				// TODO show error
			}

			public void onSuccess(Vector<Word9> result) {
				words = result;
				labels.clear();
				for (Word9 word9 : result) {
					labels.addAll(word9.getLabels());
				}
				for (DatabaseUpdatedNotifier notifier : notifiers) {
					notifier.databaseUpdated();
				}
			}
		});

	}

	public Vector<Word9> getWords() {
		return this.words;
	}

	public Set<String> getLabels() {
		return labels;
	}

	private Vector<Word9> words = new Vector<Word9>();

	private Set<String> labels = new HashSet<String>();

	private final ListWordsServiceAsync listWordsService = GWT
			.create(ListWordsService.class);
}
