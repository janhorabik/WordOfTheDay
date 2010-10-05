package wordOfTheDay.server;

import java.util.LinkedList;
import java.util.List;

import javax.jdo.JDOHelper;
import javax.jdo.PersistenceManager;
import javax.jdo.PersistenceManagerFactory;

import com.google.appengine.api.datastore.Query;

import wordOfTheDay.client.DateHelper;
import wordOfTheDay.client.Word7;

public final class PMF {
	private static final PersistenceManagerFactory pmfInstance = JDOHelper
			.getPersistenceManagerFactory("transactions-optional");

	private PMF() {
	}

	public static PersistenceManagerFactory get() {
		return pmfInstance;
	}

	public static Word7 persistentWordToWord(PersistentWord22 persistentWord2) {
		return persistentWordToWordWithPreviousPossible(persistentWord2,
				hasPreviousThen(persistentWord2));
	}

	public static Word7 persistentWordToWordWithPreviousPossible(
			PersistentWord22 persistentWord, boolean previousPossible) {
		System.out.println("convertion of " + persistentWord);
		boolean nextPossible = Date.getCurrentDate() > persistentWord.getDate();
		return new Word7(persistentWord.getName(), persistentWord
				.getExplanation(),
				persistentWord.getUsage() == null ? new LinkedList<String>()
						: new LinkedList<String>(persistentWord.getUsage()),
				persistentWord.getDate(), previousPossible, nextPossible, Date
						.getCurrentDate() == persistentWord.getDate(),
				persistentWord.getEmail(),
				persistentWord.getLabels() == null ? new LinkedList<String>()
						: new LinkedList<String>(persistentWord.getLabels()));
	}

	private static boolean hasPreviousThen(PersistentWord22 word) {
		PersistenceManager pm = pmfInstance.getPersistenceManager();
		String query = "SELECT FROM " + PersistentWord22.class.getName()
				+ " where email == '" + word.getEmail() + "' && date < "
				+ word.getDate();
		List<PersistentWord22> list = (List<PersistentWord22>) pm.newQuery(
				query).execute();
		System.out.println("size: " + list.size());
		return !list.isEmpty();
	}

	public static List<PersistentWord22> getAllWords(String email) {
		email = getSQLString(email);
		PersistenceManager pm = pmfInstance.getPersistenceManager();
		String query = "select from " + PersistentWord22.class.getName()
				+ " where email == " + email + " order by date ";
		System.out.println("query: " + query);
		return (List<PersistentWord22>) pm.newQuery(query).execute();
	}

	public static PersistentWord22 getWord(WordKey wordKey) {

		try {
			PersistenceManager pm = pmfInstance.getPersistenceManager();
			PersistentWord22 word = pm.getObjectById(PersistentWord22.class,
					PersistentWord22.generateKey(wordKey.getEmail(), wordKey
							.getDate()));
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

	public static int getYoungestAvailableDate(String email) {
		email = getSQLString(email);
		PersistenceManager pm = pmfInstance.getPersistenceManager();
		String query = "SELECT FROM " + PersistentWord22.class.getName()
				+ " where email == " + email + " ORDER BY date DESC LIMIT 1";
		List<PersistentWord22> list = (List<PersistentWord22>) pm.newQuery(
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
		String query = "select from " + PersistentWord22.class.getName()
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
		String query = "select from " + PersistentWord22.class.getName()
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

	private static void deleteWord(String email, String date) {
		System.out.println("delete " + date);
		PersistenceManager pmi = PMF.get().getPersistenceManager();
		email = getSQLString(email);
		Integer dateInt = DateHelper.toIntWithoutSpace(date);
		String query = "select FROM " + PersistentWord22.class.getName()
				+ " where email == " + email + " && date == " + dateInt;
		pmi.newQuery(query).deletePersistentAll();
	}
}