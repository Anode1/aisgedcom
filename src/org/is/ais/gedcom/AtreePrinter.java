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

import java.io.BufferedWriter;

import java.io.FileWriter;
import java.io.PrintWriter;
import java.util.ArrayList;
import java.util.Collections;
import java.util.HashMap;
import java.util.Iterator;


public class AtreePrinter {

	
	public static void printAtree(Tree tree, HashMap directAncestors) throws Exception{
	
		boolean noPathPrefix=Parameters.getAsBoolean(Constants.ATREE_NO_PREFIX_KEY);
		String outputFile=Parameters.getString(Constants.OUTUT_FILE_KEY);
		if(outputFile==null){
			outputFile=Constants.DEFAULT_ATREE_FILE;
		}
		
		//print report
		PrintWriter out = null;
		try{
			out = new PrintWriter(new BufferedWriter(new FileWriter(outputFile, false)));			
			
			ArrayList view = new ArrayList(directAncestors.keySet());
			Collections.sort(view, new FamilyLinesComparator());
			
		    Iterator itr = view.iterator();
		    while(itr.hasNext()){
		    	String path=(String)itr.next();
		    	String id=(String)directAncestors.get(path);
		    	
		    	Node node = (Node)tree.indiIds.get(id);
		    	if(noPathPrefix){
		    		path=Util.familyLineWithoutSubject(path);
		    	}
		    	out.print(path);
		    	out.print(" ");
				out.print(Gedcom.getName(node));
				
				PrintUtil.printDates(node, out);

		    	//out.print(" ");
		    	//out.print(id);
		    	
		    	//System.out.println(value+" "+names.get(0));
		    	
		    	out.println();
		    }//while
			
		}
		finally{
			if(out!=null)try{out.close();}catch(Exception ex){}
		}		
	}

}
