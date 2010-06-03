package wordOfTheDay.server;

import java.util.Calendar;
import java.util.GregorianCalendar;

import wordOfTheDay.client.DateHelper;

public class Date {
	public static int getDate(int year, int month, int day) {
		// 20100523
		return year * 10000 + month * 100 + day;
	}

	public static int getCurrentDate() {
		java.util.Date d = new java.util.Date();
		return getDate(d.getYear() + 1900, d.getMonth() + 1, d.getDate());
	}

	public static int getYear(int date) {
		return DateHelper.getYear(date);
	}

	public static int getDay(int date) {
		return DateHelper.getDay(date);
	}

	public static int getMonth(int date) {
		return DateHelper.getMonth(date);
	}

	public static int getNextDay(int date) {
		return addNrDays(date, 1);
	}

	public static int getPreviousDay(int date) {
		return addNrDays(date, -1);
	}

	public static int addNrDays(int date, int numDays) {
		Calendar calendar = new GregorianCalendar(getYear(date),
				getMonth(date)-1, getDay(date));
		calendar.add(Calendar.DAY_OF_MONTH, numDays);
		return getDate(calendar.get(Calendar.YEAR), calendar
				.get(Calendar.MONTH)+1, calendar.get(Calendar.DAY_OF_MONTH));
	}

	public static void main(String[] args) {
		System.out.println(getDate(2010, 5, 23));
		System.out.println(getDay(20100523));
		System.out.println(getMonth(20100523));
		System.out.println(getYear(20100523));
	}
}
