package wordOfTheDay.server.service;

import javax.jdo.PersistenceManager;

import wordOfTheDay.client.Word6;
import wordOfTheDay.client.addWord.AddWordService;
import wordOfTheDay.server.PMF;
import wordOfTheDay.server.PersistentWord18;

import com.google.gwt.user.server.rpc.RemoteServiceServlet;

/**
 * The server side implementation of the RPC service.
 */
@SuppressWarnings("serial")
public class AddWordServiceImpl extends RemoteServiceServlet implements
		AddWordService {

	public String addWord(Word6 word) {
		String email = (String) this.getThreadLocalRequest().getSession()
				.getAttribute("email");
		PersistenceManager pm = PMF.get().getPersistenceManager();
		pm.makePersistent(new PersistentWord18(word.getName(), word
				.getExplanation(), word.getUsage(), PMF
				.getYoungestAvailableDate(email), email));
		return "Word added";
	}
}
