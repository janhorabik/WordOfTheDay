package wordOfTheDay.client.listWords;

import java.util.List;
import java.util.Set;

import wordOfTheDay.client.DataModel;
import wordOfTheDay.client.Note;

import com.google.gwt.user.client.rpc.AsyncCallback;

public interface NoteServiceAsync {

	void addModel(DataModel model, AsyncCallback<Integer> callback);

	void addNote(Note note, AsyncCallback<Integer> callback);

	void changeModel(DataModel model, AsyncCallback<Void> callback);

	void changeNote(Note note, AsyncCallback<Void> callback);

	void removeModel(int modelId, AsyncCallback<Void> callback);

	void listModels(AsyncCallback<List<DataModel>> callback);

	void listNotes(AsyncCallback<List<Note>> callback);

	void removeLabel(String label, int dataModelSeqNum,
			AsyncCallback<Void> callback);

	void renameLabel(String oldName, String newName, int dataModelSeqNum,
			AsyncCallback<Void> callback);

	void removeNotes(Set<Note> notes, AsyncCallback<Void> callback);

}
