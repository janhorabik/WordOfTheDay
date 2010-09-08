package wordOfTheDay.client.home;

import wordOfTheDay.client.DayChoice2;
import wordOfTheDay.client.Word6;

import com.google.gwt.user.client.rpc.AsyncCallback;

/**
 * The async counterpart of <code>GetTodaysService</code>.
 */
public interface GetTodaysWordServiceAsync {
	public void getTodaysWord(int date, DayChoice2 dayChoice,
			AsyncCallback<Word6> callback);
}
