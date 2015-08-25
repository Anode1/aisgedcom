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
import java.util.ArrayList;

import java.util.Calendar;
import java.util.Hashtable;
import java.util.Stack;


/**
 * We store some information between reports, so should be careful - not to reuse it
 */
public class Node {

	private String id;
	public Hashtable data=new Hashtable();
	private String path=""; //only for direct ancestors
	private int distance; //only for descending lines (children, grand-children etc - of direct ancestors)
	public int year=Integer.MIN_VALUE; //MIN - uninitialize, 0 - unknown
	
	
	public Node(String id){
		this.id=id;
	}
	
	
	public String getId(){
		return id;
	}
	
	
	/**
	 * Get year (also cache it for future) 
	 */
	public int getYear(){
		if(year==Integer.MIN_VALUE){ //if not initialized - parse and initialize
			String birthDate = (String)data.get("INDI.BIRT.DATE");
			if(birthDate==null){
				year=0;
			}
			else{
				year=DateUtil.parseYear(birthDate, true);
			}
		}
		return year;
	}
	
	
	/**
	 * Get generations path
	 */
	public String getPath(){
		return path.toString();
	}
	
	
	public void setPath(String path){
		this.path=path;
	}
	
	
	/**
	 * Adds distance in generations
	 */
	public void setDistance(int i){
		distance=i;
	}
	
	
	/**
	 * Gets distance in generations
	 */
	public int getDistance(){
		return distance;
	}
	
	
	public boolean isDirectAncestor(){
		return path.length()>0;
	}
	
	
	/**
	 * For debugging purposes only
	 */
	public String toString(){
		StringBuffer sb = new StringBuffer();
		sb.append(id);
		sb.append(", path="+path);
		sb.append(", name="+Gedcom.getName(this));
		sb.append(", distance="+distance);

		return sb.toString();
	}
}
