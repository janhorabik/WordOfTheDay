package wordOfTheDay.client.listWords;

import wordOfTheDay.client.dbOnClient.DatabaseOnClient;
import wordOfTheDay.client.listWords.advancedTable.AdvancedTable;
import wordOfTheDay.client.listWords.advancedTable.DataFilter;
import wordOfTheDay.client.listWords.advancedTable.LabelBeginFilter;

import com.google.gwt.event.dom.client.MouseDownEvent;
import com.google.gwt.event.dom.client.MouseDownHandler;
import com.google.gwt.event.logical.shared.SelectionEvent;
import com.google.gwt.event.logical.shared.SelectionHandler;
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
			for (final String label : treeLabel.split(":")) {
				if (parentItem == null) {
					// look at top:
					parentItem = findElement(tree, label);
					if (parentItem == null) {
						// create new
						parentItem = createItem(tree, label);
					}
				} else {
					TreeItem currentItem = findElement(parentItem, label);
					if (currentItem == null) {
						// create new
						currentItem = createItem(parentItem, label);
					}
					parentItem = currentItem;
				}
			}
		}
		tree.addSelectionHandler(new SelectionHandler<TreeItem>() {
			public void onSelection(SelectionEvent<TreeItem> event) {
				String labelToFilter = "";
				TreeItem item = event.getSelectedItem();
				while (item != null) {
					labelToFilter = item.getText() + ":" + labelToFilter;
					item = item.getParentItem();
				}
				DataFilter filter = new LabelBeginFilter(labelToFilter
						.substring(0, labelToFilter.length() - 2));
				DataFilter[] filters = { filter };
				table.applyFilters(filters);
			}
		});

		tree.addMouseDownHandler(new MouseDownHandler() {

			public void onMouseDown(MouseDownEvent event) {
				event.getSource().toString();
			}
		});
		return tree;
	}

	private TreeItem createItem(TreeItem parentItem, final String label) {
		TreeItem currentItem;
		Anchor anchor = createAnchor(label);
		currentItem = parentItem.addItem(anchor);
		if (label.length() > MAX_LABEL_LEN) {
			currentItem.setTitle(label);
		}
		currentItem.setUserObject(label);
		return currentItem;
	}

	private TreeItem createItem(Tree tree, final String label) {
		TreeItem parentItem;
		Anchor anchor = createAnchor(label);
		parentItem = tree.addItem(anchor);
		if (label.length() > MAX_LABEL_LEN) {
			parentItem.setTitle(label);
		}
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

	private Anchor createAnchor(final String label) {
		String shortLabel = label.length() > MAX_LABEL_LEN ? label.substring(0,
				MAX_LABEL_LEN) : label;
		final Anchor anchor = new Anchor(shortLabel);
		return anchor;
	}

}
