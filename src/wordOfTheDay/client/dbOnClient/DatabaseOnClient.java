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
import wordOfTheDay.client.dashboard.Dashboard;
import wordOfTheDay.client.dashboard.ListWordsWithAdvancedTable;
import wordOfTheDay.client.dashboard.labelsTree.LabelsTree;
import wordOfTheDay.client.dashboard.modelsList.ModelsList;
import wordOfTheDay.client.dashboard.notesTable.BeginSearcher;
import wordOfTheDay.client.dashboard.notesTable.DataFilter;
import wordOfTheDay.client.dashboard.notesTable.LabelBeginFilter;
import wordOfTheDay.client.dashboard.notesTable.NotesTable;

import com.google.gwt.core.client.GWT;
import com.google.gwt.user.client.Window;
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

	private ListWordsWithAdvancedTable listWordsWithAdvancedTable;

	public DatabaseOnClient() {
		// update();
	}

	public void setDashboard(Dashboard dashboard) {
		this.dashboard = dashboard;
	}

	public void setListWordsWithAdvancedTable(ListWordsWithAdvancedTable table) {
		this.listWordsWithAdvancedTable = table;
	}

	public void setNotesTable(NotesTable notesTableArg) {
		this.notesTable = notesTableArg;
	}

	public void setLabelsTree(LabelsTree labelsTreeArg) {
		this.labelsTree = labelsTreeArg;
	}

	public void setModelsList(ModelsList modelsListArg) {
		this.modelsList = modelsListArg;
	}

	public void setCurrentDataModel(int num) {
		currentDataModel = num;
		notesTable.drawTable();
		labelsTree.update();
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

			public void onSuccess(List<Note> result) {
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
				}
				++servicesFinished;
				checkIfNotify();
			}

			public void onFailure(Throwable caught) {
			}
		});
	}

	private void rebuildLabels() {
		labels.clear();
		for (Integer modelSeqNum : models.keySet()) {
			Set<String> newSet = new HashSet<String>();
			labels.put(modelSeqNum, newSet);
		}
		for (List<Note> notesSet : notes.values())
			for (Note note : notesSet) {
				int key = note.getDataModelSeqNum();
				if (!labels.containsKey(key)) {
					System.out.println("Wrong note: " + note
							+ " its model seqNum is not in " + models);

				} else {
					Set<String> set = labels.get(key);
					set.addAll(note.getLabels());
					labels.put(key, set);
				}
			}
	}

	protected void checkIfNotify() {
		if (servicesFinished == 2) {
			for (DatabaseUpdatedNotifier notifier : notifiers) {
				rebuildLabels();
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

	private LabelsTree labelsTree;

	private NotesTable notesTable;

	private ModelsList modelsList;

	private Dashboard dashboard;

	public int getCurrentDataModelSeqNum() {
		return this.currentDataModel;
	}

	public Collection<String> getLabels() {
		return new LinkedList<String>();
	}

	public void newNoteWasAdded(Note note) {
		if (notes.get(note.getDataModelSeqNum()) == null) {
			Window.alert("Note: " + note + " wrong datamodelseqNum");
		} else {
			notes.get(note.getDataModelSeqNum()).add(note);
			labels.get(note.getDataModelSeqNum()).addAll(note.getLabels());
			labelsTree.newNoteWasAdded(note);
			notesTable.newNoteWasAdded(note);
		}
	}

	public void newModelWasAdded(DataModel model) {
		models.put(model.getSeqNum(), model);
		notes.put(model.getSeqNum(), new LinkedList<Note>());
		labels.put(model.getSeqNum(), new HashSet<String>());
		modelsList.newModelWasAdded(model);
		listWordsWithAdvancedTable.actNumModels(models.size());
	}

	public void labelWasRemoved(String label) {
		for (Set<String> set : labels.values()) {
			Set<String> toRemove = new HashSet<String>();
			for (String labelToCheck : set) {
				if (BeginSearcher.lookFor(label.split(":"), labelToCheck))
					toRemove.add(labelToCheck);
			}
			set.removeAll(toRemove);
		}
		for (List<Note> notesList : notes.values())
			for (Note note : notesList) {
				Set<String> toRemove = new HashSet<String>();
				for (String labelToCheck : note.getLabels()) {
					if (BeginSearcher.lookFor(label.split(":"), labelToCheck))
						toRemove.add(labelToCheck);
				}
				note.getLabels().removeAll(toRemove);
			}
		labelsTree.update();
		notesTable.databaseChanged();
	}

	public void labelWasRenamed(String label, String newLabel) {
		for (Set<String> set : labels.values()) {
			Set<String> toRemove = new HashSet<String>();
			Set<String> toAdd = new HashSet<String>();
			for (String labelToCheck : set) {
				if (BeginSearcher.lookFor(label.split(":"), labelToCheck)) {
					toRemove.add(labelToCheck);
					toAdd.add(newLabel
							+ labelToCheck.substring(label.length(),
									labelToCheck.length()));
				}
			}
			set.removeAll(toRemove);
			set.addAll(toAdd);
		}
		for (List<Note> notesList : notes.values())
			for (Note note : notesList) {
				Set<String> toRemove = new HashSet<String>();
				Set<String> toAdd = new HashSet<String>();
				for (String labelToCheck : note.getLabels()) {
					if (BeginSearcher.lookFor(label.split(":"), labelToCheck)) {
						toRemove.add(labelToCheck);
						toAdd.add(newLabel
								+ labelToCheck.substring(label.length(),
										labelToCheck.length()));
					}
				}
				note.getLabels().removeAll(toRemove);
				note.getLabels().addAll(toAdd);
			}
		labelsTree.update();
		notesTable.databaseChanged();
	}

	public void modelWasRemoved(int seqNum) {
		models.remove(seqNum);
		notes.remove(seqNum);
		labels.remove(seqNum);
		modelsList.modelWasRemoved(seqNum);
		if (currentDataModel == seqNum) {
			currentDataModel = -1;
			labelsTree.update();
			notesTable.drawTable();
		}
		listWordsWithAdvancedTable.actNumModels(models.size());
	}

	public void modelWasRenamed(int seqNum, String newName) {
		models.get(seqNum).setName(newName);
		modelsList.modelWasRenamed(seqNum, newName);
	}

	public void notesWereRemoved(Set<Note> set) {
		if (set.isEmpty())
			return;
		int dataModelSeqNum = set.iterator().next().getDataModelSeqNum();
		notes.get(dataModelSeqNum).removeAll(set);
		rebuildLabels();
		labelsTree.notesWereRemoved(set);
		notesTable.notesWereRemoved(set);
	}

	public void noteWasChanged(Note note) {

		for (Note n : notes.get(note.getDataModelSeqNum())) {
			if (n.getSeqNum() == note.getSeqNum()) {
				n.setFields(note.getFields());
				n.setLabels(note.getLabels());
				break;
			}
		}
		rebuildLabels();
		labelsTree.update();
		// notesTable.databaseChanged();
	}

	public void setCurrentLabel(String longName) {
		DataFilter filter = new LabelBeginFilter(longName);
		notesTable.applyFilter(filter);
	}

	public void initiate() {
		dashboard.initiate();
		update();
	}
}
