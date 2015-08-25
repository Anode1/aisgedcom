package org.is.ais.gedcom;

public class Constants {

	//Do not change the following line: it's been substituted automatically from ant script (build.xml) and should be changed there
	public static final String releaseVersionString = "0.81";
	
	public static final String DEFAULT_ATREE_FILE =  "atree.txt";
	public static final String DEFAULT_DESC_FILE  =  "descending.txt";
	public static final String STAT_FILE          =  "statistics.txt";
	public static final String ANOMALIES_FILE     =  "anomalies.txt";
	public static final String TWO_RELATIVES_FILE =  "relations.txt";
	public static final String GEDCOM_FILE        =  "gedcom.ged";
	
	public static final String LANG_SUFFIX = ".lang"; //extension of relations terminology files
	public static final String LANG_PREFIX = "relations_";
	
	public static final String DEFAULT_DEMO_GEDCOM_FILE = "test1.ged";
	public static final String DEFAULT_DEMO_GEDCOM_ID = "@3@";
	
	public static final String DELIM=".";
	
	public static final int YEARS_THRESHOLD_FOR_LIKELY_LIVING=100;
	public static final int NUM_GENERATIONS_LIKELY_NOT_LIVING=4;
	
	public static final String INPUT_FILE_KEY="input";
	public static final String OUTUT_FILE_KEY="output";
	public static final String START_ID_KEY="startId"; //for atree
	public static final String SECOND_ID_KEY="secondId";  //second Id 
	public static final String ATREE_NO_PREFIX_KEY="noatreeprefix";
	public static final String LIVING_KEY="living";
	public static final String LANG_KEY="lang";
	public static final String SHOW_ANOMALIES="anomalies";
	public static final String SHOW_CENTURIES="centures";
	public static final String GENERATE_STATISTICS="stat";
	public static final String GENERATE_ATREE="atree";
	public static final String NO_NAMES_IN_RELATIONS="nonamesinrelations";
	public static final String ATREE2GEDCOM="atree2gedcom";
	public static final String TRANSCODE="transcode";
	public static final String E1="e1";
	public static final String E2="e2";
	public static final String TO_TRANSLIT="to_translit";
}
