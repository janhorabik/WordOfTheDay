package wordOfTheDay.server.service;

import java.util.List;

import javax.jdo.PersistenceManager;

import wordOfTheDay.client.Word9;
import wordOfTheDay.client.addWord.AddWordService;
import wordOfTheDay.server.Date;
import wordOfTheDay.server.PMF;
import wordOfTheDay.server.PersistentWord25;
import wordOfTheDay.server.WordKey;

import com.google.gwt.user.server.rpc.RemoteServiceServlet;

/**
 * The server side implementation of the RPC service.
 */
@SuppressWarnings("serial")
public class AddWordServiceImpl extends RemoteServiceServlet implements
		AddWordService {

	public String addWord(Word9 word, boolean add) {
		String email = (String) this.getThreadLocalRequest().getSession()
				.getAttribute("email");
		PersistenceManager pm = PMF.get().getPersistenceManager();
		PersistentWord25 pWord = new PersistentWord25(word.getName(), word
				.getExplanation(), word.getUsage(), add ? PMF
				.getYoungestAvailableDate(email) : word.getDate(), email, word
				.getLabels());

		if (!add) {
			// we edit - delete previous word of this date
			PMF.deleteWord(word.getEmail(), Date.getDate(word.getDate()));
		}
		pm.makePersistent(pWord);
		return "Word added";
	}

	@Override
	public void addLabel(int date, String label) {
		PersistenceManager pm = PMF.get().getPersistenceManager();
		String email = (String) this.getThreadLocalRequest().getSession()
				.getAttribute("email");
		WordKey wordKey = new WordKey(date, email);
		PersistentWord25 word = PMF.getWord(wordKey);
		List<String> labels = word.getLabels();
		labels.add(label);
		PersistentWord25 newWord = new PersistentWord25(word.getName(), word
				.getExplanation(), word.getUsage(), date, email, labels);
		PMF.deleteWord(email, Date.getDate(word.getDate()));
		pm.makePersistent(newWord);
	}
}
