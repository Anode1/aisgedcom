package org.is.ais.gedcom;

import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileReader;
import java.io.FileWriter;
import java.io.InputStreamReader;
import java.io.PrintWriter;
import java.util.ArrayList;
import java.util.Collections;
import java.util.HashMap;
import java.util.Iterator;

import org.is.ais.gedcom.tworelatives.CodeMap;

public class AnomaliesParser {

	
	public static void main(String[] args) throws Exception{

		PrintWriter out = null;
		BufferedReader reader = null;
		try{
			out = new PrintWriter(new BufferedWriter(new FileWriter("chart.txt", false)));			
			
			out.println("age,persons");
			
			File file = new File("C:\\anomalies.txt");
			if(!file.exists() || !file.canRead()){
				System.out.println("Cannot read file");
				System.out.println();
			}

			reader = new BufferedReader(new FileReader(file));
				
			//read header
			reader.readLine();
			reader.readLine();
			reader.readLine();
			
			int currentAge=Integer.MIN_VALUE;
			int currentNumber=0;
			
			String line=null;
			int lineCounter=3;

			while((line=reader.readLine())!=null){
					
				lineCounter++;
				line=line.trim();
				if("".equals(line)){
					continue;
				}
				
				if(line.startsWith("@")){
					if(currentAge==Integer.MIN_VALUE){
						System.out.println("Current age is undefined, line "+lineCounter);
						break;
					}
					currentNumber++;
				}
				else{
					if(currentAge!=Integer.MIN_VALUE){
						//write down previous age
						out.print(currentAge);
						out.print("\t");
						out.print(currentNumber);
						out.println();
						currentNumber=0;
					}
					
					//age
					currentAge = Integer.parseInt(line);
				}
			}//while
			
			//last number
			out.print(currentAge);
			out.print("\t");
			out.print(currentNumber);
			out.println();
		}
		finally{
			if(out!=null)try{out.close();}catch(Exception ex){}
			if(reader!=null)try{reader.close();}catch(Exception ex){}
		}		
	}
}
