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
import java.util.Calendar;
import java.util.Collections;
import java.util.Comparator;
import java.util.HashMap;
import java.util.Iterator;
import java.util.Map;


public class DescendingReport {


	public static void printDescendingLines(Tree tree, HashMap y, HashMap mito) throws Exception{
		
		PrintWriter out = null;
		try{
			out = new PrintWriter(new BufferedWriter(new FileWriter(Constants.DEFAULT_DESC_FILE, false)));			
			
			out.println("=================================================================");
			out.println("Male lineages (Y-DNA haplogroups)"); 
			out.println("=================================================================");
			
			printFamilyLines(out, tree, y, true);
			
			out.println();
			out.println();
			out.println("=================================================================");
			out.println("Female lineages (mito-DNA haplogroups)"); 
			out.println("=================================================================");
			
			printFamilyLines(out, tree, mito, false);
		}
		finally{
			if(out!=null)try{out.close();}catch(Exception ex){}
		}		
	}	


	private static void printFamilyLines(PrintWriter out, Tree tree, HashMap familyLines, boolean maleLine) throws Exception{
		
		ArrayList view = new ArrayList(familyLines.keySet());
		Collections.sort(view, new FamilyLinesComparator());
		
		boolean onlyLiving=Parameters.getInstance().getAsBoolean(Constants.LIVING_KEY);
		
		Iterator itr = view.iterator();
		int haplogroupCounter=0;
		while(itr.hasNext()){

			String familyLinePath=(String)itr.next();
			HashMap ids=(HashMap)familyLines.get(familyLinePath);
			
			//sort
			ArrayList view2 = new ArrayList();
			Iterator itr3=ids.entrySet().iterator();
			while(itr3.hasNext()){
				Map.Entry pairs = (Map.Entry)itr3.next();
				Node node=(Node)pairs.getValue();
				
				if(onlyLiving){
					if(Gedcom.isLiving(node)){//skip not living persons
						view2.add(node); 
					}	
				}
				else{
					view2.add(node); //all
				}

			}
			Collections.sort(view2, new Comparator() {
			      public int compare(Object o1, Object o2) {
			        int n1 = ((Node)o1).getDistance();
			        int n2 = ((Node)o2).getDistance();
			        return (n1<n2 ? -1 : (n1==n2 ? 0 : 1));
			      }
			});			
			
			boolean titlePrinted=false;

			//Iterator itr2 = ids.keySet().iterator();
			Iterator itr2=view2.iterator();
			while(itr2.hasNext()){
				
				if(!titlePrinted){
					printTitle(out, ++haplogroupCounter, familyLinePath);
					titlePrinted=true;
				}
					
				Node node = (Node)itr2.next();
				
				String id=node.getId();
				
				//out.print(Util.familyLineWithoutSubject(node.getPath()));
				out.print("\t"); //indent of each line

				out.print(Gedcom.getName(node));
				
				PrintUtil.printDates(node, out);
				
				//id for debugging
				out.print(" ");
				//out.print(directAncestorId);
				out.print(node.getDistance());

				//System.out.println(value+" "+names.get(0));

				out.println();
			}
		}//while
	}

	
	private static void printTitle(PrintWriter out, int haplogroupCounter, String familyLinePath){
		out.println();
		out.print("Haplogroup "+ haplogroupCounter + " (Lineage: ");
		out.print(Util.familyLineWithoutSubject(familyLinePath));
		out.println(")");
		out.println();
	}
	
	

}
