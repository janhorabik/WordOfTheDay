package wordOfTheDay.client.addWord;

import wordOfTheDay.client.Word5;

import com.google.gwt.user.client.rpc.AsyncCallback;

/**
 * The async counterpart of <code>AddWordService</code>.
 */
public interface AddWordServiceAsync {
	public void addWord(Word5 word, AsyncCallback<String> callback);
}
