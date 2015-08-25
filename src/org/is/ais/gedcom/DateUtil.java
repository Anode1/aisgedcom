/*
 	Copyright (C) 2011 Vasili Gavrilov

    This program is free software: you can redistribute it and/or modify
    it under the terms of the GNU General Public License as published by
    the Free Software Foundation, either version 3 of the License, or
    (at your option) any later version.

    This program is distributed in the hope that it will be useful,
    but WITHOUT ANY WARRANTY; without even the implied warranty of
    MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the
    GNU General Public License for more details.

    You should have received a copy of the GNU General Public License
    along with this program.  If not, see <http://www.gnu.org/licenses/>.
*/
package org.is.ais.gedcom;

import java.util.Calendar;

import java.util.Date;
import java.util.HashMap;


public class DateUtil {

	public static int thisYear=new Date().getYear()+1900;
	//private static java.text.SimpleDateFormat format1 = new java.text.SimpleDateFormat("dd MMM yyyy");
	//private static java.text.SimpleDateFormat format2 = new java.text.SimpleDateFormat("MMM yyyy");
	//private static java.text.SimpleDateFormat format3 = new java.text.SimpleDateFormat("yyyy");
	
	public final static HashMap MONTHS=new HashMap();
	static{
		MONTHS.put("JAN", 1);		MONTHS.put("FEB", 2);		MONTHS.put("MAR", 3);		MONTHS.put("APR", 4);
		MONTHS.put("MAY", 5);		MONTHS.put("JUN", 6);		MONTHS.put("JUL", 7);		MONTHS.put("AUG", 8);
		MONTHS.put("SEP", 9);		MONTHS.put("OCT", 10);		MONTHS.put("NOV", 11);		MONTHS.put("DEC", 12);
	}	

	/**
	 * Interval in years between 2 dates been represented as Calendars
	 */
	/*
	public static int getFullYears(Calendar cal1, Calendar cal2){
		if(cal1==null || cal2==null || cal2.before(cal1))
			return -1;
			//throw new Exception(date1 + " is earlier than " + date2);
		int days1 = cal1.get(java.util.Calendar.DAY_OF_YEAR);
		int days2 = cal2.get(java.util.Calendar.DAY_OF_YEAR);
		boolean lessThanYear = days1>days2;
		int years = cal2.get(java.util.Calendar.YEAR) - cal1.get(java.util.Calendar.YEAR); 
		if(lessThanYear) years--;
		return years;
	}
	*/
	
	/**
	 * Interval in years between 2 dates 
	 */
	/*
	public static int getFullYears(Date date1, Date date2){
		Calendar cal1 = Calendar.getInstance();
		Calendar cal2 = Calendar.getInstance();
		cal1.setTime(date1);
		cal2.setTime(date2);
		return getFullYears(cal1, cal2);
	}
	*/
	
	/**
	 * Interval in days between 2 dates been represented as Calendars
	 */
	/*
	public static int getDays(Calendar startDate, Calendar endDate){

		//if(cal2.before(cal1))
		//	throw new Exception(date1 + " is earlier than " + date2);
	    //Calendar sDate = (Calendar) startDate.clone(); 
		int days = 0;
		while (startDate.before(endDate)) {  
			startDate.add(Calendar.DAY_OF_MONTH, 1);  
		    days++;  
		}
		return days;  
	}
	*/
	
	/*
	public static double getYears(Calendar startDate, Calendar endDate){
		int days=getDays(startDate, endDate);
		double years = days/365.25;
		return years;
	}*/
	
	
	/**
	 * Interval in months between 2 dates been represented as Calendars
	 */
	/*
	public static int getMonths(Calendar startDate, Calendar endDate){

		//if(cal2.before(cal1))
		//	throw new Exception(date1 + " is earlier than " + date2);
	    //Calendar sDate = (Calendar) startDate.clone(); 
		int months = 0;
		while (startDate.before(endDate)) {  
			startDate.add(Calendar.MONTH, 1);  
		    months++;  
		}
		return months-1;  
	}*/	
	

	/*
	public static int monthsBetween(Calendar startDate, Calendar endDate){
		
		int date1=startDate.get(Calendar.DATE);
		int month1=startDate.get(Calendar.MONTH);
		int year1=startDate.get(Calendar.YEAR);
		int date2=endDate.get(Calendar.DATE);
		int month2=endDate.get(Calendar.MONTH);
		int year2=endDate.get(Calendar.YEAR);
		return ((year2-year1)*12) + (month2-month1) + ((date2>=date1)?1:0)-1;
	} 
	*/
	
	/*
	public static long daysBetween(Calendar startDate, Calendar endDate) {  
	    //Calendar sDate = (Calendar) startDate.clone(); 
		Calendar sDate = startDate;
	    long daysBetween = 0;  
	  
	    int y1 = startDate.get(Calendar.YEAR);  
	    int y2 = endDate.get(Calendar.YEAR);  
	    int m1 = startDate.get(Calendar.MONTH);  
	    int m2 = endDate.get(Calendar.MONTH);  
	  
	    while (((y2 - y1) * 12 + (m2 - m1)) > 12) {  
	  
	        //move to Jan 01  
	        if ( sDate.get(Calendar.MONTH) == Calendar.JANUARY  
	             && sDate.get(Calendar.DAY_OF_MONTH) == sDate.getActualMinimum(Calendar.DAY_OF_MONTH)) {  
	  
	            daysBetween += sDate.getActualMaximum(Calendar.DAY_OF_YEAR);  
	            sDate.add(Calendar.YEAR, 1);  
	        } else {  
	            int diff = 1 + sDate.getActualMaximum(Calendar.DAY_OF_YEAR) - sDate.get(Calendar.DAY_OF_YEAR);  
	            sDate.add(Calendar.DAY_OF_YEAR, diff);  
	            daysBetween += diff;  
	        }  
	        y1 = sDate.get(Calendar.YEAR);  
	    }  
	  
	    //** optimize for month **  
	    //while the difference is more than a month, add a month to start month  
	    while ((m2 - m1) % 12 > 1) {  
	        daysBetween += sDate.getActualMaximum(Calendar.DAY_OF_MONTH);  
	        sDate.add(Calendar.MONTH, 1);  
	        m1 = sDate.get(Calendar.MONTH);  
	    }  
	  
	    // process remainder date  
	    while (sDate.before(endDate)) {  
	        sDate.add(Calendar.DAY_OF_MONTH, 1);  
	        daysBetween++;  
	    }  
	  
	    return daysBetween;  
	}  */
	
	
	/**
	 * Interval in days between 2 dates
	 */
	/*
	public static int getDays(Date date1, Date date2){
		
		Calendar cal1 = Calendar.getInstance();
		Calendar cal2 = Calendar.getInstance();		
		cal1.setTime(date1);
		cal2.setTime(date2);
		return getDays(cal1, cal2);
	}*/
	
	
	/**
	 * Removes GEDCOM-specific fixed tag from the date string - if present (not needed)
	 */
	public static String cleanGedcomDate(String s){
		if(s!=null && s.startsWith("@#DJULIAN@ ")){
			return s.substring(11, s.length());
		}
		return s;
	}

	
	/*
	 * Parse year. We do not use it in the code (only in tests - to check Calendar)
	 */
	/*
	public static int getYear(String dateString, boolean ignoreAbouts){
		Calendar cal=parseDate(dateString, ignoreAbouts);
		if(cal==null)
			return 0;
		return cal.get(Calendar.YEAR);
	}*/
	
/*
	public static Calendar parseDate(String date, boolean ignoreAbouts){		
		try{
			int length=date.length();
			if(date==null || length<4)
				return null;

			Calendar cal = Calendar.getInstance();
			
			if(length==10){	// 5 APR 1888
				//return format1.parse(date);
				if(date.charAt(1)!=' '||date.charAt(5)!=' '){
					return null;
				}
				int day=Integer.parseInt(date.substring(0, 1));
				int month=(Integer)MONTHS.get(date.substring(2, 5));
				int year=Integer.parseInt(date.substring(6));
				cal.set(year, month-1, day);
				return cal;
			}
			else if(length==11){	//12 MAY 1789
				//return format1.parse(date);
				if(date.charAt(2)!=' '||date.charAt(6)!=' '){
					return null;
				}
				int day=Integer.parseInt(date.substring(0, 2));
				int month=(Integer)MONTHS.get(date.substring(3, 6));
				int year=Integer.parseInt(date.substring(7));
				cal.set(year, month-1, day);
				return cal;
			}			
			else if(length==4){ //1826
				int year=Integer.parseInt(date);
				//set middle of the year (July 1)
				cal.set(year, 6, 1);
				return cal;
			}
			else if(length==8){ //MAY 1889 			ABT 1810  				BEF 1816 				AFT 1834
				int year=Integer.parseInt(date.substring(4));
				String monthLetters=date.substring(0, 3);
				Integer month=(Integer)MONTHS.get(monthLetters);
				if(month!=null){ //recognized
					cal.set(year, month-1, 15);
				}
				else if(date.startsWith("BEF")){
					if(ignoreAbouts)
						return null;
					cal.set(year, 0, 1); // JAN 1
				}
				else if(date.startsWith("ABT")){
					if(ignoreAbouts)
						return null;					
					cal.set(year, 6, 1); // JUL 1
				}
				else if(date.startsWith("AFT")){
					if(ignoreAbouts)
						return null;					
					cal.set(year, 11, 31); // DEC 31
				}
				else if(date.startsWith("EST")){
					if(ignoreAbouts)
						return null;					
					cal.set(year, 6, 1); // JUL 1
				}				
				return cal;
			}
			else if(date.startsWith("BEF") || date.startsWith("BET") || date.startsWith("ABT") || date.startsWith("AFT")){
				if(ignoreAbouts)
					return null;
				int len = date.length();
				int year=Integer.parseInt(date.substring(len-4));
				cal.set(year, 6, 1); // JUL 1
				return null;
			}
			else{
				System.out.println("Unrecognized date (this format is not supported): "+date);
				return null;
			}
		}
		catch(Exception e){
			System.out.println("Unrecognized date (this format is not supported): "+date+ " exception: " + e);
			return null;
		}
	}
	*/
	
	
	public static int year2Century(int year){
		return 1+year/100; 
	}
	
	
	public static int parseYear(String date, boolean ignoreAbouts){		
		try{
			int length=date.length();
			if(date==null || length<4)
				return 0;
			
			if(length==10){	// 5 APR 1888
				if(date.charAt(1)!=' '||date.charAt(5)!=' '){
					return 0;
				}
				return Integer.parseInt(date.substring(6));
			}
			else if(length==11){	//12 MAY 1789
				if(date.charAt(2)!=' '||date.charAt(6)!=' '){
					return 0;
				}
				return Integer.parseInt(date.substring(7));
			}			
			else if(length==4){ //1826
				return Integer.parseInt(date);
			}
			else if(length==8){ //MAY 1889 			ABT 1810  				BEF 1816 				AFT 1834
				int year=Integer.parseInt(date.substring(4));
				String monthLetters=date.substring(0, 3);
				Integer month=(Integer)MONTHS.get(monthLetters);
				if(month!=null){ //month recognized, so it is not BEF,ABT,AFT,EST
					return year;
				}
				else if(date.startsWith("BEF")){
					if(ignoreAbouts)
						return 0;
					return year;
				}
				else if(date.startsWith("ABT")){
					if(ignoreAbouts)
						return 0;					
					return year;
				}
				else if(date.startsWith("AFT")){
					if(ignoreAbouts)
						return 0;					
					return year;
				}
				else if(date.startsWith("EST")){
					if(ignoreAbouts)
						return 0;					
					return year;
				}
				else{
					System.out.println("Unrecognized date (this format is not supported): "+date);
					return 0;
				}
			} //unknown length
			else if(date.startsWith("BEF") || date.startsWith("BET") || date.startsWith("ABT") || date.startsWith("AFT")){
				if(ignoreAbouts)
					return 0;
				int len = date.length();
				int year=Integer.parseInt(date.substring(len-4));
				return year;
			}
			else{
				System.out.println("Unrecognized date (this format is not supported): "+date);
				return 0;
			}
		}
		catch(Exception e){
			System.out.println("Unrecognized date (this format is not supported): "+date+ " exception: " + e);
			return 0;
		}
	}	
	
	
}
