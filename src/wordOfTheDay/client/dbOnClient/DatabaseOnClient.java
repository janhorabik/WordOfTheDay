package wordOfTheDay.client.dbOnClient;

import java.util.Collection;
import java.util.HashMap;
import java.util.HashSet;
import java.util.LinkedList;
import java.util.List;
import java.util.Map;
import java.util.Set;
import java.util.Vector;

import wordOfTheDay.client.DataModel;
import wordOfTheDay.client.Note;
import wordOfTheDay.client.Services;
import wordOfTheDay.client.Word9;
import wordOfTheDay.client.listWords.ListWordsService;
import wordOfTheDay.client.listWords.ListWordsServiceAsync;

import com.google.gwt.core.client.GWT;
import com.google.gwt.user.client.rpc.AsyncCallback;

public class DatabaseOnClient {

	private List<DatabaseUpdatedNotifier> notifiers = new LinkedList<DatabaseUpdatedNotifier>();

	private String login = "Anonymous";

	private Map<Integer, DataModel> models = new HashMap<Integer, DataModel>();

	// key: DataModelSeqNum
	private Map<Integer, List<Note>> notes = new HashMap<Integer, List<Note>>();

	private Map<Integer, Set<String>> labels = new HashMap<Integer, Set<String>>();

	private int servicesFinished;

	private int currentDataModel = -1;

	public DatabaseOnClient() {
		// update();
	}

	public void setCurrentDataModel(int num) {
		currentDataModel = num;
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
		// listWordsService.listWords(new AsyncCallback<Vector<Word9>>() {
		// public void onFailure(Throwable caught) {
		// System.out.println(caught);
		// // TODO show error
		// }
		//
		// public void onSuccess(Vector<Word9> result) {
		// words = result;
		// labels.clear();
		// for (Word9 word9 : result) {
		// labels.addAll(word9.getLabels());
		// }
		// for (DatabaseUpdatedNotifier notifier : notifiers) {
		// notifier.databaseUpdated();
		// }
		// }
		// });
		this.servicesFinished = 0;
		Services.noteService.listModels(new AsyncCallback<List<DataModel>>() {
			public void onFailure(Throwable caught) {
			}

			public void onSuccess(List<DataModel> result) {
				boolean currentDataModelExists = false;
				models.clear();
				for (DataModel dataModel : result) {
					models.put(dataModel.getSeqNum(), dataModel);
					if (dataModel.getSeqNum() == currentDataModel)
						currentDataModelExists = true;
				}
				if (!currentDataModelExists)
					currentDataModel = -1;
				++servicesFinished;
				checkIfNotify();
			}
		});
		Services.noteService.listNotes(new AsyncCallback<List<Note>>() {

			@Override
			public void onSuccess(List<Note> result) {
				labels.clear();
				notes.clear();
				for (Note note : result) {
					Integer key = note.getDataModelSeqNum();
					if (notes.containsKey(key)) {
						notes.get(key).add(note);
					} else {
						List<Note> newList = new LinkedList<Note>();
						newList.add(note);
						notes.put(key, newList);
					}
					if (labels.containsKey(key)) {
						Set<String> set = labels.get(key);
						set.addAll(note.getLabels());
						labels.put(key, set);
					} else {
						Set<String> newSet = new HashSet<String>();
						newSet.addAll(note.getLabels());
						labels.put(key, newSet);
					}
				}
				++servicesFinished;
				checkIfNotify();
			}

			@Override
			public void onFailure(Throwable caught) {
			}
		});
	}

	protected void checkIfNotify() {
		if (servicesFinished == 2) {
			for (DatabaseUpdatedNotifier notifier : notifiers) {
				notifier.databaseUpdated();
			}
		}
	}

	public List<Note> getNotes(int dataSeqNum) {
		if (notes.containsKey(dataSeqNum))
			return notes.get(dataSeqNum);
		else
			return new LinkedList<Note>();
	}

	public Vector<Word9> getWords() {
		return this.words;
	}

	public Set<String> getLabels(int dataModelSeqNum) {
		return labels.get(dataModelSeqNum);
	}

	public Set<String> getLabelsOfCurrentDataModel() {
		if ((currentDataModel == -1) || (!labels.containsKey(currentDataModel)))
			return new HashSet<String>();
		return labels.get(currentDataModel);
	}

	public Word9 getWord(int date) {
		for (Word9 word : words) {
			if (word.getDate() == date)
				return word;
		}
		return null;
	}

	public List<DataModel> getModels() {
		List<DataModel> ret = new LinkedList<DataModel>();
		for (DataModel dataModel : models.values()) {
			ret.add(dataModel);
		}
		return ret;
	}

	public DataModel getModel(int seqNum) {
		return models.get(seqNum);
	}

	private Vector<Word9> words = new Vector<Word9>();

	public final static ListWordsServiceAsync listWordsService = GWT
			.create(ListWordsService.class);

	public int getCurrentDataModelSeqNum() {
		return this.currentDataModel;
	}

	public Collection<String> getLabels() {
		return new LinkedList<String>();
	}
}
