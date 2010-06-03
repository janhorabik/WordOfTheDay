package wordOfTheDay.client.deleteWords;

import com.google.gwt.user.client.rpc.AsyncCallback;

/**
 * The async counterpart of <code>DeleteWordsService</code>.
 */
public interface DeleteWordsServiceAsync {
	public void deleteWords(AsyncCallback<String> callback);
}
