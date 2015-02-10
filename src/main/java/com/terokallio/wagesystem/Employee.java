/**
 * 
 */
package com.terokallio.wagesystem;

import java.math.BigDecimal;
import java.math.RoundingMode;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Collections;
import java.util.Comparator;
import java.util.Date;
import java.util.Iterator;
import java.util.List;
import java.util.ListIterator;


/**
 * This class illustrates Employee object. 
 * Holds information of Employee work shifts
 * Calculates also salaries, overtime, evening, and normal wages.
 * 
 * @author terokallio
 * @version 0.1
 *
 */
public class Employee {

	private String id = null;
	private String name = null;
	private String salary = null;
	private List<WorkShift> workList = null; 
	
	static final double HOURLY_WAGE = 3.75; 
	static final double EVENING_WAGE = 3.75 + 1.15;
	static final double EIGHT_HOUR_WORK_DAY_IN_MINUTES = 480;
	
	// THESE ARE IN MINUTES
	private long normalTime = 0;
	private long overTime = 0;
	private long eveningTime = 0;
	private long overTime25 = 0;
	private long overTime50 = 0;
	private long overTime100 = 0;
	
	// salaries 
	private double monthlyWage; 
	private double regularHoursSalary;
	private double eveningHoursSalary;
	private double totalOverTimeSalary;
	private double overTimeSalary25;
	private double overTimeSalary50;
	private double overTimeSalary100;
	
	
	/**
	 * Constructor
	 * 
	 */
	public Employee() { 	}
	
	public Employee(String workerId, String workerName, List<WorkShift> list) 
	{
		this.id = workerId;
		this.name = workerName;
		this.workList = list;
		
		scanByDate();
		calculateSalary();
		
	}
	
	/**
	 * Calculate salaries 
	 */
	private void calculateSalary() {
		
		this.regularHoursSalary = round(this.normalTime / 60  * HOURLY_WAGE);
		this.eveningHoursSalary = round(this.eveningTime / 60 * EVENING_WAGE);
		this.overTimeSalary25 = round(this.overTime25 / 60 * (HOURLY_WAGE * 1.25));
		this.overTimeSalary50 = round(this.overTime50 / 60 * (HOURLY_WAGE * 1.50));
		this.overTimeSalary100 = round(this.overTime100 / 60 * (HOURLY_WAGE * 2));
		this.totalOverTimeSalary = round(this.overTimeSalary25 + this.overTimeSalary50
				+ overTimeSalary100);
		this.monthlyWage = round(this.regularHoursSalary + eveningHoursSalary + totalOverTimeSalary);
		
	}
	
	/**
	 * Round double types to 2 decimals
	 * @param value
	 * @return
	 */
	public double round(double value) {
	    BigDecimal bd = new BigDecimal(value);
	    bd = bd.setScale(2, RoundingMode.HALF_UP);
	    return bd.doubleValue();
	}

	/**
	 * Scan each work shift per day, and count the hours per each 
	 * workday for normal, evening, and overtime work
	 */
	private void scanByDate() {
		// lets go thru all entries
		ListIterator<WorkShift> it = workList.listIterator();
		while (it.hasNext()) {
			WorkShift ws = (WorkShift) it.next();

			int dayOfYear = this.getDate(ws.startTime);
			
			// we will pass a list of work shifts to calculate amount
			// of work minutes per day. This is it.
			List<WorkShift> worksPerDay = new ArrayList<>();
			worksPerDay.add(ws);
			
			// we need to check if day has multiple entries.
			while (it.hasNext()) {
				WorkShift ws2 = (WorkShift) it.next();
				int otherDayOfYear = this.getDate(ws2.startTime);
				if (dayOfYear == otherDayOfYear) {
					worksPerDay.add(ws2);
				}
				else {
					// after 
					it.previous(); break;
				}	
			}
			calculateNormalTimePerDay(worksPerDay);
			calculateOverTimePerDay(worksPerDay);
			calculateEveningTimePerDay(worksPerDay);
			
		}
	}
	
	/**
	 * Calculate normal worktime in a day
	 * @param worksPerDay
	 */
	protected void calculateNormalTimePerDay(List<WorkShift> worksPerDay) {
		Iterator<WorkShift> it = worksPerDay.iterator();
		double totalTimeForDay = 0;
		while (it.hasNext()) {
			WorkShift ws = (WorkShift) it.next();
			totalTimeForDay += ws.getNormalMinutes();
			
		}
		// if work shifts are longer than 8h during normal hours.
		// don't add here, as they are calculated as overtime, so max 8h
		if (totalTimeForDay > EIGHT_HOUR_WORK_DAY_IN_MINUTES) {
			totalTimeForDay = EIGHT_HOUR_WORK_DAY_IN_MINUTES;
		}
		this.normalTime += totalTimeForDay;
	}
	
	/**
	 * Calculate amount of overtime per day
	 */
	private void calculateOverTimePerDay(List<WorkShift> worksPerDay) 
	{
		Iterator<WorkShift> li = worksPerDay.iterator();
		long totalTimeForDay = 0;
		while (li.hasNext()) {
			WorkShift ws = (WorkShift) li.next();
			totalTimeForDay += ws.getTotalMinutes();
		}
		// see if total time is more than 8 hours
		// and add overtime if so. 
		if (totalTimeForDay > EIGHT_HOUR_WORK_DAY_IN_MINUTES) {
			this.overTime += totalTimeForDay - EIGHT_HOUR_WORK_DAY_IN_MINUTES;
			
			calculateOvertimeTimeBrackets(totalTimeForDay);
			
		}
	}
	
	/**
	 * Calculate overtime amounts for each overtime bracket
	 * 
	 * First 2 Hours > 8 Hours = Hourly Wage + 25% 
	 * Next 2 Hours = Hourly Wage + 50% 
	 * After That = Hourly Wage + 100%
	 *
	 * @param totalTimeForDay
	 */
	private void calculateOvertimeTimeBrackets(long totalTimeForDay) {
		
		if (totalTimeForDay < (EIGHT_HOUR_WORK_DAY_IN_MINUTES + 120)) {
			this.overTime25 += totalTimeForDay - EIGHT_HOUR_WORK_DAY_IN_MINUTES;
		}
		else if (totalTimeForDay < (EIGHT_HOUR_WORK_DAY_IN_MINUTES + 240)) { 
			this.overTime25 += 120;
			this.overTime50 += totalTimeForDay - EIGHT_HOUR_WORK_DAY_IN_MINUTES - 120;
		}
		else if (totalTimeForDay >= (EIGHT_HOUR_WORK_DAY_IN_MINUTES + 240)) {
			this.overTime25 += 120;
			this.overTime50 += 120;
			this.overTime100 += totalTimeForDay - EIGHT_HOUR_WORK_DAY_IN_MINUTES - 240;
		}
		else {
			// we should never get here
			System.err.println("calculateOvertimeTimeBrackets() : WE HAVE A BUG");
		}
		
	}

	/**
	 * Calculate amount of evening time per day
	 * 
	 */
	private void calculateEveningTimePerDay(List<WorkShift> worksPerDay) 
	{
		Iterator<WorkShift> li = worksPerDay.iterator();
		long totalEveningTime = 0;
		while (li.hasNext()) {
			WorkShift ws = (WorkShift) li.next();
			totalEveningTime += ws.getAmountOfEveningTime();
		}
		this.eveningTime += totalEveningTime;
	}
		
	/**
	 * Helper method for getting int hour of Date object
	 * @param startTime
	 * @return int hour
	 */
	private int getDate(Date startTime) {
		Calendar calendar = Calendar.getInstance();
		calendar.setTime(startTime);
		return calendar.get(Calendar.DAY_OF_YEAR);
	}
	
	
	/**
	 * Sort work shifts by day, just to be sure
	 */
	private void sortWorkshifts() {
		Collections.sort(workList, new Comparator<WorkShift>() {
		    public int compare(WorkShift w1, WorkShift w2) {
		        return w1.getDay().compareTo(w2.getDay());
		    }
		});
	}
	
	/**
	 * @return the id
	 */
	protected String getId() {
		return id;
	}
	/**
	 * @param id the id to set
	 */
	protected void setId(String id) {
		this.id = id;
	}
	/**
	 * @return the name
	 */
	protected String getName() {
		return name;
	}
	/**
	 * @param name the name to set
	 */
	protected void setName(String name) {
		this.name = name;
	}
	/**
	 * @return the salary
	 */
	protected String getSalary() {
		return salary;
	}
	/**
	 * @param salary the salary to set
	 */
	protected void setSalary(String salary) {
		this.salary = salary;
	}
	/**
	 * 
	 */
	protected void addWorkShift(WorkShift ws) {
		this.workList.add(ws);
	}
	/**
	 * 
	 */
	protected void addAllWorkShifts(List<WorkShift> workList) {
		this.workList = workList;
	}
	/**
	 * @return the normalTime
	 */
	protected long getNormalTime() {
		return normalTime;
	}
	/**
	 * @return the overTime
	 */
	protected long getOverTime() {
		return overTime;
	}
	/**
	 * @return the eveningTime
	 */
	protected long getEveningTime() {
		return eveningTime;
	}

	/**
	 * @return the overTime25
	 */
	protected long getOverTime25() {
		return overTime25;
	}

	/**
	 * @return the overTime50
	 */
	protected long getOverTime50() {
		return overTime50;
	}

	/**
	 * @return the overTime100
	 */
	protected long getOverTime100() {
		return overTime100;
	}

	/**
	 * @return the monthlyWage
	 */
	protected double getMonthlyWage() {
		return monthlyWage;
	}

	/**
	 * @return the regularHoursSalary
	 */
	protected double getRegularHoursSalary() {
		return regularHoursSalary;
	}

	/**
	 * @return the eveningHoursSalary
	 */
	protected double getEveningHoursSalary() {
		return eveningHoursSalary;
	}

	/**
	 * @return the totalOverTimeSalary
	 */
	protected double getTotalOverTimeSalary() {
		return totalOverTimeSalary;
	}

	/**
	 * @return the overTimeSalary25
	 */
	protected double getOverTimeSalary25() {
		return overTimeSalary25;
	}

	/**
	 * @return the overTimeSalary50
	 */
	protected double getOverTimeSalary50() {
		return overTimeSalary50;
	}

	/**
	 * @return the overTimeSalary100
	 */
	protected double getOverTimeSalary100() {
		return overTimeSalary100;
	}
	
}
