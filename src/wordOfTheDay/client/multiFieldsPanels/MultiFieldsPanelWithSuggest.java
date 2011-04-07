package wordOfTheDay.client.multiFieldsPanels;

import java.util.List;

import wordOfTheDay.client.dbOnClient.DatabaseOnClient;

import com.google.gwt.user.client.ui.HasText;
import com.google.gwt.user.client.ui.MultiWordSuggestOracle;
import com.google.gwt.user.client.ui.SuggestBox;

public class MultiFieldsPanelWithSuggest extends MultiFieldsPanel {

	private DatabaseOnClient database;

	protected MultiFieldsPanelWithSuggest(String title, String buttonTitle,
			DatabaseOnClient databaseArg) {
		super(title, buttonTitle);
		this.database = databaseArg;
	}

	protected MultiFieldsPanelWithSuggest(String title, String buttonTitle,
			DatabaseOnClient databaseOnClientArg, List<String> fields) {
		super(title, buttonTitle, fields);
		this.database = databaseOnClientArg;
	}

	public static MultiFieldsPanelWithSuggest create(String title,
			String buttonTitleArg, DatabaseOnClient db) {
		MultiFieldsPanelWithSuggest ret = new MultiFieldsPanelWithSuggest(
				title, buttonTitleArg, db);
		ret.updateFieldsPanel();
		return ret;
	}

	public static MultiFieldsPanelWithSuggest create(String title,
			String buttonTitleArg, DatabaseOnClient db, List<String> fields) {
		MultiFieldsPanelWithSuggest ret = new MultiFieldsPanelWithSuggest(
				title, buttonTitleArg, db, fields);
		ret.updateFieldsPanel();
		return ret;
	}

	public HasText createTextField() {
		MultiWordSuggestOracle oracle = new MultiWordSuggestOracle(" :");
		oracle.clear();
		oracle.addAll(database.getLabelsOfCurrentDataModel());
		return new SuggestBox(oracle);
	}
}
