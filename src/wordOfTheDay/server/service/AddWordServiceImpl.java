package wordOfTheDay.server.service;

import javax.jdo.PersistenceManager;

import wordOfTheDay.client.Word9;
import wordOfTheDay.client.addWord.AddWordService;
import wordOfTheDay.server.PMF;
import wordOfTheDay.server.PersistentWord24;

import com.google.gwt.user.server.rpc.RemoteServiceServlet;

/**
 * The server side implementation of the RPC service.
 */
@SuppressWarnings("serial")
public class AddWordServiceImpl extends RemoteServiceServlet implements
		AddWordService {

	public String addWord(Word9 word) {
		String email = (String) this.getThreadLocalRequest().getSession()
				.getAttribute("email");
		PersistenceManager pm = PMF.get().getPersistenceManager();
		
		pm.makePersistent(new PersistentWord24(word.getName(), word
				.getExplanation(), word.getUsage(), PMF
				.getYoungestAvailableDate(email), email, word.getLabels()));
		return "Word added";
	}
}
