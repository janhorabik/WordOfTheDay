package wordOfTheDay.server.service;

import java.util.LinkedList;
import java.util.logging.Logger;

import wordOfTheDay.client.DayChoice2;
import wordOfTheDay.client.Word4;
import wordOfTheDay.client.home.GetTodaysWordService;
import wordOfTheDay.server.Date;
import wordOfTheDay.server.PMF;
import wordOfTheDay.server.PersistentWord13;
import wordOfTheDay.server.WordKey;
import wordOfTheDay.server.WordsCache;

import com.google.gwt.user.server.rpc.RemoteServiceServlet;

/**
 * The server side implementation of the RPC service.
 */
@SuppressWarnings("serial")
public class GetTodaysWordServiceImpl extends RemoteServiceServlet implements
		GetTodaysWordService {

	// public Word getTodaysWord() {
	// return WordsCache.getInstance().getRandom();
	// }

	private static final Logger log = Logger
			.getLogger(GetTodaysWordServiceImpl.class.getName());

	// for users not logged in
	private static final String defaultEmail = "janhorabik@gmail.com";

	public Word4 getTodaysWord(int date, DayChoice2 dayChoice) {
		log.info("gettingWord entered");
		String email = (String) this.getThreadLocalRequest().getSession()
				.getAttribute("email");
		if (email == null)
			email = defaultEmail;
		if (date == 0)
			date = Date.getCurrentDate();
		switch (dayChoice) {
		case TOMORROW:
			date = Date.getNextDay(date);
			break;
		case YESTERDAY:
			date = Date.getPreviousDay(date);
			break;
		}
		System.out.println("day: " + date);
		log.info("getting Word: getting instance of WordsCache");
		WordKey wordKey = new WordKey(date, email);
		Word4 word = WordsCache.getInstance().get(wordKey);
		log.info("getting Word: instance of WordsCache got - finish");
		if (word != null)
			return word;
		log.info("Word not found - getting from DB");
		PersistentWord13 persistentWord = PMF.getWord(wordKey);
		if (persistentWord != null) {
			word = PMF.persistentWordToWord(persistentWord);
			WordsCache.getInstance().put(wordKey, word);
			log.severe("Word got from DB - finish");
			return word;
		}
		log.info("getting Word: returning empty word");
		return new Word4("", "", new LinkedList<String>(), Date
				.getCurrentDate(), false, false, email);
	}
}
