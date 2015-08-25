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
import java.util.Map;


public class Statistics{

	public static java.text.DecimalFormat reportingFormat = new java.text.DecimalFormat("##0.00");

	
	public void print(Tree tree) throws Exception{
		
		boolean showTooYoungTooOld=Parameters.getAsBoolean(Constants.SHOW_ANOMALIES);
		boolean showCentures=Parameters.getAsBoolean(Constants.SHOW_CENTURIES);
	
		
		PrintWriter out = null;
		try{
			out = new PrintWriter(new BufferedWriter(new FileWriter(Constants.STAT_FILE, false)));
			
			int malesTotal=0; int femalesTotal=0; 
			int fatherDaughter_all=0; int motherDaughter_all=0;	int fatherSon_all=0; int motherSon_all=0;
			int fatherDaughter=0; int motherDaughter=0;	int fatherSon=0; int motherSon=0;
			
			ArrayList listWomanAges=new ArrayList();
			ArrayList listMenAges=new ArrayList();
			ArrayList listMotherDaughterAges=new ArrayList();
			ArrayList listFatherSonAges=new ArrayList();
			HashMap centuresFatherSon=null, centuresMotherDaughter=null, centuriesAll=null;
			if(showCentures){
				centuresFatherSon=new HashMap();
				centuresMotherDaughter = new HashMap();
				centuriesAll=new HashMap();
			}
			HashMap ages=null;
			if(showTooYoungTooOld){
				ages=new HashMap(); //age_in_years=>@id@
			}
			
			Iterator itr = tree.indiIds.keySet().iterator();
			while(itr.hasNext()){
				String id=(String)itr.next();
				Node node=(Node)tree.indiIds.get(id);
				
				String gender = Gedcom.getGender(node);
				if(gender==null)
					continue;
					
				boolean boy;
				if("M".equals(gender)){
					malesTotal++;
					boy=true;
				}
				else if("F".equals(gender)){
					femalesTotal++;
					boy=false;
				}
				else{
					throw new Exception("Undetermined gender");
				}
				
				//int total=malesTotal+femalesTotal;
				//if(total%1000==0)System.out.println(total+ " processed from "+tree.indiIds.size());
				
				Node fatherNode = Gedcom.getParent(tree, node, true);
				Node motherNode = Gedcom.getParent(tree, node, false);
				
				if(fatherNode!=null){
					if(boy)
						fatherSon_all++;
					else
						fatherDaughter_all++;
				}
				if(motherNode!=null){
					if(boy)
						motherSon_all++;
					else
						motherDaughter_all++;
				}
				
				//Calendar childDate=null;
				
				Node[] nodes = new Node[]{fatherNode, motherNode};
				for(int i=0; i<2; i++){
					Node parentNode=nodes[i];
					if(parentNode==null)
						continue;
					
					boolean isMan;
					if(i==0)
						isMan=true;
					else isMan=false;
					
					//here we are if parent exists
					
					int childYear=node.getYear();
					if(childYear==0)
						break; //no need to check parents anyway

					int parentYear=parentNode.getYear();
					if(parentYear==0)
						continue;
					
					//here we have both child and parent with valid dates, so we can compare
					int parentAge=childYear-parentYear;					
					
					if(showTooYoungTooOld){
						ArrayList ids=(ArrayList)ages.get(parentAge);
						if(ids==null){
							ids=new ArrayList();
							ages.put(parentAge, ids);
						}
						ids.add(node);
					}					
					
					//check that parent birth date is not later than child date
					if(parentAge<0){
						//System.out.println("Child DOB is earlier than parent DOB -- ignored: " + node.getId() +" "+ Gedcom.getName(node) + " " + birthDateChild+" vs "+parentNode.getId()+" "+Gedcom.getName(parentNode) + " " +birthDateParent);
						continue;
					}
					if(parentAge<=12){
						//System.out.println("Parent DOB is too close to parent DOB -- ignored: " + node.getId() +" "+ Gedcom.getName(node) + " " + birthDateChild+" vs "+parentNode.getId()+" "+Gedcom.getName(parentNode) + " " +birthDateParent);
						continue;
					}
					if(parentAge>90){
						//System.out.println("Parent DOB is too old ("+(childYear-parentYear)+" years old!) to be a parent/mother -- ignored: " + node.getId() +" "+ Gedcom.getName(node) + " " + birthDateChild+" vs "+parentNode.getId()+" "+Gedcom.getName(parentNode) + " " +birthDateParent);
						continue;
					}

					int century=DateUtil.year2Century(childYear);					
					if(isMan){
						if(boy){
							fatherSon++;
							listFatherSonAges.add(parentAge);

							if(showCentures){
								ArrayList dates=(ArrayList)centuresFatherSon.get(century);
								if(dates==null){
									dates=new ArrayList();
									centuresFatherSon.put(century, dates);
								}
								dates.add(parentAge);
							}

						}
						else
							fatherDaughter++;
						
						listMenAges.add(parentAge);
					}
					else{
						if(boy)
							motherSon++;
						else{
							motherDaughter++;
							listMotherDaughterAges.add(parentAge);
							
							if(showCentures){
								ArrayList dates=(ArrayList)centuresMotherDaughter.get(century);
								if(dates==null){
									dates=new ArrayList();
									centuresMotherDaughter.put(century, dates);
								}
								dates.add(parentAge);								
							}
						}
						listWomanAges.add(parentAge);
					}
					
					if(showCentures){
						ArrayList dates=(ArrayList)centuriesAll.get(century);
						if(dates==null){
							dates=new ArrayList();
							centuriesAll.put(century, dates);
						}
						dates.add(parentAge);
					}
					/*if(century==21){
						System.out.println(years);
					}*/
					

				}//for every parent
			
			}//for all nodes
			
			
			out.println("------------------------");
			out.println(" Statistics report");
			out.println("------------------------");
			out.println();
		    out.println("Total persons: " + (malesTotal+femalesTotal));
		    out.println("Males total: " + malesTotal);
		    out.println("Females total: " + femalesTotal);
		    out.println();
		    out.println("Pairs Father-Son total: " + fatherSon_all);
		    out.println("\tamong them with good dates (used in caluculations below): " + fatherSon);
		    out.println("Pairs Father-Daughter total: " + fatherDaughter_all);
		    out.println("\tamong them with good dates (used in caluculations below): " + fatherDaughter);
		    out.println("Pairs Mother-Son total: " + motherSon_all);
		    out.println("\tamong them with good dates (used in caluculations below): " + motherSon);
		    out.println("Pairs Mother-Daughter total: " + motherDaughter_all);
		    out.println("\tamong them with good dates (used in caluculations below): " + motherDaughter);			
			
		    Stat statMen=getStat(listMenAges);
		    Stat statWomen=getStat(listWomanAges);

			//concatenate 2 lists and sort (i.e. make common sorted list)
			ArrayList listAllAges=listMenAges;
			listMenAges=null; //gc
			listAllAges.addAll(listWomanAges);
			listWomanAges=null; //gc
			
			Stat statAll=getStat(listAllAges);
			Stat statMotherDoughter=getStat(listMotherDaughterAges);
			Stat statFatherSon=getStat(listFatherSonAges);
			
			
		    out.println();
            out.println();
		    out.println("*** Generational interval ***");            
		    out.println();
		    
		    printStat("Mother-child", statWomen, "\t", out);
		    printStat("Mother-daughter", statMotherDoughter, "\t", out);
		    printStat("Father-child", statMen, "\t", out);
		    printStat("Father-son", statFatherSon, "\t", out);
		    printStat("All", statAll, "\t", out);
		    
		    if(showCentures){
			    out.println();
			    out.println("\tAll, by centuries:");
			    ArrayList sortedCenturiesList=new ArrayList(centuriesAll.keySet());
			    Collections.sort(sortedCenturiesList);
				Iterator itrYears = sortedCenturiesList.iterator();
				while(itrYears.hasNext()){
					Integer centuryO=(Integer)itrYears.next();
					ArrayList list=(ArrayList)centuriesAll.get(centuryO);
					Stat centuryStat=getStat(list);
					printStat("\t"+centuryO.toString(), centuryStat, "\t\t", out);
				}
				
			    out.println();
			    out.println("\tFather-son, by centuries:");
			    sortedCenturiesList=new ArrayList(centuresFatherSon.keySet());
			    Collections.sort(sortedCenturiesList);
				itrYears = sortedCenturiesList.iterator();
				while(itrYears.hasNext()){
					Integer centuryO=(Integer)itrYears.next();
					ArrayList list=(ArrayList)centuresFatherSon.get(centuryO);
					Stat centuryStat=getStat(list);
					printStat("\t"+centuryO.toString(), centuryStat, "\t\t", out);
				}
				
			    out.println();
			    out.println("\tMother-daughter, by centuries:");
			    sortedCenturiesList=new ArrayList(centuresMotherDaughter.keySet());
			    Collections.sort(sortedCenturiesList);
				itrYears = sortedCenturiesList.iterator();
				while(itrYears.hasNext()){
					Integer centuryO=(Integer)itrYears.next();
					ArrayList list=(ArrayList)centuresMotherDaughter.get(centuryO);
					Stat centuryStat=getStat(list);
					printStat("\t"+centuryO.toString(), centuryStat, "\t\t", out);
				}
		    }//showCentures
		    
		    
			if(showTooYoungTooOld){
				Anomalies.printTooYoungTooOld(tree, ages);
			}
		}
		finally{
			if(out!=null)try{out.close();}catch(Exception ex){}
		}		
	}

	
	private void printStat(String title, Stat stat, String prefix, PrintWriter out)throws Exception{
	    out.println("\t"+title+": ");
	    out.print("\t"+prefix+"Arithmetic mean: " + reportingFormat.format(stat.mean));
	    out.print(", Median: " + reportingFormat.format(stat.median));
	    out.println(", Mode: " + stat.mode);
	    out.print("\t"+prefix);
	    	out.print("Absolute deviation: " + reportingFormat.format(stat.absDeviation));
	        out.print(", Variance: " + reportingFormat.format(stat.variance));
	        out.print(", RMSD: "+reportingFormat.format(Math.sqrt(stat.variance)));
	    	out.println(", Standard deviation (unbiased): " + reportingFormat.format(stat.standardDeviation));	        
	}
	
	
	private Stat getStat(ArrayList list){
	
		Stat stat=new Stat();
	    HashMap counters=new HashMap(); //key-rounded to int => Counter
	    
		Collections.sort(list);
		//System.out.println("men:" + listMenAges);
		int sizeArray=list.size();
		if(sizeArray>0){
			//median
			int index=sizeArray/2;
			if(sizeArray%2==0){ //even - take average of 2 central ones
				int first=(Integer)list.get(index-1);
				int second=(Integer)list.get(index);
				//System.out.println("pair median:" + first+" "+second);
				stat.median=(first+second)/(double)2;
			}
			else{ //odd - take central
				stat.median=(Integer)list.get(index);
			}
			//System.out.println("median_men="+median_men);
			
			//mean
			for(int i=list.size()-1;i>=0;i--){
				int y=(Integer)list.get(i);
				stat.mean+=y;
				addForMode(counters, y);
			}
			stat.mean/=sizeArray;
			
			//variance
			for(int i=list.size()-1;i>=0;i--){
				int y=(Integer)list.get(i);
				stat.variance+=Math.pow(stat.mean-y, 2);
				stat.absDeviation+=Math.abs(stat.mean-y);
			}
			stat.absDeviation/=sizeArray;
			
			double sumOfSquares=stat.variance; 
			stat.variance/=sizeArray;

			if(sizeArray>1)
				stat.standardDeviation=Math.sqrt(sumOfSquares/(sizeArray-1));
			
			//mode
			ArrayList modes = Statistics.findMostFrequent(counters);
			stat.mode=formatModes(modes);
			
			/////////////////////////////
			/*
			int numSigmas=3;
			double sigmasMinus = stat.mean-(numSigmas*stat.standardDeviation);
			double sigmasPlus = stat.mean+(numSigmas*stat.standardDeviation);
			
			int outside=0;
			int inside=0;
			for(int i=list.size()-1;i>=0;i--){
				int y=(Integer)list.get(i);
				if(y>=sigmasMinus && y<=sigmasPlus)
					inside++;
				else
					outside++;
			}
			System.out.println(inside+" " +outside+" "+ 100*(double)inside/(double)(inside+outside) + "%");
			*/
		
			/////////////////////////////
			
		}
		return stat;
	}
	
	
	public static void addForMode(HashMap counters, int d){
		Counter c = (Counter)counters.get(d);
		if(c==null){
			c=new Counter();
			counters.put(d, c);
		}
		c.n++;
	}
	
	
	public static ArrayList findMostFrequent(HashMap counter){
		Iterator itr = counter.keySet().iterator();
		int mostFrequent=0;
		int value=0;
		while(itr.hasNext()){
			Integer age=(Integer)itr.next();
			Counter count=(Counter)counter.get(age);
			if(count.n>mostFrequent){
				mostFrequent=count.n;
				value=count.n;
			}
		}
		//now collect with the same counter (multiple values)
		ArrayList result=new ArrayList(); //if equal counters		
		itr = counter.keySet().iterator();
		while(itr.hasNext()){
			Integer age=(Integer)itr.next();
			Counter count=(Counter)counter.get(age);
			if(count.n==value){
				result.add(age);
			}
		}
		return result;
	}
	

	private static String formatModes(ArrayList modes){
		if(modes.size()==1)
			return Integer.toString((Integer)modes.get(0));
		else
			return modes.toString();
	}
	
}
