package wordOfTheDay.server;

import java.util.LinkedList;
import java.util.List;

import javax.jdo.JDOHelper;
import javax.jdo.PersistenceManager;
import javax.jdo.PersistenceManagerFactory;

import wordOfTheDay.client.Word5;

public final class PMF {
	private static final PersistenceManagerFactory pmfInstance = JDOHelper
			.getPersistenceManagerFactory("transactions-optional");

	private PMF() {
	}

	public static PersistenceManagerFactory get() {
		return pmfInstance;
	}

	public static Word5 persistentWordToWord(PersistentWord13 persistentWord2) {
		return persistentWordToWordWithPreviousPossible(persistentWord2,
				hasPreviousThen(persistentWord2.getDate()));
	}

	public static Word5 persistentWordToWordWithPreviousPossible(
			PersistentWord13 persistentWord2, boolean previousPossible) {
		boolean nextPossible = Date.getCurrentDate() > persistentWord2
				.getDate();
		return new Word5(persistentWord2.getName(), persistentWord2
				.getExplanation(),
				persistentWord2.getUsage() == null ? new LinkedList<String>()
						: new LinkedList<String>(persistentWord2.getUsage()),
				persistentWord2.getDate(), previousPossible, nextPossible,
				persistentWord2.getEmail());
	}

	private static boolean hasPreviousThen(int date) {
		PersistenceManager pm = pmfInstance.getPersistenceManager();
		String query = "select from " + PersistentWord13.class.getName()
				+ " where date < " + date;
		return !((List<PersistentWord13>) pm.newQuery(query).execute())
				.isEmpty();
	}

	public static List<PersistentWord13> getAllWords(String email) {
		email = getSQLString(email);
		PersistenceManager pm = pmfInstance.getPersistenceManager();
		String query = "select from " + PersistentWord13.class.getName()
				+ " where email == " + email + " order by date ";
		System.out.println("query: " + query);
		return (List<PersistentWord13>) pm.newQuery(query).execute();
	}

	public static PersistentWord13 getWord(WordKey wordKey) {
		String email = getSQLString(wordKey.getEmail());
		PersistenceManager pm = pmfInstance.getPersistenceManager();
		String query = "select from " + PersistentWord13.class.getName()
				+ " where date == " + wordKey.getDate() + " && email == "
				+ email;
		List<PersistentWord13> list = (List<PersistentWord13>) pm.newQuery(
				query).execute();

		if (list.isEmpty())
			return null;
		return list.get(0);
	}

	public static int getYoungestAvailableDate(String email) {
		email = getSQLString(email);
		PersistenceManager pm = pmfInstance.getPersistenceManager();
		String query = "SELECT FROM " + PersistentWord13.class.getName()
				+ " where email == " + email + " ORDER BY date DESC LIMIT 1";
		List<PersistentWord13> list = (List<PersistentWord13>) pm.newQuery(
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
		String query = "SELECT FROM " + PersistentUser6.class.getName();
		List<PersistentUser6> list = (List<PersistentUser6>) pm.newQuery(query)
				.execute();
		for (PersistentUser6 persistentUser : list) {
			if (persistentUser.getEmail().equals(email))
				return true;
		}
		return false;
	}

	public static void storeUser(String email, String passwordHash) {
		PersistenceManager pm = pmfInstance.getPersistenceManager();
		pm.makePersistent(new PersistentUser6(email, passwordHash));
	}

	public static void changePassword(String email, String newPassword) {
		email = getSQLString(email);
		PersistenceManager pm = pmfInstance.getPersistenceManager();
		String query = "delete FROM " + PersistentUser6.class.getName()
				+ " where email == " + email;
		pm.newQuery(query).execute();
		pm.makePersistent(new PersistentUser6(email, newPassword));
	}

	public static String getHashedPasswordOf(String email) {
		PersistenceManager pm = pmfInstance.getPersistenceManager();
		String query = "select from " + PersistentUser6.class.getName();
		List<PersistentUser6> list = (List<PersistentUser6>) pm.newQuery(query)
				.execute();
		for (PersistentUser6 persistentUser : list) {
			if (persistentUser.getEmail().equals(email))
				return persistentUser.getPasswordHash();
		}
		return null;
	}

	public static void deleteAllWords(String email) {
		PersistenceManager pmi = PMF.get().getPersistenceManager();
		email = getSQLString(email);
		String query = "select from " + PersistentWord13.class.getName()
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