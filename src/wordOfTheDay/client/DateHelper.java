package wordOfTheDay.client;

public class DateHelper {
	public static String toString(int date) {
		String ret = new String();
		ret += getYear(date) + " - ";
		ret += getSimpleString(getMonth(date)) + " - ";
		ret += getSimpleString(getDay(date));
		return ret;
	}

	public static String toStringWithoutSpace(int date) {
		String ret = new String();
		ret += getYear(date) + "-";
		ret += getSimpleString(getMonth(date)) + "-";
		ret += getSimpleString(getDay(date));
		return ret;
	}
	
	
	public static String getSimpleString(int number) {
		String ret = new String();
		if (number >= 10)
			ret += number;
		else
			ret += "0" + number;
		return ret;
	}

	public static int getYear(int date) {
		return date / 10000;
	}

	public static int getDay(int date) {
		return date % 100;
	}

	public static int getMonth(int date) {
		return (date % 10000) / 100;
	}

}
