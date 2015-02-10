/**
 * 
 */
package com.terokallio.wagesystem;

import java.io.FileNotFoundException;
import java.util.ArrayList;
import java.util.Collection;
import java.util.Iterator;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

/**
 * Code problem 
 * Monthly Wage Calculation System
 * 
 * @author terokallio
 * @version 0.1
 * 
 */
public class WageSystem {

	public String csvFile = null;
	
	/**
	 * Application main
	 * Takes CSV filename as an argument, 
	 * if not given will use "HourList201403.csv"
	 * 
	 * Will print results to System.out
	 * 
	 * @param args
	 */
	public static void main(String[] args) {
		String csvFile = null;
		if (args.length > 0) {
			// use the first param as filename specifier
			csvFile = args[0];
		}
		
		// lets define the name, if not defined yet
		if (csvFile == null) {
			csvFile = "HourList201403.csv";
		}
		
		WageSystem obj = new WageSystem();
		obj.run(csvFile);
	}

	/**
	 * Application run logic
	 * 1. read CSV file
	 * 2. assign work shifts to employees
	 * 3. print results
	 * 
	 * @param String CSV file
	 * @return void
	 */
	public void run(String csvFile) {
		
		// read the work shift file, and get work shifts
		Collection<WorkShift> wsc = readCVSFile(csvFile); 
		
		// lets loop thru work shifts, and assign those to Employees. 
        // lets group all works by workerId
        Map<Integer, List<WorkShift>> WorkShiftByWorkerId = wsc
        	    .stream()
        	    .collect(Collectors.groupingBy(work -> work.workerId));
        WorkShiftByWorkerId
        	.forEach((workerId, work) -> System.out.println(""));
        	//System.out.format("workerId %s: %s\n",workerId, work.toString()));
            
        // check number of workers
        int numberOfWorkers = WorkShiftByWorkerId.size();
        // System.out.println("#workers: "+numberOfWorkers);
                
        // and assign work shifts to Employees. 
        List<Employee> ec = new ArrayList<>(numberOfWorkers);
        int i = 0;
        while ( i < numberOfWorkers ) {
        	WorkShift ws = (WorkShift)WorkShiftByWorkerId.get(i+1).get(1);
        	Employee e = new Employee(Integer.toString(ws.workerId), 
        			ws.workerName,WorkShiftByWorkerId.get(i+1));
        	ec.add(i, e);
        	i++;
        }
        
        // print results
        // now prints to System.out
        printEmployeeInfo(ec);

	}

	/**
	 * Method utilized CVSFileReader to get Collection of WorkShifts
	 * @param String filename
	 * @return Collection of WorkShifts
	 */
	public Collection<WorkShift> readCVSFile(String file) {
		Collection<WorkShift> wsc = new ArrayList<WorkShift>(); 
		try {
			// lets init CSVFileReader
			CSVFileReader reader = new CSVFileReader(file);
			// and lets read the work shifts
			wsc = reader.readFileContents();
		} catch (FileNotFoundException e) {
			e.printStackTrace();
		} catch (Exception e) {
			e.printStackTrace();
		}
		return wsc;
	}
	
	/**
	 * Print Employee information 
	 * Prints to System.out
	 * 
	 * @param List<Employee> ec
	 */
	private void printEmployeeInfo(List<Employee> ec) {
		Iterator<Employee> it = ec.iterator();
		
		while (it.hasNext()) {
			Employee employee = (Employee) it.next();
			
			System.out.println("Employee id: " + employee.getId());
			System.out.print(" Name: "+ employee.getName());
			System.out.println (" Normaltime: " + employee.getNormalTime());
			System.out.print(" EveningTime: " + employee.getEveningTime());
			System.out.print(" Overtime: " + employee.getOverTime());
			
			System.out.print(" Overtime 25%: " + employee.getOverTime25());
			System.out.print(" Overtime 50%: " + employee.getOverTime50());
			System.out.print(" Overtime 100%: " + employee.getOverTime100());
			System.out.println("");
			
			System.out.print(" Wage Overtime 25%: $" + employee.getOverTimeSalary25());
			System.out.print(" Wage Overtime 50%: $" + employee.getOverTimeSalary50());
			System.out.print(" Wage Overtime 100%: $" + employee.getOverTimeSalary100());
			System.out.println(" Overtime wage total: $" +employee.getTotalOverTimeSalary());
			System.out.println(""); 

			System.out.print(" Reqular hours salary: $" + employee.getRegularHoursSalary());
			System.out.print(" Evening time salary: $" + employee.getEveningHoursSalary());
			System.out.println(" Monthly salary: $" + employee.getMonthlyWage());
			System.out.println("");
		}
	}
}
