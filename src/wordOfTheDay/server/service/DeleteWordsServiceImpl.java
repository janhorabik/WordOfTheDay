package wordOfTheDay.server.service;

import wordOfTheDay.client.deleteWords.DeleteWordsService;
import wordOfTheDay.server.PMF;
import wordOfTheDay.server.WordsCache;

import com.google.gwt.user.server.rpc.RemoteServiceServlet;

/**
 * The server side implementation of the RPC service.
 */
@SuppressWarnings("serial")
public class DeleteWordsServiceImpl extends RemoteServiceServlet implements
		DeleteWordsService {

	public String deleteWords() {
		String email = (String) this.getThreadLocalRequest().getSession()
				.getAttribute("email");
		PMF.deleteAllWords(email);
		WordsCache.getInstance().clear();
		return "All words have been deleted";
	}

}
