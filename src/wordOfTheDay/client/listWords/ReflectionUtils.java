/**
 * Helper class that provides some methods for simplified use of Java
 * reflection on the server side.
 * 
 * (c) 2007 by Svetlin Nakov - http://www.nakov.com
 * National Academy for Software Development - http://academy.devbg.org 
 * This software is freeware. Use it at your own risk.
 */

package wordOfTheDay.client.listWords;

import java.util.List;

import wordOfTheDay.client.DateHelper;
import wordOfTheDay.client.Word9;

public class ReflectionUtils {

	public static Object getPropertyValue(Word9 obj, String propertyName) {
		if (propertyName.equals("ShortName"))
			return obj.getShortName();
		else if (propertyName.equals("ShortExplanation"))
			return obj.getShortExplanation();
		else if (propertyName.equals("ShortUsage"))
			return obj.getShortUsage();
		else if (propertyName.equals("Date"))
			return obj.getDate();
		else if (propertyName.equals("ShortLabels"))
			return obj.getShortLabels();
		else if (propertyName.equals("Name"))
			return obj.getName();
		else if (propertyName.equals("Explanation"))
			return obj.getExplanation();
		else if (propertyName.equals("Usage"))
			return obj.getUsage();
		else if (propertyName.equals("Labels"))
			return obj.getLabels();
		return new String("Not Found " + propertyName);
	}

	public static String getPropertyStringValue(Word9 obj, String propertyName) {
		Object value = getPropertyValue(obj, propertyName);
		if (value == null) {
			return null;
		} else if (value.getClass() == Integer.class) {
			return DateHelper.toStringWithoutSpace((Integer) value);
		} else if (value.getClass() == String.class) {
			return value.toString();
		} else { // list
			String ret = "";
			// ret += "<ul>";
			for (String us : (List<String>) value) {
				// ret += "<li>" + us + "</li>";
				ret += us + " ";
			}
			// ret += "</ul>";
			return ret;
		}
	}
}
