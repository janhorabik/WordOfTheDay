package wordOfTheDay.server.service;

import java.io.IOException;
import java.util.logging.Logger;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import wordOfTheDay.client.Word9;
import wordOfTheDay.server.Date;
import wordOfTheDay.server.PMF;
import wordOfTheDay.server.PersistentWord24;
import wordOfTheDay.server.WordKey;
import wordOfTheDay.server.WordsCache;

public class CronService extends HttpServlet {
	private static final Logger log = Logger.getLogger(CronService.class
			.getName());

	public void doPost(HttpServletRequest req, HttpServletResponse res)
			throws ServletException, IOException {
		log.info("Cron servlet called");
		log.warning("gettingWord entered");
		String email = GetTodaysWordServiceImpl.defaultEmail;
		int date = Date.getCurrentDate();
		System.out.println("day: " + date);
		log.warning("getting Word: getting instance of WordsCache");
		WordKey wordKey = new WordKey(date, email);
		Word9 word = WordsCache.getInstance().get(wordKey);
		log.warning("getting Word: instance of WordsCache got - finish");
		if (word != null) {
			log.warning("word got from cache: " + word.getDate()
					+ word.getEmail() + word.getName());
		}
		log.warning("Word not found - getting from DB");
		PersistentWord24 persistentWord = PMF.getWord(wordKey);
		if (persistentWord != null) {
			log.severe("Word got from DB - converting start");
			word = PMF.persistentWordToWord(persistentWord);
			WordsCache.getInstance().put(wordKey, word);
			log.severe("converting word finished");
		}
		log.warning("getting Word: returning empty word");
		res.getOutputStream().print("ok");
	}

	public void doGet(HttpServletRequest req, HttpServletResponse res)
			throws ServletException, IOException {
		doPost(req, res);
	}

}