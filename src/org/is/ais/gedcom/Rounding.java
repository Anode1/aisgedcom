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

import java.math.BigDecimal;

public class Rounding {


	public static java.text.DecimalFormat decimal0Format = new java.text.DecimalFormat("##0");
	public static java.text.DecimalFormat decimal1Format = new java.text.DecimalFormat("##0.0");
	public static java.text.DecimalFormat decimal2Format = new java.text.DecimalFormat("##0.00");
	public static java.text.DecimalFormat decimal4Format = new java.text.DecimalFormat("##0.0000");	
	
	
	public static double round1(double num) {
		double result = num * 10.0;
		result = Math.round(result);
		result = result / 10.0;
		return result;
	}	
	

	public static double round(double d, int c) {
		double temp=Math.round(d*Math.pow(10,c));
		return temp/Math.pow(10,c);
	}
	
	
	public static int round0Decimals(double d) {
    	String formatted = decimal0Format.format(d);
    	return Integer.parseInt(formatted);
	}	
	
	
	public static double round1Decimals(double d) {
    	String formatted = decimal1Format.format(d);
    	return Double.valueOf(formatted).doubleValue();
	}

	
	public static double round2Decimals(double d) {
    	String formatted = decimal2Format.format(d);
    	return Double.valueOf(formatted).doubleValue();
	}
	
	
	public static double round4Decimals(double d) {
    	String formatted = decimal4Format.format(d);
    	return Double.valueOf(formatted).doubleValue();
	}
	
	
	public static double roundBigDecimals(double d, int c){
		BigDecimal bd = new BigDecimal(d);
		bd = bd.setScale(10, BigDecimal.ROUND_HALF_UP); //this is a hack - to avoid inconsistencies of rounding between java and SAS
		bd = bd.setScale(c, BigDecimal.ROUND_HALF_UP);
		return bd.doubleValue();
	}
}
