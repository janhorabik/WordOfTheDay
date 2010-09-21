package wordOfTheDay.client.addWord;

import wordOfTheDay.client.Word7;

import com.google.gwt.user.client.rpc.AsyncCallback;

/**
 * The async counterpart of <code>AddWordService</code>.
 */
public interface AddWordServiceAsync {
	public void addWord(Word7 word, AsyncCallback<String> callback);
}
