package wordOfTheDay.server.service;

import java.util.List;

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

	public String deleteWords(List<String> dates) {
		System.out.println("delete words: " + dates);
		String email = (String) this.getThreadLocalRequest().getSession()
				.getAttribute("email");
		PMF.deleteWords(email, dates);
		WordsCache.getInstance().clear();
		return "Words have been deleted";
	}

}
