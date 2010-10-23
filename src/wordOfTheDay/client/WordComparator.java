/**
 * Coparator for sorting instances of the sample User entity class.
 * 
 * (c) 2007 by Svetlin Nakov - http://www.nakov.com
 * National Academy for Software Development - http://academy.devbg.org 
 * This software is freeware. Use it at your own risk.
 */

package wordOfTheDay.client;

import java.util.Comparator;

import wordOfTheDay.client.Word9;
import wordOfTheDay.client.listWords.ReflectionUtils;

public class WordComparator implements Comparator<Word9> {

	private String sortColumn;
	private boolean sortingOrder;

	public WordComparator(String sortColumn, boolean sortingOrder) {
		this.sortColumn = sortColumn;
		this.sortingOrder = sortingOrder;
	}

	@SuppressWarnings("unchecked")
	public int compare(Word9 word1, Word9 word2) {
		Comparable column1 = (Comparable) ReflectionUtils.getPropertyValue(
				word1, this.sortColumn).toString();
		Comparable column2 = (Comparable) ReflectionUtils.getPropertyValue(
				word2, this.sortColumn).toString();
		int compareResult = -1;
		if (column1 != null) {
			if (column2 != null) {
				compareResult = column1.compareTo(column2);
			} else {
				compareResult = 1;
			}
		}
		if (!this.sortingOrder) {
			compareResult = -1 * compareResult;
		}
		return compareResult;
	}
}
