package wordOfTheDay.server.service;

import java.util.Collections;
import java.util.LinkedList;
import java.util.List;
import java.util.Vector;
import java.util.logging.Logger;

import javax.jdo.PersistenceManager;

import wordOfTheDay.client.Word9;
import wordOfTheDay.client.deleteWords.DeleteWordsService;
import wordOfTheDay.client.listWords.ListWordsService;
import wordOfTheDay.server.PMF;
import wordOfTheDay.server.PersistentWord25;

import com.google.gwt.user.server.rpc.RemoteServiceServlet;

/**
 * The server side implementation of the RPC service.
 */
@SuppressWarnings("serial")
public class ListWordsServiceImpl extends RemoteServiceServlet implements
		ListWordsService {

	private static final Logger log = Logger
			.getLogger(ListWordsServiceImpl.class.getName());

	public Vector<Word9> listWords() {
		log.warning("list words start");
		String email = (String) this.getThreadLocalRequest().getSession()
				.getAttribute("email");
		if (email == null)
			email = GetTodaysWordServiceImpl.defaultEmail;
		log.warning("list words get all words");
		List<PersistentWord25> persistentWords = PMF.getAllWords(email);
		log.warning("list words - all words got");
		Vector<Word9> ret = new Vector<Word9>();
		for (PersistentWord25 persistentWord : persistentWords) {
			ret.add(PMF.persistentWordToWordWithPreviousPossible(
					persistentWord, true));
		}
		// ret.get(0).setPreviousDayPossible(false);
		log.warning("words converted");
		// Collections.sort(ret);
		// log.warning("words sorted");
		return ret;
	}

	@Override
	public void removeLabel(String label) {
		// TODO Auto-generated method stub
		String email = (String) this.getThreadLocalRequest().getSession()
				.getAttribute("email");
		PMF.removeLabel(email, label);
	}

	@Override
	public void renameLabel(String oldName, String newName) {
		String email = (String) this.getThreadLocalRequest().getSession()
				.getAttribute("email");
		PMF.replaceLabel(email, oldName, newName);
	}
}
