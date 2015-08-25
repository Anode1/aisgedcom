package org.is.ais.gedcom;

import java.io.BufferedWriter;
import java.io.FileWriter;
import java.io.PrintWriter;
import java.util.ArrayList;
import java.util.Collections;
import java.util.HashMap;
import java.util.Iterator;

public class Anomalies {

	
	public static void printTooYoungTooOld(Tree tree, HashMap ages) throws Exception{

		PrintWriter out = null;
		try{
			out = new PrintWriter(new BufferedWriter(new FileWriter(Constants.ANOMALIES_FILE, false)));			
			
			out.println("=================================================================");
			out.println("All persons sorted by age of the parent at the person's birth ");
			out.println("=================================================================");			
			
			ArrayList view = new ArrayList(ages.keySet());
			Collections.sort(view);			
			
		    Iterator itr = view.iterator();
		    while(itr.hasNext()){
		    	Integer age=(Integer)itr.next();
		    	ArrayList ids=(ArrayList)ages.get(age);
		    	out.println(age);
				Iterator itr2=ids.iterator();
				while(itr2.hasNext()){
					Node node=(Node)itr2.next();
					out.print("\t");
					out.print(node.getId());
					out.print(" ");
					out.print(Gedcom.getName(node));
					String birthDate = (String)node.data.get("INDI.BIRT.DATE");
					out.print(" (");
					if(birthDate!=null){
						out.print(birthDate);
					}
					else{
						out.print("?");
					}
					out.println(")");

		    	}

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
