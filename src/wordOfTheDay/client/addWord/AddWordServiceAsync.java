package wordOfTheDay.client.addWord;

import wordOfTheDay.client.Word6;

import com.google.gwt.user.client.rpc.AsyncCallback;

/**
 * The async counterpart of <code>AddWordService</code>.
 */
public interface AddWordServiceAsync {
	public void addWord(Word6 word, AsyncCallback<String> callback);
}
