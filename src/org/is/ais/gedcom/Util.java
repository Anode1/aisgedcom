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

import java.util.Stack;
import java.util.StringTokenizer;

public class Util {

	
	public static long lines2binary(String familyLines){
		int length=familyLines.length();
		if(length<1)throw new NullPointerException("Unexpected: familyLine should be of at least 1 letter length");
		long n=1;
		for(int i=1;i<length;i++){
			n<<=1;
			n|=(familyLines.charAt(i)=='F'?1:0);
		}
		return n;
	}
	
	
	public static boolean isPointer(String s){
		if(s.startsWith("@") && s.endsWith("@"))
			return true;
		return false;
	}
	
	
	public static String removeSuffixes(String str, int n) throws Exception{
		int lastIndex=findLastIndexOf(str, Constants.DELIM, n);
		if(lastIndex==-1)
			throw new Exception("Unexpected index: "+n+" in string "+str);
		return str.substring(0, lastIndex);
	}
	
	
	public static String[] tokenize(String str, String delim){

		StringTokenizer st = new StringTokenizer(str, delim, false);
		int n=st.countTokens();
		String[] toks = new String[n];
		for(int i=0; i<n; i++){
			toks[i]=st.nextToken().trim();
		}
		return toks;
	}
	
	
	public static int findLastIndexOf(String str, String delim, int n) throws Exception{

		int currentInd=0;
		for(int i=0; i<n; i++){
			int cur;
			if(i==0)
				cur=str.indexOf(delim);
			else
				cur=str.indexOf(delim, currentInd+delim.length());
			if(cur==-1)
				throw new Exception("Reached the end and n is less than tokens");
			currentInd=cur;
		}
		return currentInd;
	}	
	
	
	public static String stack2String(Stack stack){
		Object[] strings = (Object[])stack.toArray();
		StringBuffer sb=new StringBuffer();
		for(int i=0; i<strings.length; i++)
			sb.append(strings[i]);
		
		return sb.toString();
	}
	
	
	/**
	 * Removes redundant letters at the end of the path. See unit tests for examples
	 */
	public static String path2familyLine(String path){
		int pathLength = path.length();
		if(pathLength<1)
			throw new ArrayIndexOutOfBoundsException("Unexpected path length: should be at least 1");
		int i=pathLength-1;
		while(i>0 && path.charAt(i)==path.charAt(i-1)){
			i--;
		}
		return path.substring(0, i+1);
	}
	
	
	public static String familyLineWithoutSubject(String s){
		int length=s.length();
		if(length<1)
			return s;
		if(length==1)
			return s; //M->M, F->F
		
		return s.substring(1, length);
	}
	
	
	public static String iso8859toUTF8(String isoString) throws Exception{
	
		byte[] latinBytes = isoString.getBytes("ISO-8859-1");
		return new String(latinBytes, "UTF-8");
	}
	
	
	/**
	 * wrap into @ - if necessary (return null if already ok)
	 */
	public static String wrapIdIfNecessary(String id) throws Exception{
		
		if(id.startsWith("@")){
			if(id.endsWith("@")){
				return null;
			}
			else{
				return id+"@";
			}
		}
		else{
			if(id.endsWith("@")){
				return "@"+id;
			}
			else{
				return "@"+id+"@";
			}
		}
	}
	
	
	public static String outputFileFromInput(String input){
		/*
	    Calendar cal = Calendar.getInstance();
		SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd-HH-mm-ss");
		String postfix=sdf.format(cal.getTime());
		*/
		String postfix="_out";
		
		String[] withoutExt = input.split("\\.", 2);
		if(withoutExt.length==1){
			return input+postfix;
		}
		else{
			return withoutExt[0]+postfix+"."+withoutExt[1];
		}

	}	


}
