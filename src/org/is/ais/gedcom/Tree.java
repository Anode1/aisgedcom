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
import java.util.Hashtable;


/**
 * Main tree left after GEDCOM parsing. We take data from here 
 */
public class Tree {

	//public Hashtable is = new Hashtable(); //nodes indexed by ID
	//public Node mainNode = null;
	
	public Hashtable indiIds = new Hashtable(); //nodes indexed by ID
	public Hashtable famIds = new Hashtable();
	
	private Node currentRecord;
	
	
	public void addNode(int currentLevel, String recordId, String currentPrefix, String pointer, String theRest){
		//System.out.println("Adding " + prevLevel0recordId+ " " +(pointer!=null?pointer:"") + " " + currentPrefix +" " + theRest);
		
		if(currentRecord!=null) //not in the head
			//System.out.println(currentRecord); //print previous record
		
		if("TRLR".equals(currentPrefix)){
			currentRecord=null;
			return;
		}

		if(currentLevel==0){
			
			currentRecord=new Node(recordId);
			//System.out.println(currentPrefix);
			
			
			if(currentPrefix.equals("FAM")){
				famIds.put(recordId, currentRecord);
				
			}else if(currentPrefix.equals("INDI")){
				indiIds.put(recordId, currentRecord);
			}
			else{
				return; //not interested in other 0-level records
			}
				
		}
		else if(currentRecord!=null && theRest!=null){ //no need in null data
			
			//multiple values (1:M) which we will need
			if(currentPrefix.equals("FAM.CHIL") || 
					currentPrefix.equals("INDI.NAME") ||
					currentPrefix.equals("INDI.FAMC") ||
					currentPrefix.equals("INDI.FAMS")
					){
				
				ArrayList listCollected = (ArrayList)currentRecord.data.get(currentPrefix);
				if(listCollected==null){
					listCollected = new ArrayList();
					currentRecord.data.put(currentPrefix, listCollected);
				}
				listCollected.add(theRest);				
			}
			else if(currentPrefix.equals("INDI.BIRT.DATE")){
				currentRecord.data.put(currentPrefix, DateUtil.cleanGedcomDate(theRest));
			}
			else if(currentPrefix.equals("INDI.DEAT.DATE")){
				currentRecord.data.put(currentPrefix, DateUtil.cleanGedcomDate(theRest));
			}
			else if(currentPrefix.equals("INDI.FAMC.PEDI")){
				//take last FAMC and remove it - we are not interested in it:
				if(theRest.equalsIgnoreCase("adopted") || theRest.equalsIgnoreCase("foster") || theRest.equalsIgnoreCase("sealing")){
					ArrayList listCollected = (ArrayList)currentRecord.data.get("INDI.FAMC");
					listCollected.remove(listCollected.size()-1); //remove last
				}
			}
			else{
				currentRecord.data.put(currentPrefix, theRest);
			}
		}
	}
	
	
	public String toString(){
		StringBuffer sb = new StringBuffer();
		sb.append(indiIds);
		sb.append(", famIds="+famIds);
		return sb.toString();
	}
	
}
