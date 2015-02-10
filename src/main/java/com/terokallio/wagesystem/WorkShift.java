/**
 * 
 */
package com.terokallio.wagesystem;

import java.text.DateFormat;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;

/**
 * This class illustrates a single work shift
 * 
 * @author terokallio
 * @version 0.1
 *
 */
public class WorkShift {

	static final long ONE_MINUTE_IN_MILLIS = 60000;
	static final long ONE_DAY_IN_MINUTES = 1440;
	static final long EIGHT_HOUR_WORK_DAY_IN_MINUTES = 480;
	
	int workerId = -1;
	Date day = null;
	Date startTime = null;
	Date endTime = null;
	long totalMinutes;
	long regularMinutes;
	String workerName;
	
	public WorkShift() {
		
	}

	/**
	 * Constructor for WorkShift
	 * Will convert Strings immediately to more suitable objects
	 * 
	 * @param string workerName
	 * @param string2 workerId
	 * @param string3 day (format: 18.3.2014)
	 * @param string4 startTime (format: 9:30)
	 * @param string5 endTime: (format: 14:20)
	 */
	public WorkShift(String string, String string2, String string3,
			String string4, String string5) {
		workerName = string;
		workerId = Integer.valueOf(string2).intValue();
		day = parseStartTime(string3, string4);
		startTime = parseStartTime(string3, string4);
		endTime = parseEndTime(string3, string4, string5);
		
		//Total minutes of this work shift can be calculated here. 
		this.totalMinutes = getTotalTimeInMinutes(startTime, endTime);
		
		// printAll();
	}
	
	/**
	 * Creates the startTime Date object
	 *  
	 * @param day 
	 * @param time 
	 * @return Date
	 */
	protected Date parseStartTime(String day, String time) {
		DateFormat formatter = new SimpleDateFormat("dd.MM.yyyy hh:mm");
		Date date = null;
		try {
			date = (Date) formatter.parse(day+" "+time);
		} catch (ParseException e) {
			System.out.println("Could not parse the day and time");
			e.printStackTrace();
		}
		return date;
	}
	
	/**
	 * Creates the endTime Date object
	 *  
	 * @param day 
	 * @param startTime 
	 * @param endTime
	 * @return Date
	 */
	protected Date parseEndTime(String day, String startTime, String endTime) {
		DateFormat formatter = new SimpleDateFormat("dd.MM.yyyy HH:mm");
		
		Date endDate = null;
	
		try {
			endDate = (Date) formatter.parse(day+" "+endTime);
			
		} catch (ParseException e) {
			System.out.println("Could not parse the day and time");
			e.printStackTrace();
		}
	
		// we need to check if there is a day switch
		// if true, we will adjust the endDate
		if (checkDaySwitch(startTime, endTime) == true) {
			// Add a day to end Date
			// Adjust date accordingly
			// System.out.println("DAYSWITCH");
			endDate = adjustEndDate(endDate);
		}
		return endDate;
	}
		
	/**
	 * This method adjusts endDate Date object to next day
	 * @param endDate
	 * @return Date adjusted to next day
	 */
	protected Date adjustEndDate(Date endDate) {
		long t = endDate.getTime();
		endDate = new Date(t + (ONE_DAY_IN_MINUTES * ONE_MINUTE_IN_MILLIS));
		return endDate;
	}

	/**
	 * Checks for work shifts that go over midnight
	 * @param startTime2
	 * @param endTime2
	 * @return boolean true if work shift over midnight
	 */
	protected boolean checkDaySwitch(String startTime2, String endTime2) {
		SimpleDateFormat sdf = new SimpleDateFormat("HH:mm");
		Date date1 = null;
		Date date2 = null; 
    	try {
    		date1 = sdf.parse(startTime2);
			date2 = sdf.parse(endTime2);
		} catch (ParseException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
    	
    	if (date1.compareTo(date2) > 0) {
    		// date 1 is after date 2, meaning there must have 
    		// been day switch, aka work shift over midnight 
    		return true;
    	}
		return false;
	}

	/**
	 * Get all hours in this work shift
	 * @return long all hours in this work shift in minutes 
	 */
	protected long getTotalTimeInMinutes(Date startTime, Date endTime) {
		// lets get total work time in minutes
		long workMinutes = (endTime.getTime() - startTime.getTime())
				/ ONE_MINUTE_IN_MILLIS;
		return workMinutes;
	}
	
	/**
	 * Calculates total work minutes in this shift during 06:00 - 18:00
	 * reduces evening hours from total shift.
	 * 
	 * Helper method for calculating salaries.
	 * 
	 * @return long normal Minutes
	 */
	protected long getNormalMinutes() {
	
		return getTotalTimeInMinutes(this.startTime, this.endTime) 
				- getAmountOfEveningTime();
		
	}
	
	/**
	 * TODO: REFACTOR / REWRITE
	 * Get amount of Evening Time
	 * This is time between 18:00 - 06:00
	 * @return long time in Minutes
	 */
	protected long getAmountOfEveningTime() {
		long eveningTime = 0;
		int startHour = this.getHour(this.startTime);
		int endHour = this.getHour(this.endTime);
		// System.out.println("startHour = " + startHour + ", endHour = " + endHour);
		// lets just exit, if normal workshift
		if (startHour >= 6 && endHour < 18 ) {
			// System.out.println("no evening time");
			return eveningTime;
		}
		
		// lets first see if day has changed, work over midnight situation
		if (endHour < startHour) {
			if (startHour < 18) {
				eveningTime = this.endTime.getTime() - getSixPMDateObject().getTime();
			} else {
				eveningTime = this.endTime.getTime() - this.startTime.getTime();
			}			
		}
		// early starts, and end in normal works hours.
		else if (startHour < 6 && endHour < 18 ) { //&& endHour > 6)) {
			 eveningTime = getSixAMDateObject().getTime() - this.startTime.getTime();
		}
		// normal starts, late ends
		else if (startHour < 18 && startHour >= 6 && endHour >= 18 ) {
			// need to take a look at the end date. 
			// calculate by how much later
			eveningTime = this.endTime.getTime() - getSixPMDateObject().getTime();
		}
		// early start, end early 
		else if (startHour < 6 && endHour < 6) {
			eveningTime = this.endTime.getTime() - this.startTime.getTime();
		}
		// start late, end late.
		else if (startHour >= 18 && endHour >= 18) {
			eveningTime = this.endTime.getTime() - this.startTime.getTime();
		}
		// start early, end late
		else if (startHour < 06 && endHour >= 18 ) {
			eveningTime = this.endTime.getTime() - this.startTime.getTime() 
					- (720 * 60 * 1000); // 06-18 time
		}
		else {
			// we should never get here 
			System.out.println("getAmountOfEveningTime() - WE HAVE A BUG HERE");
			System.err.println("getAmountOfEveningTime() - WE HAVE A BUG HERE");
		}
		
		return eveningTime / ONE_MINUTE_IN_MILLIS;
	}
	
	/**
	 * Helper method for getting Date object for
	 * Evening time calculations. 
	 * 
	 * @return Date with same day but time set to 06:00
	 */
	private Date getSixAMDateObject() {
		Calendar cal = Calendar.getInstance();
		cal.setTime(this.startTime);
		cal.set(Calendar.HOUR_OF_DAY,6);
		cal.set(Calendar.MINUTE,0);
		cal.set(Calendar.SECOND,0);
		cal.set(Calendar.MILLISECOND,0);
		return cal.getTime();
	}
	/**
	 * Helper method for getting Date object for
	 * Evening time calculations. 
	 * 
	 * @return Date with same day but time set to 18:00
	 */
	private Date getSixPMDateObject() {
		Calendar cal = Calendar.getInstance();
		cal.setTime(this.startTime);
		cal.set(Calendar.HOUR_OF_DAY,18);
		cal.set(Calendar.MINUTE,0);
		cal.set(Calendar.SECOND,0);
		cal.set(Calendar.MILLISECOND,0);
		return cal.getTime();
	}
	
	/**
	 * Overtime check, overtime as more than 8 hour work day.
	 * Note: this only checks for this specific work shift
	 * 
	 * @return
	 */
	public boolean isOverEightHours() {
		
		if (this.totalMinutes > EIGHT_HOUR_WORK_DAY_IN_MINUTES) {
			return true;
		}
		return false;
	}
	
	/**
	 * Return Hour of Date object
	 * @param startTime
	 * @return int Hour
	 */
	private int getHour(Date startTime) {
		Calendar calendar = Calendar.getInstance();
		calendar.setTime(startTime);
		return calendar.get(Calendar.HOUR_OF_DAY);
	}
	
	private void printAll() {
		System.out.println("workerName:" + workerName + ", workerId: "
				+ workerId + ", day: " + day + ", startTime: "
				+ startTime + ", endTime: " + endTime  ); 
	}
	
	/**
	 * @return the day
	 */
	protected Date getDay() {
		return day;
	}

	/**
	 * @return the totalMinutes
	 */
	protected long getTotalMinutes() {
		return totalMinutes;
	}

}
