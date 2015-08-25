/*
 	Copyright (C) 2009 Vasili Gavrilov

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
package org.is.ais.gedcom.gui;

import org.is.ais.gedcom.Parameters;

import java.io.File;

import org.apache.log4j.PropertyConfigurator;
import org.is.ais.gedcom.*;
import java.io.InputStream;
import java.util.Enumeration;
import java.util.Properties;

/**
 * Config used in GUI. It is left here - just in case - the command line version will
 * use property file similar to mplayer or other program (to have consolidated default user
 * arguments, been passed every run.    
 */
class Config extends Properties{

	private static String applicationRoot; 
	
	public Config()throws Exception{
		this(System.getProperty("user.dir"));
	}	

	
	/**
	 * Constructor to be used from servlet or jsp.
	 * Example:
	 * put in servlet's init:
	 * new Config(config.getServletContext().getRealPath("/") + File.separator + "WEB-INF");
	 */
	public Config(String appRoot)throws Exception{

		applicationRoot=appRoot;
    //read default config
		
		String pathToProps=getConfigDir() + File.separator + "system.properties";
        load(new java.io.BufferedInputStream(new java.io.FileInputStream(pathToProps)));
	
    //populate parameters (parameters are rewritable, config properties - are not):
	    Parameters params=Parameters.getInstance();		
	    for(Enumeration en = keys(); en.hasMoreElements() ;) {
	        String aKey=(String)en.nextElement();
	        String aValue=getProperty(aKey);
	        params.addProperty(aKey, aValue);
	    }
        
	//LOGS:
	    String path2log4jprop=getConfigDir() + File.separator + "log4j.properties";
	    
	    //System.out.println("Loading from "+path2log4jprop);
	    
	    PropertyConfigurator.configure(path2log4jprop);

	    //print something into log on startup:
	    /*
	    if(log.isDebugEnabled())log.debug("------------------------------");
	    if(applicationRoot!=null){
	      if(log.isDebugEnabled())log.debug("applicationRoot used:"+applicationRoot);
	    }
	    */
	    
	}

	
	public static String getApplicationRoot(){
		return applicationRoot;
	}
	
	
	public static String getConfigDir(){
		return applicationRoot + File.separator +"conf";
	}	
	
	
	/////////////////////////////////////////////////////////
	// Convenience methods (if we'll have significantly more than 3 - we'll make 
	// it generic
		  
	
	
	public static String getDefaultInputFile(){
		return applicationRoot + File.separator + Constants.DEFAULT_DEMO_GEDCOM_FILE;
	}
	
	
	public static String getDefaultId(){
		return Constants.DEFAULT_DEMO_GEDCOM_ID;
	}
	
	
	/*
	public static String getInputsDir(){
		String value = Parameters.getString(Constants.INPUT_FILE_KEY);
		if(value!=null)
			return value;
		
		return applicationRoot;
	}*/	

}
