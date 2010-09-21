package wordOfTheDay.client.home;

import wordOfTheDay.client.DayChoice2;
import wordOfTheDay.client.Word7;

import com.google.gwt.user.client.rpc.RemoteService;
import com.google.gwt.user.client.rpc.RemoteServiceRelativePath;

/**
 * The client side stub for the RPC service.
 */
@RemoteServiceRelativePath("getTodaysWordServlet")
public interface GetTodaysWordService extends RemoteService {
	Word7 getTodaysWord(int date, DayChoice2 choice);
}
