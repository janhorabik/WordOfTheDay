package wordOfTheDay.client.addWord;

import wordOfTheDay.client.Word5;

import com.google.gwt.user.client.rpc.RemoteService;
import com.google.gwt.user.client.rpc.RemoteServiceRelativePath;

/**
 * The client side stub for the RPC service.
 */
@RemoteServiceRelativePath("addWordServlet")
public interface AddWordService extends RemoteService {
	String addWord(Word5 word);
}
