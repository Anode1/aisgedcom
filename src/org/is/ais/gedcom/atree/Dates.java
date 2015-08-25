package org.is.ais.gedcom.atree;

import java.util.ArrayList;
import java.util.StringTokenizer;

import org.is.ais.gedcom.DateUtil;

public class Dates {
	
	
	public static boolean isGoodDate(String string){
		string=string.trim();
		StringTokenizer st = new StringTokenizer(string, " ", false);
		
		ArrayList list=new ArrayList(3);
	    while (st.hasMoreTokens()) {
	    	list.add(st.nextToken());
	    }
	    int n=list.size();
		
		if(n==1){  //1826 or ?
			return isYear((String)list.get(0));
		}
		else if(n==2){ //MAY 1889 (12 months), and also ABT 1810, BEF 1816, AFT 1834, EST 1000
			String first=(String)list.get(0);
			
			if(!first.equals("?") && !first.equals("ABT") && !first.equals("BEF") && !first.equals("AFT") && !first.equals("EST") &&
					DateUtil.MONTHS.get(first)==null){
				return false; //bad month
			}
			//here month is good so we just check the year part
			return isYear((String)list.get(1));
		}
		else if(n==3){ //5 APR 1888
			String date=(String)list.get(0);
			if(date.length()>2)
				return false;
			
			String month=(String)list.get(1);
			if(!month.equals("?") && DateUtil.MONTHS.get(month)==null)
				return false;
			
			return isYear((String)list.get(2));
		}
		return false;
	}
	
	
	private static  boolean isYear(String string){
		
		if(string.equals("?"))
			return true;
		
		if(string.length()!=4)
			return false;
		for(int i=0; i<4; i++){
			if(!Character.isDigit(string.charAt(i))){
				return false;
			}
		}
		return true;
	}
	
}
