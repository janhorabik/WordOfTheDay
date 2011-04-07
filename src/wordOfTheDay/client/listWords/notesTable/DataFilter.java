/**
 * DataFilter class represents a filter that is applied to a data set
 * coming from the server side. It consists of a set of column and value
 * and virtually can handle any type of filtering. For example if you have
 * users and need filtering by name (e.g. Peter) and age (e.g. 20-30),
 * you will add 3 data filters:
 *   - name: Peter
 *   - minAge: 20
 *   - maxAge: 30 
 * On the server side you will modify the SQL query to apply filtering
 * by name and age.
 * 
 * (c) 2007 by Svetlin Nakov - http://www.nakov.com
 * National Academy for Software Development - http://academy.devbg.org 
 * This software is freeware. Use it at your own risk.
 */

package wordOfTheDay.client.listWords.notesTable;

import wordOfTheDay.client.Note;

public interface DataFilter {
	public boolean accept(Note note);
}
