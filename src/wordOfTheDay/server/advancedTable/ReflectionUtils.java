/**
 * Helper class that provides some methods for simplified use of Java
 * reflection on the server side.
 * 
 * (c) 2007 by Svetlin Nakov - http://www.nakov.com
 * National Academy for Software Development - http://academy.devbg.org 
 * This software is freeware. Use it at your own risk.
 */

package wordOfTheDay.server.advancedTable;

import java.lang.reflect.Method;
import java.util.List;

import wordOfTheDay.client.DateHelper;

public class ReflectionUtils {

	public static Object getPropertyValue(Object obj, String propertyName) {
		try {
			Class<? extends Object> objClass = obj.getClass();
			Method method = objClass.getMethod("get" + propertyName);
			Object value = method.invoke(obj, (Object[]) null);
			return value;
		} catch (Exception ex) {
			throw new RuntimeException("Property not found " + propertyName, ex);
		}
	}

	public static String getPropertyStringValue(Object obj, String propertyName) {
		Object value = getPropertyValue(obj, propertyName);
		if (value == null) {
			return null;
		} else if (value.getClass() == Integer.class) {
			return DateHelper.toStringWithoutSpace((Integer) value);
		} else if (value.getClass() == List.class) {
			String ret = "";
			ret += "<ul>";
			for (String us : (List<String>) value) {
				ret += "<li>" + us + "</li>";
			}
			ret += "</ul>";
			return ret;
		} else {
			String valueStr = value.toString();
			return valueStr;
		}
	}

}
