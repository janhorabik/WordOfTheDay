package wordOfTheDay.server;

import java.util.LinkedList;
import java.util.List;

import javax.jdo.JDOHelper;
import javax.jdo.PersistenceManager;
import javax.jdo.PersistenceManagerFactory;

import wordOfTheDay.client.Word4;

public final class PMF {
	private static final PersistenceManagerFactory pmfInstance = JDOHelper
			.getPersistenceManagerFactory("transactions-optional");

	private PMF() {
	}

	public static PersistenceManagerFactory get() {
		return pmfInstance;
	}

	public static Word4 persistentWordToWord(PersistentWord11 persistentWord2) {
		boolean nextPossible = Date.getCurrentDate() > persistentWord2
				.getDate();
		boolean previousPossible = hasPreviousThen(persistentWord2.getDate());
		return new Word4(persistentWord2.getName(), persistentWord2
				.getExplanation(),
				persistentWord2.getUsage() == null ? new LinkedList<String>()
						: new LinkedList<String>(persistentWord2.getUsage()),
				persistentWord2.getDate(), previousPossible, nextPossible,
				persistentWord2.getEmail());
	}

	private static boolean hasPreviousThen(int date) {
		PersistenceManager pm = pmfInstance.getPersistenceManager();
		String query = "select from " + PersistentWord11.class.getName()
				+ " where date < " + date;
		return !((List<PersistentWord11>) pm.newQuery(query).execute())
				.isEmpty();
	}

	public static List<PersistentWord11> getAllWords(String email) {
		email = getSQLString(email);
		PersistenceManager pm = pmfInstance.getPersistenceManager();
		String query = "select from " + PersistentWord11.class.getName()
				+ " where email == " + email;
		System.out.println("query: " + query);
		return (List<PersistentWord11>) pm.newQuery(query).execute();
	}

	public static PersistentWord11 getWord(WordKey wordKey) {
		String email = getSQLString(wordKey.getEmail());
		PersistenceManager pm = pmfInstance.getPersistenceManager();
		String query = "select from " + PersistentWord11.class.getName()
				+ " where date == " + wordKey.getDate() + " && email == "
				+ email;
		List<PersistentWord11> list = (List<PersistentWord11>) pm.newQuery(query)
				.execute();

		if (list.isEmpty())
			return null;
		return list.get(0);
	}

	public static int getYoungestAvailableDate(String email) {
		email = getSQLString(email);
		PersistenceManager pm = pmfInstance.getPersistenceManager();
		String query = "SELECT FROM " + PersistentWord11.class.getName()
				+ " where email == " + email + " ORDER BY date DESC LIMIT 1";
		List<PersistentWord11> list = (List<PersistentWord11>) pm.newQuery(query)
				.execute();
		if (list.isEmpty())
			return Date.getCurrentDate();
		else {
			int old = list.get(0).getDate();
			return Date.getNextDay(old);
		}
	}

	public static boolean hasUser(String email) {
		PersistenceManager pm = pmfInstance.getPersistenceManager();
		String query = "SELECT FROM " + PersistentUser5.class.getName();
		List<PersistentUser5> list = (List<PersistentUser5>) pm.newQuery(query)
				.execute();
		for (PersistentUser5 persistentUser : list) {
			if (persistentUser.getEmail().equals(email))
				return true;
		}
		return false;
	}

	public static void storeUser(String email, String passwordHash) {
		PersistenceManager pm = pmfInstance.getPersistenceManager();
		pm.makePersistent(new PersistentUser5(email, passwordHash));
	}

	public static void changePassword(String email, String newPassword) {
		email = getSQLString(email);
		PersistenceManager pm = pmfInstance.getPersistenceManager();
		String query = "delete FROM " + PersistentUser5.class.getName()
				+ " where email == " + email;
		pm.newQuery(query).execute();
		pm.makePersistent(new PersistentUser5(email, newPassword));
	}

	public static String getHashedPasswordOf(String email) {
		PersistenceManager pm = pmfInstance.getPersistenceManager();
		String query = "select from " + PersistentUser5.class.getName();
		List<PersistentUser5> list = (List<PersistentUser5>) pm.newQuery(query)
				.execute();
		for (PersistentUser5 persistentUser : list) {
			if (persistentUser.getEmail().equals(email))
				return persistentUser.getPasswordHash();
		}
		return null;
	}

	public static void deleteAllWords(String email) {
		PersistenceManager pmi = PMF.get().getPersistenceManager();
		email = getSQLString(email);
		String query = "select from " + PersistentWord11.class.getName()
				+ " where email == " + email;
		pmi.newQuery(query).deletePersistentAll();
	}

	private static String getSQLString(String value) {
		if (value == null)
			return "NULL";
		else
			return value = "'" + value + "'";
	}
}