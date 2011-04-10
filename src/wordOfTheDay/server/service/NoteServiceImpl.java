package wordOfTheDay.server.service;

import java.util.LinkedList;
import java.util.List;
import java.util.Set;

import javax.jdo.PersistenceManager;

import org.apache.james.mime4j.io.EOLConvertingInputStream;

import wordOfTheDay.client.DataModel;
import wordOfTheDay.client.Note;
import wordOfTheDay.client.dashboard.NoteService;
import wordOfTheDay.server.PMF;
import wordOfTheDay.server.PersistentDataModel;
import wordOfTheDay.server.PersistentNote;
import wordOfTheDay.server.PersistentWord25;

import com.google.gwt.user.server.rpc.RemoteServiceServlet;

/**
 * The server side implementation of the RPC service.
 */
@SuppressWarnings("serial")
public class NoteServiceImpl extends RemoteServiceServlet implements
		NoteService {

	@Override
	public Integer addModel(DataModel model) {
		String email = (String) this.getThreadLocalRequest().getSession()
				.getAttribute("email");
		int seqNum = PMF.getYoungestAvailableModelSeqNum(email);
		PersistentDataModel persistentModel = new PersistentDataModel(email,
				seqNum, model.getName(), model.getFields());
		PersistenceManager pm = PMF.get().getPersistenceManager();
		pm.makePersistent(persistentModel);
		System.out.println("Model " + persistentModel + " was saved. asked: "
				+ model);
		return seqNum;
	}

	@Override
	public void changeModel(DataModel model) {
		String email = (String) this.getThreadLocalRequest().getSession()
				.getAttribute("email");
		int modelId = model.getSeqNum();
		PersistenceManager pmi = PMF.get().getPersistenceManager();
		PersistentDataModel persistentModel = pmi.getObjectById(
				PersistentDataModel.class,
				PersistentDataModel.generateKey(email, modelId));
		persistentModel.setName(model.getName());
		// todo set fields
		pmi.close();
	}

	@Override
	public void removeModel(int modelId) {
		String email = (String) this.getThreadLocalRequest().getSession()
				.getAttribute("email");
		String query = "select FROM " + PersistentDataModel.class.getName()
				+ " where email == " + email + " && seqNum == " + modelId;
		PersistenceManager pmi = PMF.get().getPersistenceManager();
		pmi.newQuery(query).deletePersistentAll();
		query = "select FROM " + PersistentNote.class.getName()
				+ " where email == " + email + " && dataModelSeqNum == "
				+ modelId;
		pmi.newQuery(query).deletePersistentAll();
	}

	@Override
	public Integer addNote(Note note) {
		String email = (String) this.getThreadLocalRequest().getSession()
				.getAttribute("email");
		int seqNum = PMF.getYoungestAvailableNoteSeqNum(email);
		PersistentNote persistentNote = new PersistentNote(email, seqNum,
				note.getFields(), note.getDataModelSeqNum(), note.getLabels());
		PersistenceManager pm = PMF.get().getPersistenceManager();
		pm.makePersistent(persistentNote);
		System.out.println("Note" + persistentNote + " was saved. Asked: "
				+ note);
		return seqNum;
	}

	@Override
	public void changeNote(Note note) {
		String email = (String) this.getThreadLocalRequest().getSession()
				.getAttribute("email");
		PMF.changeNote(email, note);
	}

	@Override
	public List<DataModel> listModels() {
		String email = (String) this.getThreadLocalRequest().getSession()
				.getAttribute("email");
		List<PersistentDataModel> persistentModels = PMF.getAllModels(email);
		List<DataModel> ret = new LinkedList<DataModel>();
		for (PersistentDataModel persistentDataModel : persistentModels) {
			ret.add(PMF.persistentModelToModel(persistentDataModel));
		}
		return ret;
	}

	@Override
	public List<Note> listNotes() {
		String email = (String) this.getThreadLocalRequest().getSession()
				.getAttribute("email");
		List<PersistentNote> persistentNotes = PMF.getAllNotes(email);
		List<Note> ret = new LinkedList<Note>();
		for (PersistentNote persistentNote : persistentNotes) {
			ret.add(PMF.persistentNoteToNote(persistentNote));
		}
		return ret;
	}

	@Override
	public void removeLabel(String label, int dataModelSeqNum) {
		String email = (String) this.getThreadLocalRequest().getSession()
				.getAttribute("email");
		PMF.removeLabel(email, label, dataModelSeqNum);
	}

	@Override
	public void renameLabel(String oldName, String newName, int dataModelSeqNum) {
		String email = (String) this.getThreadLocalRequest().getSession()
				.getAttribute("email");
		PMF.replaceLabel(email, oldName, newName, dataModelSeqNum);
	}

	@Override
	public void removeNotes(Set<Note> notes) {
		if (notes.isEmpty()) {
			return;
		}
		String email = (String) this.getThreadLocalRequest().getSession()
				.getAttribute("email");
		for (Note note : notes) {
			String query = "select FROM " + PersistentNote.class.getName()
					+ " where email == " + email + " && seqNum == "
					+ note.getSeqNum() + " && dataModelSeqNum == "
					+ note.getDataModelSeqNum();
			PersistenceManager pmi = PMF.get().getPersistenceManager();
			pmi.newQuery(query).deletePersistentAll();
		}
	}
}
