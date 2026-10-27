package uo.ri.util.date;

import java.sql.Date;
import java.sql.Timestamp;
import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import java.time.temporal.TemporalAdjusters;

import uo.ri.util.random.Random;

public class Dates {

	public static LocalDate firstDayOfNextMonth() {
		return LocalDate.now().with(TemporalAdjusters.firstDayOfNextMonth());
	}

	public static LocalDate lastDayOfCurrentMonth() {
		return LocalDate.now().with(TemporalAdjusters.lastDayOfMonth());
	}

	public static Timestamp rndTimestampBetween(Date startDate, Date endDate) {
		long startMillis = startDate.getTime();
		long endMillis = endDate.getTime();
		return new Timestamp(
				startMillis
				+ (long) Random.nextDouble() * (endMillis - startMillis)
			);
	}

	public static Date rndDateThisYear() {
		return Date.valueOf(
				LocalDate.now()
				.withDayOfYear(1)
				.plusDays((long)(Random.nextDouble() * 365))
			);
	}

	public static boolean isLastDayOfMonth(LocalDate date) {
        LocalDate now = LocalDate.now();
        return date.equals( now.withDayOfMonth(now.lengthOfMonth()) );
	}

	public static LocalDate lastDayOfMonth(LocalDate date) {
		return date.with(TemporalAdjusters.lastDayOfMonth());
	}

	public static LocalDate firstDayOfMonth(LocalDate date) {
		return date.with(TemporalAdjusters.firstDayOfMonth());
	}

	public static LocalDate parseToLocalDate(String todayStr) {
		DateTimeFormatter formatter = DateTimeFormatter.ofPattern("dd/MM/yyyy");
        return LocalDate.parse(todayStr, formatter);
	}

}
