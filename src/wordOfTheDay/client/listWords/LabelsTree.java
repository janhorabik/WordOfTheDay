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
import com.google.gwt.user.client.ui.Anchor;
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
		Anchor anchor = createAnchor(label, labelSoFar);
		TreeItem currentItem = parentItem.addItem(anchor);
		currentItem.setTitle(labelSoFar);
		currentItem.setUserObject(label);
		return currentItem;
	}

	private TreeItem createItem(Tree tree, final String label,
			final String labelSoFar) {
		Anchor anchor = createAnchor(label, labelSoFar);
		TreeItem parentItem = tree.addItem(anchor);
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
				DataFilter filter = new LabelBeginFilter(labelSoFar);
				DataFilter[] filters = { filter };
				table.applyFilters(filters);
			}
		});
		return anchor;
	}
}
