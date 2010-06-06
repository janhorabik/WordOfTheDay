package wordOfTheDay.client.listWords;

import java.util.Vector;

import wordOfTheDay.client.Word5;

import com.google.gwt.user.client.rpc.RemoteService;
import com.google.gwt.user.client.rpc.RemoteServiceRelativePath;

/**
 * The client side stub for the RPC service.
 */
@RemoteServiceRelativePath("listWordsServlet")
public interface ListWordsService extends RemoteService {
	Vector<Word5> listWords();
}
