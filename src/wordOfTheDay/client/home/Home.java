package wordOfTheDay.client.home;

import wordOfTheDay.client.DateHelper;
import wordOfTheDay.client.DayChoice2;
import wordOfTheDay.client.Word6;

import com.google.gwt.core.client.GWT;
import com.google.gwt.user.client.Window;
import com.google.gwt.user.client.rpc.AsyncCallback;
import com.google.gwt.user.client.ui.HTML;
import com.google.gwt.user.client.ui.RootPanel;

/**
 * Entry point classes define <code>onModuleLoad()</code>.
 */
public class Home {

	private final GetTodaysWordServiceAsync getTodaysService = GWT
			.create(GetTodaysWordService.class);

	private final RootPanel wordPanel;

	public Home(final RootPanel wordPanelArg) {
		wordPanel = wordPanelArg;
	}

	public static DayChoice2 dayChoiceFromParam(String s) {
		System.out.println("day choice from param: " + s);
		if (s == null || s.equals(""))
			return DayChoice2.TODAY;
		if (s.equals("t"))
			return DayChoice2.TODAY;
		if (s.equals("n"))
			return DayChoice2.TOMORROW;
		if (s.equals("p"))
			return DayChoice2.YESTERDAY;
		return DayChoice2.TODAY;
	}

	private int dateFromParam(String p) {
		if (p == null || p.equals(""))
			return 0;
		return Integer.parseInt(p);
	}

	public void initiate() {
		wordPanel.clear();
		wordPanel.add(new HTML("Connecting..."));
		final int date = dateFromParam(Window.Location.getParameter("date"));
		DayChoice2 dayChoice = dayChoiceFromParam(Window.Location
				.getParameter("dayChoice"));
		System.out.println(dayChoice);

		getTodaysService.getTodaysWord(date, dayChoice,
				new AsyncCallback<Word6>() {
					public void onFailure(Throwable caught) {
						wordPanel.clear();
						wordPanel.add(new HTML(
								"Error while connecting to the service: "
										+ caught.getMessage()));
					}

					public void onSuccess(Word6 result) {
						HTML html = new HTML();
						String h = new String(
								"<div id='date'>"
										+ (result.isPreviousDayPossible() ? "<a href='WordOfTheDay.html?date="
												+ result.getDate()
												+ "&dayChoice=p'>&nbsp;&nbsp;previous&nbsp;&nbsp;</a>"
												: "")
										+ DateHelper.toString(result.getDate())
										+ (result.isNextDayPossible() ? "<a href='WordOfTheDay.html?date="
												+ result.getDate()
												+ "&dayChoice=n'>&nbsp;&nbsp;next&nbsp;&nbsp;</a>"
												: "")
										+ "</div>"
										+ "<div class='name'>"
										+ result.getName()
										+ "</div>"
										+ "<div class='explanation'><div class='label'>Meaning:</div>"
										+ result.getExplanation() + "</div>"
										+ "<div class='usage'>");
						if (result.getUsage() != null) {
							if (result.getUsage().size() > 0)
								h += "<div class='label'>Usage:</div>";
							for (String usageExample : result.getUsage()) {
								h += "<div class='usageExample'>"
										+ usageExample + "</div>";
							}
						}
						h += "</div>";
						html.setHTML(h);
						wordPanel.clear();
						wordPanel.add(html);
					}
				});
	}
}
