package wordOfTheDay.client.addWord;

import wordOfTheDay.client.Word6;

import com.google.gwt.user.client.rpc.RemoteService;
import com.google.gwt.user.client.rpc.RemoteServiceRelativePath;

/**
 * The client side stub for the RPC service.
 */
@RemoteServiceRelativePath("addWordServlet")
public interface AddWordService extends RemoteService {
	String addWord(Word6 word);
}
