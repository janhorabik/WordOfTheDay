package wordOfTheDay.server.service;

import javax.jdo.PersistenceManager;

import wordOfTheDay.client.Word9;
import wordOfTheDay.client.addWord.AddWordService;
import wordOfTheDay.server.Date;
import wordOfTheDay.server.PMF;
import wordOfTheDay.server.PersistentWord24;

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
		PersistentWord24 pWord = new PersistentWord24(word.getName(), word
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
}
