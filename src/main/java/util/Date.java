package util;

/**
 * Represents a date with year, month, and day, and includes methods for
 * validating the date, checking for leap years, and comparing dates.
 *
 * This class also provides constants for leap year and month length calculations.
 *
 * @author Joshua Goykhman (Netid: jg1986)
 */
public class Date implements Comparable<Date> {
    // Constants for leap year and month length calculations
    public static final int QUADRENNIAL = 4;
    public static final int CENTENNIAL = 100;
    public static final int QUATERCENTENNIAL = 400;
    public static final int[] BIG_MONTHS = {1, 3, 5, 7, 8, 10, 12};

    public static final int LAST_DAY_FEB_LEAP_YEAR = 29;
    public static final int LAST_DAY_FEB_NOT_LEAP_YEAR = 28;
    public static final int LAST_DAY_NORMAL_MONTH_BIG = 31;
    public static final int LAST_DAY_NORMAL_MONTH_SMALL = 30;

    public static final int DATE_MONTH_INDEX = 0;
    public static final int DATE_DAY_INDEX = 1;
    public static final int DATE_YEAR_INDEX = 2;

    // Instance variables for year, month, and day
    private int year;
    private int month;
    private int day;

    /**
     * Default constructor that initializes the date fields to invalid values (-1).
     */
    public Date() {
        this.year = -1;
        this.month = -1;
        this.day = -1;
    }

    /**
     * Constructs a date with the specified month, day, and year.
     * 
     * @param month The month of the date.
     * @param day The day of the date.
     * @param year The year of the date.
     * @throws IllegalArgumentException if the date is invalid.
     */
    public Date(int month, int day, int year) {
        if (!isDayValid(month, day, year)) {
            throw new IllegalArgumentException("Invalid date.");
        }
        this.year = year;
        this.month = month;
        this.day = day;
    }

    /**
     * Constructs a Date object with a string in the format "MM/DD/YYYY".
     *
     * @param dateString A string in the format "MM/DD/YYYY".
     * @throws IllegalArgumentException if the string is not properly formatted or the date is invalid.
     */
    public Date(String dateString) {
        try {
            String[] dateParts = dateString.split("/");
            if (dateParts.length != 3) {
                throw new IllegalArgumentException("Invalid date format. Expected MM/DD/YYYY.");
            }
            this.month = Integer.parseInt(dateParts[DATE_MONTH_INDEX]);
            this.day = Integer.parseInt(dateParts[DATE_DAY_INDEX]);
            this.year = Integer.parseInt(dateParts[DATE_YEAR_INDEX]);
        } catch (NumberFormatException e) {
            throw new IllegalArgumentException("Invalid date format. Expected numeric values in MM/DD/YYYY.");
        }
    }

    /**
     * Copy constructor that creates a new Date object by copying the values from
     * another Date object.
     *
     * @param date The Date object to copy.
     */
    public Date(Date date) {
        this.year = date.year;
        this.month = date.month;
        this.day = date.day;
    }

    /**
     * Validates the date by checking if the month and day are appropriate for the year.
     *
     * @return true if the date is valid, false otherwise.
     */
    public boolean isValid() {
        return isDayValid(month, day, year);
    }

    /**
     * Gets the month of the date.
     *
     * @return The month of the date.
     */
    public int getMonth() {
        return month;
    }

    /**
     * Gets the day of the date.
     *
     * @return The day of the date.
     */
    public int getDay() {
        return day;
    }

    /**
     * Gets the year of the date.
     *
     * @return The year of the date.
     */
    public int getYear() {
        return year;
    }

    /**
     * Validates the day of the month for the given month and year.
     *
     * @param month The month to check.
     * @param day The day to check.
     * @param year The year to check.
     * @return true if the day is valid for the given month and year, false otherwise.
     */
    private boolean isDayValid(int month, int day, int year) {
        if (year < 1 || month < 1 || month > 12) {
            return false;
        }
        int lastDay = (month == 2) ? (isLeapYear(year) ? LAST_DAY_FEB_LEAP_YEAR : LAST_DAY_FEB_NOT_LEAP_YEAR)
                                    : (isBigMonth(month) ? LAST_DAY_NORMAL_MONTH_BIG : LAST_DAY_NORMAL_MONTH_SMALL);
        return day > 0 && day <= lastDay;
    }

    /**
     * Checks if the given month is a 'big' month (having 31 days).
     *
     * @param month The month to check.
     * @return true if the month has 31 days, false otherwise.
     */
    private boolean isBigMonth(int month) {
        for (int bigMonth : BIG_MONTHS) {
            if (month == bigMonth) {
                return true;
            }
        }
        return false;
    }

    /**
     * Determines whether the given year is a leap year.
     *
     * @param year The year to check.
     * @return true if the year is a leap year, false otherwise.
     */
    private boolean isLeapYear(int year) {
        return (year % QUADRENNIAL == 0 && year % CENTENNIAL != 0) || (year % QUATERCENTENNIAL == 0);
    }

    /**
     * Compares this date to another date object.
     *
     * @param targetDate The date to compare against.
     * @return 0 if the dates are equal, 1 if this date is later, and -1 if this date is earlier.
     */
    @Override
    public int compareTo(Date targetDate) {
        if (year != targetDate.year) {
            return Integer.compare(this.year, targetDate.year);
        }
        if (month != targetDate.month) {
            return Integer.compare(this.month, targetDate.month);
        }
        return Integer.compare(this.day, targetDate.day);
    }

    /**
     * Returns a string representation of the date in the format "MM/DD/YYYY".
     *
     * @return The string representation of the date.
     */
    @Override
    public String toString() {
        return month + "/" + day + "/" + year;
    }

    /**
     * Checks if this date is equal to another date object based on their day,
     * month, and year.
     *
     * @param other The object to compare against.
     * @return true if the dates are equal, false otherwise.
     */
    @Override
    public boolean equals(Object other) {
        if (this == other) {
            return true;
        }
        if (other == null || getClass() != other.getClass()) {
            return false;
        }
        Date targetDate = (Date) other;
        return this.year == targetDate.year && this.month == targetDate.month && this.day == targetDate.day;
    }

    @Override
    public int hashCode() {
        int result = 17;
        result = 31 * result + year;
        result = 31 * result + month;
        result = 31 * result + day;
        return result;
    }

}
