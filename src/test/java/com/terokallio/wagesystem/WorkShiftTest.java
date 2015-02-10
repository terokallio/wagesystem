/**
 * 
 */
package com.terokallio.wagesystem;

import static org.junit.Assert.*;

import java.util.Calendar;
import java.util.Date;

import org.junit.Test;

/**
 * @author terokallio
 *
 */
public class WorkShiftTest {

	/**
	 * Test ws.getTotalTimeInMinutes(day1, day2);
	 */
	@Test
	public void testTotalTimeInMinutes() {
		Calendar cal = Calendar.getInstance();
		cal.set(Calendar.HOUR_OF_DAY,18);
		cal.set(Calendar.MINUTE,0);
		cal.set(Calendar.SECOND,0);
		cal.set(Calendar.MILLISECOND,0);
	    Date day1 = cal.getTime();
	    
	    Calendar cal2 = Calendar.getInstance();
		cal2.set(Calendar.HOUR_OF_DAY,18);
		cal2.set(Calendar.MINUTE,10);
		cal2.set(Calendar.SECOND,0);
		cal2.set(Calendar.MILLISECOND,0);
		Date day2 = cal2.getTime();
		
		WorkShift ws = new WorkShift("Scott Scala", "2", "2.3.2014", 
				"6:00", "14:00");
		long time = ws.getTotalTimeInMinutes(day1, day2);
		assertEquals(10, time);
	}
	
	/**
	 * Test ws.parseStartTime(String day, String time) 
	 */
	@Test
	public void testParseStartTime() {
		Calendar cal = Calendar.getInstance();
		cal.set(Calendar.YEAR, 2014);
		cal.set(Calendar.MONTH, 0);
		cal.set(Calendar.DAY_OF_MONTH, 18);
		cal.set(Calendar.HOUR_OF_DAY, 18);
		cal.set(Calendar.MINUTE,19);
		cal.set(Calendar.SECOND,0);
		cal.set(Calendar.MILLISECOND,0);
	    Date day1 = cal.getTime();
	    
	    WorkShift ws = new WorkShift("Scott Scala", "2", "2.6.2014", 
				"6:00", "14:00");
	    Date day2 = ws.parseStartTime("18.1.2014", "18:19");
	    assertEquals(day1.getTime(), day2.getTime());
	}
	
	/**
	 * Test ws.parseEndTime(String day, String time) 
	 * 
	 */
	@Test
	public void testParseEndTime() {
		Calendar cal = Calendar.getInstance();
		cal.set(Calendar.YEAR, 2014);
		cal.set(Calendar.MONTH, 0);
		cal.set(Calendar.DAY_OF_MONTH, 18);
		cal.set(Calendar.HOUR_OF_DAY, 18);
		cal.set(Calendar.MINUTE,19);
		cal.set(Calendar.SECOND,0);
		cal.set(Calendar.MILLISECOND,0);
	    Date day1 = cal.getTime();
	    
	    WorkShift ws = new WorkShift("Scott Scala", "2", "2.6.2014", 
				"6:00", "14:00");
	    
	    Date day2 = ws.parseEndTime("18.1.2014", "18:10", "18:19");
	    assertEquals(day1.getTime(), day2.getTime());
	}
	/**
	 * Test ws.parseEndTime(String day, String time) 
	 * This endtime must be on the next day. So check for day update
	 */
	@Test
	public void testParseEndTime2() {
		Calendar cal = Calendar.getInstance();
		cal.set(Calendar.YEAR, 2014);
		cal.set(Calendar.MONTH, 0);
		cal.set(Calendar.DAY_OF_MONTH, 19);
		cal.set(Calendar.HOUR_OF_DAY, 0);
		cal.set(Calendar.MINUTE,19);
		cal.set(Calendar.SECOND,0);
		cal.set(Calendar.MILLISECOND,0);
	    Date day1 = cal.getTime();
	    
	    WorkShift ws = new WorkShift("Scott Scala", "2", "2.6.2014", 
				"6:00", "14:00");
	    
	    Date day2 = ws.parseEndTime("18.1.2014", "18:10", "00:19");
	    assertEquals(day1.getTime(), day2.getTime());
	}
	    
	/**
	 * Test ws.parseEndTime(String day, String time) 
	 * This endtime must be on the next day. So check for day update
	 */
	@Test
	public void testAdjustEndDate() {
		Calendar cal = Calendar.getInstance();
		cal.set(Calendar.YEAR, 2014);
		cal.set(Calendar.MONTH, 0);
		cal.set(Calendar.DAY_OF_MONTH, 19);
		cal.set(Calendar.HOUR_OF_DAY, 0);
		cal.set(Calendar.MINUTE,19);
		cal.set(Calendar.SECOND,0);
		cal.set(Calendar.MILLISECOND,0);
	    Date day1 = cal.getTime();
	    
	    WorkShift ws = new WorkShift("Scott Scala", "2", "2.6.2014", 
				"6:00", "14:00");
	    
	    Date day2 = ws.adjustEndDate(day1);
	    // lets double check the time was changed already
	    assertNotEquals(day1.getTime(), day2.getTime());
	    
	    // lets add a day to Date 
	    Calendar cal2 = Calendar.getInstance();
	    cal2.setTime(day1);
	    cal2.add(Calendar.DATE, 1);
	    Date day3 = cal2.getTime();
	    
	    // now we should be on the same time
	    assertEquals(day3.getTime(), day2.getTime());
	}
	    
	
	
	/**
	 * TestCheckDaySwitch
	 * This tests for false operation
	 */
	@Test
	public void testCheckDaySwitch() {
		String startTime2 = "11:00";
		String endTime2 = "12:00";
		
		WorkShift ws = new WorkShift("Scott Scala", "2", "2.6.2014", 
				"6:00", "14:00");
	
		boolean switched = ws.checkDaySwitch(startTime2, endTime2);
		assertNotEquals(true, switched);
	}

	/**
	 * TestCheckDaySwitch
	 * This tests for true operation
	 */
	@Test
	public void testCheckDaySwitch2() {
		String startTime2 = "24:00";
		String endTime2 = "12:00";
		
		WorkShift ws = new WorkShift("Scott Scala", "2", "2.6.2014", 
				"6:00", "14:00");
	
		boolean switched = ws.checkDaySwitch(startTime2, endTime2);
		assertEquals(true, switched);
	}
	
	/**
	 * TestGetAmountOfEveningTime() 
	 * 
	 */
	@Test
	public void testGetAmountOfEveningTime() {
		WorkShift ws = new WorkShift("Scott Scala", "2", "2.6.2014", 
				"17:50", "19:02");
		long eveningTime = ws.getAmountOfEveningTime();
		long expected = 62;
		assertEquals(expected, eveningTime);
	}
	
	/**
	 * TestGetAmountOfEveningTime() 
	 * Test both times outside 6:00 - 18:00
	 */
	@Test
	public void testGetAmountOfEveningTime2() {
		WorkShift ws = new WorkShift("Scott Scala", "2", "2.6.2014", 
				"19:50", "21:54");
		long eveningTime = ws.getAmountOfEveningTime();
		long expected = 124;
		assertEquals(expected, eveningTime);
	}
	
	/**
	 * TestGetAmountOfEveningTime() 
	 * Test both times inside 6:00 - 18:00
	 */
	@Test
	public void testGetAmountOfEveningTime3() {
		WorkShift ws = new WorkShift("Scott Scala", "2", "2.6.2014", 
				"11:50", "14:54");
		long eveningTime = ws.getAmountOfEveningTime();
		long expected = 0;
		assertEquals(expected, eveningTime);
	}
	
	/**
	 * TestGetAmountOfEveningTime() 
	 * Test start time before 6:00 - 18:00
	 */
	@Test
	public void testGetAmountOfEveningTime4() {
		WorkShift ws = new WorkShift("Scott Scala", "2", "2.6.2014", 
				"05:50", "06:51");
		long eveningTime = ws.getAmountOfEveningTime();
		long expected = 10;
		assertEquals(expected, eveningTime);
	}
	
	/**
	 * TestGetAmountOfEveningTime() 
	 * Test over the time 6:00 - 18:00
	 */
	@Test
	public void testGetAmountOfEveningTime5() {
		WorkShift ws = new WorkShift("Scott Scala", "2", "2.6.2014", 
				"05:50", "18:11");
		long eveningTime = ws.getAmountOfEveningTime();
		long expected = 21;
		assertEquals(expected, eveningTime);
	}
	
	/**
	 * TestGetAmountOfEveningTime() 
	 * Test over the time 18:00 - 19:00
	 */
	@Test
	public void testGetAmountOfEveningTime6() {
		WorkShift ws = new WorkShift("Scott Scala", "2", "2.6.2014", 
				"18:50", "19:11");
		long eveningTime = ws.getAmountOfEveningTime();
		long expected = 21;
		assertEquals(expected, eveningTime);
	}
	/**
	 * TestGetAmountOfEveningTime() 
	 * Test over the time 6:00 - 18:00
	 */
	@Test
	public void testGetAmountOfEveningTime7() {
		WorkShift ws = new WorkShift("Scott Scala", "2", "2.6.2014", 
				"06:00", "18:00");
		long eveningTime = ws.getAmountOfEveningTime();
		long expected = 0;
		assertEquals(expected, eveningTime);
	}
	
	/**
	 * TestGetNormalMinutes() 
	 * Test over the time 6:00 - 18:00
	 */
	@Test
	public void testGetNormalMinutes() {
		WorkShift ws = new WorkShift("Scott Scala", "2", "2.6.2014", 
				"06:00", "18:00");
		long eveningTime = ws.getNormalMinutes();
		long expected = 720;
		assertEquals(expected, eveningTime);
	}
	
	/**
	 * TestGetNormalMinutes() 
	 * Test over the time 05:00 - 18:00
	 */
	@Test
	public void testGetNormalMinutes2() {
		WorkShift ws = new WorkShift("Scott Scala", "2", "2.6.2014", 
				"05:05", "19:00");
		long eveningTime = ws.getNormalMinutes();
		long expected = 720;
		assertEquals(expected, eveningTime);
	}
	
	/**
	 * TestGetNormalMinutes() 
	 * Test over the time 05:00 - 18:00
	 */
	@Test
	public void testGetNormalMinutes3() {
		WorkShift ws = new WorkShift("Scott Scala", "2", "2.6.2014", 
				"17:00", "18:02");
		long eveningTime = ws.getNormalMinutes();
		long expected = 60;
		assertEquals(expected, eveningTime);
	}
}
