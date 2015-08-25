package org.is.ais.gedcom.atree;

import java.io.BufferedWriter;
import java.io.FileOutputStream;
import java.io.FileWriter;
import java.io.OutputStreamWriter;
import java.io.PrintWriter;
import java.util.ArrayList;
import java.util.Date;
import java.util.Iterator;

import org.is.ais.gedcom.Constants;
import org.is.ais.gedcom.Parameters;
import org.is.ais.gedcom.Strings;
import org.is.ais.gedcom.tworelatives.CodeMap;

public class GedcomPrinter {
	
	
	private static java.text.SimpleDateFormat headerDateFormat = new java.text.SimpleDateFormat("dd MMM yyyy");	
	
	
	static void print(GedcomAtree gedcomAtree) throws Exception{ 
	
		String outputFile=Parameters.getString(Constants.OUTUT_FILE_KEY);
		if(outputFile==null){
			outputFile=Constants.GEDCOM_FILE;
		}
		
		PrintWriter out = null;
		try{
			//String lang=Parameters.getString(Constants.LANG_KEY);
			//if(Strings.isDefaultEncoding(lang)){
				out = new PrintWriter(new BufferedWriter(new FileWriter(outputFile, false)));
			//}
			//else{
			//	out = new PrintWriter(new BufferedWriter(new OutputStreamWriter(new FileOutputStream(outputFile),CodeMap.getCode())));
			//}
			printHeader(out);
			
			ArrayList indis=gedcomAtree.getIndis();
			
		    Iterator itr = indis.iterator();
		    while(itr.hasNext()){
		    	Data data=(Data)itr.next();
		    	printIndi(data, out);
		    }
			
			ArrayList fams=gedcomAtree.getFams();
			itr = fams.iterator();
		    while(itr.hasNext()){
		    	GedcomFam fam=(GedcomFam)itr.next();
		    	printFam(fam, out);
		    }
			
			//print trailer
			out.println("0 TRLR");
			
		}
		finally{
			if(out!=null)try{out.close();}catch(Exception ex){}
		}
	}
	
		
	private static void printHeader(PrintWriter out) throws Exception{
		out.println("0 HEAD");
		out.println("1 SOUR AISGEDCOM");
		out.println("2 NAME AISGEDCOM");
		out.println("2 VERS "+Constants.releaseVersionString);
		out.println("2 CORP SOURCEPORTAL");
		out.println("2 ADDR http://sourceforge.net/projects/aisgedcom");
		out.println("1 CHAR ANSI");
		out.println("1 DATE "+ headerDateFormat.format(new Date()));
		out.println("1 GEDC");
		out.println("2 VERS 5.5.1");
		out.println("2 FORM Lineage-Linked");
	}
	
	
	private static void printIndi(Data data, PrintWriter out) throws Exception{
		
		out.print("0 ");
		out.print(data.indiId); //always set, not null
		out.println(" INDI");

		out.print("1 NAME ");
		if(data.name!=null){
			out.print(data.name);
		}
		out.println();
		
		out.print("1 SEX ");
		out.println(data.gender); //always set, not null
		
		if(data.bDate!=null || data.bPlace!=null){
			out.println("1 BIRT");
		}
		if(data.bDate!=null){
			out.print("2 DATE ");
			out.println(data.bDate);
		}
		if(data.bPlace!=null){
			out.print("2 PLAC ");
			out.println(data.bPlace);
		}
		
		if(data.dDate!=null){
			out.println("1 DEAT");
			out.print("2 DATE ");
			out.println(data.dDate);
		}
		
		if(data.famcId!=null){ //some upper nodes do not have families
			out.print("1 FAMC ");
			out.println(data.famcId);
		}
		
		if(data.famsId!=null){
			out.print("1 FAMS ");
			out.println(data.famsId);
		}
	}
	
	
	private static void printFam(GedcomFam fam, PrintWriter out) throws Exception{
		out.print("0 ");
		out.print(fam.getFamId());
		out.print(" FAM");
		out.println();
		
		String husbId=fam.getHusbId();
		if(husbId!=null){
			out.print("1 HUSB ");
			out.print(husbId);
			out.println();
		}

		String wifeId=fam.getWifeId();
		if(wifeId!=null){
			out.print("1 WIFE ");
			out.print(wifeId);
			out.println();
		}
		
		//child is always not null (otherwise we didn't create FAM)
		out.print("1 CHIL ");
		out.print(fam.getChildId());
		out.println();
	}
	
	
}
