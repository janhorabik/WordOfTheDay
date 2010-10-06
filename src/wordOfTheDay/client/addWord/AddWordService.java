package wordOfTheDay.client.addWord;

import wordOfTheDay.client.Word8;

import com.google.gwt.user.client.rpc.RemoteService;
import com.google.gwt.user.client.rpc.RemoteServiceRelativePath;

/**
 * The client side stub for the RPC service.
 */
@RemoteServiceRelativePath("addWordServlet")
public interface AddWordService extends RemoteService {
	String addWord(Word8 word);
}
