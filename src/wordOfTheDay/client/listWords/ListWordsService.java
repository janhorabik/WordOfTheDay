package wordOfTheDay.client.listWords;

import java.util.Vector;

import wordOfTheDay.client.Word9;

import com.google.gwt.user.client.rpc.RemoteService;
import com.google.gwt.user.client.rpc.RemoteServiceRelativePath;

/**
 * The client side stub for the RPC service.
 */
@RemoteServiceRelativePath("listWordsServlet")
public interface ListWordsService extends RemoteService {
	Vector<Word9> listWords();
}
