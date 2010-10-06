package wordOfTheDay.server.service;

import java.util.LinkedList;
import java.util.logging.Logger;

import wordOfTheDay.client.DayChoice2;
import wordOfTheDay.client.Word8;
import wordOfTheDay.client.home.GetTodaysWordService;
import wordOfTheDay.server.Date;
import wordOfTheDay.server.PMF;
import wordOfTheDay.server.PersistentWord22;
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
	public static final String defaultEmail = "janhorabik@gmail.com";

	public Word8 getTodaysWord(int date, DayChoice2 dayChoice) {
		log.warning("gettingWord entered");
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
		log.warning("getting Word: getting instance of WordsCache");
		WordKey wordKey = new WordKey(date, email);
		Word8 word = WordsCache.getInstance().get(wordKey);
		log.warning("getting Word: instance of WordsCache got - finish");
		if (word != null) {
			log.warning("word got from cache: " + word.getDate()
					+ word.getEmail() + word.getName());
			return word;
		}
		log.warning("Word not found - getting from DB");
		PersistentWord22 persistentWord = PMF.getWord(wordKey);
		if (persistentWord != null) {
			log.severe("Word got from DB - converting start");
			word = PMF.persistentWordToWord(persistentWord);
			WordsCache.getInstance().put(wordKey, word);
			log.severe("converting word finished");
			return word;
		}
		log.warning("getting Word: returning empty word");
		return new Word8("", "", new LinkedList<String>(), Date
				.getCurrentDate(), false, false, false, email, null);
	}
}
