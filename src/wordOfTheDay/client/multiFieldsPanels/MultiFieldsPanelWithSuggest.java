package wordOfTheDay.client.multiFieldsPanels;

import java.util.List;
import java.util.Set;

import com.google.gwt.user.client.ui.HasText;
import com.google.gwt.user.client.ui.MultiWordSuggestOracle;
import com.google.gwt.user.client.ui.SuggestBox;

public class MultiFieldsPanelWithSuggest extends MultiFieldsPanel {

	private Set<String> database;

	protected MultiFieldsPanelWithSuggest(String title, String buttonTitle,
			Set<String> databaseArg) {
		super(title, buttonTitle);
		this.database = databaseArg;
	}

	protected MultiFieldsPanelWithSuggest(String title, String buttonTitle,
			Set<String> databaseArg, List<String> fields) {
		super(title, buttonTitle, fields);
		this.database = databaseArg;
	}

	public static MultiFieldsPanelWithSuggest create(String title,
			String buttonTitleArg, Set<String> databaseArg) {
		MultiFieldsPanelWithSuggest ret = new MultiFieldsPanelWithSuggest(
				title, buttonTitleArg, databaseArg);
		ret.updateFieldsPanel();
		return ret;
	}

	public static MultiFieldsPanelWithSuggest create(String title,
			String buttonTitleArg, Set<String> db, List<String> fields) {
		MultiFieldsPanelWithSuggest ret = new MultiFieldsPanelWithSuggest(
				title, buttonTitleArg, db, fields);
		ret.updateFieldsPanel();
		return ret;
	}

	public HasText createTextField() {
		MultiWordSuggestOracle oracle = new MultiWordSuggestOracle(" :");
		oracle.clear();
		oracle.addAll(database);
		return new SuggestBox(oracle);
	}

}
