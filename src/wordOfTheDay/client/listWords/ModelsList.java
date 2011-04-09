package wordOfTheDay.client.listWords;

import java.util.List;

import wordOfTheDay.client.DataModel;
import wordOfTheDay.client.MyPopup.ServerResponse;
import wordOfTheDay.client.dbOnClient.DatabaseOnClient;
import wordOfTheDay.client.listWords.notesTable.MessagesPanel;

import com.google.gwt.user.client.Command;
import com.google.gwt.user.client.Window;
import com.google.gwt.user.client.ui.HorizontalPanel;
import com.smartgwt.client.types.TreeModelType;
import com.smartgwt.client.widgets.grid.HoverCustomizer;
import com.smartgwt.client.widgets.grid.ListGridRecord;
import com.smartgwt.client.widgets.menu.Menu;
import com.smartgwt.client.widgets.menu.events.MenuItemClickEvent;
import com.smartgwt.client.widgets.tree.TreeGrid;
import com.smartgwt.client.widgets.tree.TreeGridField;
import com.smartgwt.client.widgets.tree.TreeNode;

class DataModelElement extends TreeNode {
	private DataModel model;

	public DataModelElement(DataModel model) {
		this.model = model;
		setAttribute("Name", model.getName());
	}

	public DataModel getModel() {
		return this.model;
	}
}

class ModelChangeCommand implements Command {

	private MessagesPanel messagesPanel;

	public ModelChangeCommand(DataModel modelArg, boolean rename,
			MessagesPanel messagesPanel) {
		this.model = modelArg;
		this.isRename = rename;
		this.messagesPanel = messagesPanel;
	}

	public void execute() {
		class Reply implements ServerResponse {
			public void error(String error) {
				messagesPanel.showMessage(error);
			}

			public void serverReplied(String reply) {
				messagesPanel.showMessage(reply);
			}

			public void askedServer(String messageAtTheBeginning) {
				messagesPanel.showMessage(messageAtTheBeginning);
			}
		}
		String newName = isRename ? newName = Window.prompt("New name",
				model.getName()) : "";
		if (newName != null) {
			ModelsList.askServerToChangeModel.setModel(model, newName,
					!isRename);
			Reply reply = new Reply();
			ModelsList.askServerToChangeModel.askServer(reply);
		}
	}

	private boolean isRename;
	private DataModel model;

};

public class ModelsList {
	private DatabaseOnClient database;

	private HorizontalPanel panel;

	private MessagesPanel messagesPanel;

	static AskServerToChangeModel askServerToChangeModel;

	private TreeGrid treeGrid;

	public ModelsList(DatabaseOnClient database,
			HorizontalPanel modelsListPanel, MessagesPanel messagesPanel) {
		this.database = database;
		this.panel = modelsListPanel;
		this.messagesPanel = messagesPanel;
		askServerToChangeModel = new AskServerToChangeModel(database);
	}

	public void update() {
		this.panel.clear();
		this.panel.setSpacing(30);
		this.panel.add(createSmartTree());
	}

	private TreeGrid createSmartTree() {
		treeGrid = new TreeGrid();
		// treeGrid.addStyleName("modelsList");
		treeGrid.setWidth(100);
		treeGrid.setHeight(400);
		treeGrid.setNodeIcon(null);
		TreeGridField field = new TreeGridField("Name", "Models");
		field.setCanSort(true);
		treeGrid.setFields(field);

		final com.smartgwt.client.widgets.tree.Tree tree = new com.smartgwt.client.widgets.tree.Tree();
		tree.setModelType(TreeModelType.PARENT);
		tree.setNameProperty("Name");

		DataModelElement[] elements = createDataModelElements();
		tree.setData(elements);

		treeGrid.setData(tree);

		Menu menu = createMenu();
		treeGrid.setContextMenu(menu);
		treeGrid.setShowHover(true);
		treeGrid.setCanHover(true);
		treeGrid.setShowEmptyMessage(false);
		// treeGrid.setIconSize(1);
		treeGrid.setHoverCustomizer(new HoverCustomizer() {
			public String hoverHTML(Object value, ListGridRecord record,
					int rowNum, int colNum) {
				return record.getAttribute("Name");
			}
		});
		treeGrid.addClickHandler(new com.smartgwt.client.widgets.events.ClickHandler() {

			public void onClick(
					com.smartgwt.client.widgets.events.ClickEvent event) {
				DataModelElement dataModelElement = (DataModelElement) treeGrid
						.getSelectedRecord();
				if (dataModelElement != null) {
					database.setCurrentDataModel(dataModelElement.getModel()
							.getSeqNum());
				}
			}
		});

		treeGrid.draw();
		return treeGrid;
	}

	private DataModelElement[] createDataModelElements() {
		DataModelElement[] elements = new DataModelElement[database.getModels()
				.size()];
		List<DataModel> models = database.getModels();
		for (int i = 0; i < models.size(); ++i)
			elements[i] = new DataModelElement(models.get(i));
		return elements;
	}

	private Menu createMenu() {
		Menu menu = new Menu();
		menu.setWidth(150);
		menu.setShowHeaderContextMenu(true);

		com.smartgwt.client.widgets.menu.MenuItem renameItem = new com.smartgwt.client.widgets.menu.MenuItem(
				"Rename");
		renameItem
				.addClickHandler(new com.smartgwt.client.widgets.menu.events.ClickHandler() {
					public void onClick(MenuItemClickEvent event) {
						ModelChangeCommand renameCommand = new ModelChangeCommand(
								((DataModelElement) treeGrid
										.getSelectedRecord()).getModel(), true,
								messagesPanel);
						renameCommand.execute();
					}
				});
		com.smartgwt.client.widgets.menu.MenuItem removeItem = new com.smartgwt.client.widgets.menu.MenuItem(
				"Remove");
		removeItem
				.addClickHandler(new com.smartgwt.client.widgets.menu.events.ClickHandler() {
					public void onClick(MenuItemClickEvent event) {
						ModelChangeCommand removeCommand = new ModelChangeCommand(
								((DataModelElement) treeGrid
										.getSelectedRecord()).getModel(),
								false, messagesPanel);
						removeCommand.execute();
					}
				});
		menu.addItem(renameItem);
		menu.addItem(removeItem);
		return menu;
	}

	public void newModelWasAdded(DataModel model) {
		update();
	}

	public void modelWasRemoved(int seqNum) {
		update();
	}

	public void modelWasRenamed(int seqNum, String newName) {
		update();
	}

}
