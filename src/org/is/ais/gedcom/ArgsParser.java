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

import java.util.Properties;

/**
 * Parser for arguments passed through command line.
 * 
 */
public class ArgsParser{
	
	
	public static boolean isWindows;	
	
    /**
     * Convenience utility function - for tests through API (to reuse the same
     * arguments as used in command-line version). This method is to be used
     * through API, and it is only simulating of POSIX argument, so throw 
     * exception if something wrong (it is not user input - as in Main!)
     */
	public static void setParameters(String[] args, Properties argsAsProps) throws Exception{
		if(new ArgsParser().setArgs(args, Parameters.getInstance()) != 0){
			throw new Exception("Something wrong with parameters"); 
		}
	}
	
	
	int setArgs(String[] args, Properties argsAsProps){

		/*  //if we require to pass something:
	    if(args==null || args.length==0){
	      printUsage();
	      return -1;
	    }
		 */
		String osName=System.getProperty("os.name").toLowerCase();
		if(osName.endsWith("nt") || osName.startsWith("window")){
			isWindows=true;
		}		
		

		for(int i = 0; i < args.length; i++){

			String arg = args[i];
			if(arg==null)continue;

			arg=arg.trim();

			//System.out.println("Processing of argument:"+arg);

			if(arg.startsWith("-")){//treat as an option

				if(arg.startsWith("-D")){ //if we'll want it in java way (no, we want POSIX!!!)
					if(treatAsAProperty(arg, i, args, argsAsProps, isWindows)==-1){
						return -1;
					}
				}
				else if (arg.equals("-h") || arg.equals("--help")){
					printUsage();
					return -1;
				}
				//else if (arg.equals("-t") || arg.equals("--trace")){
					//Logger rootLogger = logger.getRootLogger();
					//rootLogger.setLevel(Level.TRACE);
					//Main.trace=true;
				//}
				else if (arg.equals("-n") || arg.equals("--atreenoprefix")){
					argsAsProps.put(Constants.ATREE_NO_PREFIX_KEY, "true");
				}
				else if (arg.equals("--nonames")){
					argsAsProps.put(Constants.NO_NAMES_IN_RELATIONS, "true");
				}	
				else if (arg.equals("--living")){
					argsAsProps.put(Constants.LIVING_KEY, "true");
				}
				else if (arg.equals("-s") || arg.equals("--stat")){
					argsAsProps.put(Constants.GENERATE_STATISTICS, "true");
				}				
				else if (arg.equals("-a") || arg.equals("--anomalies")){
					argsAsProps.put(Constants.SHOW_ANOMALIES, "true");
				}
				else if (arg.equals("-c") || arg.equals("--centuries")){
					argsAsProps.put(Constants.SHOW_CENTURIES, "true");
				}
				else if (arg.equals("--atree2gedcom")){
					argsAsProps.put(Constants.ATREE2GEDCOM, "true");
				}
				else if (arg.equals("-f") || arg.equals("--file")){
					i++;
					if(i>=args.length){
						System.out.println("Option "+args[i-1]+" requires argument");
						System.out.println();
						return -1;
					}
					argsAsProps.put(Constants.INPUT_FILE_KEY, args[i]);
				}
				else if (arg.equals("-o") || arg.equals("--output")){
					i++;
					if(i>=args.length){
						System.out.println("Option "+args[i-1]+" requires argument");
						System.out.println();
						return -1;
					}
					argsAsProps.put(Constants.OUTUT_FILE_KEY, args[i]);
				}				
				else if (arg.equals("-i") || arg.equals("--id")){
					i++;
					if(i>=args.length){
						System.out.println("Option "+args[i-1]+" requires argument");
						System.out.println();
						return -1;
					}
					if(argsAsProps.getProperty(Constants.START_ID_KEY)!=null){ //first id has been filled already
						argsAsProps.put(Constants.SECOND_ID_KEY, args[i]);
					}
					else
						argsAsProps.put(Constants.START_ID_KEY, args[i]);
				}
				else if (arg.equals("-l") || arg.equals("--lang")){
					i++;
					if(i>=args.length){
						System.out.println("Option "+args[i-1]+" requires argument");
						System.out.println();
						return -1;
					}
					argsAsProps.put(Constants.LANG_KEY, args[i]);
				}
				else if (arg.equals("--transcode")){
					argsAsProps.put(Constants.TRANSCODE, "true");
				}
				else if (arg.equals("-e1")){
					i++;
					if(i>=args.length){
						System.out.println("Option "+args[i-1]+" requires argument");
						System.out.println();
						return -1;
					}
					argsAsProps.put(Constants.E1, args[i]);
				}
				else if (arg.equals("-e2")){
					i++;
					if(i>=args.length){
						System.out.println("Option "+args[i-1]+" requires argument");
						System.out.println();
						return -1;
					}
					argsAsProps.put(Constants.E2, args[i]);
				}
				else if (arg.equals("--translit")){
					argsAsProps.put(Constants.TO_TRANSLIT, "true");
				}	
				else{
					System.out.println("Argument: "+arg+" not supported");
					System.out.println();
					return -1;
				}
			}
			else{
				//first argument entered?
				if(argsAsProps.getProperty(Constants.INPUT_FILE_KEY)!=null){
					argsAsProps.put(Constants.START_ID_KEY, arg);
				}
				else
					argsAsProps.put(Constants.INPUT_FILE_KEY, arg); 
			}
		}//for args
		
		return 0;
	}//setArgs

	
	/**
	 * Try to parse an argument as an overriden property.
	 * This method is used by setArgs only and the last one is not used
	 */
	private int treatAsAProperty(String arg, int i, String[] args, 
			Properties argsAsProps, boolean dos){

		//System.out.println("treating arg:"+arg);

		if(dos){
			if(i+1>=args.length){
				System.out.println("property value cannot be null");
				printPropsUsage();
				return -1;
			}

			String key=arg.substring(2).trim();

			if("".equals(key)){
				System.out.println("property key cannot be null");
				printPropsUsage();
				return -1;
			}

			String value=args[i+1];
			if(value==null){
				System.out.println("property value cannot be null");
				printPropsUsage();
				return -1;
			}

			value=value.trim();
			argsAsProps.put(key,value);
			i++; //in dos '=' considered to be a delimiter
		}
		else{
			String keyAndValue=arg.substring(2).trim();

			if("".equals(keyAndValue)){
				//System.out.println("Error 1");
				printPropsUsage();
				return -1;
			}

			int indexOfEqualSign=keyAndValue.indexOf("=");
			if(indexOfEqualSign==-1){
				//System.out.println("Error 2");
				printPropsUsage();
				return -1;
			}
			String key=keyAndValue.substring(0,indexOfEqualSign).trim();
			String value=keyAndValue.substring(indexOfEqualSign+1).trim();
			argsAsProps.put(key,value);
			//System.out.println("Property added:"+key+"="+value);
		}
		return 0;
	}


	/**
	 * Print usage for properties. Not used now - when getOpt() is used
	 */
	private void printPropsUsage(){

		System.out.println("Usage for properties: -Dproperty=value");
	}  

	
	/**
	 * Print command-line usage help screen to stdout
	 */
	private void printUsage(){
		  
	   String progName="aisgedcom"; 
	   
	   System.out.println("");
	   System.out.println(" " + progName+" - GEDCOM Processing Tool "+Constants.releaseVersionString);
	   System.out.println(" Copyright (C) 2011 Vasili Gavrilov");
	   System.out.println(" This program comes with ABSOLUTELY NO WARRANTY;");
	   System.out.println(" This is free software, and you are welcome to redistribute it");
	   System.out.println(" under certain conditions; see <http://www.gnu.org/licenses/>.");
	   
	   System.out.println();
	   System.out.println("Usage: ");

	   System.out.println("     "+progName+" --file some.ged --id @3@");
	   System.out.println("                    Process a file using INDI ID (must exist in the file)");
	   System.out.println("");
	   System.out.println("     "+progName+" -f some.ged -i @3@");
	   System.out.println("                    The same as previous but shorter");
	   System.out.println("");
	   System.out.println("     "+progName+" 1.ged @3@");
	   System.out.println("                    The shortest way to pass a file");
	   System.out.println("");
	   System.out.println("     "+progName+" 1.ged -i @1@ -i @4@ --lang ru_Windows-1251");
	   System.out.println("                    Show all relations of two persons using");
	   System.out.println("                    ru_Windows-1251 for relations terms");
	   System.out.println("");
	   System.out.println("Options:");
	   System.out.println("");
	   System.out.println(" -h, --help         This help screen");
	   System.out.println(" -f, --file         Pass file (absolute or relative path)");
	   System.out.println("                    (files can also be passed as arguments without keys - see");
	   System.out.println("                    examples above)");
	   System.out.println(" -o, --output       Pass filename for output (currently works for one file only)");
	   System.out.println(" -i, --id           ID of the first INDI (must exist in the .ged file)");
	   System.out.println(" -s, --stat         Generate "+Constants.STAT_FILE);
	   System.out.println(" -n, --atreenoprefix    Remove first letter (Subject node) from the path");
	   System.out.println("                    (show lineages instead of persons)");
	   System.out.println("     --nonames      Do not show names in relations report");
	   System.out.println(" -a  --anomalies    Generate " + Constants.SHOW_ANOMALIES + " (shows ages of parents ");
	   System.out.println("                    on the moment of child birth)");
	   System.out.println(" -c  --centuries    Show additional generational intervals statistics (centures)");
	   System.out.println("     --living       Only show currently living persons (or those who does not "); 
	   System.out.println("                    have 2nd date) - in haplogoups");
	   System.out.println("                    if 2 are passed - print all relations of the 2 persons");
	   System.out.println(" -l, --lang         Language to use for the relations terminology");
	   System.out.println("                    Currently supported: en (default), ru (UTF8), ru_Windows-1251, ru-translit");
	   System.out.println("     --atree2gedcom Generate gedcom from atree file");
	   System.out.println("     --transcode    Transcode the file from encoding 1 to encoding 2. Encodings must be passed");
	   System.out.println("                    Example:");
	   System.out.println("                        "+progName+" --transcode -e1 windows-1251 -e2 UTF-8");
	   System.out.println("                    Supported encodings: windows-1251, UTF-8, KOI8-R, etc (see GUI drop-down)");
	   System.out.println("                    If --translit flag is provided then output is phonetic in english letters");
	   System.out.println("                    Example:    ");
	   System.out.println("                         "+progName+" --transcode -e1 windows-1251 -e2 translit -e3 ISO-8859-1"); 
	   System.out.println("");
	}

	
	public static void main(String[] args){

		try{

		//	new Config(); //configure from system.properties and log4j properties  

			if(new ArgsParser().setArgs(args, Parameters.getInstance()) != 0){
				return; //either we have an error in arguments or help asked to be printed
			}

			Parameters.getInstance().list(System.out);
		} 
		catch(Throwable e){
			e.printStackTrace();
		}
	}  

}

