package wordOfTheDay.server;

import java.util.LinkedList;
import java.util.List;

import javax.jdo.JDOHelper;
import javax.jdo.PersistenceManager;
import javax.jdo.PersistenceManagerFactory;

import com.google.appengine.api.datastore.Query;

import wordOfTheDay.client.DataModel;
import wordOfTheDay.client.DateHelper;
import wordOfTheDay.client.Note;
import wordOfTheDay.client.Word9;

public final class PMF {
	private static final PersistenceManagerFactory pmfInstance = JDOHelper
			.getPersistenceManagerFactory("transactions-optional");

	private PMF() {
	}

	public static PersistenceManagerFactory get() {
		return pmfInstance;
	}

	public static Word9 persistentWordToWord(PersistentWord25 persistentWord2) {
		return persistentWordToWordWithPreviousPossible(persistentWord2,
				hasPreviousThen(persistentWord2));
	}

	public static DataModel persistentModelToModel(
			PersistentDataModel persistentModel) {
		return new DataModel(persistentModel.getEmail(),
				persistentModel.getSeqNum(), persistentModel.getName(),
				persistentModel.getFields());
	}

	public static Word9 persistentWordToWordWithPreviousPossible(
			PersistentWord25 persistentWord, boolean previousPossible) {
		System.out.println("convertion of " + persistentWord);
		boolean nextPossible = Date.getCurrentDate() > persistentWord.getDate();
		return new Word9(persistentWord.getName(),
				persistentWord.getExplanation(),
				persistentWord.getUsage() == null ? new LinkedList<String>()
						: new LinkedList<String>(persistentWord.getUsage()),
				persistentWord.getDate(), previousPossible, nextPossible,
				Date.getCurrentDate() == persistentWord.getDate(),
				persistentWord.getEmail(),
				persistentWord.getLabels() == null ? new LinkedList<String>()
						: new LinkedList<String>(persistentWord.getLabels()));
	}

	private static boolean hasPreviousThen(PersistentWord25 word) {
		PersistenceManager pm = pmfInstance.getPersistenceManager();
		String query = "SELECT FROM " + PersistentWord25.class.getName()
				+ " where email == '" + word.getEmail() + "' && date < "
				+ word.getDate();
		List<PersistentWord25> list = (List<PersistentWord25>) pm.newQuery(
				query).execute();
		System.out.println("size: " + list.size());
		return !list.isEmpty();
	}

	public static List<PersistentWord25> getAllWords(String email) {
		email = getSQLString(email);
		PersistenceManager pm = pmfInstance.getPersistenceManager();

		String query = "select from " + PersistentWord25.class.getName()
				+ " where email == " + email + " order by date ";
		System.out.println("query: " + query);
		return (List<PersistentWord25>) pm.newQuery(query).execute();
	}

	public static PersistentWord25 getWord(WordKey wordKey) {

		try {
			PersistenceManager pm = pmfInstance.getPersistenceManager();
			PersistentWord25 word = pm.getObjectById(
					PersistentWord25.class,
					PersistentWord25.generateKey(wordKey.getEmail(),
							wordKey.getDate()));
			System.out.println(word + " got from db");
			return word;
		} catch (Exception e) {
			return null;
		}
		// String email = getSQLString(wordKey.getEmail());
		// PersistenceManager pm = pmfInstance.getPersistenceManager();
		// String query = "select from " + PersistentWord13.class.getName()
		// + " where date == " + wordKey.getDate() + " && email == "
		// + email;
		// List<PersistentWord13> list = (List<PersistentWord13>) pm.newQuery(
		// query).execute();
		//
		// if (list.isEmpty())
		// return null;
		// return list.get(0);
	}

	public static int getYoungestAvailableModelSeqNum(String email) {
		email = getSQLString(email);
		PersistenceManager pm = pmfInstance.getPersistenceManager();
		String query = "SELECT FROM " + PersistentDataModel.class.getName()
				+ " where email == " + email + " ORDER BY seqNum DESC LIMIT 1";
		List<PersistentDataModel> list = (List<PersistentDataModel>) pm
				.newQuery(query).execute();
		if (list.isEmpty())
			return 0;
		else {
			int old = list.get(0).getSeqNum();
			return old + 1;
		}
	}

	public static int getYoungestAvailableNoteSeqNum(String email) {
		email = getSQLString(email);
		PersistenceManager pm = pmfInstance.getPersistenceManager();
		String query = "SELECT FROM " + PersistentNote.class.getName()
				+ " where email == " + email + " ORDER BY seqNum DESC LIMIT 1";
		List<PersistentNote> list = (List<PersistentNote>) pm.newQuery(query)
				.execute();
		if (list.isEmpty())
			return 0;
		else {
			int old = list.get(0).getSeqNum();
			return old + 1;
		}
	}

	public static int getYoungestAvailableDate(String email) {
		email = getSQLString(email);
		PersistenceManager pm = pmfInstance.getPersistenceManager();
		String query = "SELECT FROM " + PersistentWord25.class.getName()
				+ " where email == " + email + " ORDER BY date DESC LIMIT 1";
		List<PersistentWord25> list = (List<PersistentWord25>) pm.newQuery(
				query).execute();
		if (list.isEmpty())
			return Date.getCurrentDate();
		else {
			int old = list.get(0).getDate();
			return Date.getNextDay(old);
		}
	}

	public static boolean hasUser(String email) {
		PersistenceManager pm = pmfInstance.getPersistenceManager();
		String query = "SELECT FROM " + PersistentUser7.class.getName();
		List<PersistentUser7> list = (List<PersistentUser7>) pm.newQuery(query)
				.execute();
		for (PersistentUser7 persistentUser : list) {
			if (persistentUser.getEmail().equals(email))
				return true;
		}
		return false;
	}

	public static void storeUser(String email, String passwordHash) {
		PersistenceManager pm = pmfInstance.getPersistenceManager();
		pm.makePersistent(new PersistentUser7(email, passwordHash));
	}

	public static void changePassword(String email, String newPassword) {
		email = getSQLString(email);
		PersistenceManager pm = pmfInstance.getPersistenceManager();
		String query = "delete FROM " + PersistentUser7.class.getName()
				+ " where email == " + email;
		pm.newQuery(query).execute();
		pm.makePersistent(new PersistentUser7(email, newPassword));
	}

	public static String getHashedPasswordOf(String email) {
		try {
			PersistenceManager pm = pmfInstance.getPersistenceManager();
			PersistentUser7 user = pm.getObjectById(PersistentUser7.class,
					email);
			System.out.println(user + " got from db");
			if (user != null)
				return user.getPasswordHash();
			return null;
		} catch (Exception e) {
			return null;
		}
	}

	public static void deleteAllWords(String email) {
		PersistenceManager pmi = PMF.get().getPersistenceManager();
		email = getSQLString(email);
		String query = "select from " + PersistentWord25.class.getName()
				+ " where email == " + email;
		pmi.newQuery(query).deletePersistentAll();
	}

	private static String getSQLString(String value) {
		if (value == null)
			return "NULL";
		else
			return value = "'" + value + "'";
	}

	// in (20100908, 20100919) does not work
	public static void fastDeleteWords(String email, List<String> dates) {
		List<Integer> intDates = DateHelper.toIntWithoutSpace(dates);
		String query = "select from " + PersistentWord25.class.getName()
				+ " where email == " + getSQLString(email) + " && date in (";
		for (Integer integer : intDates) {
			query += "'" + integer + "', ";
		}
		query = query.substring(0, query.length() - 2);
		query += ")";
		PersistenceManager pmi = PMF.get().getPersistenceManager();
		System.out.println("runnig query: delete: " + query);
		pmi.newQuery(query).deletePersistentAll();
	}

	public static void deleteWords(String email, List<String> dates) {
		System.out.println("delete words " + dates);
		for (String date : dates) {
			deleteWord(email, date);
		}
	}

	public static void deleteWord(String email, String date) {
		System.out.println("delete " + date);
		PersistenceManager pmi = PMF.get().getPersistenceManager();
		email = getSQLString(email);
		Integer dateInt = DateHelper.toIntWithoutSpace(date);
		String query = "select FROM " + PersistentWord25.class.getName()
				+ " where email == " + email + " && date == " + dateInt;
		pmi.newQuery(query).deletePersistentAll();
	}

	private static List<PersistentWord25> getWordsWithLabel(String email,
			String label) {
		List<PersistentWord25> allWords = getAllWords(email);
		List<PersistentWord25> ret = new LinkedList<PersistentWord25>();
		for (PersistentWord25 word : allWords) {
			if (word.getLabels().contains(label))
				ret.add(word);
		}
		return ret;
	}

	public static void replaceLabel(String email, String oldName, String newName) {
		removeOrReplaceLabel(email, oldName, newName, true);
	}

	private static void removeOrReplaceLabel(String email, String label,
			String newLabel, boolean replace) {
		List<PersistentWord25> wordsToDel = getWordsWithLabel(email, label);
		PersistenceManager pmi = PMF.get().getPersistenceManager();
		for (PersistentWord25 word : wordsToDel) {
			PersistentWord25 wordToChange = pmi.getObjectById(
					PersistentWord25.class,
					PersistentWord25.generateKey(word.getEmail(),
							word.getDate()));
			List<String> labels = word.getLabels();
			int index = labels.indexOf(label);
			labels.remove(index);
			if (replace)
				labels.add(index, newLabel);
			wordToChange.setLabels(labels);
		}
		pmi.close();
	}

	public static void removeLabel(String email, String label) {
		removeOrReplaceLabel(email, label, "", false);
	}

	public static void changeNote(String email, Note note) {
		PersistenceManager pmi = pmfInstance.getPersistenceManager();
		PersistentNote noteToChange = pmi.getObjectById(PersistentNote.class,
				PersistentNote.generateKey(email, note.getSeqNum()));
		noteToChange.setLabels(note.getLabels());
		noteToChange.setFields(note.getFields());
		pmi.close();
	}

	public static List<PersistentDataModel> getAllModels(String email) {
		email = getSQLString(email);
		PersistenceManager pm = pmfInstance.getPersistenceManager();

		String query = "select from " + PersistentDataModel.class.getName()
				+ " where email == " + email + " order by name asc";
		System.out.println("query: " + query);
		List<PersistentDataModel> list = (List<PersistentDataModel>) pm
				.newQuery(query).execute();
		System.out.println(" return " + list.size());
		return list;

	}

	public static List<PersistentNote> getAllNotes(String email) {
		email = getSQLString(email);
		PersistenceManager pm = pmfInstance.getPersistenceManager();

		String query = "select from " + PersistentNote.class.getName()
				+ " where email == " + email;
		System.out.println("query: " + query);
		List<PersistentNote> list = (List<PersistentNote>) pm.newQuery(query)
				.execute();
		System.out.println(" return " + list.size() + " notes.");
		return list;
	}

	public static Note persistentNoteToNote(PersistentNote persistentNote) {
		return new Note(persistentNote.getEmail(), persistentNote.getSeqNum(),
				persistentNote.getFields(),
				persistentNote.getDataModelSeqNum(), persistentNote.getLabels());
	}

	public static void removeLabel(String email, String label,
			int dataModelSeqNum) {
		removeOrReplaceLabel(email, label, "", false, dataModelSeqNum);
	}

	public static void replaceLabel(String email, String oldName,
			String newName, int dataModelSeqNum) {
		removeOrReplaceLabel(email, oldName, newName, true, dataModelSeqNum);
	}

	private static void removeOrReplaceLabel(String email, String oldLabel,
			String newLabel, boolean replace, int dataModelSeqNum) {
		List<PersistentNote> wordsToDel = getNotesWithLabel(email, oldLabel);
		PersistenceManager pmi = PMF.get().getPersistenceManager();
		for (PersistentNote note : wordsToDel) {
			PersistentNote noteToChange = pmi.getObjectById(
					PersistentNote.class,
					PersistentNote.generateKey(note.getEmail(),
							note.getSeqNum()));
			List<String> labels = note.getLabels();
			noteToChange.setLabels(changeLabels(labels, oldLabel, newLabel,
					replace));
		}
		pmi.close();
	}

	private static List<String> changeLabels(List<String> labels,
			String oldLabel, String newLabel, boolean replace) {
		List<String> newLabels = new LinkedList<String>();
		for (String currentLabel : labels) {
			if (currentLabel.startsWith(oldLabel)) {
				if (replace) {
					newLabels.add(newLabel
							+ currentLabel.substring(oldLabel.length(),
									currentLabel.length()));
				}
			} else
				newLabels.add(currentLabel);
		}
		return newLabels;
	}

	private static List<PersistentNote> getNotesWithLabel(String email,
			String label) {
		List<PersistentNote> allNotes = getAllNotes(email);
		List<PersistentNote> ret = new LinkedList<PersistentNote>();
		for (PersistentNote note : allNotes) {
			for (String labelOfNote : note.getLabels()) {
				if (labelOfNote.startsWith(label)) {
					ret.add(note);
					break;
				}
			}
		}
		return ret;
	}
}