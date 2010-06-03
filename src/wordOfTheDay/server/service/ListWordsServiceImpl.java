package wordOfTheDay.server.service;

import java.util.Collections;
import java.util.LinkedList;
import java.util.List;
import java.util.Vector;

import javax.jdo.PersistenceManager;

import wordOfTheDay.client.Word4;
import wordOfTheDay.client.deleteWords.DeleteWordsService;
import wordOfTheDay.client.listWords.ListWordsService;
import wordOfTheDay.server.PMF;
import wordOfTheDay.server.PersistentWord11;

import com.google.gwt.user.server.rpc.RemoteServiceServlet;

/**
 * The server side implementation of the RPC service.
 */
@SuppressWarnings("serial")
public class ListWordsServiceImpl extends RemoteServiceServlet implements
		ListWordsService {

	public Vector<Word4> listWords() {
		String email = (String) this.getThreadLocalRequest().getSession()
				.getAttribute("email");
		if (email == null)
			email = "NULL";
		List<PersistentWord11> persistentWords = PMF.getAllWords(email);
		Vector<Word4> ret = new Vector<Word4>();
		for (PersistentWord11 persistentWord : persistentWords) {
			ret.add(PMF.persistentWordToWord(persistentWord));
		}
		Collections.sort(ret);
		return ret;
	}
}
