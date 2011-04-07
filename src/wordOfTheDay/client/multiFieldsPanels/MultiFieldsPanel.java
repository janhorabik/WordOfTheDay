package wordOfTheDay.client.multiFieldsPanels;

import java.util.LinkedList;
import java.util.List;

import com.google.gwt.event.dom.client.ClickEvent;
import com.google.gwt.event.dom.client.ClickHandler;
import com.google.gwt.user.client.ui.Anchor;
import com.google.gwt.user.client.ui.Button;
import com.google.gwt.user.client.ui.HasText;
import com.google.gwt.user.client.ui.HorizontalPanel;
import com.google.gwt.user.client.ui.Label;
import com.google.gwt.user.client.ui.TextArea;
import com.google.gwt.user.client.ui.TextBox;
import com.google.gwt.user.client.ui.VerticalPanel;
import com.google.gwt.user.client.ui.Widget;

public class MultiFieldsPanel {
	private VerticalPanel panel = new VerticalPanel();
	private String title;
	private List<String> fields = new LinkedList<String>();
	private String buttonTitle;

	public void updateFieldsPanel() {
		class RemoveFieldClickHandler implements ClickHandler {
			RemoveFieldClickHandler(int iArg) {
				i = iArg;
			}

			public void onClick(ClickEvent event) {
				fields.remove(i);
				updateFieldsPanel();
			}

			int i;
		}
		panel.clear();
		Label titleLabel = new Label(title);
		panel.add(titleLabel);
		for (int i = 0; i < fields.size(); ++i) {
			String field = fields.get(i);
			HorizontalPanel fieldPanel = new HorizontalPanel();
			Label label = new Label(field);
			// label.addStyleName("wordOfTheDay");
			fieldPanel.add(label);
			Anchor removeFieldButton = new Anchor("x");
			// removeFieldButton.addStyleName("wordOfTheDay");
			removeFieldButton.addClickHandler(new RemoveFieldClickHandler(i));
			fieldPanel.add(removeFieldButton);
			panel.add(fieldPanel);
		}
		final HasText textBox = createTextField();
		panel.add((Widget) textBox);
		Button addFieldButton = new Button(buttonTitle);
		addFieldButton.addClickHandler(new ClickHandler() {
			public void onClick(ClickEvent event) {
				fields.add(textBox.getText());
				updateFieldsPanel();
			}
		});
		panel.add(addFieldButton);
	}

	protected MultiFieldsPanel(String titleArg, String buttonTitleArg) {
		title = titleArg;
		buttonTitle = buttonTitleArg;
	}

	protected MultiFieldsPanel(String titleArg, String buttonTitleArg,
			List<String> fieldsArg) {
		title = titleArg;
		buttonTitle = buttonTitleArg;
		fields = fieldsArg;
	}

	public static MultiFieldsPanel create(String title, String buttonTitle) {
		MultiFieldsPanel panel = new MultiFieldsPanel(title, buttonTitle);
		panel.updateFieldsPanel();
		return panel;
	}

	public VerticalPanel getPanel() {
		return this.panel;
	}

	public HasText createTextField() {
		TextBox ret = new TextBox();
		// ret.addStyleName("textArea");
		return ret;
	}

	public void clear() {
		panel.clear();
	}

	public List<String> getFields() {
		return this.fields;
	}

}
