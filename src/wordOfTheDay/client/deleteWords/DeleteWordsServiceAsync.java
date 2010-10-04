package wordOfTheDay.client.deleteWords;

import java.util.List;

import com.google.gwt.user.client.rpc.AsyncCallback;

/**
 * The async counterpart of <code>DeleteWordsService</code>.
 */
public interface DeleteWordsServiceAsync {
	public void deleteWords(AsyncCallback<String> callback);

	void deleteWords(List<String> dates, AsyncCallback<String> callback);
}
