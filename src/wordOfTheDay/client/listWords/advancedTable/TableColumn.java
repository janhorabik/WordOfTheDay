/**
 * TableColumn class specifies a column in a table. It consists of column
 * name (e.g. physical column in the database) and column title that is
 * displayed in the table header.
 * 
 * (c) 2007 by Svetlin Nakov - http://www.nakov.com
 * National Academy for Software Development - http://academy.devbg.org 
 * This software is freeware. Use it at your own risk.
 */

package wordOfTheDay.client.listWords.advancedTable;

import com.google.gwt.user.client.rpc.IsSerializable;

public class TableColumn implements IsSerializable {

	private String name;
	private String title;
	private String longName;

	public TableColumn() {
	}

	public TableColumn(String name, String title, String longName) {
		this.name = name;
		this.title = title;
		this.longName = longName;
	}

	public String getName() {
		return name;
	}

	public void setName(String name) {
		this.name = name;
	}

	public String getTitle() {
		return title;
	}

	public String getLongName() {
		return longName;
	}

	public void setTitle(String title) {
		this.title = title;
	}

}
