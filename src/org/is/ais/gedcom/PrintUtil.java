package org.is.ais.gedcom;

import java.io.PrintWriter;

public class PrintUtil {

	public static void printDates(Node node, PrintWriter out) throws Exception{
		
    	String birthDate = (String)node.data.get("INDI.BIRT.DATE");
    	String deathDate = (String)node.data.get("INDI.DEAT.DATE");
    	
    	if(birthDate!=null || deathDate!=null){
	    	out.print(" (");		    		
	    	if(birthDate!=null){
	    		out.print(birthDate);
	    	}
	    	else{
	    		out.print("?");
	    	}

			if(deathDate!=null){
				out.print("-");	

				if(deathDate!=null){
					out.print(deathDate);
				}
			}
	    	out.print(")");
    	}		
	}
	
}
