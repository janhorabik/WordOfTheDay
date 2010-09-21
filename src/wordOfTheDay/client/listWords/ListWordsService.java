package wordOfTheDay.client.listWords;

import java.util.Vector;

import wordOfTheDay.client.Word7;

import com.google.gwt.user.client.rpc.RemoteService;
import com.google.gwt.user.client.rpc.RemoteServiceRelativePath;

/**
 * The client side stub for the RPC service.
 */
@RemoteServiceRelativePath("listWordsServlet")
public interface ListWordsService extends RemoteService {
	Vector<Word7> listWords();
}
