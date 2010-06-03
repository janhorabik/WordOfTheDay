package wordOfTheDay.client.addWord;

import wordOfTheDay.client.Word4;

import com.google.gwt.user.client.rpc.AsyncCallback;

/**
 * The async counterpart of <code>AddWordService</code>.
 */
public interface AddWordServiceAsync {
	public void addWord(Word4 word, AsyncCallback<String> callback);
}
