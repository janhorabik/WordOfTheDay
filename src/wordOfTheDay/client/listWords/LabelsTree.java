package wordOfTheDay.client.listWords;

import wordOfTheDay.client.Dashboard;
import wordOfTheDay.client.dbOnClient.DatabaseOnClient;
import wordOfTheDay.client.listWords.advancedTable.AdvancedTable;
import wordOfTheDay.client.listWords.advancedTable.DataFilter;
import wordOfTheDay.client.listWords.advancedTable.LabelBeginFilter;

import com.google.gwt.event.dom.client.ClickEvent;
import com.google.gwt.event.dom.client.ClickHandler;
import com.google.gwt.event.dom.client.MouseDownEvent;
import com.google.gwt.event.dom.client.MouseDownHandler;
import com.google.gwt.event.dom.client.MouseOutEvent;
import com.google.gwt.event.dom.client.MouseOutHandler;
import com.google.gwt.event.dom.client.MouseOverEvent;
import com.google.gwt.event.dom.client.MouseOverHandler;
import com.google.gwt.user.client.ui.Anchor;
import com.google.gwt.user.client.ui.HorizontalPanel;
import com.google.gwt.user.client.ui.Image;
import com.google.gwt.user.client.ui.Tree;
import com.google.gwt.user.client.ui.TreeItem;

public class LabelsTree {

	public LabelsTree(DatabaseOnClient database, AdvancedTable table) {
		this.database = database;
		this.table = table;
	}

	private static int MAX_LABEL_LEN = 13;
	private DatabaseOnClient database;
	private AdvancedTable table;

	Tree createTree() {
		Tree tree = new Tree();
		tree.setStyleName("wordOfTheDayLeft");
		for (final String treeLabel : database.getLabels()) {
			TreeItem parentItem = null;
			String[] labels = treeLabel.split(":");
			String labelSoFar = "";
			for (int i = 0; i < labels.length; ++i) {
				String label = labels[i];
				labelSoFar += label;
				if (parentItem == null) {
					// look at top:
					parentItem = findElement(tree, label);
					if (parentItem == null) {
						// create new
						parentItem = createItem(tree, label, labelSoFar);
					}
				} else {
					TreeItem currentItem = findElement(parentItem, label);
					if (currentItem == null) {
						// create new
						currentItem = createItem(parentItem, label, labelSoFar);
					}
					parentItem = currentItem;
				}
				labelSoFar += ":";
			}
		}
		return tree;
	}

	private TreeItem createItem(TreeItem parentItem, final String label,
			String labelSoFar) {
		HorizontalPanel panel = createPanelElement(label, labelSoFar);
		TreeItem currentItem = parentItem.addItem(panel);
		currentItem.setTitle(labelSoFar);
		currentItem.setUserObject(label);
		return currentItem;
	}

	private HorizontalPanel createPanelElement(String label, String labelSoFar) {
		HorizontalPanel panel = new HorizontalPanel();
		final Anchor arrow = new Anchor("&nbsp;&nbsp;&nbsp;&nbsp;", true);
		class ArrowMouseOverHandler implements MouseOverHandler {
			public void onMouseOver(MouseOverEvent event) {
				arrow.setText(AdvancedTable.SORT_DESC_SYMBOL);
			}
		}
		;
		class ArrowMouseOutHandler implements MouseOutHandler {
			public void onMouseOut(MouseOutEvent event) {
				arrow.setHTML("&nbsp;&nbsp;&nbsp;&nbsp;");
			}
		}
		;
		arrow.addMouseOverHandler(new ArrowMouseOverHandler());
		arrow.addMouseOutHandler(new ArrowMouseOutHandler());
		panel.add(arrow);
		Anchor anchor = createAnchor(label, labelSoFar);
		anchor.addMouseOverHandler(new ArrowMouseOverHandler());
		anchor.addMouseOutHandler(new ArrowMouseOutHandler());
		panel.add(anchor);
		return panel;
	}

	private TreeItem createItem(Tree tree, final String label,
			final String labelSoFar) {
		HorizontalPanel panel = createPanelElement(label, labelSoFar);
		TreeItem parentItem = tree.addItem(panel);
		parentItem.setTitle(labelSoFar);
		parentItem.setUserObject(label);
		return parentItem;
	}

	private TreeItem findElement(TreeItem parentItem, String label) {
		int numElements = parentItem.getChildCount();
		for (int i = 0; i < numElements; ++i) {
			if (parentItem.getChild(i).getUserObject().equals(label))
				return parentItem.getChild(i);
		}
		return null;
	}

	private TreeItem findElement(Tree tree, String label) {
		int numElements = tree.getItemCount();
		for (int i = 0; i < numElements; ++i)
			if (tree.getItem(i).getUserObject().equals(label)) {
				return tree.getItem(i);
			}
		return null;
	}

	private Anchor createAnchor(final String label, final String labelSoFar) {
		String shortLabel = label.length() > MAX_LABEL_LEN ? label.substring(0,
				MAX_LABEL_LEN) : label;
		final Anchor anchor = new Anchor(shortLabel);
		anchor.addMouseDownHandler(new MouseDownHandler() {
			public void onMouseDown(MouseDownEvent event) {
				Dashboard.tooltip.show();
				Dashboard.tooltip.getContainer().setText(labelSoFar);
			}
		});
		anchor.addClickHandler(new ClickHandler() {
			public void onClick(ClickEvent event) {
				Dashboard.tooltip.hide();
				DataFilter filter = new LabelBeginFilter(labelSoFar);
				DataFilter[] filters = { filter };
				table.applyFilters(filters);
			}
		});
		return anchor;
	}
}
