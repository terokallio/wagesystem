package com.terokallio.wagesystem;

import static org.junit.Assert.*;

import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;

import org.junit.After;
import org.junit.AfterClass;
import org.junit.Before;
import org.junit.BeforeClass;
import org.junit.Test;

public class EmployeeTest {

	@BeforeClass
	public static void setUpBeforeClass() throws Exception {
	}

	@AfterClass
	public static void tearDownAfterClass() throws Exception {
	}

	@Before
	public void setUp() throws Exception {
	}

	@After
	public void tearDown() throws Exception {
	}

	/**
	 * Test correct work times (normal hours);
	 */
	@Test
	public void testTotalTimeInMinutes() {
		ArrayList<WorkShift> wsc = new ArrayList<>();
		WorkShift ws1 = new WorkShift("Scott Scala", "2", "2.3.2014", 
				"6:00", "7:00");
		WorkShift ws2 = new WorkShift("Scott Scala", "2", "2.3.2014", 
				"17:00", "18:00");
		wsc.add(ws1);
		wsc.add(ws2);
		
    	Employee e = new Employee(Integer.toString(ws1.workerId), 
    			ws1.workerName, wsc);
    			
    	
    	assertEquals(120, e.getNormalTime());
    	
    	// System.out.println("e: " + e.getNormalTime());
		// assertLongEquals(7.50, e.getMonthlyWage());
	}
	
	/**
	 * Test salary calculations  (evening hours);
	 */
	@Test
	public void testEveningWage() {
		ArrayList<WorkShift> wsc = new ArrayList<>();
		WorkShift ws1 = new WorkShift("Scott Scala", "2", "2.3.2014", 
				"18:00", "19:00");
		WorkShift ws2 = new WorkShift("Scott Scala", "2", "2.3.2014", 
				"20:00", "21:00");
		wsc.add(ws1);
		wsc.add(ws2);
		
		Employee e = new Employee(Integer.toString(ws1.workerId), 
    			ws1.workerName, wsc);
    	
    	// normal time now 0
    	assertEquals(0, e.getNormalTime());
    	
    	// evening time 120
    	assertEquals(120, e.getEveningTime());
    	
    	// evening wage for 2 hours = 3,75 * 2 + 2 * 1.75 = 9.8
    	// System.out.println("e: " + e.getEveningHoursSalary());
    	
    	assertEquals(9.8, e.getEveningHoursSalary(), 2);
    	assertEquals(9.8, e.getMonthlyWage(), 2);
    	// overtime should 0 
    	assertEquals(0, e.getOverTimeSalary50(), 2);

	}
	
	
	/**
	 * Test salary payments;
	 */
	@Test
	public void testSalaryPayments() {
		ArrayList<WorkShift> wsc = new ArrayList<>();
		WorkShift ws1 = new WorkShift("Scott Scala", "2", "2.3.2014", 
				"17:00", "19:00");
		WorkShift ws2 = new WorkShift("Scott Scala", "2", "2.3.2014", 
				"20:00", "22:00");
		wsc.add(ws1);
		wsc.add(ws2);
		
		Employee e = new Employee(Integer.toString(ws1.workerId), 
    			ws1.workerName, wsc);
    	
    	// normal time now 60
    	assertEquals(60, e.getNormalTime());
    	
    	// evening time 180
    	assertEquals(180, e.getEveningTime());
    	
    	// evening wage for 3 hours = 3,75 * 3 + 3 * 1.75 = 14.7
    	assertEquals(14.7, e.getEveningHoursSalary(), 2);
    	
    	// overtime 0 hours
    	assertEquals(0, e.getOverTimeSalary50(), 2);
    	
    	// regular hours 1
    	assertEquals(3.75, e.getRegularHoursSalary(), 2);
    	
    	// 3.75 + 14.7
    	assertEquals(18.45, e.getMonthlyWage(), 2);
    	
    	// overtime should 0 
    	assertEquals(0, e.getOverTimeSalary50(), 2);

	}

	/**
	 * Test salary payments;
	 */
	@Test
	public void testSalaryPayments2() {
		ArrayList<WorkShift> wsc = new ArrayList<>();
		WorkShift ws1 = new WorkShift("Scott Scala", "2", "2.3.2014", 
				"06:00", "19:00");
		WorkShift ws2 = new WorkShift("Scott Scala", "2", "2.3.2014", 
				"20:00", "21:00");
		wsc.add(ws1);
		wsc.add(ws2);
		
		Employee e = new Employee(Integer.toString(ws1.workerId), 
    			ws1.workerName, wsc);
    	
    	// normal time now 480 aka 8h
    	assertEquals(480, e.getNormalTime());
    	
    	// evening time 120
    	assertEquals(120, e.getEveningTime());
    	
    	// evening wage for 2 hours = 3,75 * 2 + 2 * 1.75 = 9.8
    	assertEquals(9.8, e.getEveningHoursSalary(), 2);
    	
    	// overtime 6 hours / 2 hours here ->
    	assertEquals(120, e.getOverTime25());
    	
    	// overtime 6 hours / 2 hours also here ->
    	assertEquals(120, e.getOverTime50());
    	
    	// overtime 6 hours / 2 hours also here ->
    	assertEquals(120, e.getOverTime100());
    	
    	// overtime 6 hours / 2 hours here 25&
    	assertEquals(9.375, e.getOverTimeSalary25(), 1);
    	
    	// overtime 6 hours / 2 hours here 50&
    	assertEquals(11.25, e.getOverTimeSalary50(), 1);
    	
    	// overtime 6 hours / 2 hours here 100&
    	assertEquals(15.00, e.getOverTimeSalary100(), 1);
    	
    	// regular hours 8 * 3.75 =30
    	assertEquals(30.0, e.getRegularHoursSalary(), 1);
    	
    	// 30 + 9.8 + 9.375 + 11.25 + 15
    	assertEquals(75.425, e.getMonthlyWage(), 1);
    	

	}
}
