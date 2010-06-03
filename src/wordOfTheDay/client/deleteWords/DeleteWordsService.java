package wordOfTheDay.client.deleteWords;

import com.google.gwt.user.client.rpc.RemoteService;
import com.google.gwt.user.client.rpc.RemoteServiceRelativePath;

/**
 * The client side stub for the RPC service.
 */
@RemoteServiceRelativePath("deleteWordsServlet")
public interface DeleteWordsService extends RemoteService {
	String deleteWords();
}
