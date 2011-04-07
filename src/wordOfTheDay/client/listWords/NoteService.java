package wordOfTheDay.client.listWords;

import java.util.List;
import java.util.Set;

import wordOfTheDay.client.DataModel;
import wordOfTheDay.client.Note;
import wordOfTheDay.server.PMF;

import com.google.gwt.user.client.rpc.RemoteService;
import com.google.gwt.user.client.rpc.RemoteServiceRelativePath;

/**
 * The client side stub for the RPC service.
 */
@RemoteServiceRelativePath("noteServlet")
public interface NoteService extends RemoteService {
	Integer addModel(DataModel model);

	void removeModel(int modelId);

	void changeModel(DataModel model);

	Integer addNote(Note note);

	void removeNotes(Set<Note> notes);

	void changeNote(Note note);

	List<DataModel> listModels();

	List<Note> listNotes();
	
	void removeLabel(String label, int dataModelSeqNum); 
	
	void renameLabel(String oldName, String newName, int dataModelSeqNum);
}
